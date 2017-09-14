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
package org.entirej.framework.core.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Defines the use of a lookup within a form
 * <p>
 * This is basically a holder for a specific lookup definition. The lookup
 * definition is the actual lookup properties that holds all properties needed
 * for data retrieval and display. This class defines the lookup definition and
 * the columns from the lookup definition that should be displayed when the user
 * requests the lookup to be displayed
 */
public class EJCoreFormLookupProperties implements Serializable
{
    private String            _name;
    private String            _lookupDefinitionName;
    private ArrayList<String> _columnNames;
    
    EJCoreFormLookupProperties(String name, String lookupDefinitionName)
    {
        if (name == null || name.trim().length() == 0)
        {
            throw new NullPointerException("The name property of the FormsLookup cannot bu null.");
        }
        if (lookupDefinitionName == null || lookupDefinitionName.trim().length() == 0)
        {
            throw new NullPointerException("The lookup definition name property of the FormsLookup cannot bu null.");
        }
        
        _name = name;
        _lookupDefinitionName = lookupDefinitionName;
        _columnNames = new ArrayList<String>();
    }
    
    /**
     * Returns the form local name for the given lookup
     * 
     * @return the name given to this forms lookup
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * Returns the lookup definition name of this form lookup
     * 
     * @return the name of the lookup definition upon which this lookup is based
     */
    public String getLookupDefinitionName()
    {
        return _lookupDefinitionName;
    }
    
    /**
     * Adds a column name to this forms lookup list of columns
     * <p>
     * The column name is a column defined within the lookup definition file.
     * Only columns from the lookup definition can be added however there is no
     * check until runtime
     * <p>
     * 
     * @param columnName
     *            The column name from the lookup definition file
     * @throws NullPointerException
     *             if the columnName passed is either null or of zero length
     */
    public void addColumn(String columnName)
    {
        if (columnName == null || columnName.trim().length() == 0)
        {
            throw new NullPointerException("The columnName passed to addColumn() is either null or of zero length");
        }
        _columnNames.add(columnName);
    }
    
    /**
     * Retrieves the list of defined column within this form lookup. If no
     * columns have been added, then an empty <code>Collection</code> will be
     * returned
     * 
     * @return a <code>Collection</code> of column names contained within this
     *         forms lookup
     */
    public Collection<String> getColumnNames()
    {
        return _columnNames;
    }
    
}
