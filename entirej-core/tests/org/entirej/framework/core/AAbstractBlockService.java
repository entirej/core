package org.entirej.framework.core;

import java.util.List;

import org.entirej.framework.core.service.EJBlockService;

public abstract class AAbstractBlockService implements EJBlockService<APojo>
{
   
    
    @Override
    public boolean canQueryInPages()
    {
        return false;
    }
    
    @Override
    public void executeInsert(EJForm form, List<APojo> newRecords)
    {
    }

    @Override
    public void executeUpdate(EJForm form, List<APojo> updateRecords)
    {
    }

    @Override
    public void executeDelete(EJForm form, List<APojo> deleteRecords)
    {
    }
}
