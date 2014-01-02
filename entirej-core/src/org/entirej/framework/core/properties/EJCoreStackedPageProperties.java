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

import org.entirej.framework.core.properties.containers.EJCoreCanvasPropertiesContainer;
import org.entirej.framework.core.properties.interfaces.EJStackedPageProperties;

public class EJCoreStackedPageProperties implements Comparable<EJCoreStackedPageProperties>, EJStackedPageProperties
{
    private int                             _numCols                             = 1;
    
    private String                          _name                                = "";
    private boolean                         _initiallyDisplayed                  = false;
    private String                          _backgroundColourVisualAttributeName = "";
    private String                          _foregroundColourVisualAttributeName = "";
    private String                          _firstNavigationalBlock              = "";
    private String                          _firstNavigationalItem               = "";
    
    private EJCoreCanvasPropertiesContainer _containedCanvases;
    
    public EJCoreStackedPageProperties(String name)
    {
        _name = name;
        _containedCanvases = new EJCoreCanvasPropertiesContainer();
    }
    
    public EJCoreCanvasPropertiesContainer getContainedCanvases()
    {
        return _containedCanvases;
    }
    
    public void addContainedCanvas(EJCoreCanvasProperties canvas)
    {
        if (canvas != null)
        {
            _containedCanvases.addCanvasProperties(canvas);
        }
    }
    
    /**
     * Indicates how many display columns this page will have
     * <p>
     * All canvases being added to the tab page will be inserted into a grid.
     * The grid will have any number of rows but will be limited to the amount
     * of columns as set by this parameter.
     * <p>
     * Canvases added to this page can span multiple columns and rows
     * 
     * @return The number of columns defined for this page
     */
    public int getNumCols()
    {
        return _numCols;
    }
    
    /**
     * Sets the number of columns that this page will have
     * <p>
     * All canvases being added to the tab page will be inserted into a grid.
     * The grid will have any number of rows but will be limited to the amount
     * of columns as set by this parameter.
     * 
     * @param numCols
     *            The number of columns to set
     */
    public void setNumCols(int numCols)
    {
        _numCols = numCols;
    }
    
    /**
     * Returns the name of the visual attribute that will be used when rendering
     * this stacked page
     * 
     * @return the backgroundColourVisualAttributeName
     */
    public String getBackgroundColourVisualAttributeName()
    {
        return _backgroundColourVisualAttributeName;
    }
    
    /**
     * Returns the name of the visual attribute that will be used when rendering
     * this stacked page
     * 
     * @param backgroundColourVisualAttributeName
     *            the backgroundColourVisualAttributeName to set
     */
    public void setBackgroundColourVisualAttributeName(String backgroundColourVisualAttributeName)
    {
        _backgroundColourVisualAttributeName = backgroundColourVisualAttributeName;
    }
    
    /**
     * @return the foregroundColourVisualAttributeName
     */
    public String getForegroundColourVisualAttributeName()
    {
        return _foregroundColourVisualAttributeName;
    }
    
    /**
     * @param foregroundColourVisualAttributeName
     *            the foregroundColourVisualAttributeName to set
     */
    public void setForegroundColourVisualAttributeName(String foregroundColourVisualAttributeName)
    {
        _foregroundColourVisualAttributeName = foregroundColourVisualAttributeName;
    }
    
    /**
     * The first navigational block is the block to which navigation will be
     * passed when the user clicks on this tab page
     * <p>
     * this property <b>must</b> be used in conjunction with
     * <code>FirstNavigationalItem</code> otherwise it will have no effect and
     * the focus will remain on the last block or the tab itself
     * 
     * @return Returns the firstNavigationalBlock
     * @see EJCoreTabPageProperties#getFirstNavigationalItem()
     */
    public String getFirstNavigationalBlock()
    {
        return _firstNavigationalBlock;
    }
    
    /**
     * The first navigational block is the block to which navigation will be
     * passed when this stacked page is shown
     * <p>
     * this property <b>must</b> be used in conjunction with
     * <code>FirstNavigationalItem</code> otherwise it will have no effect and
     * the focus will remain on the last block or the tab itself
     * 
     * @param firstNavigationalBlock
     *            The firstNavigationalBlock to set.
     * @throws NullPointerException
     *             if the block name passed is null or of zero length
     */
    public void setFirstNavigationalBlock(String firstNavigationalBlock)
    {
        _firstNavigationalBlock = firstNavigationalBlock;
    }
    
    /**
     * The first navigational item is the item to which navigation will be
     * passed when this stacked page is shown
     * <p>
     * this property <b>must</b> be used in conjunction with
     * <code>FirstNavigationalBlock</code> otherwise it will have no effect and
     * the focus will remain on the last block or the tab itself
     * 
     * @return Returns the firstNavigationalItem
     */
    public String getFirstNavigationalItem()
    {
        return _firstNavigationalItem;
    }
    
    /**
     * The first navigational item is the item to which navigation will be
     * passed when the user clicks on this tab page
     * <p>
     * this property <b>must</b> be used in conjunction with
     * <code>FirstNavigationalBlock</code> otherwise it will have no effect and
     * the focus will remain on the last block or the tab itself
     * 
     * @param firstNavigationalItem
     *            The name of the firstNavigationalItem
     * @throws NullPointerException
     *             if the item name passed is null or of zero length
     * 
     */
    public void setFirstNavigationalItem(String firstNavigationalItem)
    {
        _firstNavigationalItem = firstNavigationalItem;
    }
    
    /**
     * Indicates if this stacked page is initially displayed. If no stacked
     * pages are initially displayed, then the developer must display one
     * programatically if and when needed
     * 
     * @return <code>true</code> if the stacked page is initially displayed
     *         otherwise <code>false</code>
     */
    public boolean isInitiallyDisplayed()
    {
        return _initiallyDisplayed;
    }
    
    /**
     * Returns the name of this stacked page
     * 
     * @return The name of this stacked page
     */
    public String getName()
    {
        return _name;
    }
    
    public int compareTo(EJCoreStackedPageProperties props)
    {
        return getName().compareTo(props == null ? "" : props.getName());
    }
    
}
