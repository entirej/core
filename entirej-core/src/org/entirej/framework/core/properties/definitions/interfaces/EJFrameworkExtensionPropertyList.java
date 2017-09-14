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

import java.io.Serializable;
import java.util.List;

public interface EJFrameworkExtensionPropertyList extends Serializable
{
    /**
     * Returns the name of this property list
     * 
     * @return This lists name
     */
    public String getName();
    
    /**
     * Sets the name if this property list
     * 
     * @param newName
     *            The new name to set
     */
    public void setName(String newName);
    
    /**
     * Adds a new list entry to this list
     * <p>
     * The entry will only be added if it is not null
     * 
     * @param entry
     *            The entry to be added
     */
    public void addListEntry(EJFrameworkExtensionPropertyListEntry entry);
    
    /**
     * Returns a list of all list entries
     * 
     * @return The list entries
     */
    public List<EJFrameworkExtensionPropertyListEntry> getAllListEntries();
    
    /**
     * removes a list entry from this list
     * 
     * @param entry
     *            The list entry to remove
     */
    public void removeListEntry(EJFrameworkExtensionPropertyListEntry entry);
    
    /**
     * Removes all entries from this list
     */
    public void removeAllEntries();
    
    /**
     * Indicates if a list entry exists
     * 
     * @param entry
     *            The entry to check for
     * @return <code>true</code> if the entry exists, otherwise
     *         <code>false</code>
     */
    public boolean contains(EJFrameworkExtensionPropertyListEntry entry);
}
