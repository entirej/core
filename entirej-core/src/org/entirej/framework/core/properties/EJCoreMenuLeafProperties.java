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

public class EJCoreMenuLeafProperties implements Serializable
{
    private EJCoreMenuProperties    _menu;
    private EJCoreMenuLeafContainer _container;
    private String                  _name;
    private String                  _displayName;
    private String                  _baseDisplayName;
    private String                  _iconName;
    
    public EJCoreMenuLeafProperties(EJCoreMenuProperties menu, EJCoreMenuLeafContainer contianer)
    {
        _menu = menu;
        _container = contianer;
    }
    
    public void resetContainer(EJCoreMenuLeafContainer contianer)
    {
        _container = contianer;
    }
    
    public EJCoreMenuProperties getMenu()
    {
        return _menu;
    }
    
    public EJCoreMenuLeafContainer getContainer()
    {
        return _container;
    }
    
    public String getName()
    {
        return _name;
    }
    
    public void setName(String name)
    {
        _name = name;
    }
    
    public String getDisplayName()
    {
        return _displayName;
    }
    
    public void setDisplayName(String displayName)
    {
        _displayName = displayName;
    }
    
    public String getIconName()
    {
        return _iconName;
    }
    
    public void setIconName(String iconName)
    {
        _iconName = iconName;
    }
    
    /**
     * Returns the untranslated display name for this leaf
     * 
     * @return The untranslated display name
     * @see #getHint()
     */
    public String getBaseDisplayName()
    {
        return _baseDisplayName;
    }
    
    /**
     * Used to set the translated display name
     * <p>
     * EntireJ will retrieve the base display name and use the assigned
     * framework translator to translate the text. Once translated, EntireJ will
     * assign the translated text via this method
     * 
     * @param translatedName
     *            The translated display name
     */
    public void setTranslatedDisplayName(String translatedName)
    {
        _displayName = translatedName;
    }
    
    /**
     * Sets this items base display name
     * <p>
     * A base name is an untranslated display name. All display names will be
     * translated using the applications translator. If there is no translator
     * defined for the application then the base hint will be used for the items
     * hint
     * 
     * @param hint
     *            This items base hint
     */
    public void setBaseDisplayName(String displayName)
    {
        if (displayName != null && displayName.trim().length() == 0)
        {
            _baseDisplayName = null;
        }
        else
        {
            _baseDisplayName = displayName;
        }
        
        _displayName = _baseDisplayName;
    }
    
}
