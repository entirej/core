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

import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJItemProperties;
import org.entirej.framework.core.service.EJQueryCriteria;

/**
 * Contains all item properties. These properties should be used to define the
 * actions that are allowed to be performed upon this item and how the item
 * should look.
 */
public class EJCoreItemProperties implements EJItemProperties
{
    private EJCoreBlockProperties          _blockProperties;
    
    /**
     * This is the property retrieved via the pojos annotation for this item
     */
    private String                         _fieldName;
    
    private String                         _name;
    private String                         _dataTypeClassName;
    private Class<?>                       _dataTypeClass;
    private boolean                        _blockServiceItem             = false;
    private String                         _itemRendererName;
    private String                         _defaultInsertValue           = "";
    private String                         _defaultQueryValue            = "";
    private EJFrameworkExtensionProperties _rendererProperties           = null;
    
    private EJCoreLovMappingProperties     _lovMappingPropertiesOnMain   = null;
    private EJCoreLovMappingProperties     _lovMappingPropertiesOnInsert = null;
    private EJCoreLovMappingProperties     _lovMappingPropertiesOnUpdate = null;
    private EJCoreLovMappingProperties     _lovMappingPropertiesOnQuery  = null;
    
    public EJCoreItemProperties(EJCoreBlockProperties blockProperties)
    {
        this("ITEM1");
        _blockProperties = blockProperties;
    }
    
    public EJCoreItemProperties(String pName)
    {
        _name = pName;
    }
    
