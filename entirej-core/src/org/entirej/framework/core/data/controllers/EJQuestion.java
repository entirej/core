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
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;

import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.actionprocessor.EJDefaultFormActionProcessor;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.enumerations.EJQuestionButton;
import org.entirej.framework.core.internal.EJInternalEditableBlock;
import org.entirej.framework.core.service.EJQueryCriteria;

/**
 * Questions can be sent to the user in order to receive input to control the
 * execution flow of an application
 * <p>
 * The question contains all information needed to ask the question and the
 * information regarding the outcome of it
 * <p>
 * Once a question has been answered, the result, i.e. the
 * <code>{@link EJQuestionButton}</code> will be set and the a call to the
 * correct action processor must be made. The action processor can then check
 * the answer to the question and continue application flow accordingly
 */
public class EJQuestion implements Serializable
{
    private String                  _name;
    private EJForm                  _form;
    private String                  _title = "";
    private EJMessage               _message;
    private EJQuestionButton        _answerButton;
    private String                  _buttonOneText;
    private String                  _buttonTwoText;
    private String                  _buttonThreeText;
    private EJRecord                _record;
    private EJQueryCriteria         _queryCriteria;
    private EJInternalEditableBlock _block;
    private EJDataRecord            _dataRecord;

    public EJQuestion(EJForm form, String name)
    {
        _form = form;
        _name = name;
    }

    public EJQuestion(EJForm form, String name, String title, EJMessage message, String buttonOneText)
    {
        this(form, name, title, message, buttonOneText, null, null, null);
    }

    public EJQuestion(EJForm form, String name, String title, EJMessage message, String buttonOneText, EJRecord record)
    {
        this(form, name, title, message, buttonOneText, null, null, record);
    }

    public EJQuestion(EJForm form, String name, String title, EJMessage message, String buttonOneText, String buttonTwoText)
    {
        this(form, name, title, message, buttonOneText, buttonTwoText, null, null);
    }

    public EJQuestion(EJForm form, String name, String title, EJMessage message, String buttonOneText, String buttonTwoText, EJRecord record)
    {
        this(form, name, title, message, buttonOneText, buttonTwoText, null, record);
    }

    public EJQuestion(EJForm form, String name, String title, EJMessage message, String buttonOneText, String buttonTwoText, String buttonThreeText)
    {
        this(form, name, title, message, buttonOneText, buttonTwoText, buttonThreeText, null);
    }

    public EJQuestion(EJForm form, String name, String title, EJMessage message, String buttonOneText, String buttonTwoText, String buttonThreeText,
            EJRecord record)
    {
        _form = form;
        _name = name;
        _message = message;
        _record = record;

        // Use the set methods to the texts get translated
        setButtonText(EJQuestionButton.ONE, buttonOneText);
        setButtonText(EJQuestionButton.TWO, buttonTwoText);
        setButtonText(EJQuestionButton.THREE, buttonThreeText);
        setTitle(title);
    }

    public void setBlock(EJInternalEditableBlock block)
    {
        _block = block;
    }

    public EJInternalEditableBlock getBlock()
    {
        return _block;
    }

    public void setQueryCriteria(EJQueryCriteria queryCriteria)
    {
        _queryCriteria = queryCriteria;
    }

    public EJQueryCriteria getQueryCriteria()
    {
        return _queryCriteria;
    }

    public void setRecord(EJRecord record)
    {
        _record = record;
    }

    public EJRecord getRecord()
    {
        return _record;
    }

    public void setDataRecord(EJDataRecord record)
    {
        _dataRecord = record;
    }

    public EJDataRecord getDataRecord()
    {
        return _dataRecord;
    }
    
    /**
     * Returns the name of this question
     * <p>
     * A question is made in an asynchronous was and therefore application flow
     * is returned to the caller as soon as the message to display a question
     * has been processed. Normally the question will need a user to interact.
     * Once the user has chosen one of the given answers then a call to the
     * forms Action Processor method will be made. The name indicates to the
     * action processor which question was and can then act accordingly
     * 
     * @return The name of this Question
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Returns the form that the
     * <code>{@link EJDefaultFormActionProcessor}</code> will need
     * 
     * @return The form that issued the question
     */
    public EJForm getForm()
    {
        return _form;
    }

    /**
     * Sets the title of the question
     * 
     * @param title
     *            The title of the question
     */
    public void setTitle(String title)
    {
        if (title != null)
        {
            _title = _form.translateText(title);
        }
        else
        {
            _title = title;
        }
    }

    /**
     * Returns the title of this question
     * 
     * @return This questions title
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * Sets this questions message
     * 
     * @param message
     *            This questions message
     */
    public void setMessage(EJMessage message)
    {
        _message = message;
    }

    /**
     * Returns this questions message
     * 
     * @return This questions message
     */
    public String getMessageText()
    {
        return _message.getMessage();
    }

    /**
     * Sets the number of the answer answered by the user
     * 
     * @param answerNumber
     *            The number answered
     */
    public void setAnswer(EJQuestionButton answerButton)
    {
        _answerButton = answerButton;
    }

    /**
     * Return the button number that was set after the user made a choice
     * <p>
     * If there has not been an answer set, then the first answer added to this
     * question will be deemed default and this will be returned. If no answer
     * has been added then <code>-1</code> will be returned
     * 
     * @return The answer number to this question
     */
    public EJQuestionButton getAnswer()
    {
        return _answerButton;
    }

    /**
     * Sets a button text to the given text
     * 
     * {@link #setAnswer(EJQuestionButton)} and retrieved via
     * {@link #getAnswer()}
     * 
     * @param buttonNumber
     *            The button number to add
     * @param buttonText
     *            The buttons text
     */
    public void setButtonText(EJQuestionButton buttonNumber, String buttonText)
    {
        String text = buttonText;

        if (text != null)
        {
            text = _form.translateText(buttonText);
        }

        switch (buttonNumber)
        {
            case ONE:
                _buttonOneText = text;
                break;
            case TWO:
                _buttonTwoText = text;
                break;
            case THREE:
                _buttonThreeText = text;
                break;
        }
    }

    /**
     * Returns the text defined for the given button
     * <p>
     * If there is not text defined for the given number then <code>null</code>
     * will be returned
     * 
     * @param buttonNumber
     *            The number of the required button text
     * @return The text for the given button or <code>null</code> if no button
     *         text is defined for the specified button
     */
    public String getButtonText(EJQuestionButton buttonNumber)
    {
        switch (buttonNumber)
        {
            case ONE:
                return _buttonOneText;
            case TWO:
                return _buttonTwoText;
            case THREE:
                return _buttonThreeText;
        }

        return null;
    }

    /**
     * The action controller that must be called after the question has been
     * answered
     * 
     * @return The action controller to call
     */
    public EJActionController getActionProcessor()
    {
        return _form.getActionController().getUnmanagedController();
    }
}
