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
import java.util.Iterator;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJLovBlock;
import org.entirej.framework.core.EJManagedFrameworkConnection;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.EJScreenItem;
import org.entirej.framework.core.data.EJDataBlock;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.enumerations.EJLovDisplayReason;
import org.entirej.framework.core.enumerations.EJMessageLevel;
import org.entirej.framework.core.enumerations.EJRecordType;
import org.entirej.framework.core.interfaces.EJFrameworkConnection;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.EJCoreLovItemMappingProperties;
import org.entirej.framework.core.properties.EJCoreLovMappingProperties;
import org.entirej.framework.core.renderers.EJManagedItemRendererWrapper;
import org.entirej.framework.core.renderers.EJManagedQueryScreenRendererWrapper;
import org.entirej.framework.core.renderers.registry.EJBlockItemRendererRegister;
import org.entirej.framework.core.renderers.registry.EJRendererFactory;
import org.entirej.framework.core.service.EJQueryCriteria;
import org.entirej.framework.core.service.EJRestrictions;

public class EJLovController extends EJBlockController implements Serializable
{
    private final EJInternalBlock                _lovBlock;
    private final EJFormController               _formController;
    private final EJCoreLovDefinitionProperties  _definitionProperties;
    private final EJManagedLovRendererController _lovRendererController;

    public EJLovController(EJFormController formController, EJCoreLovDefinitionProperties lovDefinitionProperties)
    {
        super(formController, lovDefinitionProperties.getBlockProperties(), new EJDataBlock(lovDefinitionProperties.getBlockProperties()));

        _definitionProperties = lovDefinitionProperties;
        _formController = formController;
        _lovBlock = new EJInternalBlock(this);

        _lovRendererController = new EJManagedLovRendererController(this);
        _lovRendererController.setRenderer(EJRendererFactory.getInstance().getLovRenderer(this));

        initialiseItems();
        initialiseScreenRenderers();

        _lovRendererController.initialiseRenderer(this);
        _lovBlock.initialiseServicePojoHelper();
    }

    @Override
    public EJInternalBlock getBlock()
    {
        return _lovBlock;
    }

    public void initialiseScreenRenderers()
    {
        EJManagedQueryScreenRendererWrapper queryScreenRenderer = _lovRendererController.getQueryScreenRenderer();
        if (queryScreenRenderer != null)
        {
            queryScreenRenderer.initialiseRenderer(this);
            setQueryScreenRenderer(queryScreenRenderer.getUnmanagedRenderer());
        }
    }

    @Override
    public void setRendererFocus(boolean focus)
    {

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
        return false;
    }

    /**
     * This ensures that all changes made within the renderers are propagated to
     * the blocks data
     */
    @Override
    public void synchronizeFocusedRecord()
    {
    }

    @Override
    public void refreshAfterChange(EJDataRecord record)
    {
        if (_lovRendererController != null)
        {
            _lovRendererController.refreshAfterChange(record);
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
            getFormController().getMessenger().handleMessage(
                    new EJMessage(EJMessageLevel.MESSAGE, "Cannot perform query operation when no data accessor has been defined. Lov Block: "
                            + _definitionProperties.getName() + "." + getProperties().getName()));
            return;
        }
        EJDataRecord record = getBlock().getQueryScreenRenderer().getQueryRecord();
        if (record == null)
        {
            record = getBlock().createRecordNoAction();
        }
        getFormController().getUnmanagedActionController().initialiseRecord(getFormController().getEJForm(), new EJRecord(record), EJRecordType.QUERY);
        getFormController().getUnmanagedActionController().preOpenLovQueryScreen(_lovBlock);
        _lovRendererController.enterQuery(record);
    }

    @Override
    void executeQueryOnAllDetailRelations(boolean clearChanges)
    {
        // No detail relations for an LOV
    }

