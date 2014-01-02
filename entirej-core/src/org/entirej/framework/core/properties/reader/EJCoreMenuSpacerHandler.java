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

import org.entirej.framework.core.properties.EJCoreMenuLeafContainer;
import org.entirej.framework.core.properties.EJCoreMenuLeafProperties;
import org.entirej.framework.core.properties.EJCoreMenuLeafSpacerProperties;
import org.entirej.framework.core.properties.EJCoreMenuProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreMenuSpacerHandler extends EJCorePropertiesTagHandler implements EJCoreMenuLeafHandler
{
    private EJCoreMenuProperties           _menu;
    private EJCoreMenuLeafContainer        _container;
    private EJCoreMenuLeafSpacerProperties _spacer;
    
    private static final String            LEAF = "leaf";
    private static final String            NAME = "name";
    
    public EJCoreMenuSpacerHandler(EJCoreMenuProperties menu, EJCoreMenuLeafContainer container)
    {
        _menu = menu;
        _container = container;
        _spacer = new EJCoreMenuLeafSpacerProperties(menu, container);
    }
    
    @Override
    public EJCoreMenuLeafProperties getLeafProperties()
    {
        return _spacer;
    }
    
    @Override
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(LEAF))
        {
            _spacer = new EJCoreMenuLeafSpacerProperties(_menu, _container);
            
            String menuName = attributes.getValue(NAME);
            _spacer.setName(menuName);
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
    }
}
