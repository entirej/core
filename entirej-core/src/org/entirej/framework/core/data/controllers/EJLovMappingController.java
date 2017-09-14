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
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJLovBlock;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.data.EJDataRecord;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.internal.EJInternalBlock;
import org.entirej.framework.core.properties.EJCoreItemProperties;
import org.entirej.framework.core.properties.EJCoreLovItemMappingProperties;
import org.entirej.framework.core.properties.EJCoreLovMappingProperties;
import org.entirej.framework.core.properties.containers.EJCoreItemPropertiesContainer;
import org.entirej.framework.core.service.EJBlockService;
import org.entirej.framework.core.service.EJPostQueryCache;
import org.entirej.framework.core.service.EJQueryCriteria;
import org.entirej.framework.core.service.EJRestrictions;

public class EJLovMappingController implements Serializable
{
    private EJFrameworkManager         _frameworkManager;
    private EJInternalBlock            _block;
    private EJCoreLovMappingProperties _mappingProperties;
    private EJBlockService<?>          _dataService;
    
    public EJLovMappingController(EJFrameworkManager frameworkManager, EJInternalBlock block, EJCoreLovMappingProperties mappingProperties)
    {
        _frameworkManager = frameworkManager;
        _block = block;
        _mappingProperties = mappingProperties;
    }
    
    private void queryCompleted(EJDataRecord lovRecord, EJDataRecord blockRecord)
    {
        if (lovRecord != null && blockRecord != null)
        {
            Iterator<EJCoreLovItemMappingProperties> props = _mappingProperties.getAllItemMappingProperties().iterator();
            while (props.hasNext())
            {
                EJCoreLovItemMappingProperties mapProps = props.next();
                
                String blockItemName = mapProps.getBlockItemName();
                String lovDefItemName = mapProps.getLovDefinitionItemName();
                
                if (!lovRecord.containsItem(lovDefItemName)) continue;
                if (!blockRecord.containsItem(blockItemName)) continue;
               
                
                blockRecord.getItem(blockItemName).setValue(lovRecord.getValue(lovDefItemName));
            }
        }
    }
    
    void addLookupValuesForQueryRecord(EJFormController formController, EJDataRecord blockRecord, EJPostQueryCache cache)
    {
        // I only want to add values after a query if the developer has set the
        // corresponding property
        if (!_mappingProperties.executeAfterQuery())
        {
            return;
        }
        
        if (_mappingProperties.getLovDefinitionProperties() == null)
        {
            return;
        }
        
        if (_dataService == null)
        {
            _dataService = _mappingProperties.getLovDefinitionProperties().getBlockProperties().getBlockService();
        }
        
        if (_dataService == null)
        {
            _frameworkManager.handleMessage(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.CANNOT_PERFORM_QUERY_WITH_NO_SERVICE,
                    _mappingProperties.getLovDefinitionProperties().getBlockProperties().getName()));
            return;
        }
        
        EJCoreItemPropertiesContainer itemContainer = _mappingProperties.getLovDefinitionProperties().getBlockProperties().getItemPropertiesContainer();
        boolean itemValueSet = false;
        // Create a query record and add the item values from the block
        // record depending on the lov mapping properties
        
        EJLovController lovController = formController.getLovController(_mappingProperties.getLovDefinitionProperties().getName());
        EJQueryCriteria queryCriteria = new EJQueryCriteria(new EJLovBlock(lovController.getBlock()),_mappingProperties.includeDefaultQueryValues());
        
        // Because the lov will be called as part of the post query, I can
        // set some default values for the queryCriteria
        queryCriteria.setMaxResults(10);
        queryCriteria.setPageNumber(1);
        queryCriteria.setPageSize(10);
        queryCriteria.setQueryAllRows(true);
        
        
        for (EJCoreLovItemMappingProperties mapProps : _mappingProperties.getAllItemMappingProperties())
        {
            String blockItemName = mapProps.getBlockItemName();
            String lovDefItemName = mapProps.getLovDefinitionItemName();
            
            // Check that there is both a block item and a lov def item
            if (lovDefItemName == null || lovDefItemName.trim().length() == 0)
            {
                continue;
            }
            if (blockItemName == null || blockItemName.trim().length() == 0)
            {
                continue;
            }
            
            EJCoreItemProperties itemProps = _block.getProperties().getItemProperties(blockItemName);
            
            // I am only interested in entity items
            if (itemProps == null /*|| itemProps.isBlockServiceItem() == false*/)
            {
                continue;
            }
            
            if (itemContainer.contains(lovDefItemName) && blockRecord.getValue(blockItemName) != null)
            {
                itemValueSet = true;
                
                if (blockRecord.getValue(blockItemName) == null)
                {
                    queryCriteria.add(EJRestrictions.isNull(itemContainer.getItemProperties(lovDefItemName).getName()));
                }
                else
                {
                    queryCriteria.add(EJRestrictions.equals(itemContainer.getItemProperties(lovDefItemName).getName(), blockRecord.getValue(blockItemName)));
                }
            }
        }
        
        // Only execute a query if a query value has been set. If the query
        // is made without query values, then a global query would be made
        // and this is not always good for performance
        if (itemValueSet)
        {
            StringBuilder builder = new StringBuilder();
            builder.append(_dataService.getClass().getName()).append("|");
            builder.append(queryCriteria.getCacheKey());
            
            List<?> entities = null;
            if (cache.containsKey(builder.toString()))
            {
                entities = cache.getEntry(builder.toString());
                if (entities != null)
                {
                    for (Object entity : entities)
                    {
                        queryCompleted(new EJDataRecord(formController, lovController.getBlock(), entity, false), blockRecord);
                    }
                }
            }
            else
            {
                entities = _dataService.executeQuery(new EJForm(formController.getInternalForm()), queryCriteria);
               
                
                // Now the query record is constructed, make the query
                if (entities != null)
                {
                    cache.putEntry(builder.toString(), entities);
                    for (Object entity : entities)
                    {
                        queryCompleted(new EJDataRecord(formController, lovController.getBlock(), entity, false), blockRecord);
                    }
                }
            }
        }
    }
}
