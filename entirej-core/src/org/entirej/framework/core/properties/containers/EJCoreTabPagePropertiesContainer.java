/*******************************************************************************
 * Copyright 2013 CRESOFT AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Contributors:
 *     CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.properties.containers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.properties.EJCoreTabPageProperties;
import org.entirej.framework.core.properties.containers.interfaces.EJTabPagePropertiesContainer;
import org.entirej.framework.core.properties.interfaces.EJTabPageProperties;

public class EJCoreTabPagePropertiesContainer implements EJTabPagePropertiesContainer, Serializable
{
    private List<EJTabPageProperties> _tabPageProperties;
    private EJCoreTabPageProperties   _lastAddedTabPage;
    
    public EJCoreTabPagePropertiesContainer()
    {
        _tabPageProperties = new ArrayList<EJTabPageProperties>();
    }
    
    public boolean contains(String pageName)
    {
        Iterator<EJTabPageProperties> iti = _tabPageProperties.iterator();
        while (iti.hasNext())
        {
            EJTabPageProperties props = iti.next();
            if (props.getName().equalsIgnoreCase(pageName))
            {
                return true;
            }
        }
        return false;
    }
    
    public void addTabPageProperties(EJCoreTabPageProperties tabPageProperties)
    {
        if (tabPageProperties != null)
        {
            _tabPageProperties.add(tabPageProperties);
            _lastAddedTabPage = tabPageProperties;
        }
    }
    
    public void deleteTabPageProperties(String pageName)
    {
        Iterator<EJTabPageProperties> iti = _tabPageProperties.iterator();
        while (iti.hasNext())
        {
            EJTabPageProperties props = iti.next();
            
            if (props.getName().equalsIgnoreCase(pageName))
            {
                _tabPageProperties.remove(props);
                
                if (_lastAddedTabPage == props)
                {
                    _lastAddedTabPage = null;
                }
                
                break;
            }
        }
    }
    
    public EJCoreTabPageProperties getLastAddedCanvas()
    {
        return _lastAddedTabPage;
    }
    
    /**
     * Return the <code>TabPageProperties</code> for the given name
     * 
     * @param pageName
     *            The name of the required tab page properties
     * @return The <code>TabPageProperties</code> for the given name or null of
     *         there is no tab page with the given name
     */
    public EJTabPageProperties getTabPageProperties(String pageName)
    {
        if (pageName == null || pageName.trim().length() == 0)
        {
            return null;
        }
        else
        {
            Iterator<EJTabPageProperties> iti = _tabPageProperties.iterator();
            
            while (iti.hasNext())
            {
                EJTabPageProperties props = iti.next();
                
                if (props.getName().equalsIgnoreCase(pageName))
                {
                    return props;
                }
            }
            return null;
        }
    }
    
    /**
     * Used to return the whole list of tab pages contained within this canvas.
     * 
     * @return A <code>Collection</code> containing this canvases
     *         <code>TabPageProperties</code>
     */
    public Collection<EJTabPageProperties> getAllTabPageProperties()
    {
        return _tabPageProperties;
    }
    
    /**
     * This is an internal method to generate a default name for a newly created
     * tab page
     * <p>
     * The name will be built as follows:
     * <p>
     * <code>TAB_PAGE_</code> plus the next highest available tab page number,
     * if there are other tab page with the default name
     * 
     * @return The default canvas name
     */
    public String generateTabName()
    {
        int nextNr = 10;
        while (tabNameExists("TAB_PAGE_" + nextNr))
        {
            nextNr++;
        }
        return "TAB_PAGE_" + nextNr;
    }
    
    public boolean tabNameExists(String name)
    {
        Iterator<EJTabPageProperties> tabPagesProps = _tabPageProperties.iterator();
        while (tabPagesProps.hasNext())
        {
            EJTabPageProperties props = tabPagesProps.next();
            if (props.getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }
}
