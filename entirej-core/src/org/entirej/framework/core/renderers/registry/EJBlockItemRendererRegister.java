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
package org.entirej.framework.core.renderers.registry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJScreenItem;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJBlockController;
import org.entirej.framework.core.enumerations.EJRecordType;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.renderers.EJManagedItemRendererWrapper;
import org.entirej.framework.core.renderers.eventhandlers.EJDataItemValueChangedListener;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusListener;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusedEvent;
import org.entirej.framework.core.renderers.eventhandlers.EJScreenItemValueChangedListener;
import org.entirej.framework.core.renderers.interfaces.EJItemRenderer;

public abstract class EJBlockItemRendererRegister implements EJScreenItemValueChangedListener, EJItemFocusListener, EJDataItemValueChangedListener, Serializable
{
    private ArrayList<EJScreenItemValueChangedListener>   _screenItemValueChangedListeners;
    private EJFrameworkManager                            _frameworkManager;
    private HashMap<String, EJManagedItemRendererWrapper> _itemRendererMap;
    private EJDataRecord                                  _registeredRecord;

    private EJBlockController                             _blockController;
    private boolean                                       _itemChanged = false;
    private boolean                                       _validate    = true;

    public abstract EJScreenType getScreenType();
    
    public EJBlockItemRendererRegister(EJBlockController blockController)
    {
        _blockController = blockController;
        _frameworkManager = blockController.getFrameworkManager();
        _itemRendererMap = new HashMap<String, EJManagedItemRendererWrapper>();
        _screenItemValueChangedListeners = new ArrayList<EJScreenItemValueChangedListener>();
        if (blockController.getProperties().isControlBlock() && blockController.getProperties().addControlBlockDefaultRecord())
        {
            EJDataRecord currentRecord = blockController.createRecord(EJRecordType.INSERT);
            blockController.getDataBlock().recordCreated(currentRecord, null);
            register(currentRecord);
        }
    }

    /**
     * Used to clear and reset the register
     * <p>
     * All registered items will be removed from the register
     */
    public void resetRegister()
    {
        if(_registeredRecord!=null)
        {
            _registeredRecord.setDataItemValueChangedListener(null);
        }
        _registeredRecord = null;
        _itemRendererMap.clear();
        _screenItemValueChangedListeners.clear();
        _itemChanged = false;
        _validate = true;
    }

    public void register(EJDataRecord record)
    {
        try
        {
            _registeredRecord = record;
            if(record!=null)
            {
                _registeredRecord.setDataItemValueChangedListener(null);
            }
            
            for (EJManagedItemRendererWrapper renderer : _itemRendererMap.values())
            {
                renderer.clearValue();
            }
            refreshAfterChange(record);
        }
        finally
        {
            _itemChanged = false;
            if(record!=null)
            {
                _registeredRecord.setDataItemValueChangedListener(this);
            }
        }

    }

    public void addItemValueChangedListener(EJScreenItemValueChangedListener listener)
    {
        if (listener != null)
        {
            _screenItemValueChangedListeners.add(listener);
        }
    }

    public void removeItemValueChangedListener(EJScreenItemValueChangedListener listener)
    {
        if (listener != null)
        {
            _screenItemValueChangedListeners.remove(listener);
        }
    }

    protected void fireValueChanged(EJScreenItemController item, EJItemRenderer changedRenderer)
    {
        for (EJScreenItemValueChangedListener listener : _screenItemValueChangedListeners)
        {
            listener.screenItemValueChanged(item, changedRenderer);
        }
    }

    protected EJBlockController getBlockController()
    {
        return _blockController;
    }

    public EJDataRecord getRegisteredRecord()
    {
        // Ensure all screen items are in the registered record before returning
        // it
        if (_registeredRecord != null)
        {
            
            
                EJManagedItemRendererWrapper renderer;
                for (String itemName : _itemRendererMap.keySet())
                {
                    if (_registeredRecord.containsItem(itemName))
                    {
                        renderer = _itemRendererMap.get(itemName);
                        if (!renderer.isReadOnly())
                        {
                            Object newValue = renderer.getValue();
                            Object oldValue = _registeredRecord.getValue(itemName);
                            if((newValue==null && oldValue!=null) 
                                    || (newValue!=null && oldValue==null) 
                                    || (oldValue!=null && !oldValue.equals(newValue)))
                            {
                                _registeredRecord.setValue(itemName, newValue);
                            }
                        }
                    }
                }
           
         
                   
            
        }
        return _registeredRecord;
    }

    /**
     * Returns the registered item renderer for a given item properties
     * 
     * @param itemName
     *            The name of the required item
     * @return A managed Item renderer wrapper around the required item renderer
     *         or <code>null</code> if there was no renderer registered for the
     *         given controller
     * 
     * @see {@link EJManagedItemRendererWrapper}
     */
    public EJManagedItemRendererWrapper getManagedItemRendererForItem(String itemName)
    {
        return _itemRendererMap.get(itemName);
    }

