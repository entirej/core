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
package org.entirej.framework.core.properties.reader;

import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreItemGroupProperties;
import org.entirej.framework.core.properties.containers.EJCoreItemGroupPropertiesContainer;

public class EJCoreInsertScreenItemGroupPropertiesHandler extends EJCoreItemGroupPropertiesHandler
{
    private EJCorePropertiesHandlerFactory          _handlerFactory;
    private EJCoreInsertScreenItemPropertiesHandler _itemHandler;
    private EJCoreBlockProperties                   _blockProperties;
    
    public EJCoreInsertScreenItemGroupPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreBlockProperties blockProperties,
            EJCoreItemGroupPropertiesContainer itemGroupContainer, String exitTag)
    {
        super(handlerFactory, blockProperties, itemGroupContainer, exitTag);
        _handlerFactory = handlerFactory;
        _blockProperties = blockProperties;
    }
    
    public void setDelegate()
    {
        _itemHandler = _handlerFactory.createInsertScreenItemHandler(_blockProperties);
        setDelegate(_itemHandler);
    }
    
    public void addItemPropertiesToGroup()
    {
        getItemGroupProperties().addItemProperties(_itemHandler.getItemProperties());
    }
    
    public EJCoreItemGroupPropertiesHandler createChildItemGroupHandler(EJCoreItemGroupProperties itemGroup)
    {
        return _handlerFactory.createInsertScreenItemGroupHandler(_blockProperties, itemGroup.getChildItemGroupContainer(), "itemGroupList");
    }
}
