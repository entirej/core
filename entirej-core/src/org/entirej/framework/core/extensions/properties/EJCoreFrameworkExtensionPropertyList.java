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
package org.entirej.framework.core.extensions.properties;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionPropertyList;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionPropertyListEntry;

public class EJCoreFrameworkExtensionPropertyList implements EJFrameworkExtensionPropertyList
{
    private LinkedList<EJFrameworkExtensionPropertyListEntry> _listEntries;
    private String                                           _name;

    public EJCoreFrameworkExtensionPropertyList(String name)
    {
        _name = name;
        _listEntries = new LinkedList<EJFrameworkExtensionPropertyListEntry>();
    }

    /**
     * Returns this lists name
     *
     * @return The name of this list
     */
    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    /**
     * Adds a new list entry to this list
     * <p>
     * The entry will only be added if it is not null
     *
     * @param entry
     *            The entry to be added
     */
    public void addListEntry(EJFrameworkExtensionPropertyListEntry entry)
    {
        if (entry != null)
        {
            _listEntries.add(entry);
        }
    }

    /**
     * Returns a list of all list entries
     *
     * @return The list entries
     */
    public List<EJFrameworkExtensionPropertyListEntry> getAllListEntries()
    {
        return _listEntries;
    }

    /**
     * removes a list entry from this list
     *
     * @param entry
     *            The list entry to remove
     */
    public void removeListEntry(EJFrameworkExtensionPropertyListEntry entry)
    {
        _listEntries.remove(entry);
    }

    /**
     * Removes all entries from this list
     */
    public void removeAllEntries()
    {
        _listEntries.clear();
    }

    /**
     * Indicates if a list entry exists
     *
     * @param entry
     *            The entry to check for
     * @return <code>true</code> if the entry exists, otherwise
     *         <code>false</code>
     */
    public boolean contains(EJFrameworkExtensionPropertyListEntry entry)
    {
        return _listEntries.contains(entry);
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("FrameworkExtensionPropertyList: ");
        buf.append(_name);
        buf.append("\n");

        Iterator<EJFrameworkExtensionPropertyListEntry> iti = _listEntries.iterator();
        while (iti.hasNext())
        {
            buf.append(iti.next().toString());
        }

        return buf.toString();
    }
}