    /**
     * Instructs EntireJ to perform a query on this lov using no query criteria
     * 
     * @param queryCriteria
     *            The criteria for the query
     * @see #executeQuery(IQueryCriteria, EJFrameworkConnection)
     * 
     */
    public void executeQuery()
    {
        EJQueryCriteria queryCriteria = new EJQueryCriteria(new EJLovBlock(_lovBlock));
        executeQuery(queryCriteria);
    }

    /**
     * Instructs EntireJ to perform a query on this lov using no query criteria
     * 
     * @param queryCriteria
     *            The criteria for the query
     * @see #executeQuery(IQueryCriteria, EJFrameworkConnection)
     * 
     */
    public void executeQuery(EJItemLovController itemLovController)
    {
        EJQueryCriteria queryCriteria = new EJQueryCriteria(new EJLovBlock(_lovBlock), itemLovController);
        executeQuery(queryCriteria);
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
     *            The criteria for the query
     */
    @Override
    public void executeQuery(EJQueryCriteria queryCriteria)
    {
        if (getBlockService() == null)
        {
            getFormController().getMessenger().handleMessage(
                    new EJMessage(EJMessageLevel.MESSAGE, "Cannot perform query operation when no data service has been defined. Block: "
                            + getProperties().getName()));
            return;
        }

        if (queryCriteria == null)
        {
            throw new EJApplicationException(new EJMessage("The query criteria passed to performQueryOperation is null."));
        }

        // Sets the query criteria for use within paging etc
        setQueryCriteria(queryCriteria);

        EJManagedFrameworkConnection connection = getFrameworkManager().getConnection();
        // Clear the block so that it is ready for the newly queried records
        try
        {
            getFormController().getUnmanagedActionController().validateQueryCriteria(getFormController().getEJForm(), getQueryCriteria());
            getFormController().getUnmanagedActionController().preQuery(getFormController().getEJForm(), getQueryCriteria());
            setHasMorePages(true);

            // After the validation is ok, we can inform the renderer that a
            // query will now be executed
            if (_lovRendererController != null)
            {
                _lovRendererController.executingQuery();
            }

            // By setting the focused block controller to null, forces a
            // newRecordInstance method to be called after the query
            // operation has completed and focus returns to the block
            getFormController().setFocusedBlockController(null);

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

            // now inform the renderer that the query is finished. The renderer
            // will need to refresh itself to display the new records
//            if (_lovRendererController != null)
//            {
//                _lovRendererController.queryExecuted();
//            }
        }
        finally
        {
            connection.close();
        }
    }

    /**
     * Indicates that the user want to navigate to the next record
     */
    @Override
    public void nextRecord()
    {
        EJDataRecord record = _lovRendererController.getRecordAfter(_lovRendererController.getFocusedRecord());
        if (record != null)
        {
            getRendererController().recordSelected(record);
            newRecordInstance(record);
        }
    }

    /**
     * Indicates that the user want to navigate to the previous record
     */
    @Override
    public void previousRecord()
    {
        EJDataRecord record = _lovRendererController.getRecordBefore(_lovRendererController.getFocusedRecord());
        if (record != null)
        {
            getRendererController().recordSelected(record);
            newRecordInstance(record);
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
     * Displays the lov that is has been defined for the given item
     * <p>
     * If the user chooses a value from the lov then the values will be copied
     * to the given record using the lov mapping properties
     * 
     * @param itemLovController
     *            This is the item lov controller for the item that has the lov
     *            attached
     * @param displayReason
     *            The reason the lov is being displayed
     */
    public void displayLov(EJItemLovController itemLovController, EJLovDisplayReason displayReason)
    {
        EJManagedFrameworkConnection connection = getFormController().getFrameworkManager().getConnection();
        try
        {
            if (getDefinitionProperties().refreshAutomatically())
            {
                _lovBlock.clear(true);
            }

            if (getDefinitionProperties().executeAutomaticQuery())
            {
                try
                {
                    if (getDefinitionProperties().refreshAutomatically() || _lovBlock.getBlockRecordCount() <= 0)
                    {
                        _lovBlock.clear(true);
                        _lovBlock.executeQuery(new EJQueryCriteria(new EJLovBlock(getBlock()), itemLovController));
                    }
                }
                catch (EJApplicationException e)
                {
                    // Display the exception message and return. The return is
                    // required otherwise the displayLov message will still be
                    // displayed
                    getFormController().getFrameworkManager().handleException(e);
                    return;
                }
            }
            _lovRendererController.displayLov(itemLovController, displayReason);
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
    }

    public void validateItem(EJBlockItemRendererRegister blockItemRegister, EJCoreLovMappingProperties mappingProperties,
            EJScreenItemController itemToValidate, Object oldValue, Object newValue, String lovDefItemName)
    {
        EJQueryCriteria queryCriteria = new EJQueryCriteria(new EJLovBlock(getBlock()));

        if (!_definitionProperties.getBlockProperties().getItemPropertiesContainer().containsItemProperty(lovDefItemName))
        {
            getFormController().getMessenger().handleMessage(
                    new EJMessage(EJMessageLevel.ERROR, "Unable to validate item.\nThere is no item called " + lovDefItemName + " within the lov definition "
                            + _definitionProperties.getName()));
            return;
        }

        queryCriteria.add(EJRestrictions.equals(lovDefItemName, newValue));

        EJManagedFrameworkConnection connection = getFormController().getFrameworkManager().getConnection();
        try
        {
            executeQuery(queryCriteria);

            if (getBlockRecordCount() == 1)
            {
                lovCompleted(blockItemRegister, mappingProperties, itemToValidate, getRecord(0));
            }
            else
            {
                if (getBlockRecordCount() > 1)
                {
                    _lovRendererController.displayLov(itemToValidate.getItemLovController(), EJLovDisplayReason.VALIDATE);
                }
                else
                {
                    queryCriteria = new EJQueryCriteria(new EJLovBlock(getBlock()), itemToValidate.getItemLovController());
                    queryCriteria.add(EJRestrictions.like(lovDefItemName, newValue));
                    executeQuery(queryCriteria);

                    if (getBlockRecordCount() == 1)
                    {

                        lovCompleted(blockItemRegister, mappingProperties, itemToValidate, getRecord(0));
                    }
                    else if (getBlockRecordCount() > 1)
                    {
                        _lovRendererController.displayLov(itemToValidate.getItemLovController(), EJLovDisplayReason.VALIDATE);
                    }
                    else
                    {
                        displayLov(itemToValidate.getItemLovController(), EJLovDisplayReason.VALIDATE);
                    }
                }
            }
        }
        catch (EJApplicationException e)
        {
            getFormController().getFrameworkManager().handleException(e);
        }
        finally
        {
            connection.close();
        }

    }

    void clearAllValues(EJBlockItemRendererRegister blockItemRegister, EJCoreLovMappingProperties mappingProperties, EJScreenItemController itemToValidate)
    {
        Iterator<EJCoreLovItemMappingProperties> props = mappingProperties.getAllItemMappingProperties().iterator();
        while (props.hasNext())
        {
            EJCoreLovItemMappingProperties mapProps = props.next();
            String blockItemName = mapProps.getBlockItemName();

            // By setting the items value again, the item will be
            // marked as changed and validation will be performed
            // as soon as the user tries to navigate out of the item
            if (blockItemName != null && blockItemName.trim().length() > 0)
            {
                blockItemRegister.setItemValueNoValidate(itemToValidate.getScreenType(), blockItemName, null);
            }
        }
    }

    /**
     * This method must be called by the lov renderer when the user has finished
     * with the lov
     * <p>
     * If the user has chosen a value from the lov then the chosen record will
     * be passed. If however the user cancels the lov operation, then the lov
     * renderer must call this method passing <code>null</code> as the chosen
     * value
     * 
     * @param itemLovController
     *            The item lov controller for the item being validated
     * @param record
     *            The record the user chose, or <code>null</code> if the user
     *            canceled the lov operation
     */
    public void lovCompleted(EJItemLovController itemLovController, EJDataRecord record)
    {
        lovCompleted(itemLovController.getItemRendererRegister(), itemLovController.getLovMappingProperties(), itemLovController.getItemToValidate(), record);
    }

    /**
     * This method must be called by the lov renderer when the user has finished
     * with the lov
     * <p>
     * If the user has chosen a value from the lov then the chosen record will
     * be passed. If however the user cancels the lov operation, then the lov
     * renderer must call this method passing <code>null</code> as the chosen
     * value
     * 
     * @param record
     *            The record the user chose, or <code>null</code> if the user
     *            canceled the lov operation
     */
    void lovCompleted(EJBlockItemRendererRegister blockItemRegister, EJCoreLovMappingProperties mappingProperties, EJScreenItemController itemToValidate,
            EJDataRecord record)
    {
        if (record != null)
        {

            for (EJCoreLovItemMappingProperties mapProps : mappingProperties.getAllItemMappingProperties())
            {
                String blockItemName = mapProps.getBlockItemName();
                if (blockItemName == null || blockItemName.trim().length() == 0)
                {
                    continue;
                }

                String lovDefItemName = mapProps.getLovDefinitionItemName();

                if (record.containsItem(lovDefItemName) && blockItemName != null && blockItemName.trim().length() > 0)
                { 
                    Object oldValue = blockItemRegister.getRegisteredRecord().getValue(blockItemName);
                    blockItemRegister.setItemValueNoValidate(itemToValidate.getScreenType(), blockItemName, record.getValue(lovDefItemName));

                    EJManagedItemRendererWrapper item = blockItemRegister.getManagedItemRendererForItem(blockItemName);
                    if (item != null)
                    {
                        blockItemRegister.validateItem(item, itemToValidate.getScreenType(), oldValue, record.getValue(lovDefItemName));
                    }
                }
            }

            EJScreenItem screenItem = new EJScreenItem(itemToValidate.getBlock(), itemToValidate.getScreenType(), itemToValidate.getBlock().getScreenItem(
                    itemToValidate.getScreenType(), itemToValidate.getProperties().getReferencedItemName()));
            _formController.getManagedActionController().lovCompleted(_formController.getEJForm(), screenItem, true);

        }
        else
        {
            if (_lovRendererController.getDisplayReason() == EJLovDisplayReason.VALIDATE)
            {
                itemToValidate.getManagedItemRenderer().gainFocus();
                clearAllValues(blockItemRegister, mappingProperties, itemToValidate);

                EJScreenItem screenItem = new EJScreenItem(itemToValidate.getBlock(), itemToValidate.getScreenType(), itemToValidate.getBlock().getScreenItem(
                        itemToValidate.getScreenType(), itemToValidate.getProperties().getReferencedItemName()));
                _formController.getManagedActionController().lovCompleted(_formController.getEJForm(), screenItem, false);

            }
        }
    }

    @Override
    public EJLovRendererController getRendererController()
    {
        return _lovRendererController.getUnmanagedController();
    }

    public EJManagedLovRendererController getManagedRendererController()
    {
        return _lovRendererController;
    }

    @Override
    public EJFrameworkManager getFrameworkManager()
    {
        return _formController.getFrameworkManager();
    }

    /**
     * Returns the properties the lov definition that is controlled by this
     * controller
     * 
     * @return The lov definition properties of this controller
     */
    public EJCoreLovDefinitionProperties getDefinitionProperties()
    {
        return _definitionProperties;
    }

    /**
     * Returns the properties of the lov definitions underlying block
     * 
     * @return the properties of the lov definitions underlying block
     */
    public EJCoreBlockProperties getBlockProperties()
    {
        return _definitionProperties.getBlockProperties();
    }

    /**
     * Returns the <code>Block</code> used within this
     * <code>LovControllre</code>
     * 
     * @return This lov's controller's <code>Block</code>
     */
    public EJInternalBlock getLovBlock()
    {
        return _lovBlock;
    }

}
