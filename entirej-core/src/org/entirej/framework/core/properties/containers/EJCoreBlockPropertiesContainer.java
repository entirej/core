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
import org.entirej.framework.core.properties.EJCoreBlockProperties;

public class EJCoreBlockPropertiesContainer implements Serializable
{
    private List<EJCoreBlockProperties> _blockProperties;
    
    public EJCoreBlockPropertiesContainer()
    {
        _blockProperties = new ArrayList<EJCoreBlockProperties>();
    }
    
    public boolean contains(String blockName)
    {
        Iterator<EJCoreBlockProperties> iti = _blockProperties.iterator();
        while (iti.hasNext())
        {
            EJCoreBlockProperties props = iti.next();
            if (props.getName().equalsIgnoreCase(blockName))
            {
                return true;
            }
        }
        return false;
    }
    
    public void addBlockProperties(EJCoreBlockProperties blockProperties)
    {
        if (blockProperties != null)
        {
            _blockProperties.add(blockProperties);
        }
    }
    
    /**
     * Used to retrieve a specific blocks properties.
     * 
     * @return If the block name parameter is a valid block contained within
     *         this form, then its properties will be returned if however the
     *         name is null or not valid, then a <b>null</b> object will be
     *         returned.
     */
    public EJCoreBlockProperties getBlockProperties(String blockName)
    {
        EJParameterChecker.checkNotZeroLength(blockName, "getBlockProperties", "blockName");
        
        Iterator<EJCoreBlockProperties> iti = _blockProperties.iterator();
        
        while (iti.hasNext())
        {
            EJCoreBlockProperties props = iti.next();
            
            if (props.getName().equalsIgnoreCase(blockName))
            {
                return props;
            }
        }
        return null;
    }
    
    /**
     * Used to return the whole list of blocks contained within this form. The
     * key of the <code>HashMap</code> is the name of the block <b>in upper
     * case</b>. The value will be a <code>IBlockProperties</code> object.
     * 
     * @return A <code>HashMap</code> containing this forms
     *         <code>Block Properties</code>
     */
    public Collection<EJCoreBlockProperties> getAllBlockProperties()
    {
        return _blockProperties;
    }

}
