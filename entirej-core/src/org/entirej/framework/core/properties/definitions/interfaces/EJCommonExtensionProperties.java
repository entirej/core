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

public interface EJCommonExtensionProperties extends Serializable
{
    /**
     * Returns the name of these properties
     * <p>
     * All properties contained within a <code>RendererProperties</code> will
     * build a group of properties. The name is the name of the grouped
     * properties e.g. visualAttributes, displayCoordinates etc.
     * 
     * @return The name of this <code>RendererProperties</code>
     */
    public String getName();
    
    /**
     * Returns the full group name of this group
     * <p>
     * This name will include all master group names if this group is contained
     * within a master group
     * 
     * @return The full group name of this definition group
     */
    public String getFullGroupName();
    
    /**
     * Checks if a specific property name exists
     * 
     * @param name
     *            The name of the property to check
     * @return <code>true</code> if the property exists otherwise
     *         <code>false</code>
     */
    public boolean propertyExists(String name);
    
    /**
     * Indicates if the given property has a value
     * 
     * @return <code>true</code> if the property value is <code>null</code>,
     *         otherwise <code>false</code>
     */
    public boolean isPropertyValueNull(String propertyName);
    
    /**
     * Retrieves a given properties value in <code>String</code> format
     * 
     * @param name
     *            The name of the required parameter
     * 
     * @return The properties value or null if there is no property with the
     *         given name
     * @see EJCommonExtensionProperties#propertyExists(String)
     * @see EJCommonExtensionProperties#getBooleanProperty(String)
     * @see EJCommonExtensionProperties#getFloatParameter(String)
     * @see EJCommonExtensionProperties#getIntParameter(String)
     * @see EJCommonExtensionProperties#getDoubleProperty(String)
     */
    public String getStringProperty(String name);
    
    /**
     * Retrieves a given properties value in <code>boolean</code> format
     * <p>
     * EntireJ will attempt to convert the property value to a boolean value if
     * a conversion error occurs an exception will be thrown
     * 
     * @param name
     *            The name of the required parameter
     * @param defaultValue
     *            The default value to be returned id the given property is null
     * @return The properties value
     * @throws EJApplicationException
     *             if there is no property with the given name
     * @see EJCommonExtensionProperties#propertyExists(String)
     * @see EJCommonExtensionProperties#getStringProperty(String)
     * @see IDisplayProperties#getFloatProperty((String, float)
     * @see IDisplayProperties#getIntProperty((String, int)
     * @see EJCommonExtensionProperties#getDoubleProperty(String, double)
     */
    public boolean getBooleanProperty(String name, boolean defaultValue);
    
    /**
     * Retrieves a given properties value in <code>int</code> format
     * <p>
     * EntireJ will attempt to convert the property value to an int value if a
     * conversion error occurs an exception will be thrown
     * 
     * @param name
     *            The name of the required parameter
     * @param defaultValue
     *            This is the default value if the property required has no
     *            value
     * 
     * @return The properties value
     * @see EJCommonExtensionProperties#propertyExists(String)
     * @see EJCommonExtensionProperties#getBooleanProperty(String)
     * @see EJCommonExtensionProperties#getStringProperty(String)
     * @see EJCommonExtensionProperties#getFloatProperty(String)
     * @see EJCommonExtensionProperties#getDoubleProperty(String)
     */
    public int getIntProperty(String name, int defaultValue);
    
    /**
     * Retrieves a given properties value in <code>float</code> format
     * <p>
     * EntireJ will attempt to convert the property value to a float value if a
     * conversion error occurs an exception will be thrown
     * 
     * @param name
     *            The name of the required parameter
     * @param defaultValue
     *            The default value to be returned if the given property has not
     *            been set
     * @return The properties value
     * @see EJCommonExtensionProperties#propertyExists(String)
     * @see IDisplayProperties#getBooleanProperty((String, boolean)
     * @see IDisplayProperties#getStringProperty((String)
     * @see EJCommonExtensionProperties#getIntProperty(String)
     * @see EJCommonExtensionProperties#getDoubleProperty(String)
     */
    public float getFloatProperty(String name, float defaultValue);
    
    /**
     * Retrieves a given properties value in <code>double</code> format EntireJ
     * will attempt to convert the property value to a double value if a
     * conversion error occurs an exception will be thrown
     * 
     * @param name
     *            The name of the required parameter
     * @param defaultValue
     *            The value to be returned if the given property has not been
     *            set
     * @return The properties value
     * @throws EJApplicationException
     *             if there is no property with the given name
     * @see EJCommonExtensionProperties#propertyExists(String)
     * @see IDisplayProperties#getBooleanProperty((String)
     * @see IDisplayProperties#getStringProperty((String)
     * @see EJCommonExtensionProperties#getIntProperty(String)
     * @see EJCommonExtensionProperties#getFloatProperty(String)
     */
    public double getDoubleProperty(String name, double defaultValue);
    
}
