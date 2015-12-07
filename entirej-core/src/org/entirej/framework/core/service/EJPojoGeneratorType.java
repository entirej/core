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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EJPojoGeneratorType
{
    private Collection<EJTableColumn> _columnNames;
    private String                    _className;
    private String                    _packageName;
    private final Map<String, String> _properties = new HashMap<String, String>();

    public String getClassName()
    {
        return _className;
    }

    public void setClassName(String className)
    {
        _className = className;
    }

    public String getPackageName()
    {
        return _packageName;
    }

    public void setPackageName(String packageName)
    {
        _packageName = packageName;
    }

    public Collection<EJTableColumn> getColumns()
    {
        return _columnNames;
    }

    public void setColumnNames(Collection<EJTableColumn> columns)
    {
        _columnNames = columns;
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
