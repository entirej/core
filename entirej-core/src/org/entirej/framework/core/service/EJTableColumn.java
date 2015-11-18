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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EJTableColumn implements Serializable
{
    private String           _name;
    private String           _datatypeName;
    private EJParameterType  _parameterType;
    private final Map<String, String> _properties = new HashMap<String, String>();
    
    private boolean          _array;
    private boolean          _struct;
    
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
    
    public boolean isStruct()
    {
        return _struct;
    }
    
    public void setStruct(boolean struct)
    {
        _struct = struct;
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
        if(!_properties.containsKey(key))
        {
            return defaultVlaue;
        }
        return _properties.get(key);
    }

    public Collection<String> getPropertyKeys()
    {
        return (Collection<String>) _properties.keySet();
    }

    public String getProperty(String key)
    {
        return _properties.get(key);
    }

    public Object setProperty(String key, String vlaue)
    {
        return _properties.put(key, vlaue);
    }
    
}