    /**
     * Returns the registered item renderer for a given item properties
     * 
     * @param item
     *            The screen item
     * @return A managed Item renderer wrapper around the required item renderer
     *         or <code>null</code> if there was no renderer registered for the
     *         given controller
     * 
     * @see {@link EJManagedItemRendererWrapper}
     */
    public EJItemRenderer getItemRendererForItem(EJScreenItem item)
    {
        EJManagedItemRendererWrapper wrapper = _itemRendererMap.get(item.getName());
        if (wrapper != null)
        {
            return wrapper.getUnmanagedRenderer();
        }

        return null;
    }

    /**
     * Returns a <code>Collection</code> of all registered renderers
     * 
     * @return A <code>Collection</code> of all registered renderers
     */
    public Collection<EJManagedItemRendererWrapper> getRegisteredRenderers()
    {
        return _itemRendererMap.values();
    }

    /**
     * Indicates if any one of the items have changed.
     * 
     * @return True if changes have been made, otherwise false
     */
    public boolean changesMade()
    {
        return _itemChanged;
    }

    /**
     * Copies all values from the given record to the enabled record
     * <p>
     * All items that are currently being displayed will have their
     * corresponding renderers updated. The enabled record will be dirty after
     * this operation
     * 
     * @param record
     *            The record containing the values to copy
     */
    public void refreshAfterChange(EJDataRecord record)
    {
        if (_registeredRecord == null)
        {
            return;
        }

        // I only need to change item renderers if the record changed is the
        // same as the registered record
        if (!(record == _registeredRecord || record.getBaseRecord() == _registeredRecord))
        {
            return;

        }
        EJManagedItemRendererWrapper renderer;
        for (String itemName : _itemRendererMap.keySet())
        {
            if (_registeredRecord.containsItem(itemName))
            {
                renderer = _itemRendererMap.get(itemName);
                renderer.setInitialValue(record.getValue(itemName));
            }
        }
        _itemChanged = true;
    }

    /**
     * Sets each registered item renderers display properties according to its
     * item properties
     */
    public abstract void initialiseRegisteredRenderers();

    /**
     * Clears all registered renderers of their current values and removes the
     * registered record
     */
    public void clearRegisteredValues()
    {
        if (_blockController.getProperties().isControlBlock())
        {
            EJDataRecord currentRecord = _blockController.createRecord(EJRecordType.INSERT);
            _blockController.getDataBlock().recordCreated(currentRecord, null);
            register(currentRecord);
        }
        else
        {
            if(_registeredRecord!=null)
                _registeredRecord.setDataItemValueChangedListener(null);
            _registeredRecord = null;
        }

        try
        {
            for (EJManagedItemRendererWrapper renderer : _itemRendererMap.values())
            {
                renderer.clearValue();
            }
        }
        finally
        {
            _itemChanged = false;
        }
    }

    /**
     * Registers a given renderer for the specified item
     * <p>
     * Once an item has its renderer registered then all synchronization between
     * the blocks underlying data and the renderer will be handled by the
     * <code>EntireJCoreFramework</code>
     * <p>
     * The register will be informed when a renderer gains focus, the register
     * will then inform the underlying block that it has gained focus
     * 
     * @param renderer
     *            The renderer to register
     * @param item
     *            The item that will be registered
     * @throws NullPointerException
     *             if the renderer is null or the itemName is either null or of
     *             zero length
     */
    public void registerRendererForItem(EJItemRenderer renderer, EJScreenItemController item)
    {
        if (item == null)
        {
            throw new NullPointerException("The Item passed to registerRendererForItem is null.");
        }
        if (renderer == null)
        {
            throw new NullPointerException("The renderer passed to registerRendererForItem is null. Item: " + item.getName());
        }

        // Create a wrapper around the renderer which will catch and handle any
        // exceptions
        EJManagedItemRendererWrapper wrapper = new EJManagedItemRendererWrapper(_frameworkManager, renderer);
        wrapper.setRegisteredItemName(item.getName());

        if (!item.isSpacerItem())
        {
            _itemRendererMap.put(item.getName(), wrapper);
        }

        item.initialise(this);

    }

    /**
     * Enables an item according to its properties
     * 
     * @param screenType
     *            The type of screen
     * @param itemProps
     *            The items properties
     */
    protected void enableItem(EJScreenType screenType, EJScreenItemProperties itemProps)
    {
        // If the item is not visible, then it cannot be enabled
        if (!itemProps.isVisible() || itemProps.isSpacerItem())
        {
            return;
        }

        boolean editAllowed = itemProps.isEditAllowed();

        EJScreenItemController item = getBlockController().getScreenItem(screenType, itemProps.getReferencedItemName());

        EJManagedItemRendererWrapper renderer = getManagedItemRendererForItem(itemProps.getReferencedItemName());

        if (item != null && renderer != null)
        {
            renderer.setVisible(true);
            renderer.setEditAllowed(editAllowed);

            if (item.getItemLovController() != null && itemProps.isLovNotificationEnabled())
            {
                renderer.enableLovActivation(true);
            }
        }
        else
        {
            throw new IllegalArgumentException("There is no renderer registered for the item: " + itemProps.getReferencedItemName());
        }
    }

