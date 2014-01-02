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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.properties.interfaces.EJRendererAssignment;

public class EJCoreRendererAssignmentContainer implements Serializable
{
    public static final int            FORM_RENDERER_TYPE          = 1;
    public static final int            BLOCK_RENDERER_TYPE         = 2;
    public static final int            ITEM_RENDERER_TYPE          = 3;
    public static final int            LOV_RENDERER_TYPE           = 4;
    public static final int            QUERY_SCREEN_RENDERER_TYPE  = 5;
    public static final int            UPDATE_SCREEN_RENDERER_TYPE = 6;
    public static final int            INSERT_SCREEN_RENDERER_TYPE = 7;
    public static final int            MENU_RENDERER_TYPE          = 8;
    public static final int            APP_COMPONENT_RENDERER_TYPE = 9;
    
    private List<EJRendererAssignment> _rendererAssignments;
    private EJRendererAssignment       _lastAddedAssignment;
    
    private int                        _rendererType;
    
    public EJCoreRendererAssignmentContainer(int rendererType)
    {
        _rendererType = rendererType;
        _rendererAssignments = new ArrayList<EJRendererAssignment>();
    }
    
    public int getRendererType()
    {
        return _rendererType;
    }
    
    public boolean rendererAssignmentExists(String name)
    {
        Iterator<EJRendererAssignment> iti = _rendererAssignments.iterator();
        while (iti.hasNext())
        {
            EJRendererAssignment props = iti.next();
            if (props.getAssignedName().equalsIgnoreCase(name))
            {
                return true;
            }
        }
        return false;
    }
    
    public void addRendererAssignment(EJRendererAssignment property)
    {
        if (property != null)
        {
            _rendererAssignments.add(property);
            _lastAddedAssignment = property;
        }
    }
    
    public void deleteRendererAssignment(String name)
    {
        Iterator<EJRendererAssignment> iti = _rendererAssignments.iterator();
        
        while (iti.hasNext())
        {
            EJRendererAssignment props = iti.next();
            
            if (props.getAssignedName().equalsIgnoreCase(name))
            {
                iti.remove();
                if (_lastAddedAssignment == props)
                {
                    _lastAddedAssignment = null;
                }
                break;
            }
        }
    }
    
    public EJRendererAssignment getLastAddedAssignment()
    {
        return _lastAddedAssignment;
    }
    
    /**
     * Used to retrieve a specific renderer property.
     * 
     * @param name
     *            The name of the required renderer
     * 
     * @return If the renderer name parameter is a valid renderer contained
     *         within this container, then its properties will be returned. If
     *         however the name is null or not valid, then a <b>null</b> object
     *         will be returned.
     */
    public EJRendererAssignment getRendererAssignment(String name)
    {
        if (name == null)
        {
            return null;
        }
        
        Iterator<EJRendererAssignment> iti = _rendererAssignments.iterator();
        
        while (iti.hasNext())
        {
            EJRendererAssignment props = iti.next();
            
            if (props.getAssignedName().equalsIgnoreCase(name))
            {
                return props;
            }
        }
        return null;
    }
    
    /**
     * Used to return the whole list of renderers contained within this
     * container
     * <p>
     * 
     * @return The renderers within this container
     */
    public Collection<EJRendererAssignment> getAllRendererAssignments()
    {
        return _rendererAssignments;
    }
}
