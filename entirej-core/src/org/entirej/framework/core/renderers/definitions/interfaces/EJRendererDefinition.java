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

import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJPropertyDefinition;
import org.entirej.framework.core.properties.definitions.interfaces.EJPropertyDefinitionListener;

public interface EJRendererDefinition
{
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
     * Returns the class name of this renderers definition
     * <p>
     * The renderer definition holds properties used by the <b>EntireJ</b>
     * development plugin, to display a list of renderer specific properties to
     * the user
     * 
     * @return The class name of this renderers definition
     */
    public String getRendererClassName();
}
