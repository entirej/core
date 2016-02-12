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
package org.entirej.framework.core.interfaces;

import java.io.Serializable;
import java.util.Collection;

import org.entirej.framework.core.EJFrameworkHelper;
import org.entirej.framework.core.EJFrameworkInitialiser;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.data.controllers.EJEmbeddedFormController;
import org.entirej.framework.core.data.controllers.EJPopupFormController;
import org.entirej.framework.core.internal.EJInternalForm;

public interface EJApplicationManager extends EJFrameworkHelper, Serializable
{
    /**
     * Sets the framework manger
     * <p>
     * this will be set from the <code>{@link EJFrameworkInitialiser}</code>
     * 
     * @param frameworkManager
     *            The framework manager
     */
    public void setFrameworkManager(EJFrameworkManager frameworkManager);

    /**
     * Returns the <code>{@link EJFrameworkManager}</code> for this application
     * <p>
     * The <code>FrameworkManager</code> is used for interaction to the core
     * framework
     * 
     * @return the applications <code>{@link EJFrameworkManager}
     */
    public EJFrameworkManager getFrameworkManager();

    /**
     * Returns the applications messenger
     * <p>
     * The messenger will be used to display messages to the user
     * 
     * @return The applications messenger
     */
    public EJMessenger getApplicationMessenger();

    /**
     * Adds a given form to the FormContainer
     * <p>
     * After the {@link EJFrameworkManager} has been called to open a form, then
     * this will be called to add it to the forms container and make it visible
     * to the users
     * 
     * @param form
     *            The form to add to the form container
     * @param blocking
     *            Indicates if the form should be added in blocking or non
     *            blocking mode
     */
    public void addFormToContainer(EJInternalForm form, boolean blocking);

    /**
     * Removes a given form from the container
     * 
     * @param form
     *            The form to be removed from the container
     */
    public void removeFormFromContainer(EJInternalForm form);

    /**
     * Informs the form renderer to open a popup form
     * <p>
     * A popup form is a normal form that will be opened in a modal window or as
     * part of the current form. The modal form normally has a direct connection
     * to this form and may receive or return values to or from the calling form
     * 
     * @param popupFormController
     *            The controller holding all required values to open the popup
     *            form
     */
    public void openPopupForm(EJPopupFormController popupFormController);

    /**
     * Informs the application manager to open an embedded form
     * <p>
     * An embedded form is a normal form that will be opened within another form
     * on a form canvas. The embedded form runs as a stand alone form, but using
     * another form as its container
     * 
     * @param embeddedFormController
     *            The controller holding all required values to open the
     *            embedded form
     */
    public void openEmbeddedForm(EJEmbeddedFormController embeddedFormController);

    /**
     * Called after the form controller of the popup form has closed.
     * <p>
     * The normal approach for popup forms is that the <code>FormManager</code>
     * will get called to open a popup form. The manager will inform its
     * controller and the controller will inform the forms renderer to display
     * the popup form. When the form is closed, the controller will inform the
     * renderer that the popup window must also be closed. If the popup window
     * is closed, then the form renderer needs to inform the
     * <code>PopupFormController</code> that the form should be closed. The
     * controller will close the form and inform the renderer that the window
     * holding the popup form can now be deleted
     */
    public void popupFormClosed();

    /**
     * Used to close an embedded form
     * 
     * @param embeddedFormController
     *            The controller of the embedded form to be closed
     */
    public void closeEmbeddedForm(EJEmbeddedFormController embeddedFormController);

    /**
     * Checks to see if a form with the given name is already open
     * <p>
     * This can be used if only one form of each name is allowed to be opened
     * 
     * @param formName
     * @return <code>true</code> if the form is opened otherwise
     *         <code>false</code>
     */
    public boolean isFormOpened(String formName);

    /**
     * Instructs the application manager to switch for the given form name
     * <p>
     * If there is no form with the given name, then an exception is raised
     * otherwise, the new form will be displayed and the forms
     * {@link EJInternalForm} will be returned
     * 
     * @param formName
     *            The name of the form to switch to
     * @return The {@link EJInternalForm} of the newly focused form
     */
    public EJInternalForm switchToForm(String formName);

    /**
     * Returns the current active form within the application
     * 
     * @return The current opened form, or <code>null</code> if no form is
     *         active
     */
    public EJInternalForm getActiveForm();

    /**
     * Returns the {@link EJInternalForm} with the given name or
     * <code>null</code> if the given form is not opened
     * 
     * @param formName
     *            The name of the required form
     * @return The {@link EJInternalForm} or <code>null</code> if the form is
     *         not yet opened
     */
    public EJInternalForm getForm(String formName);

    /**
     * Indicates how many forms are currently opened
     * 
     * @return The amount of forms currently opened
     */
    public int getOpenedFormCount();

    /**
     * Returns a collection of currently opened forms or an empty collection of
     * there are currently no opened forms
     * 
     * @return A collection of opened forms or an empty collection if there are
     *         currently no opened forms
     */
    public Collection<EJInternalForm> getOpenForms();
    
    /**
     * Indicates container to update title of given form 
     * 
     * @param form
     *            The form to be updated in the the container
     */
    public void updateFormTitle(EJInternalForm form);


}
