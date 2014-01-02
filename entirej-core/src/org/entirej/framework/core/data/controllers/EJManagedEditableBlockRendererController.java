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

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.enumerations.EJManagedBlockProperty;
import org.entirej.framework.core.enumerations.EJManagedScreenProperty;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.renderers.interfaces.EJEditableBlockRenderer;
import org.entirej.framework.core.renderers.interfaces.EJInsertScreenRenderer;
import org.entirej.framework.core.renderers.interfaces.EJQueryScreenRenderer;
import org.entirej.framework.core.renderers.interfaces.EJUpdateScreenRenderer;

public class EJManagedEditableBlockRendererController implements Serializable
{
    private EJEditableBlockRendererController _rendererController;
    private EJFrameworkManager                _frameworkManager;
    
    public EJManagedEditableBlockRendererController(EJEditableBlockController blockController)
    {
        _frameworkManager = blockController.getFrameworkManager();
        _rendererController = new EJEditableBlockRendererController(blockController);
    }
    
    public void setRenderer(EJEditableBlockRenderer renderer)
    {
        _rendererController.setRenderer(renderer);
    }
    
    public EJEditableBlockRendererController getUnmanagedController()
    {
        return _rendererController;
    }
    
    public void getRenderer()
    {
        try
        {
            _rendererController.getRenderer();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void blockCleared()
    {
        try
        {
            _rendererController.blockCleared();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void detailBlocksCleared()
    {
        try
        {
            _rendererController.detailBlocksCleared();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void gainFocus()
    {
        try
        {
            _rendererController.gainFocus();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public boolean hasFocus()
    {
        try
        {
            return _rendererController.hasFocus();
        }
        catch (Exception e)
        {
            handleException(e);
            return false;
        }
    }
    
    public void initialiseRenderer(EJEditableBlockController block)
    {
        try
        {
            _rendererController.initialiseRenderer(block);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public boolean isCurrentRecordDirty()
    {
        try
        {
            return _rendererController.isCurrentRecordDirty();
        }
        catch (Exception e)
        {
            handleException(e);
            return false;
        }
    }
    
    public void askToDeleteRecord(EJDataRecord recordToDelete, String message)
    {
        try
        {
            _rendererController.askToDeleteRecord(recordToDelete, message);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void enterInsert(EJDataRecord recordToInsert)
    {
        try
        {
            _rendererController.enterInsert(recordToInsert);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void enterQuery(EJDataRecord queryRecord)
    {
        try
        {
            _rendererController.enterQuery(queryRecord);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void enterUpdate(EJDataRecord recordToUpdate)
    {
        try
        {
            _rendererController.enterUpdate(recordToUpdate);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void executingQuery()
    {
        try
        {
            _rendererController.executingQuery();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void queryExecuted()
    {
        try
        {
            _rendererController.queryExecuted();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void recordDeleted(int dataBlockRecordNumber)
    {
        try
        {
            _rendererController.recordDeleted(dataBlockRecordNumber);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void recordInserted(EJDataRecord record)
    {
        try
        {
            _rendererController.recordInserted(record);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void recordSelected(EJDataRecord record)
    {
        try
        {
            _rendererController.recordSelected(record);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void refreshAfterChange(EJDataRecord record)
    {
        try
        {
            _rendererController.refreshAfterChange(record);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void refreshBlockProperty(EJManagedBlockProperty managedBlockPropertyType)
    {
        try
        {
            _rendererController.refreshBlockProperty(managedBlockPropertyType);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void refreshBlockRendererProperty(String propertyName)
    {
        try
        {
            _rendererController.refreshBlockRendererProperty(propertyName);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void refreshItemProperty(String itemName, EJManagedScreenProperty managedItemPropertyType, EJDataRecord record)
    {
        try
        {
            _rendererController.refreshItemProperty(itemName, managedItemPropertyType, record);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void refreshItemRendererProperty(String itemName, String propertyName)
    {
        try
        {
            _rendererController.refreshItemRendererProperty(itemName, propertyName);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void setFocusToItem(EJScreenItemController item)
    {
        try
        {
            _rendererController.setFocusToItem(item);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void setHasFocus(boolean focus)
    {
        try
        {
            _rendererController.setHasFocus(focus);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void synchronize()
    {
        try
        {
            _rendererController.synchronize();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public Object getGuiComponent()
    {
        try
        {
            return _rendererController.getGuiComponent();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public EJDataRecord getFocusedRecord()
    {
        try
        {
            return _rendererController.getFocusedRecord();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public int getDisplayedRecordNumber(EJDataRecord record)
    {
        try
        {
            return _rendererController.getDisplayedRecordNumber(record);
        }
        catch (Exception e)
        {
            handleException(e);
            return -1;
        }
    }
    
    public int getBlockRecordCount()
    {
        try
        {
            return _rendererController.getBlockRecordCount();
        }
        catch (Exception e)
        {
            handleException(e);
            return 0;
        }
    }
    
    public int getDisplayedRecordCount()
    {
        try
        {
            return _rendererController.getDisplayedRecordCount();
        }
        catch (Exception e)
        {
            handleException(e);
            return 0;
        }
    }
    
    public EJDataRecord getFirstRecord()
    {
        try
        {
            return _rendererController.getFirstRecord();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public EJDataRecord getLastRecord()
    {
        try
        {
            return _rendererController.getLastRecord();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public EJDataRecord getRecordAt(int displayedRecordNumber)
    {
        try
        {
            return _rendererController.getRecordAt(displayedRecordNumber);
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public EJDataRecord getRecordAfter(EJDataRecord record)
    {
        try
        {
            return _rendererController.getRecordAfter(record);
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public EJDataRecord getRecordBefore(EJDataRecord record)
    {
        try
        {
            return _rendererController.getRecordBefore(record);
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public EJQueryScreenRenderer getQueryScreenRenderer()
    {
        try
        {
            return _rendererController.getQueryScreenRenderer();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public EJInsertScreenRenderer getInsertScreenRenderer()
    {
        try
        {
            return _rendererController.getInsertScreenRenderer();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public EJUpdateScreenRenderer getUpdateScreenRenderer()
    {
        try
        {
            return _rendererController.getUpdateScreenRenderer();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    private void handleException(Exception e)
    {
        if (e instanceof EJApplicationException)
        {
            _frameworkManager.handleException(e);
        }
        else
        {
            _frameworkManager.handleException(new EJApplicationException(e));
        }
    }
    
}
