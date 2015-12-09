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

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.enumerations.EJLineStyle;
import org.entirej.framework.core.enumerations.EJSeparatorOrientation;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJQueryScreenItemProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;

/**
 * Contains the properties required for the query screen
 * <p>
 * EntireJ requires the query screen to be displayed as a grid. Meaning each
 * item and the items label will be placed within a part of the grid using X and
 * Y coordinates.
 */
public class EJCoreQueryScreenItemProperties implements EJScreenItemProperties, EJQueryScreenItemProperties
{
    private EJCoreBlockProperties          _blockProperties;
    private EJCoreItemProperties           _referencedItemProperties;
    private EJFrameworkExtensionProperties _queryScreenRendererProperties;

    private String                          _itemLabel;
    private String                          _baseItemLabel;
    private String                          _itemHint;
    private String                          _baseItemHint;
    private String                          _referencedItemName    = "";
    private boolean                         _visible               = true;
    private boolean                         _editAllowed           = false;
    private boolean                         _mandatory             = false;
    private boolean                         _enableLovNotification = false;
    private String                          _lovMappingName;
    private boolean                         _validateFromLov       = true;
    private String                          _actionCommand         = "";
    private boolean                         _isSpacerItem          = false;
    private boolean                         _isSeparator           = false;
    private EJLineStyle                     _separatorLineStyle    = EJLineStyle.SOLID;
    private EJSeparatorOrientation          _separatorOrientation  = EJSeparatorOrientation.HORIZONTAL;
    private EJCoreVisualAttributeProperties _visualAttribute;

    public EJCoreQueryScreenItemProperties(EJCoreBlockProperties blockProperties, boolean isSpacer)
    {
        _blockProperties = blockProperties;
        _isSpacerItem = isSpacer;
    }

    public void setIsSpacerItem(boolean isSpacerItem)
    {
        _isSpacerItem = isSpacerItem;
    }

    public boolean isSpacerItem()
    {
        return _isSpacerItem;
    }

    /**
     * Returns the name of the data block item to which this item references
     * <p>
     * Each item that is displayed on the query screen can reference a block
     * item. If a reference is specified, then value on the query screen will be
     * used when making the blocks query. All other items will be display items
     * only and have no effect on the query being made
     * 
     * @return The name of the block item that this item references
     */
    public String getReferencedItemName()
    {
        return _referencedItemName;
    }

    /**
     * Returns the label defined for this block item
     * <p>
     * It is the <code>BlockRenderer</code> that decides if and how the items
     * label should be displayed
     * 
     * @return The label defined for this item
     * @see #getBaseLabel()
     */
    public String getLabel()
    {
        return _itemLabel;
    }

    /**
     * Returns the untranslated label for this item
     * 
     * @return The untranslated item label
     * @see #getLabel()
     */
    public String getBaseLabel()
    {
        return _baseItemLabel;
    }

    /**
     * Sets this items base label
     * <p>
     * A base label is an untranslated label. This label will be passed to the
     * TranslationController for translation and the translated value will be
     * available in <code>{@link #getLabel()}</code>
     * 
     * @param label
     *            This items label
     */
    public void setBaseLabel(String label)
    {
        if (label != null && label.trim().length() == 0)
        {
            _baseItemLabel = null;
        }
        else
        {
            _baseItemLabel = label;
        }

        _itemLabel = _baseItemLabel;
    }

    /**
     * Used to set the translated label
     * <p>
     * EntireJ will retrieve the base label and use the assigned framework
     * translator to translate the text. Once translated, EntireJ will assign
     * the translated text via this method
     * 
     * @param translatedLabel
     *            The translated label
     */
    public void setTranslatedLabel(String translatedLabel)
    {
        _itemLabel = translatedLabel;
    }

    /**
     * Returns the hint defined for this block item
     * <p>
     * It is the <code>ItemRenderer</code> that decides if and how the items
     * hint should be displayed
     * 
     * @return The label defined for this item
     */
    public String getHint()
    {
        return _itemHint;
    }

