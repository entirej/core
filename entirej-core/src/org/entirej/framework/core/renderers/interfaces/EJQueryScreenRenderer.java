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

import org.entirej.framework.core.EJBlock;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJBlockController;
import org.entirej.framework.core.data.controllers.EJLovController;
import org.entirej.framework.core.enumerations.EJManagedScreenProperty;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.EJCoreQueryScreenItemProperties;
import org.entirej.framework.core.renderers.registry.EJQueryScreenItemRendererRegister;

public interface EJQueryScreenRenderer extends EJRenderer
{
    /**
     * Indicates to the query screen that one of its items properties has been
     * modified
     * 
     * @param itemProperties
     * @param managedItemProperty
     */
    public void refreshItemProperty(EJCoreQueryScreenItemProperties itemProperties, EJManagedScreenProperty managedItemProperty);
    
    /**
     * Used to return the item register used for this query screen renderer
     * 
     * @return The query screens Item Register
     */
    public EJQueryScreenItemRendererRegister getItemRegister();
    
    /**
     * This indicates to the query screen renderer that one of its properties
     * has been modified
     * 
     * @param propertyName
     *            The name of the modified property
     */
    public void refreshQueryScreenRendererProperty(String propertyName);
    
    /**
     * Used to initialize the renderer when the Query Screen is being displayed
     * for a <code>{@link EJBlock}</code>
     * <p>
     * <b>EntireJ</b> will always call this initialize method after creating the
     * renderer. This can then be used to set up the renderer adding all
     * required items from the controllers list of query items
     * 
     * @param block
     *            The <code>{@link EJBlock}</code> to which this renderer
     *            belongs
     * @param record
     *            The record to use for the query screen. This record could
     *            contain values entered by the application developer as default
     *            values
     */
    public void initialiseRenderer(EJBlockController block);
    
    /**
     * Used to initialise the renderer when the Query Screen is being displayed
     * for an <code>{@link EJLovController}</code>
     * <p>
     * <b>EntireJ</b> will always call this initialise method after creating the
     * renderer. This can then be used to set up the renderer adding all
     * required items from the controllers list of query items
     * 
     * @param controller
     *            The <code>{@link EJLovController}</code> to which this
     *            renderer belongs
     */
    public void initialiseRenderer(EJLovController controller);
    
    /**
     * Used to open the renderer
     * <p>
     * The blocks renderer is responsible for opening the query screen renderer.
     * There is therefore one query screen renderer per block controller. The
     * block renderer normally retains a copy of the query record so that
     * subsequent requests to open the query screen renderer will retain
     * previous query values.
     * <p>
     * A query record can be retrieved from the block controller using the
     * {@link EJBlockController#createRecordNoAction()} method.
     * 
     * @param record
     *            this is the record that must be used for the query screen. The
     *            record can contain default values for specific items as
     *            specified by the application developer
     */
    public void open(EJDataRecord record);
    
    /**
     * Called when this renderer should be closed
     */
    public void close();
    
    /**
     * Retrieves the current query record being used within this query screen
     * renderer
     * <p>
     * <b>EntireJ</b> will make copies of this record for specific framework
     * actions
     * 
     * @return The current query record
     */
    public EJDataRecord getQueryRecord();
    
    /**
     * This method will be called by <b>EntireJ</b> if the query record was
     * modified by the forms action processor
     * <p>
     * Any changes from the record must be propagated to the query screen
     * renderer so that the user can see all modifications
     * 
     * @param record
     *            The record that was modified
     */
    public void refreshAfterChange(EJDataRecord record);
    
    /**
     * Ensures that, if any changes have been on the query screen renderer that
     * they are copied into the registered query record
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
