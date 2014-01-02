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
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.EJCoreLovMappingProperties;
import org.entirej.framework.core.properties.factory.EJCoreFormPropertiesFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreLovMappingPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCorePropertiesHandlerFactory _handlerFactory;
    private EJCoreFormProperties           _formProperties;
    private EJCoreBlockProperties          _blockProperties;
    private EJCoreLovMappingProperties     _lovMappingProperties;
    
    private static final String            ELEMENT_LOV_MAPPING  = "lovMapping";
    private static final String            ELEMENT_ITEM_MAP     = "itemMap";
    private static final String            ELEMENT_DISPLAY_NAME = "displayName";
    
    public EJCoreLovMappingPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreFormProperties formProperties,
            EJCoreBlockProperties blockProperties)
    {
        _handlerFactory = handlerFactory;
        _formProperties = formProperties;
        _blockProperties = blockProperties;
    }
    
    public EJCoreLovMappingProperties getLovMappingProperties()
    {
        return _lovMappingProperties;
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_LOV_MAPPING))
        {
            String mappingName = attributes.getValue("name");
            String lovDefinitionName = attributes.getValue("lovDefinitionName");
            String executeAfterQuery = attributes.getValue("executeAfterQuery");
            String rendererName = attributes.getValue("rendererName");
            
            _lovMappingProperties = new EJCoreLovMappingProperties(_handlerFactory.getFrameworkManager(), _blockProperties, mappingName);
            _lovMappingProperties.setExecuteAfterQuery((executeAfterQuery == null ? Boolean.TRUE.booleanValue() : Boolean.parseBoolean(executeAfterQuery)));
            
            // If I am creating a lov definition then this means that I am
            // loading a reusable lov definition. If this is the case, then I
            // will also need to load all of the referenced lov definitions
            // within the lov. This includes the lov mappings. The lov mappings
            // within a reusable lov definition will also be names of reusable
            // lov definitions. so load them too....
            if (_formProperties.isCreatingLovDefinition())
            {
                loadReusableLovDefinition(lovDefinitionName);
                _lovMappingProperties.setLovDefinitionName(getNewLovName(lovDefinitionName));
            }
            else
            {
                _lovMappingProperties.setLovDefinitionName(lovDefinitionName);
            }
            _lovMappingProperties.setLovRendererName(rendererName);
        }
        else if (name.equals(ELEMENT_ITEM_MAP))
        {
            String blockItemName = attributes.getValue("blockItemName");
            String lovDefItemName = attributes.getValue("lovDefinitionItem");
            
            _lovMappingProperties.addMappingItem(lovDefItemName, blockItemName);
        }
    }
    
    private void loadReusableLovDefinition(String reusableLovDefinitionName) throws SAXException
    {
        EJCoreFormPropertiesFactory factory = new EJCoreFormPropertiesFactory(_handlerFactory.getFrameworkManager());
        try
        {
            EJCoreLovDefinitionProperties lovDefProperties = factory.createReferencedLovDefinitionProperties(reusableLovDefinitionName);
            if (!_formProperties.getLovDefinitionContainer().contains(getNewLovName(reusableLovDefinitionName)))
            {
                lovDefProperties.internalSetName(getNewLovName(reusableLovDefinitionName));
                _formProperties.getLovDefinitionContainer().addLovDefinitionProperties(lovDefProperties);
            }
        }
        catch (Exception e)
        {
            throw new SAXException(e);
        }
    }
    
    private String getNewLovName(String lovDefinitionName)
    {
        return "$EJ$" + lovDefinitionName + "$EJ$";
    }
    
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_LOV_MAPPING))
        {
            quitAsDelegate();
            return;
        }
        
        if (name.equals(ELEMENT_DISPLAY_NAME))
        {
            _lovMappingProperties.setBaseLovDisplayName(value);
        }
    }
}
