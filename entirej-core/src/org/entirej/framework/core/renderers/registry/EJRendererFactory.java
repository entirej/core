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
package org.entirej.framework.core.renderers.registry;

import java.io.Serializable;
import java.util.HashMap;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.data.controllers.EJEditableBlockController;
import org.entirej.framework.core.data.controllers.EJLovController;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.interfaces.EJScreenItemController;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreProperties;
import org.entirej.framework.core.properties.interfaces.EJRendererAssignment;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.renderers.EJManagedFormRendererWrapper;
import org.entirej.framework.core.renderers.EJManagedItemRendererWrapper;
import org.entirej.framework.core.renderers.interfaces.EJApplicationComponentRenderer;
import org.entirej.framework.core.renderers.interfaces.EJEditableBlockRenderer;
import org.entirej.framework.core.renderers.interfaces.EJFormRenderer;
import org.entirej.framework.core.renderers.interfaces.EJItemRenderer;
import org.entirej.framework.core.renderers.interfaces.EJLovRenderer;

public class EJRendererFactory implements Serializable
{
    private static EJRendererFactory  _instance;
    private HashMap<String, Class<?>> _formRenderers;
    private HashMap<String, Class<?>> _blockRenderers;
    private HashMap<String, Class<?>> _itemRenderers;
    private HashMap<String, Class<?>> _lovRenderers;
    private HashMap<String, Class<?>> _menuRenderers;
    private HashMap<String, Class<?>> _appComponentRenderers;
    
    static
    {
        _instance = new EJRendererFactory();
    }
    
    private EJRendererFactory()
    {
        _formRenderers = new HashMap<String, Class<?>>();
        _blockRenderers = new HashMap<String, Class<?>>();
        _itemRenderers = new HashMap<String, Class<?>>();
        _lovRenderers = new HashMap<String, Class<?>>();
        _menuRenderers = new HashMap<String, Class<?>>();
        _appComponentRenderers = new HashMap<String, Class<?>>();
    }
    
    /**
     * Returns a Singleton instance of this factory
     * 
     * @return The singleton instance of this factory
     */
    public static EJRendererFactory getInstance()
    {
        return _instance;
    }
    
