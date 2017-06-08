/*******************************************************************************
 * Copyright 2013 Mojave Innovations GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors: Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.data.controllers;

import java.io.Serializable; 
import java.util.HashMap;
import java.util.Iterator;

import org.entirej.framework.core.EJActionProcessorException;
import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJBlock;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJLov;
import org.entirej.framework.core.EJManagedFrameworkConnection;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJParameterList;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.EJScreenItem;
import org.entirej.framework.core.actionprocessor.EJDefaultFormActionProcessor;
import org.entirej.framework.core.actionprocessor.interfaces.EJBlockActionProcessor;
import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;
import org.entirej.framework.core.actionprocessor.interfaces.EJLovActionProcessor;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.enumerations.EJLovDisplayReason;
import org.entirej.framework.core.enumerations.EJPopupButton;
import org.entirej.framework.core.enumerations.EJRecordType;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.internal.EJInternalLov;
import org.entirej.framework.core.processorfactories.EJActionProcessorFactory;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.service.EJQueryCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJActionController implements Serializable
{
    final Logger                                    logger = LoggerFactory.getLogger(EJActionController.class);

    private EJFormController                        _formController;
    private EJFormActionProcessor                   _formLevelActionProcessor;
    private HashMap<String, EJBlockActionProcessor> _blockLevelActionProcessors;

    private HashMap<String, EJLov>                  _lovs;
    private HashMap<String, EJLovActionProcessor>   _lovActionProcessors;

    public EJActionController(EJFormController formController)
    {
        _formController = formController;
        _blockLevelActionProcessors = new HashMap<String, EJBlockActionProcessor>();
        _lovs = new HashMap<String, EJLov>();
        _lovActionProcessors = new HashMap<String, EJLovActionProcessor>();

        loadActionProcessors();
    }

    public EJFormActionProcessor getFormActionProcessor()
    {
        return _formLevelActionProcessor;
    }

    private void loadActionProcessors()
    {
        if (_formController.getProperties().getActionProcessorClassName() != null && _formController.getProperties().getActionProcessorClassName().length() > 0)
        {
            _formLevelActionProcessor = EJActionProcessorFactory.getInstance().getActionProcessor(_formController.getProperties());
        }
        else
        {
            _formLevelActionProcessor = new EJDefaultFormActionProcessor();
        }

        // Get all the block level action processors
        Iterator<EJCoreBlockProperties> allBlockProperties = _formController.getProperties().getBlockContainer().getAllBlockProperties().iterator();
        while (allBlockProperties.hasNext())
        {
            EJCoreBlockProperties blockProperties = allBlockProperties.next();
            EJBlockActionProcessor processor = EJActionProcessorFactory.getInstance().getActionProcessor(blockProperties);
            if (processor != null)
            {
                _blockLevelActionProcessors.put(blockProperties.getName(), processor);
            }
        }

        // Get the action processors that might have been defined for the
        // LOV definition
        for (EJLovController controller : _formController.getAllLovControllers())
        {
            EJCoreLovDefinitionProperties lovDefinitionProperties = controller.getDefinitionProperties();
            EJLovActionProcessor processor = EJActionProcessorFactory.getInstance().getActionProcessor(lovDefinitionProperties);
            if (processor != null)
            {
                _lovs.put(lovDefinitionProperties.getName(), new EJLov(new EJInternalLov(controller, processor)));
                _lovActionProcessors.put(lovDefinitionProperties.getName(), processor);
            }
        }
    }

    public void questionAnswered(EJQuestion question)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START QuestionAnswered");
        EJManagedFrameworkConnection connection = _formController.getFrameworkManager().getConnection();
        try
        {
            if (question.getBlock() != null && question.getBlock().getProperties().isUsedInLovDefinition())
            {
                String lovName = question.getBlock().getProperties().getLovDefinition().getName();
                if (_lovActionProcessors.containsKey(lovName))
                {
                    if (traceEnabled)
                        logger.trace("Calling LOV level questionAnswered. Lov {}", lovName);
                    _lovActionProcessors.get(lovName).questionAnswered(question);
                    if (traceEnabled)
                        logger.trace("Called LOV level questionAnswered");
                }
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level questionAnswered");
                _formLevelActionProcessor.questionAnswered(question);
                if (traceEnabled)
                    logger.trace("Called form level questionAnswered");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END QuestionAnswered");
    }
    public void filesUploaded(EJFileUpload fileUpload)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START FilesUploaded");
        EJManagedFrameworkConnection connection = _formController.getFrameworkManager().getConnection();
        try
        {
            
                if (traceEnabled)
                    logger.trace("Calling form level filesUploaded");
                _formLevelActionProcessor.filesUploaded(fileUpload);
                if (traceEnabled)
                    logger.trace("Called form level filesUploaded");
            
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END FilesUploaded");
    }

    public void newBlockInstance(EJForm form, String blockName)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START newBlockInstance. Form: {}, Block: {}", form.getName(), blockName);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.newBlockInstance(form, blockName);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END newBlockInstance");
    }

    public void focusGained(EJForm form)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START focusGained. Form: {}", form.getName());
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.focusGained(form);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END focusGained");
    }

    public void preFormOpened(EJForm form)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preFormOpened. Form: {}", form.getName());

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.preFormOpened(form);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END preFormOpened");
    }

    public void postFormSave(EJForm form) throws EJActionProcessorException
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START postFormSave. Form: {}", form.getName());

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.postFormSave(form);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END postFormSave");
    }

    public void preFormClosed(EJForm form)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preFormClosed. Form: {}", form.getName());

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.preFormClosed(form);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END preFormClosed");
    }

    public void newFormInstance(EJForm form)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START newFormInstance. Form: {}", form.getName());
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.newFormInstance(form);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END newFormInstance");
    }

    public void popupCanvasClosing(EJForm form, String popupCanvasName, EJPopupButton button)
    {
        if (logger.isTraceEnabled())
            logger.trace("START popupCanvasClosing. Form: {}, Canvas: {}, Button: {}", form.getName(), popupCanvasName, button);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.popupCanvasClosing(form, popupCanvasName, button);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (logger.isTraceEnabled())
            logger.trace("END poupCanvasClosing");
    }

    public void popupCanvasClosed(EJForm form, String popupCanvasName, EJPopupButton button)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START popupCanvasClosed. Form: {}, Canvas: {}", form.getName(), popupCanvasName);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.popupCanvasClosed(form, popupCanvasName, button);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END poupCanvasClosed");
    }

    public void popupCanvasOpened(EJForm form, String popupCanvasName)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START popupCanvasOpened. Form: {}, Canvas: {}", form.getName(), popupCanvasName);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.popupCanvasOpened(form, popupCanvasName);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END popupCanvasOpened");
    }

    public void preOpenPopupCanvas(EJForm form, String popupCanvasName)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preOpenPopupCanvas. Form: {}, Canvas: {}", form.getName(), popupCanvasName);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.preOpenPopupCanvas(form, popupCanvasName);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END preOpenPopupCanvas");
    }

    public void modalFormClosing(EJForm form, EJParameterList parameterList)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START modalFormClosing. Form: {}", form.getName());
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.modalFormClosing(form, parameterList);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END modalFormClosing");
    }

    public void preShowStackedPage(EJForm form, String stackedCanvasName, String stackedPageName)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preShowStackedPage. Form: {}, StackedCanvasName: {}, StackedPageName: {}", form.getName(), stackedCanvasName, stackedPageName);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.preShowStackedPage(form, stackedCanvasName, stackedPageName);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END preShowStackedPage");
    }

    public void stackedPageChanged(EJForm form, String stackedCanvasName, String stackedPageName)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START stackedPageChanged. Form: {}, StackedCanvasName: {}, StackedPageName: {}", form.getName(), stackedCanvasName, stackedPageName);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.stackedPageChanged(form, stackedCanvasName, stackedPageName);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END stackedPageChanged");
    }

    public void preShowTabPage(EJForm form, String tabCanvasName, String tabPageName)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preShowTabPage. Form: {}, TabCanvasName: {}, TabPageName: {}", form.getName(), tabCanvasName, tabPageName);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.preShowTabPage(form, tabCanvasName, tabPageName);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END preShowTabPage");
    }

    public void tabPageChanged(EJForm form, String tabCanvasName, String tabPageName)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START tabPageChanged. Form: {}, TabCanvasName: {}, TabPageName: {}", form.getName(), tabCanvasName, tabPageName);

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.tabPageChanged(form, tabCanvasName, tabPageName);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END tabPageChanged");
    }
    
    public void preShowDrawerPage(EJForm form, String drawerCanvasName, String drawerPageName)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preShowDrawerPage. Form: {}, DrawerCanvasName: {}, DrawerPageName: {}", form.getName(), drawerCanvasName, drawerPageName);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.preShowTabPage(form, drawerCanvasName, drawerPageName);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END preShowDrawerPage");
    }
    
    public void drawerPageChanged(EJForm form, String drawerCanvasName, String drawerPageName)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START drawerPageChanged. Form: {}, DrawerCanvasName: {}, DrawerPageName: {}", form.getName(), drawerCanvasName, drawerPageName);

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            _formLevelActionProcessor.drawerPageChanged(form, drawerCanvasName, drawerPageName);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END drawerPageChanged");
    }

    public void popupFormClosed(EJParameterList parameterList)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START popupFormClosed");

        EJManagedFrameworkConnection connection = null;
        if (_formController != null)
        {
            connection = _formController.getFrameworkManager().getConnection();
        }
        try
        {
            _formLevelActionProcessor.popupFormClosed(parameterList);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            new EJApplicationException(e);
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        if (traceEnabled)
            logger.trace("END popupFormClosed");
    }

    public void embeddedFormClosed(EJParameterList parameterList)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START embeddedFormClosed");

        EJManagedFrameworkConnection connection = null;
        if (_formController != null)
        {
            connection = _formController.getFrameworkManager().getConnection();
        }
        try
        {
            _formLevelActionProcessor.embeddedFormClosed(parameterList);
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            new EJApplicationException(e);
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        if (traceEnabled)
            logger.trace("END embeddedFormClosed");
    }

    public void executeActionCommand(EJForm form, String blockName, String command, EJScreenType screenType)
    {
        executeActionCommand(form, blockName, command, screenType, null);
    }

    public void executeActionCommand(EJForm form, String blockName, String command, EJScreenType screenType, String lovDefinitionName)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START executeActionCommand. Form: {}, Command: {}, Screen: {}", form.getName(), command, screenType);

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            if (lovDefinitionName != null)
            {
                if (_lovActionProcessors.containsKey(lovDefinitionName))
                {
                    if (traceEnabled)
                        logger.trace("Calling LOV level executeActionCommand: Lov: {}", lovDefinitionName);
                    _lovActionProcessors.get(lovDefinitionName).executeActionCommand(_lovs.get(lovDefinitionName), blockName, command, screenType);
                    if (traceEnabled)
                        logger.trace("Called LOV level executeActionCommand");
                }
            }
            else
            {
                if (_blockLevelActionProcessors.containsKey(blockName))
                {
                    if (traceEnabled)
                        logger.trace("Calling block level executeActionCommand. Block: {}", blockName);
                    _blockLevelActionProcessors.get(blockName).executeActionCommand(form, blockName, command, screenType);
                    if (traceEnabled)
                        logger.trace("Called block level executeActionCommand");
                }
                else
                {
                    if (traceEnabled)
                        logger.trace("Calling form level executeActionCommand");
                    _formLevelActionProcessor.executeActionCommand(form, blockName, command, screenType);
                    if (traceEnabled)
                        logger.trace("Called form level executeActionCommand");
                }
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END executeActionCommand");
    }

    public void postItemChanged(EJForm form, String blockName, String itemName, EJScreenType screenType)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START postItemChange. Form: {}, Block: {}, Item: {}, Screen: {}", form.getName(), blockName, itemName, screenType);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                if (traceEnabled)
                    logger.trace("Calling block level postItemChange. Block: {}", blockName);
                _blockLevelActionProcessors.get(blockName).postItemChanged(form, blockName, itemName, screenType);
                if (traceEnabled)
                    logger.trace("Called block level postItemChange");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level postItemChange");
                _formLevelActionProcessor.postItemChanged(form, blockName, itemName, screenType);
                if (traceEnabled)
                    logger.trace("Called form level postItemChange");
            }

        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END postItemChange");
    }

    public void validateItem(EJForm form, String blockName, String itemName, EJScreenType screenType, EJRecord newValues)
    {
        validateItem(form, blockName, itemName, screenType, newValues, null);
    }

    public void validateItem(EJForm form, String blockName, String itemName, EJScreenType screenType, EJRecord newValues, String lovDefinitionName)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START validateItem. Form: {}, Item: {}, Screen: {}", form.getName(), itemName, screenType);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            if (lovDefinitionName != null)
            {
                if (_lovActionProcessors.containsKey(lovDefinitionName))
                {
                    if (traceEnabled)
                        logger.trace("Calling LOV level validateItem. Lov: {}", lovDefinitionName);
                    _lovActionProcessors.get(lovDefinitionName).validateItem(_lovs.get(lovDefinitionName), blockName, itemName, screenType);
                    if (traceEnabled)
                        logger.trace("Called LOV level validateItem");
                }
            }
            else
            {
                if (_blockLevelActionProcessors.containsKey(blockName))
                {
                    if (traceEnabled)
                        logger.trace("Calling block level validateItem. Block: {}", blockName);
                    _blockLevelActionProcessors.get(blockName).validateItem(form, blockName, itemName, screenType, newValues);
                    if (traceEnabled)
                        logger.trace("Called block level validateItem");
                }
                else
                {
                    if (traceEnabled)
                        logger.trace("Calling form level validateItem");
                    _formLevelActionProcessor.validateItem(form, blockName, itemName, screenType, newValues);
                    if (traceEnabled)
                        logger.trace("Called form level validateItem");
                }
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END validateItem");
    }

    public EJMessage getMasterDetailDeleteViolationMessage(EJForm form, String relationName)
    {
        if (logger.isTraceEnabled())
            logger.trace("START getMasterDetailDeleteViolationMessage. Form: {}, Relation: {}", form.getName(), relationName);

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            return _formLevelActionProcessor.getMasterDetailDeleteViolationMessage(form, relationName);
        }
        catch (Exception e)
        {
            connection.rollback();
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
            if (logger.isTraceEnabled())
                logger.trace("END  getMasterDetailDeleteViolationMessage");
        }
    }

    public void postQuery(EJForm form, EJRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START postQuery. Form: {}", form.getName());

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = record == null ? "" : record.getBlockName();
            if (record != null && record.isUsedInLovDefinition())
            {
                String lovName = record.getLovDefinitionName();
                if (_lovActionProcessors.containsKey(lovName))
                {
                    if (traceEnabled)
                        logger.trace("Calling LOV level postQuery. Lov: {}", lovName);
                    _lovActionProcessors.get(lovName).postQuery(_lovs.get(lovName), record);
                    if (traceEnabled)
                        logger.trace("Called LOV level postQuery");
                }
            }
            else
            {
                if (_blockLevelActionProcessors.containsKey(blockName))
                {
                    if (traceEnabled)
                        logger.trace("Calling block level postQuery. Block: {}", blockName);
                    _blockLevelActionProcessors.get(blockName).postQuery(form, record);
                    if (traceEnabled)
                        logger.trace("Called block level postQuery");
                }
                else
                {
                    if (traceEnabled)
                        logger.trace("Calling form level postQuery");
                    _formLevelActionProcessor.postQuery(form, record);
                    if (traceEnabled)
                        logger.trace("Called form level postQuery");
                }
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END postQuery");
    }

    public void preQuery(EJForm form, EJQueryCriteria queryCriteria)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preQuery. Form: {}", form.getName());

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = queryCriteria == null ? "" : queryCriteria.getBlockName();
            if (queryCriteria != null && queryCriteria.isUsedInLov())
            {
                String lovName = queryCriteria.getLovName();
                if (_lovActionProcessors.containsKey(lovName))
                {
                    if (traceEnabled)
                        logger.trace("Calling LOV  level preQuery. Lov: {}", lovName);
                    _lovActionProcessors.get(lovName).preQuery(_lovs.get(lovName), queryCriteria);
                    if (traceEnabled)
                        logger.trace("Called LOV level preQuery");
                }
            }
            else
            {
                if (_blockLevelActionProcessors.containsKey(blockName))
                {
                    if (traceEnabled)
                        logger.trace("Calling block level preQuery. Block: {}", blockName);
                    _blockLevelActionProcessors.get(blockName).preQuery(form, queryCriteria);
                    if (traceEnabled)
                        logger.trace("Called block level preQuery");
                }
                else
                {
                    if (traceEnabled)
                        logger.trace("Calling form level preQuery");
                    _formLevelActionProcessor.preQuery(form, queryCriteria);
                    if (traceEnabled)
                        logger.trace("Called form level preQuery");
                }
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END preQuery");
    }

    public void preDelete(EJForm form, EJRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preDelete. Form: {}", form.getName());
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            if (_blockLevelActionProcessors.containsKey(record.getBlockName()))
            {
                if (traceEnabled)
                    logger.trace("Calling block level preDelete. Block: {}", record.getBlockName());
                _blockLevelActionProcessors.get(record.getBlockName()).preDelete(form, (record));
                if (traceEnabled)
                    logger.trace("Called block level preDelete");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level preDelete");
                _formLevelActionProcessor.preDelete(form, record);
                if (traceEnabled)
                    logger.trace("Called form level preDelete");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END preDelete");
    }

    public void postDelete(EJForm form, EJRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START postDelete. Form: {}", form.getName());
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            if (_blockLevelActionProcessors.containsKey(record.getBlockName()))
            {
                if (traceEnabled)
                    logger.trace("Calling block level postDelete. Block: {}", record.getBlockName());
                _blockLevelActionProcessors.get(record.getBlockName()).postDelete(form, record);
                if (traceEnabled)
                    logger.trace("Calling block level postDelete");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level postDelete");

                _formLevelActionProcessor.postDelete(form, record);
                if (traceEnabled)
                    logger.trace("Calling form level postDelete");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END postDelete");
    }

    public void preInsert(EJForm form, EJRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preInsert. Form: {}", form.getName());
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            if (_blockLevelActionProcessors.containsKey(record.getBlockName()))
            {
                if (traceEnabled)
                    logger.trace("Calling block level preInsert. Block: {}", record.getBlockName());
                _blockLevelActionProcessors.get(record.getBlockName()).preInsert(form, record);
                if (traceEnabled)
                    logger.trace("Called block level preInsert");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level preInsert");
                _formLevelActionProcessor.preInsert(form, record);
                if (traceEnabled)
                    logger.trace("Called form level preInsert");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END preInsert");
    }

    public void postInsert(EJForm form, EJRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START postInsert. Form: {}", form.getName());
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            if (_blockLevelActionProcessors.containsKey(record.getBlockName()))
            {
                if (traceEnabled)
                    logger.trace("Calling block level postInsert. Block: {}", form.getName());
                _blockLevelActionProcessors.get(record.getBlockName()).postInsert(form, record);
                if (traceEnabled)
                    logger.trace("Called block level postInsert");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level postInsert");
                _formLevelActionProcessor.postInsert(form, record);
                if (traceEnabled)
                    logger.trace("Called block level postInsert");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("START postInsert");
    }

    public void preUpdate(EJForm form, EJRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preUpdate. Form: {}", form.getName());

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            if (_blockLevelActionProcessors.containsKey(record.getBlockName()))
            {
                if (traceEnabled)
                    logger.trace("Calling block level preUpdate. Block {}", record.getBlockName());
                _blockLevelActionProcessors.get(record.getBlockName()).preUpdate(form, record);
                if (traceEnabled)
                    logger.trace("Called block level preUpdate");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level preUpdate");
                _formLevelActionProcessor.preUpdate(form, record);
                if (traceEnabled)
                    logger.trace("Called form level preUpdate");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END preUpdate");
    }

    public void lovActivated(EJForm form, EJScreenItem screenItem, EJLovDisplayReason displayReason)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START lovActivated. Form: {}, ScreenItem {}, DisplayReason {}", form.getName(), (screenItem == null ? "null" : screenItem.getName()), displayReason);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = screenItem.getBlockName();
            if (screenItem != null && screenItem.isUsedInLovDefinition())
            {
                String lovName = screenItem.getLovDefinitionName();
                if (_lovActionProcessors.containsKey(lovName))
                {
                    if (traceEnabled)
                        logger.trace("Calling LOV level lovActivated. Lov: {}", lovName);
                    _lovActionProcessors.get(lovName).lovActivated(_lovs.get(lovName), screenItem, displayReason);
                    if (traceEnabled)
                        logger.trace("Called LOV level lovActivated");
                }
            }
            if (_blockLevelActionProcessors.containsKey(screenItem.getBlockName()))
            {
                if (traceEnabled)
                    logger.trace("Calling block level lovActivated. Block: {}", blockName);
                _blockLevelActionProcessors.get(blockName).lovActivated(form, screenItem, displayReason);
                if (traceEnabled)
                    logger.trace("Called block level lovActivated");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level lovActivated");
                _formLevelActionProcessor.lovActivated(form, screenItem, displayReason);
                if (traceEnabled)
                    logger.trace("Called form level lovActivated");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END lovActivated");
    }

    public void lovCompleted(EJForm form, EJScreenItem screenItem, boolean valueChosen)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START lovCompleted. Form: {}, ScreenItem {}", form.getName(), (screenItem == null ? "null" : screenItem.getName()));
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = screenItem.getBlockName();
            if (screenItem != null && screenItem.isUsedInLovDefinition())
            {
                String lovName = screenItem.getLovDefinitionName();
                if (_lovActionProcessors.containsKey(lovName))
                {
                    if (traceEnabled)
                        logger.trace("Calling LOV level lovCompleted. Lov: {}", lovName);
                    _lovActionProcessors.get(lovName).lovCompleted(_lovs.get(lovName), screenItem, valueChosen);
                    if (traceEnabled)
                        logger.trace("Called LOV level lovCompleted");
                }
            }
            if (_blockLevelActionProcessors.containsKey(screenItem.getBlockName()))
            {
                if (traceEnabled)
                    logger.trace("Calling block level lovCompleted. Block: {}", blockName);
                _blockLevelActionProcessors.get(blockName).lovCompleted(form, screenItem, valueChosen);
                if (traceEnabled)
                    logger.trace("Called block level lovCompleted");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level lovCompleted");
                _formLevelActionProcessor.lovCompleted(form, screenItem, valueChosen);
                if (traceEnabled)
                    logger.trace("Called form level lovCompleted");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END lovCompleted");
    }

    public void postUpdate(EJForm form, EJRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START postUpdate. Form: {}", form.getName());
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {

            if (_blockLevelActionProcessors.containsKey(record.getBlockName()))
            {
                if (traceEnabled)
                    logger.trace("Calling block level postUpdate. Block: {}", record.getBlockName());
                _blockLevelActionProcessors.get(record.getBlockName()).postUpdate(form, record);
                if (traceEnabled)
                    logger.trace("Called block level postUpdate");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level postUpdate");
                _formLevelActionProcessor.postUpdate(form, record);
                if (traceEnabled)
                    logger.trace("Called form level postUpdate");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END postUpdate");
    }

    public void newRecordInstance(EJForm form, EJRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START newRecordInstance. Form: {}", form.getName());

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = record == null ? "" : record.getBlockName();
            if (record != null && record.isUsedInLovDefinition())
            {
                String lovName = record.getLovDefinitionName();
                if (_lovActionProcessors.containsKey(lovName))
                {
                    if (traceEnabled)
                        logger.trace("Calling LOV level newRecordInstance. Lov: {}", lovName);
                    _lovActionProcessors.get(lovName).newRecordInstance(_lovs.get(lovName), record);
                    if (traceEnabled)
                        logger.trace("Called LOV level newRecordInstance");
                }
            }
            else
            {
                if (_blockLevelActionProcessors.containsKey(blockName))
                {
                    if (traceEnabled)
                        logger.trace("Calling block level newRecordInstance. Block: {}", record.getBlockName());
                    _blockLevelActionProcessors.get(blockName).newRecordInstance(form, record);
                    if (traceEnabled)
                        logger.trace("Called block level newRecordInstance");
                }
                else
                {
                    if (traceEnabled)
                        logger.trace("Calling form level newRecordInstance");
                    _formLevelActionProcessor.newRecordInstance(form, record);
                    if (traceEnabled)
                        logger.trace("Called form level newRecordInstance");
                }
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END newRecordInstance");
    }

    public void validateRecord(EJForm form, EJRecord record, EJRecordType recordType)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START validateRecord. Form: {}, RecordType: {}", form.getName(), recordType);
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = record == null ? "" : record.getBlockName();
            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                if (traceEnabled)
                    logger.trace("Calling block level validateRecord. Block: {}", blockName);
                _blockLevelActionProcessors.get(blockName).validateRecord(form, record, recordType);
                if (traceEnabled)
                    logger.trace("Called block level validateRecord");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level validateRecord");
                _formLevelActionProcessor.validateRecord(form, record, recordType);
                if (traceEnabled)
                    logger.trace("Called form level validateRecord");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END validateRecord");
    }

    public void validateQueryCriteria(EJForm form, EJQueryCriteria queryCriteria)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START validateQueryCriteria. Form: {}", form.getName());

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = queryCriteria == null ? "" : queryCriteria.getBlockName();

            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                if (traceEnabled)
                    logger.trace("Calling block level validateQueryCiteria. Block: {}", blockName);
                _blockLevelActionProcessors.get(blockName).validateQueryCriteria(form, queryCriteria);
                if (traceEnabled)
                    logger.trace("Called block level validateQueryCriteria");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level validateQueryCriteria");
                _formLevelActionProcessor.validateQueryCriteria(form, queryCriteria);
                if (traceEnabled)
                    logger.trace("Called form level validateQueryCriteria");
            }

            // I we are querying an LOV, then we need to check the criteria
            // before executing the query. The Form/Block is fist called to
            // validate in case they want to add some criteria to the lov before
            // executing
            if (queryCriteria != null && queryCriteria.isUsedInLov())
            {
                String lovName = queryCriteria.getLovName();
                if (_lovActionProcessors.containsKey(lovName))
                {
                    if (traceEnabled)
                        logger.trace("Calling LOV level validateQueryCriteria. Lov: {}", lovName);
                    _lovActionProcessors.get(lovName).validateQueryCriteria(_lovs.get(lovName), queryCriteria);
                    if (traceEnabled)
                        logger.trace("Called LOV level validateQueryCriteria");
                }
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END validateQueryCriteria");
    }

    public void postBlockQuery(EJForm form, EJBlock block)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START postBlockQuery. Form: {}, Block: {}", form.getName(), block.getName());
        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            if (!block.isUsedInLovDefinition())
            {
                if (_blockLevelActionProcessors.containsKey(block.getName()))
                {
                    if (traceEnabled)
                        logger.trace("Calling block level postBlockQuery. Block: {}", block.getName());
                    _blockLevelActionProcessors.get(block.getName()).postBlockQuery(form, block);
                    if (traceEnabled)
                        logger.trace("Called block level postBlockQuery");
                }
                else
                {
                    if (traceEnabled)
                        logger.trace("Calling form level postBlockQuery");
                    _formLevelActionProcessor.postBlockQuery(form, block);
                    if (traceEnabled)
                        logger.trace("Called form level postBlockQuery");
                }
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END postBlockQuery");
    }

    public void preOpenLovQueryScreen(EJInternalBlock block)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preOpenLovQueryScreen. Form: {}, Block: {}", (block == null ? "null" : block.getForm().getProperties().getName()), (block == null ? "null" : block.getProperties().getName()));

        EJManagedFrameworkConnection connection = block.getForm().getFrameworkConnection();
        try
        {
            if (block != null && block.getProperties().isUsedInLovDefinition())
            {
                String lovName = block.getProperties().getLovDefinition().getName();
                if (_lovActionProcessors.containsKey(lovName))
                {
                    if (traceEnabled)
                        logger.trace("Calling lov action processor: Lov: {}", lovName);
                    _lovActionProcessors.get(lovName).preOpenQueryScreen(_lovs.get(lovName));
                    if (traceEnabled)
                        logger.trace("Called lov action processor");
                }
            }
            else
            {
                throw new EJApplicationException(new EJMessage("The block passed to preOpenLovQueryScreen does not belong to a LOV"));
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END preOpenLovQueryScreen");
    }

    public void whenInsertCancelled(EJBlock block)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START whenInsertCancelled. Form: {} Block: {}", block.getForm().getName(), block.getName());
        EJManagedFrameworkConnection connection = block.getFrameworkManager().getConnection();
        try
        {
            String blockName = block.getName();
            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                if (traceEnabled)
                    logger.trace("Calling block level whenInsertCancelled");
                _blockLevelActionProcessors.get(blockName).whenInsertCancelled(block);
                if (traceEnabled)
                    logger.trace("Called block level whenInsertCancelled");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level whenInsertCancelled");
                _formLevelActionProcessor.whenInsertCancelled(block);
                if (traceEnabled)
                    logger.trace("Called form level whenInsertCancelled");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END whenInsertCancelled");
    }

    public void whenUpdateCancelled(EJBlock block)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START whenUpdateCancelled");

        EJManagedFrameworkConnection connection = block.getFrameworkManager().getConnection();
        try
        {
            String blockName = block.getName();
            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                if (traceEnabled)
                    logger.trace("Calling block level whenUpdateCancelled. Block: {}", blockName);
                _blockLevelActionProcessors.get(blockName).whenUpdateCancelled(block);
                if (traceEnabled)
                    logger.trace("Called block level whenUpdateCancelled");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level whenUpdateCancelled");
                _formLevelActionProcessor.whenUpdateCancelled(block);
                if (traceEnabled)
                    logger.trace("Called form level whenUpdateCancelled");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END whenUpdateCancelled");
    }

    public void preOpenScreen(EJBlock block, EJRecord record, EJScreenType screenType)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START preOpenScreen. Form:{}, Block: {}, ScreenType: {}", block.getForm().getName(), block.getName(), screenType);

        EJManagedFrameworkConnection connection = block.getFrameworkManager().getConnection();
        try
        {
            String blockName = block.getName();
            if (_blockLevelActionProcessors.containsKey(blockName))
            {
                if (traceEnabled)
                    logger.trace("Calling block level preOpenScreen");
                _blockLevelActionProcessors.get(blockName).preOpenScreen(block, record, screenType);
                if (traceEnabled)
                    logger.trace("Called block level preOpenScreen");
            }
            else
            {
                if (traceEnabled)
                    logger.trace("Calling form level preOpenScreen");
                _formLevelActionProcessor.preOpenScreen(block, record, screenType);
                if (traceEnabled)
                    logger.trace("Called form level preOpenScreen");
            }
        }
        catch (Exception e)
        {
            if (connection != null)
            {
                connection.rollback();
            }
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }

        if (traceEnabled)
            logger.trace("END preOpenScreen");
    }

    public void initialiseRecord(EJForm form, EJRecord record, EJRecordType recordType)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled)
            logger.trace("START initialiseRecord. Form: {}, RecordType: {}", form.getName(), recordType);

        EJManagedFrameworkConnection connection = form.getConnection();
        try
        {
            String blockName = record == null ? "" : record.getBlockName();
            if (record != null && record.isUsedInLovDefinition())
            {
                String lovName = record.getLovDefinitionName();
                if (_lovActionProcessors.containsKey(lovName))
                {
                    if (traceEnabled)
                        logger.trace("Calling LOV level initialiseQueryRecord. Lov: {}", lovName);
                    _lovActionProcessors.get(lovName).initialiseQueryRecord(_lovs.get(lovName), record);
                    if (traceEnabled)
                        logger.trace("Called LOV level nitialiseQueryRecord");
                }
            }
            else
            {
                if (_blockLevelActionProcessors.containsKey(blockName))
                {
                    if (traceEnabled)
                        logger.trace("Calling block level initialiseRecord. Block: {}", blockName);
                    _blockLevelActionProcessors.get(blockName).initialiseRecord(form, record, recordType);
                    if (traceEnabled)
                        logger.trace("Called block level initialiseRecord");
                }
                else
                {
                    if (traceEnabled)
                        logger.trace("Calling form level initialiseRecord");
                    _formLevelActionProcessor.initialiseRecord(form, record, recordType);
                    if (traceEnabled)
                        logger.trace("Called form level initialiseRecord");
                }
            }
        }
        catch (Exception e)
        {
            connection.rollback();
            throw new EJApplicationException(e);
        }
        finally
        {
            connection.close();
        }
        if (traceEnabled)
            logger.trace("END initialiseRecord");
    }

    public EJDataRecord preChange(EJBlockController blockController, EJScreenType screenType)
    {
        EJDataRecord registeredRecord = null;
        switch (screenType)
        {
            case MAIN:

                registeredRecord = blockController.getFocusedRecord();
                break;
            case INSERT:
                registeredRecord = blockController.getManagedInsertScreenRenderer().getInsertRecord();
                break;
            case UPDATE:
                registeredRecord = blockController.getManagedUpdateScreenRenderer().getUpdateRecord();
                break;
            case QUERY:
                registeredRecord = blockController.getManagedQueryScreenRenderer().getQueryRecord();
                break;
        }

        EJDataRecord recordToUpdate = blockController.createRecordNoAction();
        if (registeredRecord != null)
        {
            registeredRecord.copyValuesToRecord(recordToUpdate);
        }
        recordToUpdate.setBaseRecord(registeredRecord);
        return recordToUpdate;
    }

    public void postChange(EJBlockController blockController, EJDataRecord record, EJScreenType screenType)
    {

        boolean traceEnabled = logger.isTraceEnabled();
        if (record.isChanged())
        {
            if (traceEnabled)
                logger.trace("   -> record changed in action controller, making changes");

            EJDataRecord baseRecord = record.getBaseRecord();
            // The developer made some changes to the record so copy them to
            // the registered record and synchronize the changes with the
            // register

            if (baseRecord == null)
            {
                baseRecord = record;
            }
            else
            {
                record.copyValuesToRecord(baseRecord);
                record.copyItemVAsToRecord(baseRecord);
            }

            // Now get the screen renderer to copy any modified
            // values back to the record and any displayed fields
            switch (screenType)
            {
                case MAIN:
                    blockController.getRendererController().refreshAfterChange(baseRecord);
                    EJMirrorBlockSynchronizer blockSynchronizer = blockController.getMirrorBlockSynchronizer();
                    if (blockSynchronizer != null)
                    {
                        blockSynchronizer.refreshAfterChange(blockController, baseRecord);
                    }
                    break;
                case INSERT:
                    blockController.getManagedInsertScreenRenderer().refreshAfterChange(baseRecord);
                    break;
                case UPDATE:
                    blockController.getManagedUpdateScreenRenderer().refreshAfterChange(baseRecord);
                    break;
                case QUERY:
                    blockController.getManagedQueryScreenRenderer().refreshAfterChange(baseRecord);
                    break;
            }
        }
        else
        {
            EJDataRecord baseRecord = record.getBaseRecord();
            if (baseRecord != null)
            {
                record.copyItemVAsToRecord(baseRecord);

            }

            if (traceEnabled)
                logger.trace("   -> record not changed in action controller, no changes to make");
        }
    }
}
