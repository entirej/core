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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.properties.EJCoreItemGroupProperties;
import org.entirej.framework.core.properties.containers.interfaces.EJItemGroupPropertiesContainer;
import org.entirej.framework.core.properties.interfaces.EJItemGroupProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;

public class EJCoreItemGroupPropertiesContainer implements EJItemGroupPropertiesContainer
{
    private int                         _containerType;
    private List<EJItemGroupProperties> _itemGroups;
    
    public EJCoreItemGroupPropertiesContainer(int containerType)
    {
        _containerType = containerType;
        _itemGroups = new ArrayList<EJItemGroupProperties>();
    }
    
    public int getContainerType()
    {
        return _containerType;
    }
    
    public int count()
    {
        return _itemGroups.size();
    }
    
    /**
     * Indicates if there is an item group in this container with the given name
     * 
     * @param itemGroupName
     *            The name of the item group to check for
     * 
     * @return true if the item group exists otherwise false
     */
    public boolean containsItemGroup(String itemGroupName)
    {
        Iterator<EJItemGroupProperties> iti = _itemGroups.iterator();
        
        while (iti.hasNext())
        {
            EJItemGroupProperties props = iti.next();
            if (props.getName().equalsIgnoreCase(itemGroupName))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds an <code>ItemGroupProperties<code> object to this blocks item
     * properties store
     * 
     * @param itemGroupProperties
     *            The <code>ItemGroupProperties</code> to be added
     */
    public void addItemGroupProperties(EJItemGroupProperties itemGroupProperties)
    {
        if (itemGroupProperties != null)
        {
            _itemGroups.add(itemGroupProperties);
        }
    }
    
    /**
     * Returns all <code>ItemGroupProperties</code> contained within this
     * container
     * 
     * @return All <code>ItemGroupProperties</code> contained within this
     *         container
     */
    public Collection<EJItemGroupProperties> getAllItemGroupProperties()
    {
        return _itemGroups;
    }
    
    /**
     * Searches all groups for an item with the given name and indicates if it
     * exists
     * 
     * @param name
     *            The name of the item to search for
     * @return <code>true</code> if the item exists, otherwise
     *         <code>false</code>
     */
    public boolean containsItemProperties(String name)
    {
        Iterator<EJItemGroupProperties> groupProperties = _itemGroups.iterator();
        while (groupProperties.hasNext())
        {
            if (groupProperties.next().containsItemReference(name))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Searches all groups for an item with the given name and returns the items
     * screen properties
     * 
     * @param name
     *            The name of the item to search for
     * @return Returns the screen properties of the required item, otherwise
     *         <code>null</code>
     */
    public EJScreenItemProperties getScreenItemProperties(String name)
    {
        EJScreenItemProperties props = null;
        
        Iterator<EJItemGroupProperties> groupPropertiesIti = _itemGroups.iterator();
        while (groupPropertiesIti.hasNext())
        {
            EJCoreItemGroupProperties itemGroupProps = (EJCoreItemGroupProperties) groupPropertiesIti.next();
            props = itemGroupProps.getItemPropertiesForBlockItem(name);
            if (props != null)
            {
                return props;
            }
            
            props = itemGroupProps.getChildItemGroupContainer().getScreenItemProperties(name);
            if (props != null)
            {
                return props;
            }
            
        }
        
        return props;
    }
}
