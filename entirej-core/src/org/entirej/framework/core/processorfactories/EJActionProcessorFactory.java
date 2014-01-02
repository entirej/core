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
package org.entirej.framework.core.processorfactories;

import java.io.Serializable;
import java.util.HashMap;

import org.entirej.framework.core.EJActionProcessorException;
import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.actionprocessor.interfaces.EJBlockActionProcessor;
import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.actionprocessor.interfaces.EJLovActionProcessor;
import org.entirej.framework.core.actionprocessor.interfaces.EJMenuActionProcessor;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.EJCoreMenuProperties;

public class EJActionProcessorFactory implements Serializable
{
    private static EJActionProcessorFactory _instance;
    private HashMap<String, Class<?>>       _actionProcessors;
    
    static
    {
        _instance = new EJActionProcessorFactory();
    }
    
    private EJActionProcessorFactory()
    {
        _actionProcessors = new HashMap<String, Class<?>>();
    }
    
    /**
     * Returns a Singleton instance of this factory
     * 
     * @return The singleton instance of this factory
     */
    public static EJActionProcessorFactory getInstance()
    {
        return _instance;
    }
    
    /**
     * Retrieve the processor defined within the <code>BlockProperties</code>
     * specified
     * 
     * @param blockProperties
     *            The block properties containing the name of the action
     *            processor
     * @return The action processor specified within the given block properties
     *         or <code>null</code> if no action processor has been specified
     *         for the given block
     * @throws EJActionProcessorException
     */
    public EJBlockActionProcessor getActionProcessor(EJCoreBlockProperties blockProperties)
    {
        if (blockProperties == null)
        {
            return null;
        }
        
        String actionProcessorName = blockProperties.getActionProcessorClassName();
        
        if (actionProcessorName == null || actionProcessorName.trim().length() == 0)
        {
            return null;
        }
        
        if (_actionProcessors.containsKey(actionProcessorName))
        {
            return createNewBlockActionProcessorInstance(blockProperties.getFrameworkManager(), actionProcessorName);
        }
        try
        {
            Class<?> processorClass = Class.forName(actionProcessorName);
            _actionProcessors.put(actionProcessorName, processorClass);
            
            return createNewBlockActionProcessorInstance(blockProperties.getFrameworkManager(), actionProcessorName);
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.INVALID_ACTION_PROCESSOR_FOR_BLOCK, actionProcessorName, blockProperties.getName()));
        }
    }
    
    /**
     * Retrieve the processor defined within the <code>FormProperties</code>
     * specified
     * 
     * @param formProperties
     *            The form properties containing the name of the action
     *            processor
     * @return The action processor specified within the given form properties
     * @throws EJActionProcessorException
     */
    public EJFormActionProcessor getActionProcessor(EJCoreFormProperties formProperties)
    {
        if (formProperties == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_FORM_PROPERTIES_PASSED_TO_METHOD,
                    "getActionProcessor"));
        }
        
        String actionProcessorName = formProperties.getActionProcessorClassName();
        
        if (actionProcessorName == null || actionProcessorName.trim().length() == 0)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.NO_ACTION_PROCESSOR_DEFINED_FOR_FORM, formProperties.getName()));
        }
        
        if (_actionProcessors.containsKey(actionProcessorName))
        {
            return createNewFormActionProcessorInstance(formProperties.getFrameworkManager(), actionProcessorName);
        }
        try
        {
            Class<?> processorClass = Class.forName(actionProcessorName);
            _actionProcessors.put(actionProcessorName, processorClass);
            
            return createNewFormActionProcessorInstance(formProperties.getFrameworkManager(), actionProcessorName);
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.INVALID_ACTION_PROCESSOR_FOR_FORM, actionProcessorName, formProperties.getName()));
        }
    }
    
    /**
     * Retrieve the processor defined within the specified
     * <code>LovDefinitionProperties</code>
     * 
     * 
     * @param lovDefProperties
     *            The lov definition properties containing the name of the lov
     *            action processor
     * @return The action processor specified within the given lov definition
     *         properties
     * @throws EJActionProcessorException
     */
    public EJLovActionProcessor getActionProcessor(EJCoreLovDefinitionProperties lovDefProperties)
    {
        if (lovDefProperties == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_LOVDEF_PROPERTIES_PASSED_TO_METHOD,
                    "getActionProcessor"));
        }
        
        String actionProcessorName = lovDefProperties.getActionProcessorClassName();
        if (actionProcessorName == null || actionProcessorName.trim().length() == 0)
        {
            return null;
        }
        
        if (_actionProcessors.containsKey(actionProcessorName))
        {
            return createNewLovActionProcessorInstance(lovDefProperties.getBlockProperties().getFrameworkManager(), actionProcessorName);
        }
        try
        {
            Class<?> processorClass = Class.forName(actionProcessorName);
            _actionProcessors.put(actionProcessorName, processorClass);
            
            return createNewLovActionProcessorInstance(lovDefProperties.getBlockProperties().getFrameworkManager(), actionProcessorName);
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.INVALID_ACTION_PROCESSOR_FOR_LOV, actionProcessorName, lovDefProperties.getName()));
        }
    }
    
    /**
     * Retrieve the menu processor defined within the
     * <code>EJCoreMenuProperties</code>
     * 
     * @param menuProperties
     *            The menu properties containing the name of the action
     *            processor
     * @return The action processor specified within the given menu properties
     * @throws EJActionProcessorException
     */
    public EJMenuActionProcessor getActionProcessor(EJFrameworkManager frameworkManager,EJCoreMenuProperties menuProperties)
    {
        if (menuProperties == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_MENU_PROPERTIES_PASSED_TO_METHOD,
                    "getActionProcessor"));
        }
        
        String actionProcessorName = menuProperties.getActionProcessorClassName();
        
        if (actionProcessorName == null || actionProcessorName.trim().length() == 0)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.NO_ACTION_PROCESSOR_DEFINED_FOR_MENU, menuProperties.getName()));
        }
        
        try
        {
            Class<?> processorClass = Class.forName(actionProcessorName);
            return createNewMenuActionProcessorInstance(frameworkManager, processorClass);
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.INVALID_ACTION_PROCESSOR_FOR_MENU, actionProcessorName));
        }
    }
    
    /**
     * Creates a new <code>EJFormActionProcessor</code> from the class instance
     * stored within the cache
     * 
     * @param processorName
     *            The name of the processor
     * @return The new <code>EJFormActionProcessor</code> instance
     * @throws EJActionProcessorException
     */
    private EJFormActionProcessor createNewFormActionProcessorInstance(EJFrameworkManager frameworkManager, String processorName)
    {
        Class<?> processorClass = _actionProcessors.get(processorName);
        
        if (processorClass == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.NULL_PROCESSOR_NAME_PASSED_TO_METHOD, processorName));
        }
        
        Object processorObject;
        try
        {
            processorObject = processorClass.newInstance();
            if (processorObject instanceof EJFormActionProcessor)
            {
                return (EJFormActionProcessor) processorObject;
            }
            else
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                        EJFrameworkMessage.INVALID_ACTION_PROCESSOR_NAME, processorName, "EJFormActionProcessor"));
            }
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR, processorName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR, processorName), e);
        }
    }
    
    /**
     * Creates a new <code>EJBlockActionProcessor</code> from the class instance
     * stored within the cache
     * 
     * @param processorName
     *            The name of the processor
     * @return The new <code>EJFormActionProcessor</code> instance
     * @throws EJActionProcessorException
     */
    private EJBlockActionProcessor createNewBlockActionProcessorInstance(EJFrameworkManager frameworkManager, String processorName)
    {
        Class<?> processorClass = _actionProcessors.get(processorName);
        
        if (processorClass == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.NULL_PROCESSOR_NAME_PASSED_TO_METHOD, processorName));
        }
        
        Object processorObject;
        try
        {
            processorObject = processorClass.newInstance();
            if (processorObject instanceof EJBlockActionProcessor)
            {
                return (EJBlockActionProcessor) processorObject;
            }
            else
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                        EJFrameworkMessage.INVALID_ACTION_PROCESSOR_NAME, processorName, "EJBlockActionProcessor"));
            }
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR, processorName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR, processorName), e);
        }
    }
    
    private EJLovActionProcessor createNewLovActionProcessorInstance(EJFrameworkManager frameworkManager, String processorName)
    {
        Class<?> processorClass = _actionProcessors.get(processorName);
        
        if (processorClass == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.NULL_PROCESSOR_NAME_PASSED_TO_METHOD, processorName));
        }
        
        Object processorObject;
        try
        {
            processorObject = processorClass.newInstance();
            if (processorObject instanceof EJLovActionProcessor)
            {
                return (EJLovActionProcessor) processorObject;
            }
            else
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                        EJFrameworkMessage.INVALID_ACTION_PROCESSOR_NAME, processorName, "EJLovActionProcessor"));
            }
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR, processorName), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR, processorName), e);
        }
    }
    
    /**
     * Creates a new <code>EJMenuActionProcessor</code> from the given class name
     * 
     * @param processorName
     *            The name of the processor
     * @return The new <code>EJFormActionProcessor</code> instance
     * @throws EJActionProcessorException
     */
    private EJMenuActionProcessor createNewMenuActionProcessorInstance(EJFrameworkManager frameworkManager, Class<?> processorClass)
    {
        if (processorClass == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.NULL_PROCESSOR_NAME_PASSED_TO_METHOD, "createNewMenuActionProcessorInstance"));
        }
        
        Object processorObject;
        try
        {
            processorObject = processorClass.newInstance();
            if (processorObject instanceof EJMenuActionProcessor)
            {
                return (EJMenuActionProcessor) processorObject;
            }
            else
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                        EJFrameworkMessage.INVALID_ACTION_PROCESSOR_NAME, processorClass.getName(), "EJMenuActionProcessor"));
            }
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR, processorClass.getName()), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(
                    EJFrameworkMessage.UNABLE_TO_CREATE_ACTION_PROCESSOR, processorClass.getName()), e);
        }
    }
}
