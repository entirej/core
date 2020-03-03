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
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.data.controllers.EJInternalFormParameter;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.properties.containers.EJCoreBlockPropertiesContainer;
import org.entirej.framework.core.properties.containers.EJCoreCanvasPropertiesContainer;
import org.entirej.framework.core.properties.containers.EJCoreLovDefinitionPropertiesContainer;
import org.entirej.framework.core.properties.containers.EJCoreRelationPropertiesContainer;
import org.entirej.framework.core.properties.containers.interfaces.EJCanvasPropertiesContainer;
import org.entirej.framework.core.properties.containers.interfaces.EJItemGroupPropertiesContainer;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJBlockProperties;
import org.entirej.framework.core.properties.interfaces.EJCanvasProperties;
import org.entirej.framework.core.properties.interfaces.EJDrawerPageProperties;
import org.entirej.framework.core.properties.interfaces.EJFormProperties;
import org.entirej.framework.core.properties.interfaces.EJItemGroupProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.properties.interfaces.EJStackedPageProperties;
import org.entirej.framework.core.properties.interfaces.EJTabPageProperties;

public class EJCoreFormProperties implements EJFormProperties
{
    private boolean                                _isCreatingLovDefinition  = false;
    private boolean                                _isCreatingReferecedBlock = false;
    private EJFrameworkManager                     _frameworkManager;
    private String                                 _name                     = "";
    private String                                 _formRendererName         = "";
    private EJFrameworkExtensionProperties         _formRendererProperties;
    private String                                 _formTitle                = "";
    private String                                 _baseFormTitle            = "";
    private String                                 _actionProcessorClassName = "";
    private int                                    _formHeight               = 400;
    private int                                    _formWidth                = 200;
    private int                                    _numCols                  = 1;
    private String                                 _firstNavigableBlock      = "";

    private EJCoreCanvasPropertiesContainer        _canvasContainer;
    private EJCoreBlockPropertiesContainer         _blockContainer;
    private EJCoreRelationPropertiesContainer      _relationContainer;
    private EJCoreLovDefinitionPropertiesContainer _lovDefinitionContainer;

    private List<EJInternalFormParameter>          _formParameterDefinitions;

    public EJCoreFormProperties(EJFrameworkManager frameworkManager, String formName)
    {
        this(frameworkManager, formName, false, false);
    }

    public EJCoreFormProperties(EJFrameworkManager frameworkManager, String formName, boolean isCreatingLovDefinition, boolean isCreatingReferecedBlock)
    {
        _isCreatingLovDefinition = isCreatingLovDefinition;
        _isCreatingReferecedBlock = isCreatingReferecedBlock;
        _frameworkManager = frameworkManager;
        _canvasContainer = new EJCoreCanvasPropertiesContainer();
        _blockContainer = new EJCoreBlockPropertiesContainer();
        _relationContainer = new EJCoreRelationPropertiesContainer();
        _lovDefinitionContainer = new EJCoreLovDefinitionPropertiesContainer();
        _name = formName;
        _formParameterDefinitions = new ArrayList<EJInternalFormParameter>();
    }

    /**
     * This is an internal method only and is only used during the loading of a
     * form. Once the loading is completed, this method has no use
     * 
     * @return <code>true</code> if EntireJ is loading an LovDefinition
     *         otherwise <code>false</code>
     */
    public boolean isCreatingLovDefinition()
    {
        return _isCreatingLovDefinition;
    }

    /**
     * This is an internal method only and is only used during the loading of a
     * form. Once the loading is completed, this method has no use
     * 
     * @return <code>true</code> if EntireJ is loading a referenced block
     *         otherwise <code>false</code>
     */
    public boolean isCreatingReferencedBlock()
    {
        return _isCreatingReferecedBlock;
    }

    public EJFrameworkManager getFrameworkManager()
    {
        return _frameworkManager;
    }

    /**
     * Used to retrieve the name of the form for which these properties are
     * valid
     * 
     * @return The name of the form
     */
    public String getName()
    {
        return _name;
    }

    /**
     * This is an EntireJ internal method and should not be used. It will change
     * all references within the Properties from the previous name to the name
     * given. This is needed when nesting forms within each other.
     * 
     * @param newName
     *            The new name or internal name for this form when used as a
     *            nested form
     */
    public void changeName(String newName)
    {
        _name = newName;
    }

    /**
     * Returns the required width of the form
     * <p>
     * The value is the width in pixels
     * 
     * @return The required width of the form
     */
    public int getFormWidth()
    {
        return _formWidth;
    }

