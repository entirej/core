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
package org.entirej.framework.core.renderers;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.renderers.interfaces.EJItemRenderer;

public class EJManagedItemRendererWrapper implements EJItemRenderer
{
    private EJFrameworkManager              _frameworkManager;
    private EJItemRenderer                  _itemRenderer;
    private EJCoreVisualAttributeProperties _setVaProperties;
    
    public EJManagedItemRendererWrapper(EJFrameworkManager manager, EJItemRenderer renderer)
    {
        _itemRenderer = renderer;
        _frameworkManager = manager;
    }
    
    public EJItemRenderer getUnmanagedRenderer()
    {
        return _itemRenderer;
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#clearValue()}
     */
    public void clearValue()
    {
        try
        {
            _itemRenderer.clearValue();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#enableLovActivation()}
     */
    public void enableLovActivation(boolean enable)
    {
        try
        {
            _itemRenderer.enableLovActivation(enable);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#gainFocus()}
     */
    public void gainFocus()
    {
        try
        {
            _itemRenderer.gainFocus();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     */
    public EJScreenItemController getItem()
    {
        try
        {
            return _itemRenderer.getItem();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#getRegisteredItemName()}
     */
    public String getRegisteredItemName()
    {
        try
        {
            return _itemRenderer.getRegisteredItemName();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#getValue()}
     */
    public Object getValue()
    {
        try
        {
            return _itemRenderer.getValue();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#initialise()}
     */
    public void initialise(EJScreenItemController item, EJScreenItemProperties screenItemProperties)
    {
        try
        {
            _itemRenderer.initialise(item, screenItemProperties);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#isEditAllowed()}
     */
    public boolean isEditAllowed()
    {
        try
        {
            return _itemRenderer.isEditAllowed();
        }
        catch (Exception e)
        {
            handleException(e);
            return false;
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#isVisible()}
     */
    public boolean isVisible()
    {
        try
        {
            return _itemRenderer.isVisible();
        }
        catch (Exception e)
        {
            handleException(e);
            return false;
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#isMandatory()}
     */
    public boolean isMandatory()
    {
        try
        {
            return _itemRenderer.isMandatory();
        }
        catch (Exception e)
        {
            handleException(e);
            return false;
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#isValid()}
     */
    public boolean isValid()
    {
        try
        {
            return _itemRenderer.isValid();
        }
        catch (Exception e)
        {
            handleException(e);
            return false;
        }
    }
    
    /**
     * Indicates to the renderer that the item should be refreshed. This could
     * be just re-populating the items value, or refreshing all properties.
     * <p>
     * In the example of the ComboBox Item Renderer, this method could indicate
     * that the combo box's underlying lov should be refreshed and the values
     * re-populated
     * 
     */
    public void refreshItemRenderer()
    {
        try
        {
            _itemRenderer.refreshItemRenderer();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#refreshItemRendererProperty(String)}
     */
    public void refreshItemRendererProperty(String propertyName)
    {
        try
        {
            _itemRenderer.refreshItemRendererProperty(propertyName);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#setEditAllowed(boolean)}
     */
    public void setEditAllowed(boolean editAllowed)
    {
        try
        {
            _itemRenderer.setEditAllowed(editAllowed);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#setInitialValue(Object)}
     */
    public void setInitialValue(Object value)
    {
        try
        {
            _itemRenderer.setInitialValue(value);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#setMandatory(boolean)}
     */
    public void setMandatory(boolean mandatory)
    {
        try
        {
            _itemRenderer.setMandatory(mandatory);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#setRegisteredItemName(String)}
     */
    public void setRegisteredItemName(String name)
    {
        try
        {
            _itemRenderer.setRegisteredItemName(name);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#setValue(Object)}
     */
    public void setValue(Object value)
    {
        try
        {
            _itemRenderer.setValue(value);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Sets this items label
     * 
     * @param label
     *            The label to set
     */
    public void setLabel(String label)
    {
        try
        {
            _itemRenderer.setLabel(label);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Sets this items hint
     * 
     * @param hint
     *            The hint to set
     */
    public void setHint(String hint)
    {
        try
        {
            _itemRenderer.setHint(hint);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#setVisible(boolean)}
     */
    public void setVisible(boolean visible)
    {
        try
        {
            _itemRenderer.setVisible(visible);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#valueEqualsTo(Object)}
     */
    public boolean valueEqualsTo(Object value)
    {
        try
        {
            return _itemRenderer.valueEqualsTo(value);
        }
        catch (Exception e)
        {
            handleException(e);
            return false;
        }
    }
    
    /**
     * Managed delegate for the <code>ItemRenderer</code>
     * 
     * @see {@link EJItemRenderer#getGuiComponent()}
     */
    public Object getGuiComponent()
    {
        try
        {
            return _itemRenderer.getGuiComponent();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }
    
    @Override
    public void validationErrorOccurred(boolean error)
    {
        try
        {
            _itemRenderer.validationErrorOccurred(error);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    @Override
    public void setVisualAttribute(EJCoreVisualAttributeProperties visualAttributeProperties)
    {
        try
        {
            _setVaProperties = visualAttributeProperties;
            _itemRenderer.setVisualAttribute(visualAttributeProperties);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }
    
    public EJCoreVisualAttributeProperties getVisualAttributeProperties()
    {
        return _setVaProperties;
    }
    
    private void handleException(Exception e)
    {
        if (e instanceof EJApplicationException)
        {
            _frameworkManager.handleException((EJApplicationException) e);
        }
        else
        {
            _frameworkManager.handleException(new EJApplicationException(e));
        }
    }
    
    public boolean isReadOnly()
    {
        return _itemRenderer.isReadOnly();
    }
    
}
