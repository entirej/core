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
import java.util.Iterator;

import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.internal.EJInternalForm;
import org.entirej.framework.core.properties.EJCoreItemProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.renderers.EJManagedItemRendererWrapper;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusListener;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusedEvent;
import org.entirej.framework.core.renderers.eventhandlers.EJItemValueChangedListener;
import org.entirej.framework.core.renderers.interfaces.EJItemRenderer;
import org.entirej.framework.core.renderers.registry.EJRendererFactory;

public class EJMainScreenItemController implements EJScreenItemController, Comparable<EJMainScreenItemController>, Serializable
{
    private EJBlockController                     _blockController;
    private EJFrameworkManager                    _frameworkManager;
    private EJCoreItemProperties                  _itemProperties;
    private EJScreenItemProperties                _mainScreenItemProps;
    private EJManagedItemRendererWrapper          _mainScreenItemRenderer;
    private ArrayList<EJItemValueChangedListener> _mainScreenItemValueChangedListeners;
    private ArrayList<EJItemFocusListener>        _mainScreenItemFocusedListeners;
    
    private EJItemLovController                   _itemLovController;
    
    public EJMainScreenItemController(EJBlockController blockController, EJCoreItemProperties itemProperties)
    {
        _blockController = blockController;
        _frameworkManager = blockController.getFrameworkManager();
        _itemProperties = itemProperties;
        _mainScreenItemProps = _blockController.getProperties().getScreenItemGroupContainer(EJScreenType.MAIN)
                .getScreenItemProperties(_itemProperties.getName());
        _mainScreenItemValueChangedListeners = new ArrayList<EJItemValueChangedListener>();
        _mainScreenItemFocusedListeners = new ArrayList<EJItemFocusListener>();
        
        if (_mainScreenItemProps.isLovNotificationEnabled())
        {
            _itemLovController = new EJItemLovController(_blockController.getFormController(), this, _itemProperties.getLovMappingPropertiesOnMain());
        }
    }
    
    @Override
    public String getName()
    {
        return getReferencedItemProperties().getName();
    }
    
    @Override
    public void gainFocus()
    {
        _mainScreenItemRenderer.gainFocus();
    }
    
    @Override
    public EJScreenType getScreenType()
    {
        return EJScreenType.MAIN;
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
        return _mainScreenItemProps;
    }
    
    /**
     * Initializes the renderer for this item
     */
    public void initialiseRenderer()
    {
        if (_mainScreenItemProps != null)
        {
            EJManagedItemRendererWrapper renderer = EJRendererFactory.getInstance().getItemRenderer(this, _mainScreenItemProps);
            if (renderer != null)
            {
                _mainScreenItemRenderer = renderer;
            }
        }
    }
    
    /**
     * Returns the managed main screen item renderer for this item
     * 
     * @return The main screen item renderer
     */
    public EJManagedItemRendererWrapper getManagedItemRenderer()
    {
        if (_mainScreenItemRenderer != null)
        {
            return _mainScreenItemRenderer;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Returns the managed main screen item renderer for this item
     * 
     * @return The main screen item renderer
     */
    public EJItemRenderer getItemRenderer()
    {
        if (_mainScreenItemRenderer != null)
        {
            return _mainScreenItemRenderer.getUnmanagedRenderer();
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
            if (_mainScreenItemRenderer != null)
            {
                if (_blockController instanceof EJEditableBlockController)
                {
                    if (_mainScreenItemProps == null)
                    {
                        return;
                    }
                    String actionCommand = _mainScreenItemProps.getActionCommand();
                    if (actionCommand == null || actionCommand.trim().length() == 0)
                    {
                        return;
                    }
                    
                    _blockController.executeActionCommand(actionCommand, EJScreenType.MAIN);
                }
            }
        }
        catch (Exception e)
        {
            _blockController.getFormController().getFrameworkManager().handleException(e);
        }
    }
    
    /**
     * Adds a Main Screen <code>EJItemFocusedListener</code> to this renderer
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
        _mainScreenItemFocusedListeners.add(listener);
    }
    
    /**
     * Removes an <code>EJItemFocusListener</code> from this renderer
     * 
     * @param listener
     *            The focus listener
     */
    public void removeItemFocusListener(EJItemFocusListener listener)
    {
        _mainScreenItemFocusedListeners.remove(listener);
    }
    
    /**
     * Removes an <code>EJItemValueChangedListener</code> from this renderer
     * 
     * @param listener
     *            The value changed listener
     */
    public void removeItemValueChangedListener(EJItemValueChangedListener listener)
    {
        _mainScreenItemFocusedListeners.remove(listener);
    }
    
    /**
     * Adds a Main Screen <code>EJItemChangedListener</code> to this renderer
     * <p>
     * <code>ItemChangedListeners</code> must be notified if the renderers value
     * changes
     * 
     * @param listener
     *            The value changed listener
     */
    public void addItemValueChangedListener(EJItemValueChangedListener listener)
    {
        _mainScreenItemValueChangedListeners.add(listener);
    }
    
    public void itemValueChaged()
    {
        try
        {
            if (_mainScreenItemRenderer != null)
            {
                
                Iterator<EJItemValueChangedListener> valueChangedListeners = _mainScreenItemValueChangedListeners.iterator();
                while (valueChangedListeners.hasNext())
                {
                    valueChangedListeners.next().valueChanged(this, _mainScreenItemRenderer.getUnmanagedRenderer());
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
            if (_mainScreenItemRenderer != null)
            {
                EJItemFocusedEvent event = new EJItemFocusedEvent(this, _mainScreenItemRenderer.getUnmanagedRenderer());
                
                for (EJItemFocusListener listener : _mainScreenItemFocusedListeners)
                {
                    listener.focusGained(event);
                }
                _blockController.itemFocusedGained(event);
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
            if (_mainScreenItemRenderer != null)
            {
                EJItemFocusedEvent event = new EJItemFocusedEvent(this, _mainScreenItemRenderer.getUnmanagedRenderer());
                for (EJItemFocusListener listener : _mainScreenItemFocusedListeners)
                {
                    listener.focusLost(event);
                }
                
                _blockController.itemFocusedGained(event);
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
        return _mainScreenItemProps.isVisible();
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
        return _mainScreenItemProps.isSpacerItem();
    }
    
    /**
     * Indicates if this screen item should be validated against the lov values
     * 
     * @return <code>true</code> if the item should be validated against the lov
     *         values otherwise <code>false</code>
     */
    public boolean validateFromLov()
    {
        return _mainScreenItemProps.validateFromLov();
    }
    
    public int compareTo(EJMainScreenItemController controller)
    {
        if (controller == null)
        {
            return -1;
        }
        
        return _itemProperties.getName().toUpperCase().compareTo(controller.getReferencedItemProperties().getName().toUpperCase());
        
    }
    
}