    /**
     * Sets the required width of the form
     * <p>
     * The value is the width in pixels
     * 
     * @param formWidth
     *            The required width of the form
     */
    public void setFormWidth(int formWidth)
    {
        _formWidth = formWidth;
    }

    /**
     * Returns the required height of the form
     * <p>
     * The value is the height in pixels
     * 
     * @return The required height of the form
     */
    public int getFormHeight()
    {
        return _formHeight;
    }

    /**
     * Sets the required height of the form
     * <p>
     * The value is the height in pixels
     * 
     * @param formHeight
     *            The required height of the form
     */
    public void setFormHeight(int formHeight)
    {
        _formHeight = formHeight;
    }

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
    public int getNumCols()
    {
        return _numCols;
    }

    /**
     * Sets the number of columns the form should use to display the content
     * canvases
     * 
     * @param numCols
     *            The number of columns
     * @see #getNumCols()
     */
    public void setNumCols(int numCols)
    {
        _numCols = numCols;
    }

    /**
     * Retrieves the name of the renderer that is responsible for displaying
     * this form
     * 
     * @return the form renderers name
     */
    public String getFormRendererName()
    {
        return _formRendererName;
    }

    /**
     * Sets the name of the renderer that will be responsible for displaying
     * this form
     * 
     * @param formClassName
     *            the formClassName to set
     */
    public void setFormRendererName(String formRendererName)
    {
        _formRendererName = formRendererName;
    }

    /**
     * Sets the rendering properties for this form
     * <p>
     * These properties are not used internally within the EntireJ Core
     * Framework but can be used by the applications rendering engine to display
     * the form
     * <p>
     * 
     * @param renderingProperties
     *            The rendering properties for this form
     */
    public void setFormRendererProperties(EJFrameworkExtensionProperties renderingProperties)
    {
        _formRendererProperties = renderingProperties;
    }

    public EJFrameworkExtensionProperties getFormRendererProperties()
    {
        return _formRendererProperties;
    }

    /**
     * Returns a <code>RelationContainer</code> containing all
     * <code>RelationProperties</code> for this form
     * 
     * @return The forms <code>RelationContainer</code>
     */
    public EJCoreRelationPropertiesContainer getRelationContainer()
    {
        return _relationContainer;
    }

    /**
     * Returns the relation properties for the given relation name
     * 
     * @param relationName
     *            The name of the required relation properties
     * @return The relation properties for the given name or <code>null</code>
     *         if there is no relation with the given name
     */
    public EJCoreRelationProperties getRelationProperties(String relationName)
    {
        if (relationName == null || relationName.trim().length() == 0)
        {
            return null;
        }
        if (!_relationContainer.contains(relationName))
        {
            return null;
        }
        return (EJCoreRelationProperties) _relationContainer.getRelationProperties(relationName);
    }

    /**
     * Returns the master relation properties where the given block is the
     * detail
     * <p>
     * It is only possible to have on master block per detail block so this
     * method returns only one <code>RelationProperty</code>
     * <p>
     * If the given block is not part of a master-detail relationship then it
     * will have no master block and this method will return null
     * 
     * @param The
     *            properties of the detail block
     * @return The relation where the given block is the detail
     * @throws NullPointerException
     *             if the given detail block properties are null
     */
    public EJCoreRelationProperties getMasterRelationProperties(EJBlockProperties detailBlock)
    {
        if (detailBlock == null)
        {
            throw new NullPointerException("the detail block passed to getMasterRelationProperties is null.");
        }

        Iterator<EJCoreRelationProperties> relations = _relationContainer.getAllRelationProperties().iterator();

        while (relations.hasNext())
        {
            EJCoreRelationProperties relation = relations.next();

            if (relation.getDetailBlockProperties().getFormProperties().getName().equalsIgnoreCase(detailBlock.getFormProperties().getName())
                    && relation.getDetailBlockProperties().getName().equalsIgnoreCase(detailBlock.getName()))
            {
                return relation;
            }
        }
        return null;
    }

