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

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJMessage;

public class EJBlockServiceFactory implements Serializable
{
    public EJBlockServiceFactory()
    {
    }
    
    public EJBlockService<?> createBlockService(String serviceClassName)
    {
        if (serviceClassName == null || serviceClassName.trim().length() == 0)
        {
            return null;
        }
        
        try
        {
            Class<?> serviceClass = Class.forName(serviceClassName);
            Object service = serviceClass.newInstance();
            
            if (service != null && service instanceof EJBlockService<?>)
            {
                return (EJBlockService<?>) service;
            }
            else
            {
                return null;
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(new EJMessage("Unable to find service class: " + serviceClassName), e);
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(new EJMessage("Unable to instanciate service class: " + serviceClassName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(new EJMessage("Illegal access exception when trying to access service class: " + serviceClassName), e);
        }
    }
}
