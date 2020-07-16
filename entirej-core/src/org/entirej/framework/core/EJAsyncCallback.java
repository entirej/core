package org.entirej.framework.core;

public interface EJAsyncCallback<T>
{
    void completed(EJFrameworkHelper helper, T result);
    
    default void completedWithError(EJFrameworkHelper helper , Exception t) 
    {
        helper.handleException(t);
    }
}
