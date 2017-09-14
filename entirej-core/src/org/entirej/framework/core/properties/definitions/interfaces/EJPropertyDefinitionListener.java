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
package org.entirej.framework.core.properties.definitions.interfaces;

public interface EJPropertyDefinitionListener
{
    /**
     * Informs the property manager to reset the given item, if an item exists
     * with the given name
     * <p>
     * When an item is reset and it is a property containing a list of valid
     * values, then the valid values will be cleared and the new values
     * retrieved from the definition
     * 
     * @param name
     *            The name if the item to reset
     */
    public void resetItem(String name);
    
    /**
     * Informs the property manager to refresh all property values for the
     * framework extension
     */
    public void refreshProperties();
    
    /**
     * Informs the property manager to refresh the property value for the given
     * item, if the item exists
     * 
     * @param name
     *            The name of the item to refresh
     */
    public void refreshProperties(String name);
    
    /**
     * Send a message to the user
     * <p>
     * This can be used if the user needs to be informed about the value that
     * has just been entered
     * 
     * @param messageToSend
     *            The message that will be shown to the user
     */
    public void sendUserMessage(String messageToSend);
    
    /**
     * Send a message to the user and reset the given property
     * <p>
     * You can reset an property back to its previous value or,
     * <code>null</code> if it did not yet have a value
     * 
     * @param messageToSend
     *            The message that will be show to the user
     * @param property
     *            The name of the property to reset
     */
    public void sendUserMessageAndReset(String messageToSend, String property);
    
}
