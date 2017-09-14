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
package org.entirej.framework.core.properties;

import java.awt.Color;
import java.io.Serializable;

import org.entirej.framework.core.enumerations.EJFontStyle;
import org.entirej.framework.core.enumerations.EJFontWeight;

public class EJCoreVisualAttributeProperties implements Comparable<EJCoreVisualAttributeProperties>, Serializable
{
    public static final String UNSPECIFIED    = "Unspecified";
    
    /**
     * The available font weights for use within the
     * {@link EJCoreVisualAttributeProperties}
     */
    
    private String             _name;
    private String             _foregroundRgb        = UNSPECIFIED;
    private String             _backgroundRgb        = UNSPECIFIED;

    private String             _fontName             = UNSPECIFIED;
    private int                _fontSize             = -1;
    private boolean            _fontSizeAsPercentage = false;
    private EJFontStyle        _fontStyle            = EJFontStyle.Unspecified;
    private EJFontWeight       _fontWeight           = EJFontWeight.Unspecified;
    
    public EJCoreVisualAttributeProperties()
    {
        _name = "VA_NO_NAME";
    }
    
    public EJCoreVisualAttributeProperties(String name)
    {
        _name = name;
    }
    
    public void setName(String name)
    {
        _name = name;
    }
    
    /**
     * Returns the name of this <code>VisualAttribute</code>
     * 
     * @return The name of this <code>VisualAttribute</code>
     */
    public String getName()
    {
        return _name;
    }
    
    /**
     * Indicates the weight of the font for this VisualAttribute
     * <p>
     * If {@link FontWeight#Unspecified} is specified (the default) then the
     * current, default font weight will be used
     * 
     * @param weight
     *            The weight to use
     * @see FontWeight
     */
    public void setFontWeight(EJFontWeight weight)
    {
        _fontWeight = weight;
    }
    
    /**
     * Returns the {@link FontWeight} set for this visual attribute
     * <p>
     * If {@link FontWeight#Unspecified} is specified (the default) then the
     * current, default font weight will be used
     * 
     * @return The {@link FontWeight} defined for this visual attribute
     */
    public EJFontWeight getFontWeight()
    {
        return _fontWeight;
    }
    
    /**
     * Indicates the style of the font for this VisualAttribute
     * <p>
     * If {@link FontStyle#Unspecified} is specified (the default) then the
     * current, default font style will be used
     * 
     * @param style
     *            The style to use
     * @see FontStyle
     */
    public void setFontStyle(EJFontStyle style)
    {
        _fontStyle = style;
    }
    
    /**
     * Returns the {@link FontStyle} set for this visual attribute
     * <p>
     * If {@link FontStyle#Unspecified} is specified (the default) then the
     * current, default font style will be used
     * 
     * @return The {@link FontStyle} defined for this visual attribute
     */
    public EJFontStyle getFontStyle()
    {
        return _fontStyle;
    }
    
    /**
     * The name of the font
     * 
     * @return the name of the font
     */
    public String getFontName()
    {
        return _fontName;
    }
    
    /**
     * Sets the name of the font
     * 
     * @param fontName
     *            the name of the font
     */
    public void setFontName(String fontName)
    {
        _fontName = fontName;
    }
    
