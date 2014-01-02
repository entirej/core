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

import org.entirej.framework.core.properties.containers.interfaces.EJCanvasPropertiesContainer;

public interface EJStackedPageProperties extends Serializable
{
    
    public EJCanvasPropertiesContainer getContainedCanvases();
    
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
    public int getNumCols();
    
    /**
     * Returns the name of the visual attribute that will be used when rendering
     * this stacked page
     * 
     * @return the backgroundColourVisualAttributeName
     */
    public String getBackgroundColourVisualAttributeName();
    
    /**
     * @return the foregroundColourVisualAttributeName
     */
    public String getForegroundColourVisualAttributeName();
    
    /**
     * The first navigational block is the block to which navigation will be
     * passed when the user clicks on this tab page
     * <p>
     * this property <b>must</b> be used in conjunction with
     * <code>FirstNavigationalItem</code> otherwise it will have no effect and
     * the focus will remain on the last block or the tab itself
     * 
     * @return Returns the firstNavigationalBlock
     * @see #getFirstNavigationalItem()
     */
    public String getFirstNavigationalBlock();
    
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
    public String getFirstNavigationalItem();
    
    /**
     * Returns the name of this stacked page
     * 
     * @return The name of this stacked page
     */
    public String getName();
    
}
