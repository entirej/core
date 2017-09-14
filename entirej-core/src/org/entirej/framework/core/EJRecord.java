/*******************************************************************************
 * Copyright 2013 CRESOFT AG
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
 *     CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.entirej.framework.core.data.EJDataBlock;
import org.entirej.framework.core.data.EJDataItem;
import org.entirej.framework.core.data.EJDataRecord;

public class EJRecord implements Serializable
{
    private EJDataRecord _dataRecord;

    public EJRecord(EJDataRecord dataRecord)
    {
        _dataRecord = dataRecord;
    }

    /**
     * Protected method that will return this records underlying DataRecord
     * <p>
     * This method must be kept protected so that end users cannot access it
     * 
     * @return This records underlying DataRecord
     */
    protected EJDataRecord getDataRecord()
    {
        return _dataRecord;
    }

    /**
     * Executes all list of values that are marked for Execution After Query. If
     * you populate the Block Service Item of the LOV mapping, then EJ will
     * execute the lov and all mapped values will be mapped to this record. This
     * is a short cut way of populating the values without executing a service.
     */
    public void populateWithLovValues()
    {
        _dataRecord.getBlock().getBlockController().addLovMappingValuesAfterQuery(_dataRecord);
        //update ui with record changes
        synchronize();
    }

    /**
     * This is a convenience method that returns the name of the block to which
     * this record belongs
     * 
     * @return The name of the block to which this record belongs
     */
    public String getBlockName()
    {
        return _dataRecord.getBlockName();
    }

    /**
     * Indicates if this block is used within an LOV Definition
     * 
     * @return <code>true</code> if this block is used within a lov definition,
     *         otherwise <code>false</code>
     */
    public boolean isUsedInLovDefinition()
    {
        return _dataRecord.getBlockProperties().isUsedInLovDefinition();
    }

    /**
     * Returns the name of the Lov Definition of the block that holds this
     * record
     * <p>
     * If the block holding this record is not part of an Lov Devinition then
     * this method returns <code>null</code>
     * 
     * @return The name of the lov definition that this record is part of or
     *         <code>null</code> if the block is not part of an lov definition
     */
    public String getLovDefinitionName()
    {
        if (isUsedInLovDefinition())
        {
            return _dataRecord.getBlockProperties().getLovDefinition().getName();
        }
        return null;
    }

    /**
     * Indicates if this record has been changed
     * <p>
     * The status of the record is changed by the enclosing {@link EJBlock}
     * 
     * @return True if the record has been either marked for delete, update or
     *         insert otherwise false
     */
    public boolean isDirty()
    {
        return _dataRecord.isDirty();
    }

    /**
     * Checks if a specific item name exists within the record. This can be used
     * before setting an items value so that no
     * <code>InvalidColumnNameException</code> is thrown.
     * 
     * @param itemName
     *            The item name to check for
     * @return <code>true</code> if the item exists otherwise <code>false</code>
     */
    public boolean containsItem(String itemName)
    {
        return _dataRecord.containsItem(itemName);
    }

    /**
     * Returns the {@link EJItem} from this record with the name specified
     * 
     * @param itemName
     *            The item to return
     * @return The {@link EJItem} with the given name, or <code>null</code> if
     *         there is no item within this record with the given name
     */
    public EJItem getItem(String itemName)
    {
        EJDataItem item = _dataRecord.getItem(itemName);
        if (item == null)
        {
            return null;
        }

        return new EJItem(_dataRecord.getBlock(), _dataRecord, item);
    }

    /**
     * Sets the item with the given name to the value specified
     * <p>
     * The changes are automatically synchronized to the block renderer. If
     * multiple items should be set then it is advisable to use the
     * {@link #setValue(String, Object, boolean)} passing <code>true</code> to
     * the delaySynchronization parameter. This results in the values not being
     * automatically sent to the renderer, thus less gui refreshes. After all
     * items have been set, the {@link #synchronize()} method should be called
     * 
     * @param itemName
     *            The item name to set
     * @param value
     *            The value to set
     */
    public void setValue(String itemName, Object value)
    {
        setValue(itemName, value, false);
    }

    /**
     * Sets the item with the given name to the given value
     * <p>
     * If <code>false</code> is passed to the delaySynchronize then the
     * modifications of this record will not be synchronized to the block
     * renderer and the {@link #synchronize()} method should be called after all
     * record modifications are made
     * <p>
     * Delaying synchronizing can be used to optimize performance if multiple
     * setValue() are called. If the {@link #synchronize()} is not called, then
     * the changes will not be displayed on the screen
     * 
     * @param itemName
     *            The name of the item to set
     * @param value
     *            The value to set
     * @param delaySynchronize
     *            Indicates that the block to which this record belongs should
     *            wait before the changes are propagated to the screen
     * 
     */
    public void setValue(String itemName, Object value, boolean delaySynchronize)
    {
        _dataRecord.setValue(itemName, value);
        if (!delaySynchronize)
        {
            synchronize();
        }
    }

    /**
     * This method will force the block renderer to display all changed values
     * from this record
     */
    public void synchronize()
    {
        if ((_dataRecord.getBlock().containsRecord(_dataRecord.getBaseRecord()==null ? _dataRecord : _dataRecord.getBaseRecord())))
        {
            // Inform the block renderer that an item has been changed
            if (_dataRecord.getBlock() != null)
            {
                _dataRecord.getBlock().refreshAfterChange(_dataRecord);
            }
        }
    }

    /**
     * Returns the value of the data item with the given name
     * 
     * @param itemName
     *            The name of the item for which the value is required
     * 
     * @return The value of the required item or <code><b>null</b></code> if
     *         there is no item with the specified name
     */
    public Object getValue(String itemName)
    {
        return _dataRecord.getValue(itemName);
    }

    /**
     * This returns the underlying data entity of the block
     * <p>
     * The Data Entity is the POJO that is used by the EntireJ Block Service
     * 
     * @return This record as defined by its underlying entity object
     */
    public Object getBlockServicePojo()
    {
        return _dataRecord.getServicePojo();
    }

    /**
     * Returns a <code>Collection</code> of the column names as
     * <code>String</code> objects
     * 
     * @return A <code>Collection</code> of the column names in
     *         <code>String</code> format
     */
    public Collection<String> getColumnNames()
    {
        return Collections.unmodifiableCollection(_dataRecord.getColumnNames());
    }

    /**
     * Indicates how many columns the record has
     * 
     * @return The number of columns
     */
    public int getColumnCount()
    {
        return _dataRecord.getColumnCount();
    }

    /**
     * Indicates if this record has been passed to the blocks
     * {@link EJDataBlock#recordUpdated(EJDataRecord)}
     * 
     * @return <code>true</code> if the records has been updated otherwise
     *         <code>false</code>
     */
    public boolean isMarkedForUpdate()
    {
        return _dataRecord.isMarkedForUpdate();
    }

    /**
     * Indicates if this record has been changed
     * 
     * @return <code>true</code> if an items value has been changed otherwise
     *         <code>false</code>
     */
    public boolean isChanged()
    {
        return _dataRecord.isChanged();
    }

    /**
     * Indicates if this record has been passed to the blocks
     * {@link EJDataBlock#recordCreated(EJDataRecord, int)}
     * 
     * @return <code>true</code> if the records has been marked for insert,
     *         otherwise <code>false</code>
     */
    public boolean isMarkedForInsert()
    {
        return _dataRecord.isMarkedForInsert();
    }

    /**
     * Indicates if this record has been passed to the blocks
     * {@link EJDataBlock#recordDeleted(EJDataRecord)}
     * 
     * @return <code>true</code> if the records has been marked for insert,
     *         otherwise <code>false</code>
     */
    public boolean isMarkedForDelete()
    {
        return _dataRecord.isMarkedForDelete();
    }

    /**
     * Indicates if the record has been retrieved from a datasource
     * 
     * @return <code>true</code> if the record was returned from a datasource
     *         otherwise <code>false</code>
     */
    public boolean isMarkedAsQueried()
    {
        return _dataRecord.isMarkedAsQueried();
    }

    public EJRecord copyValuesToRecord(EJRecord record)
    {
        return new EJRecord(_dataRecord.copyValuesToRecord(record.getDataRecord()));
    }

    /**
     * If the entity object is of the same type as defined for this record, then
     * all values from the given object will be copied to this record
     * 
     * @param entityObject
     *            The object containing the values to copy
     */
    public void copyValuesFromEntityObject(Object entityObject)
    {
        _dataRecord.copyValuesFromEntityObject(entityObject);
    }

    /**
     * Returns a copy of this record
     * 
     * @return The new record
     */
    public EJDataRecord copy()
    {
        return _dataRecord.copy();
    }
}
