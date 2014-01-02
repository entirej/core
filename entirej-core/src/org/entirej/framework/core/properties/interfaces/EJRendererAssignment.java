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
package org.entirej.framework.core.properties.interfaces;

import java.io.Serializable;
import java.util.List;

public interface EJRendererAssignment extends Serializable
{
    /**
     * Returns the list of data type names for which this renderer should be
     * used
     * 
     * @return The list of data type names
     */
    public List<String> getDataTypeNames();
    
    /**
     * Indicates if this renderer has been defined to render the given data type
     * 
     * @param dataTypeName
     *            The name of the data type
     * @return <code>true</code> if this renderer is used for the given datatype
     *         otherwise <code>false</code>
     */
    public boolean isRendererForDataType(String dataTypeName);
    
    /**
     * Returns the name of this renderer definition property
     * 
     * @return This renderer definition property name
     */
    public String getAssignedName();
    
    /**
     * Returns the class name of the renderer
     * 
     * @return The renderer class name
     */
    public String getRendererClassName();
}
