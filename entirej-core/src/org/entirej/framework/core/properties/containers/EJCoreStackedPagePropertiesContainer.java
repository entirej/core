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

import org.entirej.framework.core.properties.EJCoreStackedPageProperties;
import org.entirej.framework.core.properties.containers.interfaces.EJStackedPagePropertiesContainer;
import org.entirej.framework.core.properties.interfaces.EJStackedPageProperties;

public class EJCoreStackedPagePropertiesContainer implements EJStackedPagePropertiesContainer, Serializable
{
    private List<EJStackedPageProperties> _stackedPageProperties;
    private EJCoreStackedPageProperties   _lastAddedPage;
    
    public EJCoreStackedPagePropertiesContainer()
    {
        _stackedPageProperties = new ArrayList<EJStackedPageProperties>();
    }
    
    public boolean contains(String pageName)
    {
        Iterator<EJStackedPageProperties> iti = _stackedPageProperties.iterator();
        while (iti.hasNext())
        {
            EJStackedPageProperties props = iti.next();
            if (props.getName().equalsIgnoreCase(pageName))
            {
                return true;
            }
        }
        return false;
    }
    
    public void addStackedPageProperties(EJCoreStackedPageProperties stackedPageProperties)
    {
        if (stackedPageProperties != null)
        {
            _stackedPageProperties.add(stackedPageProperties);
            _lastAddedPage = stackedPageProperties;
        }
    }
    
    public void deleteStackedPageProperties(String pageName)
    {
        Iterator<EJStackedPageProperties> iti = _stackedPageProperties.iterator();
        while (iti.hasNext())
        {
            EJStackedPageProperties props = iti.next();
            if (props.getName().equalsIgnoreCase(pageName))
            {
                _stackedPageProperties.remove(props);
                
                if (_lastAddedPage == props)
                {
                    _lastAddedPage = null;
                }
                
                break;
            }
        }
    }
    
    public EJCoreStackedPageProperties getLastAddedCanvas()
    {
        return _lastAddedPage;
    }
    
    /**
     * Return the <code>StackedEJStackedPagePropertiesPageProperties</code> for
     * the given name
     * 
     * @param pageName
     *            The name of the required stacked page properties
     * @return The <code>EJStackedPageProperties</code> for the given name or
     *         null of there is no stacked page with the given name
     */
    public EJStackedPageProperties getStackedPageProperties(String pageName)
    {
        if (pageName == null || pageName.trim().length() == 0)
        {
            return null;
        }
        else
        {
            Iterator<EJStackedPageProperties> iti = _stackedPageProperties.iterator();
            while (iti.hasNext())
            {
                EJStackedPageProperties props = iti.next();
                if (props.getName().equalsIgnoreCase(pageName))
                {
                    return props;
                }
            }
            return null;
        }
    }
    
    /**
     * Used to return the whole list of stacked pages contained within this
     * canvas.
     * 
     * @return A <code>Collection</code> containing this canvases
     *         <code>EJStackedPageProperties</code>
     */
    public Collection<EJStackedPageProperties> getAllStackedPageProperties()
    {
        return _stackedPageProperties;
    }
    
    /**
     * This is an internal method to generate a default name for a newly created
     * tab page
     * <p>
     * The name will be built as follows:
     * <p>
     * <code>STACKED_PAGE_</code> plus the next highest available tab page
     * number, if there are other stacked page with the default name
     * 
     * @return The default canvas name
     */
    public String generatePageName()
    {
        int nextNr = 10;
        while (pageNameExists("STACKED_PAGE_" + nextNr))
        {
            nextNr++;
        }
        return "STACKED_PAGE_" + nextNr;
    }
    
    public boolean pageNameExists(String name)
    {
        Iterator<EJStackedPageProperties> pagePropsIti = _stackedPageProperties.iterator();
        while (pagePropsIti.hasNext())
        {
            EJStackedPageProperties props = pagePropsIti.next();
            if (props.getName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }
    
}
