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
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;

import org.entirej.framework.core.EJScreenItem;
import org.entirej.framework.core.enumerations.EJLovDisplayReason;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.EJCoreLovMappingProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.renderers.EJManagedItemRendererWrapper;
import org.entirej.framework.core.renderers.interfaces.EJItemRenderer;
import org.entirej.framework.core.renderers.registry.EJBlockItemRendererRegister;

public class EJItemLovController implements Serializable
{
    private EJFormController            _formController;
    private EJCoreLovMappingProperties  _mappingProperties;
    private EJScreenItemController      _item;
    private EJLovController             _lovController;

    public EJItemLovController(EJFormController formController, EJScreenItemController item, EJCoreLovMappingProperties mappingProperties)
    {
        _item = item;
        _mappingProperties = mappingProperties;
        _formController = formController;
        if (mappingProperties != null)
        {
            _lovController = formController.getLovController(mappingProperties.getLovDefinitionProperties().getName());
        }
    }
    
    EJBlockItemRendererRegister getItemRendererRegister()
    {
        return _item.getItemRendererRegister();
    }
    
    public EJScreenItemController getItemToValidate()
    {
        return _item;
    }
    
    public EJManagedLovRendererController getManagedRenderer()
    {
        return _lovController.getManagedRendererController();
    }
    
    public EJCoreLovDefinitionProperties getLovDefinitionProperties()
    {
        return _lovController.getDefinitionProperties();
    }
    
    public EJCoreLovMappingProperties getLovMappingProperties()
    {
        return _mappingProperties;
    }
    
    public EJManagedItemRendererWrapper getManagedLovItemRenderer()
    {
        if (_item == null)
        {
            return null;
        }
        
        return _item.getManagedItemRenderer();
    }
    
    public EJItemRenderer getLovItemRenderer()
    {
        if (_item == null)
        {
            return null;
        }
        
        return _item.getItemRenderer();
    }
    
    /**
     * Displays the lov that is has been defined for the given item
     * <p>
     * If the user chooses a value from the lov then the values will be copied
     * to the given record using the lov mapping properties
     * 
     * @param displayReason
     *            The reason the lov is being displayed
     */
    public void displayLov(EJLovDisplayReason displayReason)
    {
        // An item can have lov notification enabled but not actually have an
        // lov behind it. If this is the case, call the action processor to
        // inform the developer that the lov has been activated but don't
        // execute any lov.
        if (getScreenProperties().isLovNotificationEnabled()|| _mappingProperties!=null)
        {
            EJScreenItem screenItem = new EJScreenItem(_item.getBlock(), _item.getScreenType(), _item.getBlock().getScreenItem(_item.getScreenType(),
                    _item.getProperties().getReferencedItemName()));
            _formController.getManagedActionController().lovActivated(_formController.getEJForm(), screenItem, displayReason);
        }
        
        if (!isLovActivationEnabled())
        {
            return;
        }
        
        if (_lovController != null)
        {
            _lovController.displayLov(this, displayReason);
        }
    }
    
    public boolean isLovActivationEnabled()
    {
        EJScreenItemProperties props = getScreenProperties();
        if (props != null && props.getLovMappingName() != null)
        {
            return true;
        }
        
        if (_mappingProperties != null)
        {
            return true;
        }
        return false;
    }
    
    private EJScreenItemProperties getScreenProperties()
    {
        return _item.getProperties();
    }
    
    public void validateItem(Object value)
    {
        if (getScreenProperties().isLovNotificationEnabled() || _mappingProperties!=null)
        {
            EJScreenItem screenItem = new EJScreenItem(_item.getBlock(), _item.getScreenType(), _item.getBlock().getScreenItem(_item.getScreenType(),
                    _item.getProperties().getReferencedItemName()));
            _formController.getManagedActionController().lovActivated(_formController.getEJForm(), screenItem, EJLovDisplayReason.VALIDATE);
        }
        
        if (!isLovActivationEnabled())
        {
            return;
        }
        
        if (value == null)
        {
            _lovController.clearAllValues(getItemRendererRegister(), _mappingProperties, _item);
            notifyLovCompleted(false);
            return;
        }
        
        if (!_item.validateFromLov())
        {
            return;
        }
        
        String lovDefItemName = _mappingProperties.getLovDefinitionItemForBlockItem(_item.getReferencedItemProperties().getName());
        _lovController.validateItem(getItemRendererRegister(), _mappingProperties, _item, value, lovDefItemName);
    }
    
    private void notifyLovCompleted(boolean valueChosen)
    {
        if (getScreenProperties().isLovNotificationEnabled() || _mappingProperties!=null)
        {
            EJScreenItem screenItem = new EJScreenItem(_item.getBlock(), _item.getScreenType(), _item.getBlock().getScreenItem(_item.getScreenType(),
                    _item.getProperties().getReferencedItemName()));
            _formController.getManagedActionController().lovCompleted(_formController.getEJForm(), screenItem, valueChosen);
        }
    }
}
