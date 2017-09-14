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
package org.entirej.framework.core;

import java.util.Collection;

import org.entirej.framework.core.internal.EJDefaultServicePojoHelper;

public interface EJQueryBlock
{
    public String getName();
    
    public Collection<EJBlockItem> getBlockItems();
    
    /**
     * Indicates if the block has an item with the given name
     * 
     * @param itemName
     *            the name to check for
     * @return <code>true</code> if the item exists within the block, otherwise
     *         <code>false</code>
     * 
     */
    public boolean containsItem(String itemName);
    
    public EJDefaultServicePojoHelper getServicePojoHelper();
    
    /**
     * Indicated if this block is part of a LOV Definition
     * 
     * @return <code>true</code> if this block is part of an LOV definition,
     *         otherwise <code>false</code>
     */
    public boolean isUsedInLovDefinition();
    
    /**
     * If this block is used within an LOV Definition then this method will
     * return the name of the LOV Definition
     * 
     * @return the name of the LOV Definition that this block is used in, or
     *         <code>null</code> if this block is not part of an LOV Definition
     */
    public String getLovDefinitionName();
    
    public EJBlockItem getBlockItem(String itemName);
    
    /**
     * Return the form to which this block belongs
     * 
     * @return This form to which this block belongs
     */
    public EJForm getForm();
}
