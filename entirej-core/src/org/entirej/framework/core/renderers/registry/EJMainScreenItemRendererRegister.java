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
package org.entirej.framework.core.renderers.registry;

import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJBlockController;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.interfaces.EJItemGroupProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.renderers.EJManagedItemRendererWrapper;
import org.entirej.framework.core.renderers.interfaces.EJItemRenderer;

public class EJMainScreenItemRendererRegister extends EJBlockItemRendererRegister
{
    public EJMainScreenItemRendererRegister(EJBlockController block)
    {
        super(block);
    }
    
    public EJScreenType getScreenType()
    {
        return EJScreenType.MAIN;
    }
    
    
    /**
     * Used to clear and reset the register
     * <p>
     * All registered items will be removed from the register
     */
    public void resetRegister()
    {
        for (EJManagedItemRendererWrapper wrapper : getRegisteredRenderers())
        {
            EJScreenItemController item = getBlockController().getScreenItem(EJScreenType.MAIN, wrapper.getRegisteredItemName());
            item.removeItemFocusListener(this);
            item.removeItemValueChangedListener(this);
        }
        
        super.resetRegister();
    }
    
    /**
     * Sets each registered item renderers display properties according to its
     * <code>MainScreenItemProperties</code> values
     * 
     */
    public void initialiseRegisteredRenderers()
    {
        for (EJItemGroupProperties itemGroup : getBlockController().getProperties().getScreenItemGroupContainer(EJScreenType.MAIN).getAllItemGroupProperties())
        {
            for (EJScreenItemProperties itemProperties : itemGroup.getAllItemProperties())
            {
                enableItem(itemProperties);
            }
        }
    }
    
    /**
     * Enables an item according to its properties
     * 
     * @param itemProps
     *            The items properties
     */
    public void enableItem(EJScreenItemProperties itemProps)
    {
        super.enableItem(EJScreenType.MAIN, itemProps);
    }
    
    /**
     * Sets the enabled record and the correct item renderer to the given value
     * without performing item validation if the item has an LOV
     * 
     * @param itemName
     *            The name of the item who's value will be set
     * @param value
     *            The value to set
     * @throws NullPointerException
     *             if the itemName passed is either missing or null
     * @throws IllegalArgumentException
     *             if there is no item in the enabled record with the given name
     *             or there is no item renderer registered for the given item
     */
    protected void setItemValueNoValidate(String itemName, Object value)
    {
        super.setItemValueNoValidate(EJScreenType.MAIN, itemName, value);
    }
    
    /**
     * Sets the enabled record and the correct item renderer to the given value
     * 
     * @param itemName
     *            The name of the item who's value will be set
     * @param value
     *            The value to set
     * @throws NullPointerException
     *             if the itemName passed is either missing or null
     * @throws IllegalArgumentException
     *             if there is no item in the enabled record with the given name
     *             or there is no item renderer registered for the given item
     */
    protected void setItemValue(String itemName, Object value)
    {
        super.setItemValue(EJScreenType.MAIN, itemName, value);
    }
    
    @Override
    public boolean screenItemValueChanged(EJScreenItemController item, EJItemRenderer changedRenderer, Object newValue)
    {
        boolean handled =  super.screenItemValueChanged(item, changedRenderer, newValue);
        if(!handled)
        {
            try
            {
                EJDataRecord record = getRegisteredRecord().copy();
                record.setValue(item.getName(), newValue);
                validateItem(changedRenderer, item.getScreenType(), new EJRecord(record));
                setItemValueNoValidate(item.getScreenType(), item.getName(), newValue);
                postItemChanged(changedRenderer, item.getScreenType());
            }
            finally
            {
                fireValueChanged(item, changedRenderer, newValue);
            }
        }
        return true;
    }
    
    /**
     * Registers a given renderer for the specified item
     * <p>
     * Once an item has its renderer registered then all synchronization between
     * the blocks underlying data and the renderer will be handled by the
     * <code>EntireJCoreFramework</code>
     * <p>
     * The register will be informed when a renderer gains focus, the register
     * will then inform the underlying block that it has gained focus
     * 
     * @param renderer
     *            The renderer to register
     * @param item
     *            The renderer will be registered for this item controller
     * @throws NullPointerException
     *             if the renderer is null or the itemName is either null or of
     *             zero length
     */
    public void registerRendererForItem(EJItemRenderer renderer, EJScreenItemController item)
    {
        if (item == null)
        {
            throw new NullPointerException("The Item passed to registerRendererForItem is null.");
        }
        
        if (renderer == null)
        {
            throw new NullPointerException("The renderer passed to registerRendererForItem is null. Item: " + item.getName());
        }
        
        super.registerRendererForItem(renderer, item);
        
        item.addItemFocusListener(this);
        item.addItemValueChangedListener(this);
    }
    
    public boolean fireLovValidate(EJScreenItemController item, Object newValue)
    {
        return item.getItemLovController().validateItem(newValue);
    }
    
}
