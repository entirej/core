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
import java.util.ArrayList;

import org.entirej.framework.core.data.EJDataRecord;

public class EJMirrorBlockSynchronizer implements Serializable
{
    
    private EJEditableBlockController            _mirrorParent;
    private boolean                              _executingBlockCleared       = false;
    private boolean                              _executingRecordSelected     = false;
    private boolean                              _executingRecordInserted     = false;
    private boolean                              _executingRecordDeleted      = false;
    private boolean                              _executingRecordUpdated      = false;
    private boolean                              _executingQueryExecuted      = false;
    private boolean                              _executingSynchCurrentRecord = false;
    
    private ArrayList<EJEditableBlockController> _mirroredControllers;
    
    public EJMirrorBlockSynchronizer()
    {
        _mirroredControllers = new ArrayList<EJEditableBlockController>();
    }
    
    public void addMirroredBlockController(EJEditableBlockController controller)
    {
        _mirroredControllers.add(controller);
        
        if (controller.getProperties().isMirrorParent())
        {
            _mirrorParent = controller;
        }
    }
    
    public EJEditableBlockController getMirrorParent()
    {
        return _mirrorParent;
    }
    
    public void blockCleared(EJBlockController initialisingController)
    {
        if (!_executingBlockCleared)
        {
            try
            {
                _executingBlockCleared = true;
                
                for (EJEditableBlockController controller : _mirroredControllers)
                {
                    if (controller != initialisingController)
                    {
                        controller.getRendererController().blockCleared();
                    }
                }
            }
            finally
            {
                _executingBlockCleared = false;
            }
        }
    }
    
    public void newRecordSelected(EJBlockController initialisingController, EJDataRecord recordSelected)
    {
        if (!_executingRecordSelected)
        {
            try
            {
                _executingRecordSelected = true;
                for (EJEditableBlockController controller : _mirroredControllers)
                {
                    if (controller != initialisingController)
                    {
                        controller.getRendererController().recordSelected(recordSelected);
                    }
                }
            }
            finally
            {
                _executingRecordSelected = false;
            }
        }
    }
    
    
    public void recordInserted(EJBlockController initialisingController, EJDataRecord record)
    {
        if (!_executingRecordInserted)
        {
            try
            {
                _executingRecordInserted = true;
                for (EJEditableBlockController controller : _mirroredControllers)
                {
                    if (controller != initialisingController)
                    {
                        controller.getManagedRendererController().recordInserted(record);
                    }
                }
            }
            finally
            {
                _executingRecordInserted = false;
            }
        }
    }
    
    public void recordDeleted(EJBlockController initialisingController, int dataBlockRecordNumber)
    {
        if (!_executingRecordDeleted)
        {
            try
            {
                _executingRecordDeleted = true;
                
                for (EJEditableBlockController controller : _mirroredControllers)
                {
                    if (controller != initialisingController)
                    {
                        controller.getManagedRendererController().recordDeleted(dataBlockRecordNumber);
                    }
                }
            }
            finally
            {
                _executingRecordDeleted = false;
            }
        }
    }
    
    public void refreshAfterChange(EJBlockController initialisingController, EJDataRecord record)
    {
        if (!_executingRecordUpdated)
        {
            try
            {
                _executingRecordUpdated = true;
                for (EJEditableBlockController controller : _mirroredControllers)
                {
                    if (controller != initialisingController)
                    {
                        controller.getManagedRendererController().refreshAfterChange(record);
                    }
                }
            }
            finally
            {
                _executingRecordUpdated = false;
            }
        }
    }
    
    public void queryExecuted(EJBlockController initialisingController)
    {
        if (!_executingQueryExecuted)
        {
            try
            {
                _executingQueryExecuted = true;
                for (EJEditableBlockController controller : _mirroredControllers)
                {
                    if (controller != initialisingController)
                    {
                        controller.getManagedRendererController().queryExecuted();
                    }
                }
            }
            finally
            {
                _executingQueryExecuted = false;
            }
        }
    }
    
    public void synchronize(EJBlockController initialisingController)
    {
        if (!_executingSynchCurrentRecord)
        {
            try
            {
                _executingSynchCurrentRecord = true;
                for (EJEditableBlockController controller : _mirroredControllers)
                {
                    if (controller != initialisingController)
                    {
                        controller.getManagedRendererController().synchronize();
                    }
                }
            }
            finally
            {
                _executingSynchCurrentRecord = false;
            }
        }
    }
    
}
