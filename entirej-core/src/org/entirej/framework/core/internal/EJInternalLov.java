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
import java.util.Locale;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJManagedFrameworkConnection;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.actionprocessor.interfaces.EJLovActionProcessor;
import org.entirej.framework.core.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.core.data.controllers.EJDateHelper;
import org.entirej.framework.core.data.controllers.EJFormController;
import org.entirej.framework.core.data.controllers.EJLovController;
import org.entirej.framework.core.data.controllers.EJLovRendererController;
import org.entirej.framework.core.data.controllers.EJManagedLovRendererController;
import org.entirej.framework.core.data.controllers.EJQuestion;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.interfaces.EJMessenger;
import org.entirej.framework.core.interfaces.EJTranslator;
import org.entirej.framework.core.properties.EJCoreProperties;
import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJDisplayProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJLovDefinitionProperties;

public class EJInternalLov implements Serializable
{
    private EJFormController     _formController;
    private EJLovController      _lovController;
    private EJLovActionProcessor _lovActionProcessor;
    
    public EJInternalLov(EJLovController lovController, EJLovActionProcessor lovActionProcessor)
    {
        _lovController = lovController;
        _lovActionProcessor = lovActionProcessor;
    }
    
    /**
     * Returns the action processor that is used with this lov
     * 
     * @return This lov's action processor
     */
    public EJLovActionProcessor getLovActionProcessor()
    {
        return _lovActionProcessor;
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
     * Returns this forms renderer
     * <p>
     * The method returns a wrapper around the actual form renderer. The wrapper
     * handles any exceptions thrown by the renderer. If you want to handle the
     * exceptions yourself and to react accordingly, then call the
     * {@link EJManagedLovRendererWrapper#getUnmanagedRenderer()} The unmanaged
     * renderer will not handle any of the renderer exceptions, leaving
     * everything to you
     * 
     * @return This managed wrapper around the form renderer
     */
    public EJManagedLovRendererController getManagedRendererController()
    {
        return _lovController.getManagedRendererController();
    }
    
    /**
     * Returns this lov's renderer controller
     * 
     * @return This lov's renderer controller
     */
    public EJLovRendererController getRendererController()
    {
        return _lovController.getRendererController();
    }
    
    /**
     * Returns the controller for this LovDefinition
     * 
     * @return The <code>LovController</code> for this lov
     */
    public EJLovController getController()
    {
        return _lovController;
    }
    
    /**
     * Retrieves the <code>InternalBlock</code> for the block used within this
     * lov
     * 
     * @return The block used within this lov
     */
    public EJInternalBlock getBlock()
    {
        return _lovController.getBlock();
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
     * Indicates to EJ that a question should be asked.
     * <p>
     * Asking a question is a non blocking method. This means that the control
     * will be passed back to the calling method as soon as the question is
     * presented to the user, i.e. before the user has a chance to answer the
     * question.
     * <p>
     * For this reason, the <code>FormManager</code> will call the
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
     * Instructs EJ to display a message to the users
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
     * Return the properties of the lov definition for this lov
     * 
     * @return The lov definition properties
     */
    public EJLovDefinitionProperties getProperties()
    {
        return _lovController.getDefinitionProperties();
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
        if (hasValue(propertyName) && hasValue(propertyValue))
        {
            EJFrameworkExtensionProperties lovRendererProperties = _lovController.getDefinitionProperties().getLovRendererProperties();
            if (lovRendererProperties == null)
            {
                EJMessage message = EJMessageFactory.getInstance().createMessage(
                        EJFrameworkMessage.UNABLE_TO_RETRIEVE_LOV_RENDERER_PROPERTY, _lovController.getDefinitionProperties().getName(),
                        _formController.getProperties().getName());
                throw new EJApplicationException(message);
            }
            
            lovRendererProperties.setPropertyValue(propertyName, propertyValue);
            
            EJLovRendererController renderer = _lovController.getRendererController();
            
            if (renderer != null) renderer.refreshLovRendererProperty(propertyName);
        }
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
     * Return the display properties defined for this form
     * 
     * @return Display properties defined for the form
     */
    public EJDisplayProperties getDisplayProperties()
    {
        return _lovController.getProperties().getLovDefinition().getLovRendererProperties();
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
        EJManagedFrameworkConnection localConnection = null;
        try
        {
            localConnection = _formController.getFrameworkManager().getConnection();
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
            localConnection = _formController.getFrameworkManager().getConnection();
            
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
            localConnection = _formController.getFrameworkManager().getConnection();
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
            localConnection = _formController.getFrameworkManager().getConnection();
            
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
    
    public EJDateHelper createDateHelper()
    {
        return _formController.getInternalForm().createDateHelper();
    }
    
}
