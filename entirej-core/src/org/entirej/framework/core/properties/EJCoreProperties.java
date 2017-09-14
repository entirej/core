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
import java.util.HashMap;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.interfaces.EJConnectionFactory;
import org.entirej.framework.core.interfaces.EJTranslator;
import org.entirej.framework.core.properties.containers.EJCoreRendererAssignmentContainer;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJEntireJProperties;
import org.entirej.framework.core.properties.interfaces.EJRendererAssignment;

public class EJCoreProperties implements EJEntireJProperties
{
    private static EJCoreProperties                      _instance;
    
    private HashMap<String, EJApplicationLevelParameter> _applicationLevelParameters;
    
    private ArrayList<String>                            _formPackageNames;
    private String                                       _reusableBlocksLocation;
    private String                                       _reusableLovDefinitionLocation;
    private String                                       _objectGroupDefinitionLocation;
    
    private HashMap<String, String>                      _accessProcessorNames;
    private EJCoreRendererAssignmentContainer            _formRendererAssignmentContainer;
    private EJCoreRendererAssignmentContainer            _blockRendererAssignmentContainer;
    private EJCoreRendererAssignmentContainer            _itemRendererAssignmentContainer;
    private EJCoreRendererAssignmentContainer            _lovRendererAssignmentContainer;
    private EJCoreRendererAssignmentContainer            _menuRendererAssignmentContainer;
    private EJCoreRendererAssignmentContainer            _appComponentRendererAssignmentContainer;
    private EJCoreVisualAttributeContainer               _visualAttributeContainer;
    private EJCoreMenuContainer                          _menuContainer;
    private EJCoreLayoutContainer                        _layoutContainer;
    
    private EJFrameworkExtensionProperties               _applicationDefinedPropertiesProperties;
    private EJTranslator                                 _applicationTranslator;
    private String                                       _connectionFactoryClassName;
    private String                                       _applicationManagerClassName;
    private String                                       _applicationActionProcessorClassName;
    
    static
    {
        _instance = new EJCoreProperties();
    }
    
    protected EJCoreProperties()
    {
        _formPackageNames = new ArrayList<String>();
        _accessProcessorNames = new HashMap<String, String>();
        _applicationLevelParameters = new HashMap<String, EJApplicationLevelParameter>();
        _formRendererAssignmentContainer = new EJCoreRendererAssignmentContainer(EJCoreRendererAssignmentContainer.FORM_RENDERER_TYPE);
        _blockRendererAssignmentContainer = new EJCoreRendererAssignmentContainer(EJCoreRendererAssignmentContainer.BLOCK_RENDERER_TYPE);
        _itemRendererAssignmentContainer = new EJCoreRendererAssignmentContainer(EJCoreRendererAssignmentContainer.ITEM_RENDERER_TYPE);
        _lovRendererAssignmentContainer = new EJCoreRendererAssignmentContainer(EJCoreRendererAssignmentContainer.LOV_RENDERER_TYPE);
        _menuRendererAssignmentContainer = new EJCoreRendererAssignmentContainer(EJCoreRendererAssignmentContainer.MENU_RENDERER_TYPE);
        _appComponentRendererAssignmentContainer = new EJCoreRendererAssignmentContainer(EJCoreRendererAssignmentContainer.APP_COMPONENT_RENDERER_TYPE);
        _menuContainer = new EJCoreMenuContainer();
        _visualAttributeContainer = new EJCoreVisualAttributeContainer(new ArrayList<EJCoreVisualAttributeProperties>());
        _layoutContainer = new EJCoreLayoutContainer();
    }
    
    /**
     * Returns a singleton instance of the EntireJ Properties
     * <p>
     * The EntireJProperties are intended for internal use only and are used to
     * initialise the framework and provide the package names where the
     * different form properties or renderers are held.
     * 
     * @return The <code>EntireJProperties</code> singleton instance
     */
    public static EJCoreProperties getInstance()
    {
        return _instance;
    }
    
