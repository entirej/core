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
import org.entirej.framework.core.renderers.eventhandlers.EJScreenItemValueChangedListener;
import org.entirej.framework.core.renderers.interfaces.EJItemRenderer;
import org.entirej.framework.core.renderers.registry.EJBlockItemRendererRegister;
import org.entirej.framework.core.renderers.registry.EJRendererFactory;

public class EJQueryScreenItemController implements EJScreenItemController, Comparable<EJQueryScreenItemController>
{
    private EJBlockController                     _blockController;
    private EJFrameworkManager                    _frameworkManager;
    private EJCoreItemProperties                  _itemProperties;
    private EJScreenItemProperties                _queryScreenItemProps;
    private EJManagedItemRendererWrapper          _queryScreenItemRenderer;
    private ArrayList<EJScreenItemValueChangedListener> _queryScreenItemValueChangedListeners;
    private ArrayList<EJItemFocusListener>        _queryScreenItemFocusedListeners;

    private EJItemLovController                   _itemLovController;
    private EJBlockItemRendererRegister           _blockItemRegister;

    public EJQueryScreenItemController(EJBlockController blockController, EJCoreItemProperties itemProperties)
    {
        _blockController = blockController;
        _frameworkManager = blockController.getFrameworkManager();
        _itemProperties = itemProperties;
        _queryScreenItemProps = _blockController.getProperties().getScreenItemGroupContainer(EJScreenType.QUERY)
                .getScreenItemProperties(_itemProperties.getName());

        _queryScreenItemValueChangedListeners = new ArrayList<EJScreenItemValueChangedListener>();
        _queryScreenItemFocusedListeners = new ArrayList<EJItemFocusListener>();

        if (_queryScreenItemProps.isLovNotificationEnabled())
        {
            _itemLovController = new EJItemLovController(_blockController.getFormController(), this, _itemProperties.getLovMappingPropertiesOnQuery());
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
            if (_queryScreenItemProps.isLovNotificationEnabled())
            {
                _itemLovController = new EJItemLovController(_blockController.getFormController(), this, null);
            }
        }
        else
        {
            EJCoreLovMappingProperties mappingPropertiesByName = _itemProperties.getLovMappingPropertiesByName(lovMapping);
            if (mappingPropertiesByName == null)
            {
                _itemLovController = null;
                if (_queryScreenItemProps.isLovNotificationEnabled())
                {
                    _itemLovController = new EJItemLovController(_blockController.getFormController(), this, null);
                }
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
    public void gainFocus()
    {
        _queryScreenItemRenderer.gainFocus();
    }

    @Override
    public EJScreenType getScreenType()
    {
        return EJScreenType.QUERY;
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
        return _queryScreenItemProps;
    }

    /**
     * Initialises the renderer for this item
     */
    public void initialiseRenderer()
    {
        if (_queryScreenItemProps != null)
        {
            EJManagedItemRendererWrapper renderer = EJRendererFactory.getInstance().getItemRenderer(this, _queryScreenItemProps);
            if (renderer != null)
            {
                _queryScreenItemRenderer = renderer;
            }
        }
    }

    /**
     * Returns the managed query screen item renderer for this item
     * 
     * @return The query screen item renderer
     */
    public EJManagedItemRendererWrapper getManagedItemRenderer()
    {
        if (_queryScreenItemRenderer != null)
        {
            return _queryScreenItemRenderer;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the query screen item renderer for this item
     * 
     * @return The query screen item renderer
     */
    public EJItemRenderer getItemRenderer()
    {
        if (_queryScreenItemRenderer != null)
        {
            return _queryScreenItemRenderer.getUnmanagedRenderer();
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
            if (_queryScreenItemRenderer != null)
            {
                if (_queryScreenItemProps == null)
                {
                    return;
                }
                String actionCommand = _queryScreenItemProps.getActionCommand();
                if (actionCommand == null || actionCommand.trim().length() == 0)
                {
                    return;
                }
                _blockController.executeActionCommand(actionCommand, EJScreenType.QUERY);
            }
        }
        catch (Exception e)
        {
            _blockController.getFormController().getFrameworkManager().handleException(e);
        }
    }

    /**
     * Adds a Query Screen <code>ItemFocusedListener</code> to this renderer
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
        _queryScreenItemFocusedListeners.add(listener);
    }

    /**
     * Adds a Query Screen <code>ItemChangedListener</code> to this renderer
     * <p>
     * <code>ItemChangedListeners</code> must be notified if the renderers value
     * changes
     * 
     * @param listener
     *            The value changed listener
     */
    public void addItemValueChangedListener(EJScreenItemValueChangedListener listener)
    {
        _queryScreenItemValueChangedListeners.add(listener);
    }

    /**
     * Removes an <code>EJItemFocusListener</code> from this renderer
     * 
     * @param listener
     *            The focus listener
     */
    public void removeItemFocusListener(EJItemFocusListener listener)
    {
        _queryScreenItemValueChangedListeners.remove(listener);
    }

    /**
     * Removes an <code>EJItemValueChangedListener</code> from this renderer
     * 
     * @param listener
     *            The value changed listener
     */
    public void removeItemValueChangedListener(EJScreenItemValueChangedListener listener)
    {
        _queryScreenItemValueChangedListeners.remove(listener);
    }

    public void itemValueChaged(Object newValue)
    {
        try
        {
            if (_queryScreenItemRenderer != null)
            {
                Iterator<EJScreenItemValueChangedListener> valueChangedListeners = _queryScreenItemValueChangedListeners.iterator();
                while (valueChangedListeners.hasNext())
                {
                    valueChangedListeners.next().screenItemValueChanged(this, _queryScreenItemRenderer.getUnmanagedRenderer(), newValue);
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
            if (_queryScreenItemRenderer != null)
            {
                for (EJItemFocusListener listener : _queryScreenItemFocusedListeners)
                {
                    listener.focusGained(new EJItemFocusedEvent(this, _queryScreenItemRenderer.getUnmanagedRenderer()));
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
            if (_queryScreenItemRenderer != null)
            {
                for (EJItemFocusListener listener : _queryScreenItemFocusedListeners)
                {
                    listener.focusLost(new EJItemFocusedEvent(this, _queryScreenItemRenderer.getUnmanagedRenderer()));
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
        return _queryScreenItemProps.isVisible();
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
        return _queryScreenItemProps.isSpacerItem();
    }

    /**
     * Indicates if this screen item should be validated against the lov values
     * 
     * @return <code>true</code> if the item should be validated against the lov
     *         values otherwise <code>false</code>
     */
    public boolean validateFromLov()
    {
        return _queryScreenItemProps.validateFromLov();
    }

    public int compareTo(EJQueryScreenItemController controller)
    {
        if (controller == null)
        {
            return -1;
        }

        return _itemProperties.getName().toUpperCase().compareTo(controller.getReferencedItemProperties().getName().toUpperCase());

    }

}
