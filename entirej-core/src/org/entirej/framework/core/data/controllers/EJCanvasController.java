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
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;
import java.util.Collection;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.enumerations.EJPopupButton;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.interfaces.EJCanvasProperties;
import org.entirej.framework.core.properties.interfaces.EJStackedPageProperties;
import org.entirej.framework.core.properties.interfaces.EJTabPageProperties;

public class EJCanvasController implements Serializable
{
    private EJFormController _formController;
    
    EJCanvasController(EJFormController formController)
    {
        _formController = formController;
    }
    
    /**
     * Used to set a specific tab canvas page to be visible
     * 
     * @param tabCanvasName
     *            The tab canvas
     * @param tabPageName
     *            The page to be shown
     * @param visbile
     *            If set to <code>true</code> then the tab page will be made visible otherwise it will be hidden
     */
    public void setTabPageVisible(String tabCanvasName, String tabPageName, boolean visible)
    {
        _formController.getRenderer().setTabPageVisible(tabCanvasName, tabPageName, visible);
    }
    
    /**
     * Used to show a specific tab canvas page
     * 
     * @param tabCanvasName
     *            The tab canvas
     * @param tabPageName
     *            The page to be shown
     */
    public void showTabPage(String tabCanvasName, String tabPageName)
    {
        // Inform the action processor that a new tab page will be displayed
        _formController.getManagedActionController().preShowTabPage(_formController.getEJForm(), tabCanvasName, tabPageName);
        // Display that tab page
        _formController.getRenderer().showTabPage(tabCanvasName, tabPageName);
        // Inform the action processor that a tab page has been changed
        _formController.getManagedActionController().tabPageChanged(_formController.getEJForm(), tabCanvasName, tabPageName);
        
        EJCanvasProperties canvasProperties = _formController.getProperties().getCanvasProperties(tabCanvasName);
        if (canvasProperties != null)
        {
            EJTabPageProperties tabPage = canvasProperties.getTabPageProperties(tabPageName);
            if (tabPage != null)
            {
                String block = tabPage.getFirstNavigationalBlock();
                String item = tabPage.getFirstNavigationalItem();
                
                navigateToItem(block, item);
            }
        }
    }
    
    /**
     * Returns the current tab page name of the given tab canvas
     * <p>
     * If the name given is not a tab page or no page is displayed,
     * <code>null</code> will be returned
     * 
     * @param tabCanvasName
     * @return The name of the currently displayed tab page, or if the name
     *         given is not a tab page or no page is displayed,
     *         <code>null</code> will be returned
     */
    public String getDisplayedTabPage(String tabCanvasName)
    {
        return _formController.getRenderer().getDisplayedTabPage(tabCanvasName);
    }
    
    /**
     * Used to show a specific stacked canvas page
     * 
     * @param stackedCanvasName
     *            The stacked canvas
     * @param stackedPageName
     *            The page to be shown
     */
    public void showStackedPage(String stackedCanvasName, String stackedPageName)
    {
        // Inform the action processor that a new stack page will be
        // displayed
        _formController.getManagedActionController().preShowStackedPage(_formController.getEJForm(), stackedCanvasName, stackedPageName);
        // Display that stacked page
        _formController.getRenderer().showStackedPage(stackedCanvasName, stackedPageName);
        // Inform the action processor that a stacked page has been changed
        _formController.getManagedActionController().stackedPageChanged(_formController.getEJForm(), stackedCanvasName, stackedPageName);
        
        EJCanvasProperties canvasProperties = _formController.getProperties().getCanvasProperties(stackedCanvasName);
        if (canvasProperties != null)
        {
            EJStackedPageProperties stackedPage = canvasProperties.getStackedPageProperties(stackedPageName);
            if (stackedPage != null)
            {
                String block = stackedPage.getFirstNavigationalBlock();
                String item = stackedPage.getFirstNavigationalItem();
                
                navigateToItem(block, item);
            }
        }
    }
    
    /**
     * Returns the current stacked page name of the given stacked canvas
     * <p>
     * If the name given is not a stacked page or no page is displayed,
     * <code>null</code> will be returned
     * 
     * @param stackedCanvasName
     * @return The name of the currently displayed stacked page, or if the name
     *         given is not a stacked page or no page is displayed,
     *         <code>null</code> will be returned
     */
    public String getDisplayedStackedPage(String stackedCanvasName)
    {
        return _formController.getRenderer().getDisplayedStackedPage(stackedCanvasName);
    }
    
    /**
     * Used to show a specific stacked canvas page
     * 
     * @param popupCanvasName
     *            The popup canvas to be displayed
     */
    public void showPopupCanvas(String popupCanvasName)
    {
        // Inform the action processor that a popup is going to be opened
        _formController.getManagedActionController().preOpenPopupCanvas(_formController.getEJForm(), popupCanvasName);
        // Open the popup
        _formController.getRenderer().showPopupCanvas(popupCanvasName);
        // Inform the action processor that the popup has been opened
        _formController.getManagedActionController().popupCanvasOpened(_formController.getEJForm(), popupCanvasName);
    }
    
    public void tabPageChanged(String tabCanvasName, String tabPageName)
    {
        // If the tab page has a first navigational block and item, then ensure
        // the focus is correct
        EJCanvasProperties canvasProperties = _formController.getProperties().getCanvasProperties(tabCanvasName);
        if (canvasProperties != null)
        {
            EJTabPageProperties tabPage = canvasProperties.getTabPageProperties(tabPageName);
            if (tabPage != null)
            {
                String block = tabPage.getFirstNavigationalBlock();
                String item = tabPage.getFirstNavigationalItem();
                
                navigateToItem(block, item);
            }
        }
        
        _formController.getManagedActionController().tabPageChanged(_formController.getEJForm(), tabCanvasName, tabPageName);
        
    }
    
    private void navigateToItem(String block, String itemName)
    {
        if (block != null && block.trim().length() > 0)
        {
            EJEditableBlockController blockController = _formController.getBlockController(block);
            if (blockController != null)
            {
                if (itemName != null && itemName.trim().length() > 0)
                {
                    EJScreenItemController item = blockController.getScreenItem(EJScreenType.MAIN, itemName);
                    if (blockController.getManagedRendererController() != null)
                    {
                        blockController.getManagedRendererController().setFocusToItem(item);
                    }
                }
                else
                {
                    if (blockController.getManagedRendererController() != null)
                    {
                        blockController.getManagedRendererController().gainFocus();
                    }
                }
            }
        }
    }
    
    
    
    public void closePopupCanvas(String popupCanvasName, EJPopupButton button)
    {
        try
        {
            // us the unmanaged action controller so that the developer has the possibiltiy to stop the closing operation
            _formController.getUnmanagedActionController().popupCanvasClosing(_formController.getEJForm(), popupCanvasName, button);
        
            _formController.getRenderer().closePopupCanvas(popupCanvasName);
            
            // Now let the developer know that the canvas is being closed
            _formController.getManagedActionController().popupCanvasClosed(_formController.getEJForm(), popupCanvasName, button);
        }
        catch (EJApplicationException e)
        {
            _formController.getFrameworkManager().handleException(e);
        }
    }

    public void setCanvasMessages(String canvasName, Collection<EJMessage> messages)
    {
        _formController.getRenderer().setCanvasMessages(canvasName,messages);
        
    }

    public void clearCanvasMessages(String canvasName)
    {
        _formController.getRenderer().clearCanvasMessages(canvasName);
        
    }
}
