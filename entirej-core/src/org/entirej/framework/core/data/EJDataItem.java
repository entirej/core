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
package org.entirej.framework.core.data;

import java.io.Serializable;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJBlockItem;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.common.utils.EJDataHelper;
import org.entirej.framework.core.data.controllers.EJFormController;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.properties.EJCoreItemProperties;
import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;

/**
 * Contains the actual data for a specific item.
 */
public class EJDataItem implements Serializable
{
    private EJValueChangedListener          _valueChangedListener;
    private Object                          _value;
    private EJFormController                _formController;
    private EJCoreVisualAttributeProperties _vaProperties;
    private String                          _hint;
    private EJCoreItemProperties            _itemProperties;
    
    EJDataItem(EJFormController formController, EJCoreItemProperties itemProperties, boolean addDefaultInsertValues)
    {
        _formController = formController;
        _itemProperties = itemProperties;
        
        if (addDefaultInsertValues)
        {
            setValue(EJDataHelper.getDefaultInsertValue(new EJForm(formController.getInternalForm()), new EJBlockItem(itemProperties)));
        }
    }
    
    /**
     * Sets the value changed listener for this item
     * 
     * @param listener
     *            The value changed listener
     */
    void setValueChangedListener(EJValueChangedListener listener)
    {
        _valueChangedListener = listener;
    }
    
    /**
     * Return the name of this item
     * 
     * @return This items name
     */
    public String getName()
    {
        return _itemProperties.getName();
    }
    
    /**
     * Sets the item with the given name to the given value
     * 
     * @param value
     *            The value to set
     */
    public void setValue(Object value)
    {
        if (value != null)
        {
            if (!_itemProperties.getDataTypeClass().isAssignableFrom(value.getClass()))
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_DATA_TYPE_FOR_ITEM,
                        _itemProperties.getName(), _itemProperties.getDataTypeClassName(), value.getClass().getName()));
            }
        }
        _value = value;
        
        // Now let the record know that a value has changed
        if (_valueChangedListener != null)
        {
            _valueChangedListener.valueChanged(this.getName(), value);
        }
    }
    
    /**
     * Used to retrieve this items value
     * 
     * @return The items value
     */
    public Object getValue()
    {
        return _value;
    }
    
    /**
     * Returns the properties object of this form.
     * 
     * @return This forms properties
     */
    public EJCoreItemProperties getProperties()
    {
        return _itemProperties;
    }
    
    public void setVisualAttribute(String visualAttributeName)
    {
        if (visualAttributeName == null || visualAttributeName.trim().length() == 0)
        {
            _vaProperties = null;
            return;
        }
        
        EJCoreVisualAttributeProperties vaProperties = _formController.getInternalForm().getVisualAttribute(visualAttributeName);
        if (vaProperties == null)
        {
            throw new IllegalArgumentException("There is no visual attribute with the name " + visualAttributeName + " on this form.");
        }
        
        _vaProperties = vaProperties;
    }
    
    public EJCoreVisualAttributeProperties getVisualAttribute()
    {
        return _vaProperties;
    }
    
    /**
     * Sets this item hint
     * <p>
     * Hints set on data items will be displayed when the record containing the
     * item gains focus. Setting the hint to <code>null</code> removes the hint
     * text
     * 
     * @param text
     *            The hint to set or <code>null</code> if no hint should be
     *            shown
     */
    public void setHint(String text)
    {
        _hint = text;
    }
    
    /**
     * Returns the hint set for this item instance
     * 
     * @return This items hint or <code>null</code> if no item level hint has
     *         been defined
     */
    public String getHint()
    {
        return _hint;
    }
    
    /**
     * Returns true or false depending on whether this item receives its value
     * from the blocks service or if it is entered manually by the application
     * developer
     * 
     * @return <code>true</code> if this is item receives its value from the
     *         blocks service otherwise <code>false</code>
     */
    public boolean isBlockServiceItem()
    {
        return _itemProperties.isBlockServiceItem();
    }
    
    public String toString()
    {
        return _itemProperties.getName() + ": " + (_value == null ? "NULL" : _value);
    }
    
}
