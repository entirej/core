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

import org.entirej.framework.core.properties.EJCoreDrawerPageProperties;
import org.entirej.framework.core.properties.containers.interfaces.EJDrawerPagePropertiesContainer;
import org.entirej.framework.core.properties.interfaces.EJDrawerPageProperties;

public class EJCoreDrawerPagePropertiesContainer implements EJDrawerPagePropertiesContainer, Serializable
{
    private List<EJDrawerPageProperties> _drawerPageProperties;
    private EJCoreDrawerPageProperties   _lastAddedDrawerPage;
    
    public EJCoreDrawerPagePropertiesContainer()
    {
        _drawerPageProperties = new ArrayList<EJDrawerPageProperties>();
    }
    
    public boolean contains(String pageName)
    {
        Iterator<EJDrawerPageProperties> iti = _drawerPageProperties.iterator();
        while (iti.hasNext())
        {
            EJDrawerPageProperties props = iti.next();
            if (props.getName().equalsIgnoreCase(pageName))
            {
                return true;
            }
        }
        return false;
    }
    
    public void addDrawerPageProperties(EJCoreDrawerPageProperties drawerPageProperties)
    {
        if (drawerPageProperties != null)
        {
            _drawerPageProperties.add(drawerPageProperties);
            _lastAddedDrawerPage = drawerPageProperties;
        }
    }
    
    public void deleteDrawerPageProperties(String pageName)
    {
        Iterator<EJDrawerPageProperties> iti = _drawerPageProperties.iterator();
        while (iti.hasNext())
        {
            EJDrawerPageProperties props = iti.next();
            
            if (props.getName().equalsIgnoreCase(pageName))
            {
                _drawerPageProperties.remove(props);
                
                if (_lastAddedDrawerPage == props)
                {
                    _lastAddedDrawerPage = null;
                }
                
                break;
            }
        }
    }
    
    public EJCoreDrawerPageProperties getLastAddedCanvas()
    {
        return _lastAddedDrawerPage;
    }
    
    /**
     * Return the <code>DrawerPageProperties</code> for the given name
     * 
     * @param pageName
     *            The name of the required drawer page properties
     * @return The <code>DrawerPageProperties</code> for the given name or null of
     *         there is no drawer page with the given name
     */
    public EJDrawerPageProperties getDrawerPageProperties(String pageName)
    {
        if (pageName == null || pageName.trim().length() == 0)
        {
            return null;
        }
        else
        {
            Iterator<EJDrawerPageProperties> iti = _drawerPageProperties.iterator();
            
            while (iti.hasNext())
            {
                EJDrawerPageProperties props = iti.next();
                
                if (props.getName().equalsIgnoreCase(pageName))
                {
                    return props;
                }
            }
            return null;
        }
    }
    
    /**
     * Used to return the whole list of drawer pages contained within this canvas.
     * 
     * @return A <code>Collection</code> containing this canvases
     *         <code>DrawerPageProperties</code>
     */
    public Collection<EJDrawerPageProperties> getAllDrawerPageProperties()
    {
        return _drawerPageProperties;
    }
    
    /**
     * This is an internal method to generate a default name for a newly created
     * drawer page
     * <p>
     * The name will be built as follows:
     * <p>
     * <code>DRAWER_PAGE_</code> plus the next highest available drawer page number,
     * if there are other drawer page with the default name
     * 
     * @return The default canvas name
     */
    public String generateDrawerName()
    {
        int nextNr = 10;
        while (drawerNameExists("DRAWER_PAGE_" + nextNr))
        {
            nextNr++;
        }
        return "DRAWER_PAGE_" + nextNr;
    }
    
    public boolean drawerNameExists(String name)
    {
        Iterator<EJDrawerPageProperties> drawerPagesProps = _drawerPageProperties.iterator();
        while (drawerPagesProps.hasNext())
        {
            EJDrawerPageProperties props = drawerPagesProps.next();
            if (props.getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }
}
