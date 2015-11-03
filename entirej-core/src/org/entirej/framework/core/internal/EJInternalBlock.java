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
package org.entirej.framework.core.internal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJLovBlock;
import org.entirej.framework.core.EJManagedFrameworkConnection;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.data.EJDataBlock;
import org.entirej.framework.core.data.EJDataItem;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJBlockController;
import org.entirej.framework.core.data.controllers.EJBlockRendererController;
import org.entirej.framework.core.data.controllers.EJItemController;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.enumerations.EJRecordType;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreItemProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJDisplayProperties;
import org.entirej.framework.core.renderers.EJManagedInsertScreenRendererWrapper;
import org.entirej.framework.core.renderers.EJManagedQueryScreenRendererWrapper;
import org.entirej.framework.core.renderers.EJManagedUpdateScreenRendererWrapper;
import org.entirej.framework.core.service.EJQueryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJInternalBlock implements Serializable
{
    final Logger logger = LoggerFactory.getLogger(EJInternalBlock.class);

    private EJDefaultServicePojoHelper _servicePojoHelper;
    private EJBlockController          _blockController;

    public EJInternalBlock(EJBlockController blockController)
    {
        _blockController = blockController;
    }

    public EJBlockController getBlockController()
    {
        return _blockController;
    }

    public void initialiseServicePojoHelper()
    {
        if (_servicePojoHelper == null)
        {
            if (_blockController.getMirrorBlockSynchronizer() != null && _blockController.getMirrorBlockSynchronizer().getMirrorParent() != null)
            {
                _servicePojoHelper = new EJDefaultServicePojoHelper(_blockController.getMirrorBlockSynchronizer().getMirrorParent().getProperties());
            }
            else
            {
                _servicePojoHelper = new EJDefaultServicePojoHelper(_blockController.getProperties());
            }
            _servicePojoHelper.addFieldNamesToItems();
        }
    }

    public EJDefaultServicePojoHelper getServicePojoHelper()
    {
        if (_servicePojoHelper == null)
        {
            initialiseServicePojoHelper();
        }

        return _servicePojoHelper;
    }

    public void refreshAfterChange(EJDataRecord record)
    {
        logger.trace("START refreshRecordValues");
        _blockController.refreshAfterChange(record);
        logger.trace("END refreshRecordValues");
    }

    /**
     * Indicates if operations on this controller should be ignored until its
     * master block has values
     * <p>
     * It must not be possible to perform operations on detail blocks in a
     * master-detail relationship if the details master has no values.
     * <p>
     * 
     * @return <code>true</code> if prevent masterless operation is set to true
     *         otherwise <code>false</code>
     */
    public boolean preventMasterlessOperations()
    {
        return _blockController.preventMasterlessOperations();
    }

    /**
     * Returns the <code>InternalForm</code> to which this block belongs
     * 
     * @return The {@link EJInternalForm} to which this block belongs
     */
    public EJInternalForm getForm()
    {
        return _blockController.getFormController().getInternalForm();
    }

    /**
     * Returns the paging renderer for this block
     * <p>
     * The paging renderer will be notified when the data service retrieves
     * another page of data
     * 
     * @return This blocks managed renderer
     */
    public EJBlockRendererController getRendererController()
    {
        return _blockController.getRendererController();
    }

    /**
     * Indicates if this block contains the specified record
     * <p>
     * The record must exist within the blocks list of records for this method
     * to return true. If the record has not yet been added to the block, then
     * this method will return false
     * 
     * @param record
     *            The record to check
     * @return <code>true</code> if the specified record exists within this
     *         block
     */
    public boolean containsRecord(EJDataRecord record)
    {
        return _blockController.containsRecord(record);
    }

    /**
     * Returns the display properties of the query screen on this block
     * 
     * @return The query screen display properties or <code>null</code> if this
     *         block contains no query screen
     */
    public EJDisplayProperties getQueryScreenDisplayProperties()
    {
        return _blockController.getProperties().getQueryScreenRendererProperties();
    }

    /**
     * Returns the query screen renderer defined for this block, or
     * <code>null</code> if this block uses no query screen
     * 
     * @return This blocks {@link EJManagedQueryScreenRendererWrapper} or
     *         <code>null</code> if this block uses no query screen
     */
    public EJManagedQueryScreenRendererWrapper getQueryScreenRenderer()
    {
        return _blockController.getManagedQueryScreenRenderer();
    }

    /**
     * Returns the display properties of the insert screen on this block
     * <p>
     * <b> A block used within an LOV Definition will never have an update
     * screen and will therefore always return null </b>
     * 
     * @return The insert screen display properties or <code>null</code> if this
     *         block contains no insert screen
     */
    public EJDisplayProperties getInsertScreenDisplayProperties()
    {
        return _blockController.getProperties().getInsertScreenRendererProperties();
    }

    /**
     * Returns the insert screen renderer defined for this block, or
     * <code>null</code> if this block uses no insert screen
     * <p>
     * <b> A block used within an LOV Definition will never have an update
     * screen and will therefore always return null </b>
     * 
     * @return This blocks {@link EJManagedInsertScreenRendererWrapper} or
     *         <code>null</code> if this block uses no insert screen
     */
    public EJManagedInsertScreenRendererWrapper getInsertScreenRenderer()
    {
        return _blockController.getManagedInsertScreenRenderer();
    }

    /**
     * Returns the display properties of the update screen on this block
     * <p>
     * <b> A block used within an LOV Definition will never have an update
     * screen and will therefore always return null </b>
     * 
     * @return The update screen display properties or <code>null</code> if the
     *         block contains no update screen
     */
    public EJDisplayProperties getUpdateScreenDisplayProperties()
    {
        return _blockController.getProperties().getUpdateScreenRendererProperties();
    }

    /**
     * Returns the update screen renderer defined for this block or
     * <code>null</code> if the block contains no update screen
     * <p>
     * <b> A block used within an LOV Definition will never have an update
     * screen and will therefore always return null </b>
     * 
     * @return This blocks {@link EJManagedUpdateScreenRendererWrapper} or
     *         <code>null</code> if this block uses no update screen
     */
    public EJManagedUpdateScreenRendererWrapper getUpdateScreenRenderer()
    {
        return _blockController.getManagedUpdateScreenRenderer();
    }

    /**
     * Returns a collection of all item controllers available on this block
     * 
     * @return A {@link Collection} of all item controllers within this block
     */
    public Collection<EJItemController> getAllBlockItemControllers()
    {
        return _blockController.getAllBlockItemControllers();
    }

    /**
     * Returns the block item controller with the given name or
     * <code>null</code> if there is no item with the given name
     * 
     * @param itemName
     *            The name of the required item
     * @return The block item controller with the given name
     */
    public EJItemController getBlockItemController(String itemName)
    {
        return _blockController.getBlockItemController(itemName);
    }

    /**
     * Returns the screen item with the given name
     * 
     * @param screenType
     *            The type of screen that holds the required item
     * @param itemName
     *            The name of the required item
     * @return The required screen item
     */
    public EJScreenItemController getScreenItem(EJScreenType screenType, String itemName)
    {
        EJScreenItemController item = _blockController.getScreenItem(screenType, itemName);
        if (item == null)
        {
            EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_ITEM_ON_FORM, itemName,
                    _blockController.getProperties().getName(), _blockController.getFormController().getProperties().getName());
            throw new EJApplicationException(message);
        }

        return item;
    }

    /**
     * Returns a list of all screen items defined for the given screen type
     * 
     * @param screenType
     *            The type of the screen that is required
     * @return A list of all the items on the required screen
     */
    public Collection<EJScreenItemController> getAllScreenItems(EJScreenType screenType)
    {
        return _blockController.getAllScreenItems(screenType);
    }

    /**
     * Returns the <code>FrameworkManager</code>
     * 
     * <p>
     * 
     * @return The <code>FrameworkManager</code>
     */
    public EJFrameworkManager getFrameworkManager()
    {
        return _blockController.getFrameworkManager();
    }

    /**
     * Instructs EntireJ to clear this block
     * <p>
     * If <code>disregardChanges</code> is <code>true</code> then all changes
     * made to the current block and all of its child blocks will be disregarded
     * 
     * @param disregardChanges
     *            Indicates if the blocks changes should be disregarded
     */
    public void clear(boolean disregardChanges)
    {
        _blockController.clearBlock(disregardChanges);
    }

    /**
     * Instructs EntireJ to clear the current focused record of this block
     */
    public void clearFocusedRecord()
    {
        logger.trace("START clearFocusedRecord");
        EJDataRecord record = _blockController.getFocusedRecord();
        if (record != null)
        {
            record.clear();
        }
        logger.trace("END clearFocusedRecord");
    }

    /**
     * Makes a copy of the current record of this block
     * 
     * @return A copy of the current record of this block
     */
    public EJDataRecord copyFocusedRecord()
    {
        logger.trace("START copyFocusedRecord");
        return _blockController.copyFocusedRecord();
    }

    /**
     * Creates an empty query criteria for this block
     * <p>
     * The criteria can be used for executing a query within this block.
     * Criterion can be added to restrict the query
     * 
     * @return
     */
    public EJQueryCriteria createQueryCriteria()
    {
        return new EJQueryCriteria(new EJLovBlock(_blockController.getBlock()));
    }

    /**
     * Used to create an empty record for this block
     * <p>
     * The whenCreateRecord within the blocks mediator will be fired once the
     * record has been created
     * 
     * @param recordType
     *            The type of record to be created
     * @return The newly created record
     */
    public EJDataRecord createRecord(EJRecordType recordType)
    {
        return _blockController.createRecord(recordType);
    }

    /**
     * Used to create an empty record for this block without firing the
     * whenCreateRecord action within the blocks action mediator
     * <p>
     * 
     * @return The newly created record
     */
    public EJDataRecord createRecordNoAction()
    {
        logger.trace("START createRecordNoAction - Returning directly");
        return _blockController.createRecordNoAction();
    }

    /**
     * Instructs EntireJ to perform a query on the given block using the
     * specified criteria
     * 
     * @param queryCriteria
     *            The criteria for the query
     */
    public void executeQuery(EJQueryCriteria queryCriteria)
    {
        logger.trace("START executeQuery");

        EJManagedFrameworkConnection connection = getFrameworkManager().getConnection();

        try
        {
            if (queryCriteria == null)
            {
                queryCriteria = new EJQueryCriteria(new EJLovBlock(_blockController.getBlock()));
            }
            _blockController.executeQuery(queryCriteria);
        }
        catch (EJApplicationException e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e.getFrameworkMessage(), e);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            if (connection != null)
            {
                connection.commit();
                connection.close();
            }
        }

        logger.trace("END executeQuery");
    }

    /**
     * Instructs the block to execute the given action command on the given
     * screen
     * 
     * @param actionCommand
     *            The action command to execute
     * @param screenType
     *            The screen that handles the action command
     */
    public void executeActionCommand(String actionCommand, EJScreenType screenType)
    {
        logger.trace("START executeActionCommand");
        _blockController.executeActionCommand(actionCommand, screenType);
        logger.trace("END executeActionCommand");
    }

    /**
     * Instructs EntireJ to re-query this block using the query criteria
     * previously entered
     */
    public void executeLastQuery()
    {
        logger.trace("START executeLastQuery");
        EJManagedFrameworkConnection connection = null;
        try
        {
            connection = _blockController.getFrameworkManager().getConnection();
            _blockController.executeLastQuery();
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            _blockController.getFrameworkManager().handleException(e);
        }
        finally
        {
            if (connection != null)
            {
                connection.commit();
                connection.close();
            }
        }
        logger.trace("END executeLastQuery");
    }

    /**
     * Returns the last query criteria that was used for the query of the block
     * 
     * @return The last used QueryCriteria or <code>null</code> if no query has
     *         yet been made
     */
    public EJQueryCriteria getLastQueryCriteria()
    {
        return _blockController.getQueryCriteria();

    }

    /**
     * Retrieves the current focused record for the given block
     * 
     * @param blockName
     *            The name of the block
     * @return The current focused record of the given block or
     *         <code>null</code> if there is no record focused
     */
    public EJDataRecord getFocusedRecord()
    {
        return _blockController.getFocusedRecord();
    }

    /**
     * Returns the item that is currently focused on the current record or
     * <code>null</code> if no item has focus
     * 
     * @return The item that currently has focus within the current record
     */
    public EJDataItem getFocusedItem()
    {
        return _blockController.getFocusedItem();
    }

    /**
     * Returns a collection of item names contained within this block
     * 
     * @return The names of all the items contained within the given block
     */
    public Collection<String> getItemNames()
    {
        ArrayList<String> names = new ArrayList<String>();
        Collection<EJCoreItemProperties> itemPropsSet = _blockController.getProperties().getItemPropertiesContainer().getAllItemProperties();
        for (EJCoreItemProperties itemProps : itemPropsSet)
        {
            names.add(itemProps.getName());
        }

        return names;
    }

    /**
     * Returns the item names displayed on the given screen type
     * 
     * @param screenType
     *            Item displayed on this screen will be returned
     * 
     * @return A <code>List</code> containing all item names displayed on the
     *         given screen
     * 
     */
    public Collection<String> getScreenItemNames(EJScreenType screenType)
    {
        ArrayList<String> names = new ArrayList<String>();

        switch (screenType)
        {
            case MAIN:
                return _blockController.getProperties().getScreenItemNames(screenType);
            case INSERT:
                return names;
            case QUERY:
                return _blockController.getProperties().getScreenItemNames(screenType);
            case UPDATE:
                return names;
        }

        return names;
    }

    /**
     * Indicates if this block can query its data in pages
     * <p>
     * The data service will decide if the blocks data can be retrieved in
     * pages. If the service is able to retrieve data in pages then this method
     * will return <code>true</code>, otherwise it will return
     * <code>false</code>
     * 
     * @return
     */
    public boolean canQueryInPages()
    {
        return _blockController.canQueryInPages();
    }

    /**
     * Indicates if this block has more pages
     * <p>
     * If this block is being queried in pages, then this method will indicate
     * if the block has more pages available
     * 
     * @return <code>true</code> if the block has more pages, otherwise
     *         <code>false</code>
     */
    public boolean hasMorePages()
    {
        return _blockController.hasMorePages();
    }

    /**
     * Indicates if the block is currently displaying the first page of data
     * <p>
     * If this blocks data is retrieved in pages, then this method will indicate
     * if the first page is being displayed
     * 
     * @return <code>true</code> if the first page of data is being displayed,
     *         otherwise <code>false</code>
     */
    public boolean isOnFirstPage()
    {
        return _blockController.isOnFirstPage();
    }

    /**
     * Informs this block to retrieve the next page of data if the block is
     * retrieving its data in pages
     * <p>
     * If the block has no more records to be retrieved, this method does
     * nothing
     */
    public void nextPage()
    {
        logger.trace("START nextPage");
        EJManagedFrameworkConnection connection = null;
        try
        {
            connection = _blockController.getFrameworkManager().getConnection();
            _blockController.nextPage(true);
        }
        catch (Exception e)
        {
            _blockController.getFrameworkManager().handleException(e);
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        logger.trace("END nextPage");
    }

    /**
     * Informs this block to retrieve the previous page of data if the block is
     * retrieving its data in pages
     * <p>
     * If the block is already on the first page of data, then this method will
     * do nothing
     */
    public void previousPage()
    {
        logger.trace("START previousPage");
        EJManagedFrameworkConnection connection = null;
        try
        {
            connection = _blockController.getFrameworkManager().getConnection();
            _blockController.previousPage(true);
        }
        catch (Exception e)
        {
            _blockController.getFrameworkManager().handleException(e);
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        logger.trace("END previousPage");
    }

    /**
     * Instructs the block to navigate to the next record
     * <p>
     * If the user is already on the last record, then nothing will happen
     */
    public void nextRecord()
    {
        logger.trace("START nextRecord");
        _blockController.nextRecord();
        logger.trace("END nextRecord");
    }

    /**
     * Indicates if the user is currently on the last record
     * 
     * @return <code>true</code> if the last record currently has focus,
     *         otherwise <code>false</code>
     */
    public boolean isLastDisplayedRecord()
    {
        logger.trace("START isLastDisplayedRecord");
        return _blockController.isLastDisplayedRecord();
    }

    /**
     * Instructs the block to navigate to the previous record<
     * <p>
     * If the block is already on the first record, then nothing will happen
     */
    public void previousRecord()
    {
        logger.trace("START previousRecord");
        _blockController.previousRecord();
        logger.trace("END previousRecord");
    }

    /**
     * Indicates if the user is currently on the first record of this block
     * 
     * @return <code>true</code> if the first record currently has focus,
     *         otherwise <code>false</code>
     * @throws EJBlockControllerException
     */
    public boolean isFirstDisplayedRecord()
    {
        logger.trace("START isFirstDisplayedRecord");
        return _blockController.isFirstDisplayedRecord();
    }

    /**
     * Called by the block renderer whenever a new record is chosen by the user
     * <p>
     * The record passed is the actual record that currently has user focus. If
     * this block is a master in a master-detail relationship, then all detail
     * blocks will be queried
     * 
     * @param selectedRecord
     *            The newly selected record
     */
    public void newRecordInstance(EJDataRecord selectedRecord)
    {
        logger.trace("START newRecordInstance");
        _blockController.newRecordInstance(selectedRecord);
        logger.trace("END newRecordInstance");
    }

    /**
     * Returns a collection if IDataRecords for this block Retrieving all
     * records will force <B>EntireJ</B> to refresh the blocks records. If only
     * the current record needs to be modified, use <code>setItemValue</code>
     * 
     * @return A collection of records or an empty collection if the block
     *         doesn't exist
     */
    public Collection<EJDataRecord> getRecords()
    {
        EJDataBlock block = _blockController.getDataBlock();
        if (block != null)
        {
            return block.getRecords();
        }
        else
        {
            return new ArrayList<EJDataRecord>();
        }
    }

    /**
     * Returns the amount of records this block currently holds
     * 
     * @return The amount of records within this block
     */
    public int getBlockRecordCount()
    {
        logger.trace("START getBlockRecordCount");
        return _blockController.getDataBlock().getBlockRecordCount();
    }

    /**
     * Returns the total number of records displayed by this blocks renderer
     * 
     * @return The total number of records displayed within this blocks renderer
     */
    public int getDisplayedRecordCount()
    {
        logger.trace("START getDisplayedRecordCount");
        return _blockController.getDisplayedRecordCount();
    }

    public int getDisplayedRecordNumber(EJDataRecord record)
    {
        logger.trace("START getDisplayedRecordNumber");
        return _blockController.getDisplayedRecordNumber(record);
    }

    /**
     * Returns the properties of this block
     * 
     * @return This blocks properties
     */
    public EJCoreBlockProperties getProperties()
    {
        return _blockController.getProperties();
    }

    public boolean isDeleteAllowed()
    {
        return false;
    }

    public boolean isInsertAllowed()
    {
        return false;
    }

    public boolean isInsertMode()
    {
        return false;
    }

    public boolean isUpdateAllowed()
    {
        return false;
    }

    public boolean isUpdateMode()
    {
        return false;
    }
}
