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
import org.entirej.framework.core.EJBlock;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJManagedFrameworkConnection;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.EJScreenItem;
import org.entirej.framework.core.enumerations.EJLovDisplayReason;
import org.entirej.framework.core.enumerations.EJRecordType;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.service.EJQueryCriteria;

public interface EJBlockActionProcessor extends Serializable
{
    /**
     * Called before a query is executed. The <code>IQueryCriteria</code> given,
     * holds the query criteria for this block
     * <p>
     * This method is called from the query operation of the framework. The
     * connection passed to this method is the same connection that EntireJ is
     * using. Use the connection if something needs to be done within the same
     * transaction. If an operation is required outside of the Framework
     * transaction, then a new connection must be obtained.
     * 
     * @see EJManagedFrameworkConnection
     * @param form
     *            The form from which this method is called
     * @param queryCriteria
     *            The query criteria for this block
     */
    public void preQuery(EJForm form, EJQueryCriteria queryCriteria) throws EJActionProcessorException;

    /**
     * Called for each record of a queried block. The <code>EJrRecord</code> is
     * the retrieved record. Setting values of this record, will set the value
     * of the blocks underlying data.
     * <p>
     * This method is called from the query operation of the framework. The
     * connection passed to this method is the same connection that EntireJ is
     * using. Use the connection if something needs to be done within the same
     * transaction. If an operation is required outside of the Framework
     * transaction, then a new connection must be obtained.
     * 
     * @param form
     *            The form from which this method is called
     * @param record
     *            The record retrieved
     */
    public void postQuery(EJForm form, EJRecord record) throws EJActionProcessorException;

    /**
     * Called after a block has fully completed its query action
     * <p>
     * this is different from the
     * <code>{@link #postQuery(EJForm, EJRecord)}</code> method in that this
     * method is called once after all data has been retrieved and not for each
     * record
     * 
     * @param form
     *            The form from which this method is called
     * @param block
     *            The block upon which the query was made
     * @throws EJActionProcessorException
     */
    public void postBlockQuery(EJForm form, EJBlock block) throws EJActionProcessorException;

    /**
     * Called before a record is deleted from the block but not from the blocks
     * underlying data source.
     * <p>
     * The <code>EJRecord</code> given is the record to be deleted.
     * 
     * @param form
     *            The form from which this method is called
     * @param record
     *            The record to be deleted
     */
    public void preDelete(EJForm form, EJRecord record) throws EJActionProcessorException;

    /**
     * Called after a record has been deleted from the data block but not from
     * the blocks underlying data source
     * <p>
     * The <code>EJRecord</code> is the record that was deleted.
     * 
     * @param form
     *            The form from which this method is called
     * @param record
     *            The record that was deleted
     */
    public void postDelete(EJForm form, EJRecord record) throws EJActionProcessorException;

    /**
     * Called before a record is inserted into the data block
     * <p>
     * The <code>EJRecord</code> is the record that will be inserted. This
     * method can be used to set values of the inserted record before it is
     * inserted however any items that are part of a master detail relationship
     * <B>must</B> be added within the
     * <code>{@link #initialiseRecord(EJForm, EJRecord, EJRecordType)}</code>
     * method.
     * 
     * @param form
     *            The form from which this method is called
     * @param record
     *            The record which will be inserted
     * 
     */
    public void preInsert(EJForm form, EJRecord record) throws EJActionProcessorException;

    /**
     * Called after the given record is inserted into the block
     * 
     * @param form
     *            The form from which this method is called
     * @param record
     *            The record that was inserted
     */
    public void postInsert(EJForm form, EJRecord record) throws EJActionProcessorException;

    /**
     * Called before the given record is updated within the data block
     * 
     * @param form
     *            The form from which this method is called
     * @param record
     *            The record that will be updated
     */
    public void preUpdate(EJForm form, EJRecord record) throws EJActionProcessorException;

    /**
     * Called after the given record has been updated within the data block
     * 
     * @param form
     *            The form from which this method is called
     * @param record
     *            The record that was updated
     */
    public void postUpdate(EJForm form, EJRecord record) throws EJActionProcessorException;

    /**
     * Called each time the user navigated to a new record. The
     * <code>EJRecord</code> given, is the record that was navigated to, i.e.
     * the new current record.
     * 
     * @param form
     *            The form from which this method is called
     * @param record
     *            The new current record
     */
    public void newRecordInstance(EJForm form, EJRecord record) throws EJActionProcessorException;

    /**
     * Called when an LOV is activated on the given screen item
     * <p>
     * <ul>
     * There different ways a lov can be activated:
     * <li>{@link EJLovDisplayReason#LOV} This is when a user activates the LOV
     * directly, e.g. with a defined LOV function key or a double click of the
     * mouse</li>
     * <li>{@link EJLovDisplayReason#VALIDATE} This is when the user enters a
     * value into a field with lov activation applied to it. The value will be
     * validated by the LOV controller and not via the action processor. EntireJ
     * needs to ensure that the value entered is a valid LOV value</li>
     * </ul>
     * 
     * @param form
     *            The form from which this method is called
     * @param screenItem
     *            The item that has activated the lov
     * @param displayReason
     *            The reason for the lov activation
     */
    public void lovActivated(EJForm form, EJScreenItem screenItem, EJLovDisplayReason displayReason) throws EJActionProcessorException;

