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
import org.entirej.framework.core.EJLov;
import org.entirej.framework.core.EJManagedFrameworkConnection;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.EJScreenItem;
import org.entirej.framework.core.data.controllers.EJQuestion;
import org.entirej.framework.core.enumerations.EJLovDisplayReason;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.service.EJQueryCriteria;

public interface EJLovActionProcessor extends Serializable
{
    
    /**
     * Called when an LOV is activated on the given screen item
     * <p>
     * <ul>
     * There different ways a lov can be activated:
     * <li>
     * {@link EJLovDisplayReason#LOV} This is when a user activates the LOV
     * directly, e.g. with a defined LOV function key or a double click of the
     * mouse</li>
     * <li>
     * {@link EJLovDisplayReason#VALIDATE} This is when the user enters a value
     * into a field with lov activation applied to it. The value will be
     * validated by the LOV controller and not via the action processor. EntireJ
     * needs to ensure that the value entered is a valid LOV value</li>
     * </ul>
     * 
     * @param lov
     *            The lov from which this method is called
     * @param screenItem
     *            The item that has activated the lov
     * @param displayReason
     *            The reason for the lov activation
     */
    public void lovActivated(EJLov lov, EJScreenItem screenItem, EJLovDisplayReason displayReason) throws EJActionProcessorException;
   
    /**
     * Called after a value has been chosen or the LOV is closed
     * <p>
     * 
     * @param lov
     *            The lov from which this method is called
     * @param screenItem
     *            The item that activated the lov
     * @param valueChosen
     *            Indicates if the lov was completed and a value was chosen.
     *            <code>true</code> indicates a values was chosen from the lov,
     *            <code>false</code> indicates the lov was closed but no value
     *            was chosen
     */
    public void lovCompleted(EJLov lov, EJScreenItem screenItem, boolean valueChosen) throws EJActionProcessorException;
    
    /**
     * If the <code>Lov</code> is asked to ask a question then the answer of
     * the question will be returned within this method
     * <p>
     * This method is needed because Alerts can be non blocking, ie. after the
     * alert is opened the code beneath the show method, is continued.
     * <p>
     * 
     * @param question
     *            The <code>Question</code> holds all information that is
     *            required to ask the question and to return the answer to this
     *            action processor
     */
    public void questionAnswered(EJQuestion question) throws EJActionProcessorException;
    
    /**
     * Called before a query is executed. The <code>IQueryCriteria</code> given,
     * holds the query criteria for the block used within this lov
     * <p>
     * This method is called from the query operation of the framework. The
     * connection passed to this method is the same connection that EntireJ is
     * using. Use the connection if something needs to be done within the same
     * transaction. If an operation is required outside of the Framework
     * transaction, then a new connection must be obtained.
     * 
     * @see EJManagedFrameworkConnection
     * @param lovf
     *            The lov from which this method is called
     * @param queryCriteria
     *            The query criteria for this block
     */
    public void preQuery(EJLov lov, EJQueryCriteria queryCriteria) throws EJActionProcessorException;
    
    /**
     * Called for each record retrieved by the block if the lov. The <code>IDataRecord</code>
     * is the retrieved record. Setting values of this record, will set the
     * value of the blocks underlying data.
     * <p>
     * This method is called from the query operation of the framework. The
     * connection passed to this method is the same connection that EntireJ is
     * using. Use the connection if something needs to be done within the same
     * transaction. If an operation is required outside of the Framework
     * transaction, then a new connection must be obtained.
     * 
     * @param lov
     *            The lov from which this method is called
     * @param record
     *            The record retrieved
     */
    public void postQuery(EJLov lov, EJRecord record) throws EJActionProcessorException;
    
    /**
     * Used to validate the given query criteria before a query is made on the
     * lov
     * <p>
     * If the record is not valid for the given operation then an
     * <code>ActionProcessorException</code> should be thrown
     * <p>
     * 
     * @param lov
     *            The lov from which this method is called
     * @param queryCriteria
     *            The query criteria that should be validated
     */
    public void validateQueryCriteria(EJLov lov, EJQueryCriteria queryCriteria) throws EJActionProcessorException;
    
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
     * @param record
     *            The record containing the modified value
     * @param itemName
     *            The name of the modified item
     * @param screenType
     *            The screen upon which the item was modified
     */
    public void validateItem(EJLov lov, EJRecord record, String itemName, EJScreenType screenType) throws EJActionProcessorException;
    
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
     * @param lov
     *            The lov from which this method is called
     * @param record
     *            The current record from the current block
     * @param command
     *            The action command as defined within the main screen item
     *            properties
     * @param screenType
     *            The screen from where this method was called
     */
    public void executeActionCommand(EJLov lov, EJRecord record, String command, EJScreenType screenType) throws EJActionProcessorException;
    
    /**
     * Called whenever a new record is created. The <code>DataRecord</code> is
     * the new record. Use this method to set default value for items
     * etc.
     * 
     * @param lov
     *            The lov from which this method is called
     * @param record
     *            The newly created record
     */
    public void whenCreateRecord(EJLov lov, EJRecord record) throws EJActionProcessorException;
    
    /**
     * Called each time the user navigated to a new record. The
     * <code>DataRecord</code> given, is the record that was navigated to, i.e.
     * the new current record.
     * 
     * @param lov
     *            The lov from which this method is called
     * @param record
     *            The new current record
     */
    public void newRecordInstance(EJLov lov, EJRecord record) throws EJActionProcessorException;
    
    /**
     * Called before a query screen is opened, if the lov uses a query screen
     * <p>
     * The {@link #initialiseQueryRecord(EJLov, EJDataRecord)} method will
     * be called before this method is called
     * 
     * @param lov
     *            The lov from which this method is called
     * @throws EJActionProcessorException
     */
    public void preOpenQueryScreen(EJLov lov) throws EJActionProcessorException;
    
    /**
     * Called when a record is created for one of the screens. This allows
     * developers to add default values to a record before it is displayed
     * 
     * @param lov
     *            The lov from which this method is called
     * @param record
     *            The record to initialise
     */
    public void initialiseQueryRecord(EJLov lov, EJRecord record) throws EJActionProcessorException;
}
