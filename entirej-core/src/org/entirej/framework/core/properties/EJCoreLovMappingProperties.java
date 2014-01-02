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
package org.entirej.framework.core.properties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.EJFrameworkManager;

public class EJCoreLovMappingProperties  implements Serializable
{
    private String                                  _lovMappingName;
    private String                                  _lovDefinitionName;
    private String                                  _lovRendererName    = "";
    private String                                  _lovDisplayName     = "";
    private String                                  _baseLovDisplayName = "";
    private boolean                                 _executeAfterQuery  = true;
    private EJCoreBlockProperties                   _mappedBlock;
    private List<EJCoreLovItemMappingProperties> _lovItemMappings;
    
    public EJCoreLovMappingProperties(EJFrameworkManager manager, EJCoreBlockProperties mappedBlock, String lovMappingName)
    {
        _mappedBlock = mappedBlock;
        _lovMappingName = lovMappingName;
        _lovItemMappings = new ArrayList<EJCoreLovItemMappingProperties>();
    }
    
    public String getName()
    {
        return _lovMappingName;
    }
    
    public void internalSetName(String name)
    {
        _lovMappingName = name;
    }
    

    
    public void setExecuteAfterQuery(boolean executeAfterQuery)
    {
        _executeAfterQuery = executeAfterQuery;
    }
    
    public boolean executeAfterQuery()
    {
        return _executeAfterQuery;
    }
    
    /**
     * Set the name of the lov definition upon which this mapping is based
     * <p>
     * 
     * @param definitionName
     *            The lov definition name
     */
    public void setLovDefinitionName(String definitionName)
    {
        _lovDefinitionName = definitionName;
    }
    
    /**
     * Returns the name of the lov definition upon which this mapping is based
     * 
     * @return The name of the lov mapping
     */
    public String getLovDefinitionName()
    {
        return _lovDefinitionName;
    }
    
    /**
     * Returns the <code>LovDefinitionProperties</code> for this mapping
     * 
     * @return The <code>LovDefinitionProperties</code>, or null if none has
     *         been set
     */
    public EJCoreLovDefinitionProperties getLovDefinitionProperties()
    {
        if(_mappedBlock.isReferenceBlock())
            return _mappedBlock.getFormProperties().getLovDefinitionProperties(String.format("%s.%s", _mappedBlock.getReferencedBlockName(),_lovDefinitionName));
        
        return _mappedBlock.getFormProperties().getLovDefinitionProperties(_lovDefinitionName);
    }
    
    /**
     * Clears all item mappings creates within this lov mapping
     */
    public void clearAllDefinitionMappings()
    {
        _lovItemMappings.clear();
    }
    
    /**
     * Returns the mapped block for this lov
     * <p>
     * The mapped block is the block to which EntreJ will copy lov values to
     * 
     * @return The mapped block of this lov
     */
    public EJCoreBlockProperties getMappedBlock()
    {
        return _mappedBlock;
    }
    
    /**
     * Returns the set of item mappings that are available within this lov
     * mapping properties
     * 
     * @return The set of <code>LovItemMappingProperties</code>
     */
    public Collection<EJCoreLovItemMappingProperties> getAllItemMappingProperties()
    {
        return _lovItemMappings;
    }
    
