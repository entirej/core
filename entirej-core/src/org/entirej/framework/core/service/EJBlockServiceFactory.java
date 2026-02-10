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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJBlockServiceFactory implements Serializable
{
    private static final Logger logger = LoggerFactory.getLogger(EJBlockServiceFactory.class);

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
            logger.debug("Creating block service: {}", serviceClassName);
            Class<?> serviceClass = Class.forName(serviceClassName);
            Object service = serviceClass.getDeclaredConstructor().newInstance();

            if (service instanceof EJBlockService<?> blockService)
            {
                return blockService;
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
        catch (ReflectiveOperationException e)
        {
            throw new EJApplicationException(new EJMessage("Unable to instanciate service class: " + serviceClassName), e);
        }
    }
}
