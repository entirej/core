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

import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreItemProperties;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.factory.EJCoreFormPropertiesFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreLovDefinitionPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCorePropertiesHandlerFactory            _handlerFactory;
    private EJCoreFrameworkExtensionPropertiesHandler _lovRendererHandler;
    private EJCoreLovDefinitionProperties             _lovDefinitionProperties;
    private EJCoreFormProperties                      _formProperties;
    private boolean                                   _isReferenced               = false;
    private boolean                                   _isCreatingReferecedBlock   = false;
    
    private static final String                       ELEMENT_LOV_DEF             = "lovDefinition";
    private static final String                       ELEMENT_HEIGHT              = "height";
    private static final String                       ELEMENT_AUTOMATIC_REFRESH   = "automaticRefresh";
    private static final String                       ELEMENT_WIDTH               = "width";
    private static final String                       ELEMENT_RENDERER_PROPERTIES = "lovRendererProperties";
    private static final String                       ELEMENT_BLOCK               = "block";
    private static final String                       ELEMENT_ITEM                = "item";
    private static final String                       ELEMENT_ACTION_PROCESSOR    = "actionProcessorClassName";
    
    private EJCoreBlockProperties                     _defaultItemBlockProperties;
    
    public EJCoreLovDefinitionPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreFormProperties formProperties,
            boolean isCreatingReferecedBlock)
    {
        _formProperties = formProperties;
        _handlerFactory = handlerFactory;
        _isCreatingReferecedBlock = isCreatingReferecedBlock;
    }
    
    public EJCoreLovDefinitionProperties getLovDefinitionProperties()
    {
        return _lovDefinitionProperties;
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_LOV_DEF))
        {
            String defName = attributes.getValue("name");
            
            String isReferenced = attributes.getValue("isReferenced");
            String rendererName = attributes.getValue("rendererName");
            String allowUserQuery = attributes.getValue("allowUserQuery");
            String automaticQuery = attributes.getValue("automaticQuery");
            
            if (allowUserQuery == null || allowUserQuery.trim().length() == 0)
            {
                allowUserQuery = null;
            }
            if (automaticQuery == null || automaticQuery.trim().length() == 0)
            {
                automaticQuery = null;
            }
            
            if (Boolean.parseBoolean(isReferenced))
            {
                _isReferenced = true;
                
                String refName = attributes.getValue("referencedLovDefinitionName");
                EJCoreFormPropertiesFactory factory = new EJCoreFormPropertiesFactory(_handlerFactory.getFrameworkManager());
                try
                {
                    _lovDefinitionProperties = factory.createReferencedLovDefinitionProperties(refName);
                }
                catch (Exception e)
                {
                    throw new SAXException(e);
                }
                
                _lovDefinitionProperties.internalSetName(defName);
                _lovDefinitionProperties.setReferencedLovDefinitionName(refName);
                
                _defaultItemBlockProperties = new EJCoreBlockProperties(_handlerFactory.getFrameworkManager(), _formProperties, "dummy", false, false);
            }
            else
            {
                _isReferenced = false;
                _lovDefinitionProperties = new EJCoreLovDefinitionProperties(defName);
                _lovDefinitionProperties.setLovRendererName(rendererName);
                
                _lovDefinitionProperties.setUserQueryAllowed(allowUserQuery == null ? false : Boolean.parseBoolean(allowUserQuery));
                _lovDefinitionProperties.setAutomaticQuery(automaticQuery == null ? false : Boolean.parseBoolean(automaticQuery));
                
            }
        }
        else if (name.equals(ELEMENT_BLOCK))
        {
            if (!_isReferenced)
            {
                setDelegate(_handlerFactory.createBlockHandler(_formProperties, _lovDefinitionProperties));
            }
        }
        else if (name.equals(ELEMENT_ITEM))
        {
            if (_isReferenced)
            {
                setDelegate(_handlerFactory.createItemHandler(_defaultItemBlockProperties));
            }
        }
        else if (name.equals(ELEMENT_RENDERER_PROPERTIES))
        {
            // Now I am starting the selection of the renderer properties
            _lovRendererHandler = _handlerFactory.createFrameworkExtensionPropertiesHandler(_formProperties, null, ELEMENT_RENDERER_PROPERTIES);
            setDelegate(_lovRendererHandler);
        }
    }
    
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_LOV_DEF))
        {
            quitAsDelegate();
            return;
        }
        
        if (!_isReferenced && name.equals(ELEMENT_HEIGHT))
        {
            if (value != null && value.length() > 0)
            {
                _lovDefinitionProperties.setHeight(Integer.parseInt(value.trim()));
            }
        }
        if (!_isReferenced && name.equals(ELEMENT_AUTOMATIC_REFRESH))
        {
            if (value != null && value.length() > 0)
            {
                _lovDefinitionProperties.setAutomaticRefresh(Boolean.valueOf(value.trim()));
            }
        }
        
        if (!_isReferenced && name.equals(ELEMENT_WIDTH))
        {
            if (value != null && value.length() > 0)
            {
                _lovDefinitionProperties.setWidth(Integer.parseInt(value.trim()));
            }
        }
        
        if (!_isReferenced && name.equals(ELEMENT_ACTION_PROCESSOR))
        {
            _lovDefinitionProperties.setActionProcessorClassName(value);
        }
    }
    
    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_BLOCK))
        {
            EJCoreBlockProperties blockProperties = ((EJCoreBlockPropertiesHandler) currentDelegate).getBlockProperties();
            _lovDefinitionProperties.setBlockProperties(blockProperties);
            
        }
        else if (name.equals(ELEMENT_RENDERER_PROPERTIES))
        {
            _lovDefinitionProperties.setLovRendererProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
        }
        
        // I am only interested in the default query values for Lov Items as
        // these can be overridden within the form
        if (name.equals(ELEMENT_ITEM))
        {
            EJCoreItemProperties itemProperties = ((EJCoreItemPropertiesHandler) currentDelegate).getItemProperties();
            if (itemProperties == null)
            {
                return;
            }
            
            // If the item name is null, then this item is for a screen item and
            // should be ignored
            if (itemProperties.getName() == null)
            {
                return;
            }
            
            EJCoreItemProperties lovItemProps = _lovDefinitionProperties.getBlockProperties().getItemProperties(itemProperties.getName());
            if (lovItemProps != null)
            {
                if (itemProperties.getDefaultQueryValue() != null && itemProperties.getDefaultQueryValue().trim().length() > 0)
                {
                    lovItemProps.setDefaultQueryValue(itemProperties.getDefaultQueryValue());
                }
            }
            return;
        }
    }
}
