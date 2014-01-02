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
package org.entirej.framework.core.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.data.EJDataBlock;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJEditableBlockController;
import org.entirej.framework.core.data.controllers.EJEditableBlockRendererController;
import org.entirej.framework.core.data.controllers.EJItemController;
import org.entirej.framework.core.data.controllers.EJManagedEditableBlockRendererController;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJDisplayProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJItemProperties;

public class EJInternalEditableBlock extends EJInternalBlock implements Serializable
{
    private EJEditableBlockController _blockController;
    
    public EJInternalEditableBlock(EJEditableBlockController blockController)
    {
        super(blockController);
        _blockController = blockController;
    }
    
    /**
     * Returns the properties of this block
     * 
     * @return This blocks properties
     */
    public EJCoreBlockProperties getProperties()
    {
        return _blockController.getProperties();
    }
    
    /**
     * Informs this block that it has gained focus
     * <p>
     * It is the responsibility of the block renderer to call this method when
     * it gains focus. This will inform EntireJ of the newly focused block
     * enabling EntireJ to forward, query, insert, update, delete etc.
     * operations to the correct block
     */
    public void focusGained()
    {
        _blockController.focusGained();
    }
    
    /**
     * Informs this block that it has lost focus
     * <p>
     * It is the responsibility of the block renderer to call this method when
     * it loses focus
     */
    public void focusLost()
    {
        _blockController.focusLost();
    }
    
    /**
     * Returns the managed block renderer for this block
     * <p>
     * A managed renderer is a wrapper around the actual block renderer and will
     * catch all thrown exceptions allowing the developer to concentrate on
     * actual code without having to worry about catching exceptions thrown by
     * the renderer.
     * <p>
     * If there is a need to catch the renderer exceptions and to act upon them,
     * then the Un-Managed renderer can be retrieved from the managed renderer.
     * Using the Un-Managed renderer will involve the need to catch exceptions
     * thrown by the renderer implementation
     * 
     * @return This blocks managed renderer
     */
    public EJManagedEditableBlockRendererController getManagedRenderer()
    {
        return _blockController.getManagedRendererController();
    }
    
    /**
     * Returns the renderer for this block
     * <p>
     * It is also possible to retrieve the managed renderer. The managed
     * renderer will ensure all exceptions are handled allowing the developer to
     * concentrate on his own code only catching the exceptions if and when
     * needed
     * 
     * @return This blocks managed renderer
     */
    public EJEditableBlockRendererController getRendererController()
    {
        if (getManagedRenderer() != null)
        {
            return _blockController.getRendererController();
        }
        else
        {
            return null;
        }
        
    }
    
    /**
     * Clears all detail blocks if this block is a master in a master detail
     * relationship
     * <p>
     * This blocks underlying data block will be left untouched. To clear this
     * block use the {@link EditableBlock#clear()}
     * 
     * @param clearChanges
     *            Indicates if the dirty records contained within the detail
     *            blocks should also be cleared
     * 
     * @see EditableBlock#clear()
     */
    public void clearAllDetailRelations(boolean clearChanges)
    {
        _blockController.clearAllDetailRelations(clearChanges);
    }
    
    /**
     * Informs the block renderer that the user wishes to delete the current
     * record
     * <p>
     * The renderer can display a question that asks if the record should be
     * deleted or not.
     * 
     */
    public void askToDeleteCurrentRecord(String message)
    {
        _blockController.askToDeleteCurrentRecord(message);
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
    public void deleteRecord(EJDataRecord record)
    {
        if (record == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_RECORD_PASSED_TO_METHOD,
                    "FormManager.deleteRecord"));
        }
        
