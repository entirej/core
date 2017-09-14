/*******************************************************************************
 * Copyright 2013 CRESOFT AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors: CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.core.data.controllers.EJDateHelper;
import org.entirej.framework.core.data.controllers.EJFileUpload;
import org.entirej.framework.core.data.controllers.EJFormParameter;
import org.entirej.framework.core.data.controllers.EJInternalQuestion;
import org.entirej.framework.core.data.controllers.EJManagedActionController;
import org.entirej.framework.core.data.controllers.EJQuestion;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.enumerations.EJPopupButton;
import org.entirej.framework.core.interfaces.EJMessenger;
import org.entirej.framework.core.interfaces.EJTranslator;
import org.entirej.framework.core.internal.EJInternalEditableBlock;
import org.entirej.framework.core.internal.EJInternalForm;
import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;
import org.entirej.framework.core.properties.EJFileLoader;
import org.entirej.framework.core.properties.definitions.interfaces.EJDisplayProperties;
import org.entirej.framework.core.properties.interfaces.EJFormProperties;

public class EJForm implements EJFrameworkHelper
{
    private EJInternalForm _form;

    public EJForm(EJInternalForm form)
    {
        _form = form;
    }

    /**
     * Returns the properties for of this form
     * 
     * @return This forms properties
     */
    public EJFormProperties getProperties()
    {
        return _form.getProperties();
    }

    /**
     * Returns the action controller for this form
     * 
     * @return This forms {@link EJManagedActionController}
     */
    public EJManagedActionController getActionController()
    {
        return _form.getActionController();
    }

    /**
     * Returns the name of this form
     * 
     * @return The name of this form
     */
    public String getName()
    {
        return _form.getProperties().getName();
    }

    /**
     * Returns the application messenger
     * 
     * @return The application messenger
     */
    public EJMessenger getMessenger()
    {
        return _form.getMessenger();
    }

    /**
     * This method should be called when to handle an exception in a
     * standardized manner
     * <p>
     * The exception will be sent to the applications messenger so that a
     * correct message will be displayed to the user. If the application has a
     * logger, then the exception will also be logged
     * 
     * @param exception
     *            The exception to handle
     */
    public void handleException(Exception exception)
    {
        _form.handleException(exception);
    }

    /**
     * Checks to see if a form with the given name is already open
     * <p>
     * This can be used if only one form of each name is allowed to be opened
     * 
     * @param formName
     * @return <code>true</code> if the form is already open, otherwise
     *         <code>false</code>
     */
    public boolean isFormOpened(String formName)
    {
        return _form.getFrameworkManager().getApplicationManager().isFormOpened(formName);
    }

    /**
     * Checks to see if the given form has been opened
     * 
     * @param form
     * @return <code>true</code> if the form is already open, otherwise
     *         <code>false</code>
     */
    public boolean isFormOpened(EJForm form)
    {
        return _form.getFrameworkManager().getApplicationManager().isFormOpened(form._form);
    }

    /**
     * Instructs the application manager to switch to the form with the given
     * name
     * <p>
     * If there is no form with the given name, then an exception is raised
     * otherwise, the new form will be displayed and the forms {@link EJForm}
     * will be returned
     * <p>
     * If there is more than one form opened with the given name, then the first
     * one will be opened
     * 
     * @param formName
     *            The name of the form to switch to
     * @return The {@link EJForm} of the newly focused form
     * @see EJForm#isFormOpened(String)
     */
    public EJForm switchToForm(String formName)
    {
        if (!isFormOpened(formName))
        {
            throw new EJApplicationException("There is no opened form with the name: " + formName);
        }

        EJInternalForm form = _form.getFrameworkManager().getApplicationManager().switchToForm(formName);
        return new EJForm(form);
    }

    /**
     * Instructs the application manager to switch to the given form
     * <p>
     * An exception will be thrown if you are trying to switch to a form that is
     * not yet opened
     * 
     * @param form
     *            The form to switch to
     * @see EJForm#isFormOpened(EJForm)
     */
    public void switchToForm(EJForm form)
    {
        if (!isFormOpened(form))
        {
            throw new EJApplicationException("There form passed to switchToForm is not yet opened");
        }

        _form.getFrameworkManager().getApplicationManager().switchToForm(form._form);

    }

    /**
     * Indicates if this form is dirty
     * <p>
     * A dirty form is a form that has had modifications. For example, it has
     * blocks which have records that have been deleted, inserted or updated
     * 
     * @return <code>true</code> if the form is dirty, otherwise
     *         <code>false</code>
     */
    public boolean isDirty()
    {
        return _form.isDirty();
    }

    /**
     * This method is called when a form should be closed
     * <p>
     * The Application Manager will be instructed to remove the form from the
     * application and then the framework will clean up all controllers and data
     * used by the form
     */
    public void close()
    {
        _form.close();
    }

    /**
     * This method is called when an embedded form should be closed
     * <p>
     * It is important to pass both the form name and the form canvas which
     * contains the embedded form because it is possible to open the same form
     * on different canvases multiple times
     * 
     * @param formName
     *            The name of the form to close
     * @param canvasName
     *            The canvas that contains the form
     */
    public void closeEmbeddedForm(String formName, String canvasName)
    {
        _form.closeEmbeddedForm(formName, canvasName);
    }

    /**
     * Instructs EntireJ to clear the form
     * <p>
     * If <code>disregardChanges</code> is <code>true</code> then all changes
     * made within the form will be disregarded
     * 
     * @param disregardChanges
     *            Indicates that all changes made to the forms data will be
     *            disregarded
     */
    public void clear(boolean disregardChanges)
    {
        _form.clear(disregardChanges);
    }

    /**
     * Used to set the current locale of the application
     * <p>
     * EntireJ stores a locale that is used by various item renderers for
     * example the NumberItemRenderer. It is used for the formatting of the
     * number etc. The default for the locale is {@link Locale.ENGLISH} but can
     * be changed via this method
     * 
     * @param locale
     *            The locale to use for this application
     */
    public void changeLocale(Locale locale)
    {
        _form.getFrameworkManager().changeLocale(locale);
    }

    /**
     * Returns the {@link Locale} that is currently set for this application
     * <p>
     * The {@link Locale} is used internally within EntireJ and within the
     * applications translator. The application can change the {@link Locale} as
     * required
     * 
     * @return The {@link Locale} specified for this application
     */
    public Locale getCurrentLocale()
    {
        return _form.getFrameworkManager().getCurrentLocale();
    }

    /**
     * Retrieves an instance of the forms current connection
     * 
     * @return The form connection
     */
    public EJManagedFrameworkConnection getConnection()
    {
        return _form.getFrameworkManager().getConnection();
    }

    /**
     * Instructs EntireJ to open a given popup canvas
     * <p>
     * Before the command has been sent to the <code>IFormRenderer</code> to
     * display the popup canvas, the
     * <code>{@link EJFormActionProcessor#preOpenPopupCanvas(EJForm, String)}</code>
     * will be called
     * <P>
     * Once the command has been sent to the <code>IFormRenderer</code> to
     * display the popup canvas, the
     * <code>{@link EJFormActionProcessor#popupCanvasOpened(EJForm, String)}</code>
     * will be called
     * <p>
     * The popup canvas definition, defines 3 buttons that can be shown on the
     * popup. These are defined within <code>{@link EJPopupButton}}</code>. When
     * the used clicks on one of the buttons to close the popup, the Action
     * Processors method
     * <code>{@link EJFormActionProcessor#popupCanvasClosed(EJForm, String, EJPopupButton)}</code>
     * will be called
     * 
     * @param canvasName
     *            The name of the popup canvas that should be displayed
     * @deprecated use {@link #getPopupCanvas(String)}
     */
    public void showPopupCanvas(String canvasName)
    {
        if (canvasName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD, "EJInternalForm.openPopupCanvas"));
        }

        getPopupCanvas(canvasName).open();
    }

    /**
     * Instructs EntireJ to close a given popup canvas
     * <P>
     * By calling this method to close the popup canvas, no closing and closed
     * events will be passed to the forms action processor
     * 
     * @param canvasName
     *            The name of the popup canvas that should be closed
     * @deprecated use {@link #getPopupCanvas(String)}
     */
    public void closePopupCanvas(String canvasName)
    {
        if (canvasName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD, "EJInternalForm.closePopupCanvas"));
        }

        getPopupCanvas(canvasName).close();
    }

    /**
     * Instructs EntireJ to display the given tab canvas page
     * <p>
     * Before the tab page is shown, the
     * <code>{@link EJFormActionProcessor#preShowTabPage(EJForm, String, String)}</code>
     * will be called
     * <p>
     * After the tab page has been shown, the
     * <code>{@link EJFormActionProcessor#tabPageChanged(EJForm, String, String)}</code>
     * will be called
     * 
     * @param canvasName
     *            The name of the tab canvas
     * @param pageName
     *            The name of the tab page
     * @deprecated user {@link #getTabCanvas(String tabCanvasName)}
     */
    public void showTabCanvasPage(String canvasName, String pageName)
    {
        if (canvasName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD, "Form.showTabCanvasPage"));
        }

        getTabCanvas(canvasName).showPage(pageName);
    }

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
     * @deprecated use {@link #getTabCanvas(String)}
     */

    public void setTabPageVisible(String tabCanvasName, String tabPageName, boolean visible)
    {
        getTabCanvas(tabCanvasName).setPageVisible(tabPageName, visible);

    }

    /**
     * Returns the name of the tab page which is currently displayed/active on a
     * given tab canvas
     * <p>
     * If the tab canvas name specified does not exist, then <code>null</code>
     * will be returned
     * 
     * @param tabCanvasName
     *            The name of the tab canvas to check
     * @return The currently displayed tab page on the given tab canvas
     * @deprecated use {@link #getTabCanvas(String)}
     */
    public String getDisplayedTabCanvasPage(String tabCanvasName)
    {
        return getTabCanvas(tabCanvasName).getDisplayedPageName();
    }

    /**
     * Instructs EntireJ to display the given stacked canvas page
     * <p>
     * Before the stacked page is shown, the
     * <code>{@link EJFormActionProcessor#preShowStackedPage(EJForm, String, String)}</code>
     * will be called
     * <p>
     * After the stacked page has been shown, the
     * <code>{@link EJFormActionProcessor#stackedPageChanged(EJForm, String, String)}</code>
     * will be called
     * 
     * @param canvasName
     *            The name of the stacked canvas
     * @param pageName
     *            The name of the stacked page
     * @deprecated use {@link #getStackedCanvas(String canvasName)}
     */
    public void showStackedCanvasPage(String canvasName, String pageName)
    {
        if (canvasName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD, "EJInternalForm.showStackedCanvasPage"));
        }

        getStackedCanvas(canvasName).showPage(pageName);
    }

    /**
     * Returns the name of the stacked page which is currently displayed/active
     * on a given stacked canvas
     * <p>
     * If the stacked canvas name specified does not exist, then
     * <code>null</code> will be returned
     * 
     * @param stackedCanvasName
     *            The name of the stacked canvas to check
     * @return The currently displayed stacked page on the given stacked canvas
     * @deprecated user {@link #getStackedCanvas(String)}
     */
    public String getDisplayedStackedCanvasPage(String stackedCanvasName)
    {
        return getStackedCanvas(stackedCanvasName).getDisplayedPageName();
    }

    /**
     * Returns an immutable collection of all blocks available within this form
     * 
     * @return All blocks within this form
     */
    public Collection<EJBlock> getAllBlocks()
    {
        ArrayList<EJBlock> blocks = new ArrayList<EJBlock>();

        for (EJInternalEditableBlock block : _form.getAllBlocks())
        {
            blocks.add(new EJBlock(block));
        }

        return blocks;
    }

    /**
     * Retrieves the required block
     * <p>
     * If there is no block with the given name then an exception will be thrown
     * 
     * @param blockName
     *            The name of the required block
     * @return The required block or <code>null</code> if no block exists with
     *         the given name.
     */
    public EJBlock getBlock(String blockName)
    {
        EJInternalEditableBlock block = _form.getBlock(blockName);
        if (block == null)
        {
            return null;
        }
        else
        {
            return new EJBlock(block);
        }

    }

    /**
     * Returns the current active form within the application
     * 
     * @return The current opened form, or <code>null</code> if no form is
     *         active
     */
    public EJForm getActiveForm()
    {
        EJInternalForm form = _form.getFrameworkManager().getApplicationManager().getActiveForm();
        if (form == null)
        {
            return null;
        }
        else
        {
            return new EJForm(form);
        }
    }

    /**
     * Returns a collection of currently opened forms
     * 
     * @return A collection containing the currently opened forms
     */
    public Collection<EJForm> getOpenedForms()
    {
        ArrayList<EJForm> openForms = new ArrayList<EJForm>();
        Collection<EJInternalForm> forms = _form.getFrameworkManager().getApplicationManager().getOpenedForms();

        if (forms == null || forms.isEmpty())
        {
            return openForms;
        }
        else
        {
            for (EJInternalForm form : forms)
            {
                openForms.add(new EJForm(form));
            }
            return openForms;
        }
    }

    /**
     * Returns the amount of forms that are currently opened within the Form
     * Container
     * 
     * @return The amount of forms opened within the Form Container used in the
     *         application
     */
    public int getOpenedFormCount()
    {
        return _form.getFrameworkManager().getApplicationManager().getOpenedFormCount();
    }

    /**
     * Returns the {@link EJForm} with the given name or <code>null</code> if
     * there is no opened form with this name
     * <p>
     * <b>The form must be open in order to be retrieved</b>
     * 
     * @param name
     *            The name of the form to retrieve
     * @return The {@link EJForm} with the given name or <code>null</code> if
     *         the form is not yet opened
     */
    public EJForm getForm(String name)
    {
        return _form.getFrameworkManager().getForm(name);
    }

    
    @Override
    public EJTabLayoutComponent getTabLayoutComponent(String name)
    {
        return new EJTabLayoutComponent(_form.getFrameworkManager().getApplicationManager(), name);
    }
    
    /**
     * Returns the name of the first navigable block on this form
     * <p>
     * If a form property is set, this will be returned, if now, then the first
     * displayed block in the forms list of blocks will be returned
     * 
     * @return
     */
    public String getFirstNavigableBlock()
    {
        return _form.getFirstNavigableBlock();
    }

    /**
     * Returns the currently focused block or <code>null</code> if there is no
     * block focused
     * 
     * @return The currently focused block or <code>null</code> if no block has
     *         focus
     */
    public EJBlock getFocusedBlock()
    {
        EJInternalEditableBlock block = _form.getFocusedBlock();
        if (block == null)
        {
            return null;
        }
        else
        {
            return new EJBlock(block);
        }
    }

    /**
     * Used to retrieve an {@link EJCanvas} with the given name
     * 
     * @param name
     *            The name of the canvas
     * @return The canvas
     */
    public EJCanvas getCanvas(String name)
    {
        return new EJCanvas(_form, name);
    }

    /**
     * Used to retrieve an {@link EJTabCanvas} with the given name
     * 
     * @param name
     *            The name of the tab canvas
     * @return The tab canvas
     */
    public EJTabCanvas getTabCanvas(String name)
    {
        return new EJTabCanvas(_form, name);
    }

    /**
     * Used to retrieve an {@link EJDrawerCanvas} with the given name
     * 
     * @param name
     *            The name of the drawer canvas
     * @return The drawer canvas
     */
    public EJDrawerCanvas getDrawerCanvas(String name)
    {
        return new EJDrawerCanvas(_form, name);
    }

    /**
     * Used to retrieve an {@link EJPopupCanvas} with the given name
     * 
     * @param name
     *            The name of the popup canvas
     * @return The poup canvas
     */
    public EJPopupCanvas getPopupCanvas(String name)
    {
        return new EJPopupCanvas(_form, name);
    }

    /**
     * Used to retrieve an {@link EJStackedCanvas} with the given name
     * 
     * @param name
     *            The name of the stacked canvas
     * @return The stacked canvas
     */
    public EJStackedCanvas getStackedCanvas(String name)
    {
        return new EJStackedCanvas(_form, name);
    }

    /**
     * Returns the <code>VisualAttributeProperties</code> with the given name or
     * <code>null</code> if there is no visual attribute with the given name
     * 
     * @param vaName
     *            the name of the required <code>VisualAttribute</code>
     * 
     * @return The required <code>VisualAttributeProperties</code> or
     *         <code>null</code> if there was no Visual Attribute with the given
     *         name
     */
    public EJCoreVisualAttributeProperties getVisualAttribute(String vaName)
    {
        return _form.getVisualAttribute(vaName);
    }

    /**
     * Adds the given <code>VisualAttributeProperties</code> to the frameworks
     * list of Visual Attributes
     * <p>
     * The given visual attribute will replace any visual attribute that
     * currently exists with the same name
     * 
     * @param vaProperties
     *            The visual attribute to add
     */
    public void addVisualAttribute(EJCoreVisualAttributeProperties vaProperties)
    {
        _form.addVisualAttribute(vaProperties);
    }

    /**
     * Indicates to the form manager that a question should be asked.
     * <p>
     * Asking a question is a non blocking method. This means that the control
     * will be passed back to the calling method as soon as the question is
     * presented to the user, i.e. before the user has a chance to answer the
     * question.
     * <p>
     * For this reason, the <code>EJFormManager</code> will call the actions
     * processors, question answered method of the calling action processor. The
     * <code>EJQuestion</code> contains the users answer to the question
     * <p>
     * 
     * @param question
     *            The question to be asked
     */
    public void askQuestion(EJQuestion question)
    {
        _form.askQuestion(question);
    }

    /**
     * Indicates to the form manager that a file upload has been requested
     * <p>
     * Asking for a file upload is a non blocking method. This means that the
     * control will be passed back to the calling method as soon as the file
     * upload dialog is presented to the user, i.e. before the user has a chance to
     * select files.
     * <p>
     * For this reason, {@link EJFormManager} will call the actions
     * processors, {@link EJFormActionProcessor#filesUploaded(EJFileUpload)} method of the calling action processor. The
     * <code>{@link EJFileUpload}</code> contains the users selected files
     * <p>
     * 
     * @param fileUpload
     *            The file upload request
     */
    public void uploadFile(EJFileUpload fileUpload)
    {
        _form.uploadFile(fileUpload);

    }

    /**
     * Instructs the form manager to display a message to the users
     * <p>
     * How the message is displayed is dependent upon the severity of the
     * message and how the applications {@link EJMessenger} has been implemented
     * 
     * @param message
     *            The message to display
     */
    public void showMessage(EJMessage message)
    {
        _form.showMessage(message);
    }

    /**
     * Indicates to this controller that all open changes within the form should
     * be saved
     * <p>
     * The form renderer will be informed after the changes have been saved
     */
    public void saveChanges()
    {
        _form.saveChanges();
    }

    /**
     * Used to set the value of an application level parameter
     * 
     * @param valueName
     *            The name of the value
     * @param value
     *            The parameter value
     */
    public void setApplicationLevelParameter(String valueName, Object value)
    {
        _form.setApplicationLevelParameter(valueName, value);
    }

    /**
     * Retrieves an application level parameter value with the given name
     * <p>
     * If there is no parameter with the given name then an exception will be
     * thrown
     * 
     * @param paramName
     *            The name of the required parameter
     * @return The value of the given parameter
     */
    public EJApplicationLevelParameter getApplicationLevelParameter(String paramName)
    {
        return _form.getApplicationLevelParameter(paramName);
    }

    /**
     * Sets the given form parameter to the given value
     * 
     * @param name
     *            The name of the parameter to set
     * @param value
     *            The value of the parameter
     * @throws EJApplicationException
     *             id there is no property with the given name or the data type
     *             of the given object is not the same as defined within the
     *             form
     */
    public void setFormParameter(String name, Object value)
    {
        _form.setFormParameter(name, value);
    }

    /**
     * Sets the title of the form
     * 
     * @param title
     *            The title of the form to set
     * 
     */
    public void setTitle(String title)
    {
        _form.setTitle(title);
    }

    /**
     * Returns the From Level Parameter with the given name
     * 
     * @param name
     *            The name of the required application parameter
     * @return The form parameter
     * @throws EJApplicationException
     *             if there is no parameter with the given value
     */
    public EJFormParameter getFormParameter(String name)
    {
        return _form.getFormParameter(name);
    }

    /**
     * Set the display properties for this form
     * <p>
     * The name of the properties and their functionality are dependent on the
     * form renderer being used. Please see the corresponding form renderer
     * documentation for more details
     * 
     * @param propertyName
     *            The name of the property
     * @param propertyValue
     *            The property value
     * @throws EJApplicationException
     *             if there is no display property with the given name
     */
    public void setDisplayProperty(String propertyName, String propertyValue)
    {
        _form.setDisplayProperty(propertyName, propertyValue);
    }

    /**
     * Returns the display properties defined for this form
     * 
     * @return The display properties defined for this form
     */
    public EJDisplayProperties getDisplayProperties()
    {
        return _form.getDisplayProperties();
    }

    /**
     * This must be called after <b>EntireJ</b> has asked a question using
     * {@link EJMessenger#askInternalQuestion(EJQuestion)}
     * 
     * @param questionAnswered
     *            The question the user has answered
     */
    public void internalQuestionAnswered(EJInternalQuestion question)
    {
        _form.internalQuestionAnswered(question);
    }

    /**
     * Translates a given text to the current application <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textKey
     *            The value to translate
     * @return The translated text for the given key
     */
    public String translateText(String textKey)
    {
        return _form.translateText(textKey);
    }

    /**
     * Translates a given text to the given <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textKey
     *            The value to translate
     * @return The translated text for the given key
     */
    public String translateText(String textKey, Locale locale)
    {
        return _form.translateText(textKey, locale);
    }

    /**
     * Translates a given message text to the current application
     * <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textKey
     *            The value to translate
     * @return The translated text for the given key
     */
    public String translateMessageText(String textKey)
    {
        return _form.translateMessageText(textKey, null);
    }

    /**
     * Translates a given message text to the given <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textKey
     *            The value to translate
     * @return The translated text for the given key
     */
    public String translateMessageText(String textKey, Locale locale)
    {
        return _form.translateMessageText(textKey, locale);
    }

    public void openForm(String formName)
    {
        openForm(formName, null, false);
    }

    public void openForm(String formName, EJParameterList parameterList)
    {
        openForm(formName, parameterList, false);
    }

    public void openForm(String formName, EJParameterList parameterList, boolean blocking)
    {
        _form.openForm(formName, parameterList, blocking);
    }

    /**
     * Informs EntireJ to open the given form within one of this forms canvases
     * <p>
     * This is a way to embed forms within other forms. The form will run self
     * sufficiently and totally separately from the form it is opened within.
     * The form will also have its own Action Processor and any events fired
     * within it are handled by the called form and not from within this form
     * 
     * @param formName
     *            The name of the form to be opened
     * @param formCanvasName
     *            The name of the form canvas within which the form will be
     *            opened
     * @param parameterList
     *            A list of parameters to be passed to and from the called form
     * 
     * @throws EJApplicationException
     *             if the canvas specified is not a Form Canvas
     */
    public void openEmbeddedForm(String formName, String formCanvasName, EJParameterList parameterList) throws EJApplicationException
    {
        _form.openEmbeddedForm(formName, formCanvasName, parameterList);
    }

    /**
     * Used to retrieve an embedded form that was opened within this form
     * 
     * @param formName
     *            The name of the embedded form
     * @param canvasName
     *            The name of the canvas that holds the form
     * @return The embedded <code>EJForm</code> or <code>null</code> if there is
     *         no embedded form with the given name on the given canvas
     */
    public EJForm getEmbeddedForm(String formName, String canvasName)
    {
        return _form.getEmbeddedForm(formName, canvasName);
    }

    public void runReport(String reportName, EJParameterList parameterList)
    {
        _form.getFrameworkManager().runReport(reportName, parameterList);
    }

    public void runReport(String reportName)
    {
        _form.getFrameworkManager().runReport(reportName);
    }

    public String generateReport(String reportName, EJParameterList parameterList)
    {
        return _form.getFrameworkManager().generateReport(reportName, parameterList);
    }

    public String generateReport(String reportName)
    {
        return _form.getFrameworkManager().generateReport(reportName);
    }

    public void runReportAsync(String reportName)
    {
        runReportAsync(reportName, null, null);

    }

    public void runReportAsync(String reportName, EJMessage completedMessage)
    {
        runReportAsync(reportName, null, completedMessage);

    }

    public void runReportAsync(String reportName, EJParameterList parameterList)
    {
        runReportAsync(reportName, parameterList, null);

    }

    @Override
    public void runReportAsync(String reportName, EJParameterList parameterList, EJMessage completedMessage)
    {
        _form.getFrameworkManager().runReportAsync(reportName, parameterList, completedMessage);

    }

    /**
     * Returns this forms parameter list
     * <p>
     * the parameter list is a list of properties that were declared for the
     * form within the EntireJ Form Plugin. These parameters are used when
     * either calling another form or when another form calls this form. They
     * are used to pass values to and from the calling forms
     * 
     * @return This forms parameter list
     */
    public EJParameterList getParameterList()
    {
        return _form.getParameterList();
    }

    public EJDateHelper createDateHelper()
    {
        return _form.createDateHelper();
    }

    @Override
    public EJTranslatorHelper getTranslatorHelper()
    {
        return _form.getFrameworkManager().getTranslatorHelper();
    }

    @Override
    public void handleMessage(EJMessage message)
    {
        _form.getFrameworkManager().handleMessage(message);
    }

    @Override
    public void handleException(Exception exception, boolean showUserMessage)
    {
        _form.getFrameworkManager().handleException(exception, showUserMessage);
    }

    @Override
    public void askInternalQuestion(EJInternalQuestion question)
    {
        _form.getFrameworkManager().askInternalQuestion(question);
    }


    /**
     * Loads a given fully qualified file from either the Classpath or an
     * absolute path
     * <p>
     * First the <code>EJFileLoader</code> will check the CLASSPATH for the file
     * using
     * <code>EJFileLoader.class.getClassLoader().getResourcesAsStream()</code>
     * and if this is not successful, then <code>FileLoader</code> will try to
     * create a new <code>File</code> instance using the file name given. If the
     * file is found then the file will be read into a DOM tree and the DOM
     * document will be returned. If the file is not found then
     * <code>null</code> will be returned.
     * 
     * @param fileName
     *            The file required
     * @return The file contents as a DOM tree or null if the file was not found
     * @throws IllegalArgumentException
     *             if there were problems incurred when opening required file
     */
    public InputStream loadFileFromClasspath(String fileName)
    {
        return EJFileLoader.loadFile(fileName);
    }

    /**
     * Used to check if a given file exists
     * <p>
     * <p>
     * First the <code>EJFileLoader</code> will check the CLASSPATH for the file
     * using
     * <code>EJFileLoader.class.getClassLoader().getResourcesAsStream()</code>
     * and if this is not successful, then <code>EJFileLoader</code> will try to
     * create a new <code>File</code> instance using the file name given.
     * 
     * @param fileName
     *            The name of the file to check
     * @return <code>true</code> if the file exists otherwise <code>false</code>
     */
    public static boolean fileExistsOnClasspath(String fileName)
    {
        return EJFileLoader.fileExists(fileName);
    }
}
