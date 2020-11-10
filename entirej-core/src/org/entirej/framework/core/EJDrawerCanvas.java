package org.entirej.framework.core;

import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.internal.EJInternalForm;
import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;

public class EJDrawerCanvas extends EJCanvas
{

    public EJDrawerCanvas(EJInternalForm form, String name)
    {
        super(form, name);
    }

    /**
     * Instructs EntireJ to display the given drawer canvas page
     * <p>
     * Before the tab page is shown, the
     * <code>{@link EJFormActionProcessor#preShowDrawerPage(EJForm, String, String)}</code>
     * will be called
     * <p>
     * After the drawer page has been shown, the
     * <code>{@link EJFormActionProcessor#drawerPageChanged(EJForm, String, String)}</code>
     * will be called
     * 
     * @param pageName
     *            The name of the drawer page
     */
    public void showPage(String pageName)
    {
        if (pageName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD,
                    "EJDrawerCanvas.showPage"));
        }

        try
        {
            getCanvasController().showDrawerPage(getName(), pageName);
        }
        catch (Exception e)
        {
            getForm().handleException(e);
        }
    }
    
    /**
     * Instructs EntireJ to hide the given drawer canvas page
     * 
     * 
     * @param pageName
     *            The name of the drawer page
     */
    public void closePage(String pageName)
    {
        if (pageName == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_CANVAS_NAME_PASSED_TO_METHOD,
                    "EJDrawerCanvas.closePage"));
        }

        try
        {
            getCanvasController().closeDrawerPage(getName(), pageName);
        }
        catch (Exception e)
        {
            getForm().handleException(e);
        }
    }

    /**
     * Used to set a specific drawer canvas page to be visible
     * 
     * @param drawerPageName
     *            The page to be shown
     * @param visbile
     *            If set to <code>true</code> then the drawer page will be made
     *            visible otherwise it will be hidden
     */
    public void setPageVisible(String drawerPageName, boolean visible)
    {
        getCanvasController().setDrawerPageVisible(getName(), drawerPageName, visible);
    }

    
    public void setVisible(boolean visible)
    {
        getCanvasController().setDrawerVisible(getName(),  visible);
    }
    
    /**
     * Returns the name of the drawer page which is currently displayed/active
     * 
     * @return The currently displayed drawer page
     */
    public String getDisplayedPageName()
    {
        return getCanvasController().getDisplayedDrawerPage(getName());
    }
    
    /**
     * Instructs EntireJ to display Badge on given drawer canvas page
     * @param drawerPageName
     *            The page to be shown
     * @param badge
     *            text that need to shown as a badge
     */
    public void setBadge(String pageName,String badge)
    {
        try
        {
            getCanvasController().setDrawerBadge(getName(), pageName, badge);
        }
        catch (Exception e)
        {
            getForm().handleException(e);
        }
    }
    /**
     * Instructs EntireJ to display Badge visual attribute on given drawer canvas page
     * @param drawerPageName
     *            The page to be shown
     * @param visualAttributeName
     *            The name of the visual attribute to set or <code>null</code>
     *            if the screen item should be returned to its original state
     */
    public void setBadgeVisualAttribute(String pageName,String visualAttributeName)
    {
        try
        {
            getCanvasController().setDrawerBadgeVisualAttribute(getName(), pageName, visualAttributeName);
        }
        catch (Exception e)
        {
            getForm().handleException(e);
        }
    }
    
    
}
