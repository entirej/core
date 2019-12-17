package org.entirej.framework.core.properties.interfaces;

import java.io.Serializable;

import org.entirej.framework.core.enumerations.EJCanvasMessagePosition;

public interface EJMessagePaneProperties extends Serializable 
{
    public EJCanvasMessagePosition getPosition();

    public int getSize();
    
    public String getVa();
    
    public boolean getCustomFormatting();
    
    public Boolean getCloseable();
}

