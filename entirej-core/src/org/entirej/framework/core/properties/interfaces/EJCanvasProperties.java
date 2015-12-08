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

import org.entirej.framework.core.enumerations.EJCanvasLineStyle;
import org.entirej.framework.core.enumerations.EJCanvasMessagePosition;
import org.entirej.framework.core.enumerations.EJCanvasSplitOrientation;
import org.entirej.framework.core.enumerations.EJCanvasTabPosition;
import org.entirej.framework.core.enumerations.EJCanvasType;
import org.entirej.framework.core.enumerations.EJPopupButton;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreCanvasProperties;
import org.entirej.framework.core.properties.EJCoreStackedPageProperties;
import org.entirej.framework.core.properties.containers.interfaces.EJCanvasPropertiesContainer;
import org.entirej.framework.core.properties.containers.interfaces.EJStackedPagePropertiesContainer;
import org.entirej.framework.core.properties.containers.interfaces.EJTabPagePropertiesContainer;

public interface EJCanvasProperties extends Serializable
{
    
    /**
     * If this canvas is a {@link EJCanvasType#BLOCK} then this method will
     * return the block properties of the assigned block or <code>null</code> if
     * no block has been assigned
     * <p>
     * If the canvas type is not {@link EJCanvasType#BLOCK} then this method
     * will always return <code>null</code>
     * 
     * @return The {@link EJCoreBlockProperties} for the block that is displayed
     *         on this canvas or <code>null</code> if this is not a
     *         {@link EJCanvasType#BLOCK} or no block has been assigned to this
     *         block
     */
    public EJBlockProperties getBlockProperties();
    
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
     * Returns the name of this canvases content canvas i.e. the name of the
     * canvas upon which this canvas will be displayed, or null if no content
     * canvas has been defined
     * <p>
     * If there has been no content canvas defined then the canvas display
     * properties will correspond to actual display positions on the for,
     * 
     * @return the contentCanvasName
     */
    public String getContentCanvasName();
    
    /**
     * If the content canvas is a tab canvas then a tab page must be chosen
     * <p>
     * If no tab page is chosen then this canvas cannot be displayed
     * 
     * @return The name of the tab page upon which this canvas should be
     *         displayed
     */
    public String getContentCanvasTabPageName();
    
    /**
     * If the content canvas is a stacked canvas then a stacked page must be
     * chosen
     * <p>
     * If no stacked page is chosen then this canvas cannot be displayed
     * 
     * @return The name of the stacked page upon which this canvas should be
     *         displayed
     */
    public String getContentCanvasStackedPageName();
    
    /**
     * The popup canvas can have a minimum of one button and a maximum of three
     * buttons. Button one is always the default and will always be displayed.
     * <p>
     * Button two and button three will only be displayed if a button text has
     * been assigned
     * 
     * @return Returns the text for button one
     */
    public String getButtonOneText();
    
    /**
     * The popup canvas can have a minimum of one button and a maximum of three
     * buttons. Button one is always the default and will always be displayed.
     * <p>
     * Button two and button three will only be displayed if a button text has
     * been assigned
     * 
     * @return Returns the text for button two
     */
    public String getButtonTwoText();
    
    /**
     * The popup canvas can have a minimum of one button and a maximum of three
     * buttons. Button one is always the default and will always be displayed.
     * <p>
     * Button two and button three will only be displayed if a button text has
     * been assigned
     * 
     * @return Returns the text for button three
     */
    public String getButtonThreeText();
    
    /**
     * @return Returns the tab page container
     */
    public EJTabPagePropertiesContainer getTabPageContainer();
    
    /**
     * If this canvas is a Tab then the canvas will have various tab pages, this
     * method will return a specific tab page properties for the name specified
     * <p>
     * 
     * @param name
     *            The name of the required tab
     * @return The <code>TabPageProperties</code> of the required tab page or
     *         null if there is no tab page with the given name
     */
    public EJTabPageProperties getTabPageProperties(String name);
    
    /**
     * Returns the stacked page container used to store this canvases stacked
     * pages
     * 
     * @return The <code>StackedPageContainer</code>
     */
    public EJStackedPagePropertiesContainer getStackedPageContainer();
    
