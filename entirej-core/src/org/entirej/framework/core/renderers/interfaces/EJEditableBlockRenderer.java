/*******************************************************************************
 * Copyright 2013 Mojave Innovations GmbH
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
 * Contributors: Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.renderers.interfaces;

import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJEditableBlockController;
import org.entirej.framework.core.enumerations.EJManagedBlockProperty;
import org.entirej.framework.core.interfaces.EJScreenItemController;

public interface EJEditableBlockRenderer extends EJBlockRenderer
{
    /**
     * This indicates to the block renderer that one of the block properties has
     * been modified. The renderer can modify the GUI if required
     * 
     * @param managedBlockPropertyType
     *            The property that has been changed
     */
    public void refreshBlockProperty(EJManagedBlockProperty managedBlockPropertyType);

    /**
     * This indicates to the block renderer that one of its properties has been
     * modified
     * 
     * @param propertyName
     *            The name of the modified property
     */
    public void refreshBlockRendererProperty(String propertyName);

    /**
     * Called after the renderer has been instantiated
     * <p>
     * This renderer will be responsible for rendering the
     * <code>dataBlock</code> controlled by the given
     * <code>BlockController</code>
     * 
     * @param block
     *            This renderer is responsible for rendering this block
     */
    public void initialiseRenderer(EJEditableBlockController block);

    /**
     * Informs the block renderer that a query has been executed and the block
     * now contains new records that need to be displayed to the user
     */
    public void queryExecuted();

    /**
     * Used to indicate if the current record being displayed has been modified
     * <p>
     * The framework will only know about changed records once they have been
     * inserted into the data blocks list of dirty records. If the current
     * record has been modified but not yet sent to the data block, then this
     * needs to be notified. This is needed to ensure that toolbar and menus are
     * correct for the given record. i.e. Any save buttons need to be enabled
     * 
     * @return <code>true</code> if the current record is changed, otherwise
     *         <code>false</code>
     */
    public boolean isCurrentRecordDirty();

    /**
     * Informs the block renderer that the underlying block has been cleared
     * <p>
     * If the block is cleared, then it will not contain any more data and its
     * view should be refreshed
     */
    public void blockCleared();

    /**
     * Informs the block renderer that all the detail blocks of this block have
     * been cleared. The underlying block of this renderer has not been cleared.
     */
    public void detailBlocksCleared();

    /**
     * Indicates that the given record has been inserted into the underlying
     * blocks list of records
     * <p>
     * The <code>BlockRenderer</code> may have to refresh its view in order to
     * display the given record
     * 
     * @param record
     *            The record that was inserted
     */
    public void recordInserted(EJDataRecord record);

    /**
     * Indicates that the given record has been deleted
     * <p>
     * The record will have been removed from the blocks list of records and
     * added to the dirty record list. The <code>EJBlockRenderer</code> may have
     * to refresh its view so that the given record is no longer displayed
     * <p>
     * If a call is made to the block controller using the deleted record, a
     * <code>NullPointerException</code> could be thrown because the record no
     * longer exists within the controllers list of records. The record number
     * passed is the record number at which the deleted record was before the
     * deletion
     * 
     * @param dataBlockRecordNumber
     *            The number of the deleted record. This number is the actual
     *            EJDataBlock record number and not the visible record number
     */
    public void recordDeleted(int dataBlockRecordNumber);

    /**
     * Indicates if this block renderer has the current focus
     * <p>
     * This method is very important as it enables the framework to send access
     * methods to the correct block. If the used wishes to enter a query then
     * the framework will execute this operation on the current focused block.
     * This means that if this method is not implemented correctly then
     * operations could be executed on incorrect blocks.
     * 
     * @return <code>true</code> if the renderer has focus, otherwise
     *         <code>false</code>
     */
    public boolean hasFocus();

    /**
     * Indicates to the block renderer that it has current focus
     * <p>
     * This could be used by the renderer to display a current block indicator
     * etc
     * 
     * @param focus
     *            The focus indicator for this block renderer
     */
    public void setHasFocus(boolean focus);

    /**
     * Set the filter on this renderer if the renderer uses a filter
     * 
     * @param filter
     *            The filter to set
     */
    public void setFilter(String filter);

    /**
     * Returns the filter that has been set on this renderer or
     * <code>null</code> if no filter has been set
     * 
     * @return the blocks filter or <code>null</code> if no filter has been set
     */
    public String getFilter();

    /**
     * Informs the block renderer that it should gain focus
     */
    public void gainFocus();

    /**
     * Informs the block renderer that it should set focus to the given item
     * 
     * @param item
     *            The item that should gain focus
     */
    public void setFocusToItem(EJScreenItemController item);

    /**
     * Informs the block renderer that the user wants to perform an insert
     * operation
     * <p>
     * The insert record passed is the record that will be used to display
     * values on the insert screen. If there is to be default values set on
     * before the insert screen is shown, then this method can be used to set
     * those values
     * 
     * @param recordToInsert
     *            EntireJ will insert this record once all values have been
     *            entered and the processor has validated it
     */
    public void enterInsert(EJDataRecord recordToInsert);

    /**
     * Informs the block renderer that the user wished to update the current
     * record. The current record will be wrapped within the given record to
     * ensure that block records are only modified after all validation routines
     * were successful
     */
    public void enterUpdate(EJDataRecord recordToUpdate);

    /**
     * Indicates to the block renderer that the user wishes to delete the given
     * record
     * 
     * @param recordToDelete
     *            The record to be deleted
     * @param message
     *            The message to be displayed or <code>null</code> if the
     *            standard message should be used
     */
    public void askToDeleteRecord(EJDataRecord recordToDelete, String message);

    /**
     * Returns the renderer that is to be used for executing queries on this
     * block
     * 
     * @return The {@link EJQueryScreenRenderer}
     */
    public EJQueryScreenRenderer getQueryScreenRenderer();

    /**
     * Returns the renderer that is to be used for executing inserts on this
     * block
     * 
     * @return The {@link EJInsertScreenRenderer}
     */
    public EJInsertScreenRenderer getInsertScreenRenderer();

    /**
     * Returns the renderer that is to be used for executing updates on this
     * block
     * 
     * @return The {@link EJUpdateScreenRenderer}
     */
    public EJUpdateScreenRenderer getUpdateScreenRenderer();

}
