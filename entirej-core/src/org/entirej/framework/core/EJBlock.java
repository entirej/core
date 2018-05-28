/*******************************************************************************
 * Copyright 2013 CRESOFT AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors: CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.entirej.framework.core.data.EJDataItem;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJBlockController;
import org.entirej.framework.core.data.controllers.EJItemController;
import org.entirej.framework.core.enumerations.EJRecordType;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.internal.EJDefaultServicePojoHelper;
import org.entirej.framework.core.internal.EJInternalEditableBlock;
import org.entirej.framework.core.properties.EJCoreItemProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJDisplayProperties;
import org.entirej.framework.core.service.EJQueryCriteria;

public class EJBlock implements EJQueryBlock, Serializable
{
    private EJInternalEditableBlock _block;

    public EJBlock(EJInternalEditableBlock block)
    {
        _block = block;
    }

    /**
     * Return the form to which this block belongs
     * 
     * @return This form to which this block belongs
     */
    public EJForm getForm()
    {
        return new EJForm(_block.getForm());
    }

    /**
     * Returns the name of this blocks canvas, or <code>null</code> if the block
     * is not displayed on a canvas
     * 
     * @return the name of this blocks canvas or <code>null</code> if this block
     *         is not displayed
     */
    public String getCanvasName()
    {
        return _block.getProperties().getCanvasName();
    }

    public Collection<EJBlockItem> getBlockItems()
    {
        ArrayList<EJBlockItem> blockItems = new ArrayList<EJBlockItem>();

        for (EJItemController controller : _block.getAllBlockItemControllers())
        {
            blockItems.add(new EJBlockItem(controller.getProperties()));
        }

        return blockItems;
    }

    public EJBlockItem getBlockItem(String itemName)
    {
        if (_block.getProperties().getItemProperties(itemName) == null)
        {
            throw new IllegalArgumentException("There is no item called " + itemName + " on block " + _block.getProperties().getName());
        }

        return new EJBlockItem(_block.getProperties().getItemProperties(itemName));
    }

    /**
     * Indicates if this block is a control block
     * 
     * @return <code>true</code> if this block is a control block, otherwise
     *         <code>false</code>
     */
    public boolean isControlBlock()
    {
        return _block.getProperties().isControlBlock();
    }

    /**
     * Indicates if the block has an item with the given name
     * 
     * @param itemName
     *            the name to check for
     * @return <code>true</code> if the item exists within the block, otherwise
     *         <code>false</code>
     * 
     */
    public boolean containsItem(String itemName)
    {
        EJCoreItemProperties itemProps = _block.getProperties().getItemProperties(itemName);
        if (itemProps == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns the name of this block
     * 
     * @return This blocks name
     */
    public String getName()
    {
        if(_block.getProperties().isUsedInLovDefinition())
        {
           return _block.getProperties().getLovDefinition().getName();
        }
        return _block.getProperties().getName();
    }

    /**
     * Creates an empty query criteria for this block
     * <p>
     * The criteria can be used for executing a query within this block.
     * Criterion can be added to restrict the query
     * 
     * @return
     */
    public EJQueryCriteria createQueryCriteria()
    {
        return _block.createQueryCriteria();
    }

    /**
     * Used to create an empty record for this block
     * <p>
     * The whenCreateRecord within the blocks processor will be fired once the
     * record has been created
     * 
     * @return The newly created record
     */
    public EJRecord createRecord()
    {
        return new EJRecord(_block.createRecord(EJRecordType.INSERT));
    }

    /**
     * Used to create an empty record for this block without firing the
     * whenCreateRecord action within the blocks action processor
     * <p>
     * 
     * @return The newly created record
     */
    public EJRecord createRecordNoAction()
    {
        return new EJRecord(_block.createRecordNoAction());
    }

    /**
     * Clears this blocks data
     * <p>
     * Only this controllers underlying data block will be cleared, if the block
     * is master in a master-detail relationship then its detail blocks will be
     * left untouched. To clear all detail blocks use the
     * {@link EJBlock#clearAllDetailRelations(boolean))}
     * 
     * @param clearChanges
     *            Indicates if the dirty records contained within this block
     *            should also be cleared
     * @see EJBlockController#clearAllDetailRelations()
     */
    public void clear(boolean clearChanges)
    {
        _block.clear(clearChanges);
    }

    /**
     * Clears all detail blocks if this block is a master in a master detail
     * relationship
     * <p>
     * This blocks underlying data block will be left untouched. To clear this
     * block use the {@link EJBlock#clear()}
     * 
     * @param clearChanges
     *            Indicates if the dirty records contained within the detail
     *            blocks should also be cleared
     * 
     * @see EJBlock#clear()
     */
    public void clearAllDetailRelations(boolean clearChanges)
    {
        _block.clearAllDetailRelations(clearChanges);
    }

    /**
     * Informs the block renderer that the user wishes to delete the current
     * record. This method will display a standard message.
     * <p>
     * If you want to add your own message
     * {@link #askToDeleteCurrentRecord(String)}
     * <p>
     * The renderer can display a question that asks if the record should be
     * deleted or not.
     */
    public void askToDeleteCurrentRecord()
    {
        _block.askToDeleteCurrentRecord(null);
    }

    /**
     * Informs the block renderer that the user wishes to delete the current
     * record and displays the message passed.
     * <p>
     * Use the following translate methods if you are using your own Translator
     * in a multilingual application to translate the message before sending it
     * to this method
     * 
     * <ul>
     * <li>{@link EJForm#translateMessageText(String)}</li>
     * <li>{@link EJForm#translateText(String)}</li>
     * </ul>
     * 
     * <p>
     * The renderer can display a question that asks if the record should be
     * deleted or not.
     */
    public void askToDeleteCurrentRecord(String message)
    {
        _block.askToDeleteCurrentRecord(message);
    }

    /**
     * Instructs EntireJ to delete the given record
     * <p>
     * The record will be passed to the framework for deletion. The record will
     * be removed from the blocks list of records and added to the dirty list
     * and will be sent to the blocks service for deletion when the user saves
     * the form
     * 
     * @param record
     *            The record to be deleted
     */
    public void deleteRecord(EJRecord record)
    {
        _block.deleteRecord(record.getDataRecord());
    }

    /**
     * Instructs EntireJ to insert the given record into its block
     * 
     * @param insertRecord
     *            The record to be inserted
     */
    public void insertRecord(EJRecord insertRecord)
    {
        _block.insertRecord(insertRecord.getDataRecord());
    }

    /**
     * Instructs EntireJ to update the given record
     * <p>
     * 
     * @param updateRecord
     *            The record to be inserted
     */
    public void updateRecord(EJRecord updateRecord)
    {
        _block.updateRecord(updateRecord.getDataRecord());
    }

    /**
     * Instructs EntireJ to put this block into insert mode
     * <p>
     * The result of this method is dependent upon how the block renderer puts
     * the form in a state ready for the user to enter data to create a new
     * record
     * 
     * @param copyCurrentValues
     *            If set to true, this indicates to the block controller that a
     *            new record should be created as a copy of the current record.
     *            This can be used for duplicate record functionality
     * 
     */
    public void enterInsert(boolean copyCurrentValues)
    {
        _block.enterInsert(copyCurrentValues);
    }

    /**
     * Instructs EntireJ to put this block into update mode
     * <p>
     * The result of this method is dependent upon how the block renderer puts
     * the form in a state ready for the user to update data from the current
     * record. An exception is thrown if this method is called before there is a
     * current record for the given block
     */
    public void enterUpdate()
    {
        _block.enterUpdate();
    }

    /**
     * Instructs EntireJ to put this block into query mode
     * <p>
     * The result of this method is dependent upon how the block renderer puts
     * the form in a state ready for the user to enter data for a new query
     */
    public void enterQuery()
    {
        _block.enterQuery();
    }

    /**
     * Instructs EntireJ to perform a query on the given block using no query
     * criteria
     * <p>
     * The block will create an empty {@link EJQueryCriteria}
     */
    public void executeQuery()
    {
        _block.executeQuery(createQueryCriteria());
    }
    
    /**
     * Instructs EntireJ to perform a query on the given block using no query
     * criteria in background thread 
     * <p>
     * The block will create an empty {@link EJQueryCriteria}
     */
    public void executeQueryAsync()
    {
        _block.executeQueryAsync(createQueryCriteria());
    }

    /**
     * Instructs EntireJ to perform a query on the given block using the
     * specified criteria
     * 
     * @param queryCriteria
     *            The criteria for the query
     */
    public void executeQuery(EJQueryCriteria queryCriteria)
    {
        if (queryCriteria.getBlock() == null)
        {
            queryCriteria.setBlock(this);
        }
        _block.executeQuery(queryCriteria);
    }
    
    /**
     * Instructs EntireJ to perform a query on the given block using the
     * specified criteria in background thread
     * 
     * @param queryCriteria
     *            The criteria for the query
     */
    public void executeQueryAsync(EJQueryCriteria queryCriteria)
    {
        if (queryCriteria.getBlock() == null)
        {
            queryCriteria.setBlock(this);
        }
        _block.executeQueryAsync(queryCriteria);
    }

    /**
     * If paging has been enabled for this block, then this method will inform
     * EntireJ that the user wishes to retrieve the next page of data for the
     * block
     * <p>
     * <b><i>If the block is not set to query in pages then this method does
     * nothing</i></b>
     */
    public void nextPage()
    {
        if (_block.canQueryInPages() && (!_block.getProperties().queryAllRows()))
        {
            if (_block.hasMorePages())
            {
                _block.nextPage();
            }
        }
    }

    /**
     * If paging has been enabled for this block, then this method will inform
     * EntireJ that the user wishes to retrieve the previous page of data for
     * the block
     * <p>
     * <b><i>If the block is not set to query in pages or the first page is
     * being shown then this method does nothing</i></b>
     */
    public void previousPage()
    {
        if (_block.canQueryInPages() && (!_block.getProperties().queryAllRows()))
        {
            if (!_block.isOnFirstPage())
            {
                _block.previousPage();
            }
        }
    }

    /**
     * Returns the last query criteria that was used for the query of the block
     * 
     * @return The last used QueryCriteria or <code>null</code> if no query has
     *         yet been made
     */
    public EJQueryCriteria getLastQueryCriteria()
    {
        return _block.getLastQueryCriteria();

    }

    /**
     * Instructs EntireJ to re-query this block using the query criteria
     * previously entered
     */
    public void executeLastQuery()
    {
        _block.executeLastQuery();
    }
    
    /**
     * Instructs EntireJ to re-query this block using the query criteria
     * previously entered in backgound thread
     */
    public void executeLastQueryAsync()
    {
        _block.executeLastQueryAsync();
    }

    /**
     * Forces this block to gain focus
     */
    public void gainFocus()
    {
        _block.focusGained();
    }

    /**
     * Retrieves the focused record for the given block
     * 
     * @param blockName
     *            The name of the block
     * @return The focused record of the given block or <code>null</code> if
     *         there is no record focused
     */
    public EJRecord getFocusedRecord()
    {
        EJDataRecord record = _block.getFocusedRecord();
        if (record == null)
        {
            return null;
        }
        else
        {
            return new EJRecord(record);
        }
    }

    /**
     * Return the current record in the given screen
     * <p>
     * If there is no record in the screen then <code>null</code> will be
     * returned
     * 
     * @param screenType
     *            The screen for which the record will be returned
     * @return The record that is currently being displayed on the given screen,
     *         or <code>null</code> if no record is being displayed
     */
    public EJRecord getCurrentScreenRecord(EJScreenType screenType)
    {
        if (screenType == null)
        {
            throw new IllegalArgumentException("no ScreenType passed to getCurrentScreenRecord");
        }

        EJDataRecord record = null;
        switch (screenType)
        {
            case INSERT:
                if (_block.getInsertScreenRenderer() == null)
                {
                    return null;
                }
                record = _block.getInsertScreenRenderer().getInsertRecord();
                break;
            case UPDATE:
                if (_block.getUpdateScreenRenderer() == null)
                {
                    return null;
                }

                record = _block.getUpdateScreenRenderer().getUpdateRecord();
            case QUERY:
                if (_block.getUpdateScreenRenderer() == null)
                {
                    return null;
                }

                record = _block.getUpdateScreenRenderer().getUpdateRecord();
                break;
            case MAIN:
                return getFocusedRecord();
        }

        if (record == null)
        {
            return null;
        }
        else
        {
            return new EJRecord(record);
        }
    }

    /**
     * Returns the item that is currently focused on the current record or
     * <code>null</code> if no item has focus
     * 
     * @return The item that currently has focus within the current record
     */
    public EJItem getFocusedItem()
    {
        EJDataItem item = _block.getFocusedItem();
        if (item == null)
        {
            return null;
        }

        return new EJItem(_block, _block.getFocusedRecord(), _block.getFocusedItem());
    }

    /**
     * Returns the <code>FrameworkManager</code>
     * 
     * @return The {@link EJFrameworkManager}
     */
    public EJFrameworkManager getFrameworkManager()
    {
        return _block.getFrameworkManager();
    }

    /**
     * Indicates if this block has more pages
     * <p>
     * If this block is being queried in pages, then this method will indicate
     * if the block has more pages available
     * 
     * @return <code>true</code> if the block has more pages, otherwise
     *         <code>false</code>
     */
    public boolean hasMorePages()
    {
        return _block.hasMorePages();
    }

    /**
     * Indicates if the block is currently displaying the first page of data
     * <p>
     * If this blocks data is retrieved in pages, then this method will indicate
     * if the first page is being displayed
     * 
     * @return <code>true</code> if the first page of data is being displayed,
     *         otherwise <code>false</code>
     */
    public boolean isOnFirstPage()
    {
        return _block.isOnFirstPage();
    }

    /**
     * Indicates if this block is dirty
     * <p>
     * A dirty block is a block that has had modifications. For example, it has
     * records that have been deleted, inserted or updated
     * 
     * @return <code>true</code> if the block is dirty, otherwise
     *         <code>false</code>
     */
    public boolean isDirty()
    {
        return _block.isDirty();
    }

    /**
     * Indicates if any of the child relations of this block are dirty
     * <p>
     * If this block is not a parent in a master detail relationship, then this
     * method will always return <code>false</code>
     * 
     * @return <code>true</code> if child relations are dirty
     */
    public boolean areChildRelationsDirty()
    {
        return _block.areChildRelationsDirty();
    }

    /**
     * Indicates if this block allows the user to make updates
     * 
     * @return <code>true</code> if the user can make updates, otherwise
     *         <code>false</code>
     */
    public boolean isUpdateAllowed()
    {
        return _block.isUpdateAllowed();
    }

    /**
     * Indicates if this block allows the user to make inserts
     * 
     * @return <code>true</code> if the user can make inserts, otherwise
     *         <code>false</code>
     */
    public boolean isInsertAllowed()
    {
        return _block.isInsertAllowed();
    }

    /**
     * Indicates if the user can delete records from this block
     * 
     * @return <code>true</code> if the user can delete records, otherwise
     *         <code>false</code>
     */
    public boolean isDeleteAllowed()
    {
        return _block.isDeleteAllowed();
    }

    /**
     * Indicated if this block is part of a LOV Definition
     * 
     * @return <code>true</code> if this block is part of an LOV definition,
     *         otherwise <code>false</code>
     */
    public boolean isUsedInLovDefinition()
    {
        return _block.getProperties().isUsedInLovDefinition();
    }

    /**
     * Instructs this block to navigate to the given record
     * <p>
     * If this record does not exist within this block, nothing will happen
     * 
     * @param record
     *            The record which should be navigated to
     */
    public void navigateToRecord(EJRecord record)
    {
        _block.navigateToRecord(record.getDataRecord());
    }

    /**
     * Navigates to the first record within this block
     * <p>
     * If the block has no records then no action will be performed
     */
    public void navigateToFirstRecord()
    {
        _block.navigateToFirstRecord();
    }

    /**
     * Navigates to the last record of this block
     * <p>
     * If this block has no records then no action will be performed
     */
    public void navigateToLastRecord()
    {
        _block.navigateToLastRecord();
    }

    /**
     * Set the display properties for a given block
     * <p>
     * If the property is part of a <code>propertyGroup</code> then use a dot
     * (.) Separator between the group name and the property name. e.g. If the
     * <code>xpos</code> property is in the group
     * <code>displayCoordinates</code> then the property name would be:
     * <code>displayCoordinates.xpos</code>
     * <p>
     * The name of the properties and their functionality are dependent on the
     * block renderer being used. Please see the corresponding block renderer
     * documentation for more details
     * 
     * @param propertyName
     *            The name of the property to change
     * @param propertyValue
     *            The new property value
     * @throws EJApplicationException
     *             if the given block name is invalid or if there is no display
     *             property with the given name on the given block
     */
    public void setDisplayProperty(String propertyName, String propertyValue)
    {
        _block.setDisplayProperty(propertyName, propertyValue);
    }

    /**
     * Returns the display properties for this block
     * 
     * @return The display properties for this block
     */
    public EJDisplayProperties getDisplayProperty()
    {
        return _block.getDisplayProperties();
    }

    /**
     * Used to set the blocks filter if the blocks renderer uses one
     * <p>
     * The filter on Multi Record Blocks is normally displayed over the list of
     * records and when the user types in the filter field, the list of records
     * are filtered accordingly. Use this method to set a filter
     * programmatically
     * 
     * @param filter
     *            The filter to set
     */
    public void setFilter(String filter)
    {
        _block.getManagedRenderer().setFilter(filter);
    }

    /**
     * Returns the filter that has been set on this block or <code>null</code>
     * if either none has been set or the filter is not supported by the blocks
     * renderer
     * 
     * @return The blocks filter or <code>null</code> if no filter has been set
     *         or the blocks renderer doesn't support filters
     */
    public String getFilter()
    {
        return _block.getManagedRenderer().getFilter();
    }

    /**
     * Returns the display properties for the given block item
     * 
     * @param itemName
     *            The item name
     * @return The items display properties
     * @throws EJApplicationException
     *             If there is no item on this block with the given name
     */
    public EJDisplayProperties getBlockItemDisplayProperties(String itemName)
    {
        return _block.getBlockItemDisplayProperties(itemName);
    }

    /**
     * Returns an immutable collection if IDataRecords for this block Retrieving
     * all records will force <B>EntireJ</B> to refresh the blocks records. If
     * only the current record needs to be modified, use
     * <code>setItemValue</code>
     * 
     * @return A collection of records or an empty collection if the block
     *         doesn't exist
     * @throws EJApplicationException
     *             If there is no block with the given name
     */
    public Collection<EJRecord> getBlockRecords()
    {
        ArrayList<EJRecord> records = new ArrayList<EJRecord>();
        for (EJDataRecord record : _block.getBlockRecords())
        {
            records.add(new EJRecord(record));
        }

        return Collections.unmodifiableCollection(records);
    }

    /**
     * Returns all screen items
     * 
     * @param screenType
     *            The type of screen that holds the required item
     * @param itemName
     *            The name of the required item
     * @return The required screen item
     * @throws EJApplicationException
     *             if there is no item with the specified name
     */
    public Collection<EJScreenItem> getScreenItems(EJScreenType screenType)
    {
        ArrayList<EJScreenItem> screenItems = new ArrayList<EJScreenItem>();
        for (EJScreenItemController controller : _block.getAllScreenItems(screenType))
        {
            screenItems.add(new EJScreenItem(_block, screenType, controller));
        }

        return screenItems;
    }

    /**
     * Returns the screen item with the given name
     * 
     * @param screenType
     *            The type of screen that holds the required item
     * @param itemName
     *            The name of the required item
     * @return The required screen item
     */
    public EJScreenItem getScreenItem(EJScreenType screenType, String itemName)
    {
        return new EJScreenItem(_block, screenType, _block.getScreenItem(screenType, itemName));
    }

    /**
     * If this block is used within an LOV Definition then this method will
     * return the name of the LOV Definition
     * 
     * @return the name of the LOV Definition that this block is used in, or
     *         <code>null</code> if this block is not part of an LOV Definition
     */
    public String getLovDefinitionName()
    {
        if (_block.getProperties().getLovDefinition() != null)
        {
            return _block.getProperties().getLovDefinition().getName();
        }
        else
        {
            return null;
        }
    }

    public EJDefaultServicePojoHelper getServicePojoHelper()
    {
        return _block.getServicePojoHelper();
    }

    /**
     * Used to close the given screen
     * <p>
     * The main screen cannot be closed and will be ignored if passed to this
     * method
     * 
     * @param screenType
     *            The screen type that should be closed
     */
    public void closeScreen(EJScreenType screenType)
    {
        switch (screenType)
        {
            case INSERT:
                if (_block.getInsertScreenRenderer() != null)
                {
                    _block.getInsertScreenRenderer().close();
                }
                break;
            case QUERY:
                if (_block.getQueryScreenRenderer() != null)
                {
                    _block.getQueryScreenRenderer().close();
                }
                break;
            case UPDATE:
                if (_block.getUpdateScreenRenderer() != null)
                {
                    _block.getUpdateScreenRenderer().close();
                }
                break;
            case MAIN:
                // do nothing

        }
    }

    public void nextRecord()
    {
        _block.nextRecord();
        
    }

    public void previousRecord()
    {
        _block.previousPage();
        
    }

    public void newRecordInstance(EJRecord record)
    {
        _block.newRecordInstance(record.getDataRecord() );
        
    }
}
