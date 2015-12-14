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
package org.entirej.framework.core.renderers.registry;

import java.util.Iterator;

import org.entirej.framework.core.data.controllers.EJBlockController;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.interfaces.EJItemGroupProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.renderers.EJManagedItemRendererWrapper;
import org.entirej.framework.core.renderers.interfaces.EJItemRenderer;

public class EJInsertScreenItemRendererRegister extends EJBlockItemRendererRegister
{
    public EJInsertScreenItemRendererRegister(EJBlockController block)
    {
        super(block);
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
            EJScreenItemController item = getBlockController().getScreenItem(EJScreenType.INSERT, wrapper.getRegisteredItemName());
            item.removeItemFocusListener(this);
            item.removeItemValueChangedListener(this);
        }
        
        super.resetRegister();
    }
    
    public void screenItemValueChanged(EJScreenItemController item, EJItemRenderer changedRenderer)
    {
        super.screenItemValueChanged(item, changedRenderer);
        
        try
        {
            validateItem(changedRenderer, item.getScreenType());
        }
        finally
        {
            fireValueChanged(item, changedRenderer);
        }
    }
    
    /**
     * Sets each registered item renderers display properties according to its
     * <code>InsertScreenItemProperties</code> values
     * 
     */
    public void initialiseRegisteredRenderers()
    {
        Iterator<EJItemGroupProperties> itemGroups = getBlockController().getProperties().getScreenItemGroupContainer(EJScreenType.INSERT)
                .getAllItemGroupProperties().iterator();
        while (itemGroups.hasNext())
        {
            Iterator<EJScreenItemProperties> itemProperties = itemGroups.next().getAllItemProperties().iterator();
            while (itemProperties.hasNext())
            {
                enableItem(itemProperties.next());
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
        super.enableItem(EJScreenType.INSERT, itemProps);
    }
    
    /**
     * Sets the enabled record and the correct item renderer to the given value
     * without performing item validation if the item has an lov
     * 
     * @param itemName
     *            The name of the item whos value will be set
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
        super.setItemValueNoValidate(EJScreenType.INSERT, itemName, value);
    }
    
    /**
     * Sets the enabled record and the correct item renderer to the given value
     * 
     * @param itemName
     *            The name of the item whos value will be set
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
        super.setItemValue(EJScreenType.INSERT, itemName, value);
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
            throw new NullPointerException("The item passed to registerRendererForItem is null.");
        }
        if (renderer == null)
        {
            throw new NullPointerException("The renderer passed to registerRendererForItem is null. Item: " + item.getName());
        }
        
        super.registerRendererForItem(renderer, item);
        
        item.addItemFocusListener(this);
        item.addItemValueChangedListener(this);
    }
    
    public void fireLovValidate(EJScreenItemController item, Object value)
    {
        item.getItemLovController().validateItem(value);
    }
    
}
