package org.entirej.framework.core;

import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.enumerations.EJPopupButton;
import org.entirej.framework.core.internal.EJInternalForm;

public class EJPopupCanvas extends EJCanvas
{

    public EJPopupCanvas(EJInternalForm form, String name)
    {
        super(form, name);
    }

    /**
     * Instructs EntireJ to open this popup canvas
     * <p>
     * Before the command has been sent to the <code>IFormRenderer</code> to
     * display the popup canvas, the
     * <code>{@link EJFormActionProcessor#preOpenPopupCanvas(EJForm, String)}</code>
     * will be called
     * <P>
     * Once the command has been sent to the <code>IFormRenderer</code> to
     * display the popup canvas, the
     * <code>{@link EJFormActionProcessor#popupCanvasOpened(EJForm, String)}</code>
     * will be called
     * <p>
     * The popup canvas definition, defines 3 buttons that can be shown on the
     * popup. These are defined within <code>{@link EJPopupButton}}</code>. When
     * the used clicks on one of the buttons to close the popup, the Action
     * Processors method
     * <code>{@link EJFormActionProcessor#popupCanvasClosed(EJForm, String, EJPopupButton)}</code>
     * will be called
     * 
     */
    public void open()
    {
        try
        {
            getCanvasController().showPopupCanvas(getName());
        }
        catch (Exception e)
        {
            getForm().handleException(e);
        }
    }

    /**
     * Instructs EntireJ to close this popup canvas
     * <P>
     * By calling this method to close the popup canvas, no closing and closed
     * events will be passed to the forms action processor
     */
    public void close()
    {
        try
        {
            getForm().getRenderer().closePopupCanvas(getName());
        }
        catch (Exception e)
        {
            getForm().handleException(e);
        }
    }

}