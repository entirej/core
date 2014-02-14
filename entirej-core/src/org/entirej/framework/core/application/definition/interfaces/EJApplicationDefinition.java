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
package org.entirej.framework.core.application.definition.interfaces;

import org.entirej.framework.core.properties.EJCoreLayoutItem;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJPropertyDefinition;
import org.entirej.framework.core.properties.definitions.interfaces.EJPropertyDefinitionGroup;
import org.entirej.framework.core.properties.definitions.interfaces.EJPropertyDefinitionListener;

public interface EJApplicationDefinition
{
    /**
     * Returns the application properties defined by the specific GUI
     * implementation being used
     * <p>
     * The <code>IPropertyDefinitionGroup</code> returned by this method will be
     * used by the <b>EntireJ Framework Plugin</b> to display a list of
     * properties to the user. These values will then be saved to the form
     * definition file and later made available to the application by the
     * <b>EntireJ Framework</b>
     * 
     * @return The property definitions for the application or <code>null</code>
     *         if no properties are defined
     */
    public EJPropertyDefinitionGroup getApplicationPropertyDefinitionGroup();
    
    /**
     * Used to load properties with values, if the property has been defined
     * with <code>LoadValidValuesDynamically</code>
     * 
     * @param frameworkExtensionProperties
     *            The current properties for the extension
     * @param propertyDefinition
     *            The property definition for which values should be loaded
     */
    public void loadValidValuesForProperty(EJFrameworkExtensionProperties frameworkExtensionProperties, EJPropertyDefinition propertyDefinition);
    
    /**
     * Informs the renderer definition that the value for the given property has
     * been changed
     * <p>
     * This method will only be called if the property has had its
     * <code>NotifyWhenChanged</code> property set
     * 
     * @param propertyDefinitionListener
     *            A listener that interacts with the PropertyDefinition
     * @param properties
     *            The current properties for this renderer
     * @param propertyName
     *            The name of the property that was changed
     */
    public void propertyChanged(EJPropertyDefinitionListener propertyDefinitionListener, EJFrameworkExtensionProperties properties, String propertyName);
    
    /**
     * Return the definition properties for the application
     * 
     * @return
     */
    public String getApplicationManagerClassName();
    
    /**
     * Return the Supported layout component types  by the specific GUI
     * implementation being used
     * 
     * @return
     */
    public EJCoreLayoutItem.TYPE[] getSupportedLayoutTypes();
    
}
