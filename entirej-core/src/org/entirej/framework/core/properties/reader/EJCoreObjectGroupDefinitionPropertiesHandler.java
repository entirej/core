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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.entirej.framework.core.enumerations.EJCanvasType;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreCanvasProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.EJCoreRelationProperties;
import org.entirej.framework.core.properties.containers.EJCoreBlockPropertiesContainer;
import org.entirej.framework.core.properties.containers.EJCoreCanvasPropertiesContainer;
import org.entirej.framework.core.properties.containers.EJCoreLovDefinitionPropertiesContainer;
import org.entirej.framework.core.properties.containers.EJCoreRelationPropertiesContainer;
import org.entirej.framework.core.properties.containers.interfaces.EJCanvasPropertiesContainer;
import org.entirej.framework.core.properties.factory.EJCoreFormPropertiesFactory;
import org.entirej.framework.core.properties.interfaces.EJCanvasProperties;
import org.entirej.framework.core.properties.interfaces.EJStackedPageProperties;
import org.entirej.framework.core.properties.interfaces.EJTabPageProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreObjectGroupDefinitionPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCorePropertiesHandlerFactory _handlerFactory;
    private EJCoreFormProperties           _formProperties;

    private static final String            ELEMENT_OBJGROUP_DEF = "objGroupDefinition";

    public EJCoreObjectGroupDefinitionPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreFormProperties formProperties)
    {
        _handlerFactory = handlerFactory;
        _formProperties = formProperties;
    }

   

    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {

        if (name.equals(ELEMENT_OBJGROUP_DEF))
        {

            String defName = attributes.getValue("name");

            EJCoreFormPropertiesFactory factory = new EJCoreFormPropertiesFactory(_handlerFactory.getFrameworkManager());
            try
            {
                EJCoreFormProperties objectGroupProperties = factory.createObjectGroupProperties( defName);
                //import elements
                EJCoreBlockPropertiesContainer blockContainer = objectGroupProperties.getBlockContainer();
                Collection<EJCoreBlockProperties> allBlockProperties = blockContainer.getAllBlockProperties();
                //import all blocks to form
                for (EJCoreBlockProperties block : allBlockProperties)
                {
                    
                    _formProperties.getBlockContainer().addBlockProperties(block);
                }
                
                EJCoreRelationPropertiesContainer relationContainer = objectGroupProperties.getRelationContainer();
                Collection<EJCoreRelationProperties> relationProperties = relationContainer.getAllRelationProperties();
                //import all relations
                for (EJCoreRelationProperties relation : relationProperties)
                {
                    _formProperties.getRelationContainer().addRelationProperties(relation);
                }
                
                
                EJCoreCanvasPropertiesContainer formContainer = _formProperties.getCanvasContainer();
                
                EJCoreCanvasPropertiesContainer canvasContainer = objectGroupProperties.getCanvasContainer();
                Collection<EJCanvasProperties> allCanvasProperties = canvasContainer.getAllCanvasProperties();
                for (EJCanvasProperties canvas : allCanvasProperties)
                {
                    
                    if(canvas.getType()==EJCanvasType.POPUP)
                    {
                        _formProperties.getCanvasContainer().addCanvasProperties((EJCoreCanvasProperties)canvas);
                        
                    }
                    else
                    {
                        EJCoreCanvasProperties canvasProperties = (EJCoreCanvasProperties) getCanvasProperties(_formProperties, canvas.getName());
                        if(canvasProperties == null)
                        {
                            formContainer.addCanvasProperties((EJCoreCanvasProperties)canvas);
                        }
                        else
                        {
                            EJCoreCanvasProperties coreCanvasProperties = (EJCoreCanvasProperties) canvas;
                            coreCanvasProperties.setWidth(canvasProperties.getWidth());
                            coreCanvasProperties.setHeight(canvasProperties.getHeight());
                            coreCanvasProperties.setExpandHorizontally(canvasProperties.canExpandHorizontally());
                            coreCanvasProperties.setExpandVertically(canvasProperties.canExpandVertically());
                            coreCanvasProperties.setVerticalSpan(canvasProperties.getVerticalSpan());
                            coreCanvasProperties.setHorizontalSpan(canvasProperties.getHorizontalSpan());
                            canvasProperties.getParentCanvasContainer().replaceCanvasProperties(canvasProperties, coreCanvasProperties);
                        }
                    }
                }
                
               
                
                
                EJCoreLovDefinitionPropertiesContainer lovDefinitionContainer = objectGroupProperties.getLovDefinitionContainer();
                Collection<EJCoreLovDefinitionProperties> allLovDefinitionProperties = lovDefinitionContainer.getAllLovDefinitionProperties();
                for (EJCoreLovDefinitionProperties lov : allLovDefinitionProperties)
                {
                    
                    _formProperties.getLovDefinitionContainer().addLovDefinitionProperties(lov);
                }
            }
            catch (Exception e)
            {
                throw new SAXException(e);
            }
            
        }

    }
    
    
    private static EJCanvasProperties getCanvasProperties(EJCoreFormProperties formProperties, String name)
    {
        Collection<EJCanvasProperties> canvasList =  retriveAllCanvases(formProperties);
        
        for (EJCanvasProperties canvas : canvasList)
        {
            if (canvas.getName().equals(name))
            {
                return canvas;
            }
        }
        return null;
    }
    
    
    public static Collection<EJCanvasProperties> retriveAllCanvases(EJCoreFormProperties formProperties)
    {
        List<EJCanvasProperties> canvasList = new ArrayList<EJCanvasProperties>();
        
        addCanvasesFromContainer(formProperties, formProperties.getCanvasContainer(), canvasList);
        
        return canvasList;
    }
    
    private static void addCanvasesFromContainer(EJCoreFormProperties formProperties, EJCanvasPropertiesContainer container,
            List<EJCanvasProperties> canvasList)
    {
        Iterator<EJCanvasProperties> allCanvases = container.getAllCanvasProperties().iterator();
        while (allCanvases.hasNext())
        {
            EJCanvasProperties canvas = allCanvases.next();
            canvasList.add(canvas);
            if (canvas.getType() == EJCanvasType.POPUP)
            {
                addCanvasesFromContainer(formProperties, canvas.getPopupCanvasContainer(), canvasList);
            }
            else if (canvas.getType() == EJCanvasType.TAB)
            {
                Iterator<EJTabPageProperties> allTabPages = canvas.getTabPageContainer().getAllTabPageProperties().iterator();
                while (allTabPages.hasNext())
                {
                    addCanvasesFromContainer(formProperties, allTabPages.next().getContainedCanvases(), canvasList);
                }
            }
            else if (canvas.getType() == EJCanvasType.STACKED)
            {
                Iterator<EJStackedPageProperties> allStackedPages = canvas.getStackedPageContainer().getAllStackedPageProperties().iterator();
                while (allStackedPages.hasNext())
                {
                    addCanvasesFromContainer(formProperties, allStackedPages.next().getContainedCanvases(), canvasList);
                }
            }
            else if (canvas.getType() == EJCanvasType.GROUP)
            {
                addCanvasesFromContainer(formProperties, canvas.getGroupCanvasContainer(), canvasList);
            }
            else if (canvas.getType() == EJCanvasType.SPLIT)
            {
                addCanvasesFromContainer(formProperties, canvas.getSplitCanvasContainer(), canvasList);
            }
        }
    }
    

    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_OBJGROUP_DEF))
        {
            quitAsDelegate();
            return;
        }

    }

}
