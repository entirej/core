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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJManagedFrameworkConnection;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.actionprocessor.interfaces.EJBlockActionProcessor;
import org.entirej.framework.core.data.EJDataBlock;
import org.entirej.framework.core.data.EJDataItem;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.enumerations.EJMessageLevel;
import org.entirej.framework.core.enumerations.EJRecordType;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.internal.EJInternalForm;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreItemProperties;
import org.entirej.framework.core.properties.EJCoreLovMappingProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJDisplayProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.renderers.EJManagedInsertScreenRendererWrapper;
import org.entirej.framework.core.renderers.EJManagedQueryScreenRendererWrapper;
import org.entirej.framework.core.renderers.EJManagedUpdateScreenRendererWrapper;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusListener;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusedEvent;
import org.entirej.framework.core.renderers.eventhandlers.EJNewRecordFocusedListener;
import org.entirej.framework.core.renderers.eventhandlers.EJScreenItemValueChangedListener;
import org.entirej.framework.core.renderers.interfaces.EJInsertScreenRenderer;
import org.entirej.framework.core.renderers.interfaces.EJItemRenderer;
import org.entirej.framework.core.renderers.interfaces.EJQueryScreenRenderer;
import org.entirej.framework.core.renderers.interfaces.EJUpdateScreenRenderer;
import org.entirej.framework.core.service.EJBlockService;
import org.entirej.framework.core.service.EJPostQueryCache;
import org.entirej.framework.core.service.EJQueryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EJBlockController implements Serializable
{
    final Logger                                          logger                     = LoggerFactory.getLogger(EJBlockController.class);

    private EJMirrorBlockSynchronizer                     _mirrorBlockSynchronizer;

    private int                                           _pageSize                  = 0;
    private int                                           _pageNumber                = 1;
    private boolean                                       _queryAllRows              = true;
    private int                                           _maxResults                = -1;
    private boolean                                       _hasMorePages              = false;

    /**
     * Used in conjunction with the deferred query property. This criteria will
     * contain the query criteria for the query to be executed
     */
    private EJQueryCriteria                               _queryCriteria             = null;

    private EJFrameworkManager                            _frameworkManager;
    private EJFormController                              _formController;
    private EJManagedUpdateScreenRendererWrapper          _updateScreenRenderer;
    private EJManagedInsertScreenRendererWrapper          _insertScreenRenderer;
    private EJManagedQueryScreenRendererWrapper           _queryScreenRenderer;
    private EJDataBlock                                   _dataBlock;
    private EJCoreBlockProperties                         _blockProperties;
    private String                                        _focusedItemName;

    private ArrayList<EJLovMappingController>             _lovMappingControllers     = new ArrayList<EJLovMappingController>();
    private ArrayList<EJScreenItemValueChangedListener>         _itemValueChangedListeners = new ArrayList<EJScreenItemValueChangedListener>();
    private ArrayList<EJItemFocusListener>                _itemFocusListeners        = new ArrayList<EJItemFocusListener>();
    private ArrayList<EJNewRecordFocusedListener>         _newRecordListeners        = new ArrayList<EJNewRecordFocusedListener>();

    private LinkedHashMap<String, EJItemController>       _itemProperties            = new LinkedHashMap<String, EJItemController>();
    private LinkedHashMap<String, EJScreenItemController> _mainScreenItems           = new LinkedHashMap<String, EJScreenItemController>();
    private LinkedHashMap<String, EJScreenItemController> _insertScreenItems         = new LinkedHashMap<String, EJScreenItemController>();
    private LinkedHashMap<String, EJScreenItemController> _updateScreenItems         = new LinkedHashMap<String, EJScreenItemController>();
    private LinkedHashMap<String, EJScreenItemController> _queryScreenItems          = new LinkedHashMap<String, EJScreenItemController>();

    abstract void executeQueryOnAllDetailRelations(boolean clearChanges);

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
    public abstract boolean preventMasterlessOperations();

    public EJInternalForm getForm()
    {
        return getBlock().getForm();
    }

    public abstract EJInternalBlock getBlock();

    public abstract void refreshAfterChange(EJDataRecord record);

    /**
     * Informs the blocks renderer that the user wishes to enter a query on this
     * block
     */
    public abstract void enterQuery();

    /**
     * Retrieve the renderer
     * 
     * @return The renderer used for this block
     */
    public abstract EJBlockRendererController getRendererController();

    public abstract void setRendererFocus(boolean focus);

    /**
     * Indicates that the user want to navigate to the next record
     */
    public abstract void nextRecord();

    /**
     * Indicates that the user want to navigate to the previous record
     */
    public abstract void previousRecord();

    /**
     * Creates a controller for the given data block
     * 
     * @param formController
     * @param blockProperties
     *            the properties of the given block
     * @param dataBlock
     *            the created controller will control this data block
     */
    public EJBlockController(EJFormController formController, EJCoreBlockProperties blockProperties, EJDataBlock dataBlock)
    {
        if (dataBlock == null)
        {
            throw new EJApplicationException("The DataBlock passed to the BlockController constructor is null");
        }
        _formController = formController;
        _frameworkManager = formController.getFrameworkManager();
        _dataBlock = dataBlock;
        _blockProperties = blockProperties;

    }

    protected void initialiseItems()
    {
        logger.trace("START initialiseItems");
        for (EJCoreLovMappingProperties mappingProperties : getProperties().getLovMappingContainer().getAllLovMappingProperties())
        {
            _lovMappingControllers.add(new EJLovMappingController(_frameworkManager, getBlock(), mappingProperties));
        }

        for (EJCoreItemProperties props : _blockProperties.getItemPropertiesContainer().getAllItemProperties())
        {
            _itemProperties.put(props.getName(), new EJItemController(this, props));

            EJScreenItemProperties mainScreenItemProps = getProperties().getScreenItemGroupContainer(EJScreenType.MAIN)
                    .getScreenItemProperties(props.getName());
            EJScreenItemProperties insertScreenItemProps = getProperties().getScreenItemGroupContainer(EJScreenType.INSERT)
                    .getScreenItemProperties(props.getName());
            EJScreenItemProperties updateScreenItemProps = getProperties().getScreenItemGroupContainer(EJScreenType.UPDATE)
                    .getScreenItemProperties(props.getName());
            EJScreenItemProperties queryScreenItemProps = getProperties().getScreenItemGroupContainer(EJScreenType.QUERY)
                    .getScreenItemProperties(props.getName());

            if (mainScreenItemProps != null)
            {
                EJMainScreenItemController mainController = new EJMainScreenItemController(this, props);
                _mainScreenItems.put(props.getName(), mainController);
            }

            if (insertScreenItemProps != null)
            {
                EJInsertScreenItemController insertController = new EJInsertScreenItemController(this, props);
                _insertScreenItems.put(props.getName(), insertController);
            }

            if (updateScreenItemProps != null)
            {
                EJUpdateScreenItemController updateController = new EJUpdateScreenItemController(this, props);
                _updateScreenItems.put(props.getName(), updateController);
            }

            if (queryScreenItemProps != null)
            {
                EJQueryScreenItemController queryController = new EJQueryScreenItemController(this, props);
                _queryScreenItems.put(props.getName(), queryController);
            }
        }

        logger.trace("END initialiseItems");
    }

    protected void initialiseItemRenderers()
    {
        logger.trace("START initialiseItemRenderers");

        for (EJScreenItemController itemController : _mainScreenItems.values())
        {
            itemController.initialiseRenderer();
        }

        for (EJScreenItemController itemController : _insertScreenItems.values())
        {
            itemController.initialiseRenderer();
        }

        for (EJScreenItemController itemController : _updateScreenItems.values())
        {
            itemController.initialiseRenderer();
        }

        for (EJScreenItemController itemController : _queryScreenItems.values())
        {
            itemController.initialiseRenderer();
        }

        logger.trace("END initialiseItemRenderers");
    }

    /**
     * Returns the current page number of this block
     * 
     * @return The page number
     */
    public int getPageNumber()
    {
        return _pageNumber;
    }

    protected void setPageNumber(int pageNum)
    {
        _pageNumber = pageNum;
    }

    /**
     * Indicates if this block will have all records retrieved instead of using
     * paging
     * 
     * @return <code>true</code> if all rows should be retrieved otherwise
     *         <code>false</code>
     */
    public boolean canQueryAllRows()
    {
        return _queryAllRows;
    }

    protected void setQueryAllRows(boolean queryAllRows)
    {
        _queryAllRows = queryAllRows;
    }

    /**
     * Indicates the maximum amount of rows that should be retrieved for this
     * block
     * <p>
     * The maxResults is only used in conjunction with the queryAllRows option
     * 
     * @return The maximum number of rows that should be retrieved for this
     *         block
     */
    public int getMaxResults()
    {
        return _maxResults;
    }

    protected void setMaxResults(int maxResults)
    {
        _maxResults = maxResults;
    }

    /**
     * Sets the query criteria to be used when querying this blocks data service
     * <p>
     * Query criteria may be used to re-query a block or when deferred querying
     * is being made
     * 
     * @param queryCriteria
     *            The query criteria to set
     */
    public void setQueryCriteria(EJQueryCriteria queryCriteria)
    {
        _queryCriteria = queryCriteria;
    }

    /**
     * Returns the query criteria that has been set within this block
     * 
     * @return The blocks saved query criteria
     */
    public EJQueryCriteria getQueryCriteria()
    {
        return _queryCriteria;
    }

    /**
     * Returns the number records to be displayed within one page
     * 
     * @return The page size of this block
     */
    public int getPageSize()
    {
        return _pageSize;
    }

    protected void setPageSize(int pageSize)
    {
        _pageSize = pageSize;
    }

    /**
     * If this block is part of mirroring then this method is used to set the
     * mirror synchronizer
     * <p>
     * A mirror synchronizer ensures that all blocks mirrored with this one are
     * kept in synch
     * 
     * @param mirrorBlockSynchroniser
     *            The synchronizer
     */
    public void setMirrorBlockSynchronizer(EJMirrorBlockSynchronizer mirrorBlockSynchroniser)
    {
        _mirrorBlockSynchronizer = mirrorBlockSynchroniser;
    }

    public EJMirrorBlockSynchronizer getMirrorBlockSynchronizer()
    {
        return _mirrorBlockSynchronizer;
    }

    /**
     * Returns a collection of all <code>EJDefaultItemController</code> for this
     * block controller
     * 
     * @return A <code>Collection</code> of <code>EJDefaultItemController</code>
     *         that have been added to this block controller
     */
    public Collection<EJItemController> getAllBlockItemControllers()
    {
        return _itemProperties.values();
    }

    /**
     * Returns the <code>EJDefaultItemController</code> for the item with the
     * given name
     * 
     * @param itemName
     *            The name of the item
     * @return The block item or <code>null</code> if there is no item with the
     *         given name
     */
    public EJItemController getBlockItemController(String itemName)
    {
        return _itemProperties.get(itemName);
    }

    /**
     * Returns a collection of all <code>MainScreenItemController</code> for
     * this block controller
     * 
     * @return A <code>Collection</code> of
     *         <code>MainScreenItemController</code> that have been added to
     *         this block controller
     */
    public Collection<EJScreenItemController> getAllScreenItems(EJScreenType screenType)
    {
        switch (screenType)
        {
            case MAIN:
                return _mainScreenItems.values();
            case INSERT:
                return _insertScreenItems.values();
            case QUERY:
                return _queryScreenItems.values();
            case UPDATE:
                return _updateScreenItems.values();
            default:
                return null;
        }

    }

    /**
     * Returns a given <code>EJScreenItem</code> for a given item
     * 
     * @param itemName
     *            The name of the item for which the controller should be
     *            returned
     * @return The given items controller or <code>null</code> if there is no
     *         item controller with the given name
     */
    public EJScreenItemController getScreenItem(EJScreenType screenType, String itemName)
    {
        if (itemName == null || itemName.trim().length() == 0)
        {
            return null;
        }

        switch (screenType)
        {
            case MAIN:
                return _mainScreenItems.get(itemName);
            case INSERT:
                return _insertScreenItems.get(itemName);
            case QUERY:
                return _queryScreenItems.get(itemName);
            case UPDATE:
                return _updateScreenItems.get(itemName);
            default:
                return null;
        }
    }

    /**
     * Returns the controller responsible for the form
     * 
     * @return The form controller
     */
    public EJFormController getFormController()
    {
        return _formController;
    }

    public EJFrameworkManager getFrameworkManager()
    {
        return _frameworkManager;
    }

    public boolean containsRecord(EJDataRecord record)
    {
        return _dataBlock.containsRecord(record);
    }

    public EJBlockService<?> getBlockService()
    {
        return _blockProperties.getBlockService();
    }

    public EJDataBlock getDataBlock()
    {
        return _dataBlock;
    }

    public void setDataBlock(EJDataBlock dataBlock)
    {
        _dataBlock = dataBlock;
    }

    /**
     * Navigates to the first record within this block
     * <p>
     * If the block has no records then no action will be performed
     */
    public void navigateToFirstRecord()
    {
        if (getRendererController() == null)
        {
            return;
        }

        logger.trace("START navigateToFirstRecord");

        EJDataRecord record = getRendererController().getFirstRecord();
        if (record != null)
        {
            navigateToRecord(record);
        }

        logger.trace("END navigateTorFirstRecord");
    }

    /**
     * Navigates to the last record of this block
     * <p>
     * If this block has no records then no action will be performed
     */
    public void navigateToLastRecord()
    {
        if (getRendererController() == null)
        {
            return;
        }

        logger.trace("START navigateToLastRecord");
        EJDataRecord record = getRendererController().getLastRecord();
        if (record != null)
        {
            navigateToRecord(record);
        }
        logger.trace("END navigateToLastRecord");
    }

    public abstract void navigateToRecord(EJDataRecord record);

    /**
     * Returns the current focused record
     * <p>
     * The current record will be used within EJ when a user wished to update or
     * delete the current record. The current block record is not necessarily
     * the current displayed record. This is because it is possible for the
     * renderer to only display a subset of records. For example. If a user can
     * filter the displayed records, then the third displayed record is not
     * necessarily the third record the the data block. Therefore allow the
     * renderer to decide which is the current record
     * 
     * @return The record upon which the user currently has focus
     */
    public EJDataRecord getFocusedRecord()
    {
        return getRendererController().getFocusedRecord();
    }

    /**
     * Returns the item that is currently focused on the current record or
     * <code>null</code> if no item has focus
     * 
     * @return The item that currently has focus within the current record
     * @throws EJBlockControllerException
     */
    public EJDataItem getFocusedItem()
    {
        if (getFocusedRecord() != null && _focusedItemName != null)
        {
            return getFocusedRecord().getItem(_focusedItemName);
        }
        return null;
    }

    public EJDataRecord getRecord(int recordNumber)
    {
        return getDataBlock().getRecord(recordNumber);
    }

    public EJDataRecord getRecordBefore(EJDataRecord currentRecord)
    {
        logger.trace("START getRecordBefore");

        if (currentRecord == null || getRendererController() == null)
        {
            logger.trace("END getRecordBefore. NULL");
            return null;
        }
        logger.trace("END getRecordBefore");

        return getRendererController().getRecordBefore(currentRecord);
    }

    public EJDataRecord getRecordAfter(EJDataRecord currentRecord)
    {
        logger.trace("START getRecordAfter");

        if (currentRecord == null || getRendererController() == null)
        {
            logger.trace("END getRecordAfter. NUKK");
            return null;
        }
        logger.trace("END getRecordAfter");
        return getRendererController().getRecordAfter(currentRecord);
    }

    public int getDisplayedRecordNumber(EJDataRecord record)
    {
        logger.trace("START getDisplayedRecordNumber");
        if (getRendererController() == null)
        {
            logger.trace("END getDisplayedRecordNumber: -1");

            return -1;
        }
        int recNum = getRendererController().getDisplayedRecordNumber(record);

        logger.trace("END getDisplayedRecordNumber: {}", recNum);

        return recNum;
    }

    public boolean isFirstDisplayedRecord()
    {
        logger.trace("START isFirstDisplayedRecord");
        if (getRendererController() == null)
        {
            logger.trace("END isFirstDisplayedRecord: false");
            return false;
        }

        if (getFocusedRecord() == getRendererController().getFirstRecord())
        {
            logger.trace("END isFirstDisplayedRecord: true");
            return true;
        }
        logger.trace("END isFirstDisplayedRecord: false");
        return false;
    }

    public boolean isLastDisplayedRecord()
    {
        logger.trace("START isLastDisplayedRecord");
        if (getRendererController() == null)
        {
            logger.trace("END isLastDisplayedRecord. Value: false");
            return false;
        }

        if (getFocusedRecord() == getRendererController().getLastRecord())
        {
            logger.trace("END isLastDisplayedRecord. Value: true");
            return true;
        }

        logger.trace("END isLastDisplayedRecord. Value: false");
        return false;
    }

    /**
     * Creates a new record containing all items defined within the blocks item
     * definitions
     * <p>
     * Each block item can have a default value defined by the
     * {@link EJCoreItemProperties#getCopyValueFrom()} property. This method
     * will ignore this property, to create a record and to use this value call
     * the {@link #createNewRecord(boolean)} method.
     * <p>
     * After the record has been created, the forms action processors
     * <code>whenCreateRecord</code> will be called
     * 
     * @param recordType
     *            The type of record to be created
     * @return a new record
     * @see {@link #createRecord(boolean)}
     */
    public EJDataRecord createRecord(EJRecordType recordType)
    {
        return createRecord(recordType, false);
    }

    /**
     * Creates a new record containing all items defined within the blocks item
     * definitions
     * <p>
     * After the record has been created, the forms action processors
     * <code>initialiseRecord</code> will be called
     * 
     * @param recordType
     *            The type of record to be created
     * @param addDefaultInsertValues
     *            Indicates if the values defined by
     *            {@link EJCoreItemProperties#getDefaultInsertValue()} should be
     *            added to the items within the newly create record
     * 
     * @return a new record
     */
    public EJDataRecord createRecord(EJRecordType recordType, boolean addDefaultInsertValues)
    {
        EJDataRecord record;
        record = createNewRecord(addDefaultInsertValues);
        getFormController().getManagedActionController().initialiseRecord(getFormController().getEJForm(), new EJRecord(record), recordType);
        return record;
    }

    /**
     * Creates a record without calling the
     * {@link EJBlockActionProcessor#initialiseRecord(EJForm, EJRecord,EJRecordType)}
     * method
     * <p>
     * The values defined by the
     * {@link EJCoreItemProperties#getDefaultInsertValue()} will be ignored. If
     * these values should be used then use the
     * {@link #createNewRecord(boolean)} method
     * 
     * @return A new record
     * @see {@link #createRecord()}
     */
    public EJDataRecord createRecordNoAction()
    {
        return createNewRecord(false);
    }

    /**
     * Creates a new record based upon the items within defined within the
     * blocks list of items. The record will be neutral, meaning that it is
     * neither dirty or marked as new. These statuses will be added when adding
     * the record to the block. If this is a new record, then calling the
     * <code>recordCreated</code> will mark the record as new and add it to the
     * blocks list of record.
     * 
     * @param addDefaultInsertValues
     *            Indicates if the values defined by
     *            {@link EJCoreItemProperties#getDefaultInsertValue()} should be
     *            added to the items within the newly create record
     * @return A new record
     */
    protected EJDataRecord createNewRecord(boolean addDefaultInsertValues)
    {
        EJDataRecord record = new EJDataRecord(_formController, getBlock(), addDefaultInsertValues);
        return record;
    }

    /**
     * This ensures that all changes made within the renderers are propagated to
     * the blocks data
     */
    public abstract void synchronizeFocusedRecord();

    /**
     * Copies the blocks focused record
     * <p>
     * All record items and values will be copied. The record will be marked as
     * insert and therefore can be added to the blocks list of inserted records.
     * If the record contains a primary key, then these values will need to be
     * changed before inserting the record to ensure that no integrity
     * constraints will be broken
     * 
     * After the record has been copied, the forms action processors
     * <code>initialiseRecord</code> will be called
     * 
     * @return The record copied from the current record
     */
    public EJDataRecord copyFocusedRecord()
    {
        logger.trace("START copyFocusedRecord");

        if (getFocusedRecord() == null)
        {
            return null;
        }
        // First synchronise the current record. This will ensure that if the
        // user has made any changes to the block items then, they will be
        // Propagated to the record, before it is used.
        synchronizeFocusedRecord();

        EJDataRecord record = createRecordNoAction();
        record.markForInsert(true);

        if (getFocusedRecord() == null)
        {
            return record;
        }

        Iterator<EJDataItem> dataItems = getFocusedRecord().getAllItems().iterator();
        while (dataItems.hasNext())
        {
            EJDataItem item = dataItems.next();
            if (record.containsItem(item.getName()))
            {
                record.getItem(item.getName()).setValue(item.getValue());
            }
        }
        getFormController().getManagedActionController().initialiseRecord(getFormController().getEJForm(), new EJRecord(record), EJRecordType.INSERT);
        logger.trace("END  copyFocusedRecord");
        return record;
    }

    /**
     * Re-Executes the last query
     * 
     * @throws EJBlockControllerException
     */
    public void executeLastQuery()
    {
        if (_queryCriteria != null)
        {
            executeQuery(_queryCriteria);
        }
    }

    /**
     * Indicates if this controller will be querying in pages
     * 
     * @return <code>true</code> if the data will be queried in pages, otherwise
     *         <code>false</code>
     */
    public boolean canQueryInPages()
    {
        if ((!getProperties().queryAllRows()) && (_blockProperties.getBlockService() != null && _blockProperties.getBlockService().canQueryInPages())
                && getProperties().getPageSize() > 0)
        {
            return true;
        }
        return false;
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
     *            The query criteria to be used for this block
     */
    public abstract void executeQuery(EJQueryCriteria queryCriteria);

    protected final void getPage(boolean informRenderer)
    {
        logger.trace("START getPage");

        EJManagedFrameworkConnection connection = getFrameworkManager().getConnection();
        try
        {
            clearBlock(true);

            if (_blockProperties.getBlockService() == null)
            {
                return;
            }

            _queryCriteria.setPageSize(_pageSize);
            _queryCriteria.setPageNumber(_pageNumber);
            _queryCriteria.setQueryAllRows(_queryAllRows);
            _queryCriteria.setMaxResults(_maxResults);

            logger.trace("Calling execute query on service: {}", _blockProperties.getBlockService().getClass().getName());
            List<?> entities = _blockProperties.getBlockService().executeQuery(getFormController().getEJForm(), _queryCriteria);
            logger.trace("Execute query on block service completed. {} records retrieved", (entities == null ? 0 : entities.size()));

            if (entities != null)
            {
                // Test for page site first as the hasMoreRows flag may not have
                // been set
                if (entities.size() < _pageSize)
                {
                    _hasMorePages = false;
                }
                else if ((canQueryInPages() && _queryCriteria.hasMoreRows()))
                {
                    _hasMorePages = true;
                }
                else
                {
                    _hasMorePages = false;
                }

                // Now loop through the retrieved records and add them to the
                // block

                // Create a post query cache so that lookups on each record are
                // optimized
                logger.trace("Creating post query cache and doing post queries");
                EJPostQueryCache postQueryCache = new EJPostQueryCache();
                for (Object entity : entities)
                {
                    EJDataRecord record = new EJDataRecord(_formController, getBlock(), entity, false);
                    addLovMappingValuesAfterQuery(record, postQueryCache);
                    addQueriedRecord(record);
                }
                logger.trace("Completed post queries, clearing post query cache");

                // Clear the cache to free memory
                postQueryCache.clear();

                // Let the renderer know that a query has been executed. This
                // needs to be called before the displayed record count
                // is retrieved, otherwise the renderer will return 0 because it
                // has not yet been informed that records have been retrieved
                getRendererController().queryExecuted();

                if (getBlockRecordCount() > 0)
                {
                    getRendererController().recordSelected(getRendererController().getFirstRecord());
                    if (getFocusedRecord() != null)
                    {
                        getFormController().getManagedActionController().newRecordInstance(getFormController().getEJForm(), new EJRecord(getFocusedRecord()));
                    }
                }
                else
                {
                    getRendererController().recordSelected(null);
                }

                executeQueryOnAllDetailRelations(true);

                newRecordFocused(getFocusedRecord());
            }
        }
        catch (Exception e)
        {
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }

        logger.trace("END getPage");
    }

    public void nextPage()
    {
        nextPage(true);
    }

    public void nextPage(boolean informRenderer)
    {
        logger.trace("START nextPage");

        if (_blockProperties.getBlockService() == null)
        {
            getFormController().getMessenger().handleMessage(
                    new EJMessage(EJMessageLevel.MESSAGE, "Cannot fetch next page when no data service has been defined. Block: " + getProperties().getName()));
            return;
        }

        if (!_hasMorePages)
        {
            throw new EJApplicationException(new EJMessage(EJMessageLevel.HINT, "No more records to retrieve"));
        }

        _pageNumber++;
        getPage(informRenderer);

        logger.trace("END nextPage");
    }

    public void previousPage()
    {
        previousPage(true);
    }

    public void previousPage(boolean informRenderer)
    {
        logger.trace("START previousPage");
        if (_blockProperties.getBlockService() == null)
        {
            getFormController().getMessenger().handleMessage(
                    new EJMessage(EJMessageLevel.MESSAGE, "Cannot fetch next page when no data service has been defined. Block: " + getProperties().getName()));
            return;
        }

        if (_pageNumber <= 1)
        {
            throw new EJApplicationException(new EJMessage(EJMessageLevel.HINT, "Already on the first page"));
        }

        _pageNumber--;
        getPage(informRenderer);
        logger.trace("END previousPage");
    }

    public void addLovMappingValuesAfterQuery(EJDataRecord queriedRecord)
    {
        EJPostQueryCache cache = new EJPostQueryCache();
        for (EJLovMappingController controller : _lovMappingControllers)
        {
            controller.addLookupValuesForQueryRecord(_formController, queriedRecord, cache);
        }
        cache.clear();
    }

    public void addLovMappingValuesAfterQuery(EJDataRecord queriedRecord, EJPostQueryCache cache)
    {
        for (EJLovMappingController controller : _lovMappingControllers)
        {
            controller.addLookupValuesForQueryRecord(_formController, queriedRecord, cache);
        }
    }

    /**
     * Adds a record to this controllers underlying list of records
     * <p>
     * This method should be called for each record the data access processor
     * retrieves. The forms action processors post-query method will be called
     * then the record will be added to the blocks underlying list of records
     * 
     * @param record
     */
    protected void addQueriedRecord(EJDataRecord record)
    {
        if (record != null)
        {
            _dataBlock.addQueriedRecord(record);
            getFormController().getManagedActionController().postQuery(getFormController().getEJForm(), new EJRecord(record));
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
    public void newRecordInstance(EJDataRecord selectedRecord)
    {
        if (selectedRecord != null)
        {
            newRecordFocused(selectedRecord);
        }
    }

    public void executeActionCommand(String actionCommand, EJScreenType screenType)
    {
        if (getRendererController() == null)
        {
            return;
        }

        logger.trace("START executeActionCommand: ScreenType: {}, Command: {}", screenType, actionCommand);

        logger.trace("   -> calling ActionController executeActionCommand");
        getFormController().getUnmanagedActionController().executeActionCommand(_formController.getEJForm(), _blockProperties.getName(), actionCommand, screenType);

        logger.trace("END executeActionCommand");
    }

    /**
     * Indicates if there are more pages available for this block
     * <p>
     * This method will return <code>true</code> if the block is retrieving data
     * in pages and not all pages have been retrieved
     * 
     * @return <code>true</code> if there is still more pages available,
     *         otherwise <code>false</code>
     */
    public boolean hasMorePages()
    {
        return _hasMorePages;
    }

    /**
     * Sets the has more pages flags to the value specified
     * 
     * @param hasMorePages
     *            Indicates if the block has more pages available
     */
    protected void setHasMorePages(boolean hasMorePages)
    {
        _hasMorePages = hasMorePages;
    }

    /**
     * Indicates if this controller is showing data from the first page
     * <p>
     * If this controller does not have paging enabled, then it will always
     * return <code>true</code>
     * 
     * @return <code>true</code> if the first page of data is being displayed,
     *         otherwise <code>false</code>
     */
    public boolean isOnFirstPage()
    {
        if (_pageNumber <= 1)
        {
            logger.trace("START isOnFirstPage: true");

            return true;
        }

        logger.trace("START isOnFirstPage: false");

        return false;
    }

    /**
     * Returns the total number of records displayed by this blocks renderer
     * 
     * @return The total number of records displayed within this blocks renderer
     * @throws EJBlockRendererException
     */
    public int getDisplayedRecordCount()
    {
        return getRendererController().getDisplayedRecordCount();
    }

    /**
     * Returns the total number of records held within this controllers
     * underlying data block
     * 
     * @return The total number of records within this controllers underlying
     *         data block
     */
    public int getBlockRecordCount()
    {
        return _dataBlock.getBlockRecordCount();
    }

    /**
     * Returns the properties of this controllers data block
     * 
     * @return The properties of this controllers data block
     */
    public EJCoreBlockProperties getProperties()
    {
        return _blockProperties;
    }

    /**
     * Clears this controllers underlying data block
     * <p>
     * Only this controllers underlying data block will be cleared, if the block
     * is master in a master-detail relationship then its detail blocks will be
     * left untouched. To clear all detail blocks use the
     * {@link EJBlockController#clearAllDetailRelations()}
     * 
     * @param clearChanges
     *            Indicates if the dirty records contained within this block
     *            should also be cleared
     * @see EJBlockController#clearAllDetailRelations()
     */
    public void clearBlock(boolean clearChanges)
    {
        logger.trace("START clearBlock");

        _dataBlock.clearBlock(clearChanges);

        if (getRendererController() != null)
        {
            getRendererController().recordSelected(null);
        }

        if (_mirrorBlockSynchronizer != null)
        {
            _mirrorBlockSynchronizer.blockCleared(this);
        }

        logger.trace("END clearBlock");
    }

    /**
     * Returns a <code>Collection</code> of records within this block
     * 
     * @return A <code>Collection</code> containing the blocks records
     */
    public Collection<EJDataRecord> getRecords()
    {
        return _dataBlock.getRecords();
    }

    /**
     * Called each time one of the displayed items value changes
     * <p>
     * This will be called by the blocks register when the register is informed
     * of an item renderers value changing
     * 
     * @param renderer
     *            The item renderer that has changed
     */
    public void itemValueChanged(EJScreenItemController item, EJItemRenderer renderer, Object newValue)
    {
        logger.trace("START itemValueCanged. Item: {}", item.getName());

        Iterator<EJScreenItemValueChangedListener> iti = _itemValueChangedListeners.iterator();
        while (iti.hasNext())
        {
            iti.next().screenItemValueChanged(item, renderer, newValue);
        }

        logger.trace("END itemValueChanged");

    }

    /**
     * Adds an <code>IItemValueChangedListener</code> to this blocks list of
     * listeners
     * 
     * @param listener
     *            The listener to be added
     */
    public void addItemValueChangedListener(EJScreenItemValueChangedListener listener)
    {
        _itemValueChangedListeners.add(listener);
    }

    /**
     * Removes the given listener from this blocks list of listeners if the
     * listener exists
     * 
     * @param listener
     *            The listener to be removed
     */
    public void removeItemValueChangedListener(EJScreenItemValueChangedListener listener)
    {
        _itemValueChangedListeners.remove(listener);
    }

    /**
     * Adds an <code>IItemFocusListener</code> to this blocks list of listeners
     * 
     * @param listener
     *            The listener to be added
     */
    public void addItemFocusListener(EJItemFocusListener listener)
    {
        _itemFocusListeners.add(listener);
    }

    /**
     * Removes the given listener from this blocks list of listeners if the
     * listener exists
     * 
     * @param listener
     *            The listener to be removed
     */
    public void removeItemFocusListener(EJItemFocusListener listener)
    {
        _itemFocusListeners.remove(listener);
    }

    /**
     * Indicates that an item has gained focus
     * <p>
     * If the focused item is part of an lov mapping then its
     * <code>LovController</code> will be passed. This enables any listener to
     * be able to react to LOV commands from the user
     * 
     * @param focusedEvent
     *            The focused event
     */
    public void itemFocusedGained(EJItemFocusedEvent focusedEvent)
    {
        _focusedItemName = focusedEvent.getItem().getProperties().getReferencedItemName();

        logger.trace("START itemFocusGained. Item: {}", _focusedItemName);

        Iterator<EJItemFocusListener> iti = _itemFocusListeners.iterator();
        while (iti.hasNext())
        {
            iti.next().focusGained(focusedEvent);
        }

        logger.trace("END itemFocusGained");

    }

    /**
     * Indicates that the given renderer has lost focus
     * 
     * @param focusedEvent
     *            The focused event
     */
    public void itemFocusLost(EJItemFocusedEvent focusedEvent)
    {
        logger.trace("START itemFocusLost");

        Iterator<EJItemFocusListener> iti = _itemFocusListeners.iterator();
        while (iti.hasNext())
        {
            iti.next().focusLost(focusedEvent);
        }

        logger.trace("END itemFocusLost");
    }

    /**
     * Adds an <code>INewRecordFocusedListener</code> to this blocks list of
     * listeners
     * 
     * @param listener
     *            The listener to be added
     */
    public void addNewRecordFocusedListener(EJNewRecordFocusedListener listener)
    {
        _newRecordListeners.add(listener);
    }

    /**
     * Removes the given listener from this blocks list of listeners if the
     * listener exists
     * 
     * @param listener
     *            The listener to be removed
     */
    public void removeNewRecordFocusedListener(EJNewRecordFocusedListener listener)
    {
        _newRecordListeners.remove(listener);
    }

    /**
     * Indicates that a record has gained focus
     * <p>
     * The block renderer will call the newRecordInstance method when a new
     * record has gained focus. The newRecordInstance method will then call this
     * method to inform all listeners
     * 
     * @param focusedRecord
     *            The focused record
     */
    private void newRecordFocused(EJDataRecord focusedRecord)
    {
        if (focusedRecord != null)
        {
            logger.trace("START newRecordFocused");

            Iterator<EJNewRecordFocusedListener> iti = _newRecordListeners.iterator();
            while (iti.hasNext())
            {
                iti.next().focusedGained(focusedRecord);
            }

            logger.trace("END newRecordFocused");
        }
    }

    /**
     * Returns the <code>IQueryScreenRenderer</code> defined for the block or
     * <code>null</code> if no query screen renderer has been assigned
     * 
     * @return The <code>IQueryScreenRenderer</code> defined for the block or
     *         <code>null</code> if no query screen renderer has been assigned
     */
    public EJManagedQueryScreenRendererWrapper getManagedQueryScreenRenderer()
    {
        return _queryScreenRenderer;
    }

    /**
     * Returns the <code>IQueryScreenRenderer</code> defined for the block or
     * <code>null</code> if no query screen renderer has been assigned
     * 
     * @return The <code>IQueryScreenRenderer</code> defined for the block or
     *         <code>null</code> if no query screen renderer has been assigned
     */
    public EJQueryScreenRenderer getQueryScreenRenderer()
    {
        if (_queryScreenRenderer != null)
        {
            return _queryScreenRenderer.getUnmanagedRenderer();
        }
        return null;
    }

    protected void setQueryScreenRenderer(EJQueryScreenRenderer renderer)
    {
        if (renderer == null)
        {
            _queryScreenRenderer = null;
        }
        else
        {
            _queryScreenRenderer = new EJManagedQueryScreenRendererWrapper(_frameworkManager, renderer);
        }
    }

    /**
     * Returns the <code>ManagedInsertScreenRendererWrapper</code> defined for
     * the block or <code>null</code> if no insert screen renderer has been
     * assigned
     * 
     * @return The <code>ManagedInsertScreenRendererWrapper</code> defined for
     *         the block or <code>null</code> if no insert screen renderer has
     *         been assigned
     */
    public EJManagedInsertScreenRendererWrapper getManagedInsertScreenRenderer()
    {
        return _insertScreenRenderer;
    }

    /**
     * Returns the <code>IInsertScreenRenderer</code> defined for the block or
     * <code>null</code> if no insert screen renderer has been assigned
     * 
     * @return The <code>IInsertScreenRenderer</code> defined for the block or
     *         <code>null</code> if no insert screen renderer has been assigned
     */
    public EJInsertScreenRenderer getInsertScreenRenderer()
    {
        if (_insertScreenRenderer != null)
        {
            return _insertScreenRenderer.getUnmanagedRenderer();
        }
        return null;
    }

    protected void setInsertScreenRenderer(EJInsertScreenRenderer renderer)
    {
        if (renderer == null)
        {
            _insertScreenRenderer = null;
        }
        else
        {
            _insertScreenRenderer = new EJManagedInsertScreenRendererWrapper(_frameworkManager, renderer);
        }
    }

    /**
     * Returns the <code>ManagedUpdateScreenRendererWrapper</code> defined for
     * the block or <code>null</code> if no update screen renderer has been
     * assigned
     * 
     * @return The <code>ManagedUpdateScreenRendererWrapper</code> defined for
     *         the block or <code>null</code> if no update screen renderer has
     *         been assigned
     */
    public EJManagedUpdateScreenRendererWrapper getManagedUpdateScreenRenderer()
    {
        return _updateScreenRenderer;
    }

    /**
     * Returns the display properties of the insert screen on this block
     * 
     * @return The insert screen display properties or <code>null</code> if this
     *         block contains no insert screen
     */
    public EJDisplayProperties getInsertScreenDisplayProperties()
    {
        return _blockProperties.getInsertScreenRendererProperties();
    }

    /**
     * Returns the display properties of the update screen on this block
     * 
     * @return The update screen display properties or <code>null</code> if the
     *         block contains no update screen
     */
    public EJDisplayProperties getUpdateScreenDisplayProperties()
    {
        return _blockProperties.getUpdateScreenRendererProperties();
    }

    /**
     * Returns the display properties of the query screen on this block
     * 
     * @return The query screen display properties or <code>null</code> if this
     *         block contains no query screen
     */
    public EJDisplayProperties getQueryScreenDisplayProperties()
    {
        return _blockProperties.getQueryScreenRendererProperties();
    }

    /**
     * Returns the <code>IUpdateScreenRenderer</code> defined for the block or
     * <code>null</code> if no update screen renderer has been assigned
     * 
     * @return The <code>IUpdateScreenRenderer</code> defined for the block or
     *         <code>null</code> if no update screen renderer has been assigned
     */
    public EJUpdateScreenRenderer getUpdateScreenRenderer()
    {
        if (_updateScreenRenderer != null)
        {
            return _updateScreenRenderer.getUnmanagedRenderer();
        }
        return null;
    }

    protected void setUpdateScreenRenderer(EJUpdateScreenRenderer renderer)
    {
        if (renderer == null)
        {
            _updateScreenRenderer = null;
        }
        else
        {
            _updateScreenRenderer = new EJManagedUpdateScreenRendererWrapper(_frameworkManager, renderer);
        }
    }
}
