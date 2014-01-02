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
package org.entirej.framework.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJItemController;
import org.entirej.framework.core.enumerations.EJRecordType;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.internal.EJDefaultServicePojoHelper;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.properties.EJCoreItemProperties;
import org.entirej.framework.core.service.EJQueryCriteria;

public class EJLovBlock implements EJQueryBlock, Serializable
{
    private EJInternalBlock _block;
    
    public EJLovBlock(EJInternalBlock block)
    {
        _block = block;
    }
    
    public String getName()
    {
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
    public EJDataRecord createRecord()
    {
        return _block.createRecord(EJRecordType.INSERT);
    }
    
    /**
     * Used to create an empty record for this block without firing the
     * whenCreateRecord action within the blocks action processor
     * <p>
     * 
     * @return The newly created record
     */
    public EJDataRecord createRecordNoAction()
    {
        return _block.createRecordNoAction();
    }
    
    /**
     * Instructs EntireJ to perform a query on the given block using no query
     * criteria
     * <p>
     * The block will create an empty <code>EJQueryCriteria</code>
     */
    public void executeQuery()
    {
        _block.executeQuery(createQueryCriteria());
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
        _block.executeQuery(queryCriteria);
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
     * Retrieves the focused record for the given block
     * 
     * @param blockName
     *            The name of the block
     * @return The focused record of the given block or <code>null</code> if
     *         there is no focused record
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
     * Returns the framework manager
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
     * Returns the screen item with the given name
     * 
     * @param screenType
     *            The type of screen that holds the required item
     * @param itemName
     *            The name of the required item
     * @return The required screen item
     * @throws EJApplicationException
     *             if there is no item with the specified name
     */
    public EJScreenItem getScreenItem(EJScreenType screenType, String itemName)
    {
        return new EJScreenItem(_block, screenType, _block.getScreenItem(screenType, itemName));
    }
    
    public EJBlockItem getBlockItem(String itemName)
    {
        if (_block.getProperties().getItemProperties(itemName) == null)
        {
            throw new IllegalArgumentException("There is no item called " + itemName + " on block " + _block.getProperties().getName());
        }
        
        return new EJBlockItem(_block.getProperties().getItemProperties(itemName));
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
    
    public EJDefaultServicePojoHelper getServicePojoHelper()
    {
        return _block.getServicePojoHelper();
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
}
