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

import org.entirej.framework.core.properties.EJCoreLayoutContainer;
import org.entirej.framework.core.properties.EJCoreLayoutItem;
import org.entirej.framework.core.properties.EJCoreLayoutItem.FILL;
import org.entirej.framework.core.properties.EJCoreLayoutItem.GRAB;
import org.entirej.framework.core.properties.EJCoreLayoutItem.ItemContainer;
import org.entirej.framework.core.properties.EJCoreLayoutItem.LayoutComponent;
import org.entirej.framework.core.properties.EJCoreLayoutItem.LayoutGroup;
import org.entirej.framework.core.properties.EJCoreLayoutItem.SplitGroup;
import org.entirej.framework.core.properties.EJCoreLayoutItem.TYPE;
import org.entirej.framework.core.properties.EJCoreLayoutItem.TabGroup;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreLayoutHandler extends EJCorePropertiesTagHandler
{
    EJCorePropertiesHandlerFactory _handlerFactory;
    
    public EJCoreLayoutHandler(EJCorePropertiesHandlerFactory _handlerFactory)
    {
        this._handlerFactory = _handlerFactory;
    }
    private EJCoreLayoutContainer container = new EJCoreLayoutContainer();
    
    @Override
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if ("items".equals(name))
        {
            setDelegate(new ItemContainerHandler(container));
        }
    }
    
    public EJCoreLayoutContainer getContainer()
    {
        return container;
    }
    
    @Override
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals("applicationLayout"))
        {
            quitAsDelegate();
            return;
        }
        if (name.equals("title"))
        {
            container.setTitle(value);
        }
        else if (name.equals("col"))
        {
            container.setColumns(getIntValue(value, 0));
        }
        else if (name.equals("width"))
        {
            container.setWidth(getIntValue(value, 0));
        }
        else if (name.equals("height"))
        {
            container.setHeight(getIntValue(value, 0));
        }
        
    }
    
    public static int getIntValue(String value, int defaultVal)
    {
        try
        {
            return Integer.parseInt(value);
        }
        catch (Exception e)
        {
            // ignore
        }
        return defaultVal;
    }
    
    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        
    }
    
    private class ItemContainerHandler extends EJCorePropertiesTagHandler
    {
        private ItemContainer container;
        
        public ItemContainerHandler(ItemContainer container)
        {
            this.container = container;
        }
        
        @Override
        void startLocalElement(String name, Attributes attributes) throws SAXException
        {
            if ("item".equals(name))
            {
                setDelegate(new ItemHandler(container));
            }
            
        }
        
        @Override
        void endLocalElement(String name, String value, String untrimmedValue) throws SAXException
        {
            if (name.equals("items"))
            {
                quitAsDelegate();
                return;
            }
            
        }
        
    }
    private class ItemHandler extends EJCorePropertiesTagHandler
    {
        private final ItemContainer container;
        private EJCoreLayoutItem    item;
        
        public ItemHandler(ItemContainer container)
        {
            this.container = container;
        }
        
        @Override
        void startLocalElement(String name, Attributes attributes) throws SAXException
        {
            if (name.equals("item"))
            {
                String value = attributes.getValue("type");
                if (value != null && value.length() > 0)
                {
                    TYPE type = EJCoreLayoutItem.TYPE.valueOf(value);
                    switch (type)
                    {
                        case GROUP:
                            item = new EJCoreLayoutItem.LayoutGroup();
                            break;
                        case COMPONENT:
                            item = new EJCoreLayoutItem.LayoutComponent();
                            break;
                        case SPLIT:
                            item = new EJCoreLayoutItem.SplitGroup();
                            break;
                        case TAB:
                            item = new EJCoreLayoutItem.TabGroup();
                            break;
                        case SPACE:
                            item = new EJCoreLayoutItem.LayoutSpace();
                            break;
                    
                    }
                    if (item != null) container.addItem(item);
                }
            }
            else if ("items".equals(name) && item instanceof ItemContainer)
            {
                setDelegate(new ItemContainerHandler((ItemContainer) item));
            }
            else if ("rendererProperties".equals(name) && item instanceof LayoutComponent)
            {
                setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(null, null, "rendererProperties"));
            }
            
        }
        
        @Override
        protected void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
        {
            if ("rendererProperties".equals(name) && item instanceof LayoutComponent)
            {
                LayoutComponent component = (LayoutComponent) item;
                component.setRendereProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
            }
        }
        
        @Override
        void endLocalElement(String name, String value, String untrimmedValue) throws SAXException
        {
            if (name.equals("item"))
            {
                quitAsDelegate();
                return;
            }
            else if (item != null && value != null && value.length() > 0)
            {
                if (name.equals("name"))
                {
                    item.setName(value);
                }
                if (name.equals("title"))
                {
                    item.setTitle(value);
                }
                if (name.equals("fill"))
                {
                    FILL fill = FILL.valueOf(value);
                    if (fill != null) item.setFill(fill);
                }
                else if (name.equals("grab"))
                {
                    GRAB grab = GRAB.valueOf(value);
                    if (grab != null) item.setGrab(grab);
                }
                else if (name.equals("hHint"))
                {
                    item.setHintHeight(getIntValue(value, 0));
                }
                else if (name.equals("wHint"))
                {
                    item.setHintWidth(getIntValue(value, 0));
                }
                else if (name.equals("hMin"))
                {
                    item.setMinHight(getIntValue(value, 0));
                }
                else if (name.equals("wMin"))
                {
                    item.setMinWidth(getIntValue(value, 0));
                }
                else if (name.equals("hSpan"))
                {
                    item.setHorizontalSpan(getIntValue(value, 1));
                }
                else if (name.equals("VSpan"))
                {
                    item.setVerticalSpan(getIntValue(value, 1));
                }
                switch (item.getType())
                {
                    case GROUP:
                    {
                        EJCoreLayoutItem.LayoutGroup group = (LayoutGroup) item;
                        if (name.equals("col"))
                        {
                            group.setColumns(getIntValue(value, 1));
                        }
                        else if (name.equals("border"))
                        {
                            group.setBorder(Boolean.parseBoolean(value));
                        }
                        else if (name.equals("hideMargin"))
                        {
                            group.setHideMargin(Boolean.parseBoolean(value));
                        }
                        else if (name.equals("title"))
                        {
                            group.setTitle(value);
                        }
                        break;
                    }
                    
                    case SPACE:
                        // nothing
                        break;
                    case COMPONENT:
                    {
                        EJCoreLayoutItem.LayoutComponent component = (LayoutComponent) item;
                        if (name.equals("renderer"))
                        {
                            component.setRenderer(value);
                        }
                        break;
                    }
                    
                    case SPLIT:
                    {
                        EJCoreLayoutItem.SplitGroup splitGroup = (SplitGroup) item;
                        if (name.equals("orientation"))
                        {
                            SplitGroup.ORIENTATION orientation = SplitGroup.ORIENTATION.valueOf(value);
                            if (orientation != null) splitGroup.setOrientation(orientation);
                        }
                        break;
                    }
                    case TAB:
                    {
                        EJCoreLayoutItem.TabGroup tabGroup = (TabGroup) item;
                        if (name.equals("orientation"))
                        {
                            TabGroup.ORIENTATION orientation = TabGroup.ORIENTATION.valueOf(value);
                            if (orientation != null) tabGroup.setOrientation(orientation);
                        }
                        break;
                    }
                    
                }
            }
            
        }
        
    }
}
