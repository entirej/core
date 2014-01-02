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
package org.entirej.framework.core.properties.containers.interfaces;

import java.io.Serializable;
import java.util.Collection;

import org.entirej.framework.core.properties.interfaces.EJItemGroupProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;

public interface EJItemGroupPropertiesContainer extends Serializable
{
    
    public static final int MAIN_SCREEN   = 0;
    public static final int INSERT_SCREEN = 1;
    public static final int UPDATE_SCREEN = 2;
    public static final int QUERY_SCREEN  = 3;
    
    public int getContainerType();
    
    /**
     * Returns the amount of items within this container
     * 
     * @return The number of items within this container
     */
    public int count();
    
    /**
     * Indicates if there is an item group in this container with the given name
     * 
     * @param itemGroupName
     *            The name of the item group to check for
     * 
     * @return true if the item group exists otherwise false
     */
    public boolean containsItemGroup(String itemGroupName);
    
    /**
     * Adds an <code>ItemGroupProperties<code> object to this blocks item
     * properties store
     * 
     * @param itemGroupProperties
     *            The <code>ItemGroupProperties</code> to be added
     */
    public void addItemGroupProperties(EJItemGroupProperties itemGroupProperties);
    
    /**
     * Returns all <code>ItemGroupProperties</code> contained within this
     * container
     * 
     * @return All <code>ItemGroupProperties</code> contained within this
     *         container
     */
    public Collection<EJItemGroupProperties> getAllItemGroupProperties();
    
    /**
     * Searches all groups for an item with the given name and indicates if it
     * exists
     * 
     * @param name
     *            The name of the item to search for
     * @return <code>true</code> if the item exists, otherwise
     *         <code>false</code>
     */
    public boolean containsItemProperties(String name);
    
    /**
     * Searches all groups for an item with the given name and returns the items
     * screen properties
     * 
     * @param name
     *            The name of the item to search for
     * @return Returns the screen properties of the required item, otherwise
     *         <code>null</code>
     */
    public EJScreenItemProperties getScreenItemProperties(String name);
    
}
