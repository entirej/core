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
package org.entirej.framework.core.properties.definitions.interfaces;

import java.util.Collection;

public interface EJPropertyDefinitionList extends EJNotifiablePropertyDefinition
{
    /**
     * Returns the name of this property list
     * 
     * @return The list name
     */
    public String getName();
    
    /**
     * Sets this lists label
     * <p>
     * The label will be displayed on the properties sheet but not saved to the
     * form definition file
     * <p>
     * If no label has been set, then the name will be used as the label
     * 
     * @param label
     */
    public abstract void setLabel(String label);
    
    /**
     * Returns the label for this property list
     * <p>
     * If no label has been set for this list, then the name will be used as the
     * label
     * 
     * @return This lists label
     */
    public abstract String getLabel();
    
    /**
     * Returns the description for this group
     * 
     * @return This groups description
     */
    public abstract String getDescription();
    
    /**
     * Adds a <code>IPropertyDefinition</code> to this list
     * 
     * @param definition
     *            The definition to add
     */
    public abstract void addPropertyDefinition(EJPropertyDefinition definition);
    
    /**
     * Returns a list of all <code>IPropertyDefinition</code>s available within
     * this list
     * 
     * @return The <code>PropertyDefinition</code>s available within this list
     */
    public abstract Collection<EJPropertyDefinition> getPropertyDefinitions();
    
}
