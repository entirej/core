package org.entirej.framework.core.properties;

import org.entirej.framework.core.enumerations.EJCanvasMessagePosition;
import org.entirej.framework.core.properties.interfaces.EJMessagePaneProperties;

public class EJCoreMessagePaneProperties implements EJMessagePaneProperties
{

    private static final long serialVersionUID = 1L;
    private boolean                 _closeableMessagePane = true;
    private boolean                 _customFormatting     = false;
    private String                  _va                   = null;

    private EJCanvasMessagePosition _messagePosition      = EJCanvasMessagePosition.RIGHT;

    private int                     _messagePaneSize      = 200;

    @Override
    public EJCanvasMessagePosition getPosition()
    {
        return _messagePosition;
    }

    @Override
    public int getSize()
    {
        return _messagePaneSize;
    }

    @Override
    public String getVa()
    {
        return _va;
    }

    @Override
    public boolean getCustomFormatting()
    {
        return _customFormatting;
    }

    @Override
    public Boolean getCloseable()
    {
        return _closeableMessagePane;
    }

    public void setCloseable(boolean closeableMessagePane)
    {
        this._closeableMessagePane = closeableMessagePane;
    }
    public void setCustomFormatting(boolean customFormatting)
    {
        this._customFormatting = customFormatting;
    }

    public void setPosition(EJCanvasMessagePosition messagePosition)
    {
        this._messagePosition = messagePosition;
    }

    public void setSize(int messagePaneSize)
    {
        this._messagePaneSize = messagePaneSize;
    }
    public void setVa(String va)
    {
        this._va = va;
    }
    
    
}
