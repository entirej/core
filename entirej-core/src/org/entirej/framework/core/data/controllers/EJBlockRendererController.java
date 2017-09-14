/*******************************************************************************
 * Copyright 2013 CRESOFT AG
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
 * Contributors: CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;

import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.enumerations.EJManagedScreenProperty;
import org.entirej.framework.core.renderers.interfaces.EJBlockRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJBlockRendererController implements Serializable
{
    final Logger              logger = LoggerFactory.getLogger(EJBlockRendererController.class);

    private EJBlockRenderer   _renderer;
    private EJBlockController _blockController;

    /**
     * This current record is only used if the block is not displayed
     */
    private EJDataRecord      _currentRecord;

    public EJBlockRendererController(EJBlockController controller)
    {
        _blockController = controller;
    }

    public void setRenderer(EJBlockRenderer renderer)
    {
        _renderer = renderer;
    }

    public EJBlockRenderer getRenderer()
    {
        return _renderer;
    }

    public Object getGuiComponent()
    {
        if (_renderer != null)
        {
            return _renderer.getGuiComponent();
        }
        else
        {
            return null;
        }
    }

    public void executingQuery()
    {
        if (_renderer != null)
        {
            boolean traceEnabled = logger.isTraceEnabled();
            if (traceEnabled)
                logger.trace("START executingQuery. Block: {}", _blockController.getProperties().getName());
            _renderer.executingQuery();
            if (traceEnabled)
                logger.trace("END executingQuery");
        }
    }

    public void queryExecuted()
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (_renderer != null)
        {
            if (traceEnabled)
                logger.trace("START queryExecuted. Block: {}", _blockController.getProperties().getName());
            _renderer.queryExecuted();
            if (traceEnabled)
                logger.trace("END queryExecuted");
        }
        else
        {
            if (traceEnabled)
                logger.trace("START queryExecuted. Block: {}. No renderer so setting local current record to first record of block", _blockController.getDataBlock().getName());
            _currentRecord = _blockController.getDataBlock().getRecord(0);
        }
    }

    public void refreshItemProperty(String itemName, EJManagedScreenProperty managedItemPropertyType, EJDataRecord record)
    {
        if (_renderer != null)
        {
            boolean traceEnabled = logger.isTraceEnabled();
            if (traceEnabled)
                logger.trace("START refreshItemProperty. Block: {}, ItemName: {}, ItemPropertyType: {}", _blockController.getProperties().getName(), itemName, managedItemPropertyType);
            _renderer.refreshItemProperty(itemName, managedItemPropertyType, record);
            if (traceEnabled)
                logger.trace("END refreshItemProperty");
        }
    }

    public void refreshItemRendererProperty(String itemName, String propertyName)
    {
        if (_renderer != null)
        {
            boolean traceEnabled = logger.isTraceEnabled();
            if (traceEnabled)
                logger.trace("START refreshItemRendererProperty. Block: {}, Item: {}, Property: {}", _blockController.getProperties().getName(), itemName, propertyName);
            _renderer.refreshItemRendererProperty(itemName, propertyName);
            if (traceEnabled)
                logger.trace("END refreshItemRendererProperty");
        }
    }

    public void synchronize()
    {
        if (_renderer != null)
        {
            boolean traceEnabled = logger.isTraceEnabled();
            if (traceEnabled)
                logger.trace("START synyhronize. Block: {}", _blockController.getProperties().getName());
            _renderer.synchronize();
            if (traceEnabled)
                logger.trace("END synchronize");
        }
    }

    public void refreshAfterChange(EJDataRecord record)
    {
        if (_renderer != null)
        {
            boolean traceEnabled = logger.isTraceEnabled();
            if (traceEnabled)
                logger.trace("START refreshAfterChange. Block: {}", _blockController.getProperties().getName());
            _renderer.refreshAfterChange(record);
            if (traceEnabled)
                logger.trace("END refreshAfterChange");
        }
    }

    public void recordSelected(EJDataRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (_renderer != null)
        {
            if (traceEnabled)
                logger.trace("START recordSelected. Block: {}", _blockController.getProperties().getName());
            _renderer.recordSelected(record);
            if (traceEnabled)
                logger.trace("END recordSelected");
        }
        else
        {
            if (traceEnabled)
                logger.trace("START recordSelected. Block: {}. No renderer, therfore returning contained record", _blockController.getProperties().getName());
            _currentRecord = record;
        }
    }

    public EJDataRecord getFocusedRecord()
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (_renderer != null)
        {
            if (traceEnabled)
                logger.trace("START getFocusedRecord. Block: {}", _blockController.getProperties().getName());
            return _renderer.getFocusedRecord();
        }
        else
        {
            if (traceEnabled)
                logger.trace("START getFocusedRecord. Block: {}. No renderer so return local current record", _blockController.getProperties().getName());
            return _currentRecord;
        }
    }

    public int getDisplayedRecordNumber(EJDataRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (_renderer != null)
        {
            if (traceEnabled)
                logger.trace("START getDisplayedRecordNumber. Block: {}", _blockController.getProperties().getName());
            return _renderer.getDisplayedRecordNumber(record);
        }
        else
        {
            if (traceEnabled)
                logger.trace("START getDisplayedRecordNumber. Block: {} - no renderer so returning 0", _blockController.getProperties().getName());
            return 0;
        }
    }

    public int getBlockRecordCount()
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (_renderer != null)
        {
            if (traceEnabled)
                logger.trace("START getBlockRecordCount. Block: {}", _blockController.getProperties().getName());
            return _renderer.getDisplayedRecordCount();
        }
        else
        {
            if (traceEnabled)
                logger.trace("START getBlockRecordCount. Block{}. - no renderer so returning blocks record count.", _blockController.getProperties().getName());
            return _blockController.getBlock().getBlockRecordCount();
        }
    }

    public int getDisplayedRecordCount()
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (_renderer != null)
        {
            if (traceEnabled)
                logger.trace("START getDisplayedRecordCount. Block: {}", _blockController.getProperties().getName());
            return _renderer.getDisplayedRecordCount();
        }
        else
        {
            if (traceEnabled)
                logger.trace("START getDisplayedRecordCount. Block: {}. No renderer so returning 0", _blockController.getProperties().getName());
            return 0;
        }
    }

    public EJDataRecord getFirstRecord()
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (_renderer != null)
        {
            if (traceEnabled)
                logger.trace("START getFirstRecord. Block: {}", _blockController.getProperties().getName());
            return _renderer.getFirstRecord();
        }
        else
        {
            if (traceEnabled)
                logger.trace("START getFirstRecord. Block: {}. No renderer so going to record 0 on block: {}", _blockController.getProperties().getName());
            return _blockController.getDataBlock().getRecord(0);
        }
    }

    public EJDataRecord getLastRecord()
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (_renderer != null)
        {
            if (traceEnabled)
                logger.trace("START getLastRecord. Block: {}", _blockController.getProperties().getName());
            return _renderer.getLastRecord();
        }
        else
        {
            if (traceEnabled)
                logger.trace("START getLastrecord. Block: {}. No renderer so going to record count -1 on block {}", _blockController.getProperties().getName());
            return _blockController.getDataBlock().getRecord(_blockController.getDataBlock().getBlockRecordCount() - 1);
        }
    }

    public EJDataRecord getRecordAfter(EJDataRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (_renderer != null)
        {
            if (traceEnabled)
                logger.trace("START getRecordAfter. Block: {}", _blockController.getProperties().getName());
            return _renderer.getRecordAfter(record);
        }
        else
        {
            if (traceEnabled)
                logger.trace("START getRecordAfter. Block: {}", _blockController.getProperties().getName());
            return _blockController.getDataBlock().getRecordAfter(record);
        }
    }

    public EJDataRecord getRecordBefore(EJDataRecord record)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (_renderer != null)
        {
            if (traceEnabled)
                logger.trace("START getRecordBefore. Block: {}", _blockController.getProperties().getName());
            return _renderer.getRecordBefore(record);
        }
        else
        {
            if (traceEnabled)
                logger.trace("START getRecordBefore. Block: {}", _blockController.getProperties().getName());
            return _blockController.getDataBlock().getRecordBefore(record);
        }
    }

    public EJDataRecord getRecordAt(int displayedRecordNumber)
    {
        boolean traceEnabled = logger.isTraceEnabled();
        if (_renderer != null)
        {
            if (traceEnabled)
                logger.trace("START getRecordAt. Block: {}, RecordNumber: {}", _blockController.getProperties().getName(), displayedRecordNumber);
            return _renderer.getRecordAt(displayedRecordNumber);
        }
        else
        {
            if (traceEnabled)
                logger.trace("START getRecordAt. Block: {}, RecordNumber: {}", _blockController.getProperties().getName(), displayedRecordNumber);
            return _blockController.getDataBlock().getRecord(displayedRecordNumber);
        }
    }

    public void enterQuery(EJDataRecord queryRecord)
    {
        if (_renderer != null)
        {
            boolean traceEnabled = logger.isTraceEnabled();
            if (traceEnabled)
                logger.trace("START enterQuery. Block: {}", _blockController.getProperties().getName());
            _renderer.enterQuery(queryRecord);
            if (traceEnabled)
                logger.trace("END enterQuery");
        }
    }
}
