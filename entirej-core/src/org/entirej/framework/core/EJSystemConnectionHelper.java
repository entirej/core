package org.entirej.framework.core;

import org.entirej.framework.core.EJConnectionHelper.EJFrameworkManagerProvider;

public class EJSystemConnectionHelper
{
 private static volatile EJFrameworkManagerProvider provider;
   
    
    public static void setProvider(EJFrameworkManagerProvider provider)
    {
        EJSystemConnectionHelper.provider = provider;
    }

    public static EJManagedFrameworkConnection getConnection()
    {
        if (provider != null && provider.get() != null)
            return provider.get().getSystemConnection();
        

        throw new EJApplicationException("EJFrameworkManagerProvider not initialized ");
    }

}
