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
package org.entirej.framework.core;

import java.io.Serializable;

import org.entirej.framework.core.interfaces.EJConnectionFactory;
import org.entirej.framework.core.interfaces.EJFrameworkConnection;
import org.entirej.framework.core.properties.EJCoreProperties;

public class EJConnectionRetriever implements Serializable
{
    private boolean               _closed            = false;
    private EJFrameworkConnection _frameworkConnection;
    private EJFrameworkManager    _frameworkManager;
    private EJConnectionFactory   _connectionFactory = EJCoreProperties.getInstance().getConnectionFactory();
    
    EJConnectionRetriever(EJFrameworkManager manager)
    {
        _frameworkManager = manager;
    }
    
    public void close()
    {
        _closed = true;
        if (_frameworkConnection != null)
        {
            _frameworkConnection.close();
            _frameworkConnection = null;
        }
    }
    
    boolean isClosed()
    {
        return _closed;
    }
    
    public EJFrameworkManager getFrameworkManager()
    {
        return _frameworkManager;
    }
    
    synchronized EJFrameworkConnection getConnection()
    {
        if (_frameworkConnection == null)
        {
            _frameworkConnection = makeConnection();
        }
        return _frameworkConnection;
    }
    
    private EJFrameworkConnection makeConnection()
    {
        if (_connectionFactory == null)
        {
            // Don't pass the framework manager as a parameter as this could
            // cause a loop in the application if the message tries to retrieve
            // a managed connection which causes the same exception again and
            // again
            throw new EJApplicationException(new EJMessage("Unable to retrieve connection factory: "
                    + EJCoreProperties.getInstance().getConnectionFactoryClassName()));
        }
        
        return _connectionFactory.createConnection(_frameworkManager);
    }
}
