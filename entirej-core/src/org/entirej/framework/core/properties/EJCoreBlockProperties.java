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
package org.entirej.framework.core.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.properties.containers.EJCoreItemGroupPropertiesContainer;
import org.entirej.framework.core.properties.containers.EJCoreItemPropertiesContainer;
import org.entirej.framework.core.properties.containers.EJCoreLovMappingPropertiesContainer;
import org.entirej.framework.core.properties.containers.interfaces.EJItemGroupPropertiesContainer;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJBlockProperties;
import org.entirej.framework.core.properties.interfaces.EJItemGroupProperties;
import org.entirej.framework.core.properties.interfaces.EJItemProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.service.EJBlockService;

public class EJCoreBlockProperties implements EJBlockProperties, Comparable<EJCoreBlockProperties>
{
    private EJFrameworkManager                  _frameworkManager;
    private EJCoreFormProperties                _formProperties;
    private String                              _name;
    private boolean                             _isControlBlock               = false;
    private boolean                             _isMirrorBlock                = false;
    private boolean                             _isReferenced                 = false;
    private String                              _referencedBlockName;
    private String                              _mirrorBlockName;
    private String                              _canvasName;
    private String                              _blockRendererName;
    private EJFrameworkExtensionProperties      _blockRendererProperties;
    private EJFrameworkExtensionProperties      _queryScreenRendererProperties;
    private EJFrameworkExtensionProperties      _insertScreenRendererProperties;
    private EJFrameworkExtensionProperties      _updateScreenRendererProperties;
    
    private String                              _serviceClassName;
    private EJBlockService<?>                   _blockService;
    private String                              _actionProcessorClassName;
    
    private EJCoreLovDefinitionProperties       _lovDefinition;
    
    private boolean                             _insertAllowed                = true;
    private boolean                             _updateAllowed                = true;
    private boolean                             _deleteAllowed                = true;
    private boolean                             _queryAllowed                 = true;
    private boolean                             _queryAllRows                 = false;
    private boolean                             _addControlBlockDefaultRecord = true;
    private int                                 _maxResults                   = -1;
    private int                                 _pageSize                     = 0;
    private EJCoreItemPropertiesContainer       _itemPropertiesContainer;
    private EJCoreMainScreenProperties          _mainScreenProperties;
    private EJCoreItemGroupPropertiesContainer  _mainScreenItemGroups;
    private EJCoreItemGroupPropertiesContainer  _queryScreenItemGroups;
    private EJCoreItemGroupPropertiesContainer  _updateScreenItemGroups;
    private EJCoreItemGroupPropertiesContainer  _insertScreenItemGroups;
    private EJCoreLovMappingPropertiesContainer _lovMappings;
    
