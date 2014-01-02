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

import org.entirej.framework.core.common.utils.EJParameterChecker;
import org.entirej.framework.core.properties.EJCoreLovMappingProperties;

public class EJCoreLovMappingPropertiesContainer implements Serializable
{
    private List<EJCoreLovMappingProperties> _lovMappingProperties;
    
    public EJCoreLovMappingPropertiesContainer()
    {
        _lovMappingProperties = new ArrayList<EJCoreLovMappingProperties>();
    }
    
    public boolean contains(String lovDefinitionName)
    {
        Iterator<EJCoreLovMappingProperties> iti = _lovMappingProperties.iterator();
        while (iti.hasNext())
        {
            EJCoreLovMappingProperties props = iti.next();
            if (props.getName().equalsIgnoreCase(lovDefinitionName))
            {
                return true;
            }
        }
        return false;
    }
    
    public void addLovDefinitionProperties(EJCoreLovMappingProperties lovMappingProperties)
    {
        if (lovMappingProperties != null)
        {
            _lovMappingProperties.add(lovMappingProperties);
        }
    }
    
    /**
     * Used to retrieve a specific lov mapping properties
     * 
     * @return If the lov mapping name parameter is a valid lov mapping
     *         contained within this form, then its properties will be returned
     *         if however the name is null or not valid, then a <b>null</b>
     *         object will be returned.
     */
    public EJCoreLovMappingProperties getLovMappingProperties(String mappingName)
    {
        EJParameterChecker.checkNotZeroLength(mappingName, "getLovMappingProperties", "mappingName");
        
        Iterator<EJCoreLovMappingProperties> iti = _lovMappingProperties.iterator();
        
        while (iti.hasNext())
        {
            EJCoreLovMappingProperties props = iti.next();
            
            if (props.getName().equalsIgnoreCase(mappingName))
            {
                return props;
            }
        }
        return null;
    }
    
    /**
     * Used to return the whole list of lov mappings contained within this form
     * 
     * @return The lov mappings contained within this form
     */
    public Collection<EJCoreLovMappingProperties> getAllLovMappingProperties()
    {
        return _lovMappingProperties;
    }
    
}
