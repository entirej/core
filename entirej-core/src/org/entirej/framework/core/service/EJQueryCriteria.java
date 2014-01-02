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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.EJBlockItem;
import org.entirej.framework.core.EJLovBlock;
import org.entirej.framework.core.EJQueryBlock;
import org.entirej.framework.core.common.utils.EJDataHelper;
import org.entirej.framework.core.data.controllers.EJItemLovController;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJQueryCriteria extends EJStatementCriteria implements Serializable
{
    private static Logger           logger       = LoggerFactory.getLogger(EJQueryCriteria.class);
    
    private boolean                 _hasMoreRows = true;
    private int                     _maxResults  = -1;
    private boolean                 _queryAllRows;
    private int                     _pageSize;
    private int                     _pageNumber;
    private final List<EJQuerySort> _sort        = new ArrayList<EJQuerySort>();
    
    public EJQueryCriteria()
    {
        super(null);
        logger.trace("Creating new QueryCriteria for block null");
        
    }
    
    @Deprecated
    public EJQueryCriteria(EJInternalBlock block)
    {
        this(new EJLovBlock(block));
    }
    
    /**
     * Creates a <code>QueryCriteria</code> object for the given block
     * controller
     * 
     * @param blockController
     *            The block controller for which the query criteria should be
     *            used
     */
    public EJQueryCriteria(EJQueryBlock block)
    {
        super(block);
        
        logger.trace("Creating new QueryCriteria for block {}", (block == null ? "null" : block.getName()));
        if (block != null)
        {
            addDefaultQueryValues(null);
        }
    }
    
    private void addDefaultQueryValues(EJItemLovController itemLovController)
    {
        if (getBlock() == null)
        {
            return;
        }
        
        for (EJBlockItem item : getBlock().getBlockItems())
        {
            Object defaultValue = EJDataHelper.getDefaultQueryValue(getBlock().getForm(), item);
            if (defaultValue != null)
            {
                add(EJRestrictions.equals(item.getName(), defaultValue));
            }
        }
    }
    
    /**
     * Indicates the maximum amount of rows to be retrieved if the queryAllRows
     * flag has been set
     * 
     * @param maxResults
     *            The maximum amount of rows to retrieve
     */
    public void setMaxResults(int maxResults)
    {
        _maxResults = maxResults;
    }
    
    /**
     * Returns the maximum amount of rows that should be selected if the
     * queryAllRows flag has been set
     * 
     * @see #getQueryAllRows()
     * @return The maximum amount of rows to return
     */
    public int getMaxResults()
    {
        return _maxResults;
    }
    
    /**
     * Returns the page number of the page being retrieved
     * 
     * @return The page number being retrieved
     */
    public int getPageNumber()
    {
        return _pageNumber;
    }
    
    /**
     * Returns the amount of rows per page to be retrieved
     * 
     * @return The number of rows per page to be retrieved
     */
    public int getPageSize()
    {
        return _pageSize;
    }
    
    /**
     * Sets the page number to be retrieved
     * 
     * @param pageNumber
     *            The number of the page to be retrieved
     */
    public void setPageNumber(int pageNumber)
    {
        _pageNumber = pageNumber;
    }
    
    /**
     * Sets the size of each page to be retrieved
     * <p>
     * The size of the page is the number of records to be retrieved when
     * retrieving in pages
     * <p>
     * If the queryAllRows flag is set to <code>false</code> then this means
     * that pages of data should be retrieved
     * 
     * @param pageSize
     *            The size of the pages to retrieve
     */
    public void setPageSize(int pageSize)
    {
        _pageSize = pageSize;
    }
    
    /**
     * Indicates if paging can be ignored and all rows should be retrieved
     * 
     * @return <code>true</code> if all rows should be retrieved and paging
     *         should be ignored, otherwise <code>false</code>
     */
    public boolean queryAllRows()
    {
        return _queryAllRows;
    }
    
    /**
     * If set to <code>true</code> then this will mean that paging should not be
     * used and all rows should be retrieved
     * 
     * @param queryAllRows
     *            <code>true</code> if all rows should be retrieved, otherwise
     *            <code>false</code>
     */
    public void setQueryAllRows(boolean queryAllRows)
    {
        _queryAllRows = queryAllRows;
    }
    
    /**
     * Indicates if the BlockService has more pages of data
     * <p>
     * If the query is made in pages, then EntireJ needs to know if there are
     * more pages of data available. If the block service indicates that more
     * rows are available, then the framework will give the user the option to
     * navigate to the next page
     * 
     * @return <code>true</code> if the BlockService has will return more
     *         records if the framework requests the next page of data
     * 
     * @see EJQueryCriteria#setHasMoreRows(boolean)
     */
    public boolean hasMoreRows()
    {
        return _hasMoreRows;
    }
    
    /**
     * Sets the flag to indicate if the BlockService has a next page of data
     * 
     * @param hasMoreRows
     *            <code>true</code> if the block service will return more rows
     *            when a next page of data is requested, otherwise
     *            <code>false</code>
     * 
     * @see EJQueryCriteria#hasMoreRows()
     */
    public void setHasMoreRows(boolean hasMoreRows)
    {
        _hasMoreRows = hasMoreRows;
    }
    
    /**
     * Adds a {@link EJQuerySort} to this <code>QueryCriteria</code>
     * 
     * @param sort
     *            The sort to add
     */
    public void add(EJQuerySort sort)
    {
        if (sort != null)
        {
            _sort.add(sort);
        }
    }
    
    /**
     * Removes the specified EJQuerySort from this QueryCriteria
     * 
     * @param sort
     *            The EJQuerySort to remove
     */
    public void remove(EJQuerySort sort)
    {
        if (sort != null)
        {
            _sort.remove(sort);
        }
    }
    
    /**
     * Removes the EJQuerySort with the given name
     * 
     * @param The
     *            EJQuerySort to remove
     */
    public void removeSort(String sortName)
    {
        if (sortName != null)
        {
            Iterator<EJQuerySort> iti = _sort.iterator();
            while (iti.hasNext())
            {
                EJQuerySort order = iti.next();
                if (order.getSort().equalsIgnoreCase(sortName))
                {
                    iti.remove();
                    return;
                }
            }
        }
    }
    
    /**
     * Returns a <code>List</code> of all {@link EJQuerySort} within this
     * <code>EJQueryCriteria</code>
     * 
     * @return A list of all EJSort
     */
    public List<EJQuerySort> getSorts()
    {
        return _sort;
    }
    
    /**
     * Returns a <code>Sorter</code> for the given item name
     * 
     * The name of the <code>EJQuerySort</code> will be the name defined for the
     * block item
     * 
     * @param sort
     *            The sort to retrieve
     * @return The Sort with the given name or <code>null</code> if there is no
     *         Sort with the given name
     * 
     */
    public EJQuerySort getSort(String sort)
    {
        for (EJQuerySort s : _sort)
        {
            if (s.getSort().equalsIgnoreCase(sort))
            {
                return s;
            }
        }
        
        // there was no sort with the given name
        return null;
    }
    
}
