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

import org.entirej.framework.core.enumerations.EJItemGroupAlignment;
import org.entirej.framework.core.enumerations.EJLineStyle;
import org.entirej.framework.core.enumerations.EJSeparatorOrientation;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreItemGroupProperties;
import org.entirej.framework.core.properties.containers.EJCoreItemGroupPropertiesContainer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class EJCoreItemGroupPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCoreItemGroupPropertiesContainer _itemGroupContainer;
    private EJCoreItemGroupProperties          _itemGroupProperties;
    
    private static final String                ITEM                        = "item";
    private static final String                ITEM_GROUP                  = "itemGroup";
    private static final String                ITEM_GROUP_LIST             = "itemGroupList";
    private static final String                DISPLAY_FRAME               = "displayFrame";
    private static final String                FRAME_TITLE                 = "frameTitle";
    private static final String                NUMCOLS                     = "numCols";
    private static final String                WIDTH                       = "width";
    private static final String                HEIGHT                      = "height";
    private static final String                XSPAN                       = "xspan";
    private static final String                YSPAN                       = "yspan";
    private static final String                EXPAND_HORIZONTAL           = "expandHorizontally";
    private static final String                EXPAND_VERTICAL             = "expandVertically";
    private static final String                HORIZONTAL_ALIGNMENT        = "horizontalAlignment";
    private static final String                VERTICAL_ALIGNMENT          = "verticalAlignment";
    private static final String                ELEMENT_RENDERER_PROPERTIES = "rendererProperties";
    /**
     * Indicates the tag name that causes this handler to exit and return to its
     * parent handler
     */
    private String                             _exitTag                    = "";
    private EJCorePropertiesHandlerFactory     _handlerFactory;
    private EJCoreBlockProperties              _blockProperties;
    
    public EJCoreItemGroupPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreBlockProperties blockProperties,
            EJCoreItemGroupPropertiesContainer itemGroupContainer, String exitTag)
    {
        _itemGroupContainer = itemGroupContainer;
        _exitTag = exitTag;
        _handlerFactory = handlerFactory;
        
        _blockProperties = blockProperties;
    }
    
    public EJCoreItemGroupProperties getItemGroupProperties()
    {
        return _itemGroupProperties;
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ITEM_GROUP_LIST) && _itemGroupProperties != null)
        {
            setDelegate(createChildItemGroupHandler(_itemGroupProperties));
        }
        else if (name.equals(ITEM_GROUP))
        {
            _itemGroupProperties = new EJCoreItemGroupProperties(attributes.getValue("name"), _itemGroupContainer);
            
            String isSeparator = attributes.getValue("isSeparator");
            if (isSeparator != null && Boolean.parseBoolean(isSeparator))
            {
                _itemGroupProperties.setSeparator(true);
                String linestyle = attributes.getValue("separatorLineStyle");
                if (linestyle != null )
                {
                    _itemGroupProperties.setSeparatorLineStyle(EJLineStyle.valueOf(linestyle));
                }
                String separatorOrientation = attributes.getValue("separatorOrientation");
                if (separatorOrientation != null )
                {
                    _itemGroupProperties.setSeparatorOrientation(EJSeparatorOrientation.valueOf(separatorOrientation));
                }
            }
            
           
        }
        else if (name.equals(ITEM))
        {
            setDelegate();
        }
        else if (name.equals(ELEMENT_RENDERER_PROPERTIES))
        {
            
            setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(_blockProperties.getFormProperties(), _blockProperties,
                    ELEMENT_RENDERER_PROPERTIES));
        }
    }
    
    public abstract void setDelegate();
    
    public abstract void addItemPropertiesToGroup();
    
    public abstract EJCoreItemGroupPropertiesHandler createChildItemGroupHandler(EJCoreItemGroupProperties itemGroup);
    
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(_exitTag))
        {
            quitAsDelegate();
            return;
        }
        
        if (name.equals(ITEM_GROUP))
        {
            _itemGroupContainer.addItemGroupProperties(_itemGroupProperties);
        }
        else if (name.equals(DISPLAY_FRAME))
        {
            if (value.length() > 0)
            {
                _itemGroupProperties.setDisplayGroupFrame(Boolean.parseBoolean(value));
            }
            else
            {
                _itemGroupProperties.setDisplayGroupFrame(false);
            }
        }
        else if (name.equals(NUMCOLS))
        {
            if (value.length() > 0)
            {
                int numCols = Integer.parseInt(value);
                _itemGroupProperties.setNumCols((numCols > 1 ? numCols : 1));
            }
            else
            {
                _itemGroupProperties.setNumCols(1);
            }
            
        }
        else if (name.equals(FRAME_TITLE))
        {
            _itemGroupProperties.setBaseFrameTitle(value);
            
        }
        else if (name.equals(HEIGHT))
        {
            if (value.length() > 0)
            {
                _itemGroupProperties.setHeight(Integer.parseInt(value));
            }
            else
            {
                _itemGroupProperties.setHeight(0);
            }
        }
        else if (name.equals(WIDTH))
        {
            if (value.length() > 0)
            {
                _itemGroupProperties.setWidth(Integer.parseInt(value));
            }
            else
            {
                _itemGroupProperties.setWidth(0);
            }
        }
        else if (name.equals(XSPAN))
        {
            _itemGroupProperties.setXspan(Integer.parseInt(value));
        }
        else if (name.equals(YSPAN))
        {
            _itemGroupProperties.setYspan(Integer.parseInt(value));
        }
        else if (name.equals(EXPAND_HORIZONTAL))
        {
            _itemGroupProperties.setExpandHorizontally(Boolean.parseBoolean(value));
        }
        else if (name.equals(EXPAND_VERTICAL))
        {
            _itemGroupProperties.setExpandVertically(Boolean.parseBoolean(value));
        }
        else if (name.equals(HORIZONTAL_ALIGNMENT) && value!=null&& value.length()>0)
        {
            _itemGroupProperties.setHorizontalAlignment(EJItemGroupAlignment.valueOf(value));
        }
        else if (name.equals(VERTICAL_ALIGNMENT)&& value!=null&& value.length()>0)
        {
            _itemGroupProperties.setVerticalAlignment(EJItemGroupAlignment.valueOf(value));
        }
    }
    
    @Override
    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(ITEM))
        {
            addItemPropertiesToGroup();
        }
        else if (name.equals(ELEMENT_RENDERER_PROPERTIES))
        {
            _itemGroupProperties.setRendererProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
        }
    }
}
