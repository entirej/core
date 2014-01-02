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
package org.entirej.framework.core.service;

import java.io.Serializable;

public class EJQuerySort implements Serializable
{
    private String          _sort;
    private EJQuerySortType _sortType;
    
    public static EJQuerySort ASC(String sort)
    {
        return new EJQuerySort(sort, EJQuerySortType.ASCENDING);
    }
    
    /**
     * Returns a Descending Sort for the given property name
     * 
     * @param sort
     *            The sort or the column to be sorted
     * @return A Descending Sort with the given name
     */
    public static EJQuerySort DESC(String sort)
    {
        return new EJQuerySort(sort, EJQuerySortType.DESCENDING);
    }
    
    /**
     * Returns an Ascending Sort for the given property name
     * 
     * @param sort
     *            The sort or the name of the property that should be sorted
     * @return An Ascending Sort with the given name
     */
    private EJQuerySort(String sort, EJQuerySortType type)
    {
        _sort = sort;
        _sortType = type;
    }
    
    /**
     * Returns the Sort
     * <p>
     * The sort must be equivalent to the name of the column within the service
     * that should be sorted upon, or be an actual sort statement
     * 
     * @return The name of this Sort
     */
    public String getSort()
    {
        return _sort;
    }
    
    /**
     * The type of the Sort
     * 
     * @return This Sort type
     */
    public EJQuerySortType getType()
    {
        return _sortType;
    }
    
}
