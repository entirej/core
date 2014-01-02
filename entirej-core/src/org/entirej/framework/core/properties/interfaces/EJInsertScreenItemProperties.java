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

public interface EJInsertScreenItemProperties extends EJScreenItemProperties
{
    
    /**
     * Indicates if this item is to be visible on the insert screen
     * <p>
     * 
     * @return <code>true</code> if the item should be visible, otherwise
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
     * Indicates that a value is required during insert operations
     * <p>
     * EntireJ will ensure that a value has been entered before issuing the
     * insert
     * 
     * @return The mandatory indicator
     */
    public boolean isMandatory();
    
    /**
     * Returns the name of the lov mapping assigned to this item
     * 
     * @return The name of the lov mapping assigned to this item or
     *         <code>null</code> if none was assigned
     */
    public String getLovMappingName();
    
    /**
     * Returns the action command defined for this item
     * 
     * @return This items action command
     */
    public String getActionCommand();
    
}
