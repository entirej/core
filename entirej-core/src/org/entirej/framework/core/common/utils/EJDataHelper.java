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
package org.entirej.framework.core.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJBlock;
import org.entirej.framework.core.EJBlockItem;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJRecord;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.core.data.controllers.EJFormParameter;
import org.entirej.framework.core.data.controllers.EJItemLovController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJDataHelper
{
    private static final Logger logger = LoggerFactory.getLogger(EJDataHelper.class);
            
    public static Object getDefaultInsertValue(EJForm form, EJBlockItem item)
    {
        return getDefaultValue(form, item, item.getDefaultInsertValue(), null);
    }
    
    public static Object getDefaultQueryValue(EJForm form, EJBlockItem item)
    {
        return getDefaultValue(form, item, item.getDefaultQueryValue(), null);
    }
    
    public static Object getDefaultQueryValue(EJForm form, EJBlockItem item, EJItemLovController itemLovController)
    {
        return getDefaultValue(form, item, item.getDefaultQueryValue(), itemLovController);
    }
    
    private static Object getDefaultValue(EJForm form, EJBlockItem item, String defaultValue, EJItemLovController itemLovController)
    {
        logger.trace("START getDefaultValue. Form: {}, Item: {}, defaultValue: {}", form.getProperties().getName(), item.getName(), defaultValue);
        
        if (defaultValue == null || defaultValue.trim().length() == 0)
        {
            return null;
        }
        
        String paramTypeCode = defaultValue.substring(0, defaultValue.indexOf(':'));
        String paramValue = defaultValue.substring(defaultValue.indexOf(':') + 1);
        
        logger.trace("Parameter Type: {} Value: {}", paramTypeCode, paramValue);
        
        if ("APP_PARAMETER".equals(paramTypeCode))
        {
            EJApplicationLevelParameter param = form.getApplicationLevelParameter(paramValue);
            logger.trace("Application Parameter Value: {}", param.getValue());
            return param.getValue();
        }
        else if ("FORM_PARAMETER".equals(paramTypeCode))
        {
            EJFormParameter param = form.getFormParameter(paramValue);
            logger.trace("Form Parameter Value: {}", param.getValue());
            return param.getValue();
        }
        else if ("BLOCK_ITEM".equals(paramTypeCode))
        {
            String blockName = paramValue.substring(0, paramValue.indexOf('.'));
            String itemName = paramValue.substring(paramValue.indexOf('.') + 1);
            
            logger.trace("Block Item");
            
            EJRecord record = null;
            // If the itemLovController is not null then it means that I am
            // retrieving the default value for a screen lov. Therefore I need
            // to retrieve the block_item value from the displayed screen if the
            // block displayed is the same as the block from which I should
            // retrieve the default value
            if (itemLovController != null)
            {
                logger.trace("Getting parameter for screen: {}", itemLovController.getItemToValidate().getScreenType());
                EJDataRecord dataRecord = null; 
                switch (itemLovController.getItemToValidate().getScreenType())
                {
                    case INSERT:
                        dataRecord =itemLovController.getItemToValidate().getBlock().getInsertScreenRenderer().getInsertRecord();
                        break;
                    case QUERY:
                        dataRecord = itemLovController.getItemToValidate().getBlock().getQueryScreenRenderer().getQueryRecord();
                        break;
                    case UPDATE:
                        dataRecord = itemLovController.getItemToValidate().getBlock().getUpdateScreenRenderer().getUpdateRecord();
                        break;
                    case MAIN:
                        dataRecord = itemLovController.getItemToValidate().getBlock().getFocusedRecord();
                        break;
                }
                if(dataRecord!=null)
                {
                    record = new EJRecord(dataRecord);
                }
            }
            else
            {
                logger.trace("Getting parameter for LOV: Block: "+blockName);
                
                EJBlock block = form.getBlock(blockName);
                if (block == null)
                {
                    throw new EJApplicationException(new EJMessage(form,
                            "Trying to retrieve a default value from a Block.Item value: " + blockName + "." + itemName
                                    + ", but there is not a block with the given name within this form: "+form.getProperties().getName()));
                }
                record = form.getBlock(blockName).getFocusedRecord();
            }
            
            if (record != null )
            {
                Object val = record.getValue(itemName);
                logger.trace("BlockItem value: {}", val);
                return val;
            }
            else
            {
                logger.trace("Could not find a record for the specified block");
                return null;
            }
        }
        else if ("CLASS_FIELD".equals(paramTypeCode))
        {
            logger.trace("Class Field Parameter");
            return getDefaultValueFromClassField(item, paramValue);
        }
        else
        {
            throw new EJApplicationException(new EJMessage("Trying to retrieve a default value for " + paramValue + " but an invalid type has been specified: "
                    + paramTypeCode));
        }
    }
    
    private static Object getDefaultValueFromClassField(EJBlockItem item, String paramValue)
    {
        
        if (paramValue == null || paramValue.trim().length() == 0)
        {
            return null;
        }
        
        if (!paramValue.contains("."))
        {
            throw new EJApplicationException(new EJMessage("Invalid class field name defined for field " + item.getBlockName() + "."
                    + item.getName() + ":" + paramValue));
        }
        
        String fieldName = paramValue.substring(paramValue.lastIndexOf('.') + 1);
        String fullClassName = paramValue.substring(0, paramValue.indexOf("." + fieldName));
        
        logger.trace("Getting value for item: {} in class {}", fieldName, fullClassName);
        
        try
        {
            Class<?> constantsClass = Class.forName(fullClassName);
            Field field = constantsClass.getDeclaredField(fieldName);
            
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()))
            {
                Object val = field.get(null);
                logger.trace("Got value {} ", val);
                return val;
            }
            else
            {
                throw new EJApplicationException(new EJMessage("Trying to retrieve a default value for " + paramValue
                        + " from a class field but the class field cannot be accessed: " + fullClassName));
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(new EJMessage("Trying to retrieve a default value for " + paramValue
                    + " from a class field but the class cannot be found: " + fullClassName));
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(new EJMessage("Trying to retrieve a default value for " + paramValue
                    + " from a class field but the class cannot be accessed: " + fullClassName));
        }
        catch (SecurityException e)
        {
            throw new EJApplicationException(new EJMessage("Trying to retrieve a default value for " + paramValue
                    + " from a class field but the class field cannot be accessed: " + fullClassName));
        }
        catch (NoSuchFieldException e)
        {
            throw new EJApplicationException(new EJMessage("Trying to retrieve a default value for " + paramValue
                    + " from a class field but field does not exist: " + fullClassName));
        }
    }
}
