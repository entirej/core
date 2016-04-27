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
import java.util.List;

import org.entirej.framework.core.properties.containers.interfaces.EJCanvasPropertiesContainer;

public interface EJFormProperties extends Serializable
{
    /**
     * Returns the name of the form
     * 
     * @return The form name
     */
    public String getName();
    
    /**
     * Returns the title of this form
     * 
     * @return The form title
     */
    public String getTitle();
    
    /**
     * Returns the name of the first navigable block of this form
     * 
     * @return The name of the first navigable block of this form
     */
    public String getFirstNavigableBlock();
    
    /**
     * Returns the number of display columns that this for uses
     * <p>
     * The form will lay out the main content canvases within a grid. This
     * property defines how many columns the grid should have. A value of
     * <code>1</code> (the default), indicates that all content canvases will be
     * stacked one above each other
     * 
     * @return The number of columns that the form will use to display the
     *         content canvases
     */
    public int getNumCols();
    
    /**
     * Returns the required height of the form
     * <p>
     * The value is the height in pixels
     * 
     * @return The required height of the form
     */
    public int getFormHeight();
    
    /**
     * Returns the required width of the form
     * <p>
     * The value is the width in pixels
     * 
     * @return The required width of the form
     */
    public int getFormWidth();
    
    /**
     * Retrieves the name of the renderer that is responsible for displaying
     * this form
     * 
     * @return the form renderers name
     */
    public String getFormRendererName();
    
    /**
     * Used to retrieve a specific blocks properties.
     * 
     * @param blockName
     *            The name of the required block
     * @return If the block name parameter is a valid block contained within
     *         this form, then its properties will be returned if however the
     *         name is null or not valid, then a <code><b>null</b></code> object
     *         will be returned.
     */
    public EJBlockProperties getBlockProperties(String blockName);
    
    /**
     * Returns a <code>CanvasContainer</code> containing all
     * <code>CanvasProperties</code> defined for this form
     * 
     * @return A <code>CanvasContainer</code> containing all
     *         <code>CanvasProperties</code> for this form
     */
    public EJCanvasPropertiesContainer getCanvasContainer();
    
    /**
     * Returns the canvas properties for the given name
     * 
     * @param name
     *            The name of the required canvas properties
     * @return The <code>CanvasProperties</code> with the required name or
     *         <code>null</code> if there is no canvas with the given name or
     *         the name given was null or a zero length string
     */
    public EJCanvasProperties getCanvasProperties(String name);
    
    /**
     * Returns the lov definition properties for the given name
     * 
     * @param name
     *            The name of the required lov definition properties
     * @return The <code>LovDefinitionProperties</code> with the required name
     *         or <code>null</code> if there is no lov definition with the give
     *         name
     */
    public EJLovDefinitionProperties getLovDefinitionProperties(String name);
    
    /**
     * Returns the names of all the blocks within this form
     * 
     * @return The names of blocks within the form
     */
    public List<String> getBlockNames();
    
    /**
     * Returns the names of all item definitions on the form
     * 
     * @return The names of all lov definitions within the form
     */
    public List<String> getLovDefinitionNames();
    
    /**
     * Returns the names of the items displayed on the given lov definition
     * 
     * @param lovDefinitionName
     *            The name of the lov definition
     * @return The names of the items displayed on the given lov definition
     * @throws IllegalArgumentException
     *             if there is no lov definition with the given name
     */
    public List<String> getLovDefinitionItemNames(String lovDefinitionName) throws IllegalArgumentException;
    
}
