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
package org.entirej.framework.core;

import java.io.Serializable;
import java.util.Locale;

import org.entirej.framework.core.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.core.data.controllers.EJDateHelper;
import org.entirej.framework.core.data.controllers.EJQuestion;
import org.entirej.framework.core.interfaces.EJMessenger;
import org.entirej.framework.core.interfaces.EJTranslator;
import org.entirej.framework.core.internal.EJInternalLov;
import org.entirej.framework.core.properties.EJCoreProperties;
import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJDisplayProperties;

public class EJLov implements Serializable
{
    private EJInternalLov _internalLov;
    
    public EJLov(EJInternalLov internalLov)
    {
        _internalLov = internalLov;
    }
    
    public EJMessenger getMessenger()
    {
        return _internalLov.getMessenger();
    }
    
    public EJFrameworkManager getFrameworkManager()
    {
        return _internalLov.getFrameworkManager();
    }
    
    /**
     * Returns the block that is responsible for retrieving this lov's data
     * 
     * @return The block used by this lov
     */
    public EJLovBlock getBlock()
    {
        return new EJLovBlock(_internalLov.getBlock());
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
     * For this reason, the <code>EJForm</code> will call the
     * <code>questionAnswered</code>
     * method of the calling action processor. The <code>EJQuestion</code>
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
            _internalLov.askQuestion(question);
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
            _internalLov.showMessage(message);
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
        _internalLov.handleException(exception);
    }
    
    /**
     * Used to set the value of an application level parameter
     * 
     * @param valueName
     *            The name of the value
     * @param value
     *            The parameter value
     * @throws EJApplicationException
     */
    public void setApplicationLevelParameter(String valueName, Object value)
    {
        _internalLov.setApplicationLevelParameter(valueName, value);
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
     * @throws EJApplicationException
     *             if there is no parameter with the given name
     */
    public EJApplicationLevelParameter getApplicationLevelParameter(String paramName)
    {
        return _internalLov.getApplicationLevelParameter(paramName);
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
        _internalLov.setDisplayProperty(propertyName, propertyValue);
    }
    
    /**
     * Return the display properties defined for this form
     * 
     * @return Display properties defined for the form
     */
    public EJDisplayProperties getDisplayProperties()
    {
        return _internalLov.getDisplayProperties();
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
        return _internalLov.translateMessageText(textKey);
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
        return _internalLov.translateText(textKey, locale);
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
        return _internalLov.translateMessageText(textKey);
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
        return _internalLov.translateMessageText(textKey, locale);
    }

    public EJDateHelper createDateHelper()
    {
        return _internalLov.createDateHelper();
    }
    
}
