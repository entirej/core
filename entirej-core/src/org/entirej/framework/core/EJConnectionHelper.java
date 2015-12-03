package org.entirej.framework.core;

import java.lang.ref.WeakReference;

public class EJConnectionHelper
{
    private static volatile WeakReference<EJFrameworkManager> ref;

    static synchronized void setEJFrameworkManager(EJFrameworkManager manager)
    {
        if (ref == null || ref.get() == null)
        {
            ref = new WeakReference<EJFrameworkManager>(manager);
        }

    }

    public static EJManagedFrameworkConnection getConnection()
    {
        if (ref != null && ref.get() != null)
            return ref.get().getConnection();

        throw new EJApplicationException("EJFrameworkManager not initialized ");
    }

}
