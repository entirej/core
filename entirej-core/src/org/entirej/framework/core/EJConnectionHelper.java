package org.entirej.framework.core;

public class EJConnectionHelper
{
    private static volatile EJFrameworkManagerProvider provider;
   
    
    public static void setProvider(EJFrameworkManagerProvider provider)
    {
        EJConnectionHelper.provider = provider;
    }

    public static EJManagedFrameworkConnection getConnection()
    {
        if (provider != null && provider.get() != null)
            return provider.get().getConnection();
        

        throw new EJApplicationException("EJFrameworkManagerProvider not initialized ");
    }
    
    public static EJManagedFrameworkConnection newConnection()
    {
        if (provider != null && provider.get() != null)
            return provider.get().newConnection();
        
        
        throw new EJApplicationException("EJFrameworkManagerProvider not initialized ");
    }
    
    
    public static interface EJFrameworkManagerProvider
    {
        EJFrameworkManager get();
    }

}
