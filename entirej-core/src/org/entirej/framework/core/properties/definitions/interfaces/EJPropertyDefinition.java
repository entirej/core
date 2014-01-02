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
package org.entirej.framework.core.properties.definitions.interfaces;

import java.util.Collection;

import org.entirej.framework.core.properties.definitions.EJPropertyDefinitionType;

public interface EJPropertyDefinition extends EJNotifiablePropertyDefinition
{
    /**
     * Returns the <code>PropertyDefinitionGroup</code> that contains this
     * property definition
     * 
     * @return This properties definition group
     */
    public abstract EJPropertyDefinitionGroup getParentPropertyDefinitionGroup();
    
    /**
     * Sets the parent render property definition group for this definition
     * 
     * @param group
     */
    public void setParentPropertyDefinitionGroup(EJPropertyDefinitionGroup group);
    
    /**
     * Sets this parameters label
     * <p>
     * The label is what will be displayed to the used within the properties
     * editor
     * <p>
     * If no label has been set then the Name will be used to display to the
     * user
     * 
     * @param label
     *            This properties label
     */
    public abstract void setLabel(String label);
    
    /**
     * Returns this properties label
     * 
     * @return This properties label
     */
    public String getLabel();
    
    /**
     * Returns this properties default value
     * <p>
     * The default value is a required value if this property is defined as
     * being mandatory. If the property is not of the right datatype or not one
     * of the items list of valid values (if there is a list) it will not be set
     * within the <b>EntireJ Framework Plugin</b>. No validation of the value
     * will be made
     * 
     * @return This properties default value
     */
    public abstract String getDefaultValue();
    
    /**
     * Sets this properties default value
     * 
     * @param defaultValue
     *            this properties default value
     */
    public abstract void setDefaultValue(String defaultValue);
    
    /**
     * Sets this properties description
     * <p>
     * The description will be displayed to the user within the <b>EntireJ
     * Framework Plugin</b>
     * 
     * @return This properties description
     */
    public abstract String getDescription();
    
    /**
     * Sets this properties description
     * 
     * @param description
     *            This items description
     */
    public abstract void setDescription(String description);
    
    /**
     * Indicates if this property is mandatory, meaning that the user
     * <b>must</b> enter a value
     * <p>
     * A default value <b>must</b> entered if the property has been defined as
     * mandatory if it isn't then the mandatory flag will have no effect
     * 
     * @return The mandatory flag for this property
     */
    public abstract boolean isMandatory();
    
    /**
     * Sets this property to be mandatory
     * 
     * @param mandatory
     *            The mandatory flag for this property
     */
    public abstract void setMandatory(boolean mandatory);
    
    /**
     * Indicates if the value of this property should be translated by the
     * application translator when the application is run
     * 
     * @return <code>true</code> if this is a multilingual property otherwise
     *         <code>false</code>
     */
    public boolean isMultilingual();
    
    /**
     * Sets the multilingual flag for this property
     * 
     * @param multilingual
     *            <code>true</code> if this is a multilingual property otherwise
     *            <code>false</code>
     */
    public void setMultilingual(boolean multilingual);
    
    /**
     * Indicates if this property definition can be set programatically by the
     * developer using the <code>FormManager</code>
     * 
     * @return <code>true</code> if this property can be set programatically,
     *         otherwise <code>false</code>
     */
    public boolean canBeSetProgramatically();
    
    /**
     * Sets the flag to indicate if this property definition can be set
     * programatically by the developer using the <code>FormManager</code>
     * 
     * @param set
     *            <code>true</code> if this property can be set programatically,
     *            otherwise <code>false</code>
     */
    public void setCanBeSetProgramatically(boolean set);
    
    /**
     * Returns the name of this parameter
     * 
     * @return this parameters name
     */
    public abstract String getName();
    
    /**
     * Return the data type of this property definition
     * 
     * @return The property type of this property definition
     */
    public EJPropertyDefinitionType getPropertyType();
    
    /**
     * Indicates if this property has a list of valid values
     * 
     * @return <code>true</code> if this property has valid values, otherwise
     *         <code>false</code>
     */
    public abstract boolean hasValidValues();
    
    /**
     * Sets the parameter to indicate to the framework plugin to load valid
     * values for this property dynamically
     * 
     * @param loadDynamically
     */
    public void setLoadValidValuesDynamically(boolean loadDynamically);
    
    /**
     * When set to <code>true</code>, the EntireJ Plugin will inform the
     * FrameworkExtension Definition that this property has changed
     */
    public void setNotifyWhenChanged(boolean notify);
    
