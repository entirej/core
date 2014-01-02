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
package org.entirej.framework.core.renderers.definitions.interfaces;

import org.entirej.framework.core.properties.definitions.interfaces.EJPropertyDefinitionGroup;
import org.entirej.framework.core.renderers.interfaces.EJBlockRenderer;

public interface EJBlockRendererDefinition extends EJRendererDefinition
{
    /**
     * Indicates if this block renderer requires a query screen
     * 
     * @return <code>true</code> if a query screen is required, otherwise
     *         <code>false</code>
     */
    public boolean useQueryScreen();
    
    /**
     * Indicates if this block renderer requires an insert screen
     * 
     * @return <code>true</code> if an insert screen is required, otherwise
     *         <code>false</code>
     */
    public boolean useInsertScreen();
    
    /**
     * Indicates if this block renderer requires an update screen
     * 
     * @return <code>true</code> if an update screen is required, otherwise
     *         <code>false</code>
     */
    public boolean useUpdateScreen();
    
    public EJQueryScreenRendererDefinition getQueryScreenRendererDefinition();
    
    public EJInsertScreenRendererDefinition getInsertScreenRendererDefinition();
    
    public EJUpdateScreenRendererDefinition getUpdateScreenRendererDefinition();
    
    /**
     * Indicates to the EntireJ Plugin that it can allow users to create spacer
     * items on the block renderer
     * <p>
     * Spacer items are used by users to help create complex screens.
     * 
     * @return <code>true</code> if this block renderer allows spacers,
     *         otherwise <code>false</code>
     */
    public boolean allowSpacerItems();
    
    /**
     * Indicates to the EtireJ Plugin that it can allow multiple item groups on
     * the main screen of the block
     * <p>
     * If the given renderer cannot split items up into different screen parts,
     * e.g a Table of items, then this method should return <code>false</code>
     * 
     * @return <code>true</code> if the block renderer allows items to be
     *         displayed on different item groups within the block, otherwise
     *         <code>false</code>
     */
    public boolean allowMultipleItemGroupsOnMainScreen();
    
    /**
     * Returns the renderer properties that need to be entered by the users
     * <p>
     * The <code>EJPropertyDefinitionGroup</code> returned by this method will
     * be used by the <b>EntireJ Framework Plugin</code> to display a list of
     * properties to the user. These values will then be saved to the form
     * definition file and later made available to the
     * <code>IItemRenderer</code> by the <b>EntireJ Framework</b>
     * 
     * @return The property definitions for this item or <code>null</code> if no
     *         properties are available
     */
    public EJPropertyDefinitionGroup getBlockPropertyDefinitionGroup();
    
    /**
     * It is possible that each block renderer require its item renderers to
     * have various properties set in order for the item renderer to work
     * correctly within the block. This method returns the properties group that
     * contains all required item properties
     * 
     * @return The properties the block defines for each item or
     *         <code>null</code> if no properties are defined
     */
    public EJPropertyDefinitionGroup getItemPropertiesDefinitionGroup();
    
    /**
     * Returns the renderer properties that can be entered by the users for each
     * spacer item contained within the main screen
     * <p>
     * The <code>EJPropertyDefinitionGroup</code> returned by this method will
     * be used by the <b>EntireJ Framework Plugin</code> to display a list of
     * properties to the user. These values will then be saved to the form
     * definition file and later made available to the
     * <code>{@link EJBlockRenderer}</code> by the <b>EntireJ Framework</b>
     * 
     * @return The property definitions for the main screen spacer items or
     *         <code>null</code> if no properties are defined
     */
    public EJPropertyDefinitionGroup getSpacerItemPropertiesDefinitionGroup();
    
    /**
     * Returns the renderer properties that can be entered by the users for each
     * item groups contained within the main screen
     * <p>
     * The <code>EJPropertyDefinitionGroup</code> returned by this method will
     * be used by the <b>EntireJ Framework Plugin</code> to display a list of
     * properties to the user. These values will then be saved to the form
     * definition file and later made available to the
     * <code>{@link EJBlockRenderer}</code> by the <b>EntireJ Framework</b>
     * 
     * @return The property definitions for the main screen item groups or
     *         <code>null</code> if no properties are defined
     */
    public EJPropertyDefinitionGroup getItemGroupPropertiesDefinitionGroup();
    
}
