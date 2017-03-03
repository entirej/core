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
package org.entirej.framework.core.renderers.interfaces;

import java.util.List;

import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;

public interface EJItemRenderer extends EJRenderer
{
    /**
     * Indicates to the renderer that the item should be refreshed. This could
     * be just re-populating the items value, or refreshing all properties.
     * <p>
     * In the example of the ComboBox Item Renderer, this method could indicate
     * that the combo box's underlying lov should be refreshed and the values
     * re-populated
     * 
     */
    public void refreshItemRenderer();

    /**
     * Indicates to the item renderer that one of its properties has changed
     * 
     * @param propertyName
     *            The name of the property that has changed
     */
    public void refreshItemRendererProperty(String propertyName);

    /**
     * Used to initialize the renderer using the given controller
     * <p>
     * The <code>ItemController</code> contains all properties and framework
     * callback methods needed by the item renderer
     * 
     * @param item
     *            The item
     * @param screenItemProperties
     *            The screen upon which this item will be displayed
     */
    public void initialise(EJScreenItemController item, EJScreenItemProperties screenItemProperties);

    /**
     * Used to retrieve the item used to initialize this item renderer
     * 
     * @return The renderers item
     */
    public EJScreenItemController getItem();

    /**
     * Used to clear the renderers value
     */
    public void clearValue();

    /**
     * Returns the value of the renderer
     * 
     * @return The renderers current value
     */
    public Object getValue();

    /**
     * If this item has a renderer and it is a renderer that can have multiple
     * valid values, e.g. a list box, then this method will return a list of all
     * these values.
     * 
     * @return The list of valid values allowable for this item
     */
    public List<Object> getValidValues();

    /**
     * Returns the display value of the renderer
     * <p>
     * If this is a combo box then the get display value will return the
     * description shown in the combo box and not the actual return value
     * 
     * @return The value displayed within this renderer
     */
    public String getDisplayValue();

    /**
     * Sets the initial value of this item
     * <p>
     * Setting the initial value should not fire an
     * <code>ItemValueChanged</code> event
     * 
     * @param value
     *            The renderers initial value
     */
    public void setInitialValue(Object value);

    /**
     * Sets this renderers value
     * <p>
     * Setting the renderers value will cause an <code>ItemValueChanged</code>
     * event to be fired
     * 
     * @param value
     *            The renderers new value
     */
    public void setValue(Object value);

    /**
     * Sets a message on this item
     * <p>
     * A border and icon will be displayed depending on the message level of the
     * given mesage
     * 
     * @param message
     *            The message to set
     */
    public void setMessage(EJMessage message);

    /**
     * clear any message from this item
     */
    public void clearMessage();

    /**
     * Informs the renderer if a validation error occurred
     * <p>
     * The renderer will be informed if a validation error occurred or if the
     * validation was successful. With the case of an erroneous validation
     * <code>true</code> will be passed. If validation was successful
     * <code>true</code> will be passed
     */
    public void validationErrorOccurred(boolean error);

    /**
     * Checks to see if the renderers value equals a value given
     * 
     * @param value
     *            The value for the equality check
     * @return <code>true</code> if the values are the same, <code>false</code>
     *         if they are not
     */
    public boolean valueEqualsTo(Object value);

    /**
     * Sets the edit allowed flag for this renderer
     * 
     * @param editAllowed
     *            The edit allowed flag
     */
    public void setEditAllowed(boolean editAllowed);

    /**
     * Indicates if this renderers value can be modified
     * 
     * @return <code>true</code> if the value can be modified otherwise
     *         <code>false</code>
     */
    public boolean isEditAllowed();

    /**
     * Indicates to the <code>ItemRenderer</code> that the item is to be made
     * mandatory
     * <p>
     * How the renderer actually indicates that the item is mandatory, is up to
     * the renderer. This could be for example a red border around the item or a
     * special base colour etc.
     * 
     * @param mandatory
     *            The mandatory flag
     */
    public void setMandatory(boolean mandatory);

    /**
     * Indicates that the item renderer should enable itself for LOV activation
     * <p>
     * If the block item is a list of values item then it can be enabled on each
     * screen to perform list of values operations. This could be either
     * displaying a special icon, allowing double clicks within the item etc.
     * <p>
     * The exact implementation of list of values enabling depends on the GUI
     * framework being implemented
     * 
     * @param enable
     *            Enable LOV activation for this item
     */
    public void enableLovActivation(boolean enable);

    /**
     * Indicates if this renderer should be made visible or not
     * 
     * @param visible
     *            The visible flag
     */
    public void setVisible(boolean visible);

    /**
     * Used to assign a set of visual attribute properties to this item
     * 
     * @param visualAttributeProperties
     *            The visual attribute properties to set
     * @throws EJItemRendererException
     */
    public void setVisualAttribute(EJCoreVisualAttributeProperties visualAttributeProperties);

    /**
     * Used to retrieve the visual attribute properties from this item
     * 
     * @return The {@link EJCoreVisualAttributeProperties} assigned to this item
     *         or <code>null</code> if none has been assigned
     */
    public EJCoreVisualAttributeProperties getVisualAttributeProperties();

    /**
     * Indicates if this renderer is visible
     * 
     * @return The visible flag
     */
    public boolean isVisible();

    /**
     * Indicates if this renderer is mandatory
     * 
     * @return The mandatory flag
     */
    public boolean isMandatory();

    /**
     * Indicates if this item's value is valid
     * <p>
     * EntireJ will inform all item renderers if a validation error occurs when
     * validating its value. If a validation error occurs or the item is
     * mandatory but no value has been entered, then this method must return
     * <code>false</code>. If no validation error occurred and the item has a
     * value if required, then this method must return <code>true</code>
     * 
     * @return <code>true</code> if the item is valid, otherwise
     *         <code>false</code>
     * @throws EJItemRendererException
     *             thrown if there was an error during this method
     */
    public boolean isValid();

    /**
     * Indicates to the item renderer that it should gain focus
     */
    public void gainFocus();

    /**
     * Sets the name of the block item for which this renderer is responsible
     * for
     * <p>
     * <b>This method is for internal use only and should not be used by any
     * classes outside of the EntireJ Framework Core</b>
     * 
     * @param name
     *            The name of the block item
     */
    public void setRegisteredItemName(String name);

    /**
     * Returns the name of the block item for which this renderer is responsible
     * 
     * @return The name of the block item for which this renderer is responsible
     */
    public String getRegisteredItemName();

    /**
     * Sets this items label
     * 
     * @param label
     *            The label to set
     */
    public void setLabel(String label);

    /**
     * Sets this items hint
     * 
     * @param hint
     *            The hint to set
     */
    public void setHint(String hint);

    /**
     * Indicates if this renderer is read-only
     * 
     * @return <code>true</code> if renderer is read-only, otherwise
     *         <code>false</code>
     */
    public boolean isReadOnly();
}
