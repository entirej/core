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
package org.entirej.framework.core.common.utils;


public class EJParameterChecker
{
    public static void checkNotNull(Object parameter, String methodName, String parameterName)
    {
        if (parameter == null)
        {
            throw new NullPointerException("\nThe "+parameterName+" paramater passed to "+methodName+" is null.");
        }
    }
    
    public static void checkNotZeroLength(String parameter, String methodName, String parameterName)
    {
        if (parameter == null)
        {
            throw new NullPointerException("\nThe "+parameterName+" parameter passed to "+methodName+" is null.");
        }
        if (parameter.trim().length() == 0)
        {
            throw new IllegalArgumentException("\nThe "+parameter+" parameter passed to "+methodName+" must not be zero length.");
        }
    }
    
    public static void checkNotPositive(int parameter, String methodName, String parameterName) 
    {
        if (parameter < 0)
        {
            throw new IllegalArgumentException("\nThe "+parameterName+" paramater passed to "+methodName+" must be greater than 0.");
        }
    }
}
