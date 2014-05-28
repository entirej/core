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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJManagedFrameworkConnection;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.EJParameterList;
import org.entirej.framework.core.data.EJDataBlock;
import org.entirej.framework.core.data.EJDataForm;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.enumerations.EJAskToSaveChangesOperation;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.interfaces.EJMessenger;
import org.entirej.framework.core.internal.EJInternalForm;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.EJCoreRelationProperties;
import org.entirej.framework.core.renderers.EJManagedFormRendererWrapper;
import org.entirej.framework.core.renderers.eventhandlers.EJBlockFocusedListener;
import org.entirej.framework.core.renderers.eventhandlers.EJFormEventListener;
import org.entirej.framework.core.renderers.eventhandlers.EJItemFocusListener;
import org.entirej.framework.core.renderers.eventhandlers.EJItemValueChangedListener;
import org.entirej.framework.core.renderers.eventhandlers.EJNewRecordFocusedListener;
import org.entirej.framework.core.renderers.interfaces.EJFormRenderer;
import org.entirej.framework.core.renderers.registry.EJRendererFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJFormController implements Serializable
{
    private final Logger                               LOGGER              = LoggerFactory.getLogger(EJFormController.class);
    
    private EJEditableBlockController                  _focusedBlockController;
    private EJFrameworkManager                         _frameworkManager;
    private EJParameterList                            _parameterList;
    private EJCanvasController                         _canvasController;
    private EJMessenger                                _messenger;
    private EJManagedFormRendererWrapper               _formRenderer;
    private EJManagedActionController                  _formActionController;
    private EJInternalForm                             _form;
    private EJForm                                     _ejForm;
    private EJDataForm                                 _dataForm;
    private HashMap<String, EJEditableBlockController> _blockControllers   = new HashMap<String, EJEditableBlockController>();
    private HashMap<String, EJLovController>           _lovControllers     = new HashMap<String, EJLovController>();
    
    private final ArrayList<EJFormEventListener>    _formEventListeners = new ArrayList<EJFormEventListener>();
    
    EJFormController(EJFrameworkManager frameworkManager, EJDataForm dataForm, EJMessenger messenger)
    {
        LOGGER.trace("START Constructor");
        
        if (dataForm == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_DATA_FORM_PASSED_TO_FORM_CONTROLLER));
        }
        
        _frameworkManager = frameworkManager;
        _dataForm = dataForm;
        _messenger = messenger;
        createParameterList();
        
        _formRenderer = EJRendererFactory.getInstance().getFormRenderer(frameworkManager, dataForm.getProperties());
        
        // First create the actionProcessor instance as this is can be used
        // within the block renderer initialization
        // If none has been defined, use the framework default action processor
        _formActionController = new EJManagedActionController(this);
        
        _form = new EJInternalForm(this);
        _ejForm = new EJForm(_form);
        _canvasController = new EJCanvasController(this);
        
        initialiseController();
        
        // Inform the forms action processor that the form has been created
        
        LOGGER.trace("Calling initialiseForm");
        _formRenderer.getUnmanagedRenderer().initialiseForm(_form);
        LOGGER.trace("Called initialiseForm");
        
        initialiseBlockScreenRenderers();
        
        for (EJBlockController blockController : _blockControllers.values())
        {
            blockController.initialiseItemRenderers();
        }
        
        for (EJLovController lovController : _lovControllers.values())
        {
            lovController.initialiseItemRenderers();
        }

        // getUnmanagedActionController().newFormInstance(new EJForm(_form));
        
        LOGGER.trace("END Constructor");
    }
    
    public void formInitialised()
    {
        getUnmanagedActionController().newFormInstance(new EJForm(_form));
    }
    
    private void createParameterList()
    {
        EJParameterList list = new EJParameterList();
        for (EJInternalFormParameter parameter : _dataForm.getProperties().getAllFormParameters())
        {
            EJFormParameter listParameter = new EJFormParameter(parameter.getName(), parameter.getDataType());
            list.addParameter(listParameter);
        }
        _parameterList = list;
    }
    
    public EJParameterList getParameterList()
    {
        return _parameterList;
    }
    
    private void initialiseController()
    {
        LOGGER.trace("START initialiseController");
        EJCoreFormProperties formProperties = _dataForm.getProperties();
        
        for (EJCoreLovDefinitionProperties lovDefinition : _dataForm.getProperties().getLovDefinitionContainer().getAllLovDefinitionProperties())
        {
            EJLovController lovController = new EJLovController(this, lovDefinition);
            _lovControllers.put(lovDefinition.getName(), lovController);
        }
        
        LOGGER.trace("Setting up blocks");
        ArrayList<EJEditableBlockController> mirroredParents = new ArrayList<EJEditableBlockController>();
        for (EJDataBlock block : _dataForm.getAllBlocks())
        {
            EJCoreBlockProperties blockProps = _dataForm.getProperties().getBlockProperties(block.getName());
            EJEditableBlockController blockController = new EJEditableBlockController(this, blockProps, block);
            _blockControllers.put(blockProps.getName(), blockController);
            
            if (blockProps.isMirrorParent())
            {
                mirroredParents.add(blockController);
            }
        }
        LOGGER.trace("DONE setting up blocks");
        
        // Add mirrored references
        for (EJEditableBlockController parentController : mirroredParents)
        {
            EJMirrorBlockSynchronizer blockSynchroniser = new EJMirrorBlockSynchronizer();
            blockSynchroniser.addMirroredBlockController(parentController);
            parentController.setMirrorBlockSynchronizer(blockSynchroniser);
            
            for (EJEditableBlockController childController : _blockControllers.values())
            {
                if (childController.getProperties().isMirrorChild())
                {
                    if (childController.getProperties().getMirrorBlockName().equals(parentController.getProperties().getName()))
                    {
                        blockSynchroniser.addMirroredBlockController(childController);
                        childController.setDataBlock(parentController.getDataBlock());
                        childController.setMirrorBlockSynchronizer(blockSynchroniser);
                    }
                }
            }
        }
        
        LOGGER.trace("Initialising Pojo Helpers");
        for (EJEditableBlockController parentController : mirroredParents)
        {
            parentController.getBlock().initialiseServicePojoHelper();
        }
        LOGGER.trace("DONE initialising Pojo Helpers");
        
        LOGGER.trace("Setting up relations");
        // Now loop through the relations and set the corresponding properties
        // within the correct block controllers.
        for (EJCoreRelationProperties relationProperties : formProperties.getRelationContainer().getAllRelationProperties())
        {
            EJEditableBlockController masterBlockController = getBlockController(relationProperties.getMasterBlockProperties().getName());
            EJEditableBlockController detailBlockController = getBlockController(relationProperties.getDetailBlockProperties().getName());
            
            masterBlockController.addDetailRelationBlockController(relationProperties, detailBlockController);
            detailBlockController.setMasterRelationDetails(relationProperties, masterBlockController);
        }
        LOGGER.trace("Relations DONE");
        LOGGER.trace("END initialiseController");
    }
    
    private void initialiseBlockScreenRenderers()
    {
        for (EJEditableBlockController blockController : _blockControllers.values())
        {
            blockController.initialiseScreenRenderers();
        }
    }
    
    public EJFrameworkManager getFrameworkManager()
    {
        return _frameworkManager;
    }
    
    public EJCanvasController getCanvasController()
    {
        return _canvasController;
    }
    
    public Collection<EJLovController> getAllLovControllers()
    {
        return _lovControllers.values();
    }
    
    /**
     * Returns a controller for the given lov definition
     * 
     * @param lovDefinitionName
     *            The name of the lov definition
     * 
     * @return An <code>EJLovController</code> for the given lov definition or
     *         <code>null</code> if there is no definition with the given name
     */
    public EJLovController getLovController(String lovDefinitionName)
    {
        if (lovDefinitionName == null || lovDefinitionName.trim().length() == 0)
        {
            return null;
        }
        
        return _lovControllers.get(lovDefinitionName);
    }
    
    public void addBlockFocusedListener(EJBlockFocusedListener listener)
    {
        for (EJEditableBlockController controller : _blockControllers.values())
        {
            controller.addBlockFocusListener(listener);
        }
    }
    
    public void removeBlockFocusedListener(EJBlockFocusedListener listener)
    {
        for (EJEditableBlockController controller : _blockControllers.values())
        {
            controller.removeBlockFocusListener(listener);
        }
    }
    public void addFormEventListener(EJFormEventListener listener)
    {
        _formEventListeners.add(listener);
    }
    
    public void removeFormEventListener(EJFormEventListener listener)
    {
        _formEventListeners.remove(listener);
    }
    
    public void addItemValueChangedListener(EJItemValueChangedListener listener)
    {
        for (EJEditableBlockController controller : _blockControllers.values())
        {
            controller.addItemValueChangedListener(listener);
        }
    }
    
    public void removeItemValueChangedListener(EJItemValueChangedListener listener)
    {
        for (EJEditableBlockController controller : _blockControllers.values())
        {
            controller.removeItemValueChangedListener(listener);
        }
    }
    
    public void addItemFocusListener(EJItemFocusListener listener)
    {
        for (EJEditableBlockController controller : _blockControllers.values())
        {
            controller.addItemFocusListener(listener);
        }
    }
    
    public void removeItemFocusListener(EJItemFocusListener listener)
    {
        for (EJEditableBlockController controller : _blockControllers.values())
        {
            controller.removeItemFocusListener(listener);
        }
    }
    
    public void addNewRecordFocusedListener(EJNewRecordFocusedListener listener)
    {
        for (EJEditableBlockController controller : _blockControllers.values())
        {
            controller.addNewRecordFocusedListener(listener);
        }
    }
    
    public void removeNewRecordFocusedListener(EJNewRecordFocusedListener listener)
    {
        for (EJEditableBlockController controller : _blockControllers.values())
        {
            controller.removeNewRecordFocusedListener(listener);
        }
    }
    
    /**
     * Sets this forms messenger
     * <p>
     * A messenger is required if messages or questions are to be sent to the
     * users
     * <p>
     * There is no default messenger created within the <b>EntireJ Core
     * Framework</b> because the implementation depends on what application type
     * is being used (Java Swing, ULC etc).
     * 
     * @param messenger
     *            The messenger to set
     */
    public void setMessenger(EJMessenger messenger)
    {
        _messenger = messenger;
    }
    
    /**
     * Passes the given <code>Question</code> onto this forms messenger
     * <p>
     * If no messenger has been set, then the question is ignored and no
     * question processing will take place
     * 
     * @param question
     *            The question to ask
     */
    public void askQuestion(EJQuestion question)
    {
        if (_messenger != null)
        {
            _messenger.askQuestion(question);
        }
    }
    
    /**
     * Returns this forms messenger
     * 
     * @return This forms messenger
     */
    public EJMessenger getMessenger()
    {
        return _messenger;
    }
    
    /**
     * Returns the renderer that is responsible for this form
     * 
     * @return This forms renderer
     */
    public EJManagedFormRendererWrapper getManagedRenderer()
    {
        return _formRenderer;
    }
    
    /**
     * Returns the renderer that is responsible for this form
     * 
     * @return This forms renderer
     */
    public EJFormRenderer getRenderer()
    {
        if (_formRenderer != null)
        {
            return _formRenderer.getUnmanagedRenderer();
        }
        
        return null;
    }
    
    /**
     * Return the action controller for this form
     * 
     * @return This forms action controller
     */
    public EJManagedActionController getManagedActionController()
    {
        return _formActionController;
    }
    
    public EJActionController getUnmanagedActionController()
    {
        return _formActionController.getUnmanagedController();
    }
    
    /**
     * Return the manager for this form
     * <p>
     * The form manager is used by developers within the action processor to
     * interact with the form and the <code>EntireJ Framework</code>
     * 
     * @return
     */
    public EJInternalForm getInternalForm()
    {
        return _form;
    }
    
    public EJForm getEJForm()
    {
        return _ejForm;
    }
    
    /**
     * Returns a collection of all <code>BlockControllers</code> for this form
     * controller
     * 
     * @return A <code>Collection</code> of <code>BlockControllers</code> that
     *         have been added to this form controller
     */
    public Collection<EJEditableBlockController> getAllBlockControllers()
    {
        return _blockControllers.values();
    }
    
    /**
     * Returns a collection of <code>BlockControllers</code> that will be
     * displayed on this form. A block is only displayed if it has a canvas
     * assigned
     * 
     * @return a <code>Collection</code> of displayed controllers
     */
    public Collection<EJEditableBlockController> getAllDisplayedBlockControllers()
    {
        ArrayList<EJEditableBlockController> displayedControllers = new ArrayList<EJEditableBlockController>();
        Iterator<EJEditableBlockController> allControllers = getAllBlockControllers().iterator();
        while (allControllers.hasNext())
        {
            EJEditableBlockController controller = allControllers.next();
            if (controller.isBlockDisplayed())
            {
                displayedControllers.add(controller);
            }
        }
        return displayedControllers;
    }
    
    /**
     * Returns the underlying properties of this form
     * 
     * @return The form properties of this forms underlying data form
     */
    public EJCoreFormProperties getProperties()
    {
        return _dataForm.getProperties();
    }
    
    /**
     * Returns a given <code>BlockController</code> for a given block
     * 
     * @param blockName
     *            The name of the blocks for which the controller should be
     *            returned
     * @return The given blocks controller or <code>null</code> if there is no
     *         block controller with the given name
     */
    public EJEditableBlockController getBlockController(String blockName)
    {
        if (blockName == null || blockName.trim().length() == 0)
        {
            return null;
        }
        
        return _blockControllers.get(blockName);
    }
    
    /**
     * Returns the <code>BlockController</code> for the block which currently
     * has user focus
     * <p>
     * If none of the blocks has its focused flag set, then this method will
     * return <code>null</code>
     * 
     * @return The block controller for the currently focused block or
     *         <code>null</code> if no block has focus
     */
    public EJEditableBlockController getFocusedBlockController()
    {
        return _focusedBlockController;
    }
    
    void setFocusedBlockController(EJEditableBlockController controller)
    {
        _focusedBlockController = controller;
    }
    
    public boolean isDirty()
    {
        // First loop through the blocks on this form and then all embedded
        // forms
        Iterator<EJEditableBlockController> blockControllers = getAllBlockControllers().iterator();
        while (blockControllers.hasNext())
        {
            EJEditableBlockController controller = blockControllers.next();
            
            if (controller.isBlockDirty())
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns an iterator over the block controllers of the dirty blocks of
     * this form
     * <p>
     * It is important that if saving the data in these blocks that the save
     * operation is done in the correct order. This means that if a master
     * record has been created then detail records for the master then the
     * master must be inserted before the details so that no constraint errors
     * are produced
     * <p>
     * The <code>ListIterator</code> loops over the blocks in the correct order,
     * ie, detail blocks after their masters
     * <p>
     * 
     * @return A
     *         <code>ListIterator</coed> that is used to iterator over the dirty blocks in the correct order
     */
    private ListIterator<EJEditableBlockController> getAllDirtyBlockControllers(boolean forDelete)
    {
        LinkedList<EJEditableBlockController> dirtyBlocks = new LinkedList<EJEditableBlockController>();
        
        Iterator<EJEditableBlockController> blockControllers = getAllBlockControllers().iterator();
        while (blockControllers.hasNext())
        {
            EJEditableBlockController controller = blockControllers.next();
            
            if (controller.isBlockDirty())
            {
                if (forDelete)
                {
                    addDirtyChild(dirtyBlocks, controller);
                }
                else
                {
                    addDirtyParent(dirtyBlocks, controller);
                }
                if (!dirtyBlocks.contains(controller))
                {
                    dirtyBlocks.add(controller);
                }
            }
        }
        
        return dirtyBlocks.listIterator();
    }
    
    private void addDirtyParent(LinkedList<EJEditableBlockController> dirtyBlocks, EJEditableBlockController childController)
    {
        if (childController.getMasterRelationBlockController() != null)
        {
            if (childController.getMasterRelationBlockController().isBlockDirty())
            {
                addDirtyParent(dirtyBlocks, childController.getMasterRelationBlockController());
                if (!dirtyBlocks.contains(childController.getMasterRelationBlockController()))
                {
                    dirtyBlocks.add(childController.getMasterRelationBlockController());
                }
                
            }
        }
    }
    
    private void addDirtyChild(LinkedList<EJEditableBlockController> dirtyBlocks, EJEditableBlockController parentController)
    {
        if (parentController == null)
        {
            return;
        }
        
        EJCoreBlockProperties blockProperties = parentController.getProperties();
        
        for (EJCoreRelationProperties relation : blockProperties.getFormProperties().getDetailRelationProperties(blockProperties))
        {
            EJCoreBlockProperties detailBlockProperties = relation.getDetailBlockProperties();
            EJEditableBlockController detailBlockController = parentController.getFormController().getBlockController(detailBlockProperties.getName());
            addDirtyChild(dirtyBlocks, detailBlockController);
            if (!dirtyBlocks.contains(detailBlockController))
            {
                dirtyBlocks.add(detailBlockController);
            }
        }
    }
    
    /**
     * Indicates to this controller that all open changes within the form should
     * be saved
     * <p>
     * The form renderer will be informed after the changes have been saved
     * 
     */
    public final void saveChanges()
    {
        EJManagedFrameworkConnection connection = getFrameworkManager().getConnection();
        try
        {
            // First check if there are changes with the current blocks
            Iterator<EJEditableBlockController> blockControllers = getAllBlockControllers().iterator();
            while (blockControllers.hasNext())
            {
                EJEditableBlockController dirtyBlock = blockControllers.next();
                if (dirtyBlock.getManagedRendererController() != null)
                {
                    dirtyBlock.getManagedRendererController().synchronize();
                }
            }
            
            // Loop through the dirty blocks and perform the inserts
            ListIterator<EJEditableBlockController> dirtyControllers = getAllDirtyBlockControllers(false);
            while (dirtyControllers.hasNext())
            {
                EJEditableBlockController controller = dirtyControllers.next();
                controller.insertDirtyRecords();
            }
            
            // Loop through the dirty blocks and perform the updates
            dirtyControllers = getAllDirtyBlockControllers(false);
            while (dirtyControllers.hasNext())
            {
                EJEditableBlockController controller = dirtyControllers.next();
                controller.updateDirtyRecords();
            }
            
            // Loop through the dirty blocks and perform the deletes
            dirtyControllers = getAllDirtyBlockControllers(true);
            while (dirtyControllers.hasNext())
            {
                EJEditableBlockController controller = dirtyControllers.next();
                controller.deleteDirtyRecords();
            }
            
            // If I get this far then all data was saved successfully
            // I need to loop through all dirty blocks and set flags accordingly
            dirtyControllers = getAllDirtyBlockControllers(true);
            while (dirtyControllers.hasNext())
            {
                EJEditableBlockController controller = dirtyControllers.next();
                
                for (EJDataRecord record : controller.getInsertedRecords())
                {
                    record.recordSaved();
                }
                for (EJDataRecord record : controller.getUpdatedRecords())
                {
                    record.recordSaved();
                }
                for (EJDataRecord record : controller.getDeletedRecords())
                {
                    record.recordSaved();
                }
                controller.getDataBlock().blockSaved();
            }
            
            if (_formRenderer != null)
            {
                _formRenderer.savePerformed();
            }
            for (EJFormEventListener listener : new ArrayList<EJFormEventListener>(_formEventListeners))
            {
                listener.formSaved(this);
            }
        }
        catch (Exception e)
        {
            connection.rollback();
            throw new EJApplicationException(e);
        }
        finally
        {
            if (connection != null)
            {
                connection.commit();
                connection.close();
            }
        }
    }
    
    /**
     * Clears the forms blocks and any embedded forms of any data
     * <p>
     * the form renderer will be informed after the form is cleared
     * 
     * @param clearChanges
     *            Indicates if the dirty records contained within the underlying
     *            blocks of this form should also be cleared
     */
    public void clearForm(boolean clearChanges)
    {
        Iterator<EJEditableBlockController> blockControllers = getAllBlockControllers().iterator();
        while (blockControllers.hasNext())
        {
            EJEditableBlockController controller = blockControllers.next();
            controller.clearBlock(clearChanges);
        }
        
        if (_formRenderer != null)
        {
            _formRenderer.formCleared();
        }
        for (EJFormEventListener listener : new ArrayList<EJFormEventListener>(_formEventListeners))
        {
            listener.formCleared(this);
        }
    }
    
    /**
     * This method is called when a form should be closed
     * <p>
     * The Application Manager will be instructed to remove the form from the
     * application and then the framework will clean up all controllers and data
     * used by the form
     * <p>
     * The forms ActionProcessor preFormClosed method will be called before the
     * form is closed
     */
    public void closeForm()
    {
        if (isDirty())
        {
            EJInternalQuestion q = new EJQuestionController().makeAskToSaveChangesQuestion(this, EJAskToSaveChangesOperation.QUESTION_ACTION_CLOSE_FORM);
            getMessenger().askInternalQuestion(q);
        }
        else
        {
            try
            {
                getUnmanagedActionController().preFormClosed(new EJForm(_form));
                _frameworkManager.getApplicationManager().removeFormFromContainer(_form);
                
                clearForm(true);
                if (_formRenderer != null)
                {
                    _formRenderer.formClosed();
                    _formRenderer = null;
                }
            }
            catch (Exception e)
            {
                _form.handleException(e);
            }
            
        }
    }
}
