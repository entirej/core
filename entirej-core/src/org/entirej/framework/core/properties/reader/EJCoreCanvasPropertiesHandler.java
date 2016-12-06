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

import org.entirej.framework.core.enumerations.EJCanvasDrawerPosition;
import org.entirej.framework.core.enumerations.EJCanvasMessagePosition;
import org.entirej.framework.core.enumerations.EJCanvasSplitOrientation;
import org.entirej.framework.core.enumerations.EJCanvasTabPosition;
import org.entirej.framework.core.enumerations.EJCanvasType;
import org.entirej.framework.core.enumerations.EJLineStyle;
import org.entirej.framework.core.enumerations.EJPopupButton;
import org.entirej.framework.core.properties.EJCoreCanvasProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreCanvasPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCoreCanvasProperties         _canvasProperties;
    private EJCorePropertiesHandlerFactory _handlerFactory;

    private static final String            ELEMENT_CANVAS                 = "canvas";
    private static final String            ELEMENT_WIDTH                  = "width";
    private static final String            ELEMENT_HEIGHT                 = "height";
    private static final String            ELEMENT_NUM_COLS               = "numCols";
    private static final String            ELEMENT_HORIZONTAL_SPAN        = "horizontalSpan";
    private static final String            ELEMENT_VERTICAL_SPAN          = "verticalSpan";
    private static final String            ELEMENT_EXPAND_HORIZONTALLY    = "expandHorizontally";
    private static final String            ELEMENT_EXPAND_VERTICALLY      = "expandVertically";
    private static final String            ELEMENT_DISPLAY_GROUP_FRAME    = "displayGroupFrame";
    private static final String            ELEMENT_CLOSEABLE_MESSAGE_PANE = "closeableMessagePane";
    private static final String            ELEMENT_MESSAGE_POSITION       = "messagePosition";
    private static final String            ELEMENT_MESSAGE_PANE_SIZE      = "messagePaneSize";
    private static final String            ELEMENT_FRAME_TITLE            = "groupFrameTitle";
    private static final String            ELEMENT_POPUP_PAGE_TITLE       = "popupPageTitle";
    private static final String            ELEMENT_TAB_POSITION           = "tabPosition";
    private static final String            ELEMENT_DRAWER_POSITION        = "drawerPosition";
    private static final String            ELEMENT_SPLIT_ORIENTATION      = "splitOrientation";
    private static final String            ELEMENT_LINE_STYLE             = "lineStyle";
    private static final String            ELEMENT_BUTTON_ONE_TEXT        = "buttonOneText";
    private static final String            ELEMENT_DEFAULT_BUTTON_ID      = "defaultButton";
    private static final String            ELEMENT_BUTTON_TWO_TEXT        = "buttonTwoText";
    private static final String            ELEMENT_BUTTON_THREE_TEXT      = "buttonThreeText";
    private static final String            ELEMENT_TAB_PAGE               = "tabPage";
    private static final String            ELEMENT_DRAWER_PAGE            = "drawerPage";
    private static final String            ELEMENT_STACKED_PAGE           = "stackedPage";
    private static final String            ELEMENT_INITIAL_STACKED_PAGE   = "initialStackedPageName";
    private static final String            ELEMENT_REFERRED_FORM_ID       = "referredFormId";

    private boolean                        _canvasCreated                 = false;

    public EJCoreCanvasPropertiesHandler(EJCorePropertiesHandlerFactory handlerFactory)
    {
        _handlerFactory = handlerFactory;
    }

    public EJCoreCanvasProperties getCanvasProperties()
    {
        return _canvasProperties;
    }

    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(ELEMENT_TAB_PAGE))
        {
            setDelegate(_handlerFactory.createTabPageHandler(_canvasProperties));
        }
        if (name.equals(ELEMENT_DRAWER_PAGE))
        {
            setDelegate(_handlerFactory.createDrawerPageHandler(_canvasProperties));
        }
        else if (name.equals(ELEMENT_STACKED_PAGE))
        {
            setDelegate(_handlerFactory.createStackedPageHandler(_canvasProperties));
        }
        else if (name.equals(ELEMENT_CANVAS))
        {
            if (!_canvasCreated)
            {
                String canvasName = attributes.getValue("name");
                String type = attributes.getValue("type");

                _canvasProperties = new EJCoreCanvasProperties(canvasName);
                _canvasProperties.setType(EJCanvasType.valueOf(type));

                _canvasCreated = true;
            }
            else
            {
                setDelegate(_handlerFactory.createCanvasHandler());
            }
        }
    }

    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_WIDTH))
        {
            if (value.length() > 0)
            {
                _canvasProperties.setWidth(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_HEIGHT))
        {
            if (value.length() > 0)
            {
                _canvasProperties.setHeight(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_REFERRED_FORM_ID))
        {
            if (value.length() > 0)
            {
                _canvasProperties.setReferredFormId(value);
            }
        }
        else if (name.equals(ELEMENT_NUM_COLS))
        {
            if (value.length() > 0)
            {
                _canvasProperties.setNumCols(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_HORIZONTAL_SPAN))
        {
            if (value.length() > 0)
            {
                _canvasProperties.setHorizontalSpan(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_VERTICAL_SPAN))
        {
            if (value.length() > 0)
            {
                _canvasProperties.setVerticalSpan(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_MESSAGE_PANE_SIZE))
        {
            if (value.length() > 0)
            {
                _canvasProperties.setMessagePaneSize(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_EXPAND_HORIZONTALLY))
        {
            if (value.length() > 0)
            {
                _canvasProperties.setExpandHorizontally(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_EXPAND_VERTICALLY))
        {
            if (value.length() > 0)
            {
                _canvasProperties.setExpandVertically(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_DISPLAY_GROUP_FRAME))
        {
            if (value.length() > 0)
            {
                _canvasProperties.setDisplayGroupFrame(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_CLOSEABLE_MESSAGE_PANE))
        {
            if (value != null && value.length() > 0)
            {
                _canvasProperties.setCloseableMessagePane(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_MESSAGE_POSITION))
        {
            if (value.length() > 0)
            {
                _canvasProperties.setMessagePosition(EJCanvasMessagePosition.valueOf(value));
            }
        }
        else if (name.equals(ELEMENT_POPUP_PAGE_TITLE))
        {
            _canvasProperties.setBasePopupPageTitle(value);
        }
        else if (name.equals(ELEMENT_FRAME_TITLE))
        {
            _canvasProperties.setBaseGroupFrameTitle(value);
        }

        else if (name.equals(ELEMENT_TAB_POSITION))
        {
            if (value != null && value.length() > 0)
            {
                _canvasProperties.setTabPosition(EJCanvasTabPosition.valueOf(value));
            }
        }
        else if (name.equals(ELEMENT_DRAWER_POSITION))
        {
            if (value != null && value.length() > 0)
            {
                _canvasProperties.setDrawerPosition(EJCanvasDrawerPosition.valueOf(value));
            }
        }
        else if (name.equals(ELEMENT_SPLIT_ORIENTATION))
        {
            if (value != null && value.length() > 0)
            {
                _canvasProperties.setSplitOrientation(EJCanvasSplitOrientation.valueOf(value));
            }
        }
        else if (name.equals(ELEMENT_LINE_STYLE))
        {
            if (value != null && value.length() > 0)
            {
                _canvasProperties.setLineStyle(EJLineStyle.valueOf(value));
            }
        }
        else if (name.equals(ELEMENT_INITIAL_STACKED_PAGE))
        {
            if (value != null && value.length() > 0)
            {
                _canvasProperties.setInitalStackedPageName(value);
            }
        }
        else if (name.equals(ELEMENT_BUTTON_ONE_TEXT))
        {
            _canvasProperties.setBaseButtonOneText(value);
        }
        else if (name.equals(ELEMENT_DEFAULT_BUTTON_ID))
        {
            _canvasProperties.setDefaultPopupButton(EJPopupButton.valueOf(value));
        }
        else if (name.equals(ELEMENT_BUTTON_TWO_TEXT))
        {
            _canvasProperties.setBaseButtonTwoText(value);
        }
        else if (name.equals(ELEMENT_BUTTON_THREE_TEXT))
        {
            _canvasProperties.setBaseButtonThreeText(value);
        }

        // If the element is CANVAS then, return to the parent handler
        if (name.equals(ELEMENT_CANVAS))
        {
            quitAsDelegate();
        }
    }

    public void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
        if (name.equals(ELEMENT_TAB_PAGE))
        {
            _canvasProperties.addTabPageProperties(((EJCoreTabPagePropertiesHandler) currentDelegate).getTabPageProperties());
            return;
        }
        if (name.equals(ELEMENT_DRAWER_PAGE))
        {
            _canvasProperties.addDrawerPageProperties(((EJCoreDrawerPagePropertiesHandler) currentDelegate).getDrawerPageProperties());
            return;
        }
        if (name.equals(ELEMENT_STACKED_PAGE))
        {
            _canvasProperties.addStackedPageProperties(((EJCoreStackedPagePropertiesHandler) currentDelegate).getStackedPageProperties());
            return;
        }
        if (name.equals(ELEMENT_CANVAS))
        {
            EJCoreCanvasProperties canvas = ((EJCoreCanvasPropertiesHandler) currentDelegate).getCanvasProperties();
            canvas.setContentCanvasName(_canvasProperties.getName());

            if (_canvasProperties.getType() == EJCanvasType.POPUP)
            {
                _canvasProperties.addPopupCanvasProperties(canvas);
                return;
            }
            if (_canvasProperties.getType() == EJCanvasType.GROUP)
            {
                _canvasProperties.addCanvasPropertiesToCanvasGroup(canvas);
                return;
            }
            if (_canvasProperties.getType() == EJCanvasType.SPLIT)
            {
                _canvasProperties.addCanvasPropertiesToCanvasSplit(canvas);
                return;
            }
        }
    }
}