    /**
     * Retrieve the form renderer defined within the
     * <code>IFormProperties</code> specified
     * 
     * @param formProperties
     *            The form properties containing the name of the form renderer
     * @return The form renderer wrapper specified within the given form
     *         properties. The wrapper will handle any runtime or checked
     *         exceptions from renderer. If you want to receive the exception,
     *         you can obtain the <code>UnmanagedRenderre</code> from the
     *         wrapper
     * @throws NullPointerException
     *             if the form properties passed is null
     * @throws IllegalArgumentException
     *             if the renderer name within the form properties is not
     *             specified or is not a valid <code>IFormRenderer</code>
     */
    public EJManagedFormRendererWrapper getFormRenderer(EJFrameworkManager frameworkManager, EJCoreFormProperties formProperties)
    {
        if (formProperties == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_FORM_PROPERTIES_PASSED_TO_METHOD,
                    "getFormRenderer"));
        }
        
        EJRendererAssignment rendererAssignment = null;
        String formRendererName = formProperties.getFormRendererName();
        
        if (formRendererName == null || formRendererName.trim().length() == 0)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance()
                    .createMessage(EJFrameworkMessage.NO_FORM_RENDERER_DEFINED, formProperties.getName()));
        }
        
        if (_formRenderers.containsKey(formRendererName))
        {
            return new EJManagedFormRendererWrapper(frameworkManager, createNewFormRendererInstance(formRendererName));
        }
        try
        {
            rendererAssignment = EJCoreProperties.getInstance().getApplicationAssignedFormRenderer(formProperties.getFormRendererName());
            
            Class<?> rendererClass = Class.forName(rendererAssignment.getRendererClassName());
            _formRenderers.put(rendererAssignment.getAssignedName(), rendererClass);
            return new EJManagedFormRendererWrapper(frameworkManager, createNewFormRendererInstance(rendererAssignment.getAssignedName()));
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_RENDERER,
                    formProperties.getFormRendererName()), e);
        }
    }
    
    /**
     * Retrieve the block renderer for the given controller
     * 
     * @param blockController
     *            The controller of the block for which the renderer should be
     *            retrieved
     * @return The required renderer or <code>null</code> if there is no
     *         renderer for the given controller
     * 
     */
    public EJEditableBlockRenderer getBlockRenderer(EJEditableBlockController blockController)
    {
        if (blockController == null || (blockController.getProperties().getBlockRendererName() == null))
        {
            return null;
        }
        
        String blockRendererName = blockController.getProperties().getBlockRendererName();
        
        if (blockRendererName == null || blockRendererName.trim().length() == 0)
        {
            return null;
        }
        
        if (_blockRenderers.containsKey(blockRendererName))
        {
            EJEditableBlockRenderer renderer = createNewBlockRendererInstance(blockRendererName);
            renderer.initialiseRenderer(blockController);
            return renderer;
        }
        
        EJRendererAssignment rendererAssignment = null;
        try
        {
            rendererAssignment = EJCoreProperties.getInstance().getApplicationAssignedBlockRenderer(blockRendererName);
            
            if (rendererAssignment == null)
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_BLOCK_RENDERER, blockRendererName));
            }
            
            Class<?> rendererClass = Class.forName(rendererAssignment.getRendererClassName());
            _blockRenderers.put(blockRendererName, rendererClass);
            
            EJEditableBlockRenderer renderer = createNewBlockRendererInstance(blockRendererName);
            renderer.initialiseRenderer(blockController);
            return renderer;
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_BLOCK_RENDERER, blockRendererName,
                    rendererAssignment == null ? "" : rendererAssignment.getAssignedName()), e);
        }
    }
    
    /**
     * Retrieve the item renderer for the given item controller
     * 
     * @param itemController
     *            The controller for the required renderer
     * @return The required item renderer
     */
    public EJManagedItemRendererWrapper getItemRenderer(EJScreenItemController item, EJScreenItemProperties screenItemProperties)
    {
        if (item == null)
        {
            return null;
        }
        
        String itemRendererName = item.getReferencedItemProperties().getItemRendererName();
        
        if (itemRendererName == null || itemRendererName.trim().length() == 0)
        {
            return null;
        }
        
        if (_itemRenderers.containsKey(itemRendererName))
        {
            EJItemRenderer renderer = createNewItemRendererInstance(itemRendererName);
            renderer.initialise(item, screenItemProperties);
            
            return new EJManagedItemRendererWrapper(item.getForm().getFrameworkManager(), renderer);
        }
        else
        {
            EJRendererAssignment rendererAssignment = null;
            try
            {
                rendererAssignment = EJCoreProperties.getInstance().getApplicationAssignedItemRenderer(itemRendererName);
                
                if (rendererAssignment == null)
                {
                    throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_ITEM_RENDERER, itemRendererName));
                }
                
                Class<?> rendererClass = Class.forName(rendererAssignment.getRendererClassName());
                _itemRenderers.put(itemRendererName, rendererClass);
                EJItemRenderer renderer = createNewItemRendererInstance(itemRendererName);
                renderer.initialise(item, screenItemProperties);
                
                return new EJManagedItemRendererWrapper(item.getForm().getFrameworkManager(), renderer);
            }
            catch (ClassNotFoundException e)
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_ITEM_RENDERER,
                        itemRendererName, rendererAssignment == null ? "" : rendererAssignment.getAssignedName()), e);
            }
        }
    }
    
    /**
     * Retrieve the lov renderer for the given lov controller
     * 
     * @param lovController
     *            The lov renderer will be returned for this lov controller
     * @return The required lov renderer
     */
    public EJLovRenderer getLovRenderer(EJLovController lovController)
    {
        if (lovController == null)
        {
            return null;
        }
        String lovRendererName = lovController.getDefinitionProperties().getLovRendererName();
        
        if (lovRendererName == null || lovRendererName.trim().length() == 0)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_LOV_RENDERER_PASSED_TO_METHOD,
                    "getLovRenderer"));
        }
        
        if (_lovRenderers.containsKey(lovRendererName))
        {
            EJLovRenderer renderer = createNewLovRendererInstance(lovRendererName);
            return renderer;
        }
        
        EJRendererAssignment rendererAssignment = null;
        try
        {
            rendererAssignment = EJCoreProperties.getInstance().getApplicationAssignedLovRenderer(lovRendererName);
            
            if (rendererAssignment == null)
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_LOV_RENDERER, lovRendererName));
            }
            
            Class<?> rendererClass = Class.forName(rendererAssignment.getRendererClassName());
            _lovRenderers.put(lovRendererName, rendererClass);
            EJLovRenderer renderer = createNewLovRendererInstance(lovRendererName);
            
            return renderer;
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_LOV_RENDERER, lovRendererName,
                    rendererAssignment == null ? "" : rendererAssignment.getAssignedName()), e);
        }
    }
    
    /**
     * Retrieve the menu renderer for the given menu controller
     * 
     * @param rendererName
     *            The EJApplicationComponentRenderer will be returned for this
     *            render id.
     * @return The required application component renderer
     */
    public EJApplicationComponentRenderer getApplicationComponentRenderer(String rendererName)
    {
        
        if (rendererName == null || rendererName.trim().length() == 0)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_APP_COMPONENT_RENDERER_PASSED_TO_METHOD,
                    "getApplicationComponentRenderer"));
        }
        
        if (_appComponentRenderers.containsKey(rendererName))
        {
            return createNewComponentRendererInstance(rendererName);
            
        }
        
        EJRendererAssignment rendererAssignment = null;
        try
        {
            rendererAssignment = EJCoreProperties.getInstance().getApplicationAssignedComponentRenderer(rendererName);
            
            if (rendererAssignment == null)
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_APP_COMPONENT_RENDERER, rendererName));
            }
            
            Class<?> rendererClass = Class.forName(rendererAssignment.getRendererClassName());
            _appComponentRenderers.put(rendererName, rendererClass);
            return createNewComponentRendererInstance(rendererName);
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_APP_COMPONENT, rendererName,
                    rendererAssignment == null ? "" : rendererAssignment.getAssignedName()), e);
        }
    }
    
    private EJFormRenderer createNewFormRendererInstance(String rendererName)
    {
        
        Class<?> rendererClass = _formRenderers.get(rendererName);
        
        if (rendererClass == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_RENDERER_DEFINED_FOR, rendererName));
        }
        
        Object rendererObject;
        try
        {
            rendererObject = rendererClass.newInstance();
            if (rendererObject instanceof EJFormRenderer)
            {
                return (EJFormRenderer) rendererObject;
            }
            else
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_RENDERER_NAME, rendererName,
                        "IFormRenderer"));
            }
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_RENDERER, rendererName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_RENDERER, rendererName), e);
        }
    }
    
    private EJEditableBlockRenderer createNewBlockRendererInstance(String rendererName)
    {
        Class<?> rendererClass = _blockRenderers.get(rendererName);
        
        if (rendererClass == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_RENDERER_DEFINED_FOR, rendererName));
        }
        
        Object rendererObject;
        try
        {
            rendererObject = rendererClass.newInstance();
            if (rendererObject instanceof EJEditableBlockRenderer)
            {
                return (EJEditableBlockRenderer) rendererObject;
            }
            else
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_RENDERER_NAME, rendererName,
                        "IBlockRenderer"));
            }
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_RENDERER, rendererName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_RENDERER, rendererName), e);
        }
        
    }
    
    private EJItemRenderer createNewItemRendererInstance(String rendererName)
    {
        Class<?> rendererClass = _itemRenderers.get(rendererName);
        
        if (rendererClass == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_RENDERER_DEFINED_FOR, rendererName));
        }
        
        Object rendererObject;
        try
        {
            rendererObject = rendererClass.newInstance();
            if (rendererObject instanceof EJItemRenderer)
            {
                return (EJItemRenderer) rendererObject;
            }
            else
            {
                
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_RENDERER_NAME, rendererName,
                        "IItemRenderer"));
            }
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_RENDERER, rendererName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_RENDERER, rendererName), e);
        }
    }
    
    private EJLovRenderer createNewLovRendererInstance(String rendererName)
    {
        Class<?> rendererClass = _lovRenderers.get(rendererName);
        
        if (rendererClass == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_RENDERER_DEFINED_FOR, rendererName));
        }
        
        Object rendererObject;
        try
        {
            rendererObject = rendererClass.newInstance();
            if (rendererObject instanceof EJLovRenderer)
            {
                return (EJLovRenderer) rendererObject;
            }
            else
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_RENDERER_NAME, rendererName,
                        "ILovRenderer"));
            }
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_RENDERER, rendererName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_RENDERER, rendererName), e);
        }
        
    }
    
    private EJApplicationComponentRenderer createNewComponentRendererInstance(String rendererName)
    {
        Class<?> rendererClass = _appComponentRenderers.get(rendererName);
        
        if (rendererClass == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_RENDERER_DEFINED_FOR, rendererName));
        }
        
        Object rendererObject;
        try
        {
            rendererObject = rendererClass.newInstance();
            if (rendererObject instanceof EJApplicationComponentRenderer)
            {
                return (EJApplicationComponentRenderer) rendererObject;
            }
            else
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_RENDERER_NAME, rendererName,
                        "EJApplicationComponentRenderer"));
            }
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_RENDERER, rendererName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_RENDERER, rendererName), e);
        }
        
    }
}
