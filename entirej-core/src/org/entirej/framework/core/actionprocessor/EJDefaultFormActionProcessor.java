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
package org.entirej.framework.core.actionprocessor;

import org.entirej.framework.core.EJActionProcessorException;
import org.entirej.framework.core.EJBlock;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJParameterList;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.EJScreenItem;
import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.data.controllers.EJFileUpload;
import org.entirej.framework.core.data.controllers.EJQuestion;
import org.entirej.framework.core.enumerations.EJLovDisplayReason;
import org.entirej.framework.core.enumerations.EJPopupButton;
import org.entirej.framework.core.enumerations.EJRecordType;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.service.EJQueryCriteria;

public class EJDefaultFormActionProcessor implements EJFormActionProcessor
{
   
    public void postFormSave(EJForm form) throws EJActionProcessorException
    {
        
    }
    
    public void lovActivated(EJForm form, EJScreenItem screenItem, EJLovDisplayReason displayReason) throws EJActionProcessorException
    {
    }
    
    public void lovCompleted(EJForm form, EJScreenItem screenItem, boolean valueChosen) throws EJActionProcessorException
    {
    }
    
    public void questionAnswered(EJQuestion question) throws EJActionProcessorException
    {
    }
    
    
    @Override
    public void filesUploaded(EJFileUpload fileUpload) throws EJActionProcessorException
    {        
    }
    
    public EJMessage getMasterDetailDeleteViolationMessage(EJForm form, String relationName) throws EJActionProcessorException
    {
        return null;
    }
    
    public void newBlockInstance(EJForm form, String blockName) throws EJActionProcessorException
    {
    }
    
    public void focusGained(EJForm form) throws EJActionProcessorException
    {
        
    }
    
    public void preFormOpened(EJForm form) throws EJActionProcessorException
    {
    }
    
    public void preFormClosed(EJForm form) throws EJActionProcessorException
    {
    }
    
    public void newFormInstance(EJForm form) throws EJActionProcessorException
    {
    }
 
    public void popupCanvasClosing(EJForm form, String popupCanvasName, EJPopupButton button) throws EJActionProcessorException
    {
    }
    
    public void popupCanvasClosed(EJForm form, String popupCanvasName, EJPopupButton button) throws EJActionProcessorException
    {
    }
    
    public void popupCanvasOpened(EJForm form, String popupCanvasName) throws EJActionProcessorException
    {
    }
    
    public void preOpenPopupCanvas(EJForm form, String popupCanvasName) throws EJActionProcessorException
    {
    }
    
    public void stackedPageChanged(EJForm form, String stackedCanvasName, String stackedPageName) throws EJActionProcessorException
    {
    }
    
    public void tabPageChanged(EJForm form, String tabCanvasName, String tabPageName) throws EJActionProcessorException
    {
    }

    public void drawerPageChanged(EJForm form, String drawerCanvasName, String drawerPageName) throws EJActionProcessorException
    {
    }

    public void executeActionCommand(EJForm form, String blockName, String command, EJScreenType screenType) throws EJActionProcessorException
    {
    }
    
    public void validateItem(EJForm form, String blockName, String itemName, EJScreenType screenType, EJRecord newValues) throws EJActionProcessorException
    {
    }
    
    public void postItemChanged(EJForm form, String blockName, String itemName, EJScreenType screenType) throws EJActionProcessorException
    {
    }
    
    public void embeddedFormClosed(EJParameterList parameterList) throws EJActionProcessorException
    {
    }
    
    public void popupFormClosed(EJParameterList parameterList) throws EJActionProcessorException
    {
    }
    
    public void postQuery(EJForm form, EJRecord record) throws EJActionProcessorException
    {
    }
    
    public void postBlockQuery(EJForm form, EJBlock block) throws EJActionProcessorException
    {
    }
    
    public void newRecordInstance(EJForm form, EJRecord record) throws EJActionProcessorException
    {
    }
    
    public void validateRecord(EJForm form, EJRecord record, EJRecordType recordType) throws EJActionProcessorException
    {
    }
    
    public void validateQueryCriteria(EJForm form, EJQueryCriteria queryCriteria) throws EJActionProcessorException
    {
    }
    
    public void postDelete(EJForm form, EJRecord record) throws EJActionProcessorException
    {
    }
    
    public void postInsert(EJForm form, EJRecord record) throws EJActionProcessorException
    {
    }
    
    public void postUpdate(EJForm form, EJRecord record) throws EJActionProcessorException
    {
    }
    
    public void preDelete(EJForm form, EJRecord record) throws EJActionProcessorException
    {
    }
    
    public void preInsert(EJForm form, EJRecord record) throws EJActionProcessorException
    {
    }
    
    public void preUpdate(EJForm form, EJRecord record) throws EJActionProcessorException
    {
    }
    
    public void preQuery(EJForm form, EJQueryCriteria queryCriteria) throws EJActionProcessorException
    {
    }
    
    public void modalFormClosing(EJForm form, EJParameterList parameterList) throws EJActionProcessorException
    {
        
    }
    
    public void preShowStackedPage(EJForm form, String stackedCanvasName, String stackedPageName) throws EJActionProcessorException
    {
    }
    
    public void preShowTabPage(EJForm form, String tabCanvasName, String tabPageName) throws EJActionProcessorException
    {
    }
    
    public void preShowDrawerPage(EJForm form, String drawerCanvasName, String drawerPageName) throws EJActionProcessorException
    {
    }
    
    public void initialiseRecord(EJForm form, EJRecord record, EJRecordType recordType) throws EJActionProcessorException
    {
    }
    
    public void preOpenScreen(EJBlock block, EJRecord record, EJScreenType screenType) throws EJActionProcessorException
    {
    }
    
    public void whenUpdateCancelled(EJBlock block) throws EJActionProcessorException
    {
    }
    
    public void whenInsertCancelled(EJBlock block) throws EJActionProcessorException
    {
    }
    
}
