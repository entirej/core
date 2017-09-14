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


/**
 * A generic Connection class that can be implemented by the application
 * <p>
 * This connection class is implemented differently depending on what type of
 * application is being created and how the application handles the retrieval
 * and modification of data
 * <p>
 * Most EntireJ applications will be database applications and this class will
 * be a wrapper around the actual database connection. If however the
 * application retrieves data from a message queue or maybe a web service, then
 * this class could hold totally different information, information required by
 * the actual data services
 */
public interface EJFrameworkConnection extends Serializable
{
    /**
     * The connection object is the actual abject that is used by the data
     * service to manipulate data
     * <p>
     * If the application retrieves data from a database then the connection
     * object would probably be the actual java.sql.Connection. The actual
     * connection object is defined within the connection factory
     */
    public Object getConnectionObject(); 
    
    /**
     * Used to save any changes made using this connection
     */
    public void commit();
    
    /**
     * Used to undo any changes made using this connection
     */
    public void rollback();
    
    /**
     * Must be called when the transaction is completed
     * <p>
     * After calling this method, the connection object must not be used. This
     * is because the connection object contained will be closed
     * <p>
     * e.g. If the connection object is a database connection then the
     * connection will be closed and returned to the connection pool and
     * therefore, if the connection would be used, a SQL exception will be
     * thrown
     */
    public void close();
    
}
