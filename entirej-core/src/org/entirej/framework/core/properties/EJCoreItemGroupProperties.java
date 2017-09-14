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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.enumerations.EJItemGroupAlignment;
import org.entirej.framework.core.enumerations.EJLineStyle;
import org.entirej.framework.core.enumerations.EJSeparatorOrientation;
import org.entirej.framework.core.properties.containers.EJCoreItemGroupPropertiesContainer;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJItemGroupProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;

public class EJCoreItemGroupProperties implements EJItemGroupProperties
{
    private String                             _name;
    
    private boolean                            _displayGroupFrame;
    private String                             _frameTitle;
    private String                             _baseFrameTitle;
    
    private int                                _numCols;
    private int                                _width;
    private int                                _height;
    private int                                _xspan;
    private int                                _yspan;
    
    private boolean                            _expandHorizontally;
    private boolean                            _expandVertically;
    
    private EJItemGroupAlignment               _horizontalAlignment = EJItemGroupAlignment.FILL;
    private EJItemGroupAlignment               _verticalAlignment = EJItemGroupAlignment.FILL;
    
    private EJFrameworkExtensionProperties     _rendererProperties;
    
    private EJCoreItemGroupPropertiesContainer _parentItemGroupContainer;
    private EJCoreItemGroupPropertiesContainer _childItemGroupContainer;
    private List<EJScreenItemProperties>       _itemProperties;
    

    private boolean                         _isSeparator           = false;
    private EJLineStyle                     _separatorLineStyle    = EJLineStyle.SOLID;
    private EJSeparatorOrientation          _separatorOrientation  = EJSeparatorOrientation.HORIZONTAL;
    
    public EJCoreItemGroupProperties(String name, EJCoreItemGroupPropertiesContainer parentItemGroupContainer)
    {
        _parentItemGroupContainer = parentItemGroupContainer;
        _childItemGroupContainer = new EJCoreItemGroupPropertiesContainer(_parentItemGroupContainer.getContainerType());
        _itemProperties = new ArrayList<EJScreenItemProperties>();
        _name = name;
    }
    
    public EJCoreItemGroupPropertiesContainer getParentItemGroupContainer()
    {
        return _parentItemGroupContainer;
    }
    
    public EJCoreItemGroupPropertiesContainer getChildItemGroupContainer()
    {
        return _childItemGroupContainer;
    }
    
    /**
     * used to set a new name to this item group properties
     * <p>
     * The name will only be set if the name given is not a zero length string
     * 
     * @param name
     *            The new name
     */
    public void setName(String name)
    {
        if (name != null && name.trim().length() > 0)
        {
            _name = name;
        }
    }
    
    /**
     * If set to <code>true</code> then the block renderer should add a frame
     * around the groups items
     * 
     * @param display
     *            The display group frame indicator
     */
    public void setDisplayGroupFrame(boolean display)
    {
        _displayGroupFrame = display;
    }
    
    /**
     * Indicates if a frame should be displayed around the groups items
     * 
     * @return The display indicator
     */
    public boolean dispayGroupFrame()
    {
        return _displayGroupFrame;
    }
    
    /**
     * Sets this items base frame title
     * <p>
     * A base frame title is an untranslated title. All frame titles will be
     * translated using the applications translator. If there is no translator
     * defined for the application then the base frame title will be used for
     * the frame title
     * 
     * @param baseTitle
     *            This items base frame title
     */
    public void setBaseFrameTitle(String baseTitle)
    {
        if (baseTitle != null && baseTitle.trim().length() == 0)
        {
            _baseFrameTitle = null;
        }
        else
        {
            _baseFrameTitle = baseTitle;
        }
        _frameTitle = _baseFrameTitle;
    }
    
    /**
     * Returns the untranslated name that will be displayed within the item
     * groups frame
     * 
     * @return The untranslated frame name for this item group
     */
    public String getBaseFrameTitle()
    {
        return _baseFrameTitle;
    }
    
    /**
     * Used to set the translated title
     * <p>
     * EntireJ will retrieve the base title and use the assigned framework
     * translator to translate the text. Once translated, EntireJ will assign
     * the translated text via this method
     * 
     * @param translatedTitle
     *            The translated title
     */
    public void setTranslatedFrameTitle(String translatedTitle)
    {
        _frameTitle = translatedTitle;
    }
    
    /**
     * Returns the name that will be displayed within the item groups frame
     * 
     * @return The item groups frame
     */
    public String getFrameTitle()
    {
        return _frameTitle;
    }
    
    public void setHeight(int height)
    {
        _height = height;
    }
    
    public int getHeight()
    {
        return _height;
    }
    
    /**
     * Indicates how many display columns this group will have
     * <p>
     * All items being added to this group will be inserted into a grid. The
     * grid will have any number of rows but will be limited to the amount of
     * columns as set by this parameter.
     * <p>
     * Items added to this page can span multiple columns and rows
     * 
     * @return The number of columns defined for this group
     */
    public int getNumCols()
    {
        return _numCols;
    }
    
