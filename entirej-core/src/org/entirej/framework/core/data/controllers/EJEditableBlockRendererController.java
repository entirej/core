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

import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.enumerations.EJManagedBlockProperty;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.renderers.interfaces.EJEditableBlockRenderer;
import org.entirej.framework.core.renderers.interfaces.EJInsertScreenRenderer;
import org.entirej.framework.core.renderers.interfaces.EJQueryScreenRenderer;
import org.entirej.framework.core.renderers.interfaces.EJUpdateScreenRenderer;

public class EJEditableBlockRendererController extends EJBlockRendererController implements Serializable
{
    private EJEditableBlockRenderer _renderer;
    
    public EJEditableBlockRendererController(EJBlockController controller)
    {
        super(controller);
    }
    
    public void setRenderer(EJEditableBlockRenderer renderer)
    {
        _renderer = renderer;
        super.setRenderer(renderer);
    }
    
    public void refreshBlockProperty(EJManagedBlockProperty managedBlockPropertyType)
    {
        if (_renderer != null)
        {
            _renderer.refreshBlockProperty(managedBlockPropertyType);
        }
    }
    
    public void refreshBlockRendererProperty(String propertyName)
    {
        if (_renderer != null)
        {
            _renderer.refreshBlockRendererProperty(propertyName);
        }
    }
    
    public void initialiseRenderer(EJEditableBlockController block)
    {
        if (_renderer != null)
        {
            _renderer.initialiseRenderer(block);
        }
    }
    
    public boolean isCurrentRecordDirty()
    {
        if (_renderer != null)
        {
            return _renderer.isCurrentRecordDirty();
        }
        return false;
    }
    
    public void blockCleared()
    {
        if (_renderer != null)
        {
            _renderer.blockCleared();
        }
    }
    
    public void detailBlocksCleared()
    {
        if (_renderer != null)
        {
            _renderer.detailBlocksCleared();
        }
    }
    
    public void recordInserted(EJDataRecord record)
    {
        if (_renderer != null)
        {
            _renderer.recordInserted(record);
        }
    }
    
    public void recordDeleted(int dataBlockRecordNumber)
    {
        if (_renderer != null)
        {
            _renderer.recordDeleted(dataBlockRecordNumber);
        }
    }
    
    public void setFilter(String filter)
    {
        if (_renderer != null)
        {
            _renderer.setFilter(filter);
        }
    }
    
    public String getFilter()
    {
        if (_renderer != null)
        {
            return _renderer.getFilter();
        }
        else
        {
            return null;
        }
    }
    
    public boolean hasFocus()
    {
        if (_renderer != null)
        {
            _renderer.hasFocus();
        }
        return false;
    }
    
    public void setHasFocus(boolean focus)
    {
        if (_renderer != null)
        {
            _renderer.setHasFocus(focus);
        }
    }
    
    public void gainFocus()
    {
        if (_renderer != null)
        {
            _renderer.gainFocus();
        }
    }
    
    public void setFocusToItem(EJScreenItemController item)
    {
        if (_renderer != null)
        {
            _renderer.setFocusToItem(item);
        }
    }
    
    public void enterInsert(EJDataRecord recordToInsert)
    {
        if (_renderer != null)
        {
            _renderer.enterInsert(recordToInsert);
        }
    }
    
    public void enterUpdate(EJDataRecord recordToUpdate)
    {
        if (_renderer != null)
        {
            _renderer.enterUpdate(recordToUpdate);
        }
    }
    
    public void askToDeleteRecord(EJDataRecord recordToDelete, String message)
    {
        if (_renderer != null)
        {
            _renderer.askToDeleteRecord(recordToDelete, message);
        }
    }
    
    public EJQueryScreenRenderer getQueryScreenRenderer()
    {
        if (_renderer != null)
        {
            return _renderer.getQueryScreenRenderer();
        }
        else
        {
            return null;
        }
    }
    
    public EJInsertScreenRenderer getInsertScreenRenderer()
    {
        if (_renderer != null)
        {
            return _renderer.getInsertScreenRenderer();
        }
        else
        {
            return null;
        }
    }
    
    public EJUpdateScreenRenderer getUpdateScreenRenderer()
    {
        if (_renderer != null)
        {
            return _renderer.getUpdateScreenRenderer();
        }
        else
        {
            return null;
        }
    }
    
}
