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

public interface EJInsertScreenRendererDefinition extends EJRendererDefinition
{
    /**
     * Indicates to the EntireJ Plugin that it can allow users to create spacer
     * items on this screen
     * <p>
     * Spacer items are used by users to help create complex screens
     * 
     * @return <code>true</code> if this screen renderer allows spacers,
     *         otherwise <code>false</code>
     */
    public boolean allowSpacerItems();
    
    /**
     * Returns the renderer properties that are defined for the insert screen
     * <p>
     * The <code>EJPropertyDefinitionGroup</code> returned by this method will
     * be used by the <b>EntireJ Framework Plugin</code> to display a list of
     * properties to the user. These values will then be saved to the form
     * definition file and later made available to the
     * <code>EJInsertScreenRenderer</code> by the <b>EntireJ Framework</b>
     * 
     * @return The property definitions defined for the insert screen or
     *         <code>null</code> if no properties are available
     */
    public EJPropertyDefinitionGroup getInsertScreenPropertyDefinitionGroup();
    
    /**
     * Returns the renderer properties that can be entered by the users for each
     * item contained within the insert screen
     * <p>
     * The <code>EJPropertyDefinitionGroup</code> returned by this method will
     * be used by the <b>EntireJ Framework Plugin</code> to display a list of
     * properties to the user. These values will then be saved to the form
     * definition file and later made available to the
     * <code>EJInsertScreenRenderer</code> by the <b>EntireJ Framework</b>
     * 
     * @return The property definitions for the insert screen items or
     *         <code>null</code> if no properties are defined
     */
    public EJPropertyDefinitionGroup getItemPropertyDefinitionGroup();
    
    /**
     * Returns the renderer properties that can be entered by the users for each
     * spacer item contained within the insert screen
     * <p>
     * The <code>EJPropertyDefinitionGroup</code> returned by this method will
     * be used by the <b>EntireJ Framework Plugin</code> to display a list of
     * properties to the user. These values will then be saved to the form
     * definition file and later made available to the
     * <code>EJInsertScreenRenderer</code> by the <b>EntireJ Framework</b>
     * 
     * @return The property definitions for the insert screen spacer items or
     *         <code>null</code> if no properties are defined
     */
    public EJPropertyDefinitionGroup getSpacerItemPropertyDefinitionGroup();
    
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
