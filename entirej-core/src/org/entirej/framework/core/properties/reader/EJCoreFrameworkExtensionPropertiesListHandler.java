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

import org.entirej.framework.core.extensions.properties.EJCoreFrameworkExtensionPropertyList;
import org.entirej.framework.core.extensions.properties.EJCoreFrameworkExtensionPropertyListEntry;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreFrameworkExtensionPropertiesListHandler extends EJCorePropertiesTagHandler
{
    private EJCoreFrameworkExtensionPropertyList      _propertyList;
    private static final String                       ELEMENT_PROPERTY            = "property";
    private static final String                       ELEMENT_PROPERTY_LIST       = "propertyList";
    private static final String                       ELEMENT_PROPERTY_LIST_ENTRY = "listEntry";
    
    private String                                    _currentPropertyName        = "";
    
    private EJCoreFrameworkExtensionPropertyListEntry _listEntry;
    
    public EJCoreFrameworkExtensionPropertiesListHandler()
    {
    }
    
    public EJCoreFrameworkExtensionPropertyList getPropertyList()
    {
        return _propertyList;
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_PROPERTY_LIST))
        {
            String listName = attributes.getValue("name");
            _propertyList = new EJCoreFrameworkExtensionPropertyList(listName);
        }
        else if (name.equals(ELEMENT_PROPERTY_LIST_ENTRY))
        {
            _listEntry = new EJCoreFrameworkExtensionPropertyListEntry();
        }
        else if (name.equals(ELEMENT_PROPERTY))
        {
            _currentPropertyName = attributes.getValue("name");
        }
    }
    
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (ELEMENT_PROPERTY_LIST.equals(name))
        {
            quitAsDelegate();
            return;
        }
        
        if (name.equals(ELEMENT_PROPERTY_LIST_ENTRY))
        {
            _propertyList.addListEntry(_listEntry);
        }
        else if (name.equals(ELEMENT_PROPERTY))
        {
            _listEntry.addProperty(_currentPropertyName, value);
        }
    }
}