    /**
     * Sets the number of columns that this group will have
     * <p>
     * All items being added to the group page will be inserted into a grid. The
     * grid will have any number of rows but will be limited to the amount of
     * columns as set by this parameter.
     * 
     * @param numCols
     *            The number of columns to set
     */
    public void setNumCols(int numCols)
    {
        _numCols = numCols;
    }
    
    public void setWidth(int width)
    {
        _width = width;
    }
    
    public int getWidth()
    {
        return _width;
    }
    
    public int getXspan()
    {
        return _xspan;
    }
    
    public void setXspan(int xspan)
    {
        _xspan = xspan;
    }
    
    public int getYspan()
    {
        return _yspan;
    }
    
    public void setYspan(int yspan)
    {
        _yspan = yspan;
    }
    
    public boolean canExpandHorizontally()
    {
        return _expandHorizontally;
    }
    
    public void setExpandHorizontally(boolean expandHorizontally)
    {
        _expandHorizontally = expandHorizontally;
    }
    
    public boolean canExpandVertically()
    {
        return _expandVertically;
    }
    
    public void setExpandVertically(boolean expandVertically)
    {
        _expandVertically = expandVertically;
    }
    
    
    @Override
    public EJItemGroupAlignment getHorizontalAlignment()
    {
        return _horizontalAlignment;
    }
    
    public void setHorizontalAlignment(EJItemGroupAlignment horizontalAlignment)
    {
        this._horizontalAlignment = horizontalAlignment;
    }
    
    @Override
    public EJItemGroupAlignment getVerticalAlignment()
    {
        return _verticalAlignment;
    }
    
    public void setVerticalAlignment(EJItemGroupAlignment verticalAlignment)
    {
        this._verticalAlignment = verticalAlignment;
    }
    
    /**
     * Returns the name of this item group
     * 
     * @return The item group name
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * Indicates if there is an item in this container that already references
     * the given block item name
     * 
     * @param itemName
     *            The name of the item to check for
     * 
     * @return true if the item exists otherwise false
     */
    public boolean containsItemReference(String blockItemName)
    {
        Iterator<EJScreenItemProperties> iti = _itemProperties.iterator();
        
        while (iti.hasNext())
        {
            EJScreenItemProperties props = iti.next();
            if (props.getReferencedItemName().equalsIgnoreCase(blockItemName))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Adds a <code>EJScreenItemProperties<code> object to this blocks item
     * properties store
     * 
     * @param itemProperties
     *            The <code>EJScreenItemProperties</code> to be added
     */
    public void addItemProperties(EJScreenItemProperties itemProperties)
    {
        if (itemProperties != null)
        {
            _itemProperties.add(itemProperties);
        }
    }
    
    /**
     * Returns all <code>EJScreenItemProperties</code> contained within this
     * container
     * 
     * @return All <code>EJScreenItemProperties</code> contained within this
     *         container
     */
    public Collection<EJScreenItemProperties> getAllItemProperties()
    {
        return _itemProperties;
    }
    
    /**
     * Get a <code>EJScreenItemProperties</code> object which references the
     * given block item
     * 
     * @param itemName
     *            The block item name
     * 
     * @return The <code>EJScreenItemProperties</code> which references the given
     *         block item or <code>null</code> if there is no reference to the
     *         given item name
     */
    public EJScreenItemProperties getItemPropertiesForBlockItem(String itemName)
    {
        if (itemName == null)
        {
            return null;
        }
        
        Iterator<EJScreenItemProperties> props = _itemProperties.iterator();
        
        while (props.hasNext())
        {
            EJScreenItemProperties item = props.next();
            
            if (item.getReferencedItemName().equalsIgnoreCase(itemName))
            {
                return item;
            }
        }
        return null;
    }
    
    /**
     * Delete the item that references the given block item name
     * 
     * @param itemName
     *            The name of the block item
     */
    public void deleteItem(String itemName)
    {
        Iterator<EJScreenItemProperties> props = _itemProperties.iterator();
        
        while (props.hasNext())
        {
            EJScreenItemProperties item = props.next();
            
            if (item.getReferencedItemName().equalsIgnoreCase(itemName))
            {
                _itemProperties.remove(item);
                break;
            }
        }
    }
    
    @Override
    public EJFrameworkExtensionProperties getRendererProperties()
    {
        return _rendererProperties;
    }
    
    public void setRendererProperties(EJFrameworkExtensionProperties properties)
    {
        _rendererProperties = properties;
    }
    
    
    @Override
    public EJLineStyle getSeparatorLineStyle()
    {
        return _separatorLineStyle;
    }

    @Override
    public EJSeparatorOrientation getSeparatorOrientation()
    {
        return _separatorOrientation;
    }

    @Override
    public boolean isSeparator()
    {
        return _isSeparator;
    }

    public void setSeparatorLineStyle(EJLineStyle separatorLineStyle)
    {
        _separatorLineStyle = separatorLineStyle;
    }

    public void setSeparatorOrientation(EJSeparatorOrientation separatorOrientation)
    {
        _separatorOrientation = separatorOrientation;
    }

    public void setSeparator(boolean isSeparator)
    {
        _isSeparator = isSeparator;
    }
    
}
