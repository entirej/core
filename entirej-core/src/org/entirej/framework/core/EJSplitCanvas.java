package org.entirej.framework.core;

import org.entirej.framework.core.internal.EJInternalForm;

public class EJSplitCanvas extends EJCanvas
{

    public EJSplitCanvas(EJInternalForm form, String name)
    {
        super(form, name);
    }

    public void setVisible(String name, boolean visible)
    {
        try
        {
            getCanvasController().setSplitPageVisible(getName(), name, visible);
        }
        catch (Exception e)
        {
            getForm().handleException(e);
        }
    }

}
