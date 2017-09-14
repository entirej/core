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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.entirej.framework.core.enumerations.EJFrameworkMessage;

public class EJPojoHelper implements Serializable
{
    private HashMap<Class<?>, HashMap<String, MethodMapping>> _mappings = new HashMap<Class<?>, HashMap<String, MethodMapping>>();
    
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
    public void setFieldValue(String itemName, Object dataEntity, Object value)
    {
        if (itemName == null && dataEntity != null)
        {
            return;
        }
        
        if (_mappings.containsKey(dataEntity.getClass()))
        {
            MethodMapping mapping = _mappings.get(dataEntity.getClass()).get(itemName);
            if (mapping != null)
            {
                try
                {
                    mapping.getMethod().invoke(dataEntity, value);
                    return;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_CALL_METHOD,
                            mapping.getMethod().getName() + " on " + dataEntity.getClass().getName()), e);
                }
            }
        }
        
        addMapingAndValue(itemName, dataEntity, value);
    }
    
    private void addMapingAndValue(String itemName, Object dataEntity, Object value)
    {
        for (Method method : dataEntity.getClass().getMethods())
        {
            if (method.getName().startsWith("set"))
            {
                String fieldName = getFieldName(dataEntity.getClass(), method);
                if (itemName.equals(fieldName))
                {
                    try
                    {
                        method.invoke(dataEntity, value);
                        MethodMapping mapping = new MethodMapping(dataEntity.getClass(), method, itemName);
                        if (_mappings.containsKey(mapping.getType()))
                        {
                            _mappings.get(mapping.getType()).put(itemName, mapping);
                        }
                        else
                        {
                            HashMap<String, EJPojoHelper.MethodMapping> entry = new HashMap<String, EJPojoHelper.MethodMapping>();
                            entry.put(itemName, mapping);
                            _mappings.put(mapping.getType(), entry);
                        }
                        return;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_CALL_METHOD,
                                method.getName() + " on " + dataEntity.getClass().getName()), e);
                    }
                }
            }
        }
    }
    
    public static List<String> getFieldNames(Class<?> pojo)
    {
        ArrayList<String> fieldNames = new ArrayList<String>();
        if (pojo == null)
        {
            return fieldNames;
        }
        
        try
        {
            Set<String> items = new TreeSet<String>();
            for (Method method : pojo.getMethods())
            {
                EJFieldName annotation = method.getAnnotation(EJFieldName.class);
                if(annotation!=null )
                {
                    String fieldName = annotation.value();
                    if (fieldName != null && !fieldNames.contains(fieldName))
                    {
                        items.add(fieldName);
                    }  
                }
                
            }
            fieldNames.addAll(items);
           
            return fieldNames;
        }
        catch (Exception e)
        {
            throw new EJApplicationException(new EJMessage("Unable to retrieve annotations from Pojo: " + pojo.getName()), e);
        }
    }
    
    /**
     * 
     * @param pojo
     *            Retrieve the field name from this pojo
     * @param methodName
     *            The method name that contains the {@link EJFieldName}
     *            annotation
     * @return The field name
     * @throws EJApplicationException
     *             if there was a problem getting the annotation
     */
    public static String getFieldName(Class<?> pojo, String methodName)
    {
        if (pojo == null || methodName == null)
        {
            return null;
        }
        
        try
        {
            Method method = pojo.getMethod(methodName);
            
            
            return getFieldName(pojo, method);
        }
        catch (Exception e)
        {
            throw new EJApplicationException(new EJMessage("Unable to retrieve annotations from Pojo: " + pojo.getName() + " : " + methodName), e);
        }
    }
    
    /**
     * 
     * @param pojo
     *            Retrieve the field name from this pojo
     * @param methodName
     *            The method name that contains the {@link EJFieldName}
     *            annotation
     * @return The field name
     * @throws EJApplicationException
     *             if there was a problem getting the annotation
     */
    public static String getFieldName(Class<?> pojo, Method method)
    {
        if (pojo == null || method == null)
        {
            return null;
        }
        
        try
        {
            EJFieldName annotation = method.getAnnotation(EJFieldName.class);
            if(annotation!=null )
            {
                return annotation.value();
            }
           
            
           
            
            return null;
        }
        catch (Exception e)
        {
            throw new EJApplicationException(new EJMessage("Unable to retrieve annotations from Pojo: " + pojo.getName() + " : " + method.getName()), e);
        }
    }
    
    /**
     * 
     * @param pojo
     *            Retrieve all data types from this pojo. Data types are
     *            returned for all methods with an {@link EJFieldName}
     *            annotation
     * @return All data types
     * @throws EJApplicationException
     *             if there was a problem getting the annotation
     */
    public static ArrayList<Class<?>> getDataTypes(Class<?> pojo)
    {
        ArrayList<Class<?>> datatypes = new ArrayList<Class<?>>();
        if (pojo == null)
        {
            return datatypes;
        }
        
        try
        {
            for (Method method : pojo.getMethods())
            {
                
                EJFieldName annotation = method.getAnnotation(EJFieldName.class);
                if(annotation!=null )
                {
                    if (method.getReturnType().equals(void.class))
                    {
                        if (method.getParameterTypes().length == 1)
                        {
                            if (!datatypes.contains(method.getParameterTypes()[0]))
                            {
                                datatypes.add(method.getParameterTypes()[0]);
                            }
                        }
                    }
                    else
                    {
                        if (!datatypes.contains(method.getReturnType()))
                        {
                            datatypes.add(method.getReturnType());
                        }
                    }
                }
                
            }
            
            return datatypes;
        }
        catch (Exception e)
        {
            throw new EJApplicationException(new EJMessage("Unable to retrieve annotations from Pojo: " + pojo.getName()), e);
        }
    }
    
    /**
     * 
     * @param pojo
     *            Retrieve the data type from this pojo
     * @param fieldName
     *            The field name to search for. If the {@link EJFieldName} has
     *            been added to a setter then the first parameter value will be
     *            used.
     * @return The field name
     * @throws EJApplicationException
     *             if there was a problem getting the annotation
     */
    public Class<?> getDataType(Class<?> pojo, String fieldName)
    {
        if (pojo == null || fieldName == null)
        {
            return null;
        }
        
        try
        {
            for (Method method : pojo.getMethods())
            {
                
                EJFieldName annotation = method.getAnnotation(EJFieldName.class);
                if(annotation!=null && fieldName.equals(annotation.value()))
                {
                    if (method.getReturnType().equals(void.class))
                    {
                        if (method.getParameterTypes().length == 1)
                        {
                            return method.getParameterTypes()[0];
                        }
                    }
                    else
                    {
                        return method.getReturnType();
                    }
                }
                
            }
            
            return null;
        }
        catch (Exception e)
        {
            throw new EJApplicationException(new EJMessage("Unable to retrieve annotations from Pojo: " + pojo.getName()), e);
        }
    }
    
    public static Map<String, Class<?>> getGettersAndDatatypes(Class<?> inputType)
    {
        
        Map<String, Class<?>> propertyNames = new LinkedHashMap<String, Class<?>>();
        if (inputType == null)
        {
            return propertyNames;
        }
        
        String methodName = "";
        
        for (Method method : inputType.getMethods())
        {
            methodName = method.getName();
            
            // Retrieve all the getter methods
            if (methodName.startsWith("get") || methodName.startsWith("is"))
            {
                if (methodName.startsWith("get"))
                {
                    methodName = methodName.substring(3);
                }
                else
                {
                    methodName = methodName.substring(2);
                }
                
                // Only include the method if the getter method
                // has no input parameters
                if (method.getParameterTypes().length == 0)
                {
                    // Only getters that return a value are needed
                    if (method.getReturnType() != null)
                    {
                        Class<?> returnType = method.getReturnType();
                        // The type variable will end with ' ()'. This needs to
                        // be removed
                        
                        String fieldName = getFieldName(inputType, method);
                        if (fieldName != null && fieldName.trim().length() > 0)
                        {
                            // If I get this far then the method can be used
                            propertyNames.put(method.getName(), returnType);
                        }
                    }
                }
            }
        }
        Map<String, Class<?>> sotedPropertyNames = new LinkedHashMap<String, Class<?>>();
        SortedSet<String> keys = new TreeSet<String>(propertyNames.keySet());
        for (String key : keys) { 
           Class<?> value = propertyNames.get(key);
           sotedPropertyNames.put(key, value);
        }
        
        return sotedPropertyNames;
    }
    
    class MethodMapping
    {
        private Class<?> _type;
        private Method   _method;
        private String   _fieldName;
        
        public MethodMapping(Class<?> type, Method method, String fieldName)
        {
            _type = type;
            _method = method;
            _fieldName = fieldName;
        }
        
        public Class<?> getType()
        {
            return _type;
        }
        
        public Method getMethod()
        {
            return _method;
        }
        
        public String getFieldName()
        {
            return _fieldName;
        }
    }
    
}
