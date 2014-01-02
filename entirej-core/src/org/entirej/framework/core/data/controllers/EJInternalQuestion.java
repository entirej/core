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
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;

import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.enumerations.EJAskToSaveChangesOperation;

/**
 * <b>FOR INTERNAL USE ONLY, USE {@link EJQuestion} INSTEAD</b>
 * <p>
 * Questions can be sent to the user in order to receive input to control the
 * execution flow of an application
 * <p>
 * The question contains all information needed to ask the question and the
 * information regarding the outcome of it
 * <p>
 * Once a question has been answered, the result, i.e. the answer number will be
 * set and the a call to the forms
 * <code>{@link EJFormActionProcessor#questionAnswered(EJQuestion)}</code> must
 * be made. The action processor can then check the answer to the question and
 * continue application flow accordingly
 */
public class EJInternalQuestion extends EJQuestion implements Serializable
{
    private EJAskToSaveChangesOperation _operation;
    
    EJInternalQuestion(EJForm form, String name)
    {
        super(form, name);
    }
    
    void setOperation(EJAskToSaveChangesOperation operation)
    {
        _operation = operation;
    }
    
    EJAskToSaveChangesOperation getOperation()
    {
        return _operation;
    }
}
