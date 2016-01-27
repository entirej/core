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

import java.util.Collection;

import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreItemProperties;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.factory.EJCoreFormPropertiesFactory;
import org.entirej.framework.core.properties.interfaces.EJItemProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreBlockPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCorePropertiesHandlerFactory _handlerFactory;
    private EJCoreBlockProperties          _blockProperties;
    private boolean                        _referenced;
    private EJCoreFormProperties           _formProperties;
    private EJCoreLovDefinitionProperties  _lovDefinitionProperties;

    protected static final String          ELEMENT_BLOCK                             = "block";
    private static final String            ELEMENT_OBJECTGROUP                       = "objectgroup";
    protected static final String          ELEMENT_MIRRORED_BLOCK                    = "isMirrored";
    protected static final String          ELEMENT_MIRROR_PARENT                     = "mirrorParent";
    protected static final String          ELEMENT_QUERY_ALLOWED                     = "queryAllowed";
    protected static final String          ELEMENT_INSERT_ALLOWED                    = "insertAllowed";
    protected static final String          ELEMENT_UPDATE_ALLOWED                    = "updateAllowed";
    protected static final String          ELEMENT_DELETE_ALLOWED                    = "deleteAllowed";
    protected static final String          ELEMENT_QUERY_ALL_ROWS                    = "queryAllRows";
    protected static final String          ELEMENT_ADD_DEFAULT_CONTROL_RECORD        = "addControlBlockDefaultRecord";
    protected static final String          ELEMENT_MAX_RESULTS                       = "maxResults";
    protected static final String          ELEMENT_PAGE_SIZE                         = "pageSize";
    protected static final String          ELEMENT_CANVAS                            = "canvasName";
    protected static final String          ELEMENT_RENDERER                          = "blockRendererName";
    protected static final String          ELEMENT_QUERY_SCREEN_RENDERER             = "queryScreenRendererName";
    protected static final String          ELEMENT_INSERT_SCREEN_RENDERER            = "insertScreenRendererName";
    protected static final String          ELEMENT_UPDATE_SCREEN_RENDERER            = "updateScreenRendererName";
    protected static final String          ELEMENT_SERVICE_CLASS_NAME                = "serviceClassName";

    protected static final String          ELEMENT_ACTION_PROCESSOR                  = "actionProcessorClassName";
    protected static final String          ELEMENT_PROPERTY                          = "property";

    protected static final String          ELEMENT_ITEM                              = "item";
    protected static final String          ELEMENT_LOV_MAPPING                       = "lovMapping";

    protected static final String          ELEMENT_MAIN_SCREEN_PROPERTIES            = "mainScreenProperties";
    protected static final String          ELEMENT_MAIN_SCREEN                       = "mainScreen";
    protected static final String          ELEMENT_QUERY_SCREEN                      = "queryScreen";
    protected static final String          ELEMENT_INSERT_SCREEN                     = "insertScreen";
    protected static final String          ELEMENT_UPDATE_SCREEN                     = "updateScreen";

    protected static final String          ELEMENT_RENDERER_PROPERTIES               = "blockRendererProperties";
    protected static final String          ELEMENT_INSERT_SCREEN_RENDERER_PROPERTIES = "insertScreenRendererProperties";
    protected static final String          ELEMENT_QUERY_SCREEN_RENDERER_PROPERTIES  = "queryScreenRendererProperties";
    protected static final String          ELEMENT_UPDATE_SCREEN_RENDERER_PROPERTIES = "updateScreenRendererProperties";

    public EJCoreBlockPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreFormProperties formProperties,
            EJCoreLovDefinitionProperties lovDefinitionProperties)
    {
        _handlerFactory = handlerFactory;
        _formProperties = formProperties;
        _lovDefinitionProperties = lovDefinitionProperties;
    }

    public EJCoreBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }

    public void setBlockProperties(EJCoreBlockProperties blokProperties)
    {
        _blockProperties = blokProperties;
    }

    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_ITEM))
        {
            setDelegate(_handlerFactory.createItemHandler(getBlockProperties()));
            return;
        }
        else if (name.equals(ELEMENT_LOV_MAPPING))
        {
            setDelegate(_handlerFactory.createLovMappingHandler(_formProperties, getBlockProperties()));
            return;
        }
        if (name.equals(ELEMENT_BLOCK))
        {
            String blockName = attributes.getValue("name");
            String referenced = attributes.getValue("referenced");
            String referencedBlockName = attributes.getValue("referencedBlockName");
            String isControlBlock = attributes.getValue("controlBlock");
            _referenced = Boolean.parseBoolean(referenced);
            if (_referenced)
            {
                String objectGroup = attributes.getValue(ELEMENT_OBJECTGROUP);
                if (objectGroup != null && objectGroup.length() > 0)
                {
                    // objectgroup block holder
                    setBlockProperties(new EJCoreBlockProperties(_handlerFactory.getFrameworkManager(), _formProperties, blockName, true, true));
                    _blockProperties.internalSetName(blockName);
                }
                else
                {
                    EJCoreFormPropertiesFactory formFactory = new EJCoreFormPropertiesFactory(_handlerFactory.getFrameworkManager());
                    setBlockProperties(formFactory.createReferencedBlockProperties(_formProperties, referencedBlockName));
                    getBlockProperties().setLovDefinitionProperties(_lovDefinitionProperties);
                    getBlockProperties().internalSetName(blockName);
                    getBlockProperties().internalSetReferenced(true);
                    
                    
                    //ref-block loves need to be updated to imported name of form or object-group for correct mapping 
                    Collection<EJCoreLovDefinitionProperties> allLovDefinitionProperties = _blockProperties.getFormProperties().getLovDefinitionContainer()
                            .getAllLovDefinitionProperties();
                    for (EJCoreLovDefinitionProperties lovDefProperties : allLovDefinitionProperties)
                    {
                        Collection<EJItemProperties> allItemProperties = lovDefProperties.getBlockProperties().getAllItemProperties();
                        for (EJItemProperties item : allItemProperties)
                        {
                            String queryValue = item.getDefaultQueryValue();

                            if (queryValue != null && queryValue.trim().length() > 0 && queryValue.indexOf(":") > 0)
                            {
                                if ("BLOCK_ITEM".equals(queryValue.substring(0, queryValue.indexOf(":"))))
                                {

                                    String value = queryValue.substring(queryValue.indexOf(":") + 1);
                                    String[] split = value.split("\\.");
                                    if (split.length == 2)
                                    {
                                        if (_blockProperties.getReferencedBlockName().equals(split[0]))
                                        {
                                            ((EJCoreItemProperties) item).setDefaultQueryValue(
                                                    queryValue.replaceAll(_blockProperties.getReferencedBlockName(), _blockProperties.getName()));
                                        }
                                    }

                                }
                            }
                        }
                    }
                }

            }
            else
            {
                setBlockProperties(new EJCoreBlockProperties(_handlerFactory.getFrameworkManager(), _formProperties, blockName,
                        Boolean.parseBoolean(isControlBlock == null ? "false" : isControlBlock), false));
                getBlockProperties().setLovDefinitionProperties(_lovDefinitionProperties);
            }
        }
        else if (name.equals(ELEMENT_MAIN_SCREEN_PROPERTIES))
        {
            setDelegate(_handlerFactory.createMainScreenHandler(getBlockProperties()));
        }
        if (!_referenced)
        {
            if (name.equals(ELEMENT_RENDERER_PROPERTIES))
            {
                // Now I am starting the selection of the renderer properties
                setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(_formProperties, getBlockProperties(), ELEMENT_RENDERER_PROPERTIES));
            }
            else if (name.equals(ELEMENT_QUERY_SCREEN_RENDERER_PROPERTIES))
            {
                setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(_formProperties, getBlockProperties(),
                        ELEMENT_QUERY_SCREEN_RENDERER_PROPERTIES));
            }
            else if (name.equals(ELEMENT_INSERT_SCREEN_RENDERER_PROPERTIES))
            {
                setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(_formProperties, getBlockProperties(),
                        ELEMENT_INSERT_SCREEN_RENDERER_PROPERTIES));
            }
            else if (name.equals(ELEMENT_UPDATE_SCREEN_RENDERER_PROPERTIES))
            {
                setDelegate(_handlerFactory.createFrameworkExtensionPropertiesHandler(_formProperties, getBlockProperties(),
                        ELEMENT_UPDATE_SCREEN_RENDERER_PROPERTIES));
            }

            else if (name.equals(ELEMENT_MAIN_SCREEN))
            {
                setDelegate(_handlerFactory.createMainScreenItemGroupHandler(getBlockProperties(),
                        getBlockProperties().getScreenItemGroupContainer(EJScreenType.MAIN), ELEMENT_MAIN_SCREEN));
            }
            else if (name.equals(ELEMENT_INSERT_SCREEN))
            {
                setDelegate(_handlerFactory.createInsertScreenItemGroupHandler(getBlockProperties(),
                        getBlockProperties().getScreenItemGroupContainer(EJScreenType.INSERT), ELEMENT_INSERT_SCREEN));
            }
            else if (name.equals(ELEMENT_UPDATE_SCREEN))
            {
                setDelegate(_handlerFactory.createUpdateScreenItemGroupHandler(getBlockProperties(),
                        getBlockProperties().getScreenItemGroupContainer(EJScreenType.UPDATE), ELEMENT_UPDATE_SCREEN));
            }
            else if (name.equals(ELEMENT_QUERY_SCREEN))
            {
                setDelegate(_handlerFactory.createQueryScreenItemGroupHandler(getBlockProperties(),
                        getBlockProperties().getScreenItemGroupContainer(EJScreenType.QUERY), ELEMENT_QUERY_SCREEN));
            }
        }

    }

    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_BLOCK))
        {
            quitAsDelegate();
            return;
        }

        if (name.equals(ELEMENT_MIRRORED_BLOCK))
        {
            if (value.length() > 0)
            {
                getBlockProperties().setIsMirroredBlock(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_MIRROR_PARENT))
        {
            getBlockProperties().setMirrorBlockName(value);
        }
        else if (name.equals(ELEMENT_INSERT_ALLOWED))
        {
            getBlockProperties().setInsertAllowed(Boolean.parseBoolean(value));
        }
        else if (name.equals(ELEMENT_QUERY_ALLOWED))
        {
            getBlockProperties().setQueryAllowed(Boolean.parseBoolean(value));
        }
        else if (name.equals(ELEMENT_UPDATE_ALLOWED))
        {
            getBlockProperties().setUpdateAllowed(Boolean.parseBoolean(value));
        }
        else if (name.equals(ELEMENT_DELETE_ALLOWED))
        {
            getBlockProperties().setDeleteAllowed(Boolean.parseBoolean(value));
        }
        else if (name.equals(ELEMENT_QUERY_ALL_ROWS))
        {
            if (value.length() > 0)
            {
                getBlockProperties().setQueryAllRows(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_ADD_DEFAULT_CONTROL_RECORD))
        {
            if (value.length() > 0)
            {
                getBlockProperties().setAddControlBlockDefaultRecord(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_MAX_RESULTS))
        {
            if (value.length() > 0)
            {
                getBlockProperties().setMaxResults(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_PAGE_SIZE))
        {
            if (value.length() > 0)
            {
                getBlockProperties().setPageSize(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_CANVAS))
        {
            getBlockProperties().setCanvasName(value);
        }
        else if (name.equals(ELEMENT_RENDERER))
        {
            getBlockProperties().setBlockRendererName(value);
        }
        else if (name.equals(ELEMENT_SERVICE_CLASS_NAME))
        {
            getBlockProperties().setServiceClassName(value);
        }
        else if (name.equals(ELEMENT_ACTION_PROCESSOR))
        {
            getBlockProperties().setActionProcessorClassName(value);
        }
    }

    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_ITEM))
        {
            EJCoreItemProperties itemProperties = ((EJCoreItemPropertiesHandler) currentDelegate).getItemProperties();
            if (itemProperties == null)
            {
                return;
            }

            // If the item name is null, then this item is for a screen item and
            // should be ignored
            if (itemProperties.getName() == null)
            {
                return;
            }
            if (_referenced)
            {
                EJCoreItemProperties refItemProps = getBlockProperties().getItemProperties(itemProperties.getName());
                if (refItemProps != null)
                {
                    if (itemProperties.getDefaultQueryValue() != null && itemProperties.getDefaultQueryValue().trim().length() > 0)
                    {
                        refItemProps.setDefaultQueryValue(itemProperties.getDefaultQueryValue());
                    }
                    if (itemProperties.getDefaultInsertValue() != null && itemProperties.getDefaultInsertValue().trim().length() > 0)
                    {
                        refItemProps.setDefaultInsertValue(itemProperties.getDefaultInsertValue());
                    }
                }
   
                

            }
            else
            {
                getBlockProperties().addItemProperties(itemProperties);
            }
            return;
        }
        else if (name.equals(ELEMENT_LOV_MAPPING))
        {
            getBlockProperties().addLovMapping(((EJCoreLovMappingPropertiesHandler) currentDelegate).getLovMappingProperties());
            return;
        }
        if (!_referenced)
        {
            if (name.equals(ELEMENT_RENDERER_PROPERTIES))
            {
                getBlockProperties().setBlockRendererProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
            }
            else if (name.equals(ELEMENT_QUERY_SCREEN_RENDERER_PROPERTIES))
            {
                getBlockProperties().setQueryScreenRendererProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
            }
            else if (name.equals(ELEMENT_INSERT_SCREEN_RENDERER_PROPERTIES))
            {
                getBlockProperties().setInsertScreenRendererProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
            }
            else if (name.equals(ELEMENT_UPDATE_SCREEN_RENDERER_PROPERTIES))
            {
                getBlockProperties().setUpdateScreenRendererProperties(((EJCoreFrameworkExtensionPropertiesHandler) currentDelegate).getMainPropertiesGroup());
            }
        }
    }
}