    /**
     * Indicates if an Item Mapping already exists for the given block item name
     * 
     * @param blockItemName
     *            The block item name
     * @return <code>true</code> if an item mapping already exists otherwise
     *         <code>false</code>
     */
    public boolean containsItemMappingForBlockItem(String blockItemName)
    {
        Iterator<EJCoreLovItemMappingProperties> itemMappings = getAllItemMappingProperties().iterator();
        while (itemMappings.hasNext())
        {
            EJCoreLovItemMappingProperties props = itemMappings.next();
            if ((props.getBlockItemName().equalsIgnoreCase(blockItemName)))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Retrieves the definition item name for a given block item name or
     * <code>null</code> if there is no mapping for the given block item
     * 
     * @param blockItemName
     *            The name of the block item
     * @return The name of the mapped lov definition item or <code>null</code>
     *         if there is no item with the given name
     */
    public String getLovDefinitionItemForBlockItem(String blockItemName)
    {
        Iterator<EJCoreLovItemMappingProperties> itemMappings = getAllItemMappingProperties().iterator();
        while (itemMappings.hasNext())
        {
            EJCoreLovItemMappingProperties props = itemMappings.next();
            if ((props.getBlockItemName().equalsIgnoreCase(blockItemName)))
            {
                return props.getLovDefinitionItemName();
            }
        }
        return null;
    }
    
    /**
     * Indicates if an Item Mapping already exists for the given lov definition
     * name
     * 
     * @param lovDefinitionItem
     *            The name of the lov definition item
     * @return <code>true</code> if an item mapping already exists otherwise
     *         <code>false</code>
     */
    public boolean containsItemMappingForLovDefinitionItem(String lovDefinitionItem)
    {
        Iterator<EJCoreLovItemMappingProperties> itemMappings = getAllItemMappingProperties().iterator();
        while (itemMappings.hasNext())
        {
            EJCoreLovItemMappingProperties props = itemMappings.next();
            if ((props.getLovDefinitionItemName().equalsIgnoreCase(lovDefinitionItem)))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Retrieves the block item name for a given lov definition item name or
     * <code>null</code> if there is no mapping for the given lov definition
     * item
     * 
     * @param lovDefinitionItem
     *            The name of the lov definition item
     * @return The name of the mapped block item or <code>null</code> if there
     *         is no item with the given name
     */
    public String getBlockItemNameForLovDefinitionItem(String lovDefinitionItem)
    {
        Iterator<EJCoreLovItemMappingProperties> itemMappings = getAllItemMappingProperties().iterator();
        while (itemMappings.hasNext())
        {
            EJCoreLovItemMappingProperties props = itemMappings.next();
            if ((props.getLovDefinitionItemName().equalsIgnoreCase(lovDefinitionItem)))
            {
                return props.getBlockItemName();
            }
        }
        return null;
    }
    
    /**
     * Sets the mapping between the given lov definition item and the block item
     * <p>
     * If there is no lov definition item with the given name, then no mapping
     * will be made
     * 
     * @param lovDefinitionItemName
     *            The name of the lov definition item
     * @param blockItemName
     *            The name of the block item
     */
    public void setMappingItem(String lovDefinitionItemName, String blockItemName)
    {
        Iterator<EJCoreLovItemMappingProperties> itemMappings = getAllItemMappingProperties().iterator();
        while (itemMappings.hasNext())
        {
            EJCoreLovItemMappingProperties props = itemMappings.next();
            if ((props.getLovDefinitionItemName().equalsIgnoreCase(lovDefinitionItemName)))
            {
                props.setBlockItemName(blockItemName);
                break;
            }
        }
    }
    
    /**
     * Adds a new item map to this lov mapping
     * <p>
     * If there is already a mapping for the given item definition item name or
     * the name is empty, then nothing will be done
     * 
     * @param position
     *            The position of the mapping item within this lov mapping
     * @param lovDefinitionItemName
     *            The name of the lov definition item
     * @param blockItemName
     *            The name of the block item
     */
    public void addMappingItem( String lovDefinitionItemName, String blockItemName)
    {
        if (lovDefinitionItemName == null || lovDefinitionItemName.trim().length() == 0)
        {
            return;
        }
        
        if (!containsItemMappingForLovDefinitionItem(lovDefinitionItemName))
        {
            EJCoreLovItemMappingProperties props = new EJCoreLovItemMappingProperties( lovDefinitionItemName, (blockItemName == null ? ""
                    : blockItemName));
            _lovItemMappings.add(props);
        }
    }
    
    /**
     * The name of the renderer used for the display of this lov
     * <p>
     * All renderers are defined within the <b>EntireJ Properties</b>
     * 
     * @return The renderer for this lov
     */
    public String getLovRendererName()
    {
        return _lovRendererName;
    }
    
    public void setLovRendererName(String name)
    {
        _lovRendererName = name;
    }
    
    /**
     * Sets the display name for the lov
     * <p>
     * The base lov display name is the un-translated display name for the lov.
     * The base name will be translated by the applications translator
     * 
     * @param name
     *            The display name
     */
    public void setBaseLovDisplayName(String name)
    {
        if (name != null && name.trim().length() == 0)
        {
            _baseLovDisplayName = null;
        }
        else
        {
            _baseLovDisplayName = name;
        }
        _lovDisplayName = _baseLovDisplayName;
    }
    
    public String getBaseLovDisplayName()
    {
        return _baseLovDisplayName;
    }
    
    /**
     * Returns the name that the lov renderer should display for this lov
     * 
     * @return The lov display name
     */
    public String getLovDisplayName()
    {
        return _lovDisplayName;
    }
    
    /**
     * Used to set the translated display name
     * <p>
     * EntireJ will retrieve the base love display name and use the assigned
     * framework translator to translate the text. Once translated, EntireJ will
     * assign the translated text via this method
     * 
     * @param translatedDisplayName
     *            The translated display name
     */
    public void setTranslatedLovDisplayName(String translatedDisplayName)
    {
        _lovDisplayName = translatedDisplayName;
    }
    

    
}