    /**
     * Returns a <code>Collection</code> containing all relations where the
     * given block is the master in a master detail relationship.
     * <p>
     * It is possible for a given block to have more than one detail blocks and
     * therefore more than one relation. For this reason, this method will
     * return a <code>Collection</code> containing all necessary relationships.
     * 
     * @param masterBlock
     *            The properties of the block used as a master in a
     *            master-detail relationship-
     * @return A collection of relations where the given block is the master
     *         block or an empty list if there are no relations having the given
     *         block as the master.
     */
    public Collection<EJCoreRelationProperties> getDetailRelationProperties(EJCoreBlockProperties masterBlock)
    {
        ArrayList<EJCoreRelationProperties> relationList = new ArrayList<EJCoreRelationProperties>();

        if (masterBlock == null)
        {
            return relationList;
        }

        Iterator<EJCoreRelationProperties> relations = _relationContainer.getAllRelationProperties().iterator();
        while (relations.hasNext())
        {
            EJCoreRelationProperties relation = relations.next();

            if (relation.getMasterBlockProperties().getFormProperties().getName().equalsIgnoreCase(masterBlock.getFormProperties().getName())
                    && relation.getMasterBlockProperties().getName().equalsIgnoreCase(masterBlock.getName()))
            {
                relationList.add(relation);
            }
        }
        return relationList;
    }

    /**
     * Add a given relation to this forms list of relations
     * 
     * @param relation
     *            The relation to be added
     * @throws NullPointerException
     *             if the relation properties is null
     */
    public void addRelationProperties(EJCoreRelationProperties relationProperties)
    {
        if (relationProperties == null)
        {
            throw new NullPointerException("The relation properties passed to addRelationProperites is null");
        }
        _relationContainer.addRelationProperties(relationProperties);
    }

    /**
     * Adds a canvas to this forms list of canvases
     * 
     * @param canvas
     *            the canvas to add
     * @throws NullPointerException
     *             if the canvas is null
     */
    public void addCanvasProperties(EJCoreCanvasProperties canvas)
    {
        if (canvas == null)
        {
            throw new NullPointerException("The canvas passed to addCanvas is null");
        }
        _canvasContainer.addCanvasProperties(canvas);
    }

    /**
     * Returns a <code>CanvasContainer</code> containing all
     * <code>CanvasProperties</code> defined for this form
     * 
     * @return A <code>CanvasContainer</code> containing all
     *         <code>CanvasProperties</code> for this form
     */
    public EJCoreCanvasPropertiesContainer getCanvasContainer()
    {
        return _canvasContainer;
    }

    /**
     * Returns the canvas properties for the given name
     * 
     * @param name
     *            The name of the required canvas properties
     * @return The <code>CanvasProperties</code> with the required name or
     *         <code>null</code> if there is no canvas with the given name or
     *         the name given was null or a zero length string
     */
    public EJCanvasProperties getCanvasProperties(String name)
    {
        if (name == null || name.trim().length() == 0)
        {
            return null;
        }

        return getCanvasProps(_canvasContainer, name);
    }

    private EJCanvasProperties getCanvasProps(EJCanvasPropertiesContainer container, String canvasName)
    {
        Iterator<EJCanvasProperties> allCanvases = container.getAllCanvasProperties().iterator();
        while (allCanvases.hasNext())
        {
            EJCanvasProperties canvas = allCanvases.next();

            if (canvas.getName().equalsIgnoreCase(canvasName))
            {
                return canvas;
            }

            switch (canvas.getType())
            {
                case POPUP:
                    EJCanvasProperties canvasProps = getCanvasProps(canvas.getPopupCanvasContainer(), canvasName);
                    if (canvasProps != null)
                        return canvasProps;
                    break;

                case TAB:
                    Iterator<EJTabPageProperties> allTabPages = canvas.getTabPageContainer().getAllTabPageProperties().iterator();
                    while (allTabPages.hasNext())
                    {
                        EJCanvasProperties tabPageChildCanvas = getCanvasProps(allTabPages.next().getContainedCanvases(), canvasName);
                        if (tabPageChildCanvas != null && tabPageChildCanvas.getName().equalsIgnoreCase(canvasName))
                        {
                            return tabPageChildCanvas;
                        }
                    }
                    break;
                case DRAWER:
                    Iterator<EJDrawerPageProperties> allDrawerPages = canvas.getDrawerPageContainer().getAllDrawerPageProperties().iterator();
                    while (allDrawerPages.hasNext())
                    {
                        EJCanvasProperties tabPageChildCanvas = getCanvasProps(allDrawerPages.next().getContainedCanvases(), canvasName);
                        if (tabPageChildCanvas != null && tabPageChildCanvas.getName().equalsIgnoreCase(canvasName))
                        {
                            return tabPageChildCanvas;
                        }
                    }
                    break;
                case STACKED:
                    Iterator<EJStackedPageProperties> allStackedPages = canvas.getStackedPageContainer().getAllStackedPageProperties().iterator();
                    while (allStackedPages.hasNext())
                    {
                        EJCanvasProperties stackedPageChildCanvas = getCanvasProps(allStackedPages.next().getContainedCanvases(), canvasName);
                        if (stackedPageChildCanvas != null && stackedPageChildCanvas.getName().equalsIgnoreCase(canvasName))
                        {
                            return stackedPageChildCanvas;
                        }
                    }

                    break;
                case GROUP:

                    EJCanvasProperties gcanvasProps = getCanvasProps(canvas.getGroupCanvasContainer(), canvasName);
                    if (gcanvasProps != null)
                        return gcanvasProps;
                    break;
                case SPLIT:

                    EJCanvasProperties scanvasProps = getCanvasProps(canvas.getSplitCanvasContainer(), canvasName);
                    if (scanvasProps != null)
                        return scanvasProps;
                    break;
                case BLOCK:
                    break;

            }

        }
        return null;
    }

