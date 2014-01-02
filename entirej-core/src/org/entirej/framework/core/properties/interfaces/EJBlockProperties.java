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
package org.entirej.framework.core.properties.interfaces;

import java.io.Serializable;
import java.util.Collection;

import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.properties.containers.interfaces.EJItemGroupPropertiesContainer;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.service.EJBlockService;

public interface EJBlockProperties extends Serializable
{
    /**
     * Returns the main screen properties for this block
     * 
     * @return the main screen properties for this block
     */
    public EJMainScreenProperties getMainScreenProperties();
    
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
    public EJScreenItemProperties getScreenItemProperties(EJScreenType screenType, String itemName);
    
    /**
     * Returns the properties of each item defined within this block
     * 
     * @return The properties of each item defined within this block
     */
    public Collection<EJItemProperties> getAllItemProperties();
    
    /**
     * Returns the item properties for the given item
     * 
     * @param itemName
     *            The item name
     * @return The properties of the given item
     */
    public EJItemProperties getItemProperties(String itemName);
    
    /**
     * Returns the rendering properties defined for the insert screen renderer
     * assigned to this block
     * 
     * @return The insert screen rendering properties
     */
    public EJFrameworkExtensionProperties getInsertScreenRendererProperties();
    
    /**
     * Returns the name of the canvas upon which this blocks data should be
     * displayed or <code>null</code> if no canvas has been defined for the
     * block
     * <p>
     * If the canvas that was chosen is a type of TAB then the blocks data
     * should be displayed on the correct tab page
     * <p>
     * If no canvas has been defined for this block, then the block will not be
     * displayed anywhere
     * <p>
     * 
     * @return the name of the canvas upon which the blocks data will be
     *         displayed or <code>null</code> if no canvas has been defined
     */
    public String getCanvasName();
    
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
    public boolean isControlBlock();
    
    /**
     * Indicates if this block is a referenced block
     * 
     * @return <code>true</code> if this is a referenced block otherwise
     *         <code>false</code>
     */
    public boolean isReferenceBlock();
    
    /**
     * This method is used within the EntireJ Plugin
     * 
     * @return <code>null</code>
     */
    public String getDescription();
    
    /**
     * Indicates if this block is a mirror block
     * <p>
     * the MirrorParent defines with which block this block should synchronize
     * with
     * 
     * @return <code>true</code> if this is a mirror block otherwise
     *         <code>false</code>
     */
    public boolean isMirrorBlock();
    
    /**
     * Returns the name of the block that this block is the mirror of
     * 
     * @return The name of the mirror block
     */
    public String getMirrorBlockName();
    
    /**
     * Returns the properties of the Form to which this block belongs
     * 
     * @return The form properties
     */
    public EJFormProperties getFormProperties();
    
    /**
     * Indicates if this block is used within an LOV Definition
     * 
     * @return <code>true</code> if this block is used within a lov definition,
     *         otherwise <code>false</code>
     */
    public boolean isUsedInLovDefinition();
    
    /**
     * Returns the lov definition properties this block belongs too, if this
     * block is part of a <code>LovDefinition</code>
     * 
     * @return The <code>LovDefinitionProperties</code> of this block if it is
     *         used within a lov definition
     */
    public EJLovDefinitionProperties getLovDefinition();
    
    /**
     * Returns the internal name of this block
     * 
     * @return The name of this block
     */
    public String getName();
    
    /**
     * The block renderer is the class that will be responsible for displaying
     * the contents of this block
     * 
     * @return the blockRendererClassName
     */
    public String getBlockRendererName();
    
    /**
     * Gets the fully qualified class name of the service that is responsible
     * for the retrieval and modification of this blocks data
     * 
     * @return The fully qualified class name of the service that is responsible
     *         for the retrieval and modification of this blocks data
     */
    public String getServiceClassName();
    
    /**
     * Returns the service used to retrieve and manipulate the data of this
     * block
     * 
     * @return This blocks service
     */
    public EJBlockService<?> getBlockService();
    
    /**
     * The Action Processor is responsible for actions within this block
     * <p>
     * Actions can include buttons being pressed, check boxes being selected or
     * pre-post query methods etc.
     * 
     * @return The name of the Action Processor responsible for this form.
     */
    public String getActionProcessorClassName();
    
    /**
     * Returns the rendering properties defined for the query screen renderer
     * assigned to this block
     * 
     * @return The query screen rendering properties
     */
    public EJFrameworkExtensionProperties getQueryScreenRendererProperties();
    
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
    public Collection<String> getScreenItemNames(EJScreenType screenType);
    
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
    public EJItemGroupPropertiesContainer getScreenItemGroupContainer(EJScreenType screenType);
    
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
    public EJFrameworkExtensionProperties getBlockRendererProperties();
    
    /**
     * Indicates if all rows should be retrieved for the block instead of in
     * pages
     * 
     * @return the query all rows indicator
     */
    public boolean queryAllRows();
    
    /**
     * The maximum amount of records that should be selected for this block
     * 
     * @return The maximum amount of records to be selected for this block
     */
    public int getMaxResults();
    
    /**
     * This is the amount of records the data service will retrieve when it
     * returns its rows in pages
     * 
     * @return The blocks paging size
     */
    public int getPageSize();
    
    /**
     * Indicates inserts are allowed to be made on this block.
     * 
     * @return true if this block allows insert operations otherwise false
     */
    public boolean isInsertAllowed();
    
    /**
     * Indicates if updates are allowed on this blocks data.
     * 
     * @return The flag indicating if update operations are allowed on this
     *         block
     */
    public boolean isUpdateAllowed();
    
    /**
     * Indicates if delete operations are allowed on this blocks data
     * 
     * @return <code>true</code> if delete operations are allowed otherwise
     *         <code>false</code>
     */
    public boolean isDeleteAllowed();
    
    /**
     * Indicates if queries are allowed on the data blocks defined by these
     * block properties
     * 
     * @return true if query operations are allowed otherwise false
     */
    public boolean isQueryAllowed();
    
}
