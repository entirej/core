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
package org.entirej.framework.core.data.controllers;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;

public class EJApplicationLevelParameter implements Serializable
{
    private List<ParameterChangedListener> _paramChangeListeners = new ArrayList<ParameterChangedListener>();
    
    private String           _name  = null;
    private Object           _value = null;
    private Class<?>         _dataTypeClass;
    
    
    public EJApplicationLevelParameter(String name, String dataTypeName)
    {
        if (dataTypeName == null)
        {
            EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_DATA_TYPE_PASSED_TO_METHOD, "ApplicationLevelParameter Constructor");
            throw new EJApplicationException(message);
        }
        
        _name = name;
        setDataTypeClass(dataTypeName);
    }
    
    public EJApplicationLevelParameter(String name, Class<?> dataType)
    {
        _name = name;
        _dataTypeClass = dataType;
    }
    
    public void setValue(Object value)
    {
        
        if (value == null)
        {
            _value = null;
            return;
        }
        
        if (!_dataTypeClass.isInstance(value))
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.INVALID_DATA_TYPE_FOR_APP_LEVEL_PARAMETER, _name, _dataTypeClass.getName(), value.getClass().getName()));
        }
        
        Object oldValue = getValue();
        _value = value;
        
        fireParameterChanged(oldValue, _value);
    }
    
    private void fireParameterChanged(Object oldValue, Object newValue)
    {
        for (ParameterChangedListener listener  : new ArrayList<ParameterChangedListener>(_paramChangeListeners))
        {
            listener.parameterChanged(getName(), oldValue, newValue);
        }
    }
    
    public String getName()
    {
        return _name;
    }
    
    public Object getValue()
    {
        return _value;
    }
    
    public Class<?> getDataType()
    {
        return _dataTypeClass;
    }
    
    private void setDataTypeClass(String dataTypeName)
    {
        try
        {
            _dataTypeClass = Class.forName(dataTypeName);
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(new EJMessage(e.getMessage()));
        }
    }
    
    public void addParameterChangedListener(ParameterChangedListener listener)
    {
       if (listener != null && !_paramChangeListeners.contains(listener))
       {
           _paramChangeListeners.add(listener);
       }
    }
    
    public void removeParameterChangedListener(ParameterChangedListener listener)
    {
        if (listener != null)
        {
            _paramChangeListeners.remove(listener);
        }
    }
    
    public static interface ParameterChangedListener
    {
        public void parameterChanged(String parameterName, Object oldValue, Object newValue);
    }
}
