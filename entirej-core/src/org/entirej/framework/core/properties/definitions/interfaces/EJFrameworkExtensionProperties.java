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
package org.entirej.framework.core.properties.definitions.interfaces;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.entirej.framework.core.extensions.properties.EJCoreFrameworkExtensionProperty;
import org.entirej.framework.core.extensions.properties.EJCoreFrameworkExtensionPropertyList;
import org.entirej.framework.core.properties.interfaces.EJBlockProperties;
import org.entirej.framework.core.properties.interfaces.EJFormProperties;

public interface EJFrameworkExtensionProperties extends EJDisplayProperties  , Serializable
{
    /**
     * Returns the form properties to which this framework extension belongs
     * 
     * @return The form properties
     */
    public EJFormProperties getFormProperties();
    
    /**
     * Returns the block properties if they are available for this extension
     * 
     * @return The block properties if they are available for this extension
     */
    public EJBlockProperties getBlockProperties();
    
    /**
     * Returns the parent group of this group or <code>null</code> if this does
     * not belong to another group
     * 
     * @return This properties parent group
     */
    public EJFrameworkExtensionProperties getParentGroup();
    
    /**
     * Returns a map of property name/value pairs
     * <p>
     * The maps key will be the name of the property and the maps value, will be
     * the property value
     * 
     * @return A map of property name/value pairs
     */
    public Map<String, EJCoreFrameworkExtensionProperty> getAllProperties();
    
    /**
     * Returns the <code>RendererProperties</code> for the given group name
     * <p>
     * 
     * @param groupName
     *            The name of the required properties group
     * @return The properties for the required group If the group name is null
     *         or of zero length or if there is no properties group with the
     *         given name
     */
    public abstract EJFrameworkExtensionProperties getPropertyGroup(String groupName);
    
    /**
     * Returns a <code>Collection</code> of all property groups contained witin
     * this group
     * 
     * @return A <code>Collection</code> of property groups within this group
     * @see #addPropertyGroup(String, EJFrameworkExtensionProperties)
     * @see #getPropertyGroup(String)
     * @see #get
     */
    public Collection<EJFrameworkExtensionProperties> getAllPropertyGroups();
    
    /**
     * Returns a <code>Collection</code> of all property lists contained within
     * this group
     * 
     * @return A <code>Collection</code> of property lists within this group
     * @see #addPropertyList(EJCoreFrameworkExtensionPropertyList)
     */
    public Collection<EJFrameworkExtensionPropertyList> getAllPropertyLists();
    
    /**
     * Returns a specific properties list contained within this
     * <code>FrameworkExtensionProperties</code>
     * 
     * @param listName
     *            The name of the required list
     * 
     * @return The required properties list or <code>null</code> if there is no
     *         property list with the given name
     */
    public EJCoreFrameworkExtensionPropertyList getPropertyList(String listName);
    
    /**
     * Used to update a given render property
     * <p>
     * If the property is part of a <code>propertyGroup</code> then use a dot
     * (.) separator between the group name and the property name. e.g. If the
     * <code>xpos</code> property is in the group
     * <code>displayCoordinates</code> then the property name would be:
     * <code>displayCoordinates.xpos</code>
     * 
     * @param name
     * @param value
     */
    public void setPropertyValue(String name, String value);
    
    /**
     * Will copy the property values from the given properties to this property
     * group
     * <p>
     * The method will also iterate over all sub group to ensure all properties
     * are set. If the given properties contains a property name that does not
     * exist within this group, then it will be ignored
     * 
     * @param properties
     *            The properties to copy
     */
    public void copyValuesFromGroup(EJFrameworkExtensionProperties properties);
}