    /**
     * If this canvas is a Stacked canvas then the canvas will have various
     * stacked pages, this method will return a specific stacked page properties
     * for the name specified
     * <p>
     * 
     * @param name
     *            The name of the required stacked page
     * @return The <code>{@link EJCoreStackedPageProperties}</code> of the
     *         required stacked page or null if there is no stacked page with
     *         the given name
     */
    public EJStackedPageProperties getStackedPageProperties(String name);
    
    /**
     * Returns the canvas container that stores the canvases belonging to the
     * popup canvas
     * 
     * @return The canvas container
     */
    public EJCanvasPropertiesContainer getPopupCanvasContainer();
    
    /**
     * If this canvas is a Popup canvas then it will contain one or more
     * canvases. this method will return a specific stacked page properties for
     * the name specified
     * <p>
     * 
     * @param name
     *            The name of the required popup canvas
     * @return The <code>{@link EJCoreCanvasProperties}</code> of the required
     *         canvas or null if there is no canvas with the given name
     */
    public EJCanvasProperties getPopupCanvasProperties(String name);
    
    /**
     * Returns the canvas container that stores the canvases belonging to the
     * canvas group
     * 
     * @return The canvas group container
     */
    public EJCanvasPropertiesContainer getGroupCanvasContainer();
    
    /**
     * If this canvas is a Group Canvas then it will contain one or more
     * canvases. this method will return a specific canvas properties for the
     * canvas name specified
     * <p>
     * 
     * @param canvasName
     *            The canvas name
     * @return The <code>{@link EJCoreCanvasProperties}</code> of the required
     *         canvas or <code>null</code> if there is no canvas with the given
     *         name
     */
    public EJCanvasProperties getGroupCanvasProperties(String canvasName);
    
    /**
     * Returns the canvas container that stores the canvases belonging to the
     * canvas group
     * 
     * @return The canvas split container
     */
    public EJCanvasPropertiesContainer getSplitCanvasContainer();
    
    /**
     * If this canvas is a Split Canvas then it will contain one or more
     * canvases. this method will return a specific canvas properties for the
     * canvas name specified
     * <p>
     * 
     * @param canvasName
     *            The canvas name
     * @return The <code>{@link EJCoreCanvasProperties}</code> of the required
     *         canvas or <code>null</code> if there is no canvas with the given
     *         name
     */
    public EJCanvasProperties getSplitCanvasProperties(String canvasName);
    
    /**
     * @return Returns the display Frame flag of this canvas
     */
    public Boolean getDisplayGroupFrame();
    
    
    
    public Boolean getCloseableMessagePane();
    
    
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
     * All canvases will be displaed within a grid. The vertical span indicates
     * how many rows in the grid this canvas will span
     * 
     * @return Returns the verticalSpan for this canvas
     * @see EJCoreCanvasProperties#getHorizontalSpan()
     */
    public int getVerticalSpan();
    
    /**
     * @return Returns the type of this canvas.
     */
    public EJCanvasType getType();
    
    /**
     * Returns the position of the tab
     * 
     * @return The tab position
     * @see EJCoreCanvasProperties#setTabPosition(EJCanvasTabPosition)
     */
    public EJCanvasTabPosition getTabPosition();
    
    /**
     * Returns the orientation of the Split
     * 
     * @return The split orientation
     * @see EJCoreCanvasProperties#setTabPosition(EJCanvasTabPosition)
     */
    public EJCanvasSplitOrientation getSplitOrientation();
    
    /**
     * Returns the style of the line
     * 
     * @return The line style
     * 
     */
    public EJCanvasLineStyle getLineStyle();
    
    /**
     * Returns the name of the initially displayed stacked page
     * 
     * @return The initially stacked page name
     */
    public String getInitialStackedPageName();
    
    /**
     * @return Returns the width of this canvas
     */
    public int getWidth();
    
    /**
     * The title that will be displayed on a popup canvas's window
     * 
     * @return Returns the title of the window if this canvas is a popup canvas
     */
    public String getPopupPageTitle();
    
    /**
     * The title that will be displayed on the canvas's frame if a frame is
     * displayed
     * 
     * @return Returns the title of this canvas
     */
    public String getGroupFrameTitle();
    
    /**
     * @return Returns the name of this canvas
     */
    public String getName();
    
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
    
    
    public String getReferredFormId();
    
    
    public EJCanvasMessagePosition getMessagePosition();

    public int getMessagePaneSize();
    
    
    public EJPopupButton getDefaultPopupButton();
    
}
