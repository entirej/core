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

import java.util.ArrayList;
import java.util.Iterator;

import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.internal.EJInternalForm;
import org.entirej.framework.core.properties.EJCoreItemProperties;
import org.entirej.framework.core.properties.EJCoreLovMappingProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.renderers.EJManagedItemRendererWrapper;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusListener;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusedEvent;
import org.entirej.framework.core.renderers.eventhandlers.EJItemValueChangedListener;
import org.entirej.framework.core.renderers.interfaces.EJItemRenderer;
import org.entirej.framework.core.renderers.registry.EJBlockItemRendererRegister;
import org.entirej.framework.core.renderers.registry.EJRendererFactory;

public class EJUpdateScreenItemController implements EJScreenItemController, Comparable<EJUpdateScreenItemController>
{
    private EJBlockController                     _blockController;
    private EJFrameworkManager                    _frameworkManager;
    private EJCoreItemProperties                  _itemProperties;
    private EJScreenItemProperties                _updateScreenItemProps;
    private EJManagedItemRendererWrapper          _updateScreenItemRenderer;
    private ArrayList<EJItemValueChangedListener> _updateScreenItemValueChangedListeners;
    private ArrayList<EJItemFocusListener>        _updateScreenItemFocusedListeners;
    
    private EJItemLovController                   _itemLovController;
    private EJBlockItemRendererRegister           _blockItemRegister;
    
    public EJUpdateScreenItemController(EJBlockController blockController, EJCoreItemProperties itemProperties)
    {
        _blockController = blockController;
        _frameworkManager = blockController.getFrameworkManager();
        _itemProperties = itemProperties;
        _updateScreenItemProps = _blockController.getProperties().getScreenItemGroupContainer(EJScreenType.UPDATE)
                .getScreenItemProperties(_itemProperties.getName());
        
        _updateScreenItemValueChangedListeners = new ArrayList<EJItemValueChangedListener>();
        _updateScreenItemFocusedListeners = new ArrayList<EJItemFocusListener>();
        
        if (_updateScreenItemProps.isLovNotificationEnabled())
        {
            _itemLovController = new EJItemLovController(_blockController.getFormController(), this, _itemProperties.getLovMappingPropertiesOnUpdate());
        }
    }
    
    public void initialise(EJBlockItemRendererRegister blockItemRegister)
    {
        _blockItemRegister = blockItemRegister;
    }
    
    @Override
    public EJBlockItemRendererRegister getItemRendererRegister()
    {
        return _blockItemRegister;
    }
    
    
    @Override
    public void setItemLovMapping(String lovMapping)
    {
        if (lovMapping == null)
        {
            _itemLovController = null;
        }
        else
        {
            EJCoreLovMappingProperties mappingPropertiesByName = _itemProperties.getLovMappingPropertiesByName(lovMapping);
            if (mappingPropertiesByName == null)
            {
                _itemLovController = null;
            }
            else
            {
                _itemLovController = new EJItemLovController(_blockController.getFormController(), this, mappingPropertiesByName);
            }
        }

    }
    
    @Override
    public String getName()
    {
        return getReferencedItemProperties().getName();
    }
    
    @Override
    public EJScreenType getScreenType()
    {
        return EJScreenType.UPDATE;
    }
    
