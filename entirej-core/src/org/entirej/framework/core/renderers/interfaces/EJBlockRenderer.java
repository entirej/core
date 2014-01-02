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
import org.entirej.framework.core.enumerations.EJManagedScreenProperty;

public interface EJBlockRenderer extends EJRenderer
{
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
     * This indicates to the block renderer that a property of one of the block
     * items has been modified. The renderer can modify the GUI if required
     * 
     * @param itemName
     *            The name of the item that has had a property changed
     * @param managedItemPropertyType
     *            The property that has been changed
     * @param record
     *            The record to which the item belongs or <code>null</code> if
     *            the property being refreshed is not record but block scope
     */
    public void refreshItemProperty(String itemName, EJManagedScreenProperty managedItemPropertyType, EJDataRecord record);
    
    /**
     * This indicates to the block renderer that one of the items block renderer
     * required properties have been modified
     * <p>
     * This method will be called when one of the main screen display properties
     * have changed. Any changes to insert/update/query screens will be handled
     * by the screen renderer
     * 
     * @param itemName
     *            The name of the item that was modified
     * @param propertyName
     *            The name of the modified property
     */
    public void refreshItemRendererProperty(String itemName, String propertyName);
    
    /**
     * Ensures that, if any changes have been made to the current record, that
     * they are copied into the frameworks data block
     * <p>
     * This method will normally be called when the data in the form needs to be
     * saved. This method will ensure that all changes will be saved not just
     * those already in the data block.
     */
    public void synchronize();
    
    /**
     * Indicates the blocks current record has been changed
     * <p>
     * The <code>BlockRenderer</code> may have to refresh its view in order to
     * see the modifications
     * 
     * @param record
     *            This was the record that was changed. If the renderer is
     *            showing the record, then it must be refreshed.
     */
    public void refreshAfterChange(EJDataRecord record);
    
    /**
     * Indicates that the given record should be displayed in the block
     * <p>
     * Navigation to this record must be performed
     * 
     * @param record
     *            The record to which navigation should be made
     */
    public void recordSelected(EJDataRecord record);
    
    /**
     * Returns the current focused record
     * <p>
     * The current record will be used within EJ when a user wished to update or
     * delete the current record. The current block record is not necessarily
     * the current displayed record. This is because it is possible for the
     * renderer to only display a subset of records. For example. If a user can
     * filter the displayed records, then the third displayed record is not
     * necessarily the third record the the data block. Therefore allow the
     * renderer to decide which is the current record
     * 
     * @return The record upon which the user currently has focus
     */
    public EJDataRecord getFocusedRecord();
    
    /**
     * Returns the record number of the given record according to the records
     * displayed
     * <p>
     * Because users can filter records on the display, the 5th data block
     * record, is not necessarily the 5th displayed record. It is import for
     * various functions that the displayed record is used, not the block record
     * 
     * @param record
     *            The record number will be returned for this record.
     * @return The displayed record number for the given record or -1 if the
     *         given record is not part of the blocks displayed records
     */
    public int getDisplayedRecordNumber(EJDataRecord record);
    
    /**
     * Return the number of records displayed within this block
     * 
     * @return The number of displayed records
     */
    public int getDisplayedRecordCount();
    
    /**
     * Returns the first <code>EJDataRecord</code> displayed
     * <p>
     * 
     * @return The first displayed record
     */
    public EJDataRecord getFirstRecord();
    
    /**
     * Returns the last <code>EJDataRecord</code> displayed
     * 
     * @return The last displayed record
     */
    public EJDataRecord getLastRecord();
    
    /**
     * Returns the record after the given record in this blocks displayed
     * records
     * <p>
     * If the record passed is already the last record in the block, i.e. there
     * are no more records after the given record, then null will be returned
     * 
     * @param record
     *            The current record
     * @return The record after the current record passed or <code>null</code>
     *         if the current record is already the last record in this blocks
     *         list of records
     */
    public EJDataRecord getRecordAfter(EJDataRecord record);
    
    /**
     * Returns the record before the given record in this blocks list of records
     * <p>
     * If the record passed is already the first record in the block, i.e. there
     * are no more records before the given record, then null will be returned
     * 
     * @param record
     *            The current record
     * @return The record before the current record passed or null if the
     *         current record is already the first record in this blocks list of
     *         records
     */
    public EJDataRecord getRecordBefore(EJDataRecord record);
    
    /**
     * Returns the record at the specified displayed position
     * <p>
     * If there is no record at the given position, then <code>null</code> will
     * be returned
     * <p>
     * This is important for block renderers which implement filters, so that
     * only a partial set of block records are actually displayed. The record
     * number specified is the displayed record number and not necessarily the
     * block record number
     * <p>
     * if the block holds 20 record, ten with a name column starting with 'A'
     * and ten with a the name column starting with 'B' and the user filters on
     * 'B' then the first ten records will not be displayed. Therefore if this
     * method would then be called with a <code>displayedRecordNumber</code> of
     * '1', then the first 'B' record will be returned not the first 'A' record.
     * If this method was called with the <code>displayedRecordNumber</code> set
     * to '11' then <code>null</code> will be returned as there are only ten
     * records displayed
     * 
     * @param displayedRecordNumber
     *            The displayed record number
     * @return
     */
    public EJDataRecord getRecordAt(int displayedRecordNumber);
    
    /**
     * Informs the block renderer that the user wishes to execute a query
     * <p>
     * The query record passed is the record that will be used to display values
     * on the query screen. If there is to be default values set on before the
     * query screen is shown, then this method can be used to set those values
     * 
     * @param queryRecord
     *            This is the record that EntireJ will use for the query record
     */
    public void enterQuery(EJDataRecord queryRecord);
}
