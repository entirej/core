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
import org.entirej.framework.core.data.controllers.EJItemLovController;
import org.entirej.framework.core.data.controllers.EJLovController;
import org.entirej.framework.core.enumerations.EJLovDisplayReason;

public interface EJLovRenderer extends EJBlockRenderer
{
    /**
     * This indicates to the lov renderer that one of its properties has been
     * modified
     * 
     * @param propertyName
     *            The name of the modified property
     */
    public void refreshLovRendererProperty(String propertyName);
    
    /**
     * Called after the renderer has been instantiated
     * <p>
     * This renderer will be responsible for rendering the
     * <code>dataBlock</code> controlled by the given <code>LovController</code>
     * 
     * @param dataBlock
     *            This renderer is responsible for rendering this blocks data
     */
    public void initialiseRenderer(EJLovController lovController);
    
    /**
     * Indicates that current record of the lov renderer has been modified
     * <p>
     * The <code>LovRenderer</code> may have to refresh its view in order to see
     * the modifications
     * 
     * @param record
     *            The record that was modified
     */
    public void refreshAfterChange(EJDataRecord record);
    
    /**
     * Returns the renderer responsible for the query screen
     * 
     * @return The blocks query screen renderer or <code>null</code> if the
     *         block has no query screen renderer
     */
    public EJQueryScreenRenderer getQueryScreenRenderer();
    
    /**
     * Called after the renderer has been instantiated
     * <p>
     * This renderer will be responsible for rendering the <code>lov</code>
     * controlled by the given <code>LovController</code>
     * <p>
     * The display reason passed to the renderer is used by the
     * <code>LovController</code>. The Lov Controller will use this value to
     * decide what action to take after the lov is closed.
     * 
     * @param itemLovController
     *            This is the item lov controller for the item that has the lov
     *            attached
     * @param displayReason
     *            Indicates the display reason for this lov renderer
     */
    public void displayLov(EJItemLovController itemLovController, EJLovDisplayReason displayReason);
    
    /**
     * Returns the reason the lov was displayed
     * <p>
     * The display reason will be passed to the lov renderer in the method
     * {@link #displayLov(EJLovDisplayReason)}
     * 
     * @return The reson this lov was displayed
     */
    public EJLovDisplayReason getDisplayReason();
    
    /**
     * Informs the lov renderer that the user wishes to enter a query
     * <p>
     * The record passed contains default query criteria that should be
     * displayed on a query screen
     * <p>
     * This method will be called by the <code>LovControllery</code> if the lov
     * renderer allows user queries. The Lov Renderer will provide the
     * functionality that enables the user to start a query, a toolbar button
     * etc. When clicked the button will call the lov controller and inform it
     * that the user want to enter a query
     * {@link EJItemLovController#performQueryOperation()}. The lov controller
     * will perform all necessary function and then call this method to inform
     * the <code>ILovRenderer</code> to display a query screen
     * <p>
     * This functionality is identical to that of the
     * <code>EJBlockController</code>
     * 
     * 
     * @param queryCriteria
     *            The query criteria to be displayed on the query screen
     */
    public void enterQuery(EJDataRecord record);
    
    /**
     * Informs the renderer that a query will take place
     * <p>
     * No action is required within the method but the renderer can log
     * information regarding the processing time etc of the query. Once the
     * query has executed and data are available to be displayed, the
     * {@link #queryExecuted()} will be called
     * 
     * @see #queryExecuted()
     * 
     */
    public void executingQuery();
    
    /**
     * Informs the renderer that a query has been executed and the block now
     * contains new records that need to be displayed to the user
     * 
     * @see #executingQuery()
     */
    public void queryExecuted();
    
    /**
     * Informs the block renderer that the underlying block has been cleared
     * <p>
     * If the block is cleared, then it will not contain any more data and its
     * view should be refreshed
     */
    public void blockCleared();
    
}
