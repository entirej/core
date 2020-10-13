/*******************************************************************************
 * Copyright 2013 CRESOFT AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors: CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.entirej.framework.core.interfaces.EJFrameworkConnection;

public class EJManagedFrameworkConnection implements EJFrameworkConnection
{

    private static Map<String, Long> TRACE_MAP = new HashMap<>();
    private final boolean               _initialiser ;
    private EJConnectionRetriever _connectionRetriever;

    EJManagedFrameworkConnection(EJConnectionRetriever connectionRetriever, boolean initialiser)
    {
        _connectionRetriever = connectionRetriever;
        _initialiser = initialiser;
        if (initialiser)
        {

            TRACE_MAP.put(this.toString(), System.currentTimeMillis());
            _connectionRetriever.setClosed(false);
        }
    }
    
    
    static {
        
        debugTrace();
    }
    
    public static void debugTrace()
    {
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            
            @Override
            public void run()
            {
               
                HashMap<String, Long> data = new HashMap<>(TRACE_MAP);
                Set<Entry<String,Long>> entrySet = data.entrySet();
                long currentTimeMillis = System.currentTimeMillis();
                for (Entry<String, Long> entry : entrySet)
                {
                    long time = currentTimeMillis - entry.getValue();
                    if(time > 5000) {
                        System.err.println("TRACE EJManagedFrameworkConnection :"+entry.getKey()+" open for "+time/1000+"s");
                    }
                }
            }
        }, 10000, 10000);
    }

    public void commit()
    {
        
        if (_initialiser)
        {
            TRACE_MAP.remove(this.toString());
            _connectionRetriever.getConnection().commit();
        }
    }

    public Object getConnectionObject()
    {
        // _connectionRetriever.setCloses(false);
        return _connectionRetriever.getConnection().getConnectionObject();
    }

    public void rollback()
    {
        if (_initialiser)
        {
            _connectionRetriever.getConnection().rollback();
        }
    }

    public void close()
    {

        if (_initialiser)
        {


            TRACE_MAP.remove(this.toString());
            try
            {
                _connectionRetriever.getConnection().commit();
            }
            finally
            {
                _connectionRetriever.close();
            }

        }
    }
}
