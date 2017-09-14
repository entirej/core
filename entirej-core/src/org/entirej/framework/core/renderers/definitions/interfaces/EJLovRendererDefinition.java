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
package org.entirej.framework.core.renderers.definitions.interfaces;

import org.entirej.framework.core.properties.definitions.interfaces.EJPropertyDefinitionGroup;

public interface EJLovRendererDefinition extends EJRendererDefinition
{
    /**
     * Indicates to the EntireJ Plugin that it can allow users to create spacer
     * items on this lov
     * <p>
     * Spacer items are used by users to help create complex lov screens
     * 
     * @return <code>true</code> if this lov renderer allows spacers, otherwise
     *         <code>false</code>
     */
    public boolean allowSpacerItems();
    
    /**
     * Returns the renderer properties that need to be entered by the users
     * <p>
     * The <code>EJPropertyDefinitionGroup</code> returned by this method will
     * be used by the <b>EntireJ Framework Plugin</code> to display a list of
     * properties to the user. These values will then be saved to the form
     * definition file and later made available to the
     * <code>EJLovRenderer</code> by the <b>EntireJ Framework</b>
     * 
     * @return The property definitions for this item or <code>null</code> if no
     *         properties are available
     */
    public EJPropertyDefinitionGroup getLovPropertyDefinitionGroup();
    
    /**
     * It is possible that each lov renderer requires its item renderers to have
     * various properties set in order for the item renderer to work correctly
     * within the lov. This method returns the properties group that contains
     * all required item properties
     * 
     * @return The lov renderer defined properties for each item or
     *         <code>null</code> if no properties are available
     */
    public EJPropertyDefinitionGroup getItemPropertiesDefinitionGroup();
    
    /**
     * It is possible for an LOV renderer to allow the display of spacer item in
     * order to ease the creation of complex screens. This method returns the
     * definition group for a spacer item or <code>null</code> if there is no
     * spacers allowed on this lov
     * 
     * @return The lov renderer defined properties for each spacer item or
     *         <code>null</code> if no spacer items are allowed
     */
    public EJPropertyDefinitionGroup getSpacerItemPropertiesDefinitionGroup();
    
    /**
     * Returns the renderer properties that can be entered by the users for each
     * item groups contained within the main screen
     * <p>
     * The <code>IPropertyDefinitionGroup</code> returned by this method will be
     * used by the <b>EntireJ Framework Plugin</code> to display a list of
     * properties to the user. These values will then be saved to the form
     * definition file and later made available to the
     * <code>{@link EJBlockRenderer}</code> by the <b>EntireJ Framework</b>
     * 
     * @return The property definitions for the main screen item groups or
     *         <code>null</code> if no properties are defined
     */
    public EJPropertyDefinitionGroup getItemGroupPropertiesDefinitionGroup();
    
    /**
     * Indicates that the lov renderer requires that all rows in the lov
     * definition block be retrieved
     * <p>
     * If this is not set then it is possible for the records to be retrieved in
     * pages
     * 
     * @return <code>true</code> if all records must be retrieved otherwise
     *         <code>false</code>
     */
    public boolean requiresAllRowsRetrieved();
    
    /**
     * Indicates if the lov renderer allows the user to use the standard query
     * pane as with standard blocks
     * 
     * @return <code>true</code> if the user can use a standard query, otherwise
     *         <code>false</code>
     */
    public boolean allowsUserQuery();
    
    /**
     * If the lov renderer allows a user query, then a
     * <code>{@link EJQueryScreenRendererDefinition}</code> is required. This
     * will be used to display the correct properties and viewer within the EJ
     * Plugin
     * 
     * @return The <code>{@link EJQueryScreenRendererDefinition}</code> for this
     *         Lov Renderer or <code>null</code> if the lov does not allow a
     *         user query
     */
    public EJQueryScreenRendererDefinition getQueryScreenRendererDefinition();
    
    /**
     * Indicates if the lov renderer requires an automatic query when the lov
     * window is displayed
     * <p>
     * Automatic queries are executed as the lov is opened
     * 
     * @return <code>true</code> if the renderer requires an automatic query
     *         otherwise <code>false</code>
     */
    public boolean executeAutomaticQuery();
}
