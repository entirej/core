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

import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.data.controllers.EJFileUpload;
import org.entirej.framework.core.data.controllers.EJInternalQuestion;
import org.entirej.framework.core.data.controllers.EJQuestion;

public interface EJMessenger extends Serializable
{
    
    /**
     * The Messenger will handle the message given according to its severity
     * <p>
     * How the message is actually handled depends upon the Messengers
     * implementation
     * 
     * @param message
     *            The message to handle
     */
    public void handleMessage(EJMessage message);
    
    /**
     * The Messenger will handle the exception accordingly
     * <p>
     * The framework logger will be called to handle the exception. If the
     * application has added a logging listener then the listener will be
     * informed of the error
     * 
     * @param exception
     *            The exception to handle
     */
    public void handleException(Exception exception);
    
    /**
     * The Messenger will handle the exception accordingly
     * <p>
     * The framework logger will be called to handle the exception. If the
     * application has added a logging listener then the listener will be
     * informed of the error
     * <p>
     * If the showUserMessage flag has been set to true, then the framework
     * logger will then inform the framework messenger to display the message
     * contained within the exception
     * 
     * @param exception
     *            The exception to handle
     * @param showUserMessage
     *            If set to <code>true</code> the exception message will be
     *            displayed
     */
    public void handleException(Exception exception, boolean showUserMessage);
    
    /**
     * Indicate to the message listener that a question must be asked
     * <p>
     * The <code>Question</code> holds all the information needed to ask the
     * question, the answer to the question and the
     * <code>{@link IActionProcessor}</code> that must be called after the
     * question has been answered
     * <p>
     * If the <code>IActionProcessor</code> is never called after the question
     * is answered then there is no way of knowing if the question was answered
     * and the program may not function as it should
     * 
     * @param question
     *            Contains all information needed for the question
     * @see {@link IActionProcessor#questionAnswered(IFormManager, EJQuestion)}
     */
    public void askQuestion(EJQuestion question);
    
    /**
     * Indicate to the message listener that a file upload must be asked
     * <p>
     * The <code>EJFileUpload</code> holds all the information needed to ask the
     * file upload request, the selected files and the
     * <code>{@link IActionProcessor}</code> that must be called after the
     * files has been uploaded
     * <p>
     * If the <code>IActionProcessor</code> is never called after the file upload
     *  then there is no way of knowing if the file uploaded 
     * and the program may not function as it should
     * 
     * @param fileUpload
     *            Contains all information needed for the file upload
     * @see {@link IActionProcessor#filesUploaded(IFormManager, EJFileUpload)}
     */
    public void uploadFile(EJFileUpload fileUpload);
    
    /**
     * Indicates to the messenger that the <b>EntireJ Framework</b> needs to ask
     * the user a question
     * <p>
     * Questions asked by <b>EntireJ</b> must be returned to the
     * {@link IForm} which can be retrieved by
     * {@link EJQuestion#getForm()}
     * <p>
     * If the application wants to change the text of the message, then it can
     * use its own text instead of the default text assigned by <b>EntireJ</b>.
     * The name of each question asked by <b>EntireJ</b> along with its default
     * text, can be found in the <b>EntireJ</b> documentation
     * 
     * @param question
     *            The question <b>EntireJ</b> needs to ask
     */
    public void askInternalQuestion(EJInternalQuestion question);
    
}
