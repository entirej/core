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

import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.internal.EJInternalForm;
import org.entirej.framework.core.properties.EJCoreItemProperties;

public class EJItemController implements Comparable<EJItemController> , Serializable
{
    private EJBlockController    _blockController;
    private EJFrameworkManager   _frameworkManager;
    private EJCoreItemProperties _itemProperties;
    
    public EJItemController(EJBlockController blockController, EJCoreItemProperties itemProperties)
    {
        _blockController = blockController;
        _frameworkManager = blockController.getFrameworkManager();
        _itemProperties = itemProperties;
    }
    
    public EJInternalForm getForm()
    {
        return _blockController.getBlock().getForm();
    }
    
    public EJInternalBlock getBlock()
    {
        return _blockController.getBlock();
    }
    
    /**
     * Returns the controller responsible for the form
     * 
     * @return The form controller
     */
    public EJFormController getFormController()
    {
        return _blockController.getFormController();
    }
    
    public EJBlockController getBlockController()
    {
        return _blockController;
    }
    
    public EJFrameworkManager getFrameworkManager()
    {
        return _frameworkManager;
    }
    
    public EJCoreItemProperties getProperties()
    {
        return _itemProperties;
    }
    
   
    
    public int compareTo(EJItemController controller)
    {
        if (controller == null)
        {
            return -1;
        }
        
        return _itemProperties.getName().toUpperCase().compareTo(controller.getProperties().getName().toUpperCase());
        
    }
    
}
