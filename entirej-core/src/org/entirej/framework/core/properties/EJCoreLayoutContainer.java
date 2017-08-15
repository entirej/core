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
package org.entirej.framework.core.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.entirej.framework.core.properties.EJCoreLayoutItem.ItemContainer;

public class EJCoreLayoutContainer implements ItemContainer
{
    
    private int                    _width;
    private int                    _height;
    private int                    columns = 1;
    private List<EJCoreLayoutItem> items   = new ArrayList<EJCoreLayoutItem>();
    
    public int getColumns()
    {
        return columns;
    }
    
    public void setColumns(int columns)
    {
        this.columns = columns;
    }
    
    public String getTitle()
    {
        return _title;
    }
    
    public void setTitle(String title)
    {
        this._title = title;
    }
    
    public int getWidth()
    {
        return _width;
    }
    
    public void setWidth(int width)
    {
        this._width = width;
    }
    
    public int getHeight()
    {
        return _height;
    }
    
    public void setHeight(int height)
    {
        this._height = height;
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
