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
package org.entirej.framework.core.renderers.interfaces;

import java.util.Collection;

import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.data.controllers.EJEmbeddedFormController;
import org.entirej.framework.core.internal.EJInternalForm;

public interface EJFormRenderer extends EJRenderer
{

    /**
     * This indicates to the form renderer that one of its properties has been
     * modified
     * 
     * @param propertyName
     *            The name of the modified property
     */
    public void refreshFormRendererProperty(String propertyName);

    /**
     * Used to initialize the renderer with the given form controller
     * 
     * @param form
     *            This renderer will be the visual representation for this form
     */
    public void initialiseForm(EJInternalForm form);

    /**
     * Returns the controller represented by this renderer
     * 
     * @return This renderers form
     */
    public EJInternalForm getForm();

    /**
     * Informs the form renderer that a save operation has been performed
     */
    public void savePerformed();

    /**
     * Informs the form renderer that the form has been cleared of any data
     */
    public void formCleared();

    /**
     * Informs the form renderer that the form is being closed
     */
    public void formClosed();

    /**
     * Instructs the form renderer to show a stacked page
     * <p>
     * 
     * @param stackedCanvasName
     *            The name of the stacked canvas containing the required page
     * @param stackedPageName
     *            The name of the page to be shown
     */
    public void showStackedPage(String stackedCanvasName, String stackedPageName);

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
    public String getDisplayedStackedPage(String stackedCanvasName);

    /**
     * Instructs the form renderer to show a tab page
     * <p>
     * 
     * @param tabCanvasName
     *            The name of the tab canvas containing the required page
     * @param tabPageName
     *            The name of the page to be shown
     */
    public void showTabPage(String tabCanvasName, String tabPageName);

    /**
     * Used to set a specific tab canvas page to be visible
     * 
     * @param tabCanvasName
     *            The tab canvas
     * @param tabPageName
     *            The page to be shown
     * @param visbile
     *            If set to <code>true</code> then the tab page will be made
     *            visible otherwise it will be hidden
     */
    public void setTabPageVisible(String tabCanvasName, String tabPageName, boolean visible);

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
    public String getDisplayedTabPage(String tabCanvasName);

    /**
     * Instructs the form renderer to show a popup canvas
     * <p>
     * 
     * @param popupCanvasName
     *            The name of the popup canvas to be shown
     */
    public void showPopupCanvas(String popupCanvasName);

    /**
     * Instructs the form renderer to open an embedded form
     * 
     * @param controller
     *            The embedded form controller that should be opened
     */
    public void openEmbeddedForm(EJEmbeddedFormController controller);

    /**
     * Used to close an embedded form
     * 
     * @param controller
     *            The controller of the embedded form
     */
    public void closeEmbeddedForm(EJEmbeddedFormController controller);

    /**
     * Instructs the form renderer to close a popup canvas
     * <p>
     * 
     * @param popupCanvasName
     *            The name of the popup canvas to be closed
     */
    public void closePopupCanvas(String popupCanvasName);

    /**
     * Forces the form to gain initial focus
     * <p>
     * The initial focus of the form should be the first navigable item on the
     * first available canvas. If the first available canvas is a tab page or a
     * stacked page, then the focus will be sent to the item defined by the
     * First navigational block and the First navigational item properties
     * 
     */
    public void gainInitialFocus();

    /**
     * Sets the canvas messages to a specific canvas
     * <p>
     * Canvas Messages are displayed either to the right, left, top or bottom of
     * a canvas and displays a list of messages for the user. This is especially
     * helpful on popup canvases which are being used as insert/update screens
     * 
     * @param canvasName
     *            The name of the canvas that will be displaying the messages
     * @param messages
     *            The messages to be displayed
     * 
     * @see #clearCanvasMessages(String)
     */
    public void clearCanvasMessages(String canvasName);

    /**
     * Clears the messages that have been set on the given canvas
     * 
     * @param canvasName
     *            The name of the canvas that should be cleared of messages
     * 
     * @see #setCanvasMessages(String, Collection)
     */
    public void setCanvasMessages(String canvasName, Collection<EJMessage> messages);
}
