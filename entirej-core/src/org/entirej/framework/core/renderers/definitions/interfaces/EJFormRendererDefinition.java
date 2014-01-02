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

public interface EJFormRendererDefinition extends EJRendererDefinition
{
    /**
     * Returns the renderer properties that need to be entered by the users
     * <p>
     * The <code>EJPropertyDefinitionGroup</code> returned by this method will
     * be used by the <b>EntireJ Framework Plugin</b> to display a list of
     * properties to the user. These values will then be saved to the form
     * definition file and later made available to the
     * <code>EJItemRenderer</code> by the <b>EntireJ Framework</b>
     * 
     * @return The property definitions for this item or <code>null</code> if no
     *         properties are available
     */
    public EJPropertyDefinitionGroup getFormPropertyDefinitionGroup();
}
