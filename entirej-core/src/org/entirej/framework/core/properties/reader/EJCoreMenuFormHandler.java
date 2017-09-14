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
package org.entirej.framework.core.properties.reader;

import org.entirej.framework.core.properties.EJCoreMenuLeafContainer;
import org.entirej.framework.core.properties.EJCoreMenuLeafFormProperties;
import org.entirej.framework.core.properties.EJCoreMenuLeafProperties;
import org.entirej.framework.core.properties.EJCoreMenuProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreMenuFormHandler extends EJCorePropertiesTagHandler implements EJCoreMenuLeafHandler
{
    private EJCoreMenuProperties         _menu;
    private EJCoreMenuLeafContainer      _container;
    private EJCoreMenuLeafFormProperties _form;
    
    private static final String          LEAF         = "leaf";
    private static final String          NAME         = "name";
    private static final String          DISPLAY_NAME = "displayName";
    private static final String          FORM_NAME    = "formName";
    private static final String          HINT         = "hint";
    private static final String          ICON         = "icon";
    
    public EJCoreMenuFormHandler(EJCoreMenuProperties menu, EJCoreMenuLeafContainer container)
    {
        _menu = menu;
        _container = container;
    }
    
    @Override
    public EJCoreMenuLeafProperties getLeafProperties()
    {
        return _form;
    }
    
    @Override
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(LEAF))
        {
            _form = new EJCoreMenuLeafFormProperties(_menu, _container);
            
            String menuName = attributes.getValue(NAME);
            _form.setName(menuName);
        }
    }
    
    public EJCoreMenuProperties getProperties()
    {
        return _menu;
    }
    
    @Override
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(LEAF))
        {
            quitAsDelegate();
            return;
        }
        
        else if (DISPLAY_NAME.equals(name))
        {
            _form.setBaseDisplayName(value);
        }
        else if (FORM_NAME.equals(name))
        {
            _form.setFormName(value);
        }
        else if (HINT.equals(name))
        {
            _form.setBaseHint(value);
        }
        else if (ICON.equals(name))
        {
            _form.setIconName(value);
        }
    }
}
