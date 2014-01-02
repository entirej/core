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
import java.util.Properties;

public class EJTableColumn implements Serializable
{
    private String           _name;
    private String           _datatypeName;
    private EJParameterType  _parameterType;
    private final Properties _properties = new Properties();
    
    private boolean          _array;
    
    public String getName()
    {
        return _name;
    }
    
    public void setName(String name)
    {
        _name = name;
    }
    
    public String getDatatypeName()
    {
        return _datatypeName;
    }
    
    public void setDatatypeName(String datatypeName)
    {
        _datatypeName = datatypeName;
    }
    
    public boolean isArray()
    {
        return _array;
    }
    
    public void setArray(boolean array)
    {
        this._array = array;
    }
    
    public void setParameterType(EJParameterType type)
    {
        _parameterType = type;
    }
    
    /**
     * Indicates the type of this parameter
     * <p>
     * The parameter type can be <code>null</code> if the type is either unknown
     * or, for example with Oracle Types, the column belongs to a collection and
     * not directly to the function or procedure
     * 
     * @return The type of this parameter or <code>null</code>
     */
    public EJParameterType getParameterType()
    {
        return _parameterType;
    }
    
    public String getProperty(String key, String defaultVlaue)
    {
        return _properties.getProperty(key, defaultVlaue);
    }
    
    public String getProperty(String key)
    {
        return _properties.getProperty(key);
    }
    
    public Object setProperty(String key, String vlaue)
    {
        return _properties.setProperty(key, vlaue);
    }
    
}
