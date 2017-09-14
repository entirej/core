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
package org.entirej.framework.core.interfaces;

import java.io.Serializable;

import org.entirej.framework.core.data.controllers.EJItemLovController;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.internal.EJInternalForm;
import org.entirej.framework.core.properties.interfaces.EJItemProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.renderers.EJManagedItemRendererWrapper;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusListener;
import org.entirej.framework.core.renderers.eventhandlers.EJScreenItemValueChangedListener;
import org.entirej.framework.core.renderers.interfaces.EJItemRenderer;
import org.entirej.framework.core.renderers.registry.EJBlockItemRendererRegister;

public interface EJScreenItemController extends Serializable
{

    /**
     * Returns the name of this item
     * 
     * @return The name of this item
     */
    public String getName();

    /**
     * Returns the {@link EJScreenType} on which this item is displayed
     * 
     * @return The type of screen upon which this item is displayed
     */
    public EJScreenType getScreenType();

    /**
     * Return the form on which this item is placed
     * 
     * @return This items form
     */
    public EJInternalForm getForm();

    /**
     * Returns the block to which this item belongs
     * 
     * @return The block to which this item belongs
     */
    public EJInternalBlock getBlock();

    /**
     * Return this items lov controller
     * 
     * @return This items lov controller or <code>null</code> if this item has
     *         no lov defined
     */
    public EJItemLovController getItemLovController();

    /**
     * update current items lov mapping
     * 
     * @param lovMapping
     *            The name of the mapping to set
     * 
     */
    public void setItemLovMapping(String lovMapping);

    /**
     * Returns the properties of the block item that is referenced by this
     * screen item
     * 
     * @return The properties of the block item that is referenced by this item
     */
    public EJItemProperties getReferencedItemProperties();

    /**
     * Returns the screen item properties of this item
     * 
     * @return The properties of this screen item
     */
    public EJScreenItemProperties getProperties();

    /**
     * Initializes the renderer for this item
     * 
     * @throws EJItemRendererException
     */
    public void initialiseRenderer();

    /**
     * Returns the managed item renderer for this item
     * 
     * @return The managed item renderer
     */
    public EJManagedItemRendererWrapper getManagedItemRenderer();

    /**
     * Indicates that this item should gain focus
     */
    public void gainFocus();

    /**
     * Returns the item renderer for this item
     * 
     * @return The item renderer
     */
    public EJItemRenderer getItemRenderer();

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
    public void executeActionCommand();

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
    public void addItemFocusListener(EJItemFocusListener listener);

    /**
     * Removes an <code>EJItemFocusListener</code> from this renderer
     * 
     * @param listener
     *            The focus listener
     */
    public void removeItemFocusListener(EJItemFocusListener listener);

    /**
     * Adds an <code>ItemChangedListener</code> to this renderer
     * <p>
     * <code>ItemChangedListeners</code> must be notified if the renderers value
     * changes
     * 
     * @param listener
     *            The value changed listener
     */
    public void addItemValueChangedListener(EJScreenItemValueChangedListener listener);

    /**
     * Removes an <code>EJItemValueChangedListener</code> from this renderer
     * 
     * @param listener
     *            The value changed listener
     */
    public void removeItemValueChangedListener(EJScreenItemValueChangedListener listener);

    /**
     * Indicates that this items value has been modified
     * <p>
     * The item renderer will inform this item that its value has been changed.
     * EntireJ can then inform any registered action processors of the change
     * 
     * @param newValue
     *            The value after it was changed
     */
    public void itemValueChaged(Object newValue);

    /**
     * Indicates that this item has gained focus
     * <p>
     * The item renderer will inform this item that it has gained focus. EntireJ
     * can then inform all focus listeners of the event
     */
    public void itemFocusGained();

    /**
     * Indicates that this item has lost focus
     * <p>
     * The item renderer will inform this item that it has lost focus. EntireJ
     * can then inform all focus listeners of the event
     */
    public void itemFocusLost();

    /**
     * Indicates if this item is to be made visible
     * <p>
     * 
     * @return <code>true</code> if the item should be made visible, otherwise
     *         <code>false</code>
     */
    public abstract boolean isVisible();

    /**
     * Indicates if this screen item should be validated against the lov values
     * 
     * @return <code>true</code> if the item should be validated against the lov
     *         values otherwise <code>false</code>
     */
    public boolean validateFromLov();

    /**
     * Indicates of the screen item controlled by this controller is a spacer
     * item
     * 
     * @return <code>true</code> if this controller controls a spacer item,
     *         otherwise <code>false</code>
     */
    public boolean isSpacerItem();

    public void initialise(EJBlockItemRendererRegister blockItemRegister);

    public EJBlockItemRendererRegister getItemRendererRegister();
}
