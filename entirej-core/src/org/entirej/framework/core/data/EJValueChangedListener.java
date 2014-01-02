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
package org.entirej.framework.core.data;

import java.io.Serializable;


public interface EJValueChangedListener extends Serializable
{
    /**
     * Indicates when a Data Item Value has been changed
     * <p>
     * After receiving this event, the record will modify the entity object
     * accordingly
     * 
     * @param itemName
     *            The name of the item that has changed
     * @param value
     *            The value the item was changed to
     */
    public void valueChanged(String itemName, Object value);
}
