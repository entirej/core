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
package org.entirej.framework.core.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EJStatementParameter implements Serializable
{
    private int                               _type;
    private int                               _position;
    private EJParameterType                   _parameterType;
    private Object                            _value;
    private String                            _fieldName;
    
    private static final Map<String, Integer> JAVA_TYPE_TO_DB = new ConcurrentHashMap<String, Integer>();
    static
    {
        JAVA_TYPE_TO_DB.put(String.class.getName(), Types.VARCHAR);
        JAVA_TYPE_TO_DB.put(Timestamp.class.getName(), Types.TIMESTAMP);
        JAVA_TYPE_TO_DB.put(Time.class.getName(), Types.TIME);
        JAVA_TYPE_TO_DB.put(Date.class.getName(), Types.DATE);
        JAVA_TYPE_TO_DB.put(BigDecimal.class.getName(), Types.DECIMAL);
        JAVA_TYPE_TO_DB.put(Integer.class.getName(), Types.INTEGER);
        JAVA_TYPE_TO_DB.put(Boolean.class.getName(), Types.BOOLEAN);
        JAVA_TYPE_TO_DB.put(Byte.class.getName(), Types.TINYINT);
        JAVA_TYPE_TO_DB.put(Short.class.getName(), Types.SMALLINT);
        JAVA_TYPE_TO_DB.put(Long.class.getName(), Types.BIGINT);
        JAVA_TYPE_TO_DB.put(Float.class.getName(), Types.REAL);
        JAVA_TYPE_TO_DB.put(Double.class.getName(), Types.DOUBLE);
        JAVA_TYPE_TO_DB.put(String.class.getName(), Types.VARCHAR);
    }
    
    public EJStatementParameter(EJParameterType paramType)
    {
        this(null, null, paramType, null);
    }
    
    public EJStatementParameter(Object value)
    {
        this(null, null, EJParameterType.IN, value);
    }
    
    public EJStatementParameter(String fieldName, EJParameterType paramType, Object value)
    {
        this(fieldName, null, paramType, value);
    }
    
    public EJStatementParameter(String fieldName, Class<?> type)
    {
        this(fieldName, type, EJParameterType.IN, null);
    }
    
    public EJStatementParameter(String fieldName, Class<?> type, EJParameterType paramType)
    {
        this(fieldName, type, paramType, null);
    }
    
    public EJStatementParameter(String fieldName, Class<?> type, Object value)
    {
        this(fieldName, type, EJParameterType.IN, value);
    }
    
    public EJStatementParameter(String fieldName, Class<?> type, EJParameterType paramType, Object value)
    {
        if (paramType == null)
        {
            throw new NullPointerException("The ParameterType parameter passed to StatementParameter cannot be null");
        }
        
        if (type != null)
        {
            setJdbcType(getSqlType(type));
        }
        else
        {
            setJdbcType(Types.OTHER);
        }
        
        setParameterType(paramType);
        setFieldName(fieldName);
        setValue(value);
    }
    
    public void setParameterType(EJParameterType type)
    {
        _parameterType = type;
    }
    
    public EJParameterType getParameterType()
    {
        return _parameterType;
    }
    
    public Object getValue()
    {
        return _value;
    }
    
    public void setFieldName(String fieldName)
    {
        _fieldName = fieldName;
    }
    
    public String getFieldName()
    {
        return _fieldName;
    }
    
    public void setPosition(int position)
    {
        _position = position;
    }
    
    public int getPosition()
    {
        return _position;
    }
    
    public void setValue(Object value)
    {
        _value = value;
    }
    
    public void setJdbcType(int type)
    {
        _type = type;
    }
    
    public int getJdbcType()
    {
        return _type;
    }
    
    public int getSqlType(Class<?> type)
    {
        String dataTypeName = type.getName();
        
        Integer dbtype = JAVA_TYPE_TO_DB.get(dataTypeName);
        if (dbtype != null) return dbtype.intValue();
        
        return Types.JAVA_OBJECT;
        
    }
}
