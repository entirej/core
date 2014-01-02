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

import org.entirej.framework.core.enumerations.EJFontStyle;
import org.entirej.framework.core.enumerations.EJFontWeight;
import org.entirej.framework.core.properties.EJCoreVisualAttributeProperties;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class EJCoreVisualAttributePropertiesHandler extends EJCorePropertiesTagHandler
{
    private EJCoreVisualAttributeProperties _vaProperties;
    
    private static final String             VISUAL_ATTRIBUTE = "visualAttribute";
    private static final String             FONT_NAME        = "fontName";
    private static final String             FONT_SIZE        = "fontSize";
    private static final String             STYLE            = "style";
    private static final String             WEIGHT           = "weight";
    private static final String             FOREGROUND_COLOR = "foregroundColor";
    private static final String             BACKGROUND_COLOR = "backgroundColor";
    
    @Override
    public void startLocalElement(String name, Attributes attributes) throws SAXException
    {
        if (name.equals(VISUAL_ATTRIBUTE))
        {
            String vaName = attributes.getValue("name");
            _vaProperties = new EJCoreVisualAttributeProperties(vaName);
        }
    }
    
    public EJCoreVisualAttributeProperties getProperties()
    {
        return _vaProperties;
    }
    
    @Override
    public void endLocalElement(String name, String value, String untrimmedValue)
    {
        if (name.equals(VISUAL_ATTRIBUTE))
        {
            quitAsDelegate();
            return;
        }
        
        if (name.equals(FONT_NAME))
        {
            _vaProperties.setFontName(value);
        }
        else if (name.equals(FONT_SIZE))
        {
            if (value.length() > 0)
            {
                _vaProperties.setFontSize(Integer.parseInt(value));
            }
        }
        else if (name.equals(WEIGHT))
        {
            if (value.length() > 0)
            {
                _vaProperties.setFontWeight(EJFontWeight.valueOf(value));
            }
        }
        else if (name.equals(STYLE))
        {
            if (value.length() > 0)
            {
                _vaProperties.setFontStyle(EJFontStyle.valueOf(value));
            }
        }
        else if (name.equals(FOREGROUND_COLOR))
        {
            if (value.length() > 0)
            {
                _vaProperties.setForegroundRGB(value);
            }
        }
        else if (name.equals(BACKGROUND_COLOR))
        {
            if (value.length() > 0)
            {
                _vaProperties.setBackgroundRGB(value);
            }
        }
    }
}
