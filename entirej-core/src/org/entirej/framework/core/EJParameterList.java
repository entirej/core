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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.entirej.framework.core.data.controllers.EJFormParameter;

public class EJParameterList implements Serializable
{
    private HashMap<String, EJFormParameter> _parameterList;
    
    public EJParameterList()
    {
        _parameterList = new HashMap<String, EJFormParameter>();
    }
    
    public void addParameter(EJFormParameter parameter)
    {
        if (parameter != null)
        {
            _parameterList.put(parameter.getName(), parameter);
        }
    }
    
    public boolean contains(String name)
    {
        return _parameterList.containsKey(name);
    }
    
    public EJFormParameter getParameter(String name)
    {
        return _parameterList.get(name);
    }
    
    /**
     * Returns an immutable list of parameters contained within this list
     * <p>
     * If a new parameter is to be added, use the
     * {@link #addParameter(EJFormParameter)} method
     * 
     * @return An immutable list of parameters contained within this list
     */
    public Collection<EJFormParameter> getAllParameters()
    {
        return Collections.unmodifiableCollection(_parameterList.values());
    }
    
    /**
     * Returns an immutable list of parameter names contained within this list
     * <p>
     * If a new parameter is to be added, use the
     * {@link #addParameter(EJFormParameter)} method
     * 
     * @return An immutable list of parameter names contained within this list
     */
    public Collection<String> getAllParameterNames()
    {
        return Collections.unmodifiableCollection(_parameterList.keySet());
    }
    
}
