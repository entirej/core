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
package org.entirej.framework.core.properties.interfaces;

import java.io.Serializable;

import org.entirej.framework.core.properties.EJCoreCanvasProperties;

public interface EJMainScreenProperties extends Serializable
{
    
    public EJBlockProperties getBlockProperties();
    
    /**
     * Indicates if this blocks frame should be displayed
     * <p>
     * If a frame title is given, then the block frame will contain the title
     * 
     * @return <code>true</code> if the blocks frame should be displayed
     */
    public boolean getDisplayFrame();
    
    /**
     * Returns the frame title for this block
     * 
     * @return This blocks frame title
     */
    public String getFrameTitle();
    
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
    public int getNumCols();
    
    /**
     * @return Returns the width of this canvas
     */
    public int getWidth();
    
    /**
     * If the canvas has been set as Horizontally Expandable, then the canvas
     * must be expanded if the user stretches the form. This is however
     * dependent upon the actually display renderers used for the application
     * 
     * @return Returns the expand horizontally flag
     */
    public boolean canExpandHorizontally();
    
    /**
     * If the canvas has been set as Vertically Expandable, then the canvas must
     * be expanded if the user stretches the form. This is however dependent
     * upon the actually display renderers used for the application
     */
    public boolean canExpandVertically();
    
    /**
     * @return Returns the height of this canvas
     */
    public int getHeight();
    
    /**
     * The horizontal span of this canvas
     * <p>
     * All canvases will be displayed within a grid. The horizontal span
     * indicates how many columns in the grid this canvas will span
     * 
     * @return Returns the horizontalSpan for this canvas
     * @see EJCoreCanvasProperties#getVerticalSpan()
     */
    public int getHorizontalSpan();
    
    /**
     * The vertical span of this canvas
     * <p>
     * All canvases will be displayed within a grid. The vertical span indicates
     * how many rows in the grid this canvas will span
     * 
     * @return Returns the verticalSpan for this canvas
     * @see EJCoreCanvasProperties#getHorizontalSpan()
     */
    public int getVerticalSpan();
    
}
