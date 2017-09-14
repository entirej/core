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
package org.entirej.framework.core.properties.interfaces;

import java.io.Serializable;

import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.service.EJQueryCriteria;

public interface EJItemProperties extends Serializable
{
    /**
     * Returns the value to be used as a default when inserting new records.
     * This could be a block item a form parameter or an application parameter
     * 
     * @return The default insert value
     */
    public String getDefaultInsertValue();
    
    /**
     * Returns the value to be used as a default when executing a query. This
     * value will be set to the blocks {@link EJQueryCriteria} before the blocks
     * query is executed. This value can be changed by the developer if the
     * query value is added to a query screen or by implementing one of the
     * action processor methods
     * <p>
     * This could be a block item a form parameter or an application parameter.
     * 
     * @return The default value for
     */
    public String getDefaultQueryValue();
    
    /**
     * Returns the name of the item
     * <p>
     * If this is a base table item, meaning that it will receive its data
     * directly from the blocks data source, then the name of the item will also
     * be the name of the property within the blocks base object.
     * 
     * @return The name of this item
     */
    public String getName();
    
    /**
     * Returns the value for the annotation defined for this items corresponding
     * method in the block pojo
     * <p>
     * If no fieldName annotation has been defined for the method then, this
     * method will return the name of this item
     * 
     * @return
     */
    public String getFieldName();
    
    /**
     * The full name of an item is made up as follows:
     * <code>FormName.&lt;LovDefinitionName&gt;.BlockName.ItemName</code>
     * 
     * @return The full name of this item
     */
    public String getFullName();
    
    /**
     * Returns the name of the block to which this item belongs
     * 
     * @return The name of the block to which this item belongs
     */
    public String getBlockName();
    
    /**
     * Returns true or false depending on whether this item receives its value
     * from the blocks service or if it is entered manually by the application
     * developer
     * 
     * @return <code>true</code> if this is item receives its value from the
     *         blocks service, otherwise <code>false</code>
     */
    public boolean isBlockServiceItem();
    
    /**
     * Returns the name of the renderer to use for this item
     * 
     * @return This items renderer name
     */
    public String getItemRendererName();
    
    /**
     * Returns the data type of this item
     * 
     * @return The class name of this items data type
     */
    public String getDataTypeClassName();
    
    /**
     * Returns the Class of the data type of this item
     * 
     * @return The {@link Class} of this item
     */
    public Class<?> getDataTypeClass();
    
    /**
     * Returns the <code>RenderingProperties</code> that are required by the
     * <code>ItemRenderer</code>
     * 
     * @return The required item renderer properties for this item
     */
    public EJFrameworkExtensionProperties getItemRendererProperties();
    
}
