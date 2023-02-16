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
package org.entirej.framework.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.entirej.framework.core.enumerations.EJMessageLevel;

public class EJMessage implements Serializable
{
    private Exception      _cause;
    private char           _replacementChar = '$';
    private String         _text;
    private String         _translatedMessage;
    private List<String>   _replacementStrings;
    private EJMessageLevel _messageLevel;
    private Date           _creationDate    = new Date(System.currentTimeMillis());
    private Runnable       _callback;
    
    public EJMessage(String text, String... replacementStrings)
    {
        initialise(null, EJMessageLevel.ERROR, text, '$', replacementStrings);
    }
    
    public EJMessage(EJMessageLevel messageLevel, String text, String... replacementStrings)
    {
        initialise(null, messageLevel, text, '$', replacementStrings);
    }
    
    public EJMessage(EJForm form, String text, String... replacementStrings)
    {
        initialise(form, EJMessageLevel.ERROR, text, '$', replacementStrings);
    }
    
    public EJMessage(EJForm form, EJMessageLevel messageLevel, String text, String... replacementStrings)
    {
        initialise(form, messageLevel, text, '$', replacementStrings);
    }
    
    public EJMessage(EJForm form, EJMessageLevel messageLevel, String text, char replacementChar, String... replacementStrings)
    {
        initialise(form, messageLevel, text, replacementChar, replacementStrings);
    }
    
    private void initialise(EJForm form, EJMessageLevel messageLevel, String text, char replacementChar, String... replacementStrings)
    {
        _messageLevel = messageLevel;
        _text = text;
        _replacementStrings = new ArrayList<String>();
        _replacementChar = replacementChar;
        
        if (replacementStrings != null)
        {
            for (String replacementString : replacementStrings)
            {
                _replacementStrings.add(replacementString);
            }
        }
        
        _translatedMessage = translateMessage(form);
    }
    
    private String translateMessage(EJForm form)
    {
        if (form == null)
        {
            String message = _text;
            String replacementChar = String.valueOf(_replacementChar);
            if (_replacementStrings != null)
            {
                for (int i = 0; i < _replacementStrings.size(); i++)
                {
                    message = message.replace(replacementChar + (i + 1), _replacementStrings.get(i));
                }
            }
            
            return message;
        }
        
        String message = form.translateMessageText(_text);
        
        String replacementChar = String.valueOf(_replacementChar);
        // replace strings
        if (_replacementStrings != null)
        {
            for (int i = 0; i < _replacementStrings.size(); i++)
            {
                String text = form.translateText(_replacementStrings.get(i));
                message = message.replace(replacementChar + (i + 1), text);
            }
        }
        
        return message;
    }
    
    /**
     * Returns the text of this message
     * <p>
     * The text returned from this method, will be translated and the
     * replacement strings replaced
     * 
     * @return The translated message
     */
    public String getMessage()
    {
        return _translatedMessage;
    }
    
    /**
     * The message level of this <code>Message</code>
     * 
     * @return The messages message level
     */
    public EJMessageLevel getLevel()
    {
        return _messageLevel;
    }
    
    /**
     * Indicates the date and time the message was created
     * 
     * @return The message creation date
     */
    public Date getCreationDate()
    {
        return _creationDate;
    }
    
    /**
     * Sets this messages exception
     * 
     * @param e
     *            The Exception that caused this message to be created
     */
    public void setException(Exception e)
    {
        _cause = e;
    }
    
    /**
     * Returns the exception that caused the message to be created
     * 
     * @return If this message was created as part of an exception, then the
     *         exception will be returned, otherwise <code>null</code>
     */
    public Exception getException()
    {
        return _cause;
    }
    
    /**
     * Returns the callback that action upon user action
     * 
     * @return If this message has callback action on upon user action, otherwise <code>null</code>
     */
    public Runnable getCallback()
    {
        return _callback;
    }
    /**
     * Sets this messages callback 
     * 
     * @param e
     *            The callback action upon user action
     */
    public void setCallback(Runnable _callback)
    {
        this._callback = _callback;
    }
}
