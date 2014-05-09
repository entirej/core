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

import org.entirej.framework.core.data.controllers.EJInternalFormParameter;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreCanvasProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.interfaces.EJCanvasProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreFormPropertiesHandler extends EJCorePropertiesTagHandler
{
    private boolean                        _isCreatingLovDefinition    = false;
    private boolean                        _isCreatingReferecedBlock   = false;
    private EJCorePropertiesHandlerFactory _handlerFactory;
    private EJCoreFormProperties           _formProperties;
    
    private static final String            ELEMENT_FORM_TITLE          = "formTitle";
    private static final String            ELEMENT_FORM_WIDTH          = "formWidth";
    private static final String            ELEMENT_FORM_HEIGHT         = "formHeight";
    private static final String            ELEMENT_NUM_COLS            = "numCols";
    private static final String            ELEMENT_ACTION_PROCESSOR    = "actionProcessorClassName";
    private static final String            ELEMENT_RENDERER_NAME       = "formRendererName";
    private static final String            ELEMENT_RENDERER_PROPERTIES = "formRendererProperties";
    private static final String            ELEMENT_FORM_PARAMETER      = "formParameter";
    private static final String            ELEMENT_CANVAS              = "canvas";
    private static final String            ELEMENT_BLOCK               = "block";
    private static final String            ELEMENT_BLOCK_GROUP         = "blockGroup";
    private static final String            ELEMENT_RELATION            = "relation";
    private static final String            ELEMENT_LOV_DEFINITION      = "lovDefinition";
    private static final String            ELEMENT_OBJGROUP_DEFINITION = "objGroupDefinition";
    
    public EJCoreFormPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, String formName, boolean isCreatingLovDefinition,
            boolean isCreatingReferecedBlock)
    {
        _isCreatingLovDefinition = isCreatingLovDefinition;
        _isCreatingReferecedBlock = isCreatingReferecedBlock;
        _handlerFactory = handlerFactory;
        _formProperties = new EJCoreFormProperties(handlerFactory.getFrameworkManager(), formName, isCreatingLovDefinition, isCreatingReferecedBlock);
    }
    
    public boolean isCreatingLovDefinition()
    {
        return _isCreatingLovDefinition;
    }
    
    public EJCoreFormProperties getFormProperties()
    {
        return _formProperties;
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        // Now process the FORM PROPERTIES elements
        if (name.equals(ELEMENT_RENDERER_PROPERTIES))
        {
            setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(_formProperties, null, ELEMENT_RENDERER_PROPERTIES));
        }
        else if (name.equals(ELEMENT_CANVAS))
        {
            setDelegate(_handlerFactory.createCanvasHandler());
        }
        else if (name.equals(ELEMENT_BLOCK))
        {
            setDelegate(_handlerFactory.createBlockHandler(_formProperties, null));
        }
        else if (name.equals(ELEMENT_BLOCK_GROUP))
        {
            setDelegate(_handlerFactory.createBlockGroupHandler(_formProperties));
        }
        else if (name.equals(ELEMENT_RELATION))
        {
            setDelegate(_handlerFactory.createRelationHandler(_formProperties));
        }
        else if (name.equals(ELEMENT_LOV_DEFINITION))
        {
            setDelegate(_handlerFactory.createLovDefinitionHandler(_formProperties, _isCreatingReferecedBlock));
        }
        else if (name.equals(ELEMENT_OBJGROUP_DEFINITION))
        {
            setDelegate(_handlerFactory.createObjectGroupDefinitionHandler(_formProperties));
        }
        else if (name.equals(ELEMENT_FORM_PARAMETER))
        {
            String paramName = attributes.getValue("name");
            String dataTypeName = attributes.getValue("dataType");
            
            EJInternalFormParameter parameter = new EJInternalFormParameter(paramName, dataTypeName);
            if(parameter.isValidDefaultValueType())
            {
                parameter.setValue(parameter.toDefaultValue( attributes.getValue("defaultValue")));
            }
            _formProperties.addFormParameter(parameter);
        }
    }
    
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_FORM_TITLE))
        {
            _formProperties.setBaseTitle(value);
        }
        else if (name.equals(ELEMENT_FORM_HEIGHT))
        {
            if (value.length() > 0)
            {
                _formProperties.setFormHeight(Integer.parseInt(value));
            }
            else
            {
                _formProperties.setFormHeight(0);
            }
        }
        else if (name.equals(ELEMENT_FORM_WIDTH))
        {
            if (value.length() > 0)
            {
                _formProperties.setFormWidth(Integer.parseInt(value));
            }
            else
            {
                _formProperties.setFormWidth(0);
            }
        }
        else if (name.equals(ELEMENT_NUM_COLS))
        {
            if (value.length() > 0)
            {
                _formProperties.setNumCols(Integer.parseInt(value));
            }
            else
            {
                _formProperties.setNumCols(1);
            }
        }
        else if (name.equals(ELEMENT_ACTION_PROCESSOR))
        {
            _formProperties.setActionProcessorClassName(value);
        }
        else if (name.equals(ELEMENT_RENDERER_NAME))
        {
            _formProperties.setFormRendererName(value);
        }
    }
    
    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_CANVAS))
        {
            _formProperties.addCanvasProperties(((EJCoreCanvasPropertiesHandler) currentDelegate).getCanvasProperties());
        }
        else if (name.equals(ELEMENT_BLOCK))
        {
            EJCoreBlockProperties blockProperties = ((EJCoreBlockPropertiesHandler) currentDelegate).getBlockProperties();
            _formProperties.addBlockProperties(blockProperties);
            EJCanvasProperties canvasProperties = _formProperties.getCanvasProperties(blockProperties.getCanvasName());
            if (canvasProperties != null)
            {
                ((EJCoreCanvasProperties) canvasProperties).setBlockProperties(blockProperties);
            }
            blockProperties.getScreenItemGroupContainer(EJScreenType.MAIN);
            return;
        }
        else if (name.equals(ELEMENT_BLOCK_GROUP))
        {
            
            return;
        }
        else if (name.equals(ELEMENT_RELATION))
        {
            _formProperties.addRelationProperties(((EJCoreRelationPropertiesHandler) currentDelegate).getRelationProperties());
            return;
        }
        else if (name.equals(ELEMENT_LOV_DEFINITION))
        {
            _formProperties.addLovDefinitionProperties(((EJCoreLovDefinitionPropertiesHandler) currentDelegate).getLovDefinitionProperties());
            return;
        }
        else if (name.equals(ELEMENT_OBJGROUP_DEFINITION))
        {
           
            return;
        }
        else if (name.equals(ELEMENT_RENDERER_PROPERTIES))
        {
            _formProperties.setFormRendererProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
        }
    }
    
}
