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
package org.entirej.framework.core.properties.interfaces;

import java.io.Serializable;
import java.util.Collection;

import org.entirej.framework.core.properties.EJCoreLayoutContainer;
import org.entirej.framework.core.properties.EJCoreMenuContainer;
import org.entirej.framework.core.properties.EJCoreMenuProperties;
import org.entirej.framework.core.properties.EJCoreVisualAttributeContainer;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;

public interface EJEntireJProperties extends Serializable
{
    public EJFrameworkExtensionProperties getApplicationDefinedProperties();
    
    /**
     * Retrieve a <code>Collection</code> containing all package names where
     * form definition files can be found
     * 
     * @return A <code>Collection</code> containing forms package names
     */
    public abstract Collection<String> getFormPackageNames();
    
    /**
     * Returns the package name where the re-usable blocks are contained
     * 
     * @return The package name of the reusable block location
     */
    public abstract String getReusableBlocksLocation();
    
    /**
     * Returns the package name where the re-usable lov's are contained
     * 
     * @return The package name of the reusable lov location
     */
    public abstract String getReusableLovDefinitionLocation();
    
    /**
     * Returns the package name where the re-usable Objectgroup's are contained
     * 
     * @return The package name of the reusable ObjectGroup location
     */
    public abstract String getObjectGroupDefinitionLocation();
    
    /**
     * Returns the <code>VisualAttributeContaier</code> that contains all the
     * visual attributes that can be used within the application
     * 
     * @return The <code>VisualAttributeContainer</code> for the application
     */
    public abstract EJCoreVisualAttributeContainer getVisualAttributesContainer();
    
    /**
     * Returns the item renderers that have been defined for this application
     * 
     * @return A list of renderers
     */
    public Collection<EJRendererAssignment> getApplicationAssignedItemRenderers();
    
    /**
     * Returns the menu renderers that have been defined for this application
     * 
     * @return A list of renderers
     */
    public Collection<EJRendererAssignment> getApplicationAssignedMenuRenderers();
    
    /**
     * Returns the component renderers that have been defined for this
     * application
     * 
     * @return A list of renderers
     */
    public Collection<EJRendererAssignment> getApplicationAssignedComponentRenderers();
    
    /**
     * Returns the item renderer assignment with the given name or
     * <code>null</code> if there is no such assignment
     * 
     * @param name
     *            The name of the required assignment
     * @return The required assignemd or <code>null</code> if no such assignment
     *         exists
     */
    public EJRendererAssignment getApplicationAssignedItemRenderer(String name);
    
    /**
     * Returns the menu renderer assignment with the given name or
     * <code>null</code> if there is no such assignment
     * 
     * @param name
     *            The name of the required assignment
     * @return The required assignemd or <code>null</code> if no such assignment
     *         exists
     */
    public EJRendererAssignment getApplicationAssignedMenuRenderer(String name);
    
    /**
     * Returns the component renderer assignment with the given name or
     * <code>null</code> if there is no such assignment
     * 
     * @param name
     *            The name of the required assignment
     * @return The required assignemd or <code>null</code> if no such assignment
     *         exists
     */
    public EJRendererAssignment getApplicationAssignedComponentRenderer(String name);
    
    /**
     * Returns the block renderers that have been defined for this application
     * 
     * @return A list of renderers
     */
    public Collection<EJRendererAssignment> getApplicationAssignedBlockRenderers();
    
    /**
     * Returns the block renderer assignment with the given name or
     * <code>null</code> if there is no such assignment
     * 
     * @param name
     *            The name of the required assignment
     * @return The required assignemd or <code>null</code> if no such assignment
     *         exists
     */
    public EJRendererAssignment getApplicationAssignedBlockRenderer(String name);
    
    /**
     * Returns the form renderer that have been defined for this application
     * 
     * @return A list of renderers
     */
    public Collection<EJRendererAssignment> getApplicationAssignedFormRenderers();
    
    /**
     * Returns the form renderer assignment with the given name or
     * <code>null</code> if there is no such assignment
     * 
     * @param name
     *            The name of the required assignment
     * @return The required assignemd or <code>null</code> if no such assignment
     *         exists
     */
    public EJRendererAssignment getApplicationAssignedFormRenderer(String name);
    
    /**
     * Returns the menu properties with the given name, or <code>null</code> if
     * no menu exists with the given name
     * 
     * @param name
     *            The name of the required menu
     * @return The required menu properties or <code>null</code> if no such menu
     *         exists
     */
    public EJCoreMenuProperties getMenuProperties(String name);
    
    /**
     * Returns the menu container
     * 
     * @return The menu container
     */
    public EJCoreMenuContainer getMenuContainer();
    
    /**
     * Returns the application layout container
     * 
     * @return The application container
     */
    public EJCoreLayoutContainer getLayoutContainer();
    
    /**
     * Returns the lov renderer that have been defined for this application
     * 
     * @return A list of renderers
     */
    public Collection<EJRendererAssignment> getApplicationAssignedLovRenderers();
    
    /**
     * Returns the lov renderer assignment with the given name or
     * <code>null</code> if there is no such assignment
     * 
     * @param name
     *            The name of the required assignment
     * @return The required assigned or <code>null</code> if no such assignment
     *         exists
     */
    public EJRendererAssignment getApplicationAssignedLovRenderer(String name);
}
