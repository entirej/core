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
import java.util.Map.Entry;

public interface EJRestriction<E> extends Serializable
{
    /**
     * Indicates if this restriction is for a service item
     * <p>
     * Restrictions can be set on service and control items. Service item restrictions can be assigned directly to the query of the block service however
     * non service item restrictions are to be retrieved by the service creator to make a programitical change to the service query depending on what has been
     * set for the non service restrictions
     *  
     * @return <code>true</code> if the restriction is for a service item, otherwise <code>false</code>
     */
    public boolean isServiceItemRestriction();
    
    /**
     * This indicates the name of the item within the block, to which this
     * restriction applies
     * 
     * @return The name of the block item to which this restriction applies
     */
    public String getBlockItemName();
    
    /**
     * This is the field name of the item to which this restriction applies
     * <p>
     * The field name is created from the Annotation within the POJo
     * 
     * @return The field name of the block item to which this restriction
     *         applies
     */
    public String getFieldName();
    
    /**
     * This sets the field name of this restriction
     * <p>
     * When the restriction is added to an {@link EJQueryCriteria} and the
     * blockItemName of the restriction exists within the
     * {@link EJQueryCriteria}'s block, then EntireJ will automatically copy the
     * fieldName of the block item into the restriction
     * 
     * @param fieldName
     *            The field name to set
     */
    public void setFieldName(String fieldName);
    
    /**
     * An Alias can be used by the block service if another name is required for
     * the given item. This is often used when using the
     * <code>{@link EJExpressionBuilder}</code>. The
     * <code>{@link EJExpressionBuilder}</code> builds a SQL statement using the
     * name of the <code>{@link EJRestriction}</code>. The name is converted
     * from the attribute name to a SQL name before the expression is made,
     * however, if the expression should contain, for example, a table alias,
     * then this must be added by using this method.
     * 
     * @param alias
     *            An alias for this restriction if required
     */
    public void setAlias(String alias);
    
    /**
     * Returns the alias set for this restriction or <code>null</code> if no
     * alias has been set
     * 
     * @return The alias which has been set or <code>null</code> if no alias has
     *         been set
     */
    public String getAlias();
    
    public EJRestrictionType getType();
    
    public E getValue();
    
    public E getBetweenLowerValue();
    
    public E getBetweenHigherValue();
    
    public List<E> getValueList();
    
    public String getCacheKey();
    
    public void add(EJRestrictionJoin join, EJRestriction<?> restriction);
    
    public List<Entry<EJRestrictionJoin, EJRestriction<?>>> getSubRestrictions();
}
