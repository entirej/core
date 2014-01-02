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
package org.entirej.framework.core.properties;

import java.util.List;

import org.entirej.framework.core.properties.interfaces.EJRendererAssignment;

/**
 * Indicates the name and class of a renderer that has been assigned to this
 * application
 */
public class EJCoreRendererAssignment implements EJRendererAssignment, Comparable<EJRendererAssignment>
{
    private String _name;
    private String _className;
    private int    _rendererType;
    
    public EJCoreRendererAssignment(String name, String className, int rendererType)
    {
        _name = name;
        _className = className;
        _rendererType = rendererType;
    }
    
    public void setRendererClassName(String className)
    {
        _className = className;
    }
    
    public String getAssignedName()
    {
        return _name;
    }
    
    public String getRendererClassName()
    {
        return _className;
    }
    
    /**
     * Returns this renderers type
     * <p>
     * The renderer type is defined as a constant within
     * {@link RendererContainer}
     * 
     * @return The renderer type
     * 
     * @see {@link RendererContainer}
     */
    public int getRendererType()
    {
        return _rendererType;
    }
    
    /**
     * Compares this renderer property to another and returns if they are the
     * same
     * <p>
     * The comparison is made on the renderers name
     * 
     * @param property
     *            The property to compare with
     * 
     * @return 0 if the items are the same otherwise -1
     */
    public int compareTo(EJRendererAssignment property)
    {
        if (getAssignedName().equalsIgnoreCase(property.getAssignedName()))
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EJCoreRendererAssignment other = (EJCoreRendererAssignment) obj;
        if (_name == null)
        {
            if (other._name != null) return false;
        }
        else if (!_name.equals(other._name)) return false;
        return true;
    }
    
    /**
     * This property is not loaded as it used within the EntireJPlugin. Loading
     * it here will only use unnecessary overheads
     */
    public List<String> getDataTypeNames()
    {
        return null;
    }
    
    /**
     * This property is not loaded as it used within the EntireJPlugin. Loading
     * it here will only use unnecessary overheads
     */
    public boolean isRendererForDataType(String dataTypeName)
    {
        return false;
    }
    
}
