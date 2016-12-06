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
package org.entirej.framework.core.properties.reader;

import org.entirej.framework.core.properties.EJCoreCanvasProperties;
import org.entirej.framework.core.properties.EJCoreDrawerPageProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreDrawerPagePropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCorePropertiesHandlerFactory _handlerFactory;
    private EJCoreCanvasProperties         _drawerCanvas;
    private EJCoreDrawerPageProperties     _drawerPageProperties;

    private static final String            ELEMENT_DRAWER_PAGE     = "drawerPage";
    private static final String            ELEMENT_PAGE_TITLE      = "pageTitle";
    private static final String            ELEMENT_FIRST_NAV_BLOCK = "firstNavigationalBlock";
    private static final String            ELEMENT_FIRST_NAV_ITEM  = "firstNavigationalItem";
    private static final String            ELEMENT_ENABLED         = "enabled";
    private static final String            ELEMENT_VISIBLE         = "visible";
    private static final String            ELEMENT_NUM_COLS        = "numCols";
    private static final String            ELEMENT_CANVAS          = "canvas";
    private static final String            ELEMENT_WIDTH           = "width";

    public EJCoreDrawerPagePropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory, EJCoreCanvasProperties tabCanvas)
    {
        _drawerCanvas = tabCanvas;
        _handlerFactory = handlerFactory;
    }

    public EJCoreDrawerPageProperties getDrawerPageProperties()
    {
        return _drawerPageProperties;
    }

    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_CANVAS))
        {
            setDelegate(_handlerFactory.createCanvasHandler());
            return;
        }
        else if (name.equals(ELEMENT_DRAWER_PAGE))
        {
            String tabName = attributes.getValue("name");
            _drawerPageProperties = new EJCoreDrawerPageProperties(tabName);
        }
    }

    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_DRAWER_PAGE))
        {
            quitAsDelegate();
            return;
        }

        else if (name.equals(ELEMENT_PAGE_TITLE))
        {
            _drawerPageProperties.setBasePageTitle(value);
        }
        else if (name.equals(ELEMENT_FIRST_NAV_BLOCK))
        {
            _drawerPageProperties.setFirstNavigationalBlock(value);
        }
        else if (name.equals(ELEMENT_FIRST_NAV_ITEM))
        {
            _drawerPageProperties.setFirstNavigationalItem(value);
        }
        else if (name.equals(ELEMENT_ENABLED))
        {
            if (value.length() > 0)
            {
                _drawerPageProperties.setEnabled(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_VISIBLE))
        {
            if (value.length() > 0)
            {
                _drawerPageProperties.setVisible(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_NUM_COLS))
        {
            if (value.length() > 0)
            {
                _drawerPageProperties.setNumCols(Integer.parseInt(value));
            }
            else
            {
                _drawerPageProperties.setNumCols(1);
            }
        }
        else if (name.equals(ELEMENT_WIDTH))
        {
            if (value.length() > 0)
            {
                _drawerPageProperties.setDrawerWidth(Integer.parseInt(value));
            }
            else
            {
                _drawerPageProperties.setDrawerWidth(200);
            }
        }

    }

    @Override
    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_CANVAS))
        {
            EJCoreCanvasProperties canvas = ((EJCoreCanvasPropertiesHandler) currentDelegate).getCanvasProperties();
            canvas.setContentCanvasName(_drawerCanvas.getName());
            canvas.setContentCanvasTabPageName(_drawerPageProperties.getName());
            _drawerPageProperties.addContainedCanvas(canvas);
        }
    }

}
