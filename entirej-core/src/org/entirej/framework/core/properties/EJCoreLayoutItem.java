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
package org.entirej.framework.core.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;

public class EJCoreLayoutItem implements Serializable
{
    public enum FILL
    {
        HORIZONTAL, BOTH, VERTICAL, NONE
    }
    public enum GRAB
    {
        HORIZONTAL, BOTH, VERTICAL, NONE
    }
    
    public enum TYPE
    {
        GROUP, SPACE, COMPONENT, SPLIT, TAB
    }
    
    private final TYPE type;
    
    private String     name;
    private String     title;
    private int        hintHeight;
    private int        hintWidth;
    private int        minHeight;
    private int        minWidth;
    private int        horizontalSpan = 1;
    private int        verticalSpan   = 1;
    
    private FILL       fill           = FILL.BOTH;
    private GRAB       grab           = GRAB.BOTH;
    
    public EJCoreLayoutItem(TYPE type)
    {
        this.type = type;
    }
    
    public TYPE getType()
    {
        return type;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public int getHintHeight()
    {
        return hintHeight;
    }
    
    public void setHintHeight(int hintHeight)
    {
        this.hintHeight = hintHeight;
    }
    
    public int getHintWidth()
    {
        return hintWidth;
    }
    
    public void setHintWidth(int hintWidth)
    {
        this.hintWidth = hintWidth;
    }
    
    public int getMinHeight()
    {
        return minHeight;
    }
    
    public void setMinHight(int minHeight)
    {
        this.minHeight = minHeight;
    }
    
    public int getMinWidth()
    {
        return minWidth;
    }
    
    public void setMinWidth(int minWidth)
    {
        this.minWidth = minWidth;
    }
    
    public int getHorizontalSpan()
    {
        return horizontalSpan;
    }
    
    public void setHorizontalSpan(int horizontalSpan)
    {
        this.horizontalSpan = horizontalSpan;
    }
    
    public int getVerticalSpan()
    {
        return verticalSpan;
    }
    
    public void setVerticalSpan(int verticalSpan)
    {
        this.verticalSpan = verticalSpan;
    }
    
    public FILL getFill()
    {
        return fill;
    }
    
    public void setFill(FILL fill)
    {
        this.fill = fill;
    }
    
    public GRAB getGrab()
    {
        return grab;
    }
    
    public void setGrab(GRAB grab)
    {
        this.grab = grab;
    }
    
    public static interface ItemContainer extends Serializable
    {
        
        public abstract void addItem(EJCoreLayoutItem item);
        
        public abstract void addItem(int index, EJCoreLayoutItem item);
        
        public abstract boolean addAllItems(Collection<? extends EJCoreLayoutItem> items);
        
        public abstract void clearItems();
        
        public abstract boolean removeItem(EJCoreLayoutItem item);
        
        public abstract List<EJCoreLayoutItem> getItems();
        
    }
    
    public static class LayoutGroup extends EJCoreLayoutItem implements ItemContainer
    {
        private boolean                border;
        private int                    columns = 1;
        private boolean                hideMargin;
        
        private List<EJCoreLayoutItem> items   = new ArrayList<EJCoreLayoutItem>();
        
        public LayoutGroup()
        {
            super(TYPE.GROUP);
        }
        
        public int getColumns()
        {
            return columns;
        }
        
        public void setColumns(int columns)
        {
            this.columns = columns;
        }
        
        public boolean isBorder()
        {
            return border;
        }
        
        public void setBorder(boolean border)
        {
            this.border = border;
        }
        
        
        
        public void addItem(EJCoreLayoutItem item)
        {
            items.add(item);
        }
        
        public void addItem(int index, EJCoreLayoutItem item)
        {
            items.add(index, item);
        }
        
        public boolean addAllItems(Collection<? extends EJCoreLayoutItem> items)
        {
            return this.items.addAll(items);
        }
        
        public void clearItems()
        {
            items.clear();
        }
        
        public boolean removeItem(EJCoreLayoutItem item)
        {
            return items.remove(item);
        }
        
        public List<EJCoreLayoutItem> getItems()
        {
            return items;// new ArrayList<>(items);
        }
        
        public void setHideMargin(boolean hideMargin)
        {
            this.hideMargin = hideMargin;
        }
        
        public boolean isHideMargin()
        {
            return hideMargin;
        }
        
    }
    
    public static class SplitGroup extends EJCoreLayoutItem implements ItemContainer
    {
        
        private List<EJCoreLayoutItem> items = new ArrayList<EJCoreLayoutItem>();
        public enum ORIENTATION
        {
            HORIZONTAL, VERTICAL
        }
        
        private ORIENTATION orientation = ORIENTATION.HORIZONTAL;
        
        public SplitGroup()
        {
            super(TYPE.SPLIT);
        }
        
        public ORIENTATION getOrientation()
        {
            return orientation;
        }
        
        public void setOrientation(ORIENTATION orientation)
        {
            this.orientation = orientation;
        }
        
        public void addItem(EJCoreLayoutItem item)
        {
            items.add(item);
        }
        
        public void addItem(int index, EJCoreLayoutItem item)
        {
            items.add(index, item);
        }
        
        public boolean addAllItems(Collection<? extends EJCoreLayoutItem> items)
        {
            return this.items.addAll(items);
        }
        
        public void clearItems()
        {
            items.clear();
        }
        
        public boolean removeItem(EJCoreLayoutItem item)
        {
            return items.remove(item);
        }
        
        public List<EJCoreLayoutItem> getItems()
        {
            return items;// new ArrayList<>(items);
        }
        
    }
    
    public static class TabGroup extends EJCoreLayoutItem implements ItemContainer
    {
        
        private List<EJCoreLayoutItem> items = new ArrayList<EJCoreLayoutItem>();
        public enum ORIENTATION
        {
            TOP, BOTTOM
        }
        
        private ORIENTATION orientation = ORIENTATION.TOP;
        
        public TabGroup()
        {
            super(TYPE.TAB);
        }
        
        public ORIENTATION getOrientation()
        {
            return orientation;
        }
        
        public void setOrientation(ORIENTATION orientation)
        {
            this.orientation = orientation;
        }
        
        public void addItem(EJCoreLayoutItem item)
        {
            items.add(item);
        }
        
        public void addItem(int index, EJCoreLayoutItem item)
        {
            items.add(index, item);
        }
        
        public boolean addAllItems(Collection<? extends EJCoreLayoutItem> items)
        {
            return this.items.addAll(items);
        }
        
        public void clearItems()
        {
            items.clear();
        }
        
        public boolean removeItem(EJCoreLayoutItem item)
        {
            return items.remove(item);
        }
        
        public List<EJCoreLayoutItem> getItems()
        {
            return items;// new ArrayList<>(items);
        }
        
    }
    
    public static class LayoutSpace extends EJCoreLayoutItem
    {
        public LayoutSpace()
        {
            super(TYPE.SPACE);
        }
    }
    
    public static class LayoutComponent extends EJCoreLayoutItem
    {
        private String                         renderer;
        private EJFrameworkExtensionProperties rendereProperties;
        
        public LayoutComponent()
        {
            super(TYPE.COMPONENT);
        }
        
        public String getRenderer()
        {
            return renderer;
        }
        
        public void setRenderer(String renderer)
        {
            this.renderer = renderer;
        }
        
        public EJFrameworkExtensionProperties getRendereProperties()
        {
            return rendereProperties;
        }
        
        public void setRendereProperties(EJFrameworkExtensionProperties rendereProperties)
        {
            this.rendereProperties = rendereProperties;
        }
        
    }
}
