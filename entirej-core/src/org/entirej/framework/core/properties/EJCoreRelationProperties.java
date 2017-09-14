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

public class EJCoreRelationProperties implements Comparable<EJCoreRelationProperties>, Serializable
{
    private String                                  _name;
    private EJCoreBlockProperties                   _masterBlock;
    private EJCoreBlockProperties                   _detailBlock;
    private boolean                                 _preventMasterlessOperations = true;
    private boolean                                 _deferredQuery               = false;
    private boolean                                 _autoQuery                   = true;
    
    private ArrayList<EJCoreRelationJoinProperties> _joinList;
    
    public EJCoreRelationProperties(String name)
    {
        _name = name;
        _joinList = new ArrayList<EJCoreRelationJoinProperties>();
    }
    
    /**
     * @return the name of this relation
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * Masterless operations are operation that can be executed on the detail in
     * a master detail relationship
     * <p>
     * If the PreventMasterlessOperation is set to <code>true</code>, the
     * default value, then no operations (queries, inserts, deleted etc) can be
     * executed on a child if no parent exists
     * 
     * @param prevent
     */
    public void setPreventMasterlessOperations(boolean prevent)
    {
        _preventMasterlessOperations = prevent;
    }
    
    /**
     * Indicates if masterless operations are allowed on the detail block
     * 
     * @return <code>true</code> if masterless operations are allowed, otherwise
     *         <code>false</code>
     */
    public boolean preventMasterlessOperations()
    {
        return _preventMasterlessOperations;
    }
    
    /**
     * Indicates if this relations query should be deferred until either the
     * user executes the query or when the user navigates to the detail block
     * 
     * @param deferred
     *            <code>true</code> if the relations query is deferred,
     *            otherwise <code>false</code>
     */
    public void setDeferredQuery(boolean deferred)
    {
        _deferredQuery = deferred;
    }
    
    /**
     * Indicates if this relations query should be deferred until either the
     * user executes the query or when the user navigates to the detail block
     * 
     * @return deferred <code>true</code> if the relations query is deferred,
     *         otherwise <code>false</code>
     */
    public boolean isDeferredQuery()
    {
        return _deferredQuery;
    }
    
    /**
     * Indicates if the relation query should be executed automatically when the
     * user navigates to the block
     * <p>
     * The AutoQuery is used in conjunction with DefferedQuery
     * 
     * @return
     */
    public boolean isAutoQuery()
    {
        return _autoQuery;
    }
    
    /**
     * Sets the Auto Query option for this relation
     * 
     * @param query
     *            <code>true</code> if auto query should be enabled otherwise
     *            <code>false</code>
     */
    public void setAutoQuery(boolean query)
    {
        _autoQuery = query;
    }
    
    /**
     * Returns the properties of the master block within this relation
     * 
     * @return The properties of the master block
     */
    public EJCoreBlockProperties getMasterBlockProperties()
    {
        return _masterBlock;
    }
    
    /**
     * Sets the master block in this relation
     * 
     * @param properties
     *            the properties of the master block
     * @throws NullPointerException
     *             if the properties passed are null
     */
    public void setMasterBlockProperties(EJCoreBlockProperties properties)
    {
        if (properties == null)
        {
            throw new NullPointerException("The block properties passed to setMasterBlockProperties is null.");
        }
        _masterBlock = properties;
    }
    
    /**
     * Sets the detail block in this relation
     * 
     * @param properties
     *            the properties of the detail block
     * @throws NullPointerException
     *             if the properties passed are null
     */
    public void setDetailBlockProperties(EJCoreBlockProperties properties)
    {
        if (properties == null)
        {
            throw new NullPointerException("The block properties passed to setDetailBlockProperties is null.");
        }
        _detailBlock = properties;
    }
    
    /**
     * Returns the properties of the detail block within this relation
     * 
     * @return The properties of the detail block
     */
    public EJCoreBlockProperties getDetailBlockProperties()
    {
        return _detailBlock;
    }
    
    /**
     * Adds join items to this block relation
     * <p>
     * A join is made by linking an item in the detail block to an item within
     * the master block. This method is used to create this join by passing in
     * an item name from the master block and and item name from the detail
     * block. If the item names are valid then a join will be created. If the
     * link from master to detail blocks is made over more than one item, then a
     * join will be needed for each item.
     * 
     * @param masterItemName
     *            The name of the master item in the join
     * @param detailItemName
     *            The name of the detail item in the join
     */
    public void addJoin(String masterItemName, String detailItemName)
    {
        if (masterItemName == null || masterItemName.trim().length() == 0)
        {
            throw new NullPointerException("The masterItemName passed to addJoin is eithin null or of zero length.");
        }
        if (detailItemName == null || detailItemName.trim().length() == 0)
        {
            throw new NullPointerException("The detailItemName passed to addJoin is eithin null or of zero length.");
        }
        
        EJCoreItemProperties masterItem = _masterBlock.getItemProperties(masterItemName);
        
        if (masterItem != null)
        {
            EJCoreItemProperties detailItem = _detailBlock.getItemProperties(detailItemName);
            
            if (detailItem != null)
            {
                EJCoreRelationJoinProperties join = new EJCoreRelationJoinProperties(masterItem, detailItem);
                _joinList.add(join);
            }
            else
            {
                throw new IllegalArgumentException("The detail item name passed to addJoin doesn't exist within its block. Detail Item: " + detailItemName);
            }
        }
        else
        {
            throw new IllegalArgumentException("The master item name passed to addJoin doesn't exist within its block. Master Item: " + masterItemName);
        }
    }
    
    /**
     * Returns a collection of <code>RelationJoinProperties</code> used within
     * this relation
     * 
     * @return The collection containing the <code>RelationJoinProperties</code>
     */
    public Collection<EJCoreRelationJoinProperties> getRelationJoins()
    {
        return _joinList;
    }
    
    public int compareTo(EJCoreRelationProperties rel)
    {
        return _name.compareTo(rel == null ? "" : rel.getName());
    }
    
}