    /**
     * Adds an <code>LovDefinitionProperties</codE> to this forms list of lov
     * definitions
     * 
     * @param lovDefinitions
     *            the lov definition to add
     * @throws NullPointerException
     *             if the lov definition is null
     */
    public void addLovDefinitionProperties(EJCoreLovDefinitionProperties lovDefinition)
    {
        if (lovDefinition == null)
        {
            throw new NullPointerException("The lov definition passed to addLovDefinition is null");
        }
        _lovDefinitionContainer.addLovDefinitionProperties(lovDefinition);
    }

    /**
     * Returns an <code>LovDefinitionContainer</code> containing all
     * <code>LovDefinitionProperties</code> defined for this form
     * 
     * @return A <code>LovDefinitionContainer</code> containing all
     *         <code>LovDefinitionProperties</code> for this form
     */
    public EJCoreLovDefinitionPropertiesContainer getLovDefinitionContainer()
    {
        return _lovDefinitionContainer;
    }

    /**
     * Returns the lov definition properties for the given name
     * 
     * @param name
     *            The name of the required lov definition properties
     * @return The <code>LovDefinitionProperties</code> with the required name
     *         or <code>null</code> if there is no lov definition with the give
     *         name
     */
    public EJCoreLovDefinitionProperties getLovDefinitionProperties(String name)
    {
        if (name == null || name.trim().length() == 0)
        {
            return null;
        }
        if (_lovDefinitionContainer.contains(name))
        {
            return _lovDefinitionContainer.getLovDefinitionProperties(name);
        }
        else
        {
            return null;
        }
    }

    /**
     * the title of the form. This will be the translated title code if a title
     * code has been set otherwise it will return the title code.
     * 
     * @return The title of the form
     */
    public String getTitle()
    {
        return _formTitle;
    }

    /**
     * Sets the title of this form.
     * <p>
     * The base form title is the untranslated form title. This title will be
     * translated by the applications translator if one has been defined
     * 
     * @param title
     *            The form title
     */
    public void setBaseTitle(String title)
    {
        if (title != null && title.trim().length() == 0)
        {
            _baseFormTitle = null;
        }
        else
        {
            _baseFormTitle = title;
        }
    }

    /**
     * Returns the untranslated title of this form
     * 
     * @return The untranslated title
     */
    public String getBaseTitle()
    {
        return _baseFormTitle;
    }

    /**
     * This method is used internally within <b>EntireJ</b> after translating
     * the base frame title
     * 
     * @param translatedTitle
     *            The base frame title after translation
     */
    public void setTranslatedTitle(String translatedTitle)
    {
        if (translatedTitle == null || translatedTitle.trim().length() == 0)
        {
            _formTitle = _baseFormTitle;
        }
        else
        {
            _formTitle = translatedTitle;
        }
    }

    /**
     * The Action Processor is responsible for actions within the form. Actions
     * can include buttons being pressed, check boxes being selected or pre-post
     * query methods etc.
     * 
     * @return The name of the Action Processor responsible for this form.
     */
    public String getActionProcessorClassName()
    {
        return _actionProcessorClassName;
    }

    /**
     * Sets the action processor name for this form
     * <p>
     * The processor is a mandatory parameter and trying to set the processor
     * name to a null or zero length value will result in a
     * <code>FrameworkCoreException</code>
     * 
     * @param processorName
     *            The action processor name for this form
     */
    public void setActionProcessorClassName(String processorName)
    {
        _actionProcessorClassName = processorName;
    }

