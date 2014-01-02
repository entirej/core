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

import java.io.Serializable;

import org.entirej.framework.core.properties.definitions.EJPropertyDefinitionType;

public class EJCoreFrameworkExtensionProperty implements Serializable
{
    private EJPropertyDefinitionType _propertyType;
    private String                   _name;
    private String                   _value;
    private String                   _baseValue;
    private boolean                  _multilingual = false;
    private boolean                  _isMandatory  = false;
    
    public EJCoreFrameworkExtensionProperty(EJPropertyDefinitionType propertyType, String name)
    {
        this(propertyType, name, false, null);
    }
    
    public EJCoreFrameworkExtensionProperty(EJPropertyDefinitionType propertyType, String name, boolean multilingual)
    {
        this(propertyType, name, multilingual, null);
    }
    
    public EJCoreFrameworkExtensionProperty(EJPropertyDefinitionType propertyType, String name, boolean multilingual, String value)
    {
        _propertyType = propertyType;
        _name = name;
        _multilingual = multilingual;
        _baseValue = value;
    }
    
    public EJPropertyDefinitionType getPropertyType()
    {
        return _propertyType;
    }
    
    public String getName()
    {
        return _name;
    }
    
    public void setName(String name)
    {
        _name = name;
    }
    
    public String getValue()
    {
        return _value;
    }
    
    public void setValue(String value)
    {
        _baseValue = value;
        _value = value;
    }
    
    public void setTranslatedValue(String translatdValue)
    {
        _value = translatdValue;
    }
    
    public String getBaseValue()
    {
        return _baseValue;
    }
    
    public boolean isMultilingual()
    {
        return _multilingual;
    }
    
    public void setMultilingual(boolean multilingual)
    {
        _multilingual = multilingual;
    }
    
    public boolean isMandatory()
    {
        return _isMandatory;
    }
    
    public void setMandatory(boolean mandatory)
    {
        _isMandatory = mandatory;
    }
    
}
