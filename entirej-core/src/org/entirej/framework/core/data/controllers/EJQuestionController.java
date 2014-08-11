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

import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.enumerations.EJAskToSaveChangesOperation;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.enumerations.EJQuestionButton;
import org.entirej.framework.core.internal.EJInternalEditableBlock;
import org.entirej.framework.core.service.EJQueryCriteria;

public class EJQuestionController
{
    private final String ASK_TO_SAVE_QUESTIONS = "$ASK_TO_SAVE_QUESTIONS$";
    
    private final String YES                   = "Yes";
    private final String NO                    = "No";
    private final String CANCEL                = "Cancel";
    
    public EJQuestionController()
    {
        
    }
    
    public EJInternalQuestion makeAskToSaveChangesQuestion(EJFormController form, EJAskToSaveChangesOperation operation)
    {
        EJInternalQuestion question = makeQuestion(form, operation);
        return question;
    }
    
    public EJInternalQuestion makeAskToSaveChangesQuestion(EJInternalEditableBlock block, EJAskToSaveChangesOperation operation)
    {
        EJInternalQuestion question = makeQuestion(block, operation);
        return question;
    }
    
    public EJInternalQuestion makeAskToSaveChangesQuestion(EJInternalEditableBlock block, EJAskToSaveChangesOperation operation, EJQueryCriteria queryCriteria)
    {
        EJInternalQuestion question = makeQuestion(block, operation);
        question.setQueryCriteria(queryCriteria);
        return question;
    }
    
    public EJInternalQuestion makeAskToSaveChangesQuestion(EJInternalEditableBlock block, EJAskToSaveChangesOperation operation, EJDataRecord record)
    {
        EJInternalQuestion question = makeQuestion(block, operation);
        question.setDataRecord(record);
        return question;
    }
    
    private EJInternalQuestion makeQuestion(EJInternalEditableBlock block, EJAskToSaveChangesOperation operation)
    {
        EJInternalQuestion q = new EJInternalQuestion(new EJForm(block.getForm()), ASK_TO_SAVE_QUESTIONS);
        q.setTitle("Question");
        q.setMessage(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.ASK_TO_SAVE_CHANGES));
        q.setButtonText(EJQuestionButton.ONE, YES);
        q.setButtonText(EJQuestionButton.TWO, NO);
        
        if (operation != EJAskToSaveChangesOperation.QUESTION_ACTION_NEW_RECORD_INSTANCE)
        {
            q.setButtonText(EJQuestionButton.THREE, CANCEL);
        }
        
        q.setBlock(block);
        q.setOperation(operation);
        
        return q;
    }
    
    private EJInternalQuestion makeQuestion(EJFormController form, EJAskToSaveChangesOperation operation)
    {
        EJInternalQuestion q = new EJInternalQuestion(new EJForm(form.getInternalForm()), ASK_TO_SAVE_QUESTIONS);
        q.setTitle("Question");
        q.setMessage(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.ASK_TO_SAVE_CHANGES));
        q.setButtonText(EJQuestionButton.ONE, YES);
        q.setButtonText(EJQuestionButton.TWO, NO);
        
        if (operation != EJAskToSaveChangesOperation.QUESTION_ACTION_NEW_RECORD_INSTANCE)
        {
            q.setButtonText(EJQuestionButton.THREE, CANCEL);
        }
        
        q.setOperation(operation);
        
        return q;
    }
    
    public void handleAnsweredQuestion(EJInternalQuestion question)
    {
        if (question.getName().equals(ASK_TO_SAVE_QUESTIONS))
        {
            handleAskToSaveChanges(question);
        }
    }
    
    private void handleAskToSaveChanges(EJInternalQuestion question)
    {
        if (question.getAnswer() == EJQuestionButton.ONE)
        {
            switch (question.getOperation())
            {
                case QUESTION_ACTION_QUERY_ASK:
                    question.getBlock().getForm().saveChanges();
                    question.getBlock().enterQuery();
                    break;
                case QUESTION_ACTION_QUERY_PERFORM:
                    question.getBlock().getForm().saveChanges();
                    question.getBlock().executeQuery(question.getQueryCriteria());
                    break;
                case QUESTION_ACTION_NEXT_RECORD:
                    question.getBlock().getForm().saveChanges();
                    question.getBlock().nextRecord();
                    break;
                case QUESTION_ACTION_PREVIOUS_RECORD:
                    question.getBlock().getForm().saveChanges();
                    question.getBlock().previousRecord();
                    break;
                case QUESTION_ACTION_NEW_RECORD_INSTANCE:
                    question.getBlock().getForm().saveChanges();
                    question.getBlock().newRecordInstance(question.getDataRecord());
                    break;
                case QUESTION_ACTION_CLEAR_BLOCK:
                    question.getBlock().getForm().saveChanges();
                    question.getBlock().clear(true);
                    break;
                case QUESTION_ACTION_INSERT_RECORD:
                    question.getBlock().getForm().saveChanges();
                    question.getBlock().insertRecord(question.getDataRecord());
                    break;
                case QUESTION_ACTION_CLOSE_FORM:
                    question.getForm().saveChanges();
                    question.getForm().close();
            }
        }
        else if (question.getAnswer() == EJQuestionButton.TWO)
        {
            switch (question.getOperation())
            {
                case QUESTION_ACTION_QUERY_ASK:
                    question.getBlock().clearAllDetailRelations(true);
                    question.getBlock().clear(true);
                    question.getBlock().enterQuery();
                    break;
                case QUESTION_ACTION_QUERY_PERFORM:
                    question.getBlock().clearAllDetailRelations(true);
                    question.getBlock().clear(true);
                    question.getBlock().executeQuery(question.getQueryCriteria());
                    break;
                case QUESTION_ACTION_NEXT_RECORD:
                    question.getBlock().clearAllDetailRelations(true);
                    question.getBlock().nextRecord();
                    break;
                case QUESTION_ACTION_PREVIOUS_RECORD:
                    question.getBlock().clearAllDetailRelations(true);
                    question.getBlock().previousRecord();
                    break;
                case QUESTION_ACTION_NEW_RECORD_INSTANCE:
                    question.getBlock().clearAllDetailRelations(true);
                    question.getBlock().newRecordInstance(question.getDataRecord());
                    break;
                case QUESTION_ACTION_CLEAR_BLOCK:
                    question.getBlock().clear(true);
                    break;
                case QUESTION_ACTION_INSERT_RECORD:
                    question.getBlock().clearAllDetailRelations(true);
                    question.getBlock().insertRecord(question.getDataRecord());
                    break;
                case QUESTION_ACTION_CLOSE_FORM:
                    question.getForm().clear(true);
                    question.getForm().close();
                    break;
            }
        }
        else
        {
            switch (question.getOperation())
            {
                case QUESTION_ACTION_QUERY_ASK:
                    break;
                case QUESTION_ACTION_QUERY_PERFORM:
                    question.getBlock().enterQuery();
                    break;
                case QUESTION_ACTION_NEXT_RECORD:
                    break;
                case QUESTION_ACTION_PREVIOUS_RECORD:
                    break;
                case QUESTION_ACTION_CLEAR_BLOCK:
                    break;
                case QUESTION_ACTION_INSERT_RECORD:
                    break;
                case QUESTION_ACTION_CLOSE_FORM:
                    break;
                case QUESTION_ACTION_NEW_RECORD_INSTANCE:
                    break;
            }
        }
        
    }
}
