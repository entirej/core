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
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreItemPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCorePropertiesHandlerFactory _handlerFactory;
    private EJCoreItemProperties           _itemProperties;
    private EJCoreFormProperties           _formProperties;
    private EJCoreBlockProperties          _blockProperties;
    
    private static final String            ELEMENT_ITEM                     = "item";
    private static final String            ELEMENT_DATA_TYPE_CLASS_NAME     = "dataTypeClassName";
    private static final String            ELEMENT_BLOCK_SERVICE_ITEM       = "blockServiceItem";
    private static final String            ELEMENT_ITEM_RENDERER            = "itemRendererName";
    private static final String            ELEMENT_DEFAULT_INSERT_VALUE     = "defaultInsertValue";
    private static final String            ELEMENT_DEFAULT_QUERY_VALUE      = "defaultQueryValue";
    private static final String            ELEMENT_ITEM_RENDERER_PROPERTIES = "itemRendererProperties";
    
    public EJCoreItemPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
        _formProperties = blockProperties.getFormProperties();
        _itemProperties = new EJCoreItemProperties(blockProperties);
        _handlerFactory = handlerFactory;
    }
    
    public EJCoreItemProperties getItemProperties()
    {
        return _itemProperties;
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_ITEM))
        {
            _itemProperties.setName(attributes.getValue("name"));
        }
        else if (name.equals(ELEMENT_ITEM_RENDERER_PROPERTIES))
        {
            // Now I am starting the selection of the renderer properties
            setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(_formProperties, _blockProperties, ELEMENT_ITEM_RENDERER_PROPERTIES));
        }
    }
    
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_ITEM))
        {
            quitAsDelegate();
            return;
        }
        
        else if (name.equals(ELEMENT_DATA_TYPE_CLASS_NAME))
        {
            _itemProperties.setDataTypeClassName(value);
        }
        else if (name.equals(ELEMENT_BLOCK_SERVICE_ITEM))
        {
            _itemProperties.setBlockServiceItem(Boolean.parseBoolean(value));
        }
        else if (name.equals(ELEMENT_ITEM_RENDERER))
        {
            _itemProperties.setItemRendererName(value);
        }
        else if (name.equals(ELEMENT_DEFAULT_INSERT_VALUE))
        {
            _itemProperties.setDefaultInsertValue(value);
        }
        else if (name.equals(ELEMENT_DEFAULT_QUERY_VALUE))
        {
            _itemProperties.setDefaultQueryValue(value);
        }
    }
    
    @Override
    protected void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_ITEM_RENDERER_PROPERTIES))
        {
            _itemProperties.setItemRendererProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
        }
    }
    
}
