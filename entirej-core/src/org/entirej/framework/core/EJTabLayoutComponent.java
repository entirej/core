package org.entirej.framework.core;

import org.entirej.framework.core.actionprocessor.interfaces.EJApplicationActionProcessor;
import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.interfaces.EJApplicationManager;

public class EJTabLayoutComponent 
{

    private EJApplicationManager manager;
    private String name;
    public EJTabLayoutComponent(EJApplicationManager manager, String name)
    {
       this.name = name;
       this.manager = manager;
    }

    /**
     * Instructs EntireJ to display the given tab layout page
     * <p>
     * Before the tab page is shown, the
     * <code>{@link EJApplicationActionProcessor#preShowTabPage(EJForm, String, String)}</code>
     * will be called
     * <p>
     * After the tab page has been shown, the
     * <code>{@link EJApplicationActionProcessor#tabPageChanged(EJForm, String, String)}</code>
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
                    "EJTabLayoutComponent.showPage"));
        }
        
        try
        {
            EJApplicationActionProcessor actionProcessor = manager.getApplicationActionProcessor();
            if(actionProcessor!=null)
            {
                actionProcessor.preShowTabPage(manager, name, pageName);
            }
        }
        catch (Exception e)
        {
            
            throw new EJApplicationException(e);
        }

        try
        {
            manager.showTabPage(getName(), pageName);
        }
        catch (Exception e)
        {
            manager.handleException(e);
        }
    }
    
    public String getName()
    {
        return name;
    }

    /**
     * Used to set a specific tab layout page to be visible
     * 
     * @param tabPageName
     *            The page to be shown
     * @param visbile
     *            If set to <code>true</code> then the tab page will be made
     *            visible otherwise it will be hidden
     */
    public void setPageVisible(String tabPageName, boolean visible)
    {
        manager.setTabPageVisible(getName(), tabPageName, visible);
    }

    /**
     * Used to set a specific tab layout page to be enable
     * 
     * @param tabPageName
     *            The page to be shown
     * @param enable
     *            If set to <code>true</code> then the tab page will be made
     *            enable otherwise it will be disable
     */
    public void setPageEnable(String tabPageName, boolean enable)
    {
        manager.setTabPageEnable(getName(), tabPageName, enable);
    }
    
    /**
     * Returns the name of the tab page which is currently displayed/active
     * 
     * @return The currently displayed tab page
     */
    public String getDisplayedPageName()
    {
        return manager.getDisplayedTabPage(getName());
    }
    
    /**
     * Instructs EntireJ to display Badge on  given tab layout page
     * @param tabPageName
     *            The page to be shown
     * @param badge
     *            text that need to shown as a badge
     */
    public void setBadge(String pageName,String badge)
    {
        try
        {
            manager.setTabBadge(getName(),pageName, badge);
        }
        catch (Exception e)
        {
            manager.handleException(e);
        }
    }
}
