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

import org.entirej.framework.core.enumerations.EJCanvasMessagePosition;
import org.entirej.framework.core.enumerations.EJCanvasSplitOrientation;
import org.entirej.framework.core.enumerations.EJCanvasTabPosition;
import org.entirej.framework.core.enumerations.EJCanvasType;
import org.entirej.framework.core.properties.containers.EJCoreCanvasPropertiesContainer;
import org.entirej.framework.core.properties.containers.EJCoreStackedPagePropertiesContainer;
import org.entirej.framework.core.properties.containers.EJCoreTabPagePropertiesContainer;
import org.entirej.framework.core.properties.interfaces.EJCanvasProperties;
import org.entirej.framework.core.properties.interfaces.EJStackedPageProperties;
import org.entirej.framework.core.properties.interfaces.EJTabPageProperties;

public class EJCoreCanvasProperties implements EJCanvasProperties
{
    // If this is a BlockCanvas then this variable will hold the properties of
    // the block which will be displayed upon it
    private EJCoreBlockProperties                _blockProperties;

    private EJCanvasType                         _type                         = EJCanvasType.BLOCK;
    // Can be either a Tab Canvas or a CONTENT type canvas
    private String                               _contentCanvasName            = "";
    // The name of the tab page if the content canvas is a tab
    private String                               _contentCanvasTabPageName     = "";
    // The name of the stacked page if the content canvas is a stacked canvas
    private String                               _contentCanvasStackedPageName = "";
    // The x position on the main form or the content canvas if one has been
    // defined
    private int                                  _width                        = 0;
    private int                                  _height                       = 0;
    private int                                  _numCols                      = 1;
    private int                                  _verticalSpan                 = 1;
    private int                                  _horizontalSpan               = 1;
    private int                                  _messagePaneSize              = 200;
    private boolean                              _expandHorizontally           = true;
    private boolean                              _expandVertically             = true;
    private boolean                              _displayGroupFrame            = false;
    private boolean                              _closeableMessagePane         = true;
    private String                               _name                         = "";
    private String                               _popupPageTitle               = "";
    private String                               _basePopupPageTitle           = "";
    private String                               _groupFrameTitle              = "";
    private String                               _baseGroupFrameTitle          = "";
    private String                               _buttonOneText                = "";
    private String                               _baseButtonOneText            = "";
    private String                               _buttonTwoText                = "";
    private String                               _baseButtonTwoText            = "";
    private String                               _buttonThreeText              = "";
    private String                               _baseButtonThreeText          = "";
    private String                               _initiallyStackedPageName     = "";

    private EJCanvasMessagePosition              _messagePosition              = EJCanvasMessagePosition.RIGHT;

    private EJCoreTabPagePropertiesContainer     _tabPages;
    private EJCoreStackedPagePropertiesContainer _stackedPages;
    private EJCoreCanvasPropertiesContainer      _popupCanvasContainer;
    private EJCoreCanvasPropertiesContainer      _groupCanvasContainer;
    private EJCoreCanvasPropertiesContainer      _splitCanvasContainer;
    private EJCoreCanvasPropertiesContainer      _parentCanvasContainer;

    private String                               _referredFormId;

    //
    // If the Canvas type is TAB, then the following properties are also
    // available
    private EJCanvasTabPosition                  _tabPosition                  = EJCanvasTabPosition.TOP;
    private EJCanvasSplitOrientation             _splitOrientation             = EJCanvasSplitOrientation.HORIZONTAL;

