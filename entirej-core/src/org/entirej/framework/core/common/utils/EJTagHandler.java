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
package org.entirej.framework.core.common.utils;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class EJTagHandler extends DefaultHandler
{
    // child handlers
    private EJTagHandler _currentSubHandler = null;
    
    private StringBuffer _characterBuffer   = new StringBuffer();
    private String       _value             = null;
    private String       _untrimmedValue    = null;
    
    /**
     * handle tag 'name' in this handler; e.g. read attributes
     */
    public abstract void startLocalElement(String uri, String localName, String name, Attributes attributes) throws SAXException;
    
    public abstract void endLocalElement(String uri, String localName, String name) throws SAXException;
    
    public void endSubElement(String uri, String localName, String name) throws SAXException
    {
        
    }
    
    /**
     * called by SAX parser when element starts: "<name attributes="value">"
     * 
     */
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
    {
        // reset content buffer
        _characterBuffer.setLength(0);
        
        // no current subHandler: handle locally or set a subHandler
        if (_currentSubHandler == null)
        {
            startLocalElement(uri, localName, name, attributes);
        }
        
        // delegate to subHandler
        if (_currentSubHandler != null)
        {
            _currentSubHandler.startElement(uri, localName, name, attributes);
        }
    }
    
    /**
     * called by SAX parser when element ends: "</name>"
     */
    public void endElement(String uri, String localName, String name) throws SAXException
    {
        // delegate to subHandler
        if (_currentSubHandler != null)
        {
            _currentSubHandler.endElement(uri, localName, name);
            
            // cleanup after subHandler
            endSubElement(uri, localName, name);
            return;
        }
        
        // handle locally
        _untrimmedValue = _characterBuffer.toString();
        _value = _untrimmedValue.trim();
        
        endLocalElement(uri, localName, name);
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        // delegate to subHandler
        if (_currentSubHandler != null)
        {
            _currentSubHandler.characters(ch, start, length);
            return;
        }
        
        // handle locally
        _characterBuffer.append(ch, start, length);
    }
    
    public EJTagHandler getCurrentSubHandler()
    {
        return _currentSubHandler;
    }
    
    public void setCurrentSubHandler(EJTagHandler subHandler)
    {
        _currentSubHandler = subHandler;
    }
    
    /**
     * Returns the tag value with white leading and trailing spaces removed
     * 
     * @return The tag value with white leading and trailing spaces removed
     */
    public String getValue()
    {
        return _value;
    }
    
    /**
     * Returns the tag value including leading and trailing spaced
     * 
     * @return Returns the tag value including leading and trailing spaced
     */
    public String getUntrimmedValue()
    {
        return _untrimmedValue;
    }
}
