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
package org.entirej.framework.core.extensions.properties;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.common.utils.EJParameterChecker;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionPropertyList;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionPropertyListEntry;
import org.entirej.framework.core.properties.interfaces.EJBlockProperties;
import org.entirej.framework.core.properties.interfaces.EJFormProperties;

public class EJCoreFrameworkExtensionProperties implements EJFrameworkExtensionProperties
{
    private String                                            _name;
    private EJFrameworkExtensionProperties                    _parentGroup;
    private HashMap<String, EJCoreFrameworkExtensionProperty> _properties;
    private HashMap<String, EJFrameworkExtensionProperties>   _propertyGroups;
    private HashMap<String, EJFrameworkExtensionPropertyList> _propertyLists;
    private EJFormProperties                                  _formProperties;
    private EJBlockProperties                                 _blockProperties;
    
    /**
     * Creates a new <code>FrameworkExtensionProperties</code> object
     */
    public EJCoreFrameworkExtensionProperties(EJFormProperties formProperties, EJBlockProperties blockProperties,
            String name, EJFrameworkExtensionProperties parentProperties)
    {
        _formProperties = formProperties;
        _blockProperties = blockProperties;
        _name = name;
        _properties = new HashMap<String, EJCoreFrameworkExtensionProperty>();
        _propertyGroups = new HashMap<String, EJFrameworkExtensionProperties>();
        _propertyLists = new HashMap<String, EJFrameworkExtensionPropertyList>();
        _parentGroup = parentProperties;
    }
    
    public EJFormProperties getFormProperties()
    {
        return _formProperties;
    }
    
