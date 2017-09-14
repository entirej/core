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

import org.entirej.framework.core.properties.EJCoreMenuLeafBranchProperties;
import org.entirej.framework.core.properties.EJCoreMenuLeafContainer;
import org.entirej.framework.core.properties.EJCoreMenuLeafProperties;
import org.entirej.framework.core.properties.EJCoreMenuProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreMenuBranchHandler extends EJCorePropertiesTagHandler implements EJCoreMenuLeafHandler
{
    private EJCoreMenuProperties           _menu;
    private EJCoreMenuLeafContainer        _container;
    private EJCoreMenuLeafBranchProperties _branch;
    
    private static final String            LEAF            = "leaf";
    private static final String            NAME            = "name";
    private static final String            DISPLAY_NAME    = "displayName";
    private static final String            ICON            = "icon";
    
    private boolean                        _parentSelected = false;
    
    public EJCoreMenuBranchHandler(EJCoreMenuProperties menu, EJCoreMenuLeafContainer container)
    {
        _menu = menu;
        _container = container;
    }
    
    @Override
    public EJCoreMenuLeafProperties getLeafProperties()
    {
        return _branch;
    }
    
    @Override
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(LEAF))
        {
            if (!_parentSelected)
            {
                _branch = new EJCoreMenuLeafBranchProperties(_menu, _container);
                
                String menuName = attributes.getValue(NAME);
                _branch.setName(menuName);
                _parentSelected = true;
            }
            else
            {
                String type = attributes.getValue("type");
                if ("BRANCH".equals(type))
                {
                    setDelegate(new EJCoreMenuBranchHandler(_menu, _menu));
                }
                else if ("FORM".equals(type))
                {
                    setDelegate(new EJCoreMenuFormHandler(_menu, _menu));
                }
                else if ("ACTION".equals(type))
                {
                    setDelegate(new EJCoreMenuActionHandler(_menu, _menu));
                }
                else if ("SPACER".equals(type))
                {
                    setDelegate(new EJCoreMenuSpacerHandler(_menu, _menu));
                    
                }
            }
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
            _branch.setBaseDisplayName(value);
        }
        else if (ICON.equals(name))
        {
            _branch.setIconName(value);
        }
    }
    
    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(LEAF))
        {
            _branch.addLeaf(((EJCoreMenuLeafHandler) currentDelegate).getLeafProperties());
            return;
        }
        
    }
    
}
