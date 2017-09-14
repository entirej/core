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

import org.entirej.framework.core.extensions.properties.EJCoreFrameworkExtensionProperties;
import org.entirej.framework.core.extensions.properties.EJCoreFrameworkExtensionProperty;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.definitions.EJPropertyDefinitionType;
import org.entirej.framework.core.properties.factory.EJCoreFormPropertiesFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreFrameworkExtensionPropertiesHandler extends EJCorePropertiesTagHandler
{
    private String                             _exitTag              = "";
    private EJCorePropertiesHandlerFactory     _handlerFactory;
    private EJCoreFormProperties               _formProperties;
    private EJCoreBlockProperties              _blockProperties;
    private EJCoreFrameworkExtensionProperties _mainPropertyGroup;
    private EJCoreFrameworkExtensionProperties _currentPropertyGroup;
    
    private static final String                ELEMENT_GROUP         = "propertyGroup";
    private static final String                ELEMENT_PROPERTY      = "property";
    private static final String                ELEMENT_PROPERTY_LIST = "propertyList";
    
    private EJCoreFrameworkExtensionProperty   _property;
    
    public EJCoreFrameworkExtensionPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreFormProperties formProperties,
            EJCoreBlockProperties blockProperties, String exitTag)
    {
        _handlerFactory = handlerFactory;
        _formProperties = formProperties;
        _mainPropertyGroup = new EJCoreFrameworkExtensionProperties(formProperties, blockProperties, "MAIN", null);
        _currentPropertyGroup = _mainPropertyGroup;
        _exitTag = exitTag;
    }
    
    public EJCoreFrameworkExtensionProperties getMainPropertiesGroup()
    {
        return _mainPropertyGroup;
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_GROUP))
        {
            // If the element being read is a renderer group then retrieve the
            // group from the current group. If the group does not exist, then
            // there is an error in the configuration
            EJCoreFrameworkExtensionProperties props = new EJCoreFrameworkExtensionProperties(_formProperties, _blockProperties, attributes.getValue("name"),
                    _currentPropertyGroup);
            _currentPropertyGroup.addPropertyGroup(props);
            _currentPropertyGroup = props;
        }
        else if (name.equals(ELEMENT_PROPERTY))
        {
            String propertyName = attributes.getValue("name");
            String multilingual = attributes.getValue("multilingual");
            EJPropertyDefinitionType propertyType = EJPropertyDefinitionType.valueOf(attributes.getValue("propertyType"));
            _property = new EJCoreFrameworkExtensionProperty(propertyType, propertyName, Boolean.parseBoolean(multilingual));
        }
        else if (name.equals(ELEMENT_PROPERTY_LIST))
        {
            setDelegate(_handlerFactory.createFrameworkExtensionPropertiesListHandler());
        }
    }
    
    public void endLocalElement(String name, String value, String untrimmedValue) throws SAXException
    {
        if (_exitTag.equals(name))
        {
            quitAsDelegate();
            return;
        }
        
        if (name.equals(ELEMENT_GROUP))
        {
            // If I have come to the end of a group, then set the current
            // group to its parent
            _currentPropertyGroup = (EJCoreFrameworkExtensionProperties) _currentPropertyGroup.getParentGroup();
        }
        else if (name.equals(ELEMENT_PROPERTY))
        {
            // I need to check if the value entered is a name of a reusable lov
            // definition. If it is, then I need to load the definition. The lov
            // definition need only be loaded if the form being loaded is
            // actually the reusable lov definition itself.
            if (_formProperties != null
                    && _formProperties.isCreatingLovDefinition()
                    && (_property.getPropertyType() == EJPropertyDefinitionType.LOV_DEFINITION || _property.getPropertyType() == EJPropertyDefinitionType.LOV_DEFINITION_WITH_ITEMS))
            {
                String lovDefinitionName;
                if (value.indexOf('.') == -1)
                {
                    lovDefinitionName = value;
                }
                else
                {
                    lovDefinitionName = value.substring(0, value.indexOf('.'));
                }
                
                loadReusableLovDefinition(lovDefinitionName);
                _property.setValue(getNewLovName(lovDefinitionName) + value.substring(value.indexOf('.')));
            }
            _property.setValue(value);
            _currentPropertyGroup.addProperty(_property);
        }
    }
    
    private String getNewLovName(String lovDefinitionName)
    {
        if (_blockProperties.isReferenceBlock())
        {
            return _blockProperties.getName() + "$EJ$" + lovDefinitionName + "$EJ$";
        }
        return "$EJ$" + lovDefinitionName + "$EJ$";
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
    
    @Override
    protected void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_PROPERTY_LIST))
        {
            _currentPropertyGroup.addPropertyList(((EJCoreFrameworkExtensionPropertiesListHandler) currentDelegate).getPropertyList());
        }
    }
}
