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

import org.entirej.framework.core.properties.EJCoreCanvasProperties;
import org.entirej.framework.core.properties.EJCoreStackedPageProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreStackedPagePropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCoreCanvasProperties         _stackedCanvas;
    private EJCoreStackedPageProperties    _stackedPageProperties;
    private EJCorePropertiesHandlerFactory _handlerFactory;
    
    private static final String            ELEMENT_STACKED_PAGE      = "stackedPage";
    private static final String            ELEMENT_BACK_VISUAL_ATTR  = "backgroundColourVisualAttribute";
    private static final String            ELEMENT_FRONT_VISUAL_ATTR = "foregroundColourVisualAttribute";
    private static final String            ELEMENT_FIRST_NAV_BLOCK   = "firstNavigationalBlock";
    private static final String            ELEMENT_FIRST_NAV_ITEM    = "firstNavigationalItem";
    private static final String            ELEMENT_NUM_COLS          = "numCols";
    private static final String            ELEMENT_CANVAS            = "canvas";
    
    public EJCoreStackedPagePropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreCanvasProperties stackedCanvas)
    {
        _stackedCanvas = stackedCanvas;
        _handlerFactory = handlerFactory;
    }
    
    public EJCoreStackedPageProperties getStackedPageProperties()
    {
        return _stackedPageProperties;
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_CANVAS))
        {
            setDelegate(_handlerFactory.createCanvasHandler());
            return;
        }
        else if (name.equals(ELEMENT_STACKED_PAGE))
        {
            String pageName = attributes.getValue("name");
            _stackedPageProperties = new EJCoreStackedPageProperties(pageName);
        }
    }
    
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_STACKED_PAGE))
        {
            quitAsDelegate();
            return;
        }
        else if (name.equals(ELEMENT_BACK_VISUAL_ATTR))
        {
            _stackedPageProperties.setBackgroundColourVisualAttributeName(value);
        }
        else if (name.equals(ELEMENT_FRONT_VISUAL_ATTR))
        {
            _stackedPageProperties.setForegroundColourVisualAttributeName(value);
        }
        else if (name.equals(ELEMENT_NUM_COLS))
        {
            if (value.length() > 0)
            {
                _stackedPageProperties.setNumCols(Integer.parseInt(value));
            }
            else
            {
                _stackedPageProperties.setNumCols(1);
            }
        }
        else if (name.equals(ELEMENT_FIRST_NAV_BLOCK))
        {
            _stackedPageProperties.setFirstNavigationalBlock(value);
        }
        else if (name.equals(ELEMENT_FIRST_NAV_ITEM))
        {
            _stackedPageProperties.setFirstNavigationalItem(value);
        }
    }
    
    @Override
    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_CANVAS))
        {
            EJCoreCanvasProperties canvas = ((EJCoreCanvasPropertiesHandler) currentDelegate).getCanvasProperties();
            canvas.setContentCanvasName(_stackedCanvas.getName());
            canvas.setContentCanvasStackedPageName(_stackedPageProperties.getName());
            _stackedPageProperties.addContainedCanvas(canvas);
        }
    }
}
