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

import org.entirej.framework.core.EJActionProcessorException;
import org.entirej.framework.core.EJBlock;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJParameterList;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.EJScreenItem;
import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.enumerations.EJLovDisplayReason;
import org.entirej.framework.core.enumerations.EJPopupButton;
import org.entirej.framework.core.enumerations.EJRecordType;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJApplicationManager;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.service.EJQueryCriteria;

public class EJManagedActionController implements EJFormActionProcessor, Serializable
{
    private EJApplicationManager _appManager;
    private EJActionController   _unmanagedController;
    
    public EJManagedActionController(EJFormController formController)
    {
        _appManager = formController.getFrameworkManager().getApplicationManager();
//        _unmanagedController = new EJActionController(formController);
    }
    
    void initialise(EJFormController formController)
    {
        _unmanagedController = new EJActionController(formController);
    }
    
    public EJActionController getUnmanagedController()
    {
        return _unmanagedController;
    }
    
    public void postFormSave(EJForm form) throws EJActionProcessorException
    {
        try
        {
            _unmanagedController.postFormSave(form);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void questionAnswered(EJQuestion question)
    {
        try
        {
            _unmanagedController.questionAnswered(question);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void fileUploaded(EJFileUpload fileUpload) throws EJActionProcessorException
    {
        try
        {
            _unmanagedController.filesUploaded(fileUpload);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
        
    }
    
    public EJMessage getMasterDetailDeleteViolationMessage(EJForm form, String relationName)
    {
        try
        {
            return _unmanagedController.getMasterDetailDeleteViolationMessage(form, relationName);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
            return null;
        }
    }
    
    public void newBlockInstance(EJForm form, String blockName)
    {
        try
        {
            _unmanagedController.newBlockInstance(form, blockName);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void focusGained(EJForm form)
    {
        try
        {
            _unmanagedController.focusGained(form);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void newFormInstance(EJForm form)
    {
        try
        {
            _unmanagedController.newFormInstance(form);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void preFormOpened(EJForm form)
    {
        try
        {
            _unmanagedController.preFormOpened(form);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    @Override
    public void postFormOpened(EJForm form)
    {
        try
        {
            _unmanagedController.postFormOpened(form);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
        
    }
    
    public void preFormClosed(EJForm form)
    {
        try
        {
            _unmanagedController.preFormClosed(form);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void lovActivated(EJForm form, EJScreenItem screenItem, EJLovDisplayReason displayReason)
    {
        try
        {
            _unmanagedController.lovActivated(form, screenItem, displayReason);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void lovCompleted(EJForm form, EJScreenItem screenItem, boolean valueChosen)
    {
        try
        {
            _unmanagedController.lovCompleted(form, screenItem, valueChosen);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void popupCanvasClosing(EJForm form, String popupCanvasName, EJPopupButton button)
    {
        try
        {
            _unmanagedController.popupCanvasClosing(form, popupCanvasName, button);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void popupCanvasClosed(EJForm form, String popupCanvasName, EJPopupButton button)
    {
        try
        {
            _unmanagedController.popupCanvasClosed(form, popupCanvasName, button);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void popupCanvasOpened(EJForm form, String popupCanvasName)
    {
        try
        {
            _unmanagedController.popupCanvasOpened(form, popupCanvasName);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void preOpenPopupCanvas(EJForm form, String popupCanvasName)
    {
        try
        {
            _unmanagedController.preOpenPopupCanvas(form, popupCanvasName);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void preOpenScreen(EJBlock block, EJRecord record, EJScreenType screenType)
    {
        try
        {
            _unmanagedController.preOpenScreen(block, record, screenType);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void preOpenLovQueryScreen(EJInternalBlock block) throws Exception
    {
        try
        {
            _unmanagedController.preOpenLovQueryScreen(block);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void initialiseRecord(EJForm form, EJRecord record, EJRecordType recordType)
    {
        try
        {
            _unmanagedController.initialiseRecord(form, record, recordType);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void preShowStackedPage(EJForm form, String stackedCanvasName, String stackedPageName)
    {
        try
        {
            _unmanagedController.preShowStackedPage(form, stackedCanvasName, stackedPageName);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void modalFormClosing(EJForm form, EJParameterList parameterList)
    {
        try
        {
            _unmanagedController.modalFormClosing(form, parameterList);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void stackedPageChanged(EJForm form, String stackedCanvasName, String stackedPageName)
    {
        try
        {
            _unmanagedController.stackedPageChanged(form, stackedCanvasName, stackedPageName);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void preShowTabPage(EJForm form, String tabCanvasName, String tabPageName)
    {
        try
        {
            _unmanagedController.preShowTabPage(form, tabCanvasName, tabPageName);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void tabPageChanged(EJForm form, String tabCanvasName, String tabPageName)
    {
        try
        {
            _unmanagedController.tabPageChanged(form, tabCanvasName, tabPageName);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void preShowDrawerPage(EJForm form, String drawerCanvasName, String drawerPageName)
    {
        try
        {
            _unmanagedController.preShowDrawerPage(form, drawerCanvasName, drawerPageName);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void drawerPageChanged(EJForm form, String drawerCanvasName, String drawerPageName)
    {
        try
        {
            _unmanagedController.drawerPageChanged(form, drawerCanvasName, drawerPageName);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void popupFormClosed(EJParameterList parameterList)
    {
        try
        {
            _unmanagedController.popupFormClosed(parameterList);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }

    public void embeddedFormClosed(EJParameterList parameterList)
    {
        try
        {
            _unmanagedController.embeddedFormClosed(parameterList);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void executeActionCommand(EJForm form, String blockName, String command, EJScreenType screenType)
    {
        try
        {
            _unmanagedController.executeActionCommand(form, blockName, command, screenType);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void validateItem(EJForm form, String blockName, String itemName, EJScreenType screenType, EJRecord newValues)
    {
        try
        {
            _unmanagedController.validateItem(form, blockName, itemName, screenType, newValues);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void postItemChanged(EJForm form, String blockName, String itemName, EJScreenType screenType)
    {
        try
        {
            _unmanagedController.postItemChanged(form, blockName, itemName, screenType);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void validateQueryCriteria(EJForm form, EJQueryCriteria queryCriteria)
    {
        try
        {
            _unmanagedController.validateQueryCriteria(form, queryCriteria);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void whenInsertCancelled(EJBlock block)
    {
        try
        {
            _unmanagedController.whenInsertCancelled(block);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void whenUpdateCancelled(EJBlock block)
    {
        try
        {
            _unmanagedController.whenUpdateCancelled(block);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void postBlockQuery(EJForm form, EJBlock block)
    {
        
        try
        {
            _unmanagedController.postBlockQuery(form, block);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void postQuery(EJForm form, EJRecord record)
    {
        
        try
        {
            _unmanagedController.postQuery(form, record);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void preQuery(EJForm form, EJQueryCriteria queryCriteria)
    {
        
        try
        {
            _unmanagedController.preQuery(form, queryCriteria);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void preInsert(EJForm form, EJRecord record)
    {
        
        try
        {
            _unmanagedController.preInsert(form, record);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void postInsert(EJForm form, EJRecord record)
    {
        
        try
        {
            _unmanagedController.postInsert(form, record);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void preUpdate(EJForm form, EJRecord record)
    {
        
        try
        {
            _unmanagedController.preUpdate(form, record);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void postUpdate(EJForm form, EJRecord record)
    {
        
        try
        {
            _unmanagedController.postUpdate(form, record);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void preDelete(EJForm form, EJRecord record)
    {
        
        try
        {
            _unmanagedController.preDelete(form, record);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void postDelete(EJForm form, EJRecord record)
    {
        try
        {
            _unmanagedController.postDelete(form, record);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void newRecordInstance(EJForm form, EJRecord record)
    {
        try
        {
            _unmanagedController.newRecordInstance(form, record);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
    
    public void validateRecord(EJForm form, EJRecord record, EJRecordType recordType)
    {
        
        try
        {
            _unmanagedController.validateRecord(form, record, recordType);
        }
        catch (Exception e)
        {
            _appManager.handleException(e);
        }
    }
}
