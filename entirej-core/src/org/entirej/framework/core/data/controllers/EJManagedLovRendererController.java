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
import org.entirej.framework.core.enumerations.EJLovDisplayReason;
import org.entirej.framework.core.enumerations.EJManagedScreenProperty;
import org.entirej.framework.core.renderers.EJManagedQueryScreenRendererWrapper;
import org.entirej.framework.core.renderers.interfaces.EJBlockRenderer;
import org.entirej.framework.core.renderers.interfaces.EJLovRenderer;
import org.entirej.framework.core.renderers.interfaces.EJQueryScreenRenderer;

public class EJManagedLovRendererController implements EJBlockRenderer , Serializable
{
    private EJLovRendererController             _rendererController;
    private EJFrameworkManager                  _frameworkManager;
    private EJManagedQueryScreenRendererWrapper _queryScreenRendererWrapper;
    
    public EJManagedLovRendererController(EJLovController lovController)
    {
        _frameworkManager = lovController.getFrameworkManager();
        _rendererController = new EJLovRendererController(lovController);
        
        
        
    }
    
    public void setRenderer(EJLovRenderer renderer)
    {
        _rendererController.setRenderer(renderer);
        EJQueryScreenRenderer queryScreenRenderer;
        try
        {
            queryScreenRenderer = renderer!=null ? renderer.getQueryScreenRenderer() : null;
            if (queryScreenRenderer != null)
            {
                _queryScreenRendererWrapper = new EJManagedQueryScreenRendererWrapper(_frameworkManager, queryScreenRenderer);
            }
            else
            {
                _queryScreenRendererWrapper = null;
            }
        }
        catch (Exception e)
        {
            _frameworkManager.handleException(e);
        }
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
    
    public EJLovRendererController getUnmanagedController()
    {
        return _rendererController;
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
    
    public void refreshLovRendererProperty(String propertyName)
    {
        try
        {
            _rendererController.refreshLovRendererProperty(propertyName);
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
    
    public void displayLov(EJItemLovController lovController, EJLovDisplayReason displayReason)
    {
        try
        {
            _rendererController.displayLov(lovController, displayReason);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public EJLovDisplayReason getDisplayReason()
    {
        try
        {
            return _rendererController.getDisplayReason();
        }
        catch (Exception e)
        {
            handleException(e);
            return EJLovDisplayReason.LOV;
        }
    }
    
    public EJManagedQueryScreenRendererWrapper getQueryScreenRenderer()
    {
        return _queryScreenRendererWrapper;
    }
    
    public void initialiseRenderer(EJLovController lovController)
    {
        try
        {
            _rendererController.initialiseRenderer(lovController);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void enterQuery(EJDataRecord record)
    {
        try
        {
            _rendererController.enterQuery(record);
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
    
    private void handleException(Exception e)
    {
        if (e instanceof EJApplicationException)
        {
            _frameworkManager.handleException((EJApplicationException) e);
        }
        else
        {
            e.printStackTrace();
            _frameworkManager.handleException(new EJApplicationException(e));
        }
    }
}
