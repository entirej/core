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

import java.util.ArrayList;
import java.util.List;

public class EJCoreMenuLeafBranchProperties extends EJCoreMenuLeafProperties implements EJCoreMenuLeafContainer
{
    private EJCoreMenuLeafProperties       _lastAddedLeaf;
    private List<EJCoreMenuLeafProperties> _menuLeaves;
    
    public EJCoreMenuLeafBranchProperties(EJCoreMenuProperties menu, EJCoreMenuLeafContainer container)
    {
        super(menu, container);
        _menuLeaves = new ArrayList<EJCoreMenuLeafProperties>();
    }
    
    public void clear()
    {
        _menuLeaves.clear();
        _lastAddedLeaf = null;
    }
    
    public boolean containsLeaf(String leafName)
    {
        for (EJCoreMenuLeafProperties leaf : _menuLeaves)
        {
            if (leaf.getName().equalsIgnoreCase(leafName))
            {
                return true;
            }
        }
        return false;
    }
    
    public void addLeaf(EJCoreMenuLeafProperties leaf)
    {
        if (leaf != null)
        {
            _menuLeaves.add(leaf);
            leaf.resetContainer(this);
            _lastAddedLeaf = leaf;
        }
    }
    
    public void removeLeaf(String leafName)
    {
        for (EJCoreMenuLeafProperties leaf : _menuLeaves)
        {
            if (leaf.getName().equalsIgnoreCase(leafName))
            {
                _menuLeaves.remove(leaf);
                
                if (_lastAddedLeaf == leaf)
                {
                    _lastAddedLeaf = null;
                }
                break;
            }
        }
    }
    
    public EJCoreMenuLeafProperties getLastAddedLeaf()
    {
        return _lastAddedLeaf;
    }
    
    public EJCoreMenuLeafProperties getLeaf(String leafName)
    {
        for (EJCoreMenuLeafProperties leaf : _menuLeaves)
        {
            if (leaf.getName().equalsIgnoreCase(leafName))
            {
                return leaf;
            }
        }
        return null;
    }
    
    public List<EJCoreMenuLeafProperties> getLeaves()
    {
        return _menuLeaves;
    }
    
}
