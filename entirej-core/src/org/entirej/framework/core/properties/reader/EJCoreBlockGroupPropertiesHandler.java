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

import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreCanvasProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.interfaces.EJCanvasProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreBlockGroupPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCorePropertiesHandlerFactory _handlerFactory;
    private EJCoreFormProperties           _formProperties;

    private static final String            ELEMENT_BLOCK_GROUP = "blockGroup";
    private static final String            ELEMENT_BLOCK = "block";

    public EJCoreBlockGroupPropertiesHandler(EJCorePropertiesHandlerFactory factory, EJCoreFormProperties formProperties)
    {
        _handlerFactory = factory;
        _formProperties = formProperties;
    }

    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_BLOCK))
        {
            setDelegate(_handlerFactory.createBlockHandler(_formProperties, null));
        }
    }

    @Override
    void endLocalElement(String name, String value, String untrimmedValue) throws SAXException
    {
        if (name.equals(ELEMENT_BLOCK_GROUP))
        {
            quitAsDelegate();
            return;
        }
        
    }

    
    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
       
        if (name.equals(ELEMENT_BLOCK))
        {
            EJCoreBlockProperties blockProperties = ((EJCoreBlockPropertiesHandler) currentDelegate).getBlockProperties();
            _formProperties.addBlockProperties(blockProperties);
            EJCanvasProperties canvasProperties = _formProperties.getCanvasProperties(blockProperties.getCanvasName());
            if (canvasProperties != null)
            {
                ((EJCoreCanvasProperties) canvasProperties).setBlockProperties(blockProperties);
            }
            blockProperties.getScreenItemGroupContainer(EJScreenType.MAIN);
            return;
        }
    }
}
