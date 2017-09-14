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

import org.entirej.framework.core.properties.containers.EJCoreCanvasPropertiesContainer;
import org.entirej.framework.core.properties.interfaces.EJTabPageProperties;

public class EJCoreTabPageProperties implements EJTabPageProperties
{
    private String                          _name                   = "";
    private int                             _numCols                = 1;
    private String                          _pageTitle              = "";
    private String                          _basePageTitle          = "";
    private String                          _firstNavigationalBlock = "";
    private String                          _firstNavigationalItem  = "";
    private boolean                         _enabled                = true;
    private boolean                         _visible                = true;
    
    private EJCoreCanvasPropertiesContainer _containedCanvases;
    
    public EJCoreTabPageProperties(String name)
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
     * If the tab page is not visible then it will need to be made visible
     * before the user can navigate to it
     * <p>
     * 
     * @return Returns the enabled.
     * @see EJCoreTabPageProperties#isEnabled()
     */
    public boolean isVisible()
    {
        return _visible;
    }
    
    /**
     * If the tab page is not visible then it will need to be made visible
     * before the user can navigate to it
     * <p>
     * 
     * @param visible
     *            The flag to set
     */
    public void setVisible(boolean visible)
    {
        _visible = visible;
    }
    
    /**
     * If the tab page is not enabled, the user can still see it, but not
     * navigate to it
     * <p>
     * 
     * @return Returns the enabled flag
     * @see EJCoreTabPageProperties#isVisible()
     */
    public boolean isEnabled()
    {
        return _enabled;
    }
    
    /**
     * If the tab page is not enabled, the user can still see it, but not
     * navigate to it
     * <p>
     * 
     * @param enabled
     *            The enabled flag
     * @see EJCoreTabPageProperties#setVisible(boolean)
     */
    public void setEnabled(boolean enabled)
    {
        _enabled = enabled;
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
     * passed when the user clicks on this tab page
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
     * passed when the user clicks on this tab page
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
     * @return Returns the name of this tab page
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * The page title is the text that will apear in the tab itself
     * <p>
     * The page title is the translated version of the page title code if it was
     * translated otherwise the page title code will be returned
     * 
     * @return Returns the pageTitle of this tab page
     */
    public String getPageTitle()
    {
        return _pageTitle;
    }
    
    /**
     * Sets this canvas's base page title
     * <p>
     * A base page title is an untranslated title. All tab page titles will be
     * translated using the applications translator. If there is no translator
     * defined for the application then the base page title will be used for the
     * tab page tile
     * 
     * @param pageTitle
     *            This tab page tile
     */
    public void setBasePageTitle(String pageTitle)
    {
        if (pageTitle != null && pageTitle.trim().length() == 0)
        {
            _basePageTitle = null;
        }
        else
        {
            _basePageTitle = pageTitle;
        }
        
        _pageTitle = _basePageTitle;
    }
    
    /**
     * Returns the untranslated page title
     * <p>
     * The value will be translated by EntireJ using the applications translator
     * 
     * @return The untranslated page title
     */
    public String getBasePageTitle()
    {
        return _basePageTitle;
    }
    
    /**
     * Used to set the translated page title
     * <p>
     * EntireJ will retrieve the base page title and use the assigned framework
     * translator to translate the text. Once translated, EntireJ will assign
     * the translated text via this method
     * 
     * @param translatedPageTitle
     *            The translated page title
     */
    public void setTranslatedPageTitle(String translatedPageTitle)
    {
        _pageTitle = translatedPageTitle;
    }
    
}
