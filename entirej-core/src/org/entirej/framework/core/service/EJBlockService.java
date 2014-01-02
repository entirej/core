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
import java.util.List;

import org.entirej.framework.core.EJForm;

public interface EJBlockService<E> extends Serializable
{
    /**
     * This service will query all entities that match the given query criteria
     * and return them as a <code>List</code>
     * <p>
     * Each entity within the list will made available within the EntireJ
     * Framework
     * <p>
     * The form passed is the form from which this method is called. The form
     * can be used to retrieve values that can be used within the query
     * <p>
     * The <code>{@link EJQueryCriteria}</code> contains the methods to know how
     * many records exist per page and what page should be returned. I the user
     * requests a page where no data is available then an empty list will be
     * returned
     * 
     * @param form
     *            The form from which this method is called
     * @param queryCriteria
     *            The query criteria to use for the query
     * 
     * @return A <code>List</code> of entity objects
     */
    public List<E> executeQuery(EJForm form, EJQueryCriteria queryCriteria);
    
    /**
     * Indicates if this service can retrieve data in pages
     * <p>
     * If the service retrieves its data in pages, then EntireJ will always ask
     * for one more record than the page size. This one extra record will
     * indicate to the framework that another page of data is available. If less
     * records are returned that the page size, then this will indicate that all
     * records have been retrieved and no more pages exist
     * <p>
     * The <code>{@link IQueryCriteria}</code> contains the methods to know how
     * many records exist per page and what page should be returned. I the user
     * requests a page where no data is available then an empty list will be
     * returned
     * 
     * @return <code>true</code> if this service retrieves data in pages,
     *         otherwise <code>false</code>
     */
    public boolean canQueryInPages();
    
    /**
     * This service will insert all new entities provided into the services
     * underlying data source
     * 
     * @param form
     *            The form from which this method is called
     * @param newEntities
     *            The new entities
     */
    public void executeInsert(EJForm form, List<E> newEntities);
    
    /**
     * This service will update all given entities in the services underlying
     * data source
     * 
     * @param form
     *            The form from which this method is called
     * @param modifiedEntities
     *            The entities to be updated
     */
    public void executeUpdate(EJForm form, List<E> modifiedEntities);
    
    /**
     * This service will delete the given entities from the services underlying
     * data source
     * 
     * @param form
     *            The form from which this method is called
     * @param entitiesToDelete
     *            The entities to delete
     */
    public void executeDelete(EJForm form, List<E> entitiesToDelete);
    
}
