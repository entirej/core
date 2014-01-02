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

public class EJInsertScreenItemController implements EJScreenItemController, Comparable<EJInsertScreenItemController> , Serializable
{
    private EJBlockController                     _blockController;
    private EJFrameworkManager                    _frameworkManager;
    private EJCoreItemProperties                  _itemProperties;
    private EJScreenItemProperties                _insertScreenItemProps;
    private EJManagedItemRendererWrapper          _insertScreenItemRenderer;
    private ArrayList<EJItemValueChangedListener> _insertScreenItemValueChangedListeners;
    private ArrayList<EJItemFocusListener>        _insertScreenItemFocusedListeners;
    
    private EJItemLovController                   _itemLovController;
    
    public EJInsertScreenItemController(EJBlockController blockController, EJCoreItemProperties itemProperties)
    {
        _blockController = blockController;
        _frameworkManager = blockController.getFrameworkManager();
        _itemProperties = itemProperties;
        _insertScreenItemProps = _blockController.getProperties().getScreenItemGroupContainer(EJScreenType.INSERT)
                .getScreenItemProperties(_itemProperties.getName());
        
        _insertScreenItemValueChangedListeners = new ArrayList<EJItemValueChangedListener>();
        _insertScreenItemFocusedListeners = new ArrayList<EJItemFocusListener>();
        
        if (_insertScreenItemProps.isLovNotificationEnabled())
        {
            _itemLovController = new EJItemLovController(_blockController.getFormController(), this, _itemProperties.getLovMappingPropertiesOnInsert());
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
        return EJScreenType.INSERT;
    }
    
    @Override
    public void gainFocus()
    {
        _insertScreenItemRenderer.gainFocus();
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
        return _insertScreenItemProps;
    }
    
    /**
     * Initialises the renderer for this item
     */
    public void initialiseRenderer()
    {
        if (_insertScreenItemProps != null)
        {
            EJManagedItemRendererWrapper renderer = EJRendererFactory.getInstance().getItemRenderer(this, _insertScreenItemProps);
            if (renderer != null)
            {
                _insertScreenItemRenderer = renderer;
            }
        }
    }
    
    /**
     * Returns the managed insert screen item renderer for this item
     * 
     * @return The insert screen item renderer
     */
    public EJManagedItemRendererWrapper getManagedItemRenderer()
    {
        if (_insertScreenItemRenderer != null)
        {
            return _insertScreenItemRenderer;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Returns the insert screen item renderer for this item
     * 
     * @return The insert screen item renderer
     */
    public EJItemRenderer getItemRenderer()
    {
        if (_insertScreenItemRenderer != null)
        {
            return _insertScreenItemRenderer.getUnmanagedRenderer();
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
            if (_insertScreenItemRenderer != null)
            {
                if (_blockController instanceof EJEditableBlockController)
                {
                    if (_insertScreenItemProps == null)
                    {
                        return;
                    }
                    String actionCommand = _insertScreenItemProps.getActionCommand();
                    if (actionCommand == null || actionCommand.trim().length() == 0)
                    {
                        return;
                    }
                    
                    _blockController.executeActionCommand(actionCommand, EJScreenType.INSERT);
                }
            }
        }
        catch (Exception e)
        {
            _blockController.getFormController().getFrameworkManager().handleException(e);
        }
    }
    
    /**
     * Adds an <code>EJItemFocusListener</code> to this renderer
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
        _insertScreenItemFocusedListeners.add(listener);
    }
    
    /**
     * Removes an <code>EJItemFocusListener</code> from this renderer
     * 
     * @param listener
     *            The focus listener
     */
    public void removeItemFocusListener(EJItemFocusListener listener)
    {
        _insertScreenItemFocusedListeners.remove(listener);
    }
    
    /**
     * Adds a Insert Screen <code>ItemChangedListener</code> to this renderer
     * <p>
     * <code>ItemChangedListeners</code> must be notified if the renderers value
     * changes
     * 
     * @param listener
     *            The value changed listener
     */
    public void addItemValueChangedListener(EJItemValueChangedListener listener)
    {
        _insertScreenItemValueChangedListeners.add(listener);
    }
    
    /**
     * Removes an <code>EJItemValueChangedListener</code> from this renderer
     * 
     * @param listener
     *            The value changed listener
     */
    public void removeItemValueChangedListener(EJItemValueChangedListener listener)
    {
        _insertScreenItemValueChangedListeners.remove(listener);
    }
    
    public void itemValueChaged()
    {
        try
        {
            if (_insertScreenItemRenderer != null)
            {
                Iterator<EJItemValueChangedListener> valueChangedListeners = _insertScreenItemValueChangedListeners.iterator();
                while (valueChangedListeners.hasNext())
                {
                    valueChangedListeners.next().valueChanged(this, _insertScreenItemRenderer.getUnmanagedRenderer());
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
            if (_insertScreenItemRenderer != null)
            {
                for (EJItemFocusListener listener : _insertScreenItemFocusedListeners)
                {
                    listener.focusGained(new EJItemFocusedEvent(this, _insertScreenItemRenderer.getUnmanagedRenderer()));
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
            if (_insertScreenItemRenderer != null)
            {
                for (EJItemFocusListener listener : _insertScreenItemFocusedListeners)
                {
                    listener.focusLost(new EJItemFocusedEvent(this, _insertScreenItemRenderer.getUnmanagedRenderer()));
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
        return _insertScreenItemProps.isVisible();
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
        return _insertScreenItemProps.isSpacerItem();
    }
    
    /**
     * Indicates if this screen item should be validated agains the lov values
     * 
     * @return <code>true</code> if the item should be validated against the lov
     *         values otherwise <code>false</code>
     */
    public boolean validateFromLov()
    {
        return _insertScreenItemProps.validateFromLov();
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_itemProperties == null) ? 0 : _itemProperties.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EJInsertScreenItemController other = (EJInsertScreenItemController) obj;
        if (_itemProperties == null)
        {
            if (other._itemProperties != null) return false;
        }
        else if (!_itemProperties.equals(other._itemProperties)) return false;
        return true;
    }
    
    public int compareTo(EJInsertScreenItemController controller)
    {
        if (controller == null)
        {
            return -1;
        }
        
        return _itemProperties.getName().toUpperCase().compareTo(controller.getReferencedItemProperties().getName().toUpperCase());
        
    }
    
}
