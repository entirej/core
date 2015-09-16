package org.entirej.framework.core;

import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.internal.EJInternalForm;

public class EJStackedCanvas extends EJCanvas
{

    public EJStackedCanvas(EJInternalForm form, String name)
    {
        super(form, name);
    }

    /**
     * Instructs EntireJ to display the given stacked canvas page
     * <p>
     * Before the stacked page is shown, the
     * <code>{@link EJFormActionProcessor#preShowStackedPage(EJForm, String, String)}</code>
     * will be called
     * <p>
     * After the stacked page has been shown, the
     * <code>{@link EJFormActionProcessor#stackedPageChanged(EJForm, String, String)}</code>
     * will be called
     * 
     * @param canvasName
     *            The name of the stacked canvas
     * @param pageName
     *            The name of the stacked page
     */
    public void showPage(String pageName)
    {
        try
        {
            getCanvasController().showStackedPage(getName(), pageName);
        }
        catch (Exception e)
        {
            getForm().handleException(e);
        }
    }

    /**
     * Returns the name of the page which is currently displayed/active
     * 
     * @return The currently displayed page
     */
    public String getDisplayedPageName()
    {
        return getCanvasController().getDisplayedStackedPage(getName());
    }
}