    @Override
    public void gainFocus()
    {
        _updateScreenItemRenderer.gainFocus();
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
    
    public EJItemLovController getItemLovController()
    {
        return _itemLovController;
    }
    
    public EJCoreItemProperties getReferencedItemProperties()
    {
        return _itemProperties;
    }
    
    public EJScreenItemProperties getProperties()
    {
        return _updateScreenItemProps;
    }
    
    /**
     * Initializes the renderer for this item
     */
    public void initialiseRenderer()
    {
        if (_updateScreenItemProps != null)
        {
            EJManagedItemRendererWrapper renderer = EJRendererFactory.getInstance().getItemRenderer(this, _updateScreenItemProps);
            if (renderer != null)
            {
                _updateScreenItemRenderer = renderer;
            }
        }
    }
    
    /**
     * Returns the managed update screen item renderer for this item
     * 
     * @return The update screen item renderer
     */
    public EJManagedItemRendererWrapper getManagedItemRenderer()
    {
        if (_updateScreenItemRenderer != null)
        {
            return _updateScreenItemRenderer;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Returns the update screen item renderer for this item
     * 
     * @return The update screen item renderer
     */
    public EJItemRenderer getItemRenderer()
    {
        if (_updateScreenItemRenderer != null)
        {
            return _updateScreenItemRenderer.getUnmanagedRenderer();
        }
        else
        {
            return null;
        }
    }
    
    public void executeActionCommand()
    {
        try
        {
            if (_updateScreenItemRenderer != null)
            {
                if (_updateScreenItemProps == null)
                {
                    return;
                }
                String actionCommand = _updateScreenItemProps.getActionCommand();
                if (actionCommand == null || actionCommand.trim().length() == 0)
                {
                    return;
                }
                _blockController.executeActionCommand(actionCommand, EJScreenType.UPDATE);
            }
        }
        catch (Exception e)
        {
            _blockController.getFormController().getFrameworkManager().handleException(e);
        }
    }
    
    /**
     * Adds a Update Screen <code>ItemFocusedListener</code> to this renderer
     * <p>
     * The listeners must be informed when a new item gains focus. This allows
     * the form controller to keep block operation and menu/toolbar operations
     * in sync
     * 
     * @param listener
     *            The focus listener
     */
    public void addItemFocusListener(EJItemFocusListener listener)
    {
        _updateScreenItemFocusedListeners.add(listener);
    }
    
    /**
     * Removes an <code>EJItemFocusListener</code> from this renderer
     * 
     * @param listener
     *            The focus listener
     */
    public void removeItemFocusListener(EJItemFocusListener listener)
    {
        _updateScreenItemFocusedListeners.remove(listener);
    }
    
    /**
     * Removes an <code>EJItemValueChangedListener</code> from this renderer
     * 
     * @param listener
     *            The value changed listener
     */
    public void removeItemValueChangedListener(EJItemValueChangedListener listener)
    {
        _updateScreenItemValueChangedListeners.remove(listener);
    }
    
    /**
     * Adds a Update Screen <code>ItemChangedListener</code> to this renderer
     * <p>
     * <code>ItemChangedListeners</code> must be notified if the renderers value
     * changes
     * 
     * @param listener
     *            The value changed listener
     */
    public void addItemValueChangedListener(EJItemValueChangedListener listener)
    {
        _updateScreenItemValueChangedListeners.add(listener);
    }
    
    public void itemValueChaged()
    {
        try
        {
            if (_updateScreenItemRenderer != null)
            {
                Iterator<EJItemValueChangedListener> valueChangedListeners = _updateScreenItemValueChangedListeners.iterator();
                while (valueChangedListeners.hasNext())
                {
                    valueChangedListeners.next().valueChanged(this, _updateScreenItemRenderer.getUnmanagedRenderer());
                }
            }
        }
        catch (Exception e)
        {
            _blockController.getFormController().getFrameworkManager().handleException(e);
        }
    }
    
    public void itemFocusGained()
    {
        try
        {
            if (_updateScreenItemRenderer != null)
            {
                for (EJItemFocusListener listener : _updateScreenItemFocusedListeners)
                {
                    listener.focusGained(new EJItemFocusedEvent(this, _updateScreenItemRenderer.getUnmanagedRenderer()));
                }
            }
        }
        catch (Exception e)
        {
            _blockController.getFormController().getFrameworkManager().handleException(e);
        }
    }
    
    public void itemFocusLost()
    {
        try
        {
            if (_updateScreenItemRenderer != null)
            {
                for (EJItemFocusListener listener : _updateScreenItemFocusedListeners)
                {
                    listener.focusLost(new EJItemFocusedEvent(this, _updateScreenItemRenderer.getUnmanagedRenderer()));
                }
            }
        }
        catch (Exception e)
        {
            _blockController.getFormController().getFrameworkManager().handleException(e);
        }
    }
    
    /**
     * Indicates if this item is to be made visible
     * <p>
     * 
     * @return <code>true</code> if the item should be made visible, otherwise
     *         <code>false</code>
     */
    public boolean isVisible()
    {
        return _updateScreenItemProps.isVisible();
    }
    
    /**
     * Indicates of the screen item controlled by this controller is a spacer
     * item
     * 
     * @return <code>true</code> if this controller controls a spacer item,
     *         otherwise <code>false</code>
     */
    public boolean isSpacerItem()
    {
        return _updateScreenItemProps.isSpacerItem();
    }
    
    /**
     * Indicates if this screen item should be validated agains the lov values
     * 
     * @return <code>true</code> if the item should be validated against the lov
     *         values otherwise <code>false</code>
     */
    public boolean validateFromLov()
    {
        return _updateScreenItemProps.validateFromLov();
    }
    
    public int compareTo(EJUpdateScreenItemController controller)
    {
        if (controller == null)
        {
            return -1;
        }
        
        return _itemProperties.getName().toUpperCase().compareTo(controller.getReferencedItemProperties().getName().toUpperCase());
        
    }
    
}
