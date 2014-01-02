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
package org.entirej.framework.core.renderers.interfaces;

import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJEditableBlockController;
import org.entirej.framework.core.enumerations.EJManagedScreenProperty;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.EJCoreUpdateScreenItemProperties;

public interface EJUpdateScreenRenderer extends EJRenderer
{
    /**
     * Indicates to the update screen that one of its items properties has been
     * modified
     * 
     * @param itemProperties
     * @param managedItemProperty
     */
    public void refreshItemProperty(EJCoreUpdateScreenItemProperties itemProperties, EJManagedScreenProperty managedItemProperty);
    
    /**
     * This indicates to the update screen renderer that one of its properties
     * has been modified
     * 
     * @param propertyName
     *            The name of the modified property
     */
    public void refreshUpdateScreenRendererProperty(String propertyName);
    
    /**
     * Used to initialize the renderer
     * <p>
     * <b>EntireJ</b> will always call this initialize method after creating the
     * renderer. This can then be used to set up the renderer adding all
     * required items from the controllers list of query items
     * 
     * @param block
     *            The block to which this renderer belongs
     */
    public void initialiseRenderer(EJEditableBlockController block);
    
    /**
     * Used to open the renderer
     * <p>
     * The blocks renderer is responsible for opening the update screen
     * renderer. There is therefore one update screen renderer per block
     * controller
     * <p>
     * The record passed is a wrapper around the actual record being updated.
     * This will ensure that record modifications are only made after all
     * validation routines have been successful. This also allows the user to
     * cancel the update operation within effecting the blocks record
     */
    public void open(EJDataRecord recordToUpdate);
    
    /**
     * Called when this renderer should be closed
     */
    public void close();
    
    /**
     * Retrieves the current record being used within this update screen
     * renderer
     * <p>
     * <b>EntireJ</b> will make copies of this record for specific framework
     * actions
     * 
     * @return The current record being updated
     */
    public EJDataRecord getUpdateRecord();
    
    /**
     * This method will be called by <b>EntireJ</b> if the update record was
     * modified by the forms action processor
     * <p>
     * Any changes from the record must be propagated to the insert screen
     * renderer so that the user can see all modifications
     * 
     * @param record
     *            The record that was changed
     */
    public void refreshAfterChange(EJDataRecord record);
    
    /**
     * Ensures that, if any changes have been on the insert screen renderer that
     * they are copied into the registered insert record
     */
    public void synchronize();
    
    /**
     * Returns an item with the given name
     * 
     * @param itemName
     *            The name of the required item
     */
    public EJScreenItemController getItem(String itemName);
}
