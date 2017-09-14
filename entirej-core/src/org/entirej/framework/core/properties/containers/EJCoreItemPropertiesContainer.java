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
package org.entirej.framework.core.properties.containers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.properties.EJCoreItemProperties;

public class EJCoreItemPropertiesContainer implements Serializable
{
    private List<EJCoreItemProperties> _itemProperties;
    
    public EJCoreItemPropertiesContainer()
    {
        _itemProperties = new ArrayList<EJCoreItemProperties>();
    }
    
    public boolean containsItemProperty(String name)
    {
        Iterator<EJCoreItemProperties> iti = _itemProperties.iterator();
        
        while (iti.hasNext())
        {
            EJCoreItemProperties props = iti.next();
            if (props.getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds an
     * <code>EJCoreItemProperties<code> object to this blocks item properties store
     * 
     * @param itemProperties
     *            The Item Properties object to be added
     */
    public void addItemProperties(EJCoreItemProperties itemProperties)
    {
        if (itemProperties != null)
        {
            _itemProperties.add(itemProperties);
        }
    }
    
    /**
     * Returns the <code>EJCoreItemProperties</code> contained within this
     * container
     * 
     * @return All <code>EJCoreItemProperties</code> contained within this
     *         container
     */
    public Collection<EJCoreItemProperties> getAllItemProperties()
    {
        return _itemProperties;
    }
    
    /**
     * Indicates if there is an item in this container with the given name
     * 
     * @param itemName
     *            The name of the item to check for
     * @return <code>true</code> if the item exists otherwise <code>false</code>
     */
    public boolean contains(String itemName)
    {
        Iterator<EJCoreItemProperties> iti = _itemProperties.iterator();
        
        while (iti.hasNext())
        {
            EJCoreItemProperties item = iti.next();
            
            if (item.getName().equalsIgnoreCase(itemName))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns the <code>ItemProperties</code> for the given item name or
     * <code>null</code> if there is no item with the given name
     * 
     * @param itemName
     *            The name of the item to search for
     * @return The properties of the given item or <code>null</code> if an
     *         invalid or nonexistent item name was passed
     */
    public EJCoreItemProperties getItemProperties(String itemName)
    {
        if (itemName == null)
        {
            return null;
        }
        
        Iterator<EJCoreItemProperties> props = _itemProperties.iterator();
        
        while (props.hasNext())
        {
            EJCoreItemProperties item = props.next();
            
            if (item.getName().equalsIgnoreCase(itemName))
            {
                return item;
            }
        }
        return null;
    }
}