    /**
     * Called after a value has been chosen or the LOV is closed
     * <p>
     * 
     * @param form
     *            The form from which this method is called
     * @param screenItem
     *            The item that has activated the lov
     * @param valueChosen
     *            Indicates if the lov was completed and a value was chosen.
     *            <code>true</code> indicates a values was chosen from the lov,
     *            <code>false</code> indicates the lov was closed but no value
     *            was chosen
     */
    public void lovCompleted(EJForm form, EJScreenItem screenItem, boolean valueChosen) throws EJActionProcessorException;

    /**
     * Called when validation is required on a given record
     * <p>
     * Depending on the record type, the record must be validated for INSERT,
     * UPDATE or DELETE. If the record is not valid for the given operation then
     * an <code>ActionProcessorException</code> should be thrown
     * <p>
     * If you wish to validate the query criteria entered by the user then
     * please use the following method
     * <code>{@link #validateQueryCriteria(EJForm, EJQueryCriteria)}</code>
     * <p>
     * 
     * @param form
     *            The form from which this method is called
     * @param record
     *            The record being validated
     * @param recordType
     *            The type of record being validated
     * @see EJBlockActionProcessor#validateQueryCriteria(EJForm,
     *      EJQueryCriteria)
     */
    public void validateRecord(EJForm form, EJRecord record, EJRecordType recordType) throws EJActionProcessorException;

    /**
     * Used to validate the given query criteria before a query is made on the
     * block
     * <p>
     * If the record is not valid for the given operation then an
     * <code>ActionProcessorException</code> should be thrown
     * <p>
     * 
     * @param form
     *            The form from which this method is called
     * @param queryCriteria
     *            The query criteria that should be validated
     */
    public void validateQueryCriteria(EJForm form, EJQueryCriteria queryCriteria) throws EJActionProcessorException;

    /**
     * Called after a user modifies an item
     * <p>
     * This method should be used to validate the item value. If the item value
     * entered is incorrect an <code>ActionProcessorException</code> should be
     * thrown
     * <p>
     * 
     * @param form
     *            The form from which this method is called
     * @param blockName
     *            The block that contains the item that has changed. Use the
     *            EJScreenType to find out which record has actually been
     *            changed
     * @param itemName
     *            The name of the modified item
     * @param screenType
     *            The screen upon which the item was modified
     * @param newValues
     *            This record is a copy of the block record with the new values
     *            to be validated. If the item being validated is an LOV item,
     *            then the record will contain all items available in the LOV
     *            mapping
     */
    public void validateItem(EJForm form, String blockName, String itemName, EJScreenType screenType, EJRecord newValues) throws EJActionProcessorException;

    /**
     * This method is called after the item has been changed and validated
     * <p>
     * 
     * @param form
     *            The form from which this method is called
     * @param blockName
     *            The block that contains the item that has changed. Use the
     *            EJScreenType to find out which record has actually been
     *            changed
     * @param itemName
     *            The name of the modified item
     * @param screenType
     *            The screen upon which the item was modified
     *            
     * @throws EJActionProcessorException
     */
    public void postItemChange(EJForm form, String blockName, String itemName, EJScreenType screenType) throws EJActionProcessorException;

    /**
     * Called each time an item with an action command is modified on the main
     * screen of a form
     * <p>
     * If an item can have an action command is defined by the item renderer
     * definition for each item renderer
     * <p>
     * The typical types of item renderers that execute action commands are
     * check boxes, radio buttons or push buttons
     * 
     * @param form
     *            The form from which this method is called
     * @param blockName
     *            the block that has fired the command
     * @param command
     *            The action command as defined within the main screen item
     *            properties
     * @param screenType
     *            The screen from where this method was called
     */
    public void executeActionCommand(EJForm form, String blockName, String command, EJScreenType screenType) throws EJActionProcessorException;

    /**
     * Called when a record is created for one of the screens. This allows
     * developers to add default values to a record before it is displayed
     * 
     * @param form
     *            The form from which this method is called
     * @param record
     *            The record to initialise
     * @param recordType
     *            The type of record to be initialised
     */
    public void initialiseRecord(EJForm form, EJRecord record, EJRecordType recordType) throws EJActionProcessorException;

    /**
     * Called before a query, insert or update screen is opened
     * <p>
     * The {@link #initialiseRecord(EJForm, EJRecord, EJRecordType)} method will
     * be called before this method is called
     * 
     * @param block
     *            The block which contains the screen that is being opened
     * @param record
     *            The record that is used to initialise the screen
     * @param screenType
     *            The type of screen that is being opened
     */
    public void preOpenScreen(EJBlock block, EJRecord record, EJScreenType screenType) throws EJActionProcessorException;

    /**
     * Called when an update operation is cancelled by the user
     * 
     * @param block
     *            The block that has had the update operation cancelled
     */
    public void whenUpdateCancelled(EJBlock block) throws EJActionProcessorException;

    /**
     * Called when an insert operation is cancelled by the user
     * 
     * @param block
     *            The block that has had the insert operation cancelled
     */
    public void whenInsertCancelled(EJBlock block) throws EJActionProcessorException;
}
