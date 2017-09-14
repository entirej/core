/*******************************************************************************
 * Copyright 2013 CRESOFT AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors: CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.actionprocessor.interfaces;

import java.io.Serializable;

import org.entirej.framework.core.EJActionProcessorException;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJFrameworkHelper;

public interface EJApplicationActionProcessor extends Serializable
{
    /**
     * Called when a user clicks/Select on a Application Action
     * 
     * @param helper
     *            EJFrameworkHelper to access application context
     * @param command
     *            The command being executed
     */
    public void executeActionCommand(EJFrameworkHelper helper, String command) throws EJActionProcessorException;

    /**
     * Called when application Start
     * 
     * @param helper
     *            EJFrameworkHelper to access application context
     */
    public void whenApplicationStart(EJFrameworkHelper helper) throws EJActionProcessorException;

    /**
     * Called when application ends
     * 
     * @param helper
     *            EJFrameworkHelper to access application context
     */
    public void whenApplicationEnd(EJFrameworkHelper helper) throws EJActionProcessorException;
    
    
    
    
    /**
     * Called before the given tab page is shown
     * 
     * @param helper
     *            EJFrameworkHelper to access application context
     * @param tabName
     *            The name of the tab 
     * @param tabPageName
     *            The tab page
     */
    public void preShowTabPage(EJFrameworkHelper helper, String tabName, String tabPageName) throws EJActionProcessorException;

    /**
     * Called whenever a user chooses a new tab page on a tab 
     * 
     * @param helper
     *            EJFrameworkHelper to access application context
     * @param tabName
     *            The name of the tab 
     * @param tabPageName
     *            The name of the tab within the tab canvas
     */
    public void tabPageChanged(EJFrameworkHelper helper, String tabName, String tabPageName) throws EJActionProcessorException;

}