    public void setApplicationManagerClassName(String className)
    {
        if (className == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_APPLICATION_MANAGER_PASSED_TO_METHOD,
                    "EntireJCoreProperties.setApplicationManagerClassName"));
        }
        
        _applicationManagerClassName = className;
    }
    
    public String getApplicationManagerClassName()
    {
        return _applicationManagerClassName;
    }
    public void setApplicationActionProcessorClassName(String className)
    {
        if (className == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_APPLICATION_MANAGER_PASSED_TO_METHOD,
                    "EntireJCoreProperties.setApplicationActionProcessorClassName"));
        }
        
        _applicationActionProcessorClassName = className;
    }
    
    public String getApplicationActionProcessorClassName()
    {
        return _applicationActionProcessorClassName;
    }
    
    /**
     * Adds an Application Level Parameter to this application
     * 
     * @param parameter
     *            The parameter to add
     */
    public void addApplicationLevelParameter(EJApplicationLevelParameter parameter)
    {
        if (parameter != null)
        {
            _applicationLevelParameters.put(parameter.getName(), parameter);
        }
    }
    
    public void copyApplicationLevelParameters(EJFrameworkManager frameworkManager)
    {
        for (EJApplicationLevelParameter param : _applicationLevelParameters.values())
        {
            EJApplicationLevelParameter parameter = new EJApplicationLevelParameter(param.getName(), param.getDataType());
            parameter.setValue(param.getValue());
            frameworkManager.addApplicationLevelParameter(parameter);
        }
    }
    
    public void setApplicationTranslatorClassName(String className)
    {
        if (className == null || className.trim().length() == 0)
        {
            return;
        }
        
        try
        {
            Class<?> rendererClass = Class.forName(className);
            Object obj = rendererClass.newInstance();
            
            if (obj instanceof EJTranslator)
            {
                _applicationTranslator = (EJTranslator) obj;
            }
            else
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_TRANSLATOR_NAME, className,
                        "ITranslator"));
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance()
                    .createMessage(EJFrameworkMessage.UNABLE_TO_CREATE_APPLICATION_TRANSLATOR, className), e);
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance()
                    .createMessage(EJFrameworkMessage.UNABLE_TO_CREATE_APPLICATION_TRANSLATOR, className), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance()
                    .createMessage(EJFrameworkMessage.UNABLE_TO_CREATE_APPLICATION_TRANSLATOR, className), e);
        }
    }
    
    public EJTranslator getApplicationTranslator()
    {
        return _applicationTranslator;
    }
    
    @Override
    public EJFrameworkExtensionProperties getApplicationDefinedProperties()
    {
        return _applicationDefinedPropertiesProperties;
    }
    
    public void setApplicationDefinedProperties(EJFrameworkExtensionProperties properties)
    {
        _applicationDefinedPropertiesProperties = properties;
    }
    
    @Override
    public Collection<String> getFormPackageNames()
    {
        return _formPackageNames;
    }
    
    /**
     * Adds a given package name to the list of form package names
     * 
     * @param packageName
     *            The package name to add
     * @throws NullPointerException
     *             if the package name passed is either null or of zero length
     */
    void addFormPackageName(String packageName)
    {
        if (packageName == null || packageName.trim().length() == 0)
        {
            throw new NullPointerException("The package name passed to addFormPackageName is either null or of zero length");
        }
        _formPackageNames.add(packageName);
    }
    
    /**
     * Adds an access processor name and class name to the EntireJ properties
     * <p>
     * The class name can then be used when loading the required accessor. Each
     * block has an AccessProcessor defined and it will be used when retrieving
     * or modifying the blocks data
     * 
     * @param name
     *            The name of the required Access Processor
     * @param className
     *            The fully qualified class name of the AccessProcessor with the
     *            given name
     */
    public void addAccessProcessorName(String name, String className)
    {
        if (name != null && className != null)
        {
            _accessProcessorNames.put(name, className);
        }
    }
    
    public String getAccessProcessorClassName(String name)
    {
        if (name == null)
        {
            return null;
        }
        
        return _accessProcessorNames.get(name);
    }
    
    public EJConnectionFactory getConnectionFactory()
    {
        return EJCoreManagedConnectionFactory.getInstane().getConnectionFactory();
    }
    
    public void setConnectionFactoryClassName(String className)
    {
        if (className == null || className.trim().length() == 0)
        {
            
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_CONNECTION_FACTORY_DEFINED_FOR_APPLICATION));
        }
        
        try
        {
            _connectionFactoryClassName = className;
            
            Class<?> factoryClass = Class.forName(className);
            Object obj = factoryClass.newInstance();
            
            if (obj instanceof EJConnectionFactory)
            {
                EJCoreManagedConnectionFactory.getInstane().setConnectionFactory((EJConnectionFactory) obj);
            }
            else
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_TRANSACTION_FACTORY));
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_CREATE_TRANSACTION_FACTORY, className),
                    e);
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_CREATE_TRANSACTION_FACTORY, className),
                    e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_CREATE_TRANSACTION_FACTORY, className),
                    e);
        }
    }
    
    public String getConnectionFactoryClassName()
    {
        return _connectionFactoryClassName;
    }
    
    public void setReusableBlocksLocation(String location)
    {
        _reusableBlocksLocation = location.trim();
    }
    
    @Override
    public String getReusableBlocksLocation()
    {
        return _reusableBlocksLocation;
    }
    
    public void setReusableLovDefinitionLocation(String location)
    {
        _reusableLovDefinitionLocation = location.trim();
    }
    
    @Override
    public String getReusableLovDefinitionLocation()
    {
        return _reusableLovDefinitionLocation;
    }
    public void setObjectGroupDefinitionLocation(String location)
    {
        _objectGroupDefinitionLocation = location.trim();
    }
    
    @Override
    public String getObjectGroupDefinitionLocation()
    {
        return _objectGroupDefinitionLocation;
    }
    
    @Override
    public EJRendererAssignment getApplicationAssignedItemRenderer(String name)
    {
        return _itemRendererAssignmentContainer.getRendererAssignment(name);
    }
    
    public void addItemRendererAssignment(EJRendererAssignment itemRendererAssignment)
    {
        if (itemRendererAssignment != null)
        {
            _itemRendererAssignmentContainer.addRendererAssignment(itemRendererAssignment);
        }
    }
    
    @Override
    public EJRendererAssignment getApplicationAssignedMenuRenderer(String name)
    {
        return _menuRendererAssignmentContainer.getRendererAssignment(name);
    }
    
    public void addMenuRendererAssignment(EJRendererAssignment menuRendererAssignment)
    {
        if (menuRendererAssignment != null)
        {
            _menuRendererAssignmentContainer.addRendererAssignment(menuRendererAssignment);
        }
    }
    
    @Override
    public EJRendererAssignment getApplicationAssignedLovRenderer(String name)
    {
        return _lovRendererAssignmentContainer.getRendererAssignment(name);
    }
    
    public void addLovRendererAssignment(EJRendererAssignment lovRendererAssignment)
    {
        if (lovRendererAssignment != null)
        {
            _lovRendererAssignmentContainer.addRendererAssignment(lovRendererAssignment);
        }
    }
    
    @Override
    public EJRendererAssignment getApplicationAssignedBlockRenderer(String name)
    {
        return _blockRendererAssignmentContainer.getRendererAssignment(name);
    }
    
    public void addBlockRendererAssignment(EJRendererAssignment blockRendererAssignment)
    {
        if (blockRendererAssignment != null)
        {
            _blockRendererAssignmentContainer.addRendererAssignment(blockRendererAssignment);
        }
    }
    
    @Override
    public EJRendererAssignment getApplicationAssignedFormRenderer(String name)
    {
        return _formRendererAssignmentContainer.getRendererAssignment(name);
    }
    
    public void addFormRendererAssignment(EJRendererAssignment formRendererAssignment)
    {
        if (formRendererAssignment != null)
        {
            _formRendererAssignmentContainer.addRendererAssignment(formRendererAssignment);
        }
    }
    
    @Override
    public EJCoreMenuProperties getMenuProperties(String name)
    {
        return _menuContainer.getMenuProperties(name);
    }
    
    public void addMenuProperties(EJCoreMenuProperties menuProperties)
    {
        if (menuProperties != null)
        {
            _menuContainer.addMenuProperties(menuProperties);
        }
    }
    
    @Override
    public EJCoreVisualAttributeContainer getVisualAttributesContainer()
    {
        return _visualAttributeContainer;
    }
    
    /**
     * Returns the item renderers that have been defined for this application
     * 
     * @return A list of renderers
     */
    @Override
    public Collection<EJRendererAssignment> getApplicationAssignedItemRenderers()
    {
        return _itemRendererAssignmentContainer.getAllRendererAssignments();
    }
    
    /**
     * Returns the menu renderers that have been defined for this application
     * 
     * @return A list of menu renderers
     */
    @Override
    public Collection<EJRendererAssignment> getApplicationAssignedMenuRenderers()
    {
        return _menuRendererAssignmentContainer.getAllRendererAssignments();
    }
    
    /**
     * Returns the block renderers that have been defined for this application
     * 
     * @return A list of renderers
     */
    @Override
    public Collection<EJRendererAssignment> getApplicationAssignedBlockRenderers()
    {
        return _blockRendererAssignmentContainer.getAllRendererAssignments();
    }
    
    /**
     * Returns the form renderer that have been defined for this application
     * 
     * @return A list of renderers
     */
    @Override
    public Collection<EJRendererAssignment> getApplicationAssignedFormRenderers()
    {
        return _formRendererAssignmentContainer.getAllRendererAssignments();
    }
    
    /**
     * Returns the lov renderer that have been defined for this application
     * 
     * @return A list of renderers
     */
    @Override
    public Collection<EJRendererAssignment> getApplicationAssignedLovRenderers()
    {
        return _lovRendererAssignmentContainer.getAllRendererAssignments();
    }
    
    /**
     * Returns the menu container
     * 
     * @return The menu container
     */
    @Override
    public EJCoreMenuContainer getMenuContainer()
    {
        return _menuContainer;
    }
    
    @Override
    public Collection<EJRendererAssignment> getApplicationAssignedComponentRenderers()
    {
        return _appComponentRendererAssignmentContainer.getAllRendererAssignments();
    }
    
    @Override
    public EJRendererAssignment getApplicationAssignedComponentRenderer(String name)
    {
        return _appComponentRendererAssignmentContainer.getRendererAssignment(name);
    }
    
    public void addApplicationComponentRendererAssignment(EJRendererAssignment lovRendererAssignment)
    {
        if (lovRendererAssignment != null)
        {
            _appComponentRendererAssignmentContainer.addRendererAssignment(lovRendererAssignment);
        }
    }
    
    @Override
    public EJCoreLayoutContainer getLayoutContainer()
    {
        return _layoutContainer;
    }
    
    public void setLayoutContainer(EJCoreLayoutContainer container)
    {
        _layoutContainer = container;
    }
    
}
