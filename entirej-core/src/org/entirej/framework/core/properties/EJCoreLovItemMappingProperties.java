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

import java.io.Serializable;


public class EJCoreLovItemMappingProperties implements Comparable<EJCoreLovItemMappingProperties> , Serializable
{
    private String _blockItemName;
    private String _lovDefinitionItemName;
    
    public EJCoreLovItemMappingProperties( String lovDefinitionItemName, String blockItemName)
    {
        _lovDefinitionItemName = lovDefinitionItemName;
        _blockItemName = blockItemName;
    }
    
    public void setBlockItemName(String name)
    {
        _blockItemName = name;
    }
    
    public String getBlockItemName()
    {
        return _blockItemName;
    }
    
    public String getLovDefinitionItemName()
    {
        return _lovDefinitionItemName;
    }
    
    public String getName()
    {
        return _lovDefinitionItemName;
    }
    
  
    
    public int compareTo(EJCoreLovItemMappingProperties props)
    {
        if (props == null)
        {
            return -1;
        }
        
        String thisName = _lovDefinitionItemName+_blockItemName;
        String thatName = props.getLovDefinitionItemName()+props.getBlockItemName();
        
        return thisName.toUpperCase().compareTo(thatName.toUpperCase());
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_blockItemName == null) ? 0 : _blockItemName.hashCode());
        result = prime * result + ((_lovDefinitionItemName == null) ? 0 : _lovDefinitionItemName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EJCoreLovItemMappingProperties other = (EJCoreLovItemMappingProperties) obj;
        if (_blockItemName == null)
        {
            if (other._blockItemName != null) return false;
        }
        else if (!_blockItemName.equals(other._blockItemName)) return false;
        if (_lovDefinitionItemName == null)
        {
            if (other._lovDefinitionItemName != null) return false;
        }
        else if (!_lovDefinitionItemName.equals(other._lovDefinitionItemName)) return false;
        return true;
    }

   
    
}
