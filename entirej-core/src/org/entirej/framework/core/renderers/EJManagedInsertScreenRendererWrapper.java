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
package org.entirej.framework.core.renderers;

import java.io.Serializable;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJEditableBlockController;
import org.entirej.framework.core.enumerations.EJManagedScreenProperty;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.EJCoreInsertScreenItemProperties;
import org.entirej.framework.core.renderers.interfaces.EJInsertScreenRenderer;
import org.entirej.framework.core.renderers.registry.EJInsertScreenItemRendererRegister;

public class EJManagedInsertScreenRendererWrapper implements Serializable
{
    private EJFrameworkManager     _frameworkManager;
    private EJInsertScreenRenderer _renderer;
    
    public EJManagedInsertScreenRendererWrapper(EJFrameworkManager manager, EJInsertScreenRenderer renderer)
    {
        _frameworkManager = manager;
        _renderer = renderer;
    }
    
    public EJInsertScreenRenderer getUnmanagedRenderer()
    {
        return _renderer;
    }
    
    public EJInsertScreenItemRendererRegister getItemRegister()
    {
        try
        {
            return _renderer.getItemRegister();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public EJDataRecord getInsertRecord()
    {
        try
        {
            return _renderer.getInsertRecord();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public void initialiseRenderer(EJEditableBlockController block)
    {
        try
        {
            _renderer.initialiseRenderer(block);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void open(EJDataRecord record)
    {
        try
        {
            _renderer.open(record);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void close()
    {
        try
        {
            _renderer.close();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void refreshInsertScreenRendererProperty(String propertyName)
    {
        try
        {
            _renderer.refreshInsertScreenRendererProperty(propertyName);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public void refreshItemProperty(EJCoreInsertScreenItemProperties itemProperties, EJManagedScreenProperty managedItemProperty)
    {
        try
        {
            _renderer.refreshItemProperty(itemProperties, managedItemProperty);
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
            _renderer.refreshAfterChange(record);
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
            _renderer.synchronize();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Returns an item with the given name
     * 
     * @param itemName
     *            The name of the required item
     */
    public EJScreenItemController getItem(String itemName)
    {
        try
        {
            EJScreenItemController screenItem = _renderer.getItem(itemName);
            if (screenItem == null)
            {
                handleException(new IllegalArgumentException("Unable to find item " + itemName + " on insert screen."));
                return null;
            }
            return screenItem;
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    public Object getGuiComponent()
    {
        try
        {
            Object rendererComponent = _renderer.getGuiComponent();
            if (rendererComponent == null)
            {
                handleException(new IllegalArgumentException("Renderer returned no GUI component."));
                return null;
            }
            return rendererComponent;
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
            _frameworkManager.handleException(new EJApplicationException(e));
        }
    }
}