    public EJCoreCanvasProperties(String name)
    {
        _name = name;
        _tabPages = new EJCoreTabPagePropertiesContainer();
        _stackedPages = new EJCoreStackedPagePropertiesContainer();
        _popupCanvasContainer = new EJCoreCanvasPropertiesContainer();
        _groupCanvasContainer = new EJCoreCanvasPropertiesContainer();
        _splitCanvasContainer = new EJCoreCanvasPropertiesContainer();
    }

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
    public EJCoreBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }

    /**
     * Sets the block properties if this canvas is a {@link EJCanvasType#BLOCK}
     * 
     * @param blockProperties
     *            The properties of the block displayed on this block canvas
     */
    public void setBlockProperties(EJCoreBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
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
     * Returns the name of this canvases content canvas i.e. the name of the
     * canvas upon which this canvas will be displayed, or null if no content
     * canvas has been defined
     * <p>
     * If there has been no content canvas defined then the canvas display
     * properties will correspond to actual display positions on the for,
     * 
     * @return the contentCanvasName
     */
    public String getContentCanvasName()
    {
        return _contentCanvasName;
    }

    /**
     * Sets the name of the content canvas upon which this canvas will be
     * displayed
     * <p>
     * If there has been no content canvas defined then the canvas display
     * properties will correspond to actual display positions on the for,
     * 
     * @param contentCanvasName
     *            The name of the content canvas to display this canvas on
     * @see #getContentCanvasName()
     */
    public void setContentCanvasName(String contentCanvasName)
    {
        _contentCanvasName = contentCanvasName;
    }

    /**
     * If the content canvas is a tab canvas then a tab page must be chosen
     * <p>
     * If no tab page is chosen then this canvas cannot be displayed
     * 
     * @return The name of the tab page upon which this canvas should be
     *         displayed
     */
    public String getContentCanvasTabPageName()
    {
        return _contentCanvasTabPageName;
    }

    /**
     * If the content canvas is a tab canvas then a tab page must be chosen
     * <p>
     * If no tab page is chosen then this canvas cannot be displayed
     * 
     * @param contentTabPageName
     *            The name of the tab page upon which this canvas should be
     *            displayed
     */
    public void setContentCanvasTabPageName(String contentTabPageName)
    {
        _contentCanvasTabPageName = contentTabPageName;
    }

    /**
     * If the content canvas is a stacked canvas then a stacked page must be
     * chosen
     * <p>
     * If no stacked page is chosen then this canvas cannot be displayed
     * 
     * @return The name of the stacked page upon which this canvas should be
     *         displayed
     */
    public String getContentCanvasStackedPageName()
    {
        return _contentCanvasStackedPageName;
    }

    /**
     * If the content canvas is a stacked canvas then a stacked page must be
     * chosen
     * <p>
     * If no stacked page is chosen then this canvas cannot be displayed
     * 
     * @param contentStackedPageName
     *            The name of the stacked page upon which this canvas should be
     *            displayed
     */
    public void setContentCanvasStackedPageName(String contentStackedPageName)
    {
        _contentCanvasStackedPageName = contentStackedPageName;
    }

    /**
     * Sets the name of the initially displayed stacked page
     * 
     * @param pageName
     *            The name of the initially displayed stacked page
     */
    public void setInitalStackedPageName(String pageName)
    {
        _initiallyStackedPageName = pageName;
    }

    /**
     * Sets the name of the initially displayed page of a stacked canvas
     * 
     * @return The name of the initially displayed stacked page
     */
    public String getInitialStackedPageName()
    {
        return _initiallyStackedPageName;
    }

    /**
     * The popup canvas can have a minimum of one button and a maximum of three
     * buttons. Button one is always the default and will always be displayed.
     * <p>
     * Button two and button three will only be displayed if a button text has
     * been assigned
     * 
     * @return Returns the text for button one
     */
    public String getButtonOneText()
    {
        return _buttonOneText;
    }

    /**
     * The popup canvas can have a minimum of one button and a maximum of three
     * buttons. Button one is always the default and will always be displayed.
     * <p>
     * Button two and button three will only be displayed if a button text has
     * been assigned
     * 
     * @param buttonText
     *            The text to display on button one. This text will be
     *            translated by the applications translator
     */
    public void setBaseButtonOneText(String buttonText)
    {
        if (buttonText != null && buttonText.trim().length() == 0)
        {
            _baseButtonOneText = null;
        }
        else
        {
            _baseButtonOneText = buttonText;
        }

        _buttonOneText = _baseButtonOneText;
    }

    /**
     * Returns the untranslated button one text
     * <p>
     * The value will be translated by EntireJ using the applications translator
     * 
     * @return The base button one text
     */
    public String getBaseButtonOneText()
    {
        return _baseButtonOneText;
    }

    /**
     * Used to set the translated button one text
     * <p>
     * EntireJ will retrieve the base button one text and use the assigned
     * framework translator to translate the text. Once translated, EntireJ will
     * assign the translated text via this method
     * 
     * @param translatedButtonOneText
     *            The translated button one text
     */
    public void setTranslatedButtonOneText(String translatedButtonOneText)
    {
        _buttonOneText = translatedButtonOneText;
    }

    /**
     * The popup canvas can have a minimum of one button and a maximum of three
     * buttons. Button one is always the default and will always be displayed.
     * <p>
     * Button two and button three will only be displayed if a button text has
     * been assigned
     * 
     * @return Returns the text for button two
     */
    public String getButtonTwoText()
    {
        return _buttonTwoText;
    }

    /**
     * The popup canvas can have a minimum of one button and a maximum of three
     * buttons. Button one is always the default and will always be displayed.
     * <p>
     * Button two and button three will only be displayed if a button text has
     * been assigned
     * 
     * @param buttonText
     *            The text to display on button two. This text will be
     *            translated by the applications translator
     */
    public void setBaseButtonTwoText(String buttonText)
    {
        if (buttonText != null && buttonText.trim().length() == 0)
        {
            _baseButtonTwoText = null;
        }
        else
        {
            _baseButtonTwoText = buttonText;
        }

        _buttonTwoText = _baseButtonTwoText;
    }

    /**
     * Returns the untranslated button two text
     * <p>
     * The value will be translated by EntireJ using the applications translator
     * 
     * @return The base button two text
     */
    public String getBaseButtonTwoText()
    {
        return _baseButtonTwoText;
    }

    /**
     * Used to set the translated button two text
     * <p>
     * EntireJ will retrieve the base button two text and use the assigned
     * framework translator to translate the text. Once translated, EntireJ will
     * assign the translated text via this method
     * 
     * @param translatedButtonTwoText
     *            The translated button two text
     */
    public void setTranslatedButtonTwoText(String translatedButtonTwoText)
    {
        _buttonTwoText = translatedButtonTwoText;
    }

    /**
     * The popup canvas can have a minimum of one button and a maximum of three
     * buttons. Button one is always the default and will always be displayed.
     * <p>
     * Button two and button three will only be displayed if a button text has
     * been assigned
     * 
     * @return Returns the text for button three
     */
    public String getButtonThreeText()
    {
        return _buttonThreeText;
    }

    /**
     * The popup canvas can have a minimum of one button and a maximum of three
     * buttons. Button one is always the default and will always be displayed.
     * <p>
     * Button two and button three will only be displayed if a button text has
     * been assigned
     * 
     * @param buttonText
     *            The text to display on button two. This text will be
     *            translated by the applications translator
     */
    public void setBaseButtonThreeText(String buttonText)
    {
        if (buttonText != null && buttonText.trim().length() == 0)
        {
            _baseButtonThreeText = null;
        }
        else
        {
            _baseButtonThreeText = buttonText;
        }
        _buttonThreeText = _baseButtonThreeText;
    }

    /**
     * Returns the untranslated button three text
     * <p>
     * The value will be translated by EntireJ using the applications translator
     * 
     * @return The base button three text
     */
    public String getBaseButtonThreeText()
    {
        return _baseButtonThreeText;
    }

    /**
     * Used to set the translated button three text
     * <p>
     * EntireJ will retrieve the base button three text and use the assigned
     * framework translator to translate the text. Once translated, EntireJ will
     * assign the translated text via this method
     * 
     * @param translatedButtonThreeText
     *            The translated button three text
     */
    public void setTranslatedButtonThreeText(String translatedButtonThreeText)
    {
        _buttonThreeText = translatedButtonThreeText;
    }

    /**
     * @return Returns the tab page container
     */
    public EJCoreTabPagePropertiesContainer getTabPageContainer()
    {
        return _tabPages;
    }

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
    public EJTabPageProperties getTabPageProperties(String name)
    {
        if (name == null)
        {
            return null;
        }

        return _tabPages.getTabPageProperties(name);
    }

    /**
     * Ads a tab page to this canvas
     * 
     * @param tab
     *            The tab to add
     * @throws NullPointerException
     *             if the tab passed is null
     */
    public void addTabPageProperties(EJCoreTabPageProperties tab)
    {
        if (tab != null)
        {
            _tabPages.addTabPageProperties(tab);
        }
    }

    /**
     * Returns the stacked page container used to store this canvases stacked
     * pages
     * 
     * @return The <code>StackedPageContainer</code>
     */
    public EJCoreStackedPagePropertiesContainer getStackedPageContainer()
    {
        return _stackedPages;
    }

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
    public EJStackedPageProperties getStackedPageProperties(String name)
    {
        if (name == null)
        {
            return null;
        }

        return _stackedPages.getStackedPageProperties(name);
    }

    /**
     * Ads a stacked page to this canvas
     * 
     * @param stackedPage
     *            The stacked page to add
     */
    public void addStackedPageProperties(EJCoreStackedPageProperties stackedPage)
    {
        if (stackedPage != null)
        {
            _stackedPages.addStackedPageProperties(stackedPage);
        }
    }

    /**
     * Returns the canvas container that stores the canvases belonging to the
     * popup canvas
     * 
     * @return The canvas container
     */
    public EJCoreCanvasPropertiesContainer getPopupCanvasContainer()
    {
        return _popupCanvasContainer;
    }

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
    public EJCanvasProperties getPopupCanvasProperties(String name)
    {
        if (name == null)
        {
            return null;
        }

        return _popupCanvasContainer.getCanvasProperties(name);
    }

    /**
     * Adds a canvas to this popup
     * 
     * @param stackedPage
     *            The stacked page to add
     */
    public void addPopupCanvasProperties(EJCoreCanvasProperties canvas)
    {
        if (canvas != null)
        {
            _popupCanvasContainer.addCanvasProperties(canvas);
        }
    }

    /**
     * Returns the canvas container that stores the canvases belonging to the
     * canvas group
     * 
     * @return The canvas group container
     */
    public EJCoreCanvasPropertiesContainer getGroupCanvasContainer()
    {
        return _groupCanvasContainer;
    }

    /**
     * Returns the canvas container that stores the canvases belonging to the
     * canvas split
     * 
     * @return The canvas split container
     */
    public EJCoreCanvasPropertiesContainer getSplitCanvasContainer()
    {
        return _splitCanvasContainer;
    }

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
    public EJCanvasProperties getGroupCanvasProperties(String canvasName)
    {
        if (canvasName == null)
        {
            return null;
        }

        return _groupCanvasContainer.getCanvasProperties(canvasName);
    }

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
    public EJCanvasProperties getSplitCanvasProperties(String canvasName)
    {
        if (canvasName == null)
        {
            return null;
        }

        return _splitCanvasContainer.getCanvasProperties(canvasName);
    }

    /**
     * Adds a canvas to this canvas group
     * 
     * @param canvasProperties
     *            The canvas to add
     */
    public void addCanvasPropertiesToCanvasGroup(EJCoreCanvasProperties canvasProperties)
    {
        if (canvasProperties != null)
        {
            _groupCanvasContainer.addCanvasProperties(canvasProperties);
        }
    }

    /**
     * Adds a canvas to this canvas split
     * 
     * @param canvasProperties
     *            The canvas to add
     */
    public void addCanvasPropertiesToCanvasSplit(EJCoreCanvasProperties canvasProperties)
    {
        if (canvasProperties != null)
        {
            _splitCanvasContainer.addCanvasProperties(canvasProperties);
        }
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
     * @return Returns the display Frame flag of this canvas
     */
    public Boolean getDisplayGroupFrame()
    {
        return _displayGroupFrame;
    }

    /**
     * Sets the display frame flag of this canvas
     * 
     * @param name
     *            The display frame flag of this canvas
     */
    public void setDisplayGroupFrame(Boolean display)
    {
        _displayGroupFrame = display;
    }

    public void setCloseableMessagePane(boolean closeableMessagePane)
    {
        this._closeableMessagePane = closeableMessagePane;
    }

    public Boolean getCloseableMessagePane()
    {
        return _closeableMessagePane;
    }

    /**
     * @return Returns the height of this canvas
     */
    public int getHeight()
    {
        return _height;
    }

    /**
     * @param height
     *            The height of this canvas
     */
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
     * All canvases will be displaed within a grid. The vertical span indicates
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
     * @return Returns the type of this canvas.
     */
    public EJCanvasType getType()
    {
        return _type;
    }

    /**
     * Sets the type of this canvas
     * 
     * @param type
     *            The type to set
     */
    public void setType(EJCanvasType type)
    {
        _type = type;
    }

    /**
     * Sets the tab position for this canvas
     * <p>
     * The tab positions are as follows:
     * <ul>
     * <li>TOP - The tabs will placed at the top of the canvas</li>
     * <li>BOTTOM - The tabs will be placed at the bottom of the canvas</li>
     * <li>RIGHT - The tabs will be placed at the right of the canvas</li>
     * <li>LEFT - The tabs will be placed at th left of the canvas</li>
     * </ul>
     * 
     * @param position
     *            the position of the tab pages
     */
    public void setTabPosition(EJCanvasTabPosition position)
    {
        _tabPosition = position;
    }

    /**
     * Returns the position of the tab
     * 
     * @return The tab position
     * @see EJCoreCanvasProperties#setTabPosition(EJCanvasTabPosition)
     */
    public EJCanvasTabPosition getTabPosition()
    {
        return _tabPosition;
    }

    /**
     * Sets the split orientation for this canvas
     * <p>
     * The split orientation are as follows:
     * <ul>
     * </ul>
     * 
     * @param position
     *            the position of the tab pages
     */
    public void setSplitOrientation(EJCanvasSplitOrientation position)
    {
        _splitOrientation = position;
    }

    /**
     * Returns the orientation of the split
     * 
     * @return The split orientation
     * @see EJCoreCanvasProperties#setTabPosition(EJCanvasTabPosition)
     */
    public EJCanvasSplitOrientation getSplitOrientation()
    {
        return _splitOrientation;
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
     * The title that will be displayed on a popup canvas's window
     * 
     * @return Returns the title of the window if this canvas is a popup canvas
     */
    public String getPopupPageTitle()
    {
        return _popupPageTitle;
    }

    /**
     * Sets this cnavas's window title if this canvas is a popup canvas
     * <p>
     * A base title is an untranslated title. All canvas titles will be
     * translated using the applications translator. If there is no translator
     * defined for the application then the base title will be used for the
     * popup canvas's title
     * 
     * @param name
     *            This canvas's popup title
     */
    public void setBasePopupPageTitle(String name)
    {
        if (name != null && name.trim().length() == 0)
        {
            _basePopupPageTitle = null;
        }
        else
        {
            _basePopupPageTitle = name;
        }

        _popupPageTitle = _basePopupPageTitle;
    }

    /**
     * Returns the untranslated popup page title
     * <p>
     * The value will be translated by EntireJ using the applicaitons translator
     * 
     * @return The untranslated popup page title
     */
    public String getBasePopupPageTitle()
    {
        return _basePopupPageTitle;
    }

    /**
     * Used to set the translated popup page title
     * <p>
     * EntireJ will retrieve the base popup page title and use the assigned
     * framework translator to translate the text. Once translated, EntireJ will
     * assign the translated text via this method
     * 
     * @param translatedPopupPageTitle
     *            The translated popup page title
     */
    public void setTranslatedPopupPageTitle(String translatedPopupPageTitle)
    {
        _popupPageTitle = translatedPopupPageTitle;
    }

    /**
     * The title that will be displayed on the canvas's frame if a frame is
     * displayed
     * 
     * @return Returns the title of this canvas
     */
    public String getGroupFrameTitle()
    {
        return _groupFrameTitle;
    }

    /**
     * Sets this canvas's base frame title name
     * <p>
     * A base frame title is an untranslated title. All canvas titles will be
     * translated using the applications translator. If there is no translator
     * defined for the application then the base display name will be used for
     * the canvas's title
     * 
     * @param name
     *            This canvas's base display name
     */
    public void setBaseGroupFrameTitle(String name)
    {
        if (name != null && name.trim().length() == 0)
        {
            _baseGroupFrameTitle = null;
        }
        else
        {
            _baseGroupFrameTitle = name;
        }

        _groupFrameTitle = _baseGroupFrameTitle;
    }

    /**
     * Returns the untranslated group frame title
     * <p>
     * The value will be translated by EntireJ using the applications translator
     * 
     * @return The untranslated group frame title
     */
    public String getBaseGroupFrameTitle()
    {
        return _baseGroupFrameTitle;
    }

    /**
     * Used to set the translated group frame title
     * <p>
     * EntireJ will retrieve the base group frame title and use the assigned
     * framework translator to translate the text. Once translated, EntireJ will
     * assign the translated text via this method
     * 
     * @param translatedGroupFrameTitle
     *            The translated group frame title
     */
    public void setTranslatedGroupFrameTitle(String translatedGroupFrameTitle)
    {
        _groupFrameTitle = translatedGroupFrameTitle;
    }

    /**
     * @return Returns the name of this canvas
     */
    public String getName()
    {
        return _name;
    }

    public void setParentCanvasContainer(EJCoreCanvasPropertiesContainer parentCanvasContainer)
    {
        this._parentCanvasContainer = parentCanvasContainer;
    }

    public EJCoreCanvasPropertiesContainer getParentCanvasContainer()
    {
        return _parentCanvasContainer;
    }

    public void setMessagePosition(EJCanvasMessagePosition messagePosition)
    {
        this._messagePosition = messagePosition;
    }

    public EJCanvasMessagePosition getMessagePosition()
    {
        return _messagePosition;
    }

    public int getMessagePaneSize()
    {
        return _messagePaneSize;
    }

    public void setMessagePaneSize(int messagePaneSize)
    {
        this._messagePaneSize = messagePaneSize;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        result = prime * result + ((_type == null) ? 0 : _type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EJCoreCanvasProperties other = (EJCoreCanvasProperties) obj;
        if (_name == null)
        {
            if (other._name != null)
                return false;
        }
        else if (!_name.equals(other._name))
            return false;
        if (_type != other._type)
            return false;
        return true;
    }

    @Override
    public String getReferredFormId()
    {
        return _referredFormId;
    }

    public void setReferredFormId(String referredFormId)
    {
        this._referredFormId = referredFormId;
    }

}