    /**
     * Convenience method that is used to indicate if this visual attribute has
     * a font size set
     * <p>
     * If the font size is set to a value less than zero then this is classed as
     * having no font size set
     * 
     * @return <code>true</code> if the font size has been set to a value
     *         greater than zero, otherwise false
     */
    public boolean isFontSizeSet()
    {
        if (_fontSize < 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
    /**
     * The size of the text defined by this <code>VisualAttribute</code>
     * 
     * @return the fontSize
     */
    public int getFontSize()
    {
        return _fontSize;
    }
    
    /**
     * The size relative to default font size as percentage of the text defined by this <code>VisualAttribute</code>
     * 
     * @return the fontSize
     */
    public boolean isFontSizeAsPercentage()
    {
        return _fontSizeAsPercentage;
    }
    
    public void setFontSizeAsPercentage(boolean _fontSizeAsPercentage)
    {
        this._fontSizeAsPercentage = _fontSizeAsPercentage;
    }
    
    /**
     * Sets the size of the text defined by this <code>VisualAttribute</code>
     * 
     * @param fontSize
     *            the size of the font
     */
    public void setFontSize(int fontSize)
    {
        _fontSize = fontSize;
    }
    
    /**
     * Returns the color defined as the foreground color for this visual
     * attribute
     * 
     * @return The foreground color or <code>null</code> if no foreground color
     *         has been defined
     */
    public Color getForegroundColor()
    {
        if (_foregroundRgb == null || _foregroundRgb.trim().length() == 0)
        {
            return null;
        }
        
        if (UNSPECIFIED.equals(_foregroundRgb))
        {
            return null;
        }
        
        return getColor(_foregroundRgb);
    }
    
    /**
     * Sets the foreground color for this visual attribute
     * <p>
     * The foreground color is a string made up of the RGB values for the color.
     * The format is as follows:
     * <p>
     * r[int R value]g[int G value]b[int B value]
     * <p>
     * Example: r255g255b255 for White
     * 
     * @param foregroundColor
     *            The color to use for the foreground color of this visual
     *            attribute
     */
    public void setForegroundRGB(String foregroundColor)
    {
        if (foregroundColor == null || foregroundColor.trim().length() == 0)
        {
            _foregroundRgb = UNSPECIFIED;
        }
        
        _foregroundRgb = foregroundColor;
    }
    
    /**
     * Returns the foreground color for this visual attribute
     * <p>
     * The foreground color is a string made up of the RGB values for the color.
     * The format is as follows:
     * <p>
     * r[int R value]g[int G value]b[int B value]
     * <p>
     * Example: r255g255b255 for White
     * 
     * @return a String representing the defined color or
     *         {@link EJCoreVisualAttributeProperties#UNSPECIFIED}
     */
    public String getForegroundRGB()
    {
        return _foregroundRgb;
    }
    
    /**
     * Returns the color defined as the background color for this visual
     * attribute
     * 
     * @return The background color or <code>null</code> if no background color
     *         has been defined
     */
    public Color getBackgroundColor()
    {
        if (_backgroundRgb == null || _backgroundRgb.trim().length() == 0)
        {
            return null;
        }
        
        if (UNSPECIFIED.equals(_backgroundRgb))
        {
            return null;
        }
        
        return getColor(_backgroundRgb);
        
    }
    
    /**
     * Sets the background color for this visual attribute
     * <p>
     * The background color is a string made up of the RGB values for the color.
     * The format is as follows:
     * <p>
     * r[int R value]g[int G value]b[int B value]
     * <p>
     * Example: r255g255b255 for White
     * 
     * @param backgroundColor
     *            The color to use for the background color of this visual
     *            attribute
     */
    public void setBackgroundRGB(String backgroundColor)
    {
        if (backgroundColor == null || backgroundColor.trim().length() == 0)
        {
            _backgroundRgb = UNSPECIFIED;
        }
        
        _backgroundRgb = backgroundColor;
    }
    
    /**
     * Returns the background color for this visual attribute
     * <p>
     * The background color is a string made up of the RGB values for the color.
     * The format is as follows:
     * <p>
     * r[int R value]g[int G value]b[int B value]
     * <p>
     * Example: r255g255b255 for White
     * 
     * @return a String representing the defined color or
     *         {@link EJCoreVisualAttributeProperties#UNSPECIFIED}
     */
    public String getBackgroundRGB()
    {
        return _backgroundRgb;
    }
    
    private Color getColor(String colorString)
    {
        if (colorString == null)
        {
            throw new NullPointerException();
        }
        
        if (!colorString.contains("r") || !colorString.contains("g") || !colorString.contains("b"))
        {
            throw new IllegalArgumentException("The color is not in the correct format. Expected r<color>g<color>b<color>, received: " + colorString);
        }
        
        try
        {
            
            String R = colorString.substring(colorString.indexOf('r') + 1, colorString.indexOf('g'));
            String G = colorString.substring(colorString.indexOf('g') + 1, colorString.indexOf('b'));
            String B = colorString.substring(colorString.indexOf('b') + 1);
            
            Color color = new Color(Integer.parseInt(R), Integer.parseInt(G), Integer.parseInt(B));
            
            return color;
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
    
    public EJCoreVisualAttributeProperties makeCopy()
    {
        EJCoreVisualAttributeProperties props = new EJCoreVisualAttributeProperties();
        props.setName(_name);
        props.setForegroundRGB(_foregroundRgb);
        props.setBackgroundRGB(_backgroundRgb);
        props.setFontName(_fontName);
        props.setFontSize(_fontSize);
        props.setFontStyle(_fontStyle);
        props.setFontWeight(_fontWeight);
        props.setFontSizeAsPercentage(_fontSizeAsPercentage);
        
        return props;
    }
    
    public int compareTo(EJCoreVisualAttributeProperties o)
    {
        return ((EJCoreVisualAttributeProperties) o).getName().compareTo(this.getName());
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_name == null) ? 0 : _name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EJCoreVisualAttributeProperties other = (EJCoreVisualAttributeProperties) obj;
        if (_name == null)
        {
            if (other._name != null) return false;
        }
        else if (!_name.equals(other._name)) return false;
        return true;
    }
    
}
