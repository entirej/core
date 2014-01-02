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
package org.entirej.framework.core.properties.containers.interfaces;

import java.io.Serializable;
import java.util.Collection;

import org.entirej.framework.core.properties.interfaces.EJTabPageProperties;

public interface EJTabPagePropertiesContainer extends Serializable
{
    
    public boolean contains(String pageName);
    
    /**
     * Return the <code>TabPageProperties</code> for the given name
     * 
     * @param pageName
     *            The name of the required tab page properties
     * @return The <code>EJTabPageProperties</code> for the given name or null
     *         of there is no tab page with the given name
     */
    public EJTabPageProperties getTabPageProperties(String pageName);
    
    /**
     * Used to return the whole list of tab pages contained within this canvas.
     * 
     * @return A <code>Collection</code> containing this canvases
     *         <code>EJTabPageProperties</code>
     */
    public Collection<EJTabPageProperties> getAllTabPageProperties();
    
    public boolean tabNameExists(String name);
    
}
