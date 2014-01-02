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

import java.sql.Connection;
import java.sql.SQLException;

import org.entirej.framework.core.interfaces.EJFrameworkConnection;

public class EJDefaultFrameworkConnection implements EJFrameworkConnection
{
    private Connection _connection;

    public EJDefaultFrameworkConnection(Connection connection)
    {
        _connection = connection;
    }

    @Override
    public void close()
    {
        try
        {
            _connection.close();
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
            _connection.commit();
        }
        catch (SQLException e)
        {
            throw new EJApplicationException(e);
        }
    }

    @Override
    public Object getConnectionObject()
    {
        return _connection;
    }

    @Override
    public void rollback()
    {
        try
        {
            _connection.rollback();
        }
        catch (SQLException e)
        {
            throw new EJApplicationException(e);
        }
    }

}
