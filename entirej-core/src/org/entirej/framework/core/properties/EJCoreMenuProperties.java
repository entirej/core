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
package org.entirej.framework.core.properties;

import java.util.ArrayList;
import java.util.List;

public class EJCoreMenuProperties implements EJCoreMenuLeafContainer
{
    
    private boolean                        _default                  = false;
    private String                         _name;
    
    private String                         _actionProcessorClassName = "";
    
    private List<EJCoreMenuLeafProperties> _menuLeaves;
    
    public EJCoreMenuProperties(String name)
    {
        _name = name;
        _menuLeaves = new ArrayList<EJCoreMenuLeafProperties>();
    }
    
    /**
     * The Action Processor is responsible for actions within the form. Actions
     * can include buttons being pressed, check boxes being selected or pre-post
     * query methods etc.
     * 
     * @return The name of the Action Processor responsible for this form.
     */
    public String getActionProcessorClassName()
    {
        return _actionProcessorClassName;
    }
    
    /**
     * Sets the action processor name for this form
     * <p>
     * The processor is a mandatory parameter and trying to set the processor
     * name to a null or zero length value will result in a
     * <code>FrameworkCoreException</code>
     * 
     * @param processorName
     *            The action processor name for this form
     */
    public void setActionProcessorClassName(String processorName)
    {
        _actionProcessorClassName = processorName;
    }
    
    public String getName()
    {
        return _name;
    }
    
    public void setName(String name)
    {
        _name = name;
    }
    
    public boolean isDefault()
    {
        return _default;
    }
    
    public void setDefault(boolean default1)
    {
        _default = default1;
    }
    
    public void dispose()
    {
        _menuLeaves.clear();
    }
    
    @Override
    public void addLeaf(EJCoreMenuLeafProperties leaf)
    {
        if (leaf != null)
        {
            _menuLeaves.add(leaf);
            leaf.resetContainer(this);
        }
    }
    
    @Override
    public List<EJCoreMenuLeafProperties> getLeaves()
    {
        return _menuLeaves;
    }
    
}
