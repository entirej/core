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
package org.entirej.framework.core.renderers.interfaces;

import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJEditableBlockController;
import org.entirej.framework.core.enumerations.EJManagedScreenProperty;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.EJCoreInsertScreenItemProperties;
import org.entirej.framework.core.renderers.registry.EJInsertScreenItemRendererRegister;

public interface EJInsertScreenRenderer extends EJRenderer
{
    /**
     * Indicates to the insert screen that one of its items properties has been
     * modified
     * 
     * @param itemProperties
     * @param managedItemProperty
     */
    public void refreshItemProperty(EJCoreInsertScreenItemProperties itemProperties, EJManagedScreenProperty managedItemProperty);
    
    /**
     * Used to return the item register used for this insert screen renderer
     * 
     * @return The insert screens Item Register
     */
    public EJInsertScreenItemRendererRegister getItemRegister();
    
    /**
     * This indicates to the insert screen renderer that one of its properties
     * has been modified
     * 
     * @param propertyName
     *            The name of the modified property
     */
    public void refreshInsertScreenRendererProperty(String propertyName);
    
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
     * The blocks renderer is responsible for opening the insert screen
     * renderer. There is therefore one insert screen renderer per block
     * controller
     * <p>
     * If the <code>copyCurrentRecord</code> then the user wants to create a new
     * record based upon the values from the current record. A copy of the
     * current record can be obtained from the
     * <code>{@link EJEditableBlockController#copyCurrentRecord()}</code>
     * method. Once the record has been retrieved then the forms action
     * processor must be informed:
     * <p>
     * <code>controller.getFormController().getActionController().whenCreateRecord(getFormController().getFormManager(), record);</code>
     * <p>
     * This ensures that, if the developer has written code to create primary
     * keys then this is executed on the new record. Otherwise the new record
     * will contain exactly the same primary key values as the current record,
     * thus causing constraint violations
     * 
     * @param record
     *            This is the record that should be used for the insert
     *            operations. The record can contain default values as defined
     *            by the application developer and should therefore be copied to
     *            the insert screen before displaying to the user
     */
    public void open(EJDataRecord record);
    
    /**
     * Called when this renderer should be closed
     */
    public void close();
    
    /**
     * Retrieves the current record being used within this insert screen
     * renderer
     * <p>
     * <b>EntireJ</b> will make copies of this record for specific framework
     * actions
     * 
     * @return The current record being inserted
     */
    public EJDataRecord getInsertRecord();
    
    /**
     * This method will be called by <b>EntireJ</b> if the update record was
     * modified by the forms action processor
     * <p>
     * Any changes from the record must be propagated to the insert screen
     * renderer so that the user can see all modifications
     * <p>
     * All changes are made directly to the registered record, so the renderer
     * can use this record to propagate changes
     * 
     * @param record
     *            The record that was modified
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
     * 
     * @throws EJInsertScreenRendererException
     *             if there is no item with the given name
     */
    public EJScreenItemController getItem(String itemName);
}
