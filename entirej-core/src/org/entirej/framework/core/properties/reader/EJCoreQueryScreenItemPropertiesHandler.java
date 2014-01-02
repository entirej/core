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
import org.entirej.framework.core.properties.EJCoreQueryScreenItemProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreQueryScreenItemPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCoreQueryScreenItemProperties _itemProperties;
    private EJCoreFormProperties            _formProperties;
    private EJCorePropertiesHandlerFactory  _handlerFactory;
    private EJCoreBlockProperties           _blockProperties;
    
    private static final String             ELEMENT_QUERY_SCREEN_RENDERER_PROPERTIES = "queryScreenRendererItemProperties";
    
    private static final String             ELEMENT_ITEM                             = "item";
    private static final String             ELEMENT_LABEL                            = "label";
    private static final String             ELEMENT_HINT                             = "hint";
    private static final String             ELEMENT_EDIT_ALLOWED                     = "editAllowed";
    private static final String             ELEMENT_VISIBLE                          = "visible";
    private static final String             ELEMENT_MANDATORY                        = "mandatory";
    private static final String             ELEMENT_ACTION_COMMAND                   = "actionCommand";
    private static final String             ELEMENT_VALIDATE_FROM_LOV                = "validateFromLov";
    private static final String             ELEMENT_LOV_MAPPING_NAME                 = "lovMappingName";
    private static final String             ELEMENT_ENABLE_LOV_NOTIFICATION          = "enableLovNotification";
    
    public EJCoreQueryScreenItemPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
        _handlerFactory = handlerFactory;
        _formProperties = blockProperties.getFormProperties();
        _itemProperties = new EJCoreQueryScreenItemProperties(blockProperties, false);
    }
    
    public EJCoreQueryScreenItemProperties getItemProperties()
    {
        return _itemProperties;
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_ITEM))
        {
            try
            {
                String isSpacerItem = attributes.getValue("isSpacerItem");
                if (isSpacerItem != null && Boolean.parseBoolean(isSpacerItem))
                {
                    _itemProperties.setIsSpacerItem(true);
                }
                _itemProperties.setReferencedItemName(attributes.getValue("referencedItemName"));
            }
            catch (Exception e)
            {
                throw new SAXException(e.getMessage(), e);
            }
        }
        else if (name.equals(ELEMENT_QUERY_SCREEN_RENDERER_PROPERTIES))
        {
            // Now I am starting the selection of the block required renderer
            // properties
            setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(_formProperties, _blockProperties, ELEMENT_QUERY_SCREEN_RENDERER_PROPERTIES));
        }
    }
    
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_ITEM))
        {
            quitAsDelegate();
            return;
        }
        
        if (name.equals(ELEMENT_LABEL))
        {
            _itemProperties.setBaseLabel(value);
        }
        else if (name.equals(ELEMENT_HINT))
        {
            _itemProperties.setBaseHint(value);
        }
        else if (name.equals(ELEMENT_EDIT_ALLOWED))
        {
            if (value.length() > 0)
            {
                _itemProperties.setEditAllowed(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_VISIBLE))
        {
            if (value.length() > 0)
            {
                _itemProperties.setVisible(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_MANDATORY))
        {
            if (value.length() > 0)
            {
                _itemProperties.setMandatory(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_ACTION_COMMAND))
        {
            _itemProperties.setActionCommand(value);
        }
        else if (name.equals(ELEMENT_LOV_MAPPING_NAME))
        {
            if (value.length() > 0)
            {
                _itemProperties.setLovMappingName(value);
                _itemProperties.getReferencedItemProperties().setLovMappingNameOnQuery(value);
            }
        }
        else if (name.equals(ELEMENT_VALIDATE_FROM_LOV))
        {
            if (value.length() > 0)
            {
                _itemProperties.setValidateFromLov(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_ENABLE_LOV_NOTIFICATION))
        {
            if (value.length() > 0)
            {
                _itemProperties.enableLovNotification(Boolean.parseBoolean(value));
            }
        }
    }
    
    @Override
    protected void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_QUERY_SCREEN_RENDERER_PROPERTIES))
        {
            _itemProperties.setQueryScreenRendererProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
        }
    }
    
}
