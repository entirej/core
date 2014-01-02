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

import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreRelationProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreRelationPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCoreRelationProperties _relationProperties;
    private EJCoreFormProperties     _formProperties;
    
    private static final String      ELEMENT_RELATION = "relation";
    private static final String      ELEMENT_JOIN     = "join";
    
    public EJCoreRelationPropertiesHandler(EJCoreFormProperties formProperties)
    {
        _formProperties = formProperties;
    }
    
    public EJCoreRelationProperties getRelationProperties()
    {
        return _relationProperties;
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_RELATION))
        {
            _relationProperties = new EJCoreRelationProperties(attributes.getValue("name"));
            EJCoreBlockProperties masterBlock = _formProperties.getBlockProperties(attributes.getValue("masterBlockName"));
            EJCoreBlockProperties detailBlock = _formProperties.getBlockProperties(attributes.getValue("detailBlockName"));
            _relationProperties.setMasterBlockProperties(masterBlock);
            _relationProperties.setDetailBlockProperties(detailBlock);
            
            String value = attributes.getValue("preventMasterlessOperations");
            _relationProperties.setPreventMasterlessOperations(Boolean.parseBoolean((value == null ? "false" : value)));
            value = attributes.getValue("deferredQuery");
            _relationProperties.setDeferredQuery(Boolean.parseBoolean((value == null ? "false" : value)));
            value = attributes.getValue("autoQuery");
            _relationProperties.setAutoQuery(Boolean.parseBoolean((value == null ? "true" : value)));
        }
        else if (name.equals(ELEMENT_JOIN))
        {
            String masterItem = attributes.getValue("masterItem");
            String detailItem = attributes.getValue("detailItem");
            _relationProperties.addJoin(masterItem, detailItem);
        }
    }
    
    @Override
    void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_RELATION))
        {
            quitAsDelegate();
        }
    }
    
}
