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
package org.entirej.framework.core;

import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJBlockRendererController;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.enumerations.EJManagedScreenProperty;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.properties.EJCoreInsertScreenItemProperties;
import org.entirej.framework.core.properties.EJCoreMainScreenItemProperties;
import org.entirej.framework.core.properties.EJCoreQueryScreenItemProperties;
import org.entirej.framework.core.properties.EJCoreUpdateScreenItemProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJDisplayProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJItemProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.renderers.EJManagedInsertScreenRendererWrapper;
import org.entirej.framework.core.renderers.EJManagedQueryScreenRendererWrapper;
import org.entirej.framework.core.renderers.EJManagedUpdateScreenRendererWrapper;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusListener;
import org.entirej.framework.core.renderers.eventhandlers.EJItemValueChangedListener;

public class EJScreenItem
{
    private EJScreenType           _screenType;
    private EJScreenItemController _item;
    private EJInternalBlock        _block;

    public EJScreenItem(EJInternalBlock block, EJScreenType screenType, EJScreenItemController item)
    {
        _block = block;
        _screenType = screenType;
        _item = item;
    }

    /**
     * Returns the properties of this screen items
     * 
     * @return The properties of this screen item
     */
    public EJScreenItemProperties getProperties()
    {
        return _item.getProperties();
    }

    /**
     * Returns the name of the block to which this item belongs
     * 
     * @return The name of the block to which this item belongs
     */
    public String getBlockName()
    {
        return _item.getBlock().getProperties().getName();
    }

    public void refreshItemRenderer()
    {
        _item.getManagedItemRenderer().refreshItemRenderer();
    }

    /**
     * Indicates if this item is used within an LOV Definition
     * 
     * @return <code>true</code> if this item is used within a lov definition,
     *         otherwise <code>false</code>
     */
    public boolean isUsedInLovDefinition()
    {
        return _item.getBlock().getProperties().isUsedInLovDefinition();
    }

    /**
     * If this item is used within a lov definition, then this method will
     * return the name of the lov definition
     * 
     * @return the name of the lov definition to which this item belongs or
     *         <code>null</code> if this item is not displayed within a lov
     */
    public String getLovDefinitionName()
    {
        return _item.getBlock().getProperties().getLovDefinition().getName();
    }

    /**
     * Returns the name of this item
     * 
     * @return The name of this item
     */
    public String getName()
    {
        return _item.getReferencedItemProperties().getName();
    }

    /**
     * Indicates if this item is visible
     * 
     * @return <code>true</code> if this item is visible otherwise
     *         <code>false</code>
     */
    public boolean isVisible()
    {
        if (_item.getItemRenderer() != null)
        {
            return _item.getManagedItemRenderer().isVisible();
        }

        return false;
    }

    /**
     * Indicates if lov notification has been enabled
     * <p>
     * Lov Notification is automatically enabled for items with an lov attached.
     * However it is also possible to enable lov notification for non lov items.
     * If this is the case, then EJ will call the action processor to notify the
     * developer that the lov has been activated, but no LOV will be displayed.
     * The developer can then do what is required for the specific business
     * case, e.g. Check the value entered against a Business Service or call
     * another form etc
     * 
     * @return <code>true</code> if lov notification has been enabled, otherwise
     *         <code>false</code>
     */
    public boolean isLovNotificationEnabled()
    {
        return _item.getProperties().isLovNotificationEnabled();
    }

    /**
     * Indicates if this screen item is a spacer item
     * 
     * @return return <code>true</code> if this item is a spacer item, otherwise
     *         <code>false</code>
     */
    public boolean isSpacerItem()
    {
        return _item.isSpacerItem();
    }

    /**
     * Indicates if this item is mandatory
     * 
     * @return <code>true</code> if this item is mandatory otherwise
     *         <code>false</code>
     */
    public boolean isMandatory()
    {
        if (_item.getItemRenderer() != null)
        {
            return _item.getManagedItemRenderer().isMandatory();
        }

        return false;
    }

    /**
     * Indicates if this item can be modified by the user
     * 
     * @return allowed <code>true</code> if edit is allowed otherwise
     *         <code>false</code>
     */
    public boolean isEditable()
    {
        if (_item.getItemRenderer() != null)
        {
            return _item.getManagedItemRenderer().isEditAllowed();
        }

        return false;
    }

    /**
     * Indicates if this item is valid
     * <p>
     * A valid item is one that has have validation performed on the items value
     * and the validation was successful
     * 
     * @return <code>true</code> if the item is value, otherwise
     *         <code>false</code>
     */
    public boolean isValid()
    {
        if (_item.getItemRenderer() != null)
        {
            return _item.getManagedItemRenderer().isValid();
        }

        return false;
    }

