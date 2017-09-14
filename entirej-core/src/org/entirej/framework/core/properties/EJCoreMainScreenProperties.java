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

import org.entirej.framework.core.properties.interfaces.EJMainScreenProperties;

public class EJCoreMainScreenProperties implements EJMainScreenProperties
{
    private EJCoreBlockProperties _blockProperties;
    
    private boolean               _displayFrame;
    private String                _frameTitle         = "";
    private String                _baseFrameTitle     = "";
    private int                   _numCols            = 1;
    private int                   _width              = 0;
    private int                   _height             = 0;
    private int                   _verticalSpan       = 1;
    private int                   _horizontalSpan     = 1;
    private boolean               _expandHorizontally = true;
    private boolean               _expandVertically   = true;
    
    public EJCoreMainScreenProperties(EJCoreBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
    }
    
    public EJCoreBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }
    
    /**
     * Indicates if this blocks frame should be displayed
     * <p>
     * If a frame title is given, then the block frame will contain the title
     * 
     * @return <code>true</code> if the blocks frame should be displayed
     */
    public boolean getDisplayFrame()
    {
        return _displayFrame;
    }
    
    /**
     * Set to <code>true</code> if the blocks frame should be displayed
     * 
     * @param <code>true</code> if the blocks frame should be displayed
     * 
     */
    public void setDisplayFrame(boolean display)
    {
        _displayFrame = display;
    }
    
    /**
     * Returns the frame title for this block
     * 
     * @return This blocks frame title
     */
    public String getFrameTitle()
    {
        if (_frameTitle == null || _frameTitle.trim().length() == 0)
        {
            return _baseFrameTitle;
        }
        
        return _frameTitle;
    }
    
    /**
     * Returns the untranslated frame title of this main screen
     * 
     * @return The untranslated frame title
     */
    public String getBaseFrameTitle()
    {
        return _baseFrameTitle;
    }
    
    /**
     * sets this blocks frame title
     * 
     * @param title
     *            This blocks frame title
     */
    public void setFrameTitle(String title)
    {
        _baseFrameTitle = title;
    }
    
    /**
     * Used to set the translated frame title
     * <p>
     * EntireJ will use the applications translator to translate the base frame
     * title and assign the result to this screen using this method
     * 
     * @param translatedTitle
     *            The translated base frame title
     */
    public void setTranslatedFrameTitle(String translatedTitle)
    {
        _frameTitle = translatedTitle;
    }
    
    /**
     * Indicates in how many columns this blocks main screen items will be
     * displayed
     * <p>
     * All canvases being added to the tab page will be inserted into a grid.
     * The grid will have any number of rows but will be limited to the amount
     * of columns as set by this parameter.
     * <p>
     * If the main screen renderer does not lay out its items within a grid then
     * the default value of 1 should be used
     * 
     * @return The number of columns to use for the main screen of this block
     */
    public int getNumCols()
    {
        return _numCols;
    }
    
    /**
     * Sets the number of columns that this blocks main screen will have
     * 
     * @param numCols
     *            The number of columns to set
     */
    public void setNumCols(int numCols)
    {
        _numCols = numCols;
    }
    
    /**
     * @return Returns the width of this canvas
     */
    public int getWidth()
    {
        return _width;
    }
    
    /**
     * @param width
     *            The width of this canvas
     */
    public void setWidth(int width)
    {
        _width = width;
    }
    
    /**
     * If the canvas has been set as Horizontally Expandable, then the canvas
     * must be expanded if the user stretches the form. This is however
     * dependent upon the actually display renderers used for the application
     * 
     * @return Returns the expand horizontally flag
     */
    public boolean canExpandHorizontally()
    {
        return _expandHorizontally;
    }
    
    /**
     * @param expand
     *            Sets the horizontally expandable flag for this canvas
     */
    public void setExpandHorizontally(boolean expand)
    {
        _expandHorizontally = expand;
    }
    
    /**
     * If the canvas has been set as Vertically Expandable, then the canvas must
     * be expanded if the user stretches the form. This is however dependent
     * upon the actually display renderers used for the application
     */
    public boolean canExpandVertically()
    {
        return _expandVertically;
    }
    
    /**
     * @param expand
     *            The vertically expandable flag for this canvas
     */
    public void setExpandVertically(boolean expand)
    {
        _expandVertically = expand;
    }
    
    /**
     * @return Returns the height of this canvas
     */
    public int getHeight()
    {
        return _height;
    }
    
    public void setHeight(int height)
    {
        _height = height;
    }
    
    /**
     * The horizontal span of this canvas
     * <p>
     * All canvases will be displayed within a grid. The horizontal span
     * indicates how many columns in the grid this canvas will span
     * 
     * @return Returns the horizontalSpan for this canvas
     * @see EJCoreCanvasProperties#getVerticalSpan()
     */
    public int getHorizontalSpan()
    {
        return _horizontalSpan;
    }
    
    /**
     * @param horizontalSpan
     *            The horizontalSpan of this block
     * @see EJCoreCanvasProperties#getHorizontalSpan()
     * @see EJCoreCanvasProperties#getVerticalSpan()
     */
    public void setHorizontalSpan(int horizontalSpan)
    {
        _horizontalSpan = horizontalSpan;
    }
    
    /**
     * The vertical span of this canvas
     * <p>
     * All canvases will be displayed within a grid. The vertical span indicates
     * how many rows in the grid this canvas will span
     * 
     * @return Returns the verticalSpan for this canvas
     * @see EJCoreCanvasProperties#getHorizontalSpan()
     */
    public int getVerticalSpan()
    {
        return _verticalSpan;
    }
    
    /**
     * @param verticalSpan
     *            The verticalSpan of this canvas
     */
    public void setVerticalSpan(int verticalSpan)
    {
        _verticalSpan = verticalSpan;
    }
    
    /**
     * Generates a copy of these main screen properties
     * 
     * @return A copy of these properties
     */
    public EJCoreMainScreenProperties makeCopy(EJCoreBlockProperties blockProperties)
    {
        EJCoreMainScreenProperties mainScreenProperties = new EJCoreMainScreenProperties(blockProperties);
        mainScreenProperties.setDisplayFrame(_displayFrame);
        mainScreenProperties.setFrameTitle(_frameTitle);
        mainScreenProperties.setNumCols(_numCols);
        mainScreenProperties.setExpandHorizontally(_expandHorizontally);
        mainScreenProperties.setExpandVertically(_expandVertically);
        mainScreenProperties.setVerticalSpan(_verticalSpan);
        mainScreenProperties.setHorizontalSpan(_horizontalSpan);
        mainScreenProperties.setWidth(_width);
        mainScreenProperties.setHeight(_height);
        return mainScreenProperties;
        
    }
}