    /**
     * Returns the untranslated hint for this item
     * 
     * @return The untranslated item hint
     * @see #getHint()
     */
    public String getBaseHint()
    {
        return _baseItemHint;
    }

    /**
     * Sets this items base hint
     * <p>
     * A base hint is an untranslated hint. All hints will be translated using
     * the applications translator. If there is no translator defined for the
     * application then the base hint will be used for the items hint
     * 
     * @param hint
     *            This items base hint
     */
    public void setBaseHint(String hint)
    {
        if (hint != null && hint.trim().length() == 0)
        {
            _baseItemHint = null;
        }
        else
        {
            _baseItemHint = hint;
        }
        _itemHint = _baseItemHint;
    }

    /**
     * Used to set the translated hint
     * <p>
     * EntireJ will retrieve the base hint and use the assigned framework
     * translator to translate the text. Once translated, EntireJ will assign
     * the translated text via this method
     * 
     * @param translatedHint
     *            The translated hint
     */
    public void setTranslatedHint(String translatedHint)
    {
        _itemHint = translatedHint;
    }

    /**
     * Sets the name of the block item to which this item references
     * <p>
     * 
     * @return The name of the block item
     * @throws EJApplicationException
     *             if the there is no item with the given name on the block to
     *             which this screen belongs
     */
    public void setReferencedItemName(String name)
    {
        _referencedItemName = name;
        if (!_isSpacerItem)
        {
            EJCoreItemProperties itemProperties = _blockProperties.getItemProperties(name);
            if (itemProperties == null)
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_SCREEN_ITEM_REFERENCE, "query",
                        _blockProperties.getName(), name));
            }
            else
            {
                _referencedItemProperties = itemProperties;
            }
        }
    }

    /**
     * Returns the <code>ItemProperties</code> to which this query screen item
     * references
     * 
     * @return The referenced items properties
     */
    public EJCoreItemProperties getReferencedItemProperties()
    {
        return _referencedItemProperties;
    }

    /**
     * Returns the <code>RenderingProperties</code> that are required by the
     * blocks query screen renderer
     * 
     * @return The required blocks query screen renderer properties
     */
    public EJFrameworkExtensionProperties getQueryScreenRendererProperties()
    {
        return _queryScreenRendererProperties;
    }

    /**
     * Sets the <code>RenderingProperties</code> that are required by the blocks
     * query screen renderer
     * 
     * @param properties
     *            The required blocks query screen renderer properties
     */
    public void setQueryScreenRendererProperties(EJFrameworkExtensionProperties properties)
    {
        _queryScreenRendererProperties = properties;
    }

    public EJCoreVisualAttributeProperties getVisualAttributeProperties()
    {
        return _visualAttribute;
    }

    /**
     * Sets the name of the visual attribute that should be used for this item
     * <p>
     * Setting the visual attribute to a <code>null</code> will result in the
     * visual attribute being removed and the item being set to its original
     * state
     * 
     * @param name
     *            The name of the visual attribute to be set on the item
     */
    public void setVisualAttributeName(String name)
    {
        if (name == null || name.trim().length() == 0)
        {
            _visualAttribute = null;
        }
        else if (!EJCoreProperties.getInstance().getVisualAttributesContainer().contains(name))
        {
            throw new IllegalArgumentException("There is no visual attribute with the name " + name + " defined within this application.");
        }

        _visualAttribute = EJCoreProperties.getInstance().getVisualAttributesContainer().getVisualAttributeProperties(name);
    }

    /**
     * Indicates if this item is to be visible
     * <p>
     * 
     * @return <code>true</code> if the item should be visible, otherwise
     *         <code>false</code>
     */
    public boolean isVisible()
    {
        return _visible;
    }

    /**
     * If set to <code>true</code>, this item will be visible
     * 
     * @param visible
     */
    public void setVisible(boolean visible)
    {
        _visible = visible;
    }

    /**
     * If set to <code>true</code>, users will be able to modify this items
     * value
     * 
     * @param editAllowed
     */
    public void setEditAllowed(boolean editAllowed)
    {
        _editAllowed = editAllowed;
    }

    /**
     * Indicates if this item can be modified
     * <p>
     * 
     * @return <code>true</code> if the item should is editable, otherwise
     *         <code>false</code>
     */
    public boolean isEditAllowed()
    {
        return _editAllowed;
    }

    /**
     * Indicates that a value is required by this item during query operations
     * <p>
     * EntireJ will ensure that a value has been entered before issuing the
     * query
     * 
     * @param mandatory
     *            The mandatory flag
     */
    public void setMandatory(boolean mandatory)
    {
        _mandatory = mandatory;
    }

    /**
     * Indicates that a value is required during query operations
     * <p>
     * EntireJ will ensure that a value has been entered before issuing the
     * query
     * 
     * @return The mandatory indicator
     */
    public boolean isMandatory()
    {
        return _mandatory;
    }

    /**
     * Sets the name of the lov mapping assigned to this item
     * <p>
     * If an item has an lov mapping assigned, the item will respond to the user
     * opening a lov screen from the item. How the activation is implemented is
     * dependent on how the GUI is implemented
     * 
     * @param lovMappingName
     *            The name of the lov mapping to assign
     */
    public void setLovMappingName(String lovMappingName)
    {
        _lovMappingName = lovMappingName;
    }

    /**
     * Returns the name of the lov mapping assigned to this item
     * 
     * @return The lov mapping name assigned to this item or <code>null</code>
     *         if none was assigned
     */
    public String getLovMappingName()
    {
        return _lovMappingName;
    }

    /**
     * Sets the enabled flag of Lov Notification
     * <p>
     * Lov Notification is automatically enabled for items with an lov attached.
     * However it is also possible to enable lov notification for non lov items.
     * If this is the case, then EJ will call the action processor to notify the
     * developer that the lov has been activated, but no LOV will be displayed.
     * The developer can then do what is required for the specific business
     * case, e.g. Check the value entered against a Business Service or call
     * another form etc
     * 
     * @param enable
     *            Enables lov notification
     */
    public void enableLovNotification(boolean enable)
    {
        _enableLovNotification = enable;
    }

    /**
     * Indicates if lov notification has been enabled
     * <p>
     * Lov Notification is automatically enabled for items with an lov attached.
     * However it is also possible to enable lov notification for non lov items.
     * If this is the case, then EJ will call the action processor to notify the
     * developer that the lov has been activated, but no LOV will be displayed.
     * The developer can then do what is required for the specific business
     * case, e.g. Check the value entered against a Business Service or call
     * another form etc
     * 
     * @return <code>true</code> if lov notification has been enabled, otherwise
     *         <code>false</code>
     */
    public boolean isLovNotificationEnabled()
    {
        return _enableLovNotification;
    }

    public void setValidateFromLov(boolean validateFromLov)
    {
        _validateFromLov = validateFromLov;
    }

    public boolean validateFromLov()
    {
        return _validateFromLov;
    }

    public String getActionCommand()
    {
        return _actionCommand;
    }

    public void setActionCommand(String actionCommand)
    {
        _actionCommand = actionCommand;
    }

    @Override
    public EJLineStyle getSeparatorLineStyle()
    {
        return _separatorLineStyle;
    }

    @Override
    public EJSeparatorOrientation getSeparatorOrientation()
    {
        return _separatorOrientation;
    }

    @Override
    public boolean isSeparator()
    {
        return _isSeparator;
    }

    public void setSeparatorLineStyle(EJLineStyle separatorLineStyle)
    {
        _separatorLineStyle = separatorLineStyle;
    }

    public void setSeparatorOrientation(EJSeparatorOrientation separatorOrientation)
    {
        _separatorOrientation = separatorOrientation;
    }

    public void setSeparator(boolean isSeparator)
    {
        _isSeparator = isSeparator;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("\nItem: ");
        buffer.append("\n    ReferencedBlockItemName: ");
        buffer.append(_referencedItemName);
        buffer.append("\n    Manditory:               ");
        buffer.append(_mandatory);
        buffer.append("      EditAllowed:             ");
        buffer.append(_editAllowed);

        return buffer.toString();
    }

}
