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
package org.entirej.framework.core;

import java.io.Serializable;

import org.entirej.framework.core.data.EJDataItem;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJBlockRendererController;
import org.entirej.framework.core.enumerations.EJManagedScreenProperty;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;

/**
 * Contains the actual data for a specific item.
 */
public class EJItem implements Serializable
{
    private EJInternalBlock _block;
    private EJDataItem      _dataItem;
    private EJDataRecord    _record;
    
    public EJItem(EJInternalBlock block, EJDataRecord record, EJDataItem item)
    {
        _block = block;
        _record = record;
        _dataItem = item;
    }
    
    /**
     * Return the name of this item
     * 
     * @return This items name
     */
    public String getName()
    {
        return _dataItem.getName();
    }
    
    /**
     * Sets the item with the given name to the given value
     * 
     * @param value
     *            The value to set
     */
    public void setValue(Object value)
    {
        _dataItem.setValue(value);
    }
    
    /**
     * Used to set the item instance visual attribute
     * <P>
     * An item instance visual attribute will be displayed when the record this
     * item is contained within is displayed to the user. Depending on how the
     * block renderer is displaying the blocks records will depend on how this
     * visual attribute is displayed. If for example the block is displaying
     * records in a table, then the item instance visual attribute will be a
     * cell in the table. Whereas setting the screen item visual attribute will
     * set the visual attribute to the entire column
     * <p>
     * Setting the visual attribute to <code>null</code> will return the item
     * instance to its default visual attributes
     * <p>
     * 
     * @param visualAttributeName
     *            The name of the visual attribute to set
     * @throws {@link IllegalArgumentException} if there is no visual attribute
     *         with the given name
     * 
     * @see #getVisualAttribute()
     */
    public void setVisualAttribute(String visualAttributeName)
    {
        _dataItem.setVisualAttribute(visualAttributeName);
        EJBlockRendererController blockRendererController = _block.getRendererController();
        if (blockRendererController != null)
        {
            try
            {
                blockRendererController.refreshItemProperty(_dataItem.getName(), EJManagedScreenProperty.ITEM_INSTANCE_VISUAL_ATTRIBUTE, _record);
            }
            catch (Exception e)
            {
                _block.getFrameworkManager().handleException(e);
            }
        }
        
    }
    
    
    /**
     * Used to set the item instance visual attribute
     * <P>
     * An item instance visual attribute will be displayed when the record this
     * item is contained within is displayed to the user. Depending on how the
     * block renderer is displaying the blocks records will depend on how this
     * visual attribute is displayed. If for example the block is displaying
     * records in a table, then the item instance visual attribute will be a
     * cell in the table. Whereas setting the screen item visual attribute will
     * set the visual attribute to the entire column
     * <p>
     * Setting the visual attribute to <code>null</code> will return the item
     * instance to its default visual attributes
     * <p>
     * 
     * @param visualAttribute
     *            The visual attribute to set
     *             
     * @see #getVisualAttribute()
     */
    public void setVisualAttribute(EJCoreVisualAttributeProperties visualAttribute)
    {
        _dataItem.setVisualAttribute(visualAttribute);
        EJBlockRendererController blockRendererController = _block.getRendererController();
        if (blockRendererController != null)
        {
            try
            {
                blockRendererController.refreshItemProperty(_dataItem.getName(), EJManagedScreenProperty.ITEM_INSTANCE_VISUAL_ATTRIBUTE, _record);
            }
            catch (Exception e)
            {
                _block.getFrameworkManager().handleException(e);
            }
        }
        
    }
    
    
    /**
     * Returns the visual attribute properties set on this item or
     * <code>null</code> if no visual attribute has been set
     * 
     * @return The visual attribute properties of this item or <code>null</code>
     *         if no visual attribute has been set
     * 
     * @see #setVisualAttribute(String)
     */
    public EJCoreVisualAttributeProperties getVisualAttribute()
    {
        return _dataItem.getVisualAttribute();
    }
    
    /**
     * Used to set the item instance tool tip text
     * <P>
     * An item instance tool tip will be displayed when the record this item is
     * contained within is displayed to the user. Depending on how the block
     * renderer is displaying the blocks records will depend on how this tool
     * tip is displayed. If for example the block is displaying records in a
     * table, then the item instance tool tip will be a cell in the table.
     * Whereas setting the screen item tool tip will set the tool tip to the
     * entire column
     * <p>
     * Setting the tool tip to <code>null</code> will remove the tool tip
     * <p>
     * 
     * @param text
     *            The tool tip text to set
     * 
     * @see #setHint(String, boolean)
     * @see #getHint()
     */
    public void setHint(String hint)
    {
        setHint(hint, false);
    }
    
    /**
     * Used to set the item instance tool tip text
     * <P>
     * An item instance tool tip will be displayed when the record this item is
     * contained within is displayed to the user. Depending on how the block
     * renderer is displaying the blocks records will depend on how this tool
     * tip is displayed. If for example the block is displaying records in a
     * table, then the item instance tool tip will be a cell in the table.
     * Whereas setting the screen item tool tip will set the tool tip to the
     * entire column
     * <p>
     * Setting the tool tip to <code>null</code> will remove the tool tip
     * <p>
     * 
     * @param text
     *            The tool tip text to set
     * @param translate
     *            Indicates if the text specified should be translated using the
     *            projects translator, before displaying the hint
     * 
     * @see #setHint(String)
     * @see #getHint()
     */
    public void setHint(String hint, boolean translate)
    {
        String hintToSet = hint;
        if (translate && hint != null)
        {
            hintToSet = _block.getForm().translateText(hint);
        }
        
        _dataItem.setHint(hintToSet);
        
        EJBlockRendererController blockRendererController = _block.getRendererController();
        if (blockRendererController != null)
        {
            try
            {
                blockRendererController.refreshItemProperty(_dataItem.getName(), EJManagedScreenProperty.ITEM_INSTANCE_HINT_TEXT, _record);
            }
            catch (Exception e)
            {
                _block.getFrameworkManager().handleException(e);
            }
        }
    }
    
    /**
     * Returns the hint text set on this item or <code>null</code> if no hint
     * has been set
     * 
     * @return The hint set to this item or <code>null</code> if not text has
     *         been displayed
     */
    public String getHint()
    {
        return _dataItem.getHint();
    }
    
    /**
     * Used to retrieve this items value
     * 
     * @return The items value
     */
    public Object getValue()
    {
        return _dataItem.getValue();
    }
    
    public String toString()
    {
        return _dataItem.toString();
    }
    
}
