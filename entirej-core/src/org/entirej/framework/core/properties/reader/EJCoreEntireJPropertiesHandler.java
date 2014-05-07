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

import org.entirej.framework.core.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.core.properties.EJCoreProperties;
import org.entirej.framework.core.properties.EJCoreRendererAssignment;
import org.entirej.framework.core.properties.containers.EJCoreRendererAssignmentContainer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreEntireJPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCorePropertiesHandlerFactory _handlerFactory;
    private EJCoreProperties               _properties;
    
    protected static final String          APPLICATION_MANAGER_CLASS_NAME = "applicationManager";
    protected static final String          REUSABLE_BLOCK_LOC             = "reusableBlocksLocation";
    protected static final String          REUSABLE_LOV_LOV               = "reusableLovDefinitionLocation";
    protected static final String          OBJECTGROUP_LOC                = "objectGroupDefinitionLocation";
    protected static final String          APPLICATION_PROPERTIES         = "applicationDefinedProperties";
    
    protected static final String          CONNECTION_FACTORY_CLASS_NAME  = "connectionFactoryClassName";
    protected static final String          TRANSLATOR_CLASS_NAME          = "translatorClassName";
    protected static final String          APPLICATION_LEVEL_PARAMETER    = "appicationLevelParameter";
    protected static final String          FORMS_PACKAGE                  = "formsPackage";
    
    protected static final String          FORM_RENDERERS                 = "formRenderers";
    protected static final String          BLOCK_RENDERERS                = "blockRenderers";
    protected static final String          ITEM_RENDERERS                 = "itemRenderers";
    protected static final String          LOV_RENDERERS                  = "lovRenderers";
    protected static final String          APP_COMP_RENDERERS             = "appCompRenderers";
    protected static final String          RENDERER                       = "renderer";
    private static final String            APP_MENU                       = "applicationMenu";
    
    private static final String            VISUAL_ATTRIBUTE               = "visualAttribute";
    
    private static final String            APP_LAYOUT                     = "applicationLayout";
    
    private boolean                        _selectingFormRenderers        = false;
    private boolean                        _selectingBlockRenderers       = false;
    private boolean                        _selectingItemRenderers        = false;
    private boolean                        _selectingLovRenderers         = false;
    private boolean                        _selectingAppCompRenderers     = false;
    
    public EJCoreEntireJPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory)
    {
        _handlerFactory = handlerFactory;
        _properties = EJCoreProperties.getInstance();
    }
    
    @Override
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(RENDERER))
        {
            if (_selectingFormRenderers)
            {
                addFormRenderer(attributes);
                return;
            }
            else if (_selectingBlockRenderers)
            {
                addBlockRenderer(attributes);
                return;
            }
            else if (_selectingItemRenderers)
            {
                addItemRenderer(attributes);
                return;
            }
            else if (_selectingLovRenderers)
            {
                addLovRenderer(attributes);
                return;
            }
            else if (_selectingAppCompRenderers)
            {
                addAppComponentRenderer(attributes);
                return;
            }
        }
        
        if (name.equals(APPLICATION_LEVEL_PARAMETER))
        {
            String paramName = attributes.getValue("name");
            String dataTypeName = attributes.getValue("dataType");
            
            EJApplicationLevelParameter parameter = new EJApplicationLevelParameter(paramName, dataTypeName);
            if(parameter.isValidDefaultValueType())
            {
                parameter.setValue(parameter.toDefaultValue( attributes.getValue("defaultValue")));
            }
            _properties.addApplicationLevelParameter(parameter);
        }
        else if (name.equals(FORMS_PACKAGE))
        {
            _properties.getFormPackageNames().add(attributes.getValue("name"));
        }
        else if (name.equals(FORM_RENDERERS))
        {
            _selectingFormRenderers = true;
        }
        else if (name.equals(BLOCK_RENDERERS))
        {
            _selectingBlockRenderers = true;
        }
        else if (name.equals(ITEM_RENDERERS))
        {
            _selectingItemRenderers = true;
        }
        else if (name.equals(LOV_RENDERERS))
        {
            _selectingLovRenderers = true;
        }
        else if (name.equals(APP_COMP_RENDERERS))
        {
            _selectingAppCompRenderers = true;
        }
        else if (name.equals(VISUAL_ATTRIBUTE))
        {
            setDelegate(new EJCoreVisualAttributePropertiesHandler());
        }
        else if (name.equals(APPLICATION_PROPERTIES))
        {
            setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(null, null, APPLICATION_PROPERTIES));
        }
        else if (name.equals(APP_MENU))
        {
            setDelegate(new EJCoreMenuHandler());
        }
        else if (name.equals(APP_LAYOUT))
        {
            setDelegate(new EJCoreLayoutHandler(_handlerFactory));
        }
    }
    
    @Override
    public void endLocalElement(String name, String value, String untrimmedValue) throws SAXException
    {
        if (name.equals(APPLICATION_MANAGER_CLASS_NAME))
        {
            try
            {
                _properties.setApplicationManagerClassName(value);
            }
            catch (Exception e)
            {
                throw new SAXException(e.getMessage(), e);
            }
        }
        else if (name.equals(CONNECTION_FACTORY_CLASS_NAME))
        {
            try
            {
                _properties.setConnectionFactoryClassName(value);
            }
            catch (Exception e)
            {
                throw new SAXException(e.getMessage(), e);
            }
        }
        else if (name.equals(TRANSLATOR_CLASS_NAME))
        {
            try
            {
                _properties.setApplicationTranslatorClassName(value);
            }
            catch (Exception e)
            {
                throw new SAXException(e.getMessage(), e);
            }
        }
        else if (name.equals(FORMS_PACKAGE))
        {
            _properties.getFormPackageNames().add(value);
        }
        else if (name.equals(REUSABLE_BLOCK_LOC))
        {
            _properties.setReusableBlocksLocation(value);
        }
        else if (name.equals(REUSABLE_LOV_LOV))
        {
            _properties.setReusableLovDefinitionLocation(value);
        }
        else if (name.equals(OBJECTGROUP_LOC))
        {
            _properties.setObjectGroupDefinitionLocation(value);
        }
        else if (name.equals(FORM_RENDERERS))
        {
            _selectingFormRenderers = false;
        }
        else if (name.equals(BLOCK_RENDERERS))
        {
            _selectingBlockRenderers = false;
        }
        else if (name.equals(ITEM_RENDERERS))
        {
            _selectingItemRenderers = false;
        }
        else if (name.equals(LOV_RENDERERS))
        {
            _selectingLovRenderers = false;
        }
        
    }
    
    @Override
    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(VISUAL_ATTRIBUTE))
        {
            _properties.getVisualAttributesContainer().addVisualAttribute(((EJCoreVisualAttributePropertiesHandler) currentDelegate).getProperties());
        }
        else if (name.equals(APPLICATION_PROPERTIES))
        {
            _properties.setApplicationDefinedProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
        }
        else if (name.equals(APP_MENU))
        {
            _properties.getMenuContainer().addMenuProperties(((EJCoreMenuHandler) currentDelegate).getProperties());
        }
        else if (name.equals(APP_LAYOUT))
        {
            _properties.setLayoutContainer(((EJCoreLayoutHandler) currentDelegate).getContainer());
        }
    }
    
    private void addFormRenderer(Attributes attributes)
    {
        String name = attributes.getValue("name");
        String rendererClassName = attributes.getValue("rendererClassName");
        _properties.addFormRendererAssignment(new EJCoreRendererAssignment(name, rendererClassName, EJCoreRendererAssignmentContainer.FORM_RENDERER_TYPE));
    }
    
    private void addAppComponentRenderer(Attributes attributes)
    {
        String name = attributes.getValue("name");
        String rendererClassName = attributes.getValue("rendererClassName");
        _properties.addApplicationComponentRendererAssignment(new EJCoreRendererAssignment(name, rendererClassName,
                EJCoreRendererAssignmentContainer.APP_COMPONENT_RENDERER_TYPE));
    }
    
    private void addBlockRenderer(Attributes attributes)
    {
        String name = attributes.getValue("name");
        String rendererClassName = attributes.getValue("rendererClassName");
        _properties.addBlockRendererAssignment(new EJCoreRendererAssignment(name, rendererClassName, EJCoreRendererAssignmentContainer.BLOCK_RENDERER_TYPE));
    }
    
    private void addItemRenderer(Attributes attributes)
    {
        String name = attributes.getValue("name");
        String rendererClassName = attributes.getValue("rendererClassName");
        _properties.addItemRendererAssignment(new EJCoreRendererAssignment(name, rendererClassName, EJCoreRendererAssignmentContainer.ITEM_RENDERER_TYPE));
    }
    
    private void addLovRenderer(Attributes attributes)
    {
        String name = attributes.getValue("name");
        String rendererClassName = attributes.getValue("rendererClassName");
        _properties.addLovRendererAssignment(new EJCoreRendererAssignment(name, rendererClassName, EJCoreRendererAssignmentContainer.LOV_RENDERER_TYPE));
    }
    
}
