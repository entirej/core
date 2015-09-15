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
package org.entirej.framework.core.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJManagedFrameworkConnection;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.EJParameterList;
import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.core.data.controllers.EJCanvasController;
import org.entirej.framework.core.data.controllers.EJDateHelper;
import org.entirej.framework.core.data.controllers.EJEditableBlockController;
import org.entirej.framework.core.data.controllers.EJEmbeddedFormController;
import org.entirej.framework.core.data.controllers.EJFormController;
import org.entirej.framework.core.data.controllers.EJFormParameter;
import org.entirej.framework.core.data.controllers.EJInternalQuestion;
import org.entirej.framework.core.data.controllers.EJLovController;
import org.entirej.framework.core.data.controllers.EJManagedActionController;
import org.entirej.framework.core.data.controllers.EJPopupFormController;
import org.entirej.framework.core.data.controllers.EJQuestion;
import org.entirej.framework.core.data.controllers.EJQuestionController;
import org.entirej.framework.core.enumerations.EJCanvasType;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.enumerations.EJPopupButton;
import org.entirej.framework.core.interfaces.EJMessenger;
import org.entirej.framework.core.interfaces.EJTranslator;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreProperties;
import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJDisplayProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJCanvasProperties;
import org.entirej.framework.core.renderers.EJManagedFormRendererWrapper;
import org.entirej.framework.core.renderers.eventhandlers.EJBlockFocusedListener;
import org.entirej.framework.core.renderers.eventhandlers.EJFormEventListener;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusListener;
import org.entirej.framework.core.renderers.eventhandlers.EJItemValueChangedListener;
import org.entirej.framework.core.renderers.eventhandlers.EJNewRecordFocusedListener;
import org.entirej.framework.core.renderers.interfaces.EJFormRenderer;

public class EJInternalForm implements Serializable
{
    private EJFormController                      _formController;
    private EJQuestionController                  _questionController;

    private Map<String, EJEmbeddedFormController> _embeddedForms = new HashMap<String, EJEmbeddedFormController>();

    public EJInternalForm(EJFormController formController)
    {
        _formController = formController;
        _questionController = new EJQuestionController();
    }

    /**
     * Returns the application messenger
     * 
     * @return The application messenger
     */
    public EJMessenger getMessenger()
    {
        return _formController.getMessenger();
    }

    public EJFormController getFormController()
    {
        return _formController;
    }

    /**
     * Returns a managed framework connection
     * 
     * @return The connection
     */
    public EJManagedFrameworkConnection getFrameworkConnection()
    {
        return getFrameworkManager().getConnection();
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
        _formController.getFrameworkManager().handleException(exception);
    }

    /**
     * Must be called whenever the form gains focus
     * <p>
     * This method will inform the forms action processor that the form has
     * gained focus. This will call the forms developers application logic that
     * should be called when the new form gains focus.
     */
    public void focusGained()
    {
        _formController.getManagedActionController().focusGained(new EJForm(this));
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
        return _formController.isDirty();
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
        _formController.closeForm();
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
        _formController.clearForm(disregardChanges);
    }

    /**
     * Returns the framework manager
     * 
     * @return The framework manager
     */
    public EJFrameworkManager getFrameworkManager()
    {
        return _formController.getFrameworkManager();
    }

    /**
     * Returns the action controller for this form
     * 
     * @return
     */
    public EJManagedActionController getActionController()
    {
        return _formController.getManagedActionController();
    }

    /**
     * Returns this forms renderer
     * <p>
     * The method returns a wrapper around the actual form renderer. The wrapper
     * handles any exceptions thrown by the renderer. If you want to handle the
     * exceptions yourself and to react accordingly, then call the
     * {@link EJManagedFormRendererWrapper#getUnmanagedRenderer()} The unmanaged
     * renderer will not handle any of the renderer exceptions, leaving
     * everything to you
     * 
     * @return This managed wrapper around the form renderer
     */
    public EJManagedFormRendererWrapper getManagedRenderer()
    {
        return _formController.getManagedRenderer();
    }

    /**
     * Returns this forms renderer
     * 
     * @return This forms renderer
     */
    public EJFormRenderer getRenderer()
    {
        return _formController.getManagedRenderer().getUnmanagedRenderer();
    }

    /**
     * Returns the canvas controller from this form
     * <p>
     * A CanvasManager controls everything to do with canvases, open, close, set
     * visible etc etc
     * 
     * @return The canvas controller
     */
    public EJCanvasController getCanvasController()
    {
        return _formController.getCanvasController();
    }

