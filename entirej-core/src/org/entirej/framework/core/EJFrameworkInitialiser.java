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

import org.entirej.framework.core.interfaces.EJApplicationManager;

public class EJFrameworkInitialiser
{
    public static EJApplicationManager initialiseFramework(String entireJPropertiesFileName)
    {
        try
        {
                EJFrameworkManager frameworkManager = new EJFrameworkManager(entireJPropertiesFileName);
                EJConnectionHelper.setEJFrameworkManager(frameworkManager,entireJPropertiesFileName);
                EJSystemConnectionHelper.setEJFrameworkManager(frameworkManager,entireJPropertiesFileName);
                return frameworkManager.getApplicationManager();
        }
        catch (Exception e)
        {
            throw new EJApplicationException(e);
        }
    }
}