    public EJBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }
    
    /**
     * Returns the parent group of this group or <code>null</code> if this does
     * not belong to another group
     * 
     * @return This properties parent group
     */
    public EJFrameworkExtensionProperties getParentGroup()
    {
        return _parentGroup;
    }
    
    /**
     * Returns the name of these properties
     * <p>
     * All properties contained within a
     * <code>FrameworkExtensionProperties</code> will build a group of
     * properties. The name is the name of the grouped properties e.g.
     * visualAttributes, displayCoordinates etc.
     * 
     * @return The name of this <code>RendererProperties</code>
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * Returns the full group name of this group
     * <p>
     * This name will include all master group names if this group is contained
     * within a master group
     * 
     * @return The full group name of this definition group
     */
    public String getFullGroupName()
    {
        StringBuffer name = new StringBuffer();
        
        if (_parentGroup == null)
        {
            return "";
        }
        else
        {
            name.append(_parentGroup.getFullGroupName());
            if (name.length() > 0)
            {
                name.append(".");
            }
            name.append(getName());
            return name.toString();
        }
    }
    
    public void addPropertyGroup(EJCoreFrameworkExtensionProperties propertiesGroup)
    {
        if (propertiesGroup != null)
        {
            _propertyGroups.put(propertiesGroup.getName(), propertiesGroup);
        }
    }
    
    /**
     * Returns a <code>Collection</code> of all property groups contained within
     * this group
     * 
     * @return A <code>Collection</code> of property groups within this group
     * @see #addPropertyGroup(String, EJCoreFrameworkExtensionProperties)
     * @see #getPropertyGroup(String)
     * @see #get
     */
    public Collection<EJFrameworkExtensionProperties> getAllPropertyGroups()
    {
        return _propertyGroups.values();
    }
    
    /**
     * Returns a specific properties group contained within this
     * <code>FrameworkExtenstionProperties</code>
     * 
     * @param groupName
     *            The name of the required group
     * 
     * @return The required properties group or <code>null</code> if there is no
     *         property group with the given name
     */
    public EJCoreFrameworkExtensionProperties getPropertyGroup(String groupName)
    {
        if (groupName == null)
        {
            return null;
        }
        
        if (_propertyGroups.containsKey(groupName))
        {
            return (EJCoreFrameworkExtensionProperties) _propertyGroups.get(groupName);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Adds a property list to this property
     * 
     * @param propertiesList
     *            the list to add
     */
    public void addPropertyList(EJFrameworkExtensionPropertyList propertiesList)
    {
        if (propertiesList != null)
        {
            _propertyLists.put(propertiesList.getName(), propertiesList);
        }
    }
    
    /**
     * Returns a <code>Collection</code> of all property lists contained within
     * this group
     * 
     * @return A <code>Collection</code> of property lists within this group
     * @see #addPropertyList(EJCoreFrameworkExtensionPropertyList)
     */
    public Collection<EJFrameworkExtensionPropertyList> getAllPropertyLists()
    {
        return _propertyLists.values();
    }
    
    /**
     * Returns a specific properties list contained within this
     * <code>FrameworkExtenstionProperties</code>
     * 
     * @param listName
     *            The name of the required list
     * 
     * @return The required properties list or <code>null</code> if there is no
     *         property list with the given name
     */
    public EJCoreFrameworkExtensionPropertyList getPropertyList(String listName)
    {
        if (listName == null)
        {
            return null;
        }
        
        if (_propertyLists.containsKey(listName))
        {
            return (EJCoreFrameworkExtensionPropertyList) _propertyLists.get(listName);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Adds a new property
     * <p>
     * The property is a name value pair. The properties are always strings.
     * 
     * @param property
     *            The property to add
     */
    public void addProperty(EJCoreFrameworkExtensionProperty property)
    {
        if (property != null && property.getName().trim().length() > 0)
        {
            _properties.put(property.getName(), property);
        }
    }
    
    /**
     * Returns a map of property name/value pairs
     * <p>
     * The maps key will be the name of the property and the maps value, will be
     * the property value
     * 
     * @return A map of property name/value pairs
     */
    public Map<String, EJCoreFrameworkExtensionProperty> getAllProperties()
    {
        return _properties;
    }
    
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
     *            The name of the property you wish to set
     * @param value
     *            The value to set
     */
    public void setPropertyValue(String name, String value)
    {
        StringTokenizer tokenizer = new StringTokenizer(name, ".");
        EJCoreFrameworkExtensionProperties groupProperties = this;
        while (tokenizer.hasMoreElements())
        {
            String element = (String) tokenizer.nextElement();
            
            if (tokenizer.hasMoreElements())
            {
                groupProperties = groupProperties.getPropertyGroup(element);
                
                // If the groupProperties is null, then an invalid name was
                // given within the name string parameter
                if (groupProperties == null)
                {
                    throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                            EJFrameworkMessage.INVALID_GROUP_NAME_IN_SET_PROPERTY_VALUE, name, element));
                }
                
            }
            else
            {
                if (groupProperties.propertyExists(element))
                {
                    groupProperties.updatePropertyValue(element, value);
                }
            }
        }
    }
    
    public boolean propertyExists(String name)
    {
        return _properties.containsKey(name);
    }
    
    /**
     * returns if the given property value is <code>null</code>
     */
    public boolean isPropertyValueNull(String propertyName)
    {
        if (propertyExists(propertyName))
        {
            String property = getStringProperty(propertyName);
            
            if (property == null || property.trim().length() == 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.NO_PROPERTY_IN_FRAMEWORK_EXTENSION, propertyName, _name));
        }
        
    }
    
    /**
     * Sets the given properties value
     * 
     * @param name
     *            The name of the property
     * @param value
     *            The properties value
     * @see #propertyExists(String)
     */
    void updatePropertyValue(String name, String value)
    {
        EJParameterChecker.checkNotZeroLength(name, "updatePropertyValue", "name");
        
        if (propertyExists(name))
        {
            _properties.get(name).setValue(value);
        }
        else
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.NO_PROPERTY_IN_FRAMEWORK_EXTENSION, name, _name));
        }
    }
    
    /**
     * Return the properties value as a String value
     * 
     * @param name
     *            The name of the property
     */
    public String getStringProperty(String name)
    {
        if (name == null)
        {
            return null;
        }
        
        String propertyName = null;
        
        StringTokenizer tokenizer = new StringTokenizer(name, ".");
        if (tokenizer.countTokens() > 1)
        {
            EJCoreFrameworkExtensionProperties groupProperties = this;
            while (tokenizer.hasMoreElements())
            {
                String element = (String) tokenizer.nextElement();
                if (tokenizer.hasMoreElements())
                {
                    groupProperties = groupProperties.getPropertyGroup(element);
                }
                else
                {
                    if (groupProperties!=null && element != null && groupProperties.propertyExists(element))
                    {
                        return groupProperties.getStringProperty(element);
                    }
                }
            }
        }
        else
        {
            propertyName = name;
        }
        
        if (propertyName == null)
        {
            return null;
        }
        
        if (propertyExists(propertyName))
        {
            EJCoreFrameworkExtensionProperty property = _properties.get(propertyName);
            if (property.getValue() == null || property.getValue().trim().length() == 0)
            {
                return null;
            }
            else
            {
                return property.getValue();
            }
            
        }
        else
        {
            return null;
        }
    }
    
    public boolean getBooleanProperty(String name, boolean defaultValue)
    {
        String property = getStringProperty(name);
        
        // The user wants this parameter as a boolean
        // therefore make the conversion. If this parameter
        // cannot convert to a boolean then throw an exception
        if (property == null || property.trim().length() == 0)
        {
            // No value, so return the default
            return defaultValue;
        }
        if (property.equalsIgnoreCase("true") || property.equalsIgnoreCase("false"))
        {
            return Boolean.valueOf(property).booleanValue();
        }
        else
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.VALUE_CANNOT_BE_CONVERTED_TO_BOOLEAN, name, property));
        }
    }
    
    public int getIntProperty(String name, int defaultValue)
    {
        String property = getStringProperty(name);
        
        // The user wants this parameter as an int
        // therefore make the conversion. If this parameter
        // cannot convert to an int then throw an exception
        
        try
        {
            if (property == null || property.trim().length() == 0)
            {
                return defaultValue;
            }
            else
            {
                return Integer.parseInt(property);
            }
        }
        catch (NumberFormatException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.VALUE_CANNOT_BE_CONVERTED_TO_INT, name, property));
        }
    }
    
    public float getFloatProperty(String name, float defaultValue)
    {
        String property = getStringProperty(name);
        
        // The user wants this parameter as a float
        // therefore make the conversion. If this parameter
        // cannot convert to a float then throw an exception
        try
        {
            if (property == null || property.trim().length() == 0)
            {
                return defaultValue;
            }
            else
            {
                Float floatValue = Float.valueOf(property);
                return floatValue.floatValue();
            }
        }
        catch (NumberFormatException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.VALUE_CANNOT_BE_CONVERTED_TO_FLOAT, name, property));
        }
    }
    
    public double getDoubleProperty(String name, double defaultValue)
    {
        String property = getStringProperty(name);
        
        // The user wants this parameter as a float
        // therefore make the conversion. If this parameter
        // cannot convert to a float then throw an exception
        try
        {
            if (property == null || property.trim().length() == 0)
            {
                return defaultValue;
            }
            else
            {
                double doubleValue = Double.parseDouble(property);
                return doubleValue;
            }
        }
        catch (NumberFormatException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.VALUE_CANNOT_BE_CONVERTED_TO_DOUBLE, name, property));
        }
    }
    
    public void copyValuesFromGroup(EJFrameworkExtensionProperties properties)
    {
        if (properties == null)
        {
            return;
        }
        
        String fullGroupName = properties.getFullGroupName();
        // First set any properties that might exist within the properties, then
        // iterate over all the sub groups
        Iterator<String> propertyNames = properties.getAllProperties().keySet().iterator();
        
        while (propertyNames.hasNext())
        {
            String name = propertyNames.next();
            this.setPropertyValue(buildPropertyName(fullGroupName, name), properties.getStringProperty(name));
        }
        
        Iterator<EJFrameworkExtensionPropertyList> propertyLists = properties.getAllPropertyLists().iterator();
        while (propertyLists.hasNext())
        {
            EJFrameworkExtensionPropertyList list = propertyLists.next();
            
            if (!_propertyLists.containsKey(list.getName()))
            {
                continue;
            }
            
            EJCoreFrameworkExtensionPropertyList newList = new EJCoreFrameworkExtensionPropertyList(list.getName());
            for (EJFrameworkExtensionPropertyListEntry entry : list.getAllListEntries())
            {
                
                EJCoreFrameworkExtensionPropertyListEntry newEntry = new EJCoreFrameworkExtensionPropertyListEntry();
                for (String name : entry.getAllProperties().keySet())
                {
                    newEntry.addProperty(name, entry.getProperty(name));
                }
                
                newList.addListEntry(newEntry);
            }
            
            this.addPropertyList(newList);
        }
        
        Iterator<EJFrameworkExtensionProperties> subGroups = properties.getAllPropertyGroups().iterator();
        while (subGroups.hasNext())
        {
            this.copyValuesFromGroup(subGroups.next());
        }
    }
    
    private String buildPropertyName(String fullGroupName, String definitionName)
    {
        if (fullGroupName == null || fullGroupName.trim().length() == 0)
        {
            return definitionName;
        }
        else
        {
            return fullGroupName + "." + definitionName;
        }
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        String name;
        Iterator<String> iti = _properties.keySet().iterator();
        
        buffer.append("Rendering Properties:\n");
        while (iti.hasNext())
        {
            name = (String) iti.next();
            buffer.append("Name: ");
            buffer.append(name);
            buffer.append(", Value: ");
            buffer.append(getStringProperty(name));
            buffer.append("\n");
        }
        
        iti = _propertyGroups.keySet().iterator();
        
        while (iti.hasNext())
        {
            name = (String) iti.next();
            buffer.append("Property Group: ");
            buffer.append(name);
            buffer.append("\n");
            buffer.append(getPropertyGroup(name));
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
