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

import java.io.Serializable;

import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreCanvasProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.containers.EJCoreItemGroupPropertiesContainer;

public class EJCorePropertiesHandlerFactory implements Serializable
{
    private EJFrameworkManager _frameworManager;
    
    public EJCorePropertiesHandlerFactory(EJFrameworkManager frameworkManager)
    {
        _frameworManager = frameworkManager;
    }
    
    public EJFrameworkManager getFrameworkManager()
    {
        return _frameworManager;
    }
    
    public EJCoreEntireJPropertiesHandler createEntireJPropertiesHandler()
    {
        return new EJCoreEntireJPropertiesHandler(this);
    }
    
    public EJCoreFormPropertiesHandler createFormHandler(String formName, boolean isCreatingLovDefinition, boolean isCreatingReferecedBlock)
    {
        return new EJCoreFormPropertiesHandler(this, formName, isCreatingLovDefinition, isCreatingReferecedBlock);
    }
    
    public EJCoreBlockPropertiesHandler createBlockHandler(EJCoreFormProperties formProperties, EJCoreLovDefinitionProperties lovDefinitionProperties)
    {
        return new EJCoreBlockPropertiesHandler(this, formProperties, lovDefinitionProperties);
    }
    
    public EJCoreCanvasPropertiesHandler createCanvasHandler()
    {
        return new EJCoreCanvasPropertiesHandler(this);
    }
    
    public EJCoreFrameworkExtensionPropertiesHandler createFrameworkExtensionPropertiesHandler(EJCoreFormProperties formProperties,
            EJCoreBlockProperties blockProperties, String exitTag)
    {
        return new EJCoreFrameworkExtensionPropertiesHandler(this, formProperties, blockProperties, exitTag);
    }
    
    public EJCoreFrameworkExtensionPropertiesListHandler createFrameworkExtensionPropertiesListHandler()
    {
        return new EJCoreFrameworkExtensionPropertiesListHandler();
    }
    
    public EJCoreInsertScreenItemGroupPropertiesHandler createInsertScreenItemGroupHandler(EJCoreBlockProperties blockProperties,
            EJCoreItemGroupPropertiesContainer itemGroupContainer, String exitTag)
    {
        return new EJCoreInsertScreenItemGroupPropertiesHandler(this, blockProperties, itemGroupContainer, exitTag);
    }
    
    public EJCoreInsertScreenItemPropertiesHandler createInsertScreenItemHandler(EJCoreBlockProperties blockProperties)
    {
        return new EJCoreInsertScreenItemPropertiesHandler(this, blockProperties);
    }
    
    public EJCoreItemPropertiesHandler createItemHandler(EJCoreBlockProperties blockProperties)
    {
        return new EJCoreItemPropertiesHandler(this, blockProperties);
    }
    
    public EJCoreLovDefinitionPropertiesHandler createLovDefinitionHandler(EJCoreFormProperties formProperties, boolean isCreatingReferecedBlock)
    {
        return new EJCoreLovDefinitionPropertiesHandler(this, formProperties, isCreatingReferecedBlock);
    }
    
    public EJCoreObjectGroupDefinitionPropertiesHandler createObjectGroupDefinitionHandler(EJCoreFormProperties formProperties)
    {
        return new EJCoreObjectGroupDefinitionPropertiesHandler(this, formProperties);
    }
    
    public EJCoreLovMappingPropertiesHandler createLovMappingHandler(EJCoreFormProperties formProperties, EJCoreBlockProperties blockProperties)
    {
        return new EJCoreLovMappingPropertiesHandler(this, formProperties, blockProperties);
    }
    
    public EJCoreMainScreenPropertiesHandler createMainScreenHandler(EJCoreBlockProperties blockProperties)
    {
        return new EJCoreMainScreenPropertiesHandler(blockProperties);
    }
    
    public EJCoreMainScreenItemGroupPropertiesHandler createMainScreenItemGroupHandler(EJCoreBlockProperties blockProperties,
            EJCoreItemGroupPropertiesContainer itemGroupContainer, String exitTag)
    {
        return new EJCoreMainScreenItemGroupPropertiesHandler(this, blockProperties, itemGroupContainer, exitTag);
    }
    
    public EJCoreMainScreenItemPropertiesHandler createMainScreenItemHandler(EJCoreBlockProperties blockProperties)
    {
        return new EJCoreMainScreenItemPropertiesHandler(this, blockProperties);
    }
    
    public EJCoreQueryScreenItemGroupPropertiesHandler createQueryScreenItemGroupHandler(EJCoreBlockProperties blockProperties,
            EJCoreItemGroupPropertiesContainer itemGroupContainer, String exitTag)
    {
        return new EJCoreQueryScreenItemGroupPropertiesHandler(this, blockProperties, itemGroupContainer, exitTag);
    }
    
    public EJCoreQueryScreenItemPropertiesHandler createQueryScreenItemHandler(EJCoreBlockProperties blockProperties)
    {
        return new EJCoreQueryScreenItemPropertiesHandler(this, blockProperties);
    }
    
    public EJCoreRelationPropertiesHandler createRelationHandler(EJCoreFormProperties formProperties)
    {
        return new EJCoreRelationPropertiesHandler(formProperties);
    }
    
    public EJCoreStackedPagePropertiesHandler createStackedPageHandler(EJCoreCanvasProperties stackedCanvas)
    {
        return new EJCoreStackedPagePropertiesHandler(this, stackedCanvas);
    }
    
    public EJCoreTabPagePropertiesHandler createTabPageHandler(EJCoreCanvasProperties tabCanvas)
    {
        return new EJCoreTabPagePropertiesHandler(this, tabCanvas);
    }
    
    public EJCoreUpdateScreenItemGroupPropertiesHandler createUpdateScreenItemGroupHandler(EJCoreBlockProperties blockProperties,
            EJCoreItemGroupPropertiesContainer itemGroupContainer, String exitTag)
    {
        return new EJCoreUpdateScreenItemGroupPropertiesHandler(this, blockProperties, itemGroupContainer, exitTag);
    }
    
    public EJCoreUpdateScreenItemPropertiesHandler createUpdateScreenItemHandler(EJCoreBlockProperties blockProperties)
    {
        return new EJCoreUpdateScreenItemPropertiesHandler(this, blockProperties);
    }
    
    public EJCoreVisualAttributePropertiesHandler createVisualAttributeHandler()
    {
        return new EJCoreVisualAttributePropertiesHandler();
    }
}
