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

public class EJStoredProcedureStatementParameter extends EJStatementParameter
{
    public EJStoredProcedureStatementParameter(EJParameterType paramType)
    {
        this(null, paramType, null);
    }
    
    public EJStoredProcedureStatementParameter(Object value)
    {
        this(null, EJParameterType.IN, value);
    }
    
    public EJStoredProcedureStatementParameter(EJParameterType paramType, Object value)
    {
        this(null, paramType, value);
    }
    
    public EJStoredProcedureStatementParameter(Class<?> type)
    {
        this(type, EJParameterType.IN, null);
    }
    
    public EJStoredProcedureStatementParameter(Class<?> type, EJParameterType paramType)
    {
        this(type, paramType, null);
    }
    
    public EJStoredProcedureStatementParameter(Class<?> type, Object value)
    {
        this(type, EJParameterType.IN, value);
    }
    
    public EJStoredProcedureStatementParameter(Class<?> type, EJParameterType paramType, Object value)
    {
        super(null, type, paramType, value);
    }
}
