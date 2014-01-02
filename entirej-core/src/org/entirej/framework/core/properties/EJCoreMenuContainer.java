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
package org.entirej.framework.core.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EJCoreMenuContainer implements Serializable
{
    private List<EJCoreMenuProperties> _menuProperties;
    private EJCoreMenuProperties       _lastAddedMenu;
    
    public EJCoreMenuContainer()
    {
        _menuProperties = new ArrayList<EJCoreMenuProperties>();
    }
    
    public void clear()
    {
        _menuProperties.clear();
        _lastAddedMenu = null;
    }
    
    public void addMenuProperties(EJCoreMenuProperties menuProperties)
    {
        if (menuProperties != null)
        {
            _menuProperties.add(menuProperties);
            _lastAddedMenu = menuProperties;
        }
    }
    
    public void removeMenuProperties(String menuName)
    {
        for (EJCoreMenuProperties menu : _menuProperties)
        {
            if (menu.getName().equalsIgnoreCase(menuName))
            {
                _menuProperties.remove(menu);
                
                if (_lastAddedMenu == menu)
                {
                    _lastAddedMenu = null;
                }
                
                break;
            }
        }
    }
    
    public EJCoreMenuProperties getMenuProperties(String menuName)
    {
        for (EJCoreMenuProperties menu : _menuProperties)
        {
            if (menu.getName().equalsIgnoreCase(menuName))
            {
                return menu;
            }
        }
        return null;
    }
    
    public Collection<EJCoreMenuProperties> getAllMenuProperties()
    {
        return _menuProperties;
    }
    
    public boolean contains(String menuName)
    {
        for (EJCoreMenuProperties menu : _menuProperties)
        {
            if (menu.getName().equalsIgnoreCase(menuName))
            {
                return true;
            }
        }
        return false;
    }
}
