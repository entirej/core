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
package org.entirej.framework.core.renderers.eventhandlers;

import java.io.Serializable;

import org.entirej.framework.core.data.controllers.EJBlockController;

public interface EJBlockFocusedListener extends Serializable
{
    /**
     * Indicates that a block has gained focus
     * 
     * @param focusedBlock
     *            The controller that has just gained focus
     */
    public void blockFocusGained(EJBlockController focusedBlock);
    
    /**
     * Indicates that a block has lost focus
     * 
     * @param focusedBlock
     *            The controller that has just lost focus
     */
    public void blockFocusLost(EJBlockController focusedBlock);
}
