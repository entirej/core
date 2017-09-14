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

import org.entirej.framework.core.enumerations.EJLovDisplayReason;
import org.entirej.framework.core.renderers.interfaces.EJLovRenderer;
import org.entirej.framework.core.renderers.interfaces.EJQueryScreenRenderer;

public class EJLovRendererController extends EJBlockRendererController implements EJLovRenderer , Serializable
{
    private EJLovRenderer _renderer;
    
    public EJLovRendererController(EJLovController controller)
    {
        super(controller);
    }
    
    public void setRenderer(EJLovRenderer renderer)
    {
        _renderer = renderer;
        super.setRenderer(renderer);
    }
    
    @Override
    public void refreshLovRendererProperty(String propertyName)
    {
        if (_renderer != null)
        {
            _renderer.refreshLovRendererProperty(propertyName);
        }
    }
    
    @Override
    public EJLovRenderer getRenderer()
    {
        return _renderer;
    }
    
    @Override
    public void initialiseRenderer(EJLovController lovController)
    {
        if (_renderer != null )
        {
            _renderer.initialiseRenderer(lovController);
        }
    }
    
    @Override
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
    
    @Override
    public void displayLov(EJItemLovController itemLovController, EJLovDisplayReason displayReason)
    {
        if (_renderer != null)
        {
            _renderer.displayLov(itemLovController, displayReason);
        }
    }
    
    @Override
    public EJLovDisplayReason getDisplayReason()
    {
        if (_renderer != null)
        {
            return _renderer.getDisplayReason();
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public void blockCleared()
    {
        if (_renderer != null)
        {
            _renderer.blockCleared();
        }
    }
    
}
