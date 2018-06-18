package org.entirej.framework.core;

import java.util.Collection;

import org.entirej.framework.core.data.controllers.EJCanvasController;
import org.entirej.framework.core.internal.EJInternalForm;

public class EJCanvas
{
    private EJInternalForm _form;
    private String         _name;

    protected EJCanvas(EJInternalForm form, String canvasName)
    {
        _name = canvasName;
        _form = form;
    }

    /**
     * Returns the name of this canvas
     * 
     * @return The name of this canvas
     */
    public String getName()
    {
        return _name;
    }

    protected EJInternalForm getForm()
    {
        return _form;
    }
    
    protected EJCanvasController getCanvasController()
    {
        return _form.getCanvasController();
    }

    /**
     * Sets the canvas messages to a specific canvas
     * <p>
     * Canvas Messages are displayed either to the right, left, top or bottom of
     * a canvas and displays a list of messages for the user. This is especially
     * helpful on popup canvases which are being used as insert/update screens
     * <p>
     * If an empty collection or <code>null</code> is passed, the message pane
     * for the given canvas will be cleared of messages
     * 
     * @param canvasName
     *            The name of the canvas that will be displaying the messages
     * @param messages
     *            The messages to be displayed
     * 
     * @see #clearCanvasMessages(String)
     */
    public void setCanvasMessages(Collection<EJMessage> messages)
    {
        if (messages == null || messages.size() == 0)
        {
            getCanvasController().clearCanvasMessages(_name);
        }
        else
        {
            getCanvasController().setCanvasMessages(_name, messages);
        }
    }

    /**
     * Clears the messages that have been set on the given canvas
     * 
     * @param canvasName
     *            The name of the canvas that should be cleared of messages
     * 
     * @see #setCanvasMessages(String, Collection)
     */
    public void clearCanvasMessages()
    {
        getCanvasController().clearCanvasMessages(_name);
    }
    
    
    public void setTrayContent(String id)
    {
        
        getCanvasController().setTrayContent(_name, id);
        
    }
}
