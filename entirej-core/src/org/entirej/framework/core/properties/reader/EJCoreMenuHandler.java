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
package org.entirej.framework.core.properties.reader;

import org.entirej.framework.core.properties.EJCoreMenuProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreMenuHandler extends EJCorePropertiesTagHandler
{
    private EJCoreMenuProperties _menu;
    
    private static final String  APP_MENU                    = "applicationMenu";
    private static final String  LEAF                        = "leaf";
    private static final String  NAME                        = "name";
    private static final String  DEFAULT                     = "default";
    private static final String  ACTION_PROCESSOR_CLASS_NAME = "actionProcessorClassName";
    
    @Override
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(APP_MENU))
        {
            String menuName = attributes.getValue(NAME);
            _menu = new EJCoreMenuProperties(menuName);
            _menu.setActionProcessorClassName(attributes.getValue(ACTION_PROCESSOR_CLASS_NAME));
            _menu.setDefault(Boolean.valueOf(attributes.getValue(DEFAULT)));
        }
        else if (name.equals(LEAF))
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
    
    @Override
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(APP_MENU))
        {
            quitAsDelegate();
            return;
        }
        
    }
    
    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(LEAF))
        {
            _menu.addLeaf(((EJCoreMenuLeafHandler) currentDelegate).getLeafProperties());
            return;
        }
        
    }
    
    public EJCoreMenuProperties getProperties()
    {
        return _menu;
    }
}
