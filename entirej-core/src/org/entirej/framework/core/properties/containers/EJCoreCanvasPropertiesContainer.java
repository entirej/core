/*******************************************************************************
 * Copyright 2013 Mojave Innovations GmbH
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
 *     Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.properties.containers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.properties.EJCoreCanvasProperties;
import org.entirej.framework.core.properties.containers.interfaces.EJCanvasPropertiesContainer;
import org.entirej.framework.core.properties.interfaces.EJCanvasProperties;

public class EJCoreCanvasPropertiesContainer implements EJCanvasPropertiesContainer
{
    private List<EJCanvasProperties> _canvasProperties;
    
    public EJCoreCanvasPropertiesContainer()
    {
        _canvasProperties = new ArrayList<EJCanvasProperties>();
    }
    
    public boolean contains(String canvasName)
    {
        Iterator<EJCanvasProperties> iti = _canvasProperties.iterator();
        while (iti.hasNext())
        {
            EJCanvasProperties props = iti.next();
            if (props.getName().equalsIgnoreCase(canvasName))
            {
                return true;
            }
        }
        return false;
    }
    
    public void addCanvasProperties(EJCoreCanvasProperties canvasProperties)
    {
        if (canvasProperties != null)
        {
            _canvasProperties.add(canvasProperties);
            canvasProperties.setParentCanvasContainer(this);
        }
    }
    
    
    public void replaceCanvasProperties(EJCoreCanvasProperties oldProp, EJCoreCanvasProperties newProp)
    {
        if (oldProp != null && newProp !=null)
        {
            int indexOf = _canvasProperties.indexOf(oldProp);
            if(indexOf>-1)
            {
                _canvasProperties.set(indexOf, newProp);
            }
            else
            {
                _canvasProperties.add(newProp);
            }
            ( newProp).setParentCanvasContainer(this);
        }
    }
    
    /**
     * Return the <code>CanvasProperties</code> for the given name
     * 
     * @param canvasName
     *            The name of the required canvas properties
     * @return The <code>CanvasProperties</code> for the given name or null of
     *         there is no canvas with the given name
     */
    public EJCanvasProperties getCanvasProperties(String canvasName)
    {
        if (canvasName == null || canvasName.trim().length() == 0)
        {
            return null;
        }
        else
        {
            Iterator<EJCanvasProperties> iti = _canvasProperties.iterator();
            while (iti.hasNext())
            {
                EJCanvasProperties props = iti.next();
                
                if (props.getName().equalsIgnoreCase(canvasName))
                {
                    return props;
                }
            }
            return null;
        }
    }
    
    /**
     * Used to return the whole list of canvases contained within this form.
     * 
     * @return A <code>Set</code> containing this forms
     *         <code>CanvasProperties</code>
     */
    public Collection<EJCanvasProperties> getAllCanvasProperties()
    {
        return _canvasProperties;
    }
    
}