    public EJCoreBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }
    
    /**
     * Used to set the default value for this item, when a new record is being
     * created
     * <p>
     * The value will be copied whenever a new record is created. This can be
     * when the user wants to create a new record, but also a new query record.
     * If copied in a query record, the value will be a default value but can be
     * overwritten from the accessor
     * 
     * @param defaultInsertValue
     *            The default insert value
     */
    public void setDefaultInsertValue(String defaultInsertValue)
    {
        _defaultInsertValue = defaultInsertValue;
    }
    
    /**
     * Returns the value to be used as a default when inserting a new record.
     * This could be a block item a form parameter or an application parameter
     * 
     * @return The default insert value
     */
    public String getDefaultInsertValue()
    {
        return _defaultInsertValue;
    }
    
    /**
     * Sets the value that will be used as a default for this item when
     * executing a query
     * <p>
     * This value will be set to the blocks {@link EJQueryCriteria} before the
     * blocks query is executed. This value can be changed by the developer if
     * the query value is added to a query screen or by implementing one of the
     * action processor methods
     * <p>
     * This could be a block item a form parameter or an application parameter.
     * 
     * @param defaultQueryValue
     *            The default query value
     */
    public void setDefaultQueryValue(String defaultQueryValue)
    {
        _defaultQueryValue = defaultQueryValue;
    }
    
    /**
     * Returns the value to be used as a default when executing a query on the
     * block to which this item belongs
     * <p>
     * This could be a block item a form parameter or an application parameter
     * 
     * @return The default query value
     */
    public String getDefaultQueryValue()
    {
        return _defaultQueryValue;
    }
    
    /**
     * Returns the name of the item
     * <p>
     * If this is a base table item, meaning that it will receive its data
     * directly from the blocks data source, then the name of the item will also
     * be the name of the property within the blocks base object.
     * 
     * @return The name of this item
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * Returns the value for the annotation defined for this items corresponding
     * method in the block pojo
     * <p>
     * If no fieldName annotation has been defined for the method then, this
     * method will return the name of this item
     * 
     * @return
     */
    public String getFieldName()
    {
        return _fieldName;
        
    }
    
    /**
     * Sets this items field name
     * 
     * @param fieldName
     */
    public void setFieldName(String fieldName)
    {
        _fieldName = fieldName;
    }
    
    /**
     * The full name of an item is made up as follows:
     * <code>FormName.&lt;LovDefinitionName&gt;.BlockName.ItemName</code>
     * 
     * @return The full name of this item
     */
    public String getFullName()
    {
        StringBuffer thisName = new StringBuffer();
        thisName.append(_blockProperties.getFormProperties().getName());
        thisName.append(".");
        
        if (_blockProperties.isUsedInLovDefinition())
        {
            thisName.append(_blockProperties.getLovDefinition().getName());
            thisName.append(".");
        }
        
        thisName.append(_blockProperties.getName());
        thisName.append(".");
        thisName.append(this.getName());
        
        return thisName.toString();
    }
    
    /**
     * Returns the name of the block to which this item belongs
     * 
     * @return The name of the block to which this item belongs
     */
    public String getBlockName()
    {
        return _blockProperties.getName();
    }
    
    /**
     * Sets the name of this item
     * <p>
     * 
     * @return The name of this item
     */
    public void setName(String name)
    {
        _name = name;
    }
    
    /**
     * Used to set the data type of this item
     * 
     * @param dataTypeClassName
     *            The class name of the data type assigned to this item
     */
    public void setDataTypeClassName(String dataTypeClassName)
    {
        if (dataTypeClassName == null || dataTypeClassName.trim().length() == 0)
        {
            throw new IllegalArgumentException("The data type passed to ItemProperties cannot be null or a zero length String");
        }
        
        _dataTypeClassName = dataTypeClassName;
        
        try
        {
            _dataTypeClass = Class.forName(dataTypeClassName);
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalArgumentException("The data type passed to setDataType cannot be found on the class path", e);
        }
    }
    
    /**
     * Returns the data type of this item
     * 
     * @return The class name of this items data type
     */
    public String getDataTypeClassName()
    {
        return _dataTypeClassName;
    }
    
    public Class<?> getDataTypeClass()
    {
        return _dataTypeClass;
    }
    
    /**
     * Indicates if this item receives its value from the blocks service or if
     * the value is populated manually by the application developer
     * <p>
     * The default value for this item is true
     * 
     * @param blockServiceItem
     *            set to true if this is item receives its value from the blocks
     *            service
     * 
     * @see EJCoreItemProperties#getItemStatement()
     */
    public void setBlockServiceItem(boolean blockServiceItem)
    {
        
        _blockServiceItem = blockServiceItem;
    }
    
    /**
     * Returns true or false depending on whether this item receives its value
     * from the blocks service or if it is entered manually by the application
     * developer
     * 
     * @return True if this is item receives its value from the blocks service
     *         otherwise false
     */
    public boolean isBlockServiceItem()
    {
        return _blockServiceItem;
    }
    
    /**
     * Sets the name of the renderer to be used for this item
     * 
     * @param className
     *            The item renderers class name
     */
    public void setItemRendererName(String name)
    {
        _itemRendererName = name;
    }
    
    /**
     * Returns the name of the renderer to use for this item
     * 
     * @return This items renderer name
     */
    public String getItemRendererName()
    {
        return _itemRendererName;
    }
    
    /**
     * Returns the <code>RenderingProperties</code> that are required by the
     * <code>ItemRenderer</code>
     * 
     * @return The required item renderer properties for this item
     */
    public EJFrameworkExtensionProperties getItemRendererProperties()
    {
        return _rendererProperties;
    }
    
    /**
     * Sets the <code>RenderingProperties</code> that are required by the item
     * renderer
     * 
     * @param properties
     *            The required item renderer properties for this item
     */
    public void setItemRendererProperties(EJFrameworkExtensionProperties properties)
    {
        _rendererProperties = properties;
    }
    
    public EJCoreLovMappingProperties getLovMappingPropertiesOnMain()
    {
        return _lovMappingPropertiesOnMain;
    }
    
    public void setLovMappingNameOnMain(String lovMappingName)
    {
        _lovMappingPropertiesOnMain = _blockProperties.getLovMappingContainer().getLovMappingProperties(lovMappingName);
    }
    
    public EJCoreLovMappingProperties getLovMappingPropertiesOnInsert()
    {
        return _lovMappingPropertiesOnInsert;
    }
    public EJCoreLovMappingProperties getLovMappingPropertiesByName(String lovMapping)
    {
        return _blockProperties.getLovMappingContainer().getLovMappingProperties(lovMapping);
    }
    
    public void setLovMappingNameOnInsert(String lovMappingName)
    {
        _lovMappingPropertiesOnInsert = _blockProperties.getLovMappingContainer().getLovMappingProperties(lovMappingName);
    }
    
    public EJCoreLovMappingProperties getLovMappingPropertiesOnUpdate()
    {
        return _lovMappingPropertiesOnUpdate;
    }
    
    public void setLovMappingNameOnUpdate(String lovMappingName)
    {
        _lovMappingPropertiesOnUpdate = _blockProperties.getLovMappingContainer().getLovMappingProperties(lovMappingName);
    }
    
    public EJCoreLovMappingProperties getLovMappingPropertiesOnQuery()
    {
        return _lovMappingPropertiesOnQuery;
    }
    
    public void setLovMappingNameOnQuery(String lovMappingName)
    {
        _lovMappingPropertiesOnQuery = _blockProperties.getLovMappingContainer().getLovMappingProperties(lovMappingName);
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("\nItem: ");
        buffer.append(getName());
        buffer.append("\n    Position:        ");
        buffer.append("\n    Data Type        ");
        buffer.append(_dataTypeClassName);
        buffer.append("\n    BlockServiceItem:    ");
        buffer.append(_blockServiceItem);
        
        buffer.append("\n      ItemRendererProperties:\n");
        buffer.append(_rendererProperties);
        
        return buffer.toString();
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EJCoreItemProperties other = (EJCoreItemProperties) obj;
        if (_name == null)
        {
            if (other._name != null) return false;
        }
        else if (!_name.equals(other._name)) return false;
        return true;
    }
    
}
