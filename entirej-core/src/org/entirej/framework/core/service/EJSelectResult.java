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
package org.entirej.framework.core.service;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class EJSelectResult implements Serializable
{
    private HashMap<String, EJSelectResultItem> _items;
    
    public EJSelectResult()
    {
        _items = new HashMap<String, EJSelectResultItem>();
    }
    
    void addItem(String name, Object value)
    {
        if (name != null && (!_items.containsKey(name)))
        {
            _items.put(name, new EJSelectResultItem(name, value));
        }
    }
    
    public Collection<String> getNames()
    {
        return _items.keySet();
    }
    
    public Object getItemValue(String name)
    {
        EJSelectResultItem item = _items.get(name);
        if (item != null)
        {
            return item.getValue();
        }
        
        return null;
    }
}
