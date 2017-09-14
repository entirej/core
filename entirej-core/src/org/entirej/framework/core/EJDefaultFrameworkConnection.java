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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.entirej.framework.core.interfaces.EJFrameworkConnection;

public class EJDefaultFrameworkConnection implements EJFrameworkConnection
{
    private Connection _connection;
    private EJDefaultConnectionFactory _factory;
    
    private AtomicBoolean init = new AtomicBoolean(false); 

    public EJDefaultFrameworkConnection(EJDefaultConnectionFactory factory)
    {
        _factory = factory;
    }
    Connection getInternalConnection()
    {
        if(!init.get()){
            _connection = _factory.createInternalConnection();
            init.set(true);
        }
        return _connection;
    }
 
    @Override
    public void close()
    {
        try
        {
            if(init.get())
            {
                _connection.close();
            }
        }
        catch (SQLException e)
        {
            throw new EJApplicationException(e);
        }
    }
 
    @Override
    public void commit()
    {
        try
        {
            if(init.get())
            {
                _connection.commit();
            }
        }
        catch (SQLException e)
        {
            throw new EJApplicationException(e);
        }
    }
 
    @Override
    public Object getConnectionObject()
    {
        return getInternalConnection();
    }
 
    @Override
    public void rollback()
    {
        try
        {
            if(init.get())
            {
                _connection.rollback();
            }
        }
        catch (SQLException e)
        {
            throw new EJApplicationException(e);
        }
    }

}