    /**
     * Returns the controller for the LovDefinition with the given name
     * 
     * @param lovDefinitionName
     *            The name of the lov definition for which the controller will
     *            be returned
     * @return The <code>LovController</code> of the given lov definition or
     *         <code>null</code> if there is no lov definition with the given
     *         name
     */
    public EJLovController getLovController(String lovDefinitionName)
    {
        return _formController.getLovController(lovDefinitionName);
    }

    /**
     * Instructs EntireJ to open a given popup canvas
     * <p>
     * Before the command has been sent to the <code>IFormRenderer</code> to
     * display the popup canvas, the
     * <code>{@link EJFormActionProcessor#preOpenPopupCanvas(org.entirej.framework.core.Form, String)}</code>
     * will be called
     * <P>
     * Once the command has been sent to the <code>IFormRenderer</code> to
     * display the popup canvas, the
     * <code>{@link EJFormActionProcessor#popupCanvasOpened(org.entirej.framework.core.Form, String)}</code>
     * will be called
     * <p>
     * The popup canvas definition, defines 3 buttons that can be shown on the
     * popup. These are defined within <code>{@link EJPopupButton}}</code>. When
     * the used clicks on one of the buttons to close the popup, the Action
     * Processors method
     * <code>{@link EJFormActionProcessor#popupCanvasClosed(org.entirej.framework.core.Form, String, EJPopupButton)}</code>
     * will be called
     * 
     * @param canvasName
     *            The name of the popup canvas that should be displayed
     */
    public void showPopupCanvas(String canvasName)
    {
        if (canvasName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD,
                    "EJInternalForm.openPopupCanvas"));
        }

        try
        {
            _formController.getCanvasController().showPopupCanvas(canvasName);
        }
        catch (Exception e)
        {
            _formController.getFrameworkManager().handleException(e);
        }
    }

    /**
     * Instructs EntireJ to close a given popup canvas
     * <P>
     * By calling this method to close the popup canvas, no closing and closed
     * events will be passed to the forms action processor
     * 
     * @param canvasName
     *            The name of the popup canvas that should be closed
     */
    public void closePopupCanvas(String canvasName)
    {
        if (canvasName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD,
                    "EJInternalForm.closePopupCanvas"));
        }

        try
        {
            _formController.getRenderer().closePopupCanvas(canvasName);
        }
        catch (Exception e)
        {
            _formController.getFrameworkManager().handleException(e);
        }
    }

    /**
     * Instructs EntireJ to display the given stacked canvas page
     * <p>
     * Before the stacked page is shown, the
     * <code>{@link EJFormActionProcessor#preShowStackedPage(org.entirej.framework.core.Form, String, String)}</code>
     * will be called
     * <p>
     * After the stacked page has been shown, the
     * <code>{@link EJFormActionProcessor#stackedPageChanged(org.entirej.framework.core.Form, String, String)}</code>
     * will be called
     * 
     * @param canvasName
     *            The name of the stacked canvas
     * @param pageName
     *            The name of the stacked page
     */
    public void showStackedCanvasPage(String canvasName, String pageName)
    {
        if (canvasName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD,
                    "EJInternalForm.showStackedCanvasPage"));
        }

        try
        {
            _formController.getCanvasController().showStackedPage(canvasName, pageName);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Instructs EntireJ to display the given tab canvas page
     * <p>
     * Before the tab page is shown, the
     * <code>{@link EJFormActionProcessor#preShowTabPage(org.entirej.framework.core.Form, String, String)}</code>
     * will be called
     * <p>
     * After the tab page has been shown, the
     * <code>{@link EJFormActionProcessor#tabPageChanged(org.entirej.framework.core.Form, String, String)}</code>
     * will be called
     * 
     * @param canvasName
     *            The name of the tab canvas
     * @param pageName
     *            The name of the tab page
     */
    public void showTabCanvasPage(String canvasName, String pageName)
    {
        if (canvasName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD,
                    "Form.showTabCanvasPage"));
        }

        try
        {
            _formController.getCanvasController().showTabPage(canvasName, pageName);
        }
        catch (Exception e)
        {
            handleException(e);
        }
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
     */
    public void setTabPageVisible(String tabCanvasName, String tabPageName, boolean visible)
    {
        _formController.getCanvasController().setTabPageVisible(tabCanvasName, tabPageName, visible);
    }

    /**
     * Returns an immutable collection of all blocks available within this form
     * 
     * @return All blocks within this form
     */
    public Collection<EJInternalEditableBlock> getAllBlocks()
    {
        ArrayList<EJInternalEditableBlock> blocks = new ArrayList<EJInternalEditableBlock>();

        for (EJEditableBlockController controller : _formController.getAllBlockControllers())
        {
            blocks.add(controller.getBlock());
        }

        return Collections.unmodifiableCollection(blocks);
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
    public EJInternalEditableBlock getBlock(String blockName)
    {
        EJEditableBlockController blockController = _formController.getBlockController(blockName);
        if (blockController == null)
        {
            return null;
        }

        return blockController.getBlock();
    }

    /**
     * Returns the currently focused block or <code>null</code> if there is no
     * block focused
     * 
     * @return The currently focused block or <code>null</code> if no block has
     *         focus
     */
    public EJInternalEditableBlock getFocusedBlock()
    {
        if (_formController.getFocusedBlockController() != null)
        {
            return _formController.getFocusedBlockController().getBlock();
        }
        else
        {
            return null;
        }
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
        return EJCoreProperties.getInstance().getVisualAttributesContainer().getVisualAttributeProperties(vaName);
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
        EJCoreProperties.getInstance().getVisualAttributesContainer().replaceVisualAttribute(vaProperties);
    }

    /**
     * Adds a <code>IBlockFocusedListener</code> to this form
     * <p>
     * A block focused listener will be notified whenever a block gains focus
     * 
     * @param listener
     *            The block focus listener to add
     */
    public void addBlockFocusedListener(EJBlockFocusedListener listener)
    {
        _formController.addBlockFocusedListener(listener);
    }

    /**
     * Adds an <code>IItemFocusListener</code> to this form
     * <p>
     * An item focus listener will be notified each time an item gains or loses
     * focus
     * 
     * @param listener
     *            The item focus listener to add
     */
    public void addItemFocusListener(EJItemFocusListener listener)
    {
        _formController.addItemFocusListener(listener);
    }

    /**
     * Adds an <code>IItemValueChangedListener</code> to this form
     * <p>
     * An item value changed listener will be notified each time an item is
     * modified by a user
     * 
     * @param listener
     *            The item value changed listener to add
     */
    public void addItemValueChangedListener(EJItemValueChangedListener listener)
    {
        _formController.addItemValueChangedListener(listener);
    }

    /**
     * Adds an <code>EJFormEventListener</code> to this form
     * <p>
     * An form even listener will be notified each time an form even trigger by
     * a user
     * 
     * @param listener
     *            The item value changed listener to add
     */
    public void addFormEventListener(EJFormEventListener listener)
    {
        _formController.addFormEventListener(listener);
    }

    /**
     * Adds an <code>INewRecordFocusedListener</code> to this form
     * <p>
     * A new record focus listener will be notified each time a block navigates
     * to a different record
     * 
     * @param listener
     *            The new record focus listener to add
     */
    public void addNewRecordFocusedListener(EJNewRecordFocusedListener listener)
    {
        _formController.addNewRecordFocusedListener(listener);
    }

    /**
     * Removes a <code>IBlockFocusedListener</code> from this form
     * 
     * @param The
     *            listener to remove
     * @see #addListener(EJBlockFocusedListener)
     */
    public void removeBlockFocusedListener(EJBlockFocusedListener listener)
    {
        _formController.removeBlockFocusedListener(listener);
    }

    /**
     * Removes a <code>EJFormEventListener</code> from this form
     * 
     * @param The
     *            listener to remove
     * @see #addListener(EJFormEventListener)
     */
    public void removeFormEventListener(EJFormEventListener listener)
    {
        _formController.removeFormEventListener(listener);
    }

    /**
     * Removes a <code>IItemFocusListener</code> from this form
     * 
     * @param The
     *            listener to remove
     * @see #addListener(EJItemFocusListener)
     */
    public void removeItemFocusListener(EJItemFocusListener listener)
    {
        _formController.removeItemFocusListener(listener);
    }

    /**
     * Removes a <code>IItemValueChangedListener</code> from this form
     * 
     * @param The
     *            listener to remove
     * @see #addListener(EJItemValueChangedListener)
     */
    public void removeItemValueChangedListener(EJItemValueChangedListener listener)
    {
        _formController.removeItemValueChangedListener(listener);
    }

    /**
     * Removes a <code>INewRecordFocusedListener</code> from this form
     * 
     * @param The
     *            listener to remove
     * @see #addListener(EJNewRecordFocusedListener)
     */
    public void removeNewRecordFocusedListener(EJNewRecordFocusedListener listener)
    {
        _formController.removeNewRecordFocusedListener(listener);
    }

    /**
     * Indicates to the form manager that a question should be asked.
     * <p>
     * Asking a question is a non blocking method. This means that the control
     * will be passed back to the calling method as soon as the question is
     * presented to the user, i.e. before the user has a chance to answer the
     * question.
     * <p>
     * For this reason, the <code>EJInternalForm</code> will call the
     * <code>{@link IActionProcessor#questionAnswered(String, int)}
     * method of the calling action processor. The <code>Question</code>
     * contains the users answer to the question
     * <p>
     * 
     * @param question
     *            The question to be asked
     */
    public void askQuestion(EJQuestion question)
    {
        if (question != null)
        {
            _formController.getMessenger().askQuestion(question);
        }
    }

    /**
     * This must be called after <b>EntireJ</b> has asked a question using
     * {@link EJMessenger#askInternalQuestion(EJQuestion)}
     * 
     * @param questionAnswered
     *            The question the user has answered
     */
    public void internalQuestionAnswered(EJInternalQuestion questionAnswered)
    {
        _questionController.handleAnsweredQuestion(questionAnswered);
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
        if (message != null)
        {
            _formController.getMessenger().handleMessage(message);
        }
    }

    /**
     * Indicates to this controller that all open changes within the form should
     * be saved
     * <p>
     * The form renderer will be informed after the changes have been saved
     */
    public void saveChanges()
    {
        try
        {
            _formController.saveChanges();

            // Tell the application developer that a form save has been made.
            getFormController().getUnmanagedActionController().postFormSave(_formController.getEJForm());
        }
        catch (Exception e)
        {
            throw new EJApplicationException(e);
        }
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
        _formController.getFrameworkManager().setApplicationLevelParameter(valueName, value);
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
        return _formController.getFrameworkManager().getApplicationLevelParameter(paramName);
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
        if (_formController.getParameterList().contains(name))
        {
            _formController.getParameterList().getParameter(name).setValue(value);
        }
        else
        {
            EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_FORM_PARAMETER, name,
                    _formController.getProperties().getName());
            throw new EJApplicationException(message);
        }
    }

    /**
     * Returns the From Level Parameter with the given name
     * 
     * @param name
     *            The name of the required application parameter
     * @return The form parameter
     */
    public EJFormParameter getFormParameter(String name)
    {
        if (_formController.getParameterList().contains(name))
        {
            return _formController.getParameterList().getParameter(name);
        }
        else
        {
            EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_FORM_PARAMETER, name,
                    _formController.getProperties().getName());
            throw new EJApplicationException(message);
        }
    }

    /**
     * Return the properties of this form
     * 
     * @return The form properties
     */
    public EJCoreFormProperties getProperties()
    {
        return _formController.getProperties();
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
     */
    public void setDisplayProperty(String propertyName, String propertyValue)
    {
        if (hasValue(propertyName) && hasValue(propertyValue))
        {
            EJFrameworkExtensionProperties formRendererProperties = _formController.getProperties().getFormRendererProperties();
            if (formRendererProperties == null)
            {
                EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_RETRIEVE_FORM_RENDERER_PROPERTY,
                        _formController.getProperties().getName());
                throw new EJApplicationException(message);
            }

            formRendererProperties.setPropertyValue(propertyName, propertyValue);

            EJManagedFormRendererWrapper formRenderer = _formController.getManagedRenderer();

            if (formRenderer != null)
                formRenderer.refreshFormRendererProperty(propertyName);
        }
    }

    /**
     * Return the display properties defined for this form
     * 
     * @return Display properties defined for the form
     */
    public EJDisplayProperties getDisplayProperties()
    {
        return _formController.getProperties().getFormRendererProperties();
    }

    private boolean hasValue(String value)
    {
        if (value == null || value.trim().length() == 0)
        {
            return false;
        }

        return true;
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
        EJManagedFrameworkConnection localConnection = getFrameworkManager().getConnection();
        try
        {
            return _formController.getFrameworkManager().getTranslationController().translateText(textKey);
        }
        finally
        {
            localConnection.close();
        }
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
        EJManagedFrameworkConnection localConnection = null;
        try
        {
            localConnection = getFrameworkManager().getConnection();

            if (locale == null)
            {
                return _formController.getFrameworkManager().getTranslationController().translateText(textKey);
            }
            else
            {
                return _formController.getFrameworkManager().getTranslationController().translateText(textKey, locale);
            }
        }
        finally
        {
            if (localConnection != null)
            {
                localConnection.close();
            }
        }
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
        EJManagedFrameworkConnection localConnection = null;
        try
        {
            localConnection = getFrameworkManager().getConnection();
            return _formController.getFrameworkManager().getTranslationController().translateMessageText(textKey);
        }
        finally
        {
            if (localConnection != null)
            {
                localConnection.close();
            }
        }
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
        EJManagedFrameworkConnection localConnection = null;
        try
        {
            localConnection = getFrameworkManager().getConnection();

            if (locale == null)
            {
                return _formController.getFrameworkManager().getTranslationController().translateMessageText(textKey);
            }
            else
            {
                return _formController.getFrameworkManager().getTranslationController().translateMessageText(textKey, locale);
            }
        }
        finally
        {
            if (localConnection != null)
            {
                localConnection.close();
            }
        }
    }

    public void openForm(String formName, EJParameterList parameterList)
    {
        openForm(formName, parameterList, false);
    }

    public void openForm(String formName, EJParameterList parameterList, boolean blocking)
    {
        if (formName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_FORM_NAME_PASSED_TO_METHOD,
                    "EJInternalForm.openForm"));
        }

        if (blocking)
        {
            try
            {
                EJPopupFormController popupFormController = new EJPopupFormController(_formController.getFrameworkManager(), _formController, formName,
                        parameterList);
                _formController.getFrameworkManager().openPopupForm(popupFormController);
            }
            catch (Exception e)
            {
                _formController.getFrameworkManager().handleException(e);
            }
        }
        else
        {
            try
            {
                EJInternalForm form = _formController.getFrameworkManager().createInternalForm(formName, parameterList);
                _formController.getFrameworkManager().openForm(form, blocking);
            }
            catch (Exception e)
            {
                _formController.getFrameworkManager().handleException(e);
            }
        }
    }

    public void openEmbeddedForm(String formName, String formCanvasName, EJParameterList parameterList)
    {
        if (formName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_FORM_NAME_PASSED_TO_METHOD,
                    "EJInternalForm.openEmbeddedForm"));
        }
        if (formCanvasName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD,
                    "EJInternalForm.openEmbeddedForm"));
        }

        EJCanvasProperties canvasProperties = _formController.getProperties().getCanvasProperties(formCanvasName);
        if (canvasProperties == null || EJCanvasType.FORM != canvasProperties.getType())
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_CANVAS_TYPE,
                    "EJInternalForm.openEmbeddedForm", EJCanvasType.FORM.toString()));
        }

        try
        {
            EJEmbeddedFormController embeddedFormController = new EJEmbeddedFormController(_formController.getFrameworkManager(), _formController, formName,
                    formCanvasName, parameterList);

            _embeddedForms.put(formName + ":" + formCanvasName, embeddedFormController);

            _formController.getFrameworkManager().openEmbeddedForm(embeddedFormController);
        }
        catch (Exception e)
        {
            _formController.getFrameworkManager().handleException(e);
        }

    }

    /**
     * Called when an embedded form, opened by the forms EJInternalForm, is
     * closed
     * 
     * @param formName
     *            The form to close
     * @param canvasName
     *            On which canvas the form was displayed. The same form can be
     *            displayed multiple times
     */
    public void closeEmbeddedForm(String formName, String canvasName)
    {
        try
        {
            EJEmbeddedFormController controller = _embeddedForms.get(formName + ":" + canvasName);
            if (controller == null)
            {
                return;
            }

            try
            {
                _formController.getFrameworkManager().closeEmbeddedForm(controller);

            }
            catch (Exception e)
            {
                _formController.getFrameworkManager().handleException(e);
            }

        }
        catch (Exception e)
        {
            _formController.getFrameworkManager().handleException(e);
        }
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
        EJEmbeddedFormController controller = _embeddedForms.get(formName + ":" + canvasName);
        if (controller == null)
        {
            return null;
        }

        return new EJForm(controller.getEmbeddedForm());
    }

    /**
     * Called when an embedded form, opened by the forms EJInternalForm, is
     * closed
     * 
     * @param parameterList
     *            The parameter list from the embedded form
     */
    public void popupFormClosed(EJParameterList parameterList)
    {
        try
        {
            _formController.getManagedActionController().popupFormClosed(parameterList);
            _formController.getFrameworkManager().getApplicationManager().popupFormClosed();
        }
        catch (Exception e)
        {
            _formController.getFrameworkManager().handleException(e);
        }
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
        return _formController.getParameterList();
    }

    public EJDateHelper createDateHelper()
    {
        return _formController.getFrameworkManager().getTranslationController().createDateHelper();
    }
}
