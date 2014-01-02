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
package org.entirej.framework.core.service;

import java.io.Serializable;

/**
 * Defines a property that can be used for building EJ DataService POJOs.
 * 
 * @param <T>
 *            The data type of the property
 */
public class EJPojoProperty<T> implements Serializable
{
    private T       _value           = null;
    private T       _initialValue    = null;
    private boolean _initialValueSet = false;
    
    /**
     * The constructor of this method is private in order to restrict the
     * instances of properties. Please use the given public static methods for
     * creating properties.
     */
    private EJPojoProperty()
    {
    }
    
    /**
     * Sets the value to the given property. If the property is null, a new
     * property will be created, otherwise the value is set to the given value.<br>
     * The first value set via this method will be used as a return for the
     * {@code getPropertyInitialValue(EJPojoProperty)} method.
     * 
     * @param property
     *            - The property where to set the value
     * @param value
     *            - The new value for the property
     * @return the given property or a new created one depending if the given
     *         property is null or not
     * 
     * @see #getPropertyValue(EJPojoProperty)
     * @see #getPropertyInitialValue(EJPojoProperty)
     * 
     */
    public static <T> EJPojoProperty<T> setPropertyValue(EJPojoProperty<T> property, T value)
    {
        if (property == null)
        {
            property = new EJPojoProperty<T>();
        }
        property.setValue(value);
        
        return property;
    }
    
    /**
     * Returns the value of the given property.
     * 
     * @param property
     *            - The property
     * @return a Object with the property's value, or null if the given property
     *         is null
     * @see #setPropertyValue(EJPojoProperty, Object)
     * @see #getPropertyInitialValue(EJPojoProperty)
     */
    public static <T> T getPropertyValue(EJPojoProperty<T> property)
    {
        if (property != null)
        {
            return property.getValue();
        }
        return null;
    }
    
    /**
     * Returns the initial value of the given property.
     * 
     * @param property
     *            - The property
     * @return the first value set to the given property via
     *         {@code #setPropertyValue(EJPojoProperty, Object)}
     * @see #setPropertyValue(EJPojoProperty, Object)
     */
    public static <T> T getPropertyInitialValue(EJPojoProperty<T> property)
    {
        if (property != null)
        {
            return property.getInitialValue();
        }
        return null;
    }
    
    /**
     * Clear the initial value of the given property
     * 
     * @param property
     *            - The property
     */
    public static <T> void clearInitialValue(EJPojoProperty<T> property)
    {
        if (property != null)
        {
            property.clearInitalValue();
        }
    }
    
    /**
     * Sets the given value to this property. The first value set via this
     * method will be used as a return for the {@code getInitialValue()} method.
     * 
     * @param value
     *            - The property value.
     * @see #getInitialValue()
     * @see #getValue()
     */
    private void setValue(T value)
    {
        if (!_initialValueSet)
        {
            _initialValueSet = true;
            _initialValue = value;
        }
        _value = value;
    }
    
    /**
     * Returns the value of this property.
     * 
     * @return a object with the property's value
     */
    private T getValue()
    {
        return _value;
    }
    
    /**
     * Returns the initial value of this property.
     * 
     * @return the first value set via {@code setValue(Object)}
     * @see #setValue(Object)
     */
    private T getInitialValue()
    {
        return _initialValueSet ? _initialValue : _value;
    }
    
    /**
     * Clears the initial value
     */
    private void clearInitalValue()
    {
        _initialValue = _value;
    }
}
