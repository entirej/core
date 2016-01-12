/*******************************************************************************
 * Copyright 2013 Mojave Innovations GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJBlock;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJManagedFrameworkConnection;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.data.EJDataBlock;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.enumerations.EJAskToSaveChangesOperation;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.enumerations.EJMessageLevel;
import org.entirej.framework.core.enumerations.EJRecordType;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.internal.EJInternalEditableBlock;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreRelationJoinProperties;
import org.entirej.framework.core.properties.EJCoreRelationProperties;
import org.entirej.framework.core.renderers.eventhandlers.EJBlockFocusedListener;
import org.entirej.framework.core.renderers.interfaces.EJInsertScreenRenderer;
import org.entirej.framework.core.renderers.interfaces.EJQueryScreenRenderer;
import org.entirej.framework.core.renderers.interfaces.EJUpdateScreenRenderer;
import org.entirej.framework.core.renderers.registry.EJRendererFactory;
import org.entirej.framework.core.service.EJQueryCriteria;
import org.entirej.framework.core.service.EJRestrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJEditableBlockController extends EJBlockController implements Serializable
{
    final Logger                                                               logger                = LoggerFactory.getLogger(EJEditableBlockController.class);

    // The public representation of this BlockController
    private final EJInternalEditableBlock                                      _block;

    private final EJQuestionController                                         _questionController;
    private final EJManagedEditableBlockRendererController                     _blockRendererController;
    private EJEditableBlockController                                          _masterRelationBlockController;
    private final HashMap<EJCoreRelationProperties, EJEditableBlockController> _detailRelationControllerMap;
    private EJCoreRelationProperties                                           _masterRelationProperties;
    private final ArrayList<EJBlockFocusedListener>                            _blockFocusListeners  = new ArrayList<EJBlockFocusedListener>();

    /**
     * If this property is set to true then a query should be performed due to a
     * master record changing in a master detail relationship, but the deferred
     * query property within the relationship has been set to true
     */
    private boolean                                                            _performDeferredQuery = false;
    /**
     * Used in conjunction with the deferred query property. If automatic query
     * is set to true, then the query should be executed as soon as the block
     * gains focus
     */
    private boolean                                                            _automaticQuery       = true;

    // This variable is used to mark when a block is in the process of executing
    // a query. This is mainly used to ensure that detail block queries are not
    // executed twice when a block renderer informs the framework that a new
    // record instance occurred
    private boolean                                                            _executingBlockQuery  = false;

    /**
     * Creates a controller for the given data block
     * 
     * @param dataBlock
     *            the created controller will control this data block
     */
    public EJEditableBlockController(EJFormController formController, EJCoreBlockProperties blockProperties, EJDataBlock dataBlock)
    {
        super(formController, blockProperties, dataBlock);

        if (formController == null)
        {
            throw new EJApplicationException("The FormController passed to the BlockController constructor is null");
        }

        _detailRelationControllerMap = new HashMap<EJCoreRelationProperties, EJEditableBlockController>();

        _block = new EJInternalEditableBlock(this);

        _blockRendererController = new EJManagedEditableBlockRendererController(this);
        _blockRendererController.setRenderer(EJRendererFactory.getInstance().getBlockRenderer(this));
        _questionController = new EJQuestionController();
        initialiseItems();
    }

    @Override
    public EJInternalEditableBlock getBlock()
    {
        return _block;
    }

    public void initialiseScreenRenderers()
    {
        if (_blockRendererController == null)
        {
            return;
        }

        logger.trace("START initialiseScreenRenderers");

        EJQueryScreenRenderer queryScreenRenderer = _blockRendererController.getQueryScreenRenderer();
        if (queryScreenRenderer != null && getProperties().getScreenItemGroupContainer(EJScreenType.QUERY).count() > 0)
        {
            queryScreenRenderer.initialiseRenderer(this);
            setQueryScreenRenderer(queryScreenRenderer);
        }

        EJInsertScreenRenderer insertScreenRenderer = _blockRendererController.getInsertScreenRenderer();
        if (insertScreenRenderer != null && getProperties().getScreenItemGroupContainer(EJScreenType.INSERT).count() > 0)
        {
            insertScreenRenderer.initialiseRenderer(this);
            setInsertScreenRenderer(insertScreenRenderer);
        }

        EJUpdateScreenRenderer updateScreenRenderer = _blockRendererController.getUpdateScreenRenderer();
        if (updateScreenRenderer != null && getProperties().getScreenItemGroupContainer(EJScreenType.UPDATE).count() > 0)
        {
            updateScreenRenderer.initialiseRenderer(this);
            setUpdateScreenRenderer(updateScreenRenderer);
        }

        logger.trace("END initialiseScreenRenderers");
    }

    /**
     * This ensures that all changes made within the renderers are propagated to
     * the blocks data
     */
    @Override
    public void synchronizeFocusedRecord()
    {
        getManagedRendererController().synchronize();
        if (getMirrorBlockSynchronizer() != null)
        {
            getMirrorBlockSynchronizer().synchronize(this);
        }
    }

    /**
     * Returns the managed <code>EJManagedEditableBlockRendererController</code>
     * defined for the block or <code>null</code> if no renderer has been
     * assigned
     * 
     * @return The managed <code>IBlockRenderer</code> defined for the block or
     *         <code>null</code> if no renderer has been assigned
     */
    public EJManagedEditableBlockRendererController getManagedRendererController()
    {
        return _blockRendererController;
    }

    /**
     * Returns the <code>EJManagedEditableBlockRendererController</code> defined
     * for the block or <code>null</code> if no renderer has been assigned
     * 
     * @return The <code>IBlockRenderer</code> defined for the block or
     *         <code>null</code> if no renderer has been assigned
     */
    @Override
    public EJEditableBlockRendererController getRendererController()
    {
        if (_blockRendererController != null)
        {
            return _blockRendererController.getUnmanagedController();
        }
        return null;
    }

    @Override
    public void setRendererFocus(boolean focus)
    {
        focusGained();

        if (_blockRendererController != null)
        {
            _blockRendererController.setHasFocus(focus);
        }
    }

    /**
     * Indicates if this controller has current user focus
     * 
     * @return <code>true</code> if in focus otherwise <code>false</code>
     */
    public boolean hasFocus()
    {
        return _blockRendererController == null ? false : _blockRendererController.hasFocus();
    }

    /**
     * Used to indicate to all <code>{@link EJBlockFocusedListener}</code>s that
     * the this block has lost focus
     */
    public void focusLost()
    {
        for (EJBlockFocusedListener listener : _blockFocusListeners)
        {
            listener.blockFocusLost(this);
        }
    }

    /**
     * This method must be called by the block renderer when the block gains
     * focus
     * <p>
     * Any operations that should be performed upon block focus will be
     * executed. e.g. Executing of deferred queries
     */
    public void focusGained()
    {
        try
        {
            if (getFormController().getFocusedBlockController() == null || this != getFormController().getFocusedBlockController())
            {
                // Remove the focus from the currently focused block before
                // setting the focus to the newly focused one
                if (getFormController().getFocusedBlockController() != null)
                {
                    getFormController().getFocusedBlockController().setRendererFocus(false);
                }
                getFormController().setFocusedBlockController(this);
                getFormController().getManagedActionController().getUnmanagedController().newBlockInstance(getFormController().getEJForm(),
                        getProperties().getName());

                // Inform all listeners that this block has gained focus.
                for (EJBlockFocusedListener listener : _blockFocusListeners)
                {
                    listener.blockFocusGained(this);
                }
            }
            else
            {
                return;
            }

            if (_performDeferredQuery && _automaticQuery)
            {
                try
                {
                    executeQuery(getQueryCriteria());
                }
                catch (Exception e)
                {
                    getFormController().getFrameworkManager().handleException(e);
                }

            }
        }
        catch (Exception e)
        {
            getFrameworkManager().handleException(e);
        }

    }

    public boolean isBlockDisplayed()
    {
        if (getProperties().getCanvasName() == null)
        {
            return false;
        }
        return true;
    }

    /**
     * Indicates that the user wants to navigate to the next record
     */
    @Override
    public void nextRecord()
    {
        if (_blockRendererController != null)
        {
            if (areChildRelationsDirty())
            {
                EJInternalQuestion q = _questionController.makeAskToSaveChangesQuestion(_block, EJAskToSaveChangesOperation.QUESTION_ACTION_NEXT_RECORD);
                getFormController().getMessenger().askInternalQuestion(q);
            }
            else
            {
                if (_blockRendererController.getFocusedRecord() != null)
                {
                    EJDataRecord record = _blockRendererController.getRecordAfter(_blockRendererController.getFocusedRecord());
                    if (record != null)
                    {
                        getManagedRendererController().recordSelected(record);
                        newRecordInstance(record);
                    }
                }
            }
        }
    }

    /**
     * Indicates that the user wants to navigate to the previous record
     */
    @Override
    public void previousRecord()
    {
        if (_blockRendererController != null)
        {
            if (areChildRelationsDirty())
            {
                EJInternalQuestion q = _questionController.makeAskToSaveChangesQuestion(_block, EJAskToSaveChangesOperation.QUESTION_ACTION_PREVIOUS_RECORD);
                getFormController().getMessenger().askInternalQuestion(q);
            }
            else
            {
                if (_blockRendererController.getFocusedRecord() != null)
                {
                    EJDataRecord record = _blockRendererController.getRecordBefore(_blockRendererController.getFocusedRecord());
                    if (record != null)
                    {
                        _blockRendererController.recordSelected(record);
                        newRecordInstance(record);
                    }
                }
            }
        }
    }

    public void navigateToRecord(EJDataRecord record)
    {
        logger.trace("START navigateToRecord {}", record);
        if (getDataBlock().containsRecord(record))
        {
            if (getRendererController() != null)
            {
                getRendererController().recordSelected(record);
                newRecordInstance(record);
            }
        }
        logger.trace("END navigateToRecord");
    }

    /**
     * Informs the blocks renderer that the user wishes to perform an insert
     * operation
     */
    public void enterInsert(boolean copyCurrentRecord)
    {
        if (getProperties().isInsertAllowed())
        {
            if (_blockRendererController != null)
            {
                EJDataRecord record = null;
                if (copyCurrentRecord)
                {
                    if (getFocusedRecord() != null)
                    {
                        record = _block.copyFocusedRecord();

                    }
                    else
                    {
                        record = createRecord(EJRecordType.INSERT, true);
                    }
                }
                else
                {
                    record = createRecord(EJRecordType.INSERT, true);
                }

                getFormController().getUnmanagedActionController().preOpenScreen(new EJBlock(_block), new EJRecord(record), EJScreenType.INSERT);
                _blockRendererController.enterInsert(record);
            }
        }
    }

    public void updateCancelled()
    {
        getFormController().getUnmanagedActionController().whenUpdateCancelled(new EJBlock(_block));
    }

    public void insertCancelled()
    {
        getFormController().getUnmanagedActionController().whenInsertCancelled(new EJBlock(_block));
    }

    public void executeInsert(EJDataRecord insertedRecord)
    {
        if (areChildRelationsDirty())
        {
            EJInternalQuestion q = _questionController.makeAskToSaveChangesQuestion(_block, EJAskToSaveChangesOperation.QUESTION_ACTION_INSERT_RECORD,
                    insertedRecord);
            getFormController().getMessenger().askInternalQuestion(q);
        }
        else
        {
            addMasterRelationValues(insertedRecord);

            // Ensure that the new record is valid
            getFormController().getUnmanagedActionController().validateRecord(getFormController().getEJForm(), new EJRecord(insertedRecord),
                    EJRecordType.INSERT);
            getFormController().getUnmanagedActionController().preInsert(getFormController().getEJForm(), new EJRecord(insertedRecord));

            getDataBlock().recordCreated(insertedRecord, getFocusedRecord());

            if (_blockRendererController != null)
            {
                _blockRendererController.recordInserted(insertedRecord);
            }
            if (getMirrorBlockSynchronizer() != null)
            {
                getMirrorBlockSynchronizer().recordInserted(this, insertedRecord);
            }

            newRecordInstance(insertedRecord);

            // Tell the application developer that a record has been inserted.
            // Ensure that the block has the record already, otherwise the
            // developer might try to get the record and it will not be there
            getFormController().getUnmanagedActionController().postInsert(getFormController().getEJForm(), new EJRecord(insertedRecord));
        }
    }

    /**
     * Informs the blocks renderer that the user wishes to update the current
     * record
     */
    public void enterUpdate()
    {
        if (getProperties().isUpdateAllowed())
        {
            if (_blockRendererController != null)
            {
                try
                {
                    EJDataRecord recordToUpdate = createRecordNoAction();
                    getFocusedRecord().copyValuesToRecord(recordToUpdate);

                    recordToUpdate.setBaseRecord(getFocusedRecord());
                    getFormController().getUnmanagedActionController().initialiseRecord(getFormController().getEJForm(), new EJRecord(recordToUpdate),
                            EJRecordType.UPDATE);
                    getFormController().getUnmanagedActionController().preOpenScreen(new EJBlock(_block), new EJRecord(recordToUpdate), EJScreenType.UPDATE);
                    _blockRendererController.enterUpdate(recordToUpdate);

                }
                catch (Exception e)
                {
                    getFormController().getFrameworkManager().handleException(e);
                }
            }
        }
    }

    /**
     * Informs the controller that the given record has been updated
     * <p>
     * Modified records will be added to the blocks dirty records and passed to
     * the form data access processor when the user wishes to save changes
     * <p>
     * If the record is not part of the blocks list of records, then an
     * exception is thrown
     * 
     * @param updatedRecord
     *            The updated record
     * @throws IllegalArgumentException
     *             if the record does not already exist within the underlying
     *             blocks list of records
     * @see {@link EJEditableBlockController#performUpdateOperation()}
     */
    public void executeUpdate(EJDataRecord updatedRecord)
    {
        logger.trace("START ExecuteUpdate");

        if (updatedRecord == null)
        {
            throw new EJApplicationException("The record passed to recordUpdated is null.");
        }

        if ((updatedRecord.getBaseRecord() != null && getDataBlock().containsRecord(updatedRecord.getBaseRecord()))
                || getDataBlock().containsRecord(updatedRecord))
        {
            // Record validation should only be made if the update is being
            // carried out by the framework.
            // If the framework is doing the update then a base record will
            // always be set. If the developer is making the update, no base
            // record will have been set
            if (updatedRecord.getBaseRecord() != null)
            {
                getFormController().getUnmanagedActionController().validateRecord(getFormController().getEJForm(), new EJRecord(updatedRecord),
                        EJRecordType.UPDATE);
            }

            getFormController().getUnmanagedActionController().preUpdate(getFormController().getEJForm(), new EJRecord(updatedRecord));

            // By setting the focused block controller to null, forces a
            // newRecordInstance method to be called after the query
            // operation has completed and focus returns to the block
            getFormController().setFocusedBlockController(null);

            EJDataRecord recordToUpdate = updatedRecord.getBaseRecord();

            if (recordToUpdate == null)
            {
                recordToUpdate = updatedRecord;
            }
            else
            {
                updatedRecord.copyValuesToRecord(recordToUpdate);
            }

            getDataBlock().recordUpdated(recordToUpdate);

            getFormController().getUnmanagedActionController().postUpdate(getFormController().getEJForm(), new EJRecord(updatedRecord));
            getFormController().getUnmanagedActionController().postChange(this, updatedRecord, EJScreenType.MAIN);

            // If the framework is making the update, navigate to the
            // updated record.
            if (updatedRecord.getBaseRecord() != null)
            {
                if (_blockRendererController != null)
                {
                    _blockRendererController.recordSelected(updatedRecord.getBaseRecord());
                }
            }
        }
        else
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_RECORD_PASSED_TO_PERFORM_UPDATE));
        }

        logger.trace("END ExecuteUpdate");
    }

    @Override
    public void refreshAfterChange(EJDataRecord record)
    {
        if (_blockRendererController != null)
        {
            _blockRendererController.refreshAfterChange(record);
        }
        if (getMirrorBlockSynchronizer() != null)
        {
            getMirrorBlockSynchronizer().refreshAfterChange(this, record);
        }

    }

    /**
     * Informs the block renderer that the current record should be deleted
     * 
     * @param message
     *            The message to display, or <code>null</code> if the default
     *            message should be used
     */
    public void askToDeleteCurrentRecord(String message)
    {
        if (getProperties().isDeleteAllowed())
        {
            if (hasChildRelationsWithData())
            {
                getFormController().getMessenger()
                        .handleMessage(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.CANNOT_DELETE_WHILE_CHILDREN_EXIST));
            }
            else
            {
                if (_blockRendererController != null)
                {
                    _blockRendererController.askToDeleteRecord(getFocusedRecord(), message);
                }
            }
        }
    }

    /**
     * Deletes the given record
     * <p>
     * If the record exists within the blocks list of records then it will be
     * removed and added to the list of dirty records. If the given record has
     * just been created i.e. has its new flag set to <code>true</code> then the
     * record will be removed from the data block but not added to the blocks
     * list of dirty records
     * 
     * @param record
     *            The record to be deleted
     */
    public void executeDelete(EJDataRecord deletedRecord)
    {
        if (hasChildRelationsWithData())
        {
            EJMessage userMessage = null;
            EJCoreRelationProperties relation = getFirstRelationWithValues();

            if (relation != null)
            {
                userMessage = getFormController().getUnmanagedActionController().getMasterDetailDeleteViolationMessage(getFormController().getEJForm(),
                        relation.getName());
            }

            // Only display the user message if one was specified
            if (userMessage != null)
            {
                getFormController().getMessenger().handleMessage(userMessage);
            }
            else
            {
                getFormController().getMessenger()
                        .handleMessage(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.CANNOT_DELETE_WHILE_CHILDREN_EXIST));
            }
        }
        else
        {
            if (deletedRecord == null)
            {
                EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_RECORD_PASSED_TO_METHOD,
                        "BlockController.performDeleteOperation");
                getFormController().getMessenger().handleMessage(message);
            }

            if (!getDataBlock().containsRecord(deletedRecord))
            {
                EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_RECORD_PASSED_TO_PERFORM_DELETE);
                getFormController().getMessenger().handleMessage(message);
            }

            getFormController().getUnmanagedActionController().validateRecord(getFormController().getEJForm(), new EJRecord(deletedRecord),
                    EJRecordType.DELETE);
            getFormController().getUnmanagedActionController().preDelete(getFormController().getEJForm(), new EJRecord(deletedRecord));

            // determine the next focused record
            EJDataRecord nextFocusedRecord = getRecordAfter(deletedRecord);
            if (nextFocusedRecord == null)
            {
                nextFocusedRecord = getRecordBefore(deletedRecord);
            }

            // Now do the delete. This will remove the record from the
            // blocks list of records and add it to the dirty list

            int deletedRecordNumber = getDataBlock().getRecordNumber(deletedRecord);
            // remove the record from the data block
            getDataBlock().recordDeleted(deletedRecord);

            // Inform the application developer that a record has been
            // removed from the data block
            if (_blockRendererController != null)
            {
                _blockRendererController.recordDeleted(deletedRecordNumber);
            }

            if (getMirrorBlockSynchronizer() != null)
            {
                getMirrorBlockSynchronizer().recordDeleted(this, deletedRecordNumber);
            }

            // move to next record
            if (_blockRendererController != null && nextFocusedRecord != null)
            {
                _blockRendererController.recordSelected(nextFocusedRecord);
            }
            else
            {
                clearBlock(false);
            }

            if (nextFocusedRecord != null)
            {
                newRecordInstance(nextFocusedRecord);
            }
            getFormController().getUnmanagedActionController().postDelete(getFormController().getEJForm(), new EJRecord(deletedRecord));

        }
    }

    /**
     * Informs the blocks renderer that the user wishes to enter a query on this
     * block
     */
    @Override
    public void enterQuery()
    {
        if (getBlockService() == null)
        {
            getFormController().getMessenger().handleMessage(new EJMessage(EJMessageLevel.MESSAGE,
                    "Cannot perform query operation when no data service has been defined. Block: " + getProperties().getName()));
            return;
        }

        if (_blockRendererController != null && (!preventMasterlessOperations()))
        {
            if (_block.getQueryScreenRenderer() != null)
            {
                EJDataRecord record = _block.getQueryScreenRenderer().getQueryRecord();
                if (record == null)
                {
                    record = _block.createRecordNoAction();
                }

                getFormController().getUnmanagedActionController().initialiseRecord(getFormController().getEJForm(), new EJRecord(record), EJRecordType.QUERY);
                getFormController().getUnmanagedActionController().preOpenScreen(new EJBlock(_block), new EJRecord(record), EJScreenType.QUERY);
                _blockRendererController.enterQuery(record);
            }
        }
    }

    /**
     * Executes a query on this controllers underlying block. If this record is
     * a detail in a master-detail relationship then the relation items will be
     * copied from the master record before a query will be made. If the
     * underlying block is a master in a master-detail relationship then all
     * detail blocks where this block is the master will be issued an execute
     * query command.
     * <p>
     * The query will only be executed if the
     * <code>preventMasterlessOperations</code> is <code>false</code>. This flag
     * indicates that this controller is a detail of a master-detail
     * relationship and the master contains no records. As long as the parent
     * has no records, then the detail should not be able to perform any
     * actions.
     * <p>
     * This controllers renderer will be informed after the query operation has
     * been performed
     * <p>
     * If the block has changes then they will be cleared and the block reset.
     * The changes will be ignored. If the changes should be committed before a
     * new query is executed then the form renderer should check for open
     * changes before allowing the user to execute a new query.
     * 
     * @param queryCriteria
     *            The query criteria for use within the block query
     */
    @Override
    public void executeQuery(EJQueryCriteria queryCriteria)
    {
        if (isBlockDirty() || areChildRelationsDirty())
        {
            EJInternalQuestion q = _questionController.makeAskToSaveChangesQuestion(_block, EJAskToSaveChangesOperation.QUESTION_ACTION_QUERY_PERFORM,
                    queryCriteria);
            getFormController().getMessenger().askInternalQuestion(q);
        }
        else
        {
            if (getBlockService() == null)
            {
                getFormController().getMessenger().handleMessage(new EJMessage(EJMessageLevel.MESSAGE,
                        "Cannot perform query operation when no data service has been defined. Block: " + getProperties().getName()));
            }

            if (preventMasterlessOperations())
            {
                return;
            }

            if (queryCriteria == null)
            {
                setQueryCriteria(new EJQueryCriteria(new EJBlock(_block)));
            }
            else
            {
                setQueryCriteria(queryCriteria);
            }

            addMasterRelationValues(getQueryCriteria());

            EJManagedFrameworkConnection connection = getFrameworkManager().getConnection();

            try
            {
                _executingBlockQuery = true;

                getFormController().getUnmanagedActionController().validateQueryCriteria(getFormController().getEJForm(), getQueryCriteria());
                getFormController().getUnmanagedActionController().preQuery(getFormController().getEJForm(), getQueryCriteria());

                // After the validation is ok, we can inform the renderer that a
                // query will now be executed
                if (_blockRendererController != null)
                {
                    _blockRendererController.executingQuery();
                }

                setHasMorePages(true);

                // By setting the focused block controller to null, forces a
                // newRecordInstance method to be called after the query
                // operation has completed and focus returns to the block
                // getFormController().setFocusedBlockController(null);

                // Clear the block so that it is ready for the newly queried
                // records
                clearBlock(true);
                if (canQueryInPages() && (!getProperties().queryAllRows()))
                {
                    setQueryAllRows(false);
                    setPageNumber(0);
                    setPageSize(getProperties().getPageSize());
                    // pass false so that the Renderer is not informed about the
                    // nextPage operation. They will be informed using the
                    // queryExecuted method
                    nextPage(false);
                }
                else
                {
                    // Set the query all rows to true, because the service
                    // cannot retrieve data in pages
                    setQueryAllRows(true);
                    setMaxResults(getProperties().getMaxResults());
                    setPageNumber(1);
                    setPageSize(-1);
                    // pass false so that the Renderer is not informed about the
                    // nextPage operation. They will be informed using the
                    // queryExecuted method
                    getPage(false);
                }

                // Query of the detail relations is handled within the
                // getPage(). The get page is call from the above code and from
                // within the nextPage() method

                // now inform the renderer that the query is finished. The
                // renderer will need to refresh itself to display the new
                // records
//                if (_blockRendererController != null)
//                {
//                    _blockRendererController.queryExecuted();
//                }

                if (getMirrorBlockSynchronizer() != null)
                {
                    getMirrorBlockSynchronizer().queryExecuted(this);
                }

                // Reset these flags before calling the postBlockQuery as this
                // should be called after everything is completed
                _executingBlockQuery = false;
                _performDeferredQuery = false;

                getFormController().getUnmanagedActionController().postBlockQuery(getFormController().getEJForm(), new EJBlock(_block));
            }
            finally
            {
                connection.close();
                _executingBlockQuery = false;
                _performDeferredQuery = false;
            }
        }
    }

    /**
     * Indicates that a query should be performed using the master relation
     * properties if they exist
     * <p>
     * The query will either be performed automatically or, manually depending
     * on the relation properties
     * 
     * @param queryRecord
     *            A record containing the query criteria
     */
    private void executeQueryUsingMasterRelation(EJQueryCriteria queryCriteria)
    {
        if (_masterRelationProperties == null)
        {
            _performDeferredQuery = false;
            executeQuery(queryCriteria);
            focusGained();
        }
        else
        {
            if (_masterRelationProperties.isDeferredQuery())
            {
                setQueryCriteria(queryCriteria);
                _performDeferredQuery = true;
                _automaticQuery = _masterRelationProperties.isAutoQuery();
            }
            else
            {
                _performDeferredQuery = false;
                executeQuery(queryCriteria);
            }
        }
    }

    public void insertRecord(EJDataRecord insertRecord)
    {
        getBlock().insertRecord(insertRecord);
    }

    /**
     * Used to insert the blocks dirty records
     */
    @SuppressWarnings("unchecked")
    public void insertDirtyRecords()
    {
        if (getProperties().isControlBlock() || getBlockService() == null || getProperties().isMirrorChild())
        {
            // If no access processor has been defined or the block is a
            // control block then EntireJ cannot save changes to the block
            return;
        }

        // Insert records
        // I cannot use type a cast list as I can't create a list of '?' The
        // Type Safe lists are only checked at compile time anyway
        @SuppressWarnings("rawtypes")
        List entityList = new ArrayList();
        for (EJDataRecord record : getInsertedRecords())
        {
            entityList.add(record.getServicePojo());
        }

        // Instruct the access processor to insert the record.
        if (entityList.size() > 0)
        {
            getBlockService().executeInsert(new EJForm(_block.getForm()), entityList);
        }
    }

    /**
     * Used to update the blocks dirty records
     */
    @SuppressWarnings("unchecked")
    public void updateDirtyRecords()
    {
        if (getProperties().isControlBlock() || getBlockService() == null || getProperties().isMirrorChild())
        {
            // If no access processor has been defined or the block is a
            // control block then EntireJ cannot save changes to the block
            return;
        }

        @SuppressWarnings("rawtypes")
        List entityList = new ArrayList();
        for (EJDataRecord record : getUpdatedRecords())
        {
            entityList.add(record.getServicePojo());
        }

        // Instruct the access processor to update the records
        if (entityList.size() > 0)
        {
            getBlockService().executeUpdate(new EJForm(_block.getForm()), entityList);
        }
    }

    public void deleteRecord(EJDataRecord record)
    {
        getBlock().deleteRecord(record);
    }

    /**
     * Used to delete the blocks dirty records
     */
    @SuppressWarnings("unchecked")
    public void deleteDirtyRecords()
    {
        if (getProperties().isControlBlock() || getBlockService() == null || getProperties().isMirrorChild())
        {
            // If no access processor has been defined or the block is a
            // control block then EntireJ cannot save changes to the block
            return;
        }

        // Delete records
        @SuppressWarnings("rawtypes")
        List entityList = new ArrayList();
        for (EJDataRecord record : getDeletedRecords())
        {
            entityList.add(record.getServicePojo());
        }

        // Instruct the access processor to delete the records
        if (entityList.size() > 0)
        {
            getBlockService().executeDelete(new EJForm(_block.getForm()), entityList);
        }
    }

    /**
     * Called by the block renderer whenever a new record is chosen by the user
     * <p>
     * The record passed is the actual record that currently has user focus. If
     * this block is a master in a master-detail relationship, then all detail
     * blocks will be queried
     * 
     * @param selectedRecord
     *            The newly selected record
     */
    @Override
    public void newRecordInstance(EJDataRecord selectedRecord)
    {

        EJManagedFrameworkConnection connection = getFrameworkManager().getConnection();

        try
        {
            // This method can be ignored if the block is currently being
            // queried. This method can be called by block renderers when the
            // blocks data is populated. If the block is being queried, then
            // detail relationships are handled within the executeQuery() method
            // and should not be handled here

            if (_executingBlockQuery)
            {
                // do nothing
                return;
            }

            // Ensure all detail changes are saved before navigating to a new
            // master and thus retrieving new detail records
            if (areChildRelationsDirty())
            {
                EJInternalQuestion q = _questionController.makeAskToSaveChangesQuestion(_block, EJAskToSaveChangesOperation.QUESTION_ACTION_NEW_RECORD_INSTANCE,
                        selectedRecord);
                getFormController().getMessenger().askInternalQuestion(q);
            }
            else
            {
                if (selectedRecord != null)
                {
                    getFormController().getUnmanagedActionController().newRecordInstance(getFormController().getEJForm(), new EJRecord(selectedRecord));
                    // If there are detail relationships, then query them
                    if (getDetailRelationControllers().size() > 0)
                    {
                        executeQueryOnAllDetailRelations(true);
                    }

                    if (getMirrorBlockSynchronizer() != null)
                    {
                        getMirrorBlockSynchronizer().newRecordSelected(this, selectedRecord);
                    }
                }
                super.newRecordInstance(selectedRecord);
            }
        }
        catch (Exception e)
        {
            _block.getFrameworkManager().handleException(e);
        }
        finally
        {
            connection.close();
        }
    }

    /**
     * Sets the master block controller and relation properties if the
     * underlying block of this controller is a detail on a master-detail
     * relationship. A block can only be a detail from one master block
     * 
     * @param masterBlock
     *            The master block controller for this controller
     * @param relationProperties
     *            The relation properties of this master-detail relationship
     */
    public void setMasterRelationDetails(EJCoreRelationProperties relationProperties, EJEditableBlockController masterBlock)
    {
        _masterRelationBlockController = masterBlock;
        _masterRelationProperties = relationProperties;
    }

    /**
     * Returns the master block controller of this controller if this controller
     * is part of a master-detail relationship
     * <p>
     * If this controller is not part of a master-detail relationship then there
     * will be no master block controller and <code>null</code> will be returned
     * 
     * @return The data block controller of the master block if this controller
     *         is the detail in a master-detail relationship
     */
    public EJEditableBlockController getMasterRelationBlockController()
    {
        return _masterRelationBlockController;
    }

    /**
     * Returns the relation properties for the relation between this controller
     * and its master block if this block belongs to a master detail
     * relationship
     * 
     * @return The relation properties
     */
    public EJCoreRelationProperties getMasterRelationProperties()
    {
        return _masterRelationProperties;
    }

    /**
     * Adds a detail block controller to this controller
     * <p>
     * This controller is then the master block within the master-detail
     * relationship
     * <p>
     * More than one detail can exist for any given master
     * 
     * @param relationProperties
     *            The relation
     * @param detailController
     *            The detail block controller
     */
    public void addDetailRelationBlockController(EJCoreRelationProperties relationProperties, EJEditableBlockController detailController)
    {
        if (detailController != null)
        {
            getDetailRelationControllers().put(relationProperties, detailController);
        }
    }

    /**
     * Ensures that all detail blocks from a master-detail relationship, where
     * the current block is the master, are queried. This should be called each
     * time the master record is changed, for example after record navigation.
     * 
     * @param clearChanges
     *            Indicates if the dirty records contained within the detail
     *            blocks should be cleared before executing the query
     * 
     */
    @Override
    void executeQueryOnAllDetailRelations(boolean clearChanges)
    {
        if (getBlockRecordCount() > 0)
        {
            Iterator<EJEditableBlockController> detailBlocks = getDetailRelationControllers().values().iterator();
            while (detailBlocks.hasNext())
            {

                EJEditableBlockController detailBlock = detailBlocks.next();
                if (detailBlock != null)
                {
                    detailBlock.clearBlock(clearChanges);
                    if (detailBlock.masterRelationValuesExist())
                    {
                        EJQueryCriteria queryCriteria = new EJQueryCriteria(new EJBlock(detailBlock.getBlock()));
                        addMasterRelationValues(queryCriteria);

                        detailBlock.executeQueryUsingMasterRelation(queryCriteria);
                    }
                }
            }
        }
    }

    /**
     * Clears this controllers underlying data block
     * <p>
     * Only this controllers underlying data block will be cleared, if the block
     * is master in a master-detail relationship then its detail blocks will be
     * left untouched. To clear all detail blocks use the
     * {@link EJEditableBlockController#clearAllDetailRelations()}
     * 
     * @param clearChanges
     *            Indicates if the dirty records contained within this block
     *            should also be cleared
     * 
     * @see EJEditableBlockController#clearAllDetailRelations()
     */
    @Override
    public void clearBlock(boolean clearChanges)
    {
        clearAllDetailRelations(clearChanges);
        super.clearBlock(clearChanges);

        if (_blockRendererController != null)
        {
            _blockRendererController.blockCleared();
        }
        if (getMirrorBlockSynchronizer() != null)
        {
            getMirrorBlockSynchronizer().blockCleared(this);
        }
    }

    /**
     * Clears all detail blocks if this controllers underlying block is a master
     * in a master detail relationship
     * <p>
     * This blocks underlying data block will be left untouched. To clear this
     * block use the {@link EJEditableBlockController#clearBlock()}
     * 
     * @param clearChanges
     *            Indicates if the dirty records contained within the detail
     *            blocks should also be cleared
     * 
     * @see EJEditableBlockController#clearBlock()
     */
    public void clearAllDetailRelations(boolean clearChanges)
    {
        Iterator<EJEditableBlockController> detailBlocks = getDetailRelationControllers().values().iterator();
        while (detailBlocks.hasNext())
        {
            EJEditableBlockController detailBlock = detailBlocks.next();

            if (detailBlock != null)
            {
                detailBlock.clearBlock(clearChanges);
            }
        }
        if (_blockRendererController != null)
        {
            _blockRendererController.detailBlocksCleared();
        }
    }

    public boolean hasChildRelationsWithData()
    {
        for (EJEditableBlockController childBlock : getDetailRelationControllers().values())
        {
            if (childBlock.getBlockRecordCount() > 0)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the EJCoreRelationProperties of the first relation of this block
     * that has child records
     * <p>
     * If this block has no relations or none of the child relation values have
     * data, then this method will return <code>null</code>
     * 
     * @return The first relation that has child records or <code>null</code> if
     *         this block has no child relations or the child blocks have no
     *         data
     */
    public EJCoreRelationProperties getFirstRelationWithValues()
    {
        for (EJCoreRelationProperties relation : getDetailRelationControllers().keySet())
        {
            if (getDetailRelationControllers().get(relation).getBlockRecordCount() > 0)
            {
                return relation;
            }
        }

        return null;
    }

    /**
     * Checks to see if this controllers underlying data block has been
     * modified. The check will also be performed on all detail block in a
     * master detail relationship with this block
     * 
     * @return <code>true</code> if this controllers underlying data block has
     *         changes, otherwise <code>false</code>
     */
    public boolean areChildRelationsDirty()
    {
        // Check all detail blocks to see if they have changes
        // if they do, then this controller should report itself as
        // being dirty
        Iterator<EJEditableBlockController> detailBlocks = getDetailRelationControllers().values().iterator();
        while (detailBlocks.hasNext())
        {
            EJEditableBlockController detailBlock = detailBlocks.next();

            if (detailBlock.isBlockDirty())
            {
                return true;
            }
            if (detailBlock.areChildRelationsDirty())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if this controllers underlying data block has been
     * modified. The check will also be performed on all detail block in a
     * master detail relationship with this block
     * 
     * @return <code>true</code> if this controllers underlying data block has
     *         changes, otherwise <code>false</code>
     */
    public boolean isBlockDirty()
    {
        if (getProperties().isControlBlock())
        {
            // Control blocks are never dirty
            return false;
        }

        if (getDataBlock().isDirty())
        {
            return true;
        }

        return _blockRendererController == null ? false : _blockRendererController.isCurrentRecordDirty();
    }

    /**
     * Returns a <code>Collection</code> of inserted records
     * 
     * @return A <code>Collection</code> containing the inserted records
     */
    public Collection<EJDataRecord> getInsertedRecords()
    {
        return getDataBlock().getInsertedRecords();
    }

    /**
     * Returns a <code>Collection</code> of updated records
     * 
     * @return A <code>Collection</code> containing the updated records
     */
    public Collection<EJDataRecord> getUpdatedRecords()
    {
        return getDataBlock().getUpdatedRecords();
    }

    /**
     * Returns a <code>Collection</code> of deleted records
     * 
     * @return A <code>Collection</code> containing the deleted records
     */
    public Collection<EJDataRecord> getDeletedRecords()
    {
        return getDataBlock().getDeletedRecords();
    }

    public HashMap<EJCoreRelationProperties, EJEditableBlockController> getDetailRelationControllers()
    {
        if (getProperties().isMirrorChild() && getMirrorBlockSynchronizer() != null)
        {
            return getMirrorBlockSynchronizer().getMirrorParent().getDetailRelationControllers();
        }

        return _detailRelationControllerMap;
    }

    /**
     * Indicates if operations on this controller should be ignored until its
     * master block has values
     * <p>
     * It must not be possible to perform operations on detail blocks in a
     * master-detail relationship if the details master has no values.
     * <p>
     * 
     * @return
     */
    @Override
    public boolean preventMasterlessOperations()
    {
        if (_masterRelationBlockController != null && (!masterRelationValuesExist()))
        {
            return true;
        }
        return false;
    }

    /**
     * Checks to see if this block controllers master block, if there is one,
     * has values
     * 
     * @return <code>true</code> if there are values <code>false</code> if there
     *         are no values
     */
    public boolean masterRelationValuesExist()
    {
        // If the master controller is null, then this pane is not part of a
        // master detail relationship
        if (_masterRelationBlockController == null)
        {
            return false;
        }

        EJCoreRelationProperties relationProperties = getProperties().getFormProperties().getMasterRelationProperties(getProperties());
        // If there is no relation defined then the user has set the master
        // block pane but not set the
        // relation property within the block definition xml file.
        if (relationProperties == null)
        {
            return false;
        }

        // If the name of the master block is not the same as the one in the
        // relation then there has been
        // some error in the configuration.
        if (!_masterRelationBlockController.getProperties().getName().equals(relationProperties.getMasterBlockProperties().getName()))
        {
            return false;
        }

        EJDataRecord masterRecord = _masterRelationBlockController.getFocusedRecord();
        if (masterRecord == null)
        {
            return false;
        }

        Iterator<EJCoreRelationJoinProperties> joins = relationProperties.getRelationJoins().iterator();
        EJCoreRelationJoinProperties join;
        String masterItemName;
        while (joins.hasNext())
        {
            join = joins.next();

            masterItemName = join.getMasterItem().getName();
            if (masterRecord.getValue(masterItemName) == null)
            {
                return false;
            }

        }
        return true;
    }

    private void addMasterRelationValues(EJDataRecord dataRecord)
    {
        // If the master pane is null, then this pane is not part of a master
        // detail relationship
        if (_masterRelationBlockController == null)
        {
            return;
        }

        EJCoreRelationProperties relationProperties = getProperties().getFormProperties().getMasterRelationProperties(dataRecord.getBlockProperties());
        // If there is no relation defined then the user has set the master
        // block pane but not set the
        // relation property within the block definition xml file.
        if (relationProperties == null)
        {
            return;
        }

        // If the name of the master block is not the same as the one in the
        // relation then there has been
        // some error in the configuration.
        if (!_masterRelationBlockController.getProperties().getName().equals(relationProperties.getMasterBlockProperties().getName()))
        {
            return;
        }

        EJDataRecord masterRecord = _masterRelationBlockController.getFocusedRecord();

        Iterator<EJCoreRelationJoinProperties> joins = relationProperties.getRelationJoins().iterator();

        EJCoreRelationJoinProperties join;
        String masterItemName, detailItemName;

        while (joins.hasNext())
        {
            join = joins.next();

            masterItemName = join.getMasterItem().getName();
            detailItemName = join.getDetailItem().getName();

            dataRecord.getItem(detailItemName).setValue(masterRecord.getValue(masterItemName));
        }
    }

    private void addMasterRelationValues(EJQueryCriteria detailQueryCriteria)
    {
        // If the master pane is null, then this pane is not part of a master
        // detail relationship
        if (_masterRelationBlockController == null || detailQueryCriteria.getBlockName() == null)
        {
            return;
        }

        EJCoreRelationProperties relationProperties = getProperties().getFormProperties()
                .getMasterRelationProperties(super.getFormController().getBlockController(detailQueryCriteria.getBlockName()).getProperties());
        // If there is no relation defined then the user has set the master
        // block pane but not set the
        // relation property within the block definition xml file.
        if (relationProperties == null)
        {
            return;
        }

        // If the name of the master block is not the same as the one in the
        // relation then there has been
        // some error in the configuration.
        if (!_masterRelationBlockController.getProperties().getName().equals(relationProperties.getMasterBlockProperties().getName()))
        {
            return;
        }

        EJDataRecord masterRecord = _masterRelationBlockController.getFocusedRecord();

        Iterator<EJCoreRelationJoinProperties> joins = relationProperties.getRelationJoins().iterator();

        EJCoreRelationJoinProperties join;
        String masterItemName, detailItemName;

        while (joins.hasNext())
        {
            join = joins.next();

            masterItemName = join.getMasterItem().getName();
            detailItemName = join.getDetailItem().getName();
            detailQueryCriteria.add(EJRestrictions.equals(detailItemName, masterRecord.getValue(masterItemName)));
        }
    }

    /**
     * Adds an <code>EJBlockFocusListener</code> to this controller
     * 
     * @param listener
     *            The listener to add
     */
    public void addBlockFocusListener(EJBlockFocusedListener listener)
    {
        if (listener != null)
        {
            _blockFocusListeners.add(listener);
        }
    }

    /**
     * Removes a <code>EJBlockFocusListener</code> from this controller
     * 
     * @param listener
     *            The listener to remove
     */
    public void removeBlockFocusListener(EJBlockFocusedListener listener)
    {
        if (listener != null)
        {
            _blockFocusListeners.remove(listener);
        }
    }
}
