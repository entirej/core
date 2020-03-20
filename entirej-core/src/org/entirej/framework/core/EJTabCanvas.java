package org.entirej.framework.core;

import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.internal.EJInternalForm;

public class EJTabCanvas extends EJCanvas
{

    public EJTabCanvas(EJInternalForm form, String name)
    {
        super(form, name);
    }

    /**
     * Instructs EntireJ to display the given tab canvas page
     * <p>
     * Before the tab page is shown, the
     * <code>{@link EJFormActionProcessor#preShowTabPage(EJForm, String, String)}</code>
     * will be called
     * <p>
     * After the tab page has been shown, the
     * <code>{@link EJFormActionProcessor#tabPageChanged(EJForm, String, String)}</code>
     * will be called
     * 
     * @param pageName
     *            The name of the tab page
     */
    public void showPage(String pageName)
    {
        if (pageName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD,
                    "EJTabCanvas.showPage"));
        }

        try
        {
            getCanvasController().showTabPage(getName(), pageName);
        }
        catch (Exception e)
        {
            getForm().handleException(e);
        }
    }

    /**
     * Used to set a specific tab canvas page to be visible
     * 
     * @param tabPageName
     *            The page to be shown
     * @param visbile
     *            If set to <code>true</code> then the tab page will be made
     *            visible otherwise it will be hidden
     */
    public void setPageVisible(String tabPageName, boolean visible)
    {
        getCanvasController().setTabPageVisible(getName(), tabPageName, visible);
    }

    /**
     * Returns the name of the tab page which is currently displayed/active
     * 
     * @return The currently displayed tab page
     */
    public String getDisplayedPageName()
    {
        return getCanvasController().getDisplayedTabPage(getName());
    }
    
    /**
     * Instructs EntireJ to display Badge on  given tab canvas page
     * @param tabPageName
     *            The page to be shown
     * @param badge
     *            text that need to shown as a badge
     */
    public void setBadge(String pageName,String badge)
    {
        try
        {
            getCanvasController().setTabBadge(getName(),pageName, badge);
        }
        catch (Exception e)
        {
            getForm().handleException(e);
        }
    }
    
    /**
     * Instructs EntireJ to display  visual attribute on given tab canvas page
     * @param pageName
     *            The tab page to be shown
     * @param visualAttributeName
     *            The name of the visual attribute to set or <code>null</code>
     *            if the screen item should be returned to its original state
     */
    public void setPageVisualAttribute(String pageName,String visualAttributeName)
    {
        try
        {
            getCanvasController().setTabPageVisualAttribute(getName(), pageName, visualAttributeName);
        }
        catch (Exception e)
        {
            getForm().handleException(e);
        }
    }
}
