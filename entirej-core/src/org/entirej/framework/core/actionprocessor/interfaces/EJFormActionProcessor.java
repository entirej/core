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
package org.entirej.framework.core.actionprocessor.interfaces;

import org.entirej.framework.core.EJActionProcessorException;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJParameterList;
import org.entirej.framework.core.data.controllers.EJQuestion;
import org.entirej.framework.core.enumerations.EJPopupButton;

public interface EJFormActionProcessor extends EJBlockActionProcessor
{
    
    /**
     * If the <code>Form</code> is asked to ask a question then the answer of
     * the question will be returned within this method
     * <p>
     * This method is needed because Alerts are non blocking, ie. after the
     * alert is opened the code beneath the show method, is continued.
     * <p>
     * 
     * @param question
     *            The <code>Question</code> holds all information that is
     *            required to ask the question and to return the answer to this
     *            action processor
     */
    public void questionAnswered(EJQuestion question) throws EJActionProcessorException;
    
    /**
     * This method is called when the user tries to delete a master record in a
     * master-detail relationship when the detail block contains data
     * <p>
     * EntireJ will display a standard message if this method returns no message
     * 
     * @param form
     *            The form where the violation occurred
     * @param relationName
     *            The name of the relation that was violated
     * @return The message to display or <code>null</code> if the standard
     *         message should be displayed
     */
    public EJMessage getMasterDetailDeleteViolationMessage(EJForm form, String relationName) throws EJActionProcessorException;
    
    /**
     * Called before the form is opened
     * <p>
     * Throwing an exception from within this method will stop the form from
     * opening
     * 
     * @param form
     *            The form that will be opened
     */
    public void preFormOpened(EJForm form) throws EJActionProcessorException;
    
    /**
     * Called before the form is closed
     * <p>
     * Throwing an exception from within this method will stop the form from
     * closing
     * 
     * @param form
     *            The form that will be closed
     */
    public void preFormClosed(EJForm form) throws EJActionProcessorException;
    
    /**
     * Called when a user selects a new form to be opened. This will be called
     * before displaying the form to the user
     * <p>
     * 
     * @param form
     *            The form that is opening
     */
    public void newFormInstance(EJForm form) throws EJActionProcessorException;
    
    /**
     * Called whenever the user navigates to a different block within the form
     * 
     * @param form
     *            The form from which this method is called
     * @param blockName
     *            The name of the block that gained focus
     */
    public void newBlockInstance(EJForm form, String blockName) throws EJActionProcessorException;
    
    /**
     * Called before the given tab page is shown
     * 
     * @param form
     *            The form from which this method is called
     * @param tabCanvasName
     *            The name of the tab canvas
     * @param tabPageName
     *            The tab page
     */
    public void preShowTabPage(EJForm form, String tabCanvasName, String tabPageName) throws EJActionProcessorException;
    
    /**
     * Called whenever a user chooses a new tab page on a tab canvas
     * 
     * @param form
     *            The form from which this method is called
     * @param tabCanvasName
     *            The name of the tab canvas
     * @param tabPageName
     *            The name of the tab within the tab canvas
     */
    public void tabPageChanged(EJForm form, String tabCanvasName, String tabPageName) throws EJActionProcessorException;
    
    /**
     * Called before a given stacked page will be shown
     * 
     * @param form
     *            The form from which this method is called
     * @param stackedCanvasName
     *            The name of the stacked canvas
     * @param stackedPageName
     *            The name of the stacked page that will be shown
     */
    public void preShowStackedPage(EJForm form, String stackedCanvasName, String stackedPageName) throws EJActionProcessorException;
    
    /**
     * Called whenever the user chooses a new stacked page from a stacked canvas
     * 
     * @param form
     *            The form from which this method is called
     * @param stackedCanvasName
     *            The name of the stacked canvas
     * @param stackedPageName
     *            The name of the stacked page opened
     */
    public void stackedPageChanged(EJForm form, String stackedCanvasName, String stackedPageName) throws EJActionProcessorException;
    
    /**
     * Called before a popup canvas with the given name is opened
     * 
     * @param form
     *            The form from which this method is called
     * @param popupCanvasName
     *            The name of the popup canvas which is being opened
     */
    public void preOpenPopupCanvas(EJForm form, String popupCanvasName) throws EJActionProcessorException;
    
    /**
     * Called before a popup canvas is closed
     * This enables the posibility to validate the popup and stop it from closing. To stop the closing of the popup throw an empty application exception:<p>
     * <code>throw new EJApplicationException();</code>
     * 
     * @param form
     *            The form from which this method is called
     * @param popupCanvasName
     *            The name of the popup canvas which is being closed
     * @param button
     *            The button that was pressed to close the popup canvas
     */
    public void popupCanvasClosing(EJForm form, String popupCanvasName, EJPopupButton button) throws EJActionProcessorException;
    
    /**
     * Called when a popup canvas is closed
     * 
     * @param form
     *            The form from which this method is called
     * @param popupCanvasName
     *            The name of the popup canvas which is being closed
     * @param button
     *            The button that was pressed to close the popup canvas
     */
    public void popupCanvasClosed(EJForm form, String popupCanvasName, EJPopupButton button) throws EJActionProcessorException;
    
    /**
     * Called when a popup canvas is opened
     * 
     * @param form
     *            The form from which this method is called
     * @param popupCanvasName
     *            The name of the popup canvas which was opened
     */
    public void popupCanvasOpened(EJForm form, String popupCanvasName) throws EJActionProcessorException;
    
    /**
     * Called when a popup form opened by the forms FormManager is closed
     * 
     * @param parameterList
     *            The parameter list from the popup form
     */
    public void popupFormClosed(EJParameterList parameterList) throws EJActionProcessorException;
    
    /**
     * This is called whenever the user will navigate to this form
     * 
     * @param form
     *            The form from which this method is called
     */
    public void focusGained(EJForm form) throws EJActionProcessorException;
    
    /**
     * This is called whenever a modal form is closing
     * 
     * @param form
     *            The form from which this method is called
     * @param parameterList
     *            The parameter list that was passed to the modal form when
     *            opening
     */
    public void modalFormClosing(EJForm form, EJParameterList parameterList) throws EJActionProcessorException;
}
