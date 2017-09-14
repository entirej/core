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
package org.entirej.framework.core.properties.reader;

import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreMainScreenProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreMainScreenPropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCoreBlockProperties      _blockProperties;
    private EJCoreMainScreenProperties _screenProperties;
    
    private static final String        ELEMENT_MAIN_SCREEN_PROPERTIES = "mainScreenProperties";
    private static final String        ELEMENT_DISPLAY_FRAME          = "displayFrame";
    private static final String        ELEMENT_FRAME_TITLE            = "frameTitle";
    private static final String        ELEMENT_NUM_COLS               = "numCols";
    private static final String        ELEMENT_HEIGHT                 = "height";
    private static final String        ELEMENT_WIDTH                  = "width";
    private static final String        ELEMENT_HORIZONTAL_SPAN        = "horizontalSpan";
    private static final String        ELEMENT_VERTICAL_SPAN          = "verticalSpan";
    private static final String        ELEMENT_EXPAND_HORIZONTALLY    = "expandHorizontally";
    private static final String        ELEMENT_EXPAND_VERTICALLY      = "expandVertically";
    
    public EJCoreMainScreenPropertiesHandler(EJCoreBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
        _screenProperties = new EJCoreMainScreenProperties(blockProperties);
        _blockProperties.setMainScreenProperties(_screenProperties);
    }
    
    public EJCoreMainScreenProperties getMainScreenProperties()
    {
        return _screenProperties;
    }
    
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
    }
    
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(ELEMENT_MAIN_SCREEN_PROPERTIES))
        {
            quitAsDelegate();
            return;
        }
        
        if (name.equals(ELEMENT_DISPLAY_FRAME))
        {
            if (value.length() > 0)
            {
                _screenProperties.setDisplayFrame(Boolean.parseBoolean(value));
            }
            else
            {
                _screenProperties.setDisplayFrame(true);
            }
        }
        else if (name.equals(ELEMENT_FRAME_TITLE))
        {
            _screenProperties.setFrameTitle(value);
        }
        else if (name.equals(ELEMENT_NUM_COLS))
        {
            if (value.length() > 0)
            {
                _screenProperties.setNumCols(Integer.parseInt(value));
            }
            else
            {
                _screenProperties.setNumCols(1);
            }
        }
        else if (name.equals(ELEMENT_HEIGHT))
        {
            if (value.length() > 0)
            {
                _screenProperties.setHeight(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_WIDTH))
        {
            if (value.length() > 0)
            {
                _screenProperties.setWidth(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_HORIZONTAL_SPAN))
        {
            if (value.length() > 0)
            {
                _screenProperties.setHorizontalSpan(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_VERTICAL_SPAN))
        {
            if (value.length() > 0)
            {
                _screenProperties.setVerticalSpan(Integer.parseInt(value));
            }
        }
        else if (name.equals(ELEMENT_EXPAND_HORIZONTALLY))
        {
            if (value.length() > 0)
            {
                _screenProperties.setExpandHorizontally(Boolean.parseBoolean(value));
            }
        }
        else if (name.equals(ELEMENT_EXPAND_VERTICALLY))
        {
            if (value.length() > 0)
            {
                _screenProperties.setExpandVertically(Boolean.parseBoolean(value));
            }
        }
        
    }
}
