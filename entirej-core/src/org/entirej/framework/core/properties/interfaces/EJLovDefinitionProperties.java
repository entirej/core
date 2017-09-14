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
package org.entirej.framework.core.properties.interfaces;

import java.io.Serializable;

import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;

public interface EJLovDefinitionProperties extends Serializable
{
    
    /**
     * Returns the name of this LovDefinition
     * 
     * @return
     */
    public String getName();
    
    /**
     * Returns the name of the lov definition upon which this definition is
     * based
     * 
     * @return The name of the referenced lov definition name
     */
    public String getReferencedLovDefinitionName();
    
    /**
     * The Action Processor is responsible for actions within this lov
     * <p>
     * Actions can include buttons being pressed, check boxes being selected or
     * pre-post query methods etc.
     * 
     * @return The name of the Action Processor responsible for this lov
     */
    public String getActionProcessorClassName();
    
    /**
     * Indicates if this LovDefinition references a re-usable definition
     * 
     * @return <code>true</code> if references otherwise <code>false</code>
     */
    public boolean isReferenced();
    
    /**
     * If set to <code>true</code> then the lov definition can use the blocks
     * defined query option
     * <p>
     * This could include showing a query screen for hte user to reduce the
     * number of records within the lov thus simplifying the selection of the
     * correct value
     * 
     * @return true if query operations are allowed otherwise false
     */
    public boolean isUserQueryAllowed();
    
    /**
     * The name of the renderer used for the display of this lov
     * <p>
     * All renderers are defined within the <b>EntireJ Properties</b>
     * 
     * @return The renderer for this lov
     */
    public String getLovRendererName();
    
    /**
     * Returns the rendering properties for this lov definition
     * <p>
     * These properties are not used internally within the EntireJ Core
     * Framework but can be used by the applications rendering engine to display
     * the lov
     * <p>
     * 
     * @return The lov rendering properties
     */
    public EJFrameworkExtensionProperties getLovRendererProperties();
    
    /**
     * Returns the <code>BlockProperties</code> for this lov definition
     * 
     * @return the lov definition block properties
     */
    public EJBlockProperties getBlockProperties();
    
}
