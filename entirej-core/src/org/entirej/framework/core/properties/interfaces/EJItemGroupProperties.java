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
package org.entirej.framework.core.properties.interfaces;

import java.io.Serializable;
import java.util.Collection;

import org.entirej.framework.core.properties.containers.interfaces.EJItemGroupPropertiesContainer;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;

public interface EJItemGroupProperties extends Serializable
{
    
    public EJItemGroupPropertiesContainer getParentItemGroupContainer();
    
    public EJItemGroupPropertiesContainer getChildItemGroupContainer();
    
    /**
     * Indicates if a frame should be displayed around the groups items
     * 
     * @return The display indicator
     */
    public boolean dispayGroupFrame();
    
    /**
     * Returns the name that will be displayed within the item groups frame
     * 
     * @return The item groups frame
     */
    public String getFrameTitle();
    
    public int getHeight();
    
    /**
     * Indicates if there is an item in this container that already references
     * the given block item name
     * 
     * @param itemName
     *            The name of the item to check for
     * 
     * @return true if the item exists otherwise false
     */
    public boolean containsItemReference(String blockItemName);
    
    /**
     * Indicates how many display columns this group will have
     * <p>
     * All items being added to this group will be inserted into a grid. The
     * grid will have any number of rows but will be limited to the amount of
     * columns as set by this parameter.
     * <p>
     * Items added to this page can span multiple columns and rows
     * 
     * @return The number of columns defined for this group
     */
    public int getNumCols();
    
    public int getWidth();
    
    public int getXspan();
    
    public int getYspan();
    
    public boolean canExpandHorizontally();
    
    public boolean canExpandVertically();
    
    /**
     * Returns the name of this item group
     * 
     * @return The item group name
     */
    public String getName();
    
    /**
     * Returns all <code>EJScreenItemProperties</code> contained within this
     * container
     * 
     * @return All <code>EJScreenItemProperties</code> contained within this
     *         container
     */
    public Collection<EJScreenItemProperties> getAllItemProperties();
    
    public EJFrameworkExtensionProperties getRendererProperties();
    
}
