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
package org.entirej.framework.core.extensions.properties;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.common.utils.EJParameterChecker;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionPropertyListEntry;

public class EJCoreFrameworkExtensionPropertyListEntry implements EJFrameworkExtensionPropertyListEntry
{
    private HashMap<String, String> _propertyEntries;
    
    /**
     * Creates a new <code>FrameworkExtensionPropertyListEntry</code> object
     */
    public EJCoreFrameworkExtensionPropertyListEntry()
    {
        _propertyEntries = new HashMap<String, String>();
    }
    
    /**
     * Adds a new property
     * <p>
     * The property is a name value pair. The properties are always strings
     * 
     * @param name
     *            The name of the property
     * @param property
     *            The property value
     * @throws NullPointerException
     *             Thrown if the name property is null or its value is of zero
     *             length
     */
    public void addProperty(String name, String property)
    {
        if (name != null && name.trim().length() > 0)
        {
            _propertyEntries.put(name, property);
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
    public Map<String, String> getAllProperties()
    {
        return _propertyEntries;
    }
    
    /**
     * Checks to see if a property exists with the given name
     * 
     * @param name
     *            The name of the property to check
     * @return <code>true</code> if there is a property in this list with the
     *         given name, otherwise <code>false</code>
     */
    public boolean propertyExists(String name)
    {
        return _propertyEntries.containsKey(name);
    }
    
    /**
     * Returns the property value for the given property name
     * <p>
     * 
     * @param name
     *            The name of the required proeprty
     * @return The properties value or <code>null</code> if there is no property
     *         with the given name
     */
    public String getProperty(String name)
    {
        return _propertyEntries.get(name);
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
            _propertyEntries.put(name, value);
        }
        else
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_PROPERTY_IN_FRAMEWORK_EXTENSION_NO_NAME, name));
        }
    }
    
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("FrameworkExtensionPropertyListElement:\n");
        Iterator<String> names = _propertyEntries.keySet().iterator();
        while (names.hasNext())
        {
            String name = names.next();
            buf.append("Name: ");
            buf.append(name);
            buf.append(", Value: ");
            buf.append(_propertyEntries.get(name));
            buf.append("\n");
        }
        return buf.toString();
    }
}
