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
import org.entirej.framework.core.properties.EJCoreRelationProperties;

public class EJCoreRelationPropertiesContainer implements Serializable
{
    private List<EJCoreRelationProperties> _relationProperties;
    
    public EJCoreRelationPropertiesContainer()
    {
        _relationProperties = new ArrayList<EJCoreRelationProperties>();
    }
    
    public void addRelationProperties(EJCoreRelationProperties relationProperties)
    {
        if (relationProperties != null)
        {
            _relationProperties.add(relationProperties);
        }
    }
    
    public EJCoreRelationProperties getRelationProperties(String relationName)
    {
        EJParameterChecker.checkNotZeroLength(relationName, "getRelationProperties", "relationName");
        
        Iterator<EJCoreRelationProperties> iti = _relationProperties.iterator();
        while (iti.hasNext())
        {
            EJCoreRelationProperties relation = iti.next();
            
            if (relation.getName().equalsIgnoreCase(relationName))
            {
                return relation;
            }
        }
        return null;
    }
    
    public EJCoreRelationProperties getRelationProperties(String masterBlockName, String detailBlockName)
    {
        if (masterBlockName == null || detailBlockName == null)
        {
            return null;
        }
        
        Iterator<EJCoreRelationProperties> iti = _relationProperties.iterator();
        while (iti.hasNext())
        {
            EJCoreRelationProperties relation = iti.next();
            
            if (relation.getMasterBlockProperties().getName().equalsIgnoreCase(masterBlockName))
            {
                if (relation.getDetailBlockProperties().getName().equalsIgnoreCase(detailBlockName))
                {
                    return relation;
                }
            }
        }
        return null;
    }
    
    public Collection<EJCoreRelationProperties> getAllRelationProperties()
    {
        return _relationProperties;
    }
    
    public boolean contains(String relationName)
    {
        Iterator<EJCoreRelationProperties> relations = _relationProperties.iterator();
        while (relations.hasNext())
        {
            EJCoreRelationProperties relation = relations.next();
            
            if (relation.getName().equalsIgnoreCase(relationName))
            {
                return true;
            }
        }
        return false;
    }
    
}
