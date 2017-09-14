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
package org.entirej.framework.core.interfaces;

import java.io.Serializable;

import org.entirej.framework.core.EJFrameworkManager;

/**
 * the connection factory is implemented by the application developers and
 * returns an <code>IFrameworkConnection</code> that is used within the
 * DataServices of the EntireJ Framework
 */
public interface EJConnectionFactory extends Serializable
{
    /**
     * Creates a framework connection
     * <p>
     * The framework manager passed is the framework manager of the application.
     * The framework manager can be used to access application level parameters
     * that may be required within the connection factory.
     * 
     * @param frameworkManager
     *            The framework manager for the application
     */
    public EJFrameworkConnection createConnection(EJFrameworkManager frameworkManager);
}
