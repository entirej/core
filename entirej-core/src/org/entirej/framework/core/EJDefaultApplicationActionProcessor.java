package org.entirej.framework.core;

import org.entirej.framework.core.actionprocessor.interfaces.EJApplicationActionProcessor;

public class EJDefaultApplicationActionProcessor implements EJApplicationActionProcessor
{

    @Override
    public void executeActionCommand(EJFrameworkHelper helper, String command) throws EJActionProcessorException
    {

    }

    @Override
    public void whenApplicationStart(EJFrameworkHelper helper) throws EJActionProcessorException
    {

    }

    @Override
    public void whenApplicationEnd(EJFrameworkHelper helper) throws EJActionProcessorException
    {

    }

    @Override
    public void preShowTabPage(EJFrameworkHelper helper, String tabName, String tabPageName) throws EJActionProcessorException
    {
        
        
    }

    @Override
    public void tabPageChanged(EJFrameworkHelper helper, String tabName, String tabPageName) throws EJActionProcessorException
    {
        
        
    }

}
