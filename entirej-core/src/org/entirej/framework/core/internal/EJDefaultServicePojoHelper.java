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
package org.entirej.framework.core.internal;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.EJPojoHelper;
import org.entirej.framework.core.data.EJDataItem;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.interfaces.EJItemProperties;

public class EJDefaultServicePojoHelper implements Serializable
{
    private EJCoreBlockProperties _blockProperties;
    
    EJDefaultServicePojoHelper(EJCoreBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
    }
    
    /**
     * Creates a new service pojo object based on the generic interface of the
     * EJBlockService
     * 
     * @return A new service pojo or <b><code>null</code><b> if there is no
     *         service for this block
     */
    public Object createNewPojoFromService()
    {
        if (_blockProperties.getBlockService() == null)
        {
            return null;
        }
        
        Class<?> pojoClass = null;
        try
        {
            Type[] types = _blockProperties.getBlockService().getClass().getGenericInterfaces();
            ParameterizedType type = (ParameterizedType) types[0];
            
            Type typeArgument = type.getActualTypeArguments()[0];
            
            pojoClass = (Class<?>) typeArgument;
            
            return pojoClass.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(new EJMessage("Unable to instantiate pojo from service: " + pojoClass), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(new EJMessage("Unable to access pojo from service: " + pojoClass), e);
        }
    }
    
    /**
     * Sets the data entities value to the value specified
     * 
     * @param itemName
     *            The name of the item
     * @param dataEntity
     *            The service pojo defined within the blocks service
     * @param value
     *            The value to set
     */
    public void setValue(String itemName, Object dataEntity, Object value)
    {
        EJItemProperties itemProperties = _blockProperties.getItemProperties(itemName);
        if (itemProperties == null || (!itemProperties.isBlockServiceItem()))
        {
            return;
        }
        
        // Capitalize the first letter
        String firstLetter = itemName.substring(0, 1).toUpperCase();
        StringBuilder builder = new StringBuilder();
        String methodName = builder.append("set").append(firstLetter).append(itemName.substring(1)).toString();
        invokePojoMethod(dataEntity, methodName, itemProperties.getDataTypeClass(), value);
        
    }
    
    
    /**
     * Copy all values from the source pojo to the data items specified
     * 
     * @param items
     *            The items to be set
     * @param sourcePojo
     *            The pojo containing the values
     */
    public void copyValuesFromServicePojo(Collection<EJDataItem> items, Object servicePojo)
    {
        for (EJDataItem item : items)
        {
            if (!item.getProperties().isBlockServiceItem())
            {
                continue;
            }
            
            // Now initialise the data items with the values from the entity if
            // one exists
            
            // Capitalise the first letter
            String firstLetter = item.getName().substring(0, 1);
            firstLetter = firstLetter.toUpperCase();
            StringBuilder builder = new StringBuilder();
            String methodName = builder.append("get").append(firstLetter).append(item.getName().substring(1)).toString();
            
            // Get the items value from the method and set the data item
            Object value = invokePojoMethod(servicePojo, methodName, null);
            item.setValue(value);
        }
    }
    
    private Object invokePojoMethod(Object dataEntity, String methodName, Class<?> parameterType, Object ...parameterValue)
    {
        if (dataEntity == null)
        {
            return null;
        }
        
        try
        {
            if (parameterType == null)
            {
                Method method = dataEntity.getClass().getMethod(methodName);
                return method.invoke(dataEntity, parameterValue);
            }
            else
            {
                Method method = dataEntity.getClass().getMethod(methodName, parameterType);
                return method.invoke(dataEntity, parameterValue);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_CALL_METHOD,
                    methodName + " on " + dataEntity.getClass().getName() + " using parameterType: " + parameterType), e);
        }
    }

    /**
     * Returns a new entity object based on the generic interface of the
     * EJBlockService
     * 
     * @return A new Entity Object or <b><code>null</code><b> if there is no
     *         data service for this block
     * @throws EJApplicationException
     *             if there was a problem creating the new object
     */
    public Object createNewServicePojo(Object baseEntityObject)
    {
        try
        {
            Type[] types = baseEntityObject.getClass().getGenericInterfaces();
            ParameterizedType type = (ParameterizedType) types[0];
            Type typeArgument = type.getActualTypeArguments()[0];
            Class<?> pojoClass = (Class<?>) typeArgument;
            return pojoClass.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(new EJMessage("Unable to instantiate service pojo: " + baseEntityObject), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(new EJMessage("Unable to access service pojo: " + baseEntityObject), e);
        }
    }
    
    public void addFieldNamesToItems()
    {
        if (_blockProperties.getBlockService() == null)
        {
            return;
        }
        
        Type[] types = _blockProperties.getBlockService().getClass().getGenericInterfaces();
        ParameterizedType type = (ParameterizedType) types[0];
        
        Type typeArgument = type.getActualTypeArguments()[0];
        
        Class<?> pojoClass = (Class<?>) typeArgument;
        
        for (EJItemProperties item : _blockProperties.getAllItemProperties())
        {
            if (!item.isBlockServiceItem())
            {
                continue;
            }
            
            // Now set the field name within the properties if an annotation
            // exists for this item
            
            // Capitalize the first letter
            String firstLetter = item.getName().substring(0, 1);
            firstLetter = firstLetter.toUpperCase();
            StringBuilder builder = new StringBuilder();
            String methodName = builder.append("get").append(firstLetter).append(item.getName().substring(1)).toString();
            
            // Get the items value from the method and set the data item
            String annotation = EJPojoHelper.getFieldName(pojoClass, methodName);
            _blockProperties.getItemProperties(item.getName()).setFieldName(annotation);
        }
    }
    
}