        _blockController.executeDelete((EJDataRecord) record);
    }
    
    /**
     * Instructs EntireJ to insert the given record into its block
     * 
     * @param insertRecord
     *            The record to be inserted
     */
    public void insertRecord(EJDataRecord insertRecord)
    {
        if (insertRecord == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_RECORD_PASSED_TO_METHOD, "executeInsert"));
        }
        
        _blockController.executeInsert(insertRecord);
    }
    
    /**
     * Instructs EntireJ to update the given record
     * <p>
     * 
     * @param updateRecord
     *            The record to be inserted
     */
    public void updateRecord(EJDataRecord updateRecord)
    {
        if (updateRecord == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_RECORD_PASSED_TO_METHOD, "executeUpdate"));
        }
        
        _blockController.executeUpdate(updateRecord);
    }
    
    /**
     * Indicates to EntireJ that an update was cancelled by the user
     * <p>
     * EntireJ will inform the developer that the update was cancelled so that
     * they can perform any post-update-cancel operations
     */
    public void updateCancelled()
    {
        _blockController.updateCancelled();
    }
    
    /**
     * Indicates to EntireJ that an insert was cancelled by the user
     * <p>
     * EntireJ will inform the developer that the insert was cancelled so that
     * they can perform any post-insert-cancel operations
     */
    public void insertCancelled()
    {
        _blockController.insertCancelled();
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
        _blockController.enterInsert(copyCurrentValues);
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
        if (_blockController.getFocusedRecord() != null)
        {
            _blockController.enterUpdate();
        }
        else
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.CANNOT_UPDATE_A_BLOCK_WITH_NO_RECORD,
                    _blockController.getProperties().getName()));
        }
    }
    
    /**
     * Instructs EntireJ to put this block into query mode
     * <p>
     * The result of this method is dependent upon how the block renderer puts
     * the form in a state ready for the user to enter data for a new query
     */
    public void enterQuery()
    {
        _blockController.enterQuery();
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
        return _blockController.isBlockDirty();
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
        return _blockController.areChildRelationsDirty();
    }
    
    /**
     * Indicates if this block allows the user to make updates
     * 
     * @return <code>true</code> if the user can make updates, otherwise
     *         <code>false</code>
     */
    public boolean isUpdateAllowed()
    {
        return _blockController.getProperties().isUpdateAllowed();
    }
    
    /**
     * Indicates if this block allows the user to make inserts
     * 
     * @return <code>true</code> if the user can make inserts, otherwise
     *         <code>false</code>
     */
    public boolean isInsertAllowed()
    {
        return _blockController.getProperties().isInsertAllowed();
    }
    
    /**
     * Indicates if the user can delete records from this block
     * 
     * @return <code>true</code> if the user can delete records, otherwise
     *         <code>false</code>
     */
    public boolean isDeleteAllowed()
    {
        return _blockController.getProperties().isDeleteAllowed();
    }
    
    /**
     * Indicates if a block prevents masterless operations
     * <p>
     * Masterless operations are operations on detail blocks of a master detail
     * relationship when there is no master. If preventMasterlessOperations is
     * <code>true</code> then it will, for example, not be possible to query
     * records in a detail block until the master block has queried records. Or
     * it will not be possible to insert a new record into a detail block until
     * a master record is available
     * 
     * @return <code>true</code> if masterless operations are prevented,
     *         otherwise <code>false</code>
     */
    public boolean preventMasterlessOperations()
    {
        return _blockController.preventMasterlessOperations();
    }
    
    /**
     * Instructs this block to navigate to the given record
     * <p>
     * If this record does not exist within this block, nothing will happen
     * 
     * @param record
     *            The record which should be navigated to
     */
    public void navigateToRecord(EJDataRecord record)
    {
        if (record == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_RECORD_PASSED_TO_METHOD,
                    "FormManager.navigateToRecord"));
        }
        
        _blockController.navigateToRecord((EJDataRecord) record);
    }
    
    /**
     * Navigates to the first record within this block
     * <p>
     * If the block has no records then no action will be performed
     */
    public void navigateToFirstRecord()
    {
        _blockController.navigateToFirstRecord();
    }
    
    /**
     * Navigates to the last record of this block
     * <p>
     * If this block has no records then no action will be performed
     */
    public void navigateToLastRecord()
    {
        _blockController.navigateToLastRecord();
    }
    
    /**
     * Returns the item properties for the given item
     * 
     * @param itemName
     *            The item name
     * @return The properties of the given item
     */
    public EJItemProperties getItemProperties(String itemName)
    {
        EJItemController blockItem = _blockController.getBlockItemController(itemName);
        if (blockItem == null)
        {
            EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_ITEM_NAME_PASSED_TO_METHOD, "getItemProperties");
            throw new EJApplicationException(message);
        }
        
        return blockItem.getProperties();
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
        if (hasValue(propertyName) && hasValue(propertyValue))
        {
            EJFrameworkExtensionProperties blockRendererProperties = _blockController.getProperties().getBlockRendererProperties();
            if (blockRendererProperties == null)
            {
                EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_RETRIEVE_BLOCK_RENDERER_PROPERTY,
                        _blockController.getProperties().getName(), _blockController.getFormController().getProperties().getName());
                throw new EJApplicationException(message);
            }
            
            blockRendererProperties.setPropertyValue(propertyName, propertyValue);
            
            EJManagedEditableBlockRendererController blockRendererController = _blockController.getManagedRendererController();
            
            if (blockRendererController != null) blockRendererController.refreshBlockRendererProperty(propertyName);
        }
    }
    
    /**
     * Returns the display properties for this block or <code>null</code> if no
     * renderer properties are available
     * 
     * @return The display properties for the given block
     */
    public EJDisplayProperties getDisplayProperties()
    {
        return _blockController.getProperties().getBlockRendererProperties();
    }
    
    /**
     * Returns the display properties for the given block item
     * 
     * @param blockName
     *            The name of the block
     * @param itemName
     *            The item name
     * @return The items display properties
     */
    public EJDisplayProperties getBlockItemDisplayProperties(String itemName)
    {
        EJItemController blockItem = _blockController.getBlockItemController(itemName);
        if (blockItem == null)
        {
            EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_FIND_ITEM_ON_BLOCK, itemName,
                    _blockController.getProperties().getName());
            throw new EJApplicationException(message);
        }
        
        EJDisplayProperties itemRendererProperties = blockItem.getProperties().getItemRendererProperties();
        if (itemRendererProperties == null)
        {
            EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_RETRIEVE_ITEM_RENDERER_PROPERTY,
                    _blockController.getProperties().getName(), _blockController.getFormController().getProperties().getName());
            throw new EJApplicationException(message);
        }
        
        return itemRendererProperties;
    }
    
    private boolean hasValue(String value)
    {
        if (value == null || value.trim().length() == 0)
        {
            return false;
        }
        
        return true;
    }
    
    /**
     * Returns an immutable collection if IDataRecords for this block Retrieving
     * all records will force <B>EntireJ</B> to refresh the blocks records. If
     * only the current record needs to be modified, use
     * <code>setItemValue</code>
     * 
     * @return A collection of records or an empty collection if the block
     *         doesn't exist
     */
    public Collection<EJDataRecord> getBlockRecords()
    {
        EJDataBlock block = _blockController.getDataBlock();
        if (block != null)
        {
            return Collections.unmodifiableCollection(block.getRecords());
        }
        else
        {
            return Collections.unmodifiableCollection(new ArrayList<EJDataRecord>());
        }
    }
    
    /**
     * Used to indicate if delete operations are allowed on this block
     * 
     * @param allowed
     *            <code>true</code> if delete is allowed otherwise
     *            <code>false</code>
     */
    public void setDeleteAllowed(boolean allowed)
    {
        _blockController.getProperties().setDeleteAllowed(allowed);
    }
    
    /**
     * Used to indicate if insert operations are allowed on this block
     * 
     * @param allowed
     *            <code>true</code> if delete is allowed otherwise
     *            <code>false</code>
     */
    public void setInsertAllowed(boolean allowed)
    {
        _blockController.getProperties().setInsertAllowed(allowed);
    }
    
    /**
     * Used to indicate if update operations are allowed on this block
     * 
     * @param allowed
     *            <code>true</code> if delete is allowed otherwise
     *            <code>false</code>
     */
    public void setUpdateAllowed(boolean allowed)
    {
        _blockController.getProperties().setUpdateAllowed(allowed);
    }
    
    /**
     * Used to indicate if query operations are allowed on this block
     * 
     * @param allowed
     *            <code>true</code> if query is allowed otherwise
     *            <code>false</code>
     */
    public void setQueryAllowed(boolean allowed)
    {
        _blockController.getProperties().setQueryAllowed(allowed);
    }
}
