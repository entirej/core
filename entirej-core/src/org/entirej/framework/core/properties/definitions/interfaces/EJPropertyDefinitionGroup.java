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
package org.entirej.framework.core.properties.definitions.interfaces;

import java.util.Collection;

public interface EJPropertyDefinitionGroup
{
    
    /**
     * Returns the name of this group
     * 
     * @return The group name
     */
    public abstract String getName();
    
    /**
     * Sets this groups label
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
     * Returns the label for this group
     * <p>
     * If no label has been set for this group, then the name will be used as
     * the label
     * 
     * @return This groups label
     */
    public abstract String getLabel();
    
    
    /**
     * Returns the description for this group
     * 
     * @return This groups description
     */
    public abstract String getDescription();
    
    /**
     * Returns the full group name of this group
     * <p>
     * This name will include all master group names if this gtroup is contained
     * within a master group
     * 
     * @return The full group name of this definition group
     */
    public abstract String getFullGroupName();
    
    /**
     * Adds a <code>IPropertyDefinition</code> to this group
     * 
     * @param definition
     *            The definition to add
     */
    public abstract void addPropertyDefinition(EJPropertyDefinition definition);
    
    /**
     * Returns a list of all <code>IPropertyDefinition</code>s available within
     * this group
     * 
     * @return The <code>PropertyDefinition</code>s available within this group
     */
    public abstract Collection<EJPropertyDefinition> getPropertyDefinitions();
    
    /**
     * Adds a <code>IPropertyDefinitionList</code> to this group
     * 
     * @param definitionList
     *            The definition list to add
     */
    public abstract void addPropertyDefinitionList(EJPropertyDefinitionList definitionList);
    
    /**
     * Returns a list of all <code>IPropertyDefinitionList</code>s available
     * within this group
     * 
     * @return The <code>IPropertyDefinitionList</code>s available within this
     *         group
     */
    public abstract Collection<EJPropertyDefinitionList> getPropertyDefinitionLists();
    
    /**
     * Sets the parent group of this group
     * <p>
     * 
     * @param group
     */
    public void setParentGroup(EJPropertyDefinitionGroup group);
    
    /**
     * Adds a sub group to the group
     * 
     * @param group
     *            The sub group to add
     */
    public abstract void addSubGroup(EJPropertyDefinitionGroup group);
    
    /**
     * Returns all sub groups of this group
     * 
     * @return All sub groups
     */
    public abstract Collection<EJPropertyDefinitionGroup> getSubGroups();
    
}
