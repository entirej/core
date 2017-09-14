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

import org.entirej.framework.core.enumerations.EJLineStyle;
import org.entirej.framework.core.enumerations.EJSeparatorOrientation;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreMainScreenItemProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreMainScreenItemPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCoreMainScreenItemProperties _itemProperties;
    private EJCorePropertiesHandlerFactory _handlerFactory;
    private EJCoreFormProperties           _formProperties;
    private EJCoreBlockProperties          _blockProperties;
    
    private static final String            ELEMENT_ITEM                      = "item";
    private static final String            ELEMENT_LABEL                     = "label";
    private static final String            ELEMENT_HINT                      = "hint";
    private static final String            ELEMENT_EDIT_ALLOWED              = "editAllowed";
    private static final String            ELEMENT_VISIBLE                   = "visible";
    private static final String            ELEMENT_MANDATORY                 = "mandatory";
    private static final String            ELEMENT_LOV_MAPPING_NAME          = "lovMappingName";
    private static final String            ELEMENT_VALIDATE_FROM_LOV         = "validateFromLov";
    private static final String            ELEMENT_ACTION_COMMAND            = "actionCommand";
    private static final String            ELEMENT_ENABLE_LOV_NOTIFICATION   = "enableLovNotification";
    
    private static final String            ELEMENT_BLOCK_RENDERER_PROPERTIES = "blockRendererRequiredProperties";
    private static final String            ELEMENT_LOV_RENDERER_PROPERTIES   = "lovRendererRequiredProperties";
    
    public EJCoreMainScreenItemPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
        _handlerFactory = handlerFactory;
        _formProperties = blockProperties.getFormProperties();
        _itemProperties = new EJCoreMainScreenItemProperties(blockProperties, false);
    }
    
    public EJCoreMainScreenItemProperties getItemProperties()
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
                    String isSeparator = attributes.getValue("isSeparator");
                    if (isSeparator != null && Boolean.parseBoolean(isSeparator))
                    {
                        _itemProperties.setSeparator(true);
                    }
                    
                    String linestyle = attributes.getValue("separatorLineStyle");
                    if (linestyle != null )
                    {
                        _itemProperties.setSeparatorLineStyle(EJLineStyle.valueOf(linestyle));
                    }
                    String separatorOrientation = attributes.getValue("separatorOrientation");
                    if (separatorOrientation != null )
                    {
                        _itemProperties.setSeparatorOrientation(EJSeparatorOrientation.valueOf(separatorOrientation));
                    }
                }
                _itemProperties.setReferencedItemName(attributes.getValue("referencedItemName"));
            }
            catch (Exception e)
            {
                throw new SAXException(e.getMessage(), e);
            }
        }
        else if (name.equals(ELEMENT_BLOCK_RENDERER_PROPERTIES))
        {
            // Now I am starting the selection of the block required renderer
            // properties
            setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(_formProperties, _blockProperties, ELEMENT_BLOCK_RENDERER_PROPERTIES));
        }
        else if (name.equals(ELEMENT_LOV_RENDERER_PROPERTIES))
        {
            // Now I am starting the selection of the lov required renderer
            // properties
            setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(_formProperties, _blockProperties, ELEMENT_LOV_RENDERER_PROPERTIES));
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
        else if (name.equals(ELEMENT_ENABLE_LOV_NOTIFICATION))
        {
            if (value.length() > 0)
            {
                _itemProperties.enableLovNotification(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_LOV_MAPPING_NAME))
        {
            if (value.length() > 0)
            {
                _itemProperties.setLovMappingName(value);
                _itemProperties.getReferencedItemProperties().setLovMappingNameOnMain(value);
            }
        }
        else if (name.equals(ELEMENT_ACTION_COMMAND))
        {
            _itemProperties.setActionCommand(value);
        }
        else if (name.equals(ELEMENT_VALIDATE_FROM_LOV))
        {
            if (value.length() > 0)
            {
                _itemProperties.setValidateFromLov(Boolean.parseBoolean(value));
            }
        }
    }
    
    @Override
    protected void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_BLOCK_RENDERER_PROPERTIES))
        {
            _itemProperties.setBlockRendererRequiredProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
        }
        else if (name.equals(ELEMENT_LOV_RENDERER_PROPERTIES))
        {
            _itemProperties.setLovRendererRequiredProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
        }
    }
}
