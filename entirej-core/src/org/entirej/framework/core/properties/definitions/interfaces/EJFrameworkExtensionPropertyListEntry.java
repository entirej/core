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
import java.util.Map;

public interface EJFrameworkExtensionPropertyListEntry extends Serializable
{
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
    public void addProperty(String name, String property);
    
    /**
     * Returns a map of property name/value pairs
     * <p>
     * The maps key will be the name of the property and the maps value, will be
     * the property value
     * 
     * @return A map of property name/value pairs
     */
    public Map<String, String> getAllProperties();
    
    /**
     * Checks to see if a property exists with the given name
     * 
     * @param name
     *            The name of the property to check
     * @return <code>true</code> if there is a property in this list with the
     *         given name, otherwise <code>false</code>
     */
    public boolean propertyExists(String name);
    
    /**
     * Returns the property value for the given property name
     * <p>
     * 
     * @param name
     *            The name of the required property
     * @return The properties value or <code>null</code> if there is no property
     *         with the given name
     */
    public String getProperty(String name);
}