    /**
     * Indicates if the EntireJ Plugin will inform the Framework Extension
     * Definition when this property has changed
     * 
     * @return <code>true</code> when changes will be notified, otherwise
     *         <code>false</code>
     */
    public boolean notifyWhenChanged();
    
    /**
     * Indicates if the valid values for this property should be loaded
     * dynamically
     * 
     * @return <code>true</code> if the valid values should be loaded
     *         dynamically, otherwise <code>false</code>
     */
    public boolean loadValidValuesDynamically();
    
    /**
     * Indicates if the given value is valid
     * 
     * @param name
     *            The value to check
     * @return <code>true</code> if the value is correct otherwise
     *         <code>false</code>
     */
    public abstract boolean isValidValue(String value);
    
    /**
     * Returns a <code>Map</code> of valid values for this property
     * <p>
     * The maps key is the value name and the value is the values label
     * 
     * @return The valid values for this item or an empty list if there is no
     *         valid values for this property
     */
    public abstract Collection<String> getValidValueLabels();
    
    /**
     * Returns the name of the property with the given label or null if there is
     * no property with the given label
     * 
     * @param label
     *            The label of the required property
     * @return The required property name or null if there is no property with
     *         the given label
     */
    public abstract String getValidValueNameForLabel(String label);
    
    /**
     * Returns the label for the given value or <code>null</code> if there was
     * no label for the given value no property with the given label
     * 
     * @param value
     *            The label will be returned for this value
     * @return The required label or <code>null</code> if there is no label for
     *         the value specified
     */
    public abstract String getLabelForValidValue(String value);
    
    /**
     * Adds a valid value to this property
     * 
     * @param name
     *            The name of the value. This value will be stored within the
     *            form property file and used within the framework
     * @param label
     *            The label this value. This value is only used within the
     *            <b>EntireJ Framework Plugin</b> to provide a user friendly
     *            display name
     */
    public abstract void addValidValue(String name, String label);
    
    /**
     * Clears any valid values added to this property definition
     */
    public void clearValidValues();
    
    /**
     * Indicates if the given parameters values should be entered using a
     * monospaced font (e.g. Courier New)
     * <p>
     * This can be useful when entering SQL statements or when text alignment
     * makes the values more readable
     * 
     * @param use
     *            Indicates if the Monospaced Font should be used
     */
    public abstract void setUseMonospacedFont(boolean use);
    
    /**
     * Indicates if the parameters value should be displayed in a monospaced
     * font (e.g. Courier New)
     * 
     * @return <code>true</code> if Monospaced Font should be used, otherwise
     *         <code>false</code>
     */
    public abstract boolean useMonospacedFont();
    
    /**
     * If the data type is DATATYPE_STRING, and the value that needs to be
     * entered can contain allot of data then the Plugins field can span more
     * than one row. The rows attribute defines how many rows the plugin item
     * will span
     * <p>
     * This property only effects DATATYPE_STRING types
     * 
     * @param span
     *            The amount of rows to span. This must be a positive value,
     *            negative values will be ignored and the default of 1 will be
     *            used
     */
    public abstract void setRowSpan(int span);
    
    /**
     * Sets the value for the amount of rows a string data type will span
     * <p>
     * If the data type is DATATYPE_STRING, and the value that needs to be
     * entered can contain allot of data then the Plugins field can span more
     * than one row. The rows attribute defines how many rows the plugin item
     * will span
     * <p>
     * This property only effects DATATYPE_STRING types
     * 
     * @param span
     *            The amount of rows to span. This must be a positive value,
     *            negative values will be ignored and the default of 1 will be
     *            used
     */
    public abstract int getRowSpan();
    
    /**
     * If the data type is DATATYPE_STRING, and the value that needs to be
     * entered can contain allot of data then the Plugins field can span more
     * than one row. This attribute can be used instead of the <code>
     * {@link #setRowSpan(int)}</code> to indicate that the field will grow to
     * use the excess space if required. This can be used when you want the text
     * field to be displayed within the whole wizard page
     */
    public abstract void setGrabExcessVerticalSpace();
    
    /**
     * If the data type is DATATYPE_STRING, and the value that needs to be
     * entered can contain allot of data then the Plugins field can span more
     * than one row. This attribute can be used instead of the <code>
     * {@link #setRowSpan(int)}</code> to indicate that the field will grow to
     * use the excess space if required. This can be used when you want the text
     * field to be displayed within the whole wizard page
     * 
     * @param grab
     *            If set to <code>true</code>, the field will grab any excess
     *            space within the wizard page
     */
    public abstract boolean getGrabExcessVerticalSpace();
    
}