    /**
     * Returns the name of the first navigable block of this form
     * 
     * @return The name of the first navigable block of this form
     */
    public String getFirstNavigableBlock()
    {
        return _firstNavigableBlock;
    }

    /**
     * Sets the first navigable block of this form
     * <p>
     * The first navigable block can be set by the developer or, if not set, it
     * will be set to the first visible block on the form
     * 
     * @param blockName
     *            The name of this forms first navigable block
     */
    public void setFirstNavigableBlock(String blockName)
    {
        _firstNavigableBlock = blockName;
    }

    /**
     * Adds block properties to this form.
     * <p>
     * This method will do nothing if the block properties passed are
     * <code>null</code>
     * 
     * @param blockProperties
     *            The block properties to be added to this form
     */
    public void addBlockProperties(EJCoreBlockProperties blockProperties)
    {
        if (blockProperties != null)
        {
            _blockContainer.addBlockProperties(blockProperties);
        }
    }

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
    public EJCoreBlockProperties getBlockProperties(String blockName)
    {
        if (blockName == null || blockName.trim().length() == 0)
        {
            return null;
        }
        else
        {
            if (_blockContainer.contains(blockName))
            {
                return (EJCoreBlockProperties) _blockContainer.getBlockProperties(blockName);
            }
            else
            {
                return null;
            }
        }
    }

    public Collection<EJInternalFormParameter> getAllFormParameters()
    {
        return _formParameterDefinitions;
    }

    public void addFormParameter(EJInternalFormParameter parameter)
    {
        if (parameter != null)
        {
            _formParameterDefinitions.add(parameter);
        }
    }

    public EJInternalFormParameter getFormParameter(String name)
    {
        for (EJInternalFormParameter parameter : _formParameterDefinitions)
        {
            if (parameter.getName().equalsIgnoreCase(name))
            {
                return parameter;
            }
        }

        return null;
    }

    /**
     * Used to return a <code>BlockContainer</code> containing all of the blocks
     * within this form
     * 
     * @return A <code>BlockContainer</code> containing this forms
     *         <code>BlockProperties</code>
     */
    public EJCoreBlockPropertiesContainer getBlockContainer()
    {
        return _blockContainer;
    }

    public List<String> getBlockNames()
    {
        ArrayList<String> names = new ArrayList<String>();

        Iterator<EJCoreBlockProperties> blocks = _blockContainer.getAllBlockProperties().iterator();
        while (blocks.hasNext())
        {
            names.add(blocks.next().getName());
        }

        return names;
    }

    public List<String> getLovDefinitionItemNames(String lovDefinitionName) throws IllegalArgumentException
    {
        EJCoreLovDefinitionProperties lovDef = _lovDefinitionContainer.getLovDefinitionProperties(lovDefinitionName);
        if (lovDef == null)
        {
            throw new IllegalArgumentException("Invalid LovDefinitionName passed to getDisplayLovDefinitionItems: " + lovDefinitionName);
        }
        ArrayList<String> names = new ArrayList<String>();
        addGroupItems(lovDef.getBlockProperties().getScreenItemGroupContainer(EJScreenType.MAIN), names);

        return names;
    }

    private void addGroupItems(EJItemGroupPropertiesContainer itemGroupContainer, ArrayList<String> itemNameList)
    {
        Iterator<EJItemGroupProperties> groupProperties = itemGroupContainer.getAllItemGroupProperties().iterator();
        while (groupProperties.hasNext())
        {
            EJItemGroupProperties itemGroupProperties = groupProperties.next();
            Iterator<EJScreenItemProperties> items = itemGroupProperties.getAllItemProperties().iterator();
            while (items.hasNext())
            {
                EJScreenItemProperties itemProps = items.next();
                itemNameList.add(itemProps.getReferencedItemName());
            }

            addGroupItems(itemGroupProperties.getChildItemGroupContainer(), itemNameList);
        }
    }

    public List<String> getLovDefinitionNames()
    {
        ArrayList<String> names = new ArrayList<String>();

        Iterator<EJCoreLovDefinitionProperties> lovDefs = _lovDefinitionContainer.getAllLovDefinitionProperties().iterator();
        while (lovDefs.hasNext())
        {
            names.add(lovDefs.next().getName());
        }

        return names;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("\nForm: ");
        buffer.append(getName());

        return buffer.toString();
    }
}
