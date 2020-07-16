package org.entirej.framework.core;

public interface EJAsyncCallback<T>
{
    void completed(EJFrameworkHelper helper, T result);
    
    void completedWithError(EJFrameworkHelper helper , Exception t);
}