    public EJCoreBlockProperties(EJFrameworkManager manager, EJCoreFormProperties formProperties, String blockName, boolean isControlBlock, boolean isReferenced)
    {
        if (blockName == null || blockName.trim().length() == 0)
        {
            
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_BLOCK_NAME_PASSED_TO_METHOD,
                    "BlockProperties"));
        }
        
        if (formProperties == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_FORM_PROPERTIES_PASSED_TO_METHOD,
                    "BlockProperties"));
        }
        
        _frameworkManager = manager;
        _formProperties = formProperties;
        _isControlBlock = isControlBlock;
        _isReferenced = isReferenced;
        _itemPropertiesContainer = new EJCoreItemPropertiesContainer();
        _mainScreenItemGroups = new EJCoreItemGroupPropertiesContainer(EJCoreItemGroupPropertiesContainer.MAIN_SCREEN);
        _queryScreenItemGroups = new EJCoreItemGroupPropertiesContainer(EJCoreItemGroupPropertiesContainer.QUERY_SCREEN);
        _insertScreenItemGroups = new EJCoreItemGroupPropertiesContainer(EJCoreItemGroupPropertiesContainer.INSERT_SCREEN);
        _updateScreenItemGroups = new EJCoreItemGroupPropertiesContainer(EJCoreItemGroupPropertiesContainer.UPDATE_SCREEN);
        _lovMappings = new EJCoreLovMappingPropertiesContainer();
        _mainScreenProperties = new EJCoreMainScreenProperties(this);
        _name = blockName;
        
        if (_isControlBlock)
        {
            _queryAllowed = false;
        }
    }
    
    public String getDescription()
    {
        return null;
    }
    
    /**
     * Returns the framework manager
     * 
     * @return The framework manager
     */
    public EJFrameworkManager getFrameworkManager()
    {
        return _frameworkManager;
    }
    
    /**
     * Indicates if this is a control block
     * <p>
     * Control blocks can be used as normal blocks but they have no interaction
     * with the data source. Therefore no queries, inserts updates or deletes
     * are made.
     * 
     * @return <code>true</code> if this is a control block otherwise
     *         <code>false</code>
     */
    public boolean isControlBlock()
    {
        return _isControlBlock;
    }
    
    /**
     * Indicates if a default record should be created for a control block
     * <p>
     * Most control blocks will be single record blocks with buttons or links
     * that are used to act on data on other blocks. Such blocks will require a
     * record to work correctly and should thus have a default record.
     * <p>
     * If the block is a control block that the user will use to populate
     * records which will later be processed then no default record should be
     * created.
     * <p>
     * This property indicates if such a record should be created
     * 
     * @see #setAddControlBlockDefaultRecord(boolean)
     * @return <code>true</code> if a default record should be created otherwise
     *         <code>false</code>
     */
    public boolean addControlBlockDefaultRecord()
    {
        return _addControlBlockDefaultRecord;
    }
    
    /**
     * Used to indicate if a default record should be added to this block if the
     * block is a control block
     * 
     * @see #addControlBlockDefaultRecord()
     * @param addDefaultRecord
     */
    public void setAddControlBlockDefaultRecord(boolean addDefaultRecord)
    {
        _addControlBlockDefaultRecord = addDefaultRecord;
    }
    
    /**
     * Indicates if this block is a referenced block
     * 
     * @return <code>true</code> if this is a referenced block otherwise
     *         <code>false</code>
     */
    public boolean isReferenceBlock()
    {
        return _isReferenced;
    }
    
    /**
     * Indicates if this block is a mirror block
     * <p>
     * the MirrorParent defines with which block this block should synchronize
     * with
     * 
     * @return <code>true</code> if this is a mirror block otherwise
     *         <code>false</code>
     */
    public boolean isMirrorBlock()
    {
        return _isMirrorBlock;
    }
    
    /**
     * Indicates if this is a mirrored block
     * <p>
     * If the block has been defined as mirrored but there is no mirror parent
     * defined then this block is the mirror parent
     * 
     * @param isMirrored
     *            <code>true</code> if mirrored otherwise <code>false</code>
     */
    public void setIsMirroredBlock(boolean isMirrored)
    {
        _isMirrorBlock = isMirrored;
    }
    
    public boolean isMirrorParent()
    {
        return _isMirrorBlock && (_mirrorBlockName == null || _mirrorBlockName.trim().length() == 0);
    }
    
    public boolean isMirrorChild()
    {
        return _isMirrorBlock && (_mirrorBlockName != null && _mirrorBlockName.trim().length() > 0);
    }
    
    /**
     * Returns the name of the block that this block is the mirror of
     * 
     * @return The name of the mirror block
     */
    public String getMirrorBlockName()
    {
        return _mirrorBlockName;
    }
    
    /**
     * Sets the name of this blocks mirror
     * 
     * @param name
     *            The name of the block that this block mirrors to
     */
    public void setMirrorBlockName(String name)
    {
        if (name != null && name.trim().length() > 0)
        {
            _mirrorBlockName = name;
        }
        else
        {
            _mirrorBlockName = null;
        }
    }
    
    /**
     * Resets this blocks name
     * <p>
     * <b>This method is an internal method only and should not be called from
     * application code</b>
     * </p>
     * 
     * @param name
     *            The new name for this block
     * @throws EJApplicationException
     *             If the block name passed is either null or an empty String
     */
    public void internalSetName(String name)
    {
        if (name == null || name.trim().length() == 0)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.SETTING_BLOCK_NAME_TO_NULL,
                    _formProperties.getName(), _name));
        }
        
        _name = name;
    }
    
    /**
     * Resets this blocks referenced property
     * <p>
     * <b>This method is an internal method only and should not be called from
     * application code</b>
     * </p>
     * 
     * @param referenced
     *            indicates if this block is referenced
     */
    public void internalSetReferenced(boolean referenced)
    {
        _isReferenced = referenced;
    }
    
    /**
     * Indicates if this block is used within an LOV Definition
     * 
     * @return <code>true</code> if this block is used within a lov definition,
     *         otherwise <code>false</code>
     */
    public boolean isUsedInLovDefinition()
    {
        return _lovDefinition != null;
    }
    
    public EJCoreLovDefinitionProperties getLovDefinition()
    {
        return _lovDefinition;
    }
    
    public void setLovDefinitionProperties(EJCoreLovDefinitionProperties properties)
    {
        _lovDefinition = properties;
    }
    
    /**
     * Returns the properties of the form to which this block belongs
     * 
     * @return The form properties
     */
    public EJCoreFormProperties getFormProperties()
    {
        return _formProperties;
    }
    
    /**
     * Returns the fully qualified class name of the service that is responsible
     * for the retrieval and modification of this blocks data
     * 
     * @return The fully qualified class name of the service that is responsible
     *         for the retrieval and modification of this blocks data
     */
    public String getServiceClassName()
    {
        return _serviceClassName;
    }
    
    /**
     * Sets the fully qualified class name of the service that is responsible
     * for the retrieval and modification of this blocks data
     * 
     * @param className
     *            The fully qualified class name of the service that is
     *            responsible for the retrieval and modification of this blocks
     *            data
     */
    public void setServiceClassName(String className)
    {
        _serviceClassName = className;
        
        if (className != null && className.trim().length() > 0)
        {
            _blockService = _frameworkManager.getBlockServiceFactory().createBlockService(className);
        }
    }
    
    /**
     * Returns the service used to retrieve and manipulate the data of this
     * block
     * 
     * @return This blocks service
     */
    public EJBlockService<?> getBlockService()
    {
        return _blockService;
    }
    
    /**
     * The Action Processor is responsible for actions within this block
     * <p>
     * Actions can include buttons being pressed, check boxes being selected or
     * pre-post query methods etc.
     * 
     * @return The name of the Action Processor responsible for this form.
     */
    public String getActionProcessorClassName()
    {
        return _actionProcessorClassName;
    }
    
    /**
     * Sets the action processor name for this block
     * <p>
     * If no action processor is specified for this block, then all action
     * methods will be propogated to the form level action processor
     * 
     * @param processorName
     *            The action processor name for this block
     */
    public void setActionProcessorClassName(String processorName)
    {
        _actionProcessorClassName = processorName;
    }
    
    /**
     * Returns the internal name of this block
     * 
     * @return The name of this block
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * The block renderer is the class that will be responsible for displaying
     * the contents of this block
     * 
     * @return the blockRendererClassName
     */
    public String getBlockRendererName()
    {
        return _blockRendererName;
    }
    
    /**
     * Sets the name of the block renderer that is responsible for displaying
     * this blocks data
     * 
     * @param blockRendererName
     *            the name of the Block Renderer
     */
    public void setBlockRendererName(String blockRendererName)
    {
        _blockRendererName = blockRendererName;
    }
    
    /**
     * Sets the rendering properties for this block's query screen
     * <p>
     * These properties are not used internally within the EntireJ Core
     * Framework but are used by the query screen renderer assigned to this
     * block
     * 
     * @param renderingProperties
     *            The rendering properties for the query screen renderer
     */
    public void setQueryScreenRendererProperties(EJFrameworkExtensionProperties renderingProperties)
    {
        _queryScreenRendererProperties = renderingProperties;
    }
    
    /**
     * Returns the rendering properties defined for the query screen renderer
     * assigned to this block
     * 
     * @return The query screen rendering properties
     */
    public EJFrameworkExtensionProperties getQueryScreenRendererProperties()
    {
        return _queryScreenRendererProperties;
    }
    
    /**
     * Sets the rendering properties for this block's update screen
     * <p>
     * These properties are not used internally within the EntireJ Core
     * Framework but are used by the update screen renderer assigned to this
     * block
     * 
     * @param renderingProperties
     *            The rendering properties for update screen renderer
     */
    public void setUpdateScreenRendererProperties(EJFrameworkExtensionProperties renderingProperties)
    {
        _updateScreenRendererProperties = renderingProperties;
    }
    
    /**
     * Returns the rendering properties defined for the update screen renderer
     * assigned to this block
     * 
     * @return The update screen rendering properties
     */
    public EJFrameworkExtensionProperties getUpdateScreenRendererProperties()
    {
        return _updateScreenRendererProperties;
    }
    
    /**
     * Sets the rendering properties for this block's insert screen
     * <p>
     * These properties are not used internally within the EntireJ Core
     * Framework but are used by the insert screen renderer assigned to this
     * block
     * 
     * @param renderingProperties
     *            The rendering properties for insert screen renderer
     */
    public void setInsertScreenRendererProperties(EJFrameworkExtensionProperties renderingProperties)
    {
        _insertScreenRendererProperties = renderingProperties;
    }
    
    /**
     * Returns the rendering properties defined for the insert screen renderer
     * assigned to this block
     * 
     * @return The insert screen rendering properties
     */
    public EJFrameworkExtensionProperties getInsertScreenRendererProperties()
    {
        return _insertScreenRendererProperties;
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
                addGroupItems(_mainScreenItemGroups, names);
                break;
            case INSERT:
                addGroupItems(_insertScreenItemGroups, names);
                break;
            case QUERY:
                addGroupItems(_queryScreenItemGroups, names);
                break;
            case UPDATE:
                addGroupItems(_updateScreenItemGroups, names);
                break;
        }
        
        return names;
    }
    
    /**
     * Returns the <code>ItemGroupContainer</code> that contains all item groups
     * and items for the given screen of this block
     * 
     * @return An <code>ItemGroupContainer</code> containing all items and item
     *         groups of the given screen of this block
     * 
     * @param screenType
     *            The screen type for which the item groups should be returned
     * @return The item group container for the given screen type
     */
    public EJCoreItemGroupPropertiesContainer getScreenItemGroupContainer(EJScreenType screenType)
    {
        switch (screenType)
        {
            case MAIN:
                return _mainScreenItemGroups;
            case INSERT:
                return _insertScreenItemGroups;
            case QUERY:
                return _queryScreenItemGroups;
            case UPDATE:
                return _updateScreenItemGroups;
        }
        
        return null;
    }
    
    /**
     * Returns the name of the canvas upon which this blocks data should be
     * displayed or <code>null</code> if no canvas has been defined for the
     * block
     * <p>
     * If the canvas that was chosen is a type of TAB then the blocks data
     * should be displayed on the correct tab page
     * {@link EJCoreBlockProperties#getCanvasTabPageName()}
     * <p>
     * If no canvas has been defined for this block, then the block will not be
     * displayed anywhere
     * <p>
     * 
     * @return the name of the canvas upon which the blocks data will be
     *         displayed or <code>null</code> if no canvas has been defined
     */
    public String getCanvasName()
    {
        return _canvasName;
    }
    
    /**
     * Sets the canvas name upon which this blocks data will be displayed
     * 
     * @param canvasName
     *            The name of this blocks canvas
     */
    public void setCanvasName(String canvasName)
    {
        if (canvasName != null && canvasName.trim().length() == 0)
        {
            _canvasName = null;
        }
        else
        {
            _canvasName = canvasName;
        }
    }
    
    /**
     * Sets the rendering properties for this block
     * <p>
     * These properties are not used internally within the EntireJ Core
     * Framework but can be used by the applications rendering engine to display
     * the block
     * <p>
     * 
     * @param renderingProperties
     *            The rendering properties for this block
     */
    public void setBlockRendererProperties(EJFrameworkExtensionProperties renderingProperties)
    {
        _blockRendererProperties = renderingProperties;
    }
    
    /**
     * Returns the rendering properties for this block
     * <p>
     * These properties are not used internally within the EntireJ Core
     * Framework but can be used by the applications rendering engine to display
     * the block
     * <p>
     * 
     * @return The blocks rendering properties
     */
    public EJFrameworkExtensionProperties getBlockRendererProperties()
    {
        return _blockRendererProperties;
    }
    
    /**
     * Indicates inserts are allowed to be made on this block.
     * 
     * @return true if this block allows insert operations otherwise false
     */
    public boolean isInsertAllowed()
    {
        return _insertAllowed;
    }
    
    /**
     * Used to set the flag to indicate if insert operations are allowed on this
     * block
     * 
     * @param insertAllowed
     *            True if inserts are allowed, false if they are not.
     */
    public void setInsertAllowed(boolean insertAllowed)
    {
        _insertAllowed = insertAllowed;
    }
    
    /**
     * Indicates if updates are allowed on this blocks data.
     * 
     * @return The flag indicating if update operations are allowed on this
     *         block
     */
    public boolean isUpdateAllowed()
    {
        return _updateAllowed;
    }
    
    /**
     * Used to set the flag to indicate if update operations are allowed on this
     * block
     * 
     * @param updateAllowed
     *            True if updates are allowed, false if they are not.
     */
    public void setUpdateAllowed(boolean updateAllowed)
    {
        _updateAllowed = updateAllowed;
    }
    
    /**
     * Indicates if delete operations are allowed on this blocks data
     * 
     * @return <code>true</code> if delete operations are allowed otherwise
     *         <code>false</code>
     */
    public boolean isDeleteAllowed()
    {
        return _deleteAllowed;
    }
    
    /**
     * Used to set the flag to indicate if delete operations are allowed on this
     * block
     * 
     * @param deleteAllowed
     *            True if deletes are allowed, false if they are not.
     */
    public void setDeleteAllowed(boolean deleteAllowed)
    {
        _deleteAllowed = deleteAllowed;
    }
    
    /**
     * Indicates if queries are alowed on the data blocks defined by these block
     * properties
     * 
     * @return true if query operations are allowed otherwise false
     */
    public boolean isQueryAllowed()
    {
        if (_isControlBlock)
        {
            // No query is allowed within a control block
            return false;
        }
        
        return _queryAllowed;
    }
    
    /**
     * Used to set the flag to indicate if query operations are allowed on this
     * block
     * 
     * @param pUpdate
     *            True if query are allowed, false if they are not.
     */
    public void setQueryAllowed(boolean queryAllowed)
    {
        _queryAllowed = queryAllowed;
    }
    
    /**
     * Indicates if this block is displayed
     * 
     * @return <code>true</code> if this block is displayed to the user,
     *         otherwise <code>false</code>
     */
    public boolean isDisplayed()
    {
        if (_canvasName == null || _canvasName.trim().length() == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /**
     * Indicates that the blocks controller should ensure that all rows are
     * retrieved from the data accessor and not in pages
     * 
     * @param allRows
     *            <code>true</code> indicates that all rows will be retrieved.
     *            If <code>false</code> is set, then the data will be retrieved
     *            in pages
     */
    public void setQueryAllRows(boolean allRows)
    {
        _queryAllRows = allRows;
    }
    
    /**
     * Indicates if all rows should be retrieved for the block instead of in
     * pages
     * 
     * @return the query all rows indicator
     */
    public boolean queryAllRows()
    {
        return _queryAllRows;
    }
    
    /**
     * The maximum amount of records that should be selected for this block
     * 
     * @return The maximum amount of records to be selected for this block
     */
    public int getMaxResults()
    {
        return _maxResults;
    }
    
    /**
     * sets the maximum number of rows that should be selected for this block
     * 
     * @param maxResults
     *            The maximum number of rows to retrieve if queryAllRows is
     *            selected
     */
    public void setMaxResults(int maxResults)
    {
        _maxResults = maxResults;
    }
    
    /**
     * This is the amount of records the data accessor will retrieve when it
     * returns its rows in pages
     * 
     * @return The blocks paging size
     */
    public int getPageSize()
    {
        return _pageSize;
    }
    
    /**
     * Sets this blocks paging size
     * <p>
     * If a page size is <= 0, then this will indicate to the framework that no
     * limitation on page size should be made, and all records should be
     * selected
     * 
     * @param pageSize
     *            This blocks paging size
     */
    public void setPageSize(int pageSize)
    {
        if (pageSize < 0)
        {
            _pageSize = 0;
        }
        else
        {
            _pageSize = pageSize;
        }
    }
    
    public void setName(String name)
    {
        _name = name;
    }
    
    /**
     * Ads an <code>ItemProperties</code> object to this blocks item properties
     * store
     * 
     * @param itemProperties
     *            The Item Properties to be added
     */
    public void addItemProperties(EJCoreItemProperties itemProperties)
    {
        if (itemProperties != null)
        {
            _itemPropertiesContainer.addItemProperties(itemProperties);
        }
    }
    
    /**
     * Returns the <code>ItemContainer</code> containing all
     * <code>ItemProperties</code> contained within this block
     * 
     * @return The container containing all <code>ItemProperties</code> within
     *         this block
     */
    public EJCoreItemPropertiesContainer getItemPropertiesContainer()
    {
        return _itemPropertiesContainer;
    }
    
    /**
     * Get an <code>ItemsProperties</code> object for a given item. If the given
     * item name is null or invalid, or there exists no item for the given name,
     * then a <b>null</b> value will be returned.
     * 
     * @param itemName
     *            The name of the item to search for
     * @return The properties of the given item or null if an invalid or
     *         nonexistent item name was passed
     */
    public EJCoreItemProperties getItemProperties(String itemName)
    {
        if (itemName == null || itemName.trim().length() == 0)
        {
            return null;
        }
        else
        {
            if (_itemPropertiesContainer.contains(itemName))
            {
                return _itemPropertiesContainer.getItemProperties(itemName);
            }
            else
            {
                return null;
            }
        }
    }
    
    /**
     * Returns the properties of each item defined within this block
     * 
     * @return The properties of each item defined within this block
     */
    public Collection<EJItemProperties> getAllItemProperties()
    {
        
        return new ArrayList<EJItemProperties>(_itemPropertiesContainer.getAllItemProperties());
    }
    
    /**
     * Returns the screen item properties for the given item
     * 
     * @param screenType
     *            The screen to search
     * @param itemName
     *            The item that is required
     * @return The screen properties of the given item or <code>null</code> if
     *         there was no item with the given name on the given screen
     */
    public EJScreenItemProperties getScreenItemProperties(EJScreenType screenType, String itemName)
    {
        if (itemName == null)
        {
            return null;
        }
        
        switch (screenType)
        {
            case MAIN:
                return _mainScreenItemGroups.getScreenItemProperties(itemName);
            case INSERT:
                return _insertScreenItemGroups.getScreenItemProperties(itemName);
            case UPDATE:
                return _updateScreenItemGroups.getScreenItemProperties(itemName);
            case QUERY:
                return _queryScreenItemGroups.getScreenItemProperties(itemName);
            default:
                return null;
        }
    }
    
    /**
     * Returns the main screen properties for this block
     * 
     * @return the main screen properties for this block
     */
    public EJCoreMainScreenProperties getMainScreenProperties()
    {
        return _mainScreenProperties;
    }
    
    /**
     * Sets this blocks main screen properties
     * 
     * @param mainScreenProperties
     *            This blocks main screen properties
     */
    public void setMainScreenProperties(EJCoreMainScreenProperties mainScreenProperties)
    {
        if (mainScreenProperties == null)
        {
            return;
        }
        
        _mainScreenProperties = mainScreenProperties;
    }
    
    /**
     * All main screen items are placed on one or more ItemGroups
     * <p>
     * Use this method to add an <code>ItemGroupProperties</code> to this block
     * 
     * @param itemProperties
     *            The Item Properties to be added
     */
    public void addMainScreenItemGroup(EJCoreItemGroupProperties itemGroup)
    {
        if (itemGroup != null)
        {
            _mainScreenItemGroups.addItemGroupProperties(itemGroup);
        }
    }
    
    /**
     * All query screen items are placed on one or more ItemGroups
     * <p>
     * Use this method to add an <code>ItemGroupProperties</code> to this block
     * 
     * @param itemProperties
     *            The Item Properties to be added
     */
    public void addQueryScreenItemGroup(EJCoreItemGroupProperties itemGroup)
    {
        if (itemGroup != null)
        {
            _queryScreenItemGroups.addItemGroupProperties(itemGroup);
        }
    }
    
    /**
     * All insert screen items are placed on one or more ItemGroups
     * <p>
     * Use this method to add an <code>ItemGroupProperties</code> to this block
     * 
     * @param itemProperties
     *            The Item Properties to be added
     */
    public void addInsertScreenItemGroup(EJCoreItemGroupProperties itemGroup)
    {
        if (itemGroup != null)
        {
            _insertScreenItemGroups.addItemGroupProperties(itemGroup);
        }
    }
    
    /**
     * All update screen items are placed on one or more ItemGroups
     * <p>
     * Use this method to add an <code>ItemGroupProperties</code> to this block
     * 
     * @param itemProperties
     *            The Item Properties to be added
     */
    public void addUpdateScreenItemGroup(EJCoreItemGroupProperties itemGroup)
    {
        if (itemGroup != null)
        {
            _updateScreenItemGroups.addItemGroupProperties(itemGroup);
        }
    }
    
    /**
     * Adds a <code>LovMappingProperties</code> to this blocks lov mapping
     * container
     * <p>
     * If the mapping passed is null, it will not be added
     * 
     * @param mapping
     */
    public void addLovMapping(EJCoreLovMappingProperties mapping)
    {
        if (mapping != null)
        {
            _lovMappings.addLovDefinitionProperties(mapping);
        }
    }
    
    /**
     * Returns the <code>LovMappingContainer</code> that contains all lov
     * Mappings used within this block
     * 
     * @return The <code>LovMappingContainer</code> for this block
     */
    public EJCoreLovMappingPropertiesContainer getLovMappingContainer()
    {
        if(isMirrorChild())
        {
            return _formProperties.getBlockProperties(_mirrorBlockName).getLovMappingContainer();
        }
        return _lovMappings;
    }
    
    private void addGroupItems(EJItemGroupPropertiesContainer itemGroupContainer, ArrayList<String> itemNameList)
    {
        Iterator<EJItemGroupProperties> groupProperties = itemGroupContainer.getAllItemGroupProperties().iterator();
        while (groupProperties.hasNext())
        {
            EJItemGroupProperties itemGroupProperties = groupProperties.next();
            Iterator<EJScreenItemProperties> items = itemGroupProperties.getAllItemProperties().iterator();
            while (items.hasNext())
            {
                EJScreenItemProperties itemProps = items.next();
                itemNameList.add(itemProps.getReferencedItemName());
            }
            
            addGroupItems(itemGroupProperties.getChildItemGroupContainer(), itemNameList);
        }
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\nBlock: ");
        buffer.append(getName());
        buffer.append("\n  ServiceClassName:          " + _serviceClassName);
        buffer.append("\n  CanvasName:                " + _canvasName);
        buffer.append("\n  RendererName:              " + _blockRendererName);
        buffer.append("\n  InsertAllowed:             " + _insertAllowed);
        buffer.append("\n  UpdateAllowed:             " + _updateAllowed);
        buffer.append("\n  DeleteAllowed:             " + _deleteAllowed);
        buffer.append("\n  QueryAllowed:              " + _queryAllowed);
        
        buffer.append("\nITEMS:\n");
        
        Iterator<EJCoreItemProperties> blockItems = _itemPropertiesContainer.getAllItemProperties().iterator();
        while (blockItems.hasNext())
        {
            buffer.append(blockItems.next());
            buffer.append("\n");
        }
        
        return buffer.toString();
    }
    
    public int compareTo(EJCoreBlockProperties block)
    {
        return this.getName().compareTo(block.getName());
    }
    
    public String getReferencedBlockName()
    {
        return _referencedBlockName;
    }
    
    public void setReferencedBlockName(String _referencedBlockName)
    {
        this._referencedBlockName = _referencedBlockName;
    }
    
}
