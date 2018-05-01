package org.entirej.framework.core;

import java.lang.ref.WeakReference;

public class EJConnectionHelper
{
    private static volatile WeakReference<EJFrameworkManager> ref;
    private static String entireJPropertiesFileName;

    static synchronized void setEJFrameworkManager(EJFrameworkManager manager,String entireFileName)
    {
        if (ref == null || ref.get() == null)
        {
            ref = new WeakReference<EJFrameworkManager>(manager);
        }
        entireJPropertiesFileName = entireFileName;

    }

    public static EJManagedFrameworkConnection getConnection()
    {
        if (ref != null && ref.get() != null)
            return ref.get().getSystemConnection();
        else
        {
            try
            {
                EJFrameworkManager manager = new EJFrameworkManager(entireJPropertiesFileName);
                ref = new WeakReference<EJFrameworkManager>(manager);
                return manager.getSystemConnection();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        throw new EJApplicationException("EJFrameworkManager not initialized ");
    }

}