    /**
     * Sets the enabled record and the correct item renderer to the given value
     * 
     * @param screenType
     *            The type of the screen for which this block item register is
     *            used (called from any subclass)
     * @param itemName
     *            The name of the item whos value will be set
     * @param value
     *            The value to set
     * @throws NullPointerException
     *             if the itemName passed is either missing or null
     * @throws IllegalArgumentException
     *             if there is no item in the enabled record with the given name
     *             or there is no item renderer registered for the given item
     */
    protected void setItemValue(EJScreenType screenType, String itemName, Object value)
    {
        if (itemName == null || itemName.trim().length() == 0)
        {
            throw new NullPointerException("The item name passed to setItemValue is either null or of zero length.");
        }
        if (_registeredRecord == null)
        {
            throw new NullPointerException("The register has not yet been enabled.");
        }
        if (!_registeredRecord.containsItem(itemName))
        {
            throw new IllegalArgumentException("There is no item within the record with the specified name: " + itemName);
        }

        _registeredRecord.getItem(itemName).setValue(value);

        // If the item being set is also displayed, then set the display
        // items value
        EJScreenItemController item = _blockController.getScreenItem(screenType, itemName);
        if (item != null)
        {
            if (item != null && _itemRendererMap.containsKey(itemName))
            {
                _itemRendererMap.get(itemName).setValue(value);
            }
        }
        _itemChanged = true;
    }

    /**
     * Sets the enabled record and the correct item renderer to the given value
     * without performing item validation if the item has an lov
     * 
     * @param screenType
     *            The screen type of this register
     * @param itemName
     *            The name of the item whos value will be set
     * @param value
     *            The value to set
     * @throws NullPointerException
     *             if the itemName passed is either missing or null
     * @throws IllegalArgumentException
     *             if there is no item in the enabled record with the given name
     *             or there is no item renderer registered for the given item
     */
    public void setItemValueNoValidate(EJScreenType screenType, String itemName, Object value)
    {
        if (itemName == null || itemName.trim().length() == 0)
        {
            throw new NullPointerException("The item name passed to setItemValue is either null or of zero length.");
        }
        if (_registeredRecord == null)
        {
            throw new NullPointerException("The register has not yet been enabled.");
        }
        if (!_registeredRecord.containsItem(itemName))
        {
            throw new IllegalArgumentException("There is no item within the record with the specified name: " + itemName);
        }

        _registeredRecord.getItem(itemName).setValue(value);

        try
        {
            _validate = false;
            // If the item being set is also displayed, then set the display
            // items value
            EJScreenItemController item = _blockController.getScreenItem(screenType, itemName);
            if (item != null)
            {
                if (item != null && _itemRendererMap.containsKey(itemName))
                {
                    _itemRendererMap.get(itemName).setInitialValue(value);
                }
            }
        }
        finally
        {
            _validate = true;
        }
        _itemChanged = true;
    }

    public void screenItemValueChanged(EJScreenItemController item, EJItemRenderer changedRenderer)
    {
        if (_validate)
        {
            _itemChanged = true;
            _blockController.itemValueChanged(item, changedRenderer);

            EJManagedItemRendererWrapper renderer = _itemRendererMap.get(item.getProperties().getReferencedItemName());

            if (renderer != null && item.getItemLovController() != null && item.getProperties().isLovNotificationEnabled())
            {
                fireLovValidate(item, changedRenderer.getValue());
            }
        }
    }

    public void validateItem(EJItemRenderer item, EJScreenType screenType)
    {
        try
        {
            getBlockController().getFormController().getManagedActionController().getUnmanagedController()
                    .validateItem(getBlockController().getFormController().getEJForm(), _blockController.getProperties().getName(), item.getRegisteredItemName(), screenType);
            item.validationErrorOccurred(false);
        }
        catch (Exception e)
        {
            item.validationErrorOccurred(true);
            getBlockController().getFrameworkManager().handleException(e);
        }
    }

    public abstract void fireLovValidate(EJScreenItemController item, Object value);

    public void focusGained(EJItemFocusedEvent focusedEvent)
    {
        _blockController.setRendererFocus(true);

    }

    public void focusLost(EJItemFocusedEvent focusedEvent)
    {
        _blockController.setRendererFocus(false);
    }

    public void dataItemValueChanged(String itemName, EJDataRecord changedRecord, EJScreenType screenType)
    {
        _blockController.getBlock().dataItemValueChanged(itemName, changedRecord, getScreenType());
    }
}
