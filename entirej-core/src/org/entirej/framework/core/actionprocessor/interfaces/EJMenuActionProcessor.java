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
package org.entirej.framework.core.actionprocessor.interfaces;

import java.io.Serializable;

import org.entirej.framework.core.EJActionProcessorException;
import org.entirej.framework.core.EJForm;

public interface EJMenuActionProcessor extends Serializable
{
    /**
     * Called when a user clicks on a MenuAction
     * 
     * @param menu
     *            The menu that the action is called from
     * @param command
     *            The command being executed
     */
    public void executeActionCommand( String command) throws EJActionProcessorException;
    
    /**
     * Called before a form is opened
     * 
     * @param menu
     *            The menu that is issuing the call
     * @param form
     *            The form which is to be opened
     */
    public void preOpenForm( EJForm form) throws EJActionProcessorException;
    
    /**
     * Called after a form has been closed
     * 
     * @param menu
     *            The menu that is issuing the call
     * @param form
     *            The form which has been closed
     */
    public void formClosed(EJForm form) throws EJActionProcessorException;
    
}
