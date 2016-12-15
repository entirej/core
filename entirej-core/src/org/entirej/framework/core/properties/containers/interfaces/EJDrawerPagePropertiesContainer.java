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

import org.entirej.framework.core.properties.interfaces.EJDrawerPageProperties;

public interface EJDrawerPagePropertiesContainer extends Serializable
{
    
    public boolean contains(String pageName);
    
    /**
     * Return the <code>DrawerPageProperties</code> for the given name
     * 
     * @param pageName
     *            The name of the required drawer page properties
     * @return The <code>EJDrawerPageProperties</code> for the given name or <code>null</code>
     *         of there is no drawer page with the given name
     */
    public EJDrawerPageProperties getDrawerPageProperties(String pageName);
    
    /**
     * Used to return the whole list of drawer pages contained within this canvas.
     * 
     * @return A <code>Collection</code> containing this canvases
     *         <code>EJDrawerPageProperties</code>
     */
    public Collection<EJDrawerPageProperties> getAllDrawerPageProperties();
    
    public boolean drawerNameExists(String name);
    
}
