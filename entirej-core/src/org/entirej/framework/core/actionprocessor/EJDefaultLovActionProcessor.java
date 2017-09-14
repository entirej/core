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
package org.entirej.framework.core.actionprocessor;

import org.entirej.framework.core.EJActionProcessorException;
import org.entirej.framework.core.EJLov;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.EJScreenItem;
import org.entirej.framework.core.actionprocessor.interfaces.EJLovActionProcessor;
import org.entirej.framework.core.data.controllers.EJQuestion;
import org.entirej.framework.core.enumerations.EJLovDisplayReason;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.service.EJQueryCriteria;

public class EJDefaultLovActionProcessor implements EJLovActionProcessor
{
    
    @Override
    public void lovActivated(EJLov lov, EJScreenItem screenItem, EJLovDisplayReason displayReason) throws EJActionProcessorException
    {
    }
    
    @Override
    public void lovCompleted(EJLov lov, EJScreenItem screenItem, boolean valueChosen) throws EJActionProcessorException
    {
    }
    
    @Override
    public void questionAnswered(EJQuestion question) throws EJActionProcessorException
    {
    }
    
    @Override
    public void preQuery(EJLov lov, EJQueryCriteria queryCriteria) throws EJActionProcessorException
    {
    }
    
    @Override
    public void postQuery(EJLov lov, EJRecord record) throws EJActionProcessorException
    {
    }
    
    @Override
    public void validateQueryCriteria(EJLov lov, EJQueryCriteria queryCriteria) throws EJActionProcessorException
    {
    }
    
    @Override
    public void validateItem(EJLov lov, String blockName, String itemName, EJScreenType screenType) throws EJActionProcessorException
    {
    }
    
    @Override
    public void executeActionCommand(EJLov lov, String blockName, String command, EJScreenType screenType) throws EJActionProcessorException
    {
    }
    
    @Override
    public void whenCreateRecord(EJLov lov, EJRecord record) throws EJActionProcessorException
    {
    }
    
    @Override
    public void newRecordInstance(EJLov lov, EJRecord record) throws EJActionProcessorException
    {
    }
    
    @Override
    public void preOpenQueryScreen(EJLov lov) throws EJActionProcessorException
    {
    }
    
    @Override
    public void initialiseQueryRecord(EJLov lov, EJRecord record) throws EJActionProcessorException
    {
    }
}