    /**
     * Returns the hint that has been set for this item
     * 
     * @return This items hint, or <code>null</code> if none has been set
     */
    public String getHint()
    {
        return _item.getProperties().getHint();
    }

    /**
     * Return the label that has been set on this item
     * 
     * @return This items label or <code>null</code> if no label has been set
     */
    public String getLabel()
    {
        return _item.getProperties().getLabel();
    }

    /**
     * Returns the screen type of this item
     * 
     * @return This items screen type
     */
    public EJScreenType getScreenType()
    {
        return _screenType;
    }

    /**
     * Sets this items value
     * 
     * @param value
     *            The value set
     * @throws EJApplicationException
     *             if the value given is of the wrong data type
     */
    public void setValue(Object value)
    {
        if (value != null)
        {
            EJItemProperties itemProperties = _block.getProperties().getItemProperties(getName());

            // Check if the value is of the correct data type
            if (!itemProperties.getDataTypeClass().isAssignableFrom(value.getClass()))
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_DATA_TYPE_FOR_ITEM,
                        itemProperties.getName(), itemProperties.getDataTypeClassName(), value.getClass().getName()));
            }
        }
        _item.getManagedItemRenderer().setValue(value);
    }

    /**
     * If this item has a renderer, then this method will return the value held
     * within the items renderer
     * 
     * @return The value held within the items renderer if it has one
     */
    public Object getValue()
    {
        if (_item.getManagedItemRenderer() != null)
        {
            return _item.getManagedItemRenderer().getValue();
        }
        else
        {
            return null;
        }
    }

    /**
     * Indicates that this item should gain focus
     */
    public void gainFocus()
    {
        _item.gainFocus();
    }

    /**
     * Indicates to this item that it should execute its action command if it
     * has one enabled
     * <p>
     * Any item can have an action command if the item renderer has defined one.
     * However, action commands are normally used for Buttons, Check Box's etc.
     * <p>
     * If the item has an ActionCommand assigned then EntireJ will inform the
     * action handler that the action command has been executed. i.e. the button
     * has been pressed
     */
    public void executeActionCommand()
    {
        _item.executeActionCommand();
    }

    /**
     * Sets the hint of this item
     * <p>
     * The hint will not be translated. If the hint requires translation before
     * being set, please use {@link #setHint(String, boolean)}
     * 
     * @param hint
     *            The hint to set
     */
    public void setHint(String hint)
    {
        setHint(hint, false);
    }

    /**
     * Sets the hint of this screen item
     * <p>
     * 
     * @param hint
     *            The hint to set
     * @param translate
     *            Indicates if the given hint needs to be translated before it
     *            is set to the item
     */
    public void setHint(String hint, boolean translate)
    {
        if (translate && hint != null)
        {
            String translatedLabel = _block.getForm().translateText(hint);
            setPropertyAndRefresh(EJManagedScreenProperty.HINT, translatedLabel);
        }
        else
        {
            setPropertyAndRefresh(EJManagedScreenProperty.HINT, hint);
        }
    }

    /**
     * Sets the label of this item
     * <p>
     * The label will not be translated. If the label requires translation
     * before being set, please use {@link #setLabel(String, boolean)}
     * 
     * @param label
     *            The label to set
     */
    public void setLabel(String label)
    {
        setLabel(label, false);
    }

    /**
     * Sets the label of this screen item
     * <p>
     * 
     * @param label
     *            The label to set
     * @param translate
     *            Indicates if the given label needs to be translated before it
     *            is set to the item
     */
    public void setLabel(String label, boolean translate)
    {
        if (translate && label != null)
        {
            String translatedLabel = _block.getForm().translateText(label);
            setPropertyAndRefresh(EJManagedScreenProperty.LABEL, translatedLabel);
        }
        else
        {
            setPropertyAndRefresh(EJManagedScreenProperty.LABEL, label);
        }
    }

    /**
     * Sets this item to be editable or not
     * <p>
     * If an item is editable, it can be modified by the user. A non editable
     * item can be navigated to but cannot be modified
     * 
     * @param editable
     *            <code>true</code> if this item should be editable, otherwise
     *            <code>false</code>
     */
    public void setEditable(boolean editable)
    {
        setPropertyAndRefresh(EJManagedScreenProperty.EDIT_ALLOWED, editable);
    }

    /**
     * Sets the visual attribute of this screen item
     * <p>
     * The block renderer will decide how the visual attribute will be displayed
     * as this can vary depending on how the block is built. If for example the
     * block is a single record block, then this method may result in an item
     * being set to the given visual attribute.
     * <p>
     * This method will result in the item being set on each record displayed.
     * If the user changes record, then the visual attribute will still be
     * displayed unless the developer changes it. If the visual attribute should
     * only be displayed for a given item instance, i.e. the item on a per
     * record basis. Then this can be achieved by using the {@link
     * Record.getItem(String).setVisualAttribute(String)} method from the record
     * passed to the forms action processor.
     * 
     * 
     * @param visualAttributeName
     *            The name of the visual attribute to set or <code>null</code>
     *            if the screen item should be returned to its original state
     */
    public void setVisualAttribute(String visualAttributeName)
    {
        setPropertyAndRefresh(EJManagedScreenProperty.SCREEN_ITEM_VISUAL_ATTRIBUTE, null, visualAttributeName);
    }

    /**
     * Sets this item to be visible
     * 
     * @param visible
     *            <code>true</code> if this item should be visible, otherwise
     *            <code>false</code>
     */
    public void setVisible(boolean visible)
    {
        setPropertyAndRefresh(EJManagedScreenProperty.VISIBLE, visible);
    }

    /**
     * Sets this item to be mandatory
     * <p>
     * A mandatory item, is an item that allows users to change the value {@see
     * #setEditable(boolean)} and the value is a required value
     * 
     * @param mandatory
     *            <code>true</code> if this item should be mandatory, otherwise
     *            <code>false</code>
     */
    public void setMandatory(boolean mandatory)
    {
        setPropertyAndRefresh(EJManagedScreenProperty.MANDATORY, mandatory);
    }

    /**
     * Sets this item to be valid
     * 
     * @param valid
     *            <code>true</code> if this item should be valid, otherwise
     *            <code>false</code>
     */
    public void setValid(boolean valid)
    {
        _item.getManagedItemRenderer().validationErrorOccurred(!valid);
    }

    /**
     * Returns the display properties of this item
     * 
     * @return This items display properties
     */
    public EJDisplayProperties getDisplayProperties()
    {
        switch (_screenType)
        {
            case MAIN:
                return ((EJCoreMainScreenItemProperties) _item.getProperties()).getBlockRendererRequiredProperties();
            case QUERY:
                return ((EJCoreQueryScreenItemProperties) _item.getProperties()).getQueryScreenRendererProperties();
            case INSERT:
                return ((EJCoreInsertScreenItemProperties) _item.getProperties()).getInsertScreenRendererProperties();
            case UPDATE:
                return ((EJCoreUpdateScreenItemProperties) _item.getProperties()).getUpdateScreenRendererProperties();
            default:
                return ((EJCoreMainScreenItemProperties) _item.getProperties()).getBlockRendererRequiredProperties();
        }
    }

    /**
     * Set the Screen Renderer Settings for this item
     * <p>
     * If the property is part of a <code>propertyGroup</code> then use a dot
     * (.) Separator between the group name and the property name. e.g. If the
     * <code>xpos</code> property is in the group
     * <code>displayCoordinates</code> then the property name would be:
     * <code>displayCoordinates.xpos</code>
     * <p>
     * The name of the properties and their functionality are dependent on the
     * item renderer being used. Please see the corresponding item renderer
     * documentation for more details
     * 
     * @param propertyName
     *            The name of the property
     * @param propertyValue
     *            The new property value
     * @throws EJApplicationException
     *             if either the block or item name are invalid and if the there
     *             is no display property with the given name
     */
    public void setScreenRendererProperty(String propertyName, String propertyValue)
    {
        if (hasValue(propertyName) && hasValue(propertyValue))
        {
            switch (_screenType)
            {
                case MAIN:
                    refreshMainScreenItemRenderer(propertyName, propertyValue);
                    break;

                case QUERY:
                    refreshQueryScreenItemRenderer(propertyName, propertyValue);
                    break;

                case INSERT:
                    refreshInsertScreenItemRenderer(propertyName, propertyValue);
                    break;

                case UPDATE:
                    refreshUpdateScreenItemRenderer(propertyName, propertyValue);
                    break;

            }
        }
    }

    /**
     * Set the Item Renderer Settings for this item
     * <p>
     * If the property is part of a <code>propertyGroup</code> then use a dot
     * (.) Separator between the group name and the property name. e.g. If the
     * <code>xpos</code> property is in the group
     * <code>displayCoordinates</code> then the property name would be:
     * <code>displayCoordinates.xpos</code>
     * <p>
     * The name of the properties and their functionality are dependent on the
     * item renderer being used. Please see the corresponding item renderer
     * documentation for more details
     * 
     * @param propertyName
     *            The name of the property
     * @param propertyValue
     *            The new property value
     * @throws EJApplicationException
     *             if either the block or item name are invalid and if the there
     *             is no display property with the given name
     */
    public void setItemRendererProperty(String propertyName, String propertyValue)
    {
        if (hasValue(propertyName) && hasValue(propertyValue))
        {
            refreshItemRendererProperty(propertyName, propertyValue);
        }
    }

    private void setPropertyAndRefresh(EJManagedScreenProperty property, EJDataRecord record, String visualAttributeName)
    {
        setPropertyAndRefresh(property, false, null, record, visualAttributeName);
    }

    private void setPropertyAndRefresh(EJManagedScreenProperty property, boolean allowed)
    {
        setPropertyAndRefresh(property, allowed, null, null, null);
    }

    private void setPropertyAndRefresh(EJManagedScreenProperty property, String label)
    {
        setPropertyAndRefresh(property, false, label, null, null);
    }

    private void setPropertyAndRefresh(EJManagedScreenProperty property, boolean allowed, String label, EJDataRecord record, String vaName)
    {
        switch (_screenType)
        {
            case MAIN:
                EJCoreMainScreenItemProperties mainScreenItemProps = (EJCoreMainScreenItemProperties) _item.getProperties();
                if (mainScreenItemProps != null)
                {
                    switch (property)
                    {
                        case VISIBLE:
                            mainScreenItemProps.setVisible(allowed);
                            break;
                        case EDIT_ALLOWED:
                            mainScreenItemProps.setEditAllowed(allowed);
                            break;
                        case MANDATORY:
                            mainScreenItemProps.setMandatory(allowed);
                            break;
                        case LABEL:
                            mainScreenItemProps.setBaseLabel(label);
                            break;
                        case HINT:
                            mainScreenItemProps.setBaseHint(label);
                            break;
                        case SCREEN_ITEM_VISUAL_ATTRIBUTE:
                            mainScreenItemProps.setVisualAttributeName(vaName);
                            break;
                    }
                }

                EJBlockRendererController blockRendererController = _block.getRendererController();
                if (blockRendererController != null)
                {
                    try
                    {
                        blockRendererController.refreshItemProperty(mainScreenItemProps.getReferencedItemName(), property, null);
                    }
                    catch (Exception e)
                    {
                        _block.getFrameworkManager().handleException(e);
                    }
                }
                break;

            case QUERY:
                EJCoreQueryScreenItemProperties queryScreenItemProps = (EJCoreQueryScreenItemProperties) _item.getProperties();
                if (queryScreenItemProps != null)
                {
                    switch (property)
                    {
                        case VISIBLE:
                            queryScreenItemProps.setVisible(allowed);
                            break;
                        case EDIT_ALLOWED:
                            queryScreenItemProps.setEditAllowed(allowed);
                            break;
                        case MANDATORY:
                            queryScreenItemProps.setMandatory(allowed);
                            break;
                        case LABEL:
                            queryScreenItemProps.setBaseLabel(label);
                            break;
                        case HINT:
                            queryScreenItemProps.setBaseHint(label);
                            break;
                        case SCREEN_ITEM_VISUAL_ATTRIBUTE:
                            queryScreenItemProps.setVisualAttributeName(vaName);
                            break;
                    }
                }

                EJManagedQueryScreenRendererWrapper queryScreenRenderer = _block.getQueryScreenRenderer();
                if (queryScreenRenderer != null)
                {
                    queryScreenRenderer.refreshItemProperty(queryScreenItemProps, property);
                }
                break;

            case INSERT:
                EJCoreInsertScreenItemProperties insertScreenItemProps = (EJCoreInsertScreenItemProperties) _item.getProperties();
                if (insertScreenItemProps != null)
                {
                    switch (property)
                    {
                        case VISIBLE:
                            insertScreenItemProps.setVisible(allowed);
                            break;
                        case EDIT_ALLOWED:
                            insertScreenItemProps.setEditAllowed(allowed);
                            break;
                        case MANDATORY:
                            insertScreenItemProps.setMandatory(allowed);
                            break;
                        case LABEL:
                            insertScreenItemProps.setBaseLabel(label);
                            break;
                        case HINT:
                            insertScreenItemProps.setBaseHint(label);
                            break;
                        case SCREEN_ITEM_VISUAL_ATTRIBUTE:
                            insertScreenItemProps.setVisualAttributeName(vaName);
                            break;
                    }
                }

                EJManagedInsertScreenRendererWrapper insertScreenRenderer = _block.getInsertScreenRenderer();
                if (insertScreenRenderer != null)
                {
                    insertScreenRenderer.refreshItemProperty(insertScreenItemProps, property);
                }
                break;

            case UPDATE:
                EJCoreUpdateScreenItemProperties updateScreenItemProps = (EJCoreUpdateScreenItemProperties) _item.getProperties();
                if (updateScreenItemProps != null)
                {
                    switch (property)
                    {
                        case VISIBLE:
                            updateScreenItemProps.setVisible(allowed);
                            break;
                        case EDIT_ALLOWED:
                            updateScreenItemProps.setEditAllowed(allowed);
                            break;
                        case MANDATORY:
                            updateScreenItemProps.setMandatory(allowed);
                            break;
                        case LABEL:
                            updateScreenItemProps.setBaseLabel(label);
                            break;
                        case HINT:
                            updateScreenItemProps.setBaseHint(label);
                            break;
                        case SCREEN_ITEM_VISUAL_ATTRIBUTE:
                            updateScreenItemProps.setVisualAttributeName(vaName);
                            break;
                    }
                }

                EJManagedUpdateScreenRendererWrapper updateScreenRenderer = _block.getUpdateScreenRenderer();
                if (updateScreenRenderer != null)
                {
                    updateScreenRenderer.refreshItemProperty(updateScreenItemProps, property);
                }
                break;
        }
    }

    private void refreshMainScreenItemRenderer(String propertyName, String propertyValue)
    {
        EJFrameworkExtensionProperties blockRendererRequiredProperties = ((EJCoreMainScreenItemProperties) _item.getProperties())
                .getBlockRendererRequiredProperties();
        blockRendererRequiredProperties.setPropertyValue(propertyName, propertyValue);
        _block.getRendererController().refreshItemRendererProperty(_item.getReferencedItemProperties().getName(), propertyName);
    }

    private void refreshQueryScreenItemRenderer(String propertyName, String propertyValue)
    {
        EJFrameworkExtensionProperties qsRendererProperties = ((EJCoreQueryScreenItemProperties) _item.getProperties()).getQueryScreenRendererProperties();
        qsRendererProperties.setPropertyValue(propertyName, propertyValue);
        if (_block.getQueryScreenRenderer() != null)
        {
            _block.getQueryScreenRenderer().refreshQueryScreenRendererProperty(propertyName);
        }
    }

    private void refreshInsertScreenItemRenderer(String propertyName, String propertyValue)
    {
        EJFrameworkExtensionProperties isRendererProperties = ((EJCoreInsertScreenItemProperties) _item.getProperties()).getInsertScreenRendererProperties();
        isRendererProperties.setPropertyValue(propertyName, propertyValue);
        if (_block.getInsertScreenRenderer() != null)
        {
            _block.getInsertScreenRenderer().refreshInsertScreenRendererProperty(propertyName);
        }
    }

    private void refreshItemRendererProperty(String propertyName, String propertyValue)
    {
        EJFrameworkExtensionProperties itemRendererProperties = _item.getReferencedItemProperties().getItemRendererProperties();
        itemRendererProperties.setPropertyValue(propertyName, propertyValue);
        if (_item.getItemRenderer() != null)
        {
            _item.getItemRenderer().refreshItemRendererProperty(propertyName);
        }
    }

    private void refreshUpdateScreenItemRenderer(String propertyName, String propertyValue)
    {
        EJFrameworkExtensionProperties isRendererProperties = ((EJCoreUpdateScreenItemProperties) _item.getProperties()).getUpdateScreenRendererProperties();
        isRendererProperties.setPropertyValue(propertyName, propertyValue);

        if (_block.getUpdateScreenRenderer() != null)
        {
            _block.getUpdateScreenRenderer().refreshUpdateScreenRendererProperty(propertyName);
        }
    }

    private boolean hasValue(String value)
    {
        if (value == null || value.trim().length() == 0)
        {
            return false;
        }

        return true;
    }

    /**
     * Adds am <code>ItemFocusedListener</code> to this renderer
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
        _item.addItemFocusListener(listener);
    }

    /**
     * Adds an <code>ItemChangedListener</code> to this renderer
     * <p>
     * <code>ItemChangedListeners</code> must be notified if the renderers value
     * changes
     * 
     * @param listener
     *            The value changed listener
     */
    public void addItemValueChangedListener(EJItemValueChangedListener listener)
    {
        _item.addItemValueChangedListener(listener);
    }
}
