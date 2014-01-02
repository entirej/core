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
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;

public class EJCoreLovDefinitionPropertiesContainer implements Serializable
{
    private List<EJCoreLovDefinitionProperties> _lovDefProperties;
    
    public EJCoreLovDefinitionPropertiesContainer()
    {
        _lovDefProperties = new ArrayList<EJCoreLovDefinitionProperties>();
    }
    
    public boolean contains(String lovDefinitionName)
    {
        Iterator<EJCoreLovDefinitionProperties> iti = _lovDefProperties.iterator();
        while (iti.hasNext())
        {
            EJCoreLovDefinitionProperties props = iti.next();
            if (props.getName().equalsIgnoreCase(lovDefinitionName))
            {
                return true;
            }
        }
        return false;
    }
    
    public void addLovDefinitionProperties(EJCoreLovDefinitionProperties defProperties)
    {
        if (defProperties != null)
        {
            _lovDefProperties.add(defProperties);
        }
    }
    
    /**
     * Used to retrieve a specific lov definition properties.
     * 
     * @return If the lov definition name parameter is a valid lov definition
     *         contained within this form, then its properties will be returned
     *         if however the name is null or not valid, then a <b>null</b>
     *         object will be returned.
     */
    public EJCoreLovDefinitionProperties getLovDefinitionProperties(String defName)
    {
        EJParameterChecker.checkNotZeroLength(defName, "getLovDefinitionProperties", "defName");
        
        Iterator<EJCoreLovDefinitionProperties> iti = _lovDefProperties.iterator();
        
        while (iti.hasNext())
        {
            EJCoreLovDefinitionProperties props = iti.next();
            
            if (props.getName().equalsIgnoreCase(defName))
            {
                return props;
            }
        }
        return null;
    }
    
    /**
     * Used to return the whole list of lov definitions contained within this
     * form. The
     * 
     * @return The lov definitions contained within this form
     */
    public Collection<EJCoreLovDefinitionProperties> getAllLovDefinitionProperties()
    {
        return _lovDefProperties;
    }
}
