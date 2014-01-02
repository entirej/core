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
package org.entirej.framework.core.properties.interfaces;

import java.io.Serializable;

import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;

public interface EJScreenItemProperties extends Serializable
{
    /**
     * Returns the name of the data block item to which this item references
     * <p>
     * 
     * @return The name of the block item that this item references
     */
    public abstract String getReferencedItemName();
    
    /**
     * Indicates if this screen item is a spacer item
     * 
     * @return <code>true</code> if this item is a spacer item, otherwise
     *         <code>false</code>
     */
    public boolean isSpacerItem();
    
    /**
     * Returns the label defined for this block item
     * <p>
     * It is the <code>BlockRenderer</code> that decides if and how the items
     * label should be displayed
     * 
     * @return The label defined for this item
     * @see #getBaseLabel()
     */
    public String getLabel();
    
    /**
     * Returns the hint defined for this block item
     * <p>
     * It is the <code>ItemRenderer</code> that decides if and how the items
     * hint should be displayed
     * 
     * @return The label defined for this item
     */
    public String getHint();
    
    /**
     * The action command is a string identifier sent by the item renderer to
     * the action processor when, for example a button is pressed
     * <p>
     * Only item renderers that can actually send action commands will have an
     * action command property set. Whether or not the renderer can send action
     * commands is defined within the renderer definition
     * 
     * @see IActionProcessor The command the item renderer will send
     */
    public String getActionCommand();
    
    /**
     * gets the visual attribute properties that should be used for this item
     * <p>
     * If the visual attribute properties are set to <code>null</code> then the
     * item should be displayed using its default display properties
     * 
     * @return The {@link EJCoreVisualAttributeProperties} that should be used
     *         for this item
     */
    public EJCoreVisualAttributeProperties getVisualAttributeProperties();
    
    /**
     * Indicates if this item is to be made visible
     * <p>
     * 
     * @return <code>true</code> if the item should be made visible, otherwise
     *         <code>false</code>
     */
    public boolean isVisible();
    
    /**
     * Indicates if this item can be modified
     * <p>
     * 
     * @return <code>true</code> if the item should is editable, otherwise
     *         <code>false</code>
     */
    public boolean isEditAllowed();
    
    /**
     * If the block renderer requires no insert or update screen. Then it
     * probably means that users edit the data directly within the main screen.
     * If this is the case then it is possible to set a mandatory flag. The
     * mandatory flag means that the user must enter a value for the given item.
     * 
     * @return <code>true</code> if users must enter a value for this item,
     *         otherwise <code>false</code>
     */
    public boolean isMandatory();
    
    /**
     * Sets the enabled flag of Lov Notification
     * <p>
     * Lov Notification is automatically enabled for items with an lov attached.
     * However it is also possible to enable lov notification for non lov items.
     * If this is the case, then EJ will call the action processor to notify the
     * developer that the lov has been activated, but no LOV will be displayed.
     * The developer can then do what is required for the specific business
     * case, e.g. Check the value entered against a Business Service or call
     * another form etc
     * 
     * @param enable
     *            Enables lov notification
     */
    public void enableLovNotification(boolean enable);
    
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
    public boolean isLovNotificationEnabled();
    
    /**
     * Returns the name of the lov mapping assigned to this item
     * 
     * @return The name of the lov mapping assigned to this item or
     *         <code>null</code> if none was assigned
     */
    public String getLovMappingName();
    
    /**
     * Indicates if this screen item should be validated agains the lov values
     * 
     * @return <code>true</code> if the item should be validated against the lov
     *         values otherwise <code>false</code>
     */
    public boolean validateFromLov();
    
}
