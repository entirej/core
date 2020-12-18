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

import java.io.Serializable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class EJCorePropertiesTagHandler extends DefaultHandler implements Serializable
{
    // delegate handling
    private EJCorePropertiesTagHandler _delegateHandler = null;
    private boolean                    _wantsToQuit     = false;
    
    // character handling
    private StringBuffer               _characterBuffer = new StringBuffer();
    private String                     _value           = null;
    private String                     _untrimmedValue  = null;
    
    // ----------------------------- simple TagHandler methods
    
    /**
     * handle beginning of a tag with optional attributes <name> or <name
     * attr1="val1" attr2="val2">
     * 
     * Implementation should assign attributes for tags it handles itself or
     * install a delegate handler with setDelegate()
     * 
     * @param name
     * @param attributes
     */
    abstract void startLocalElement(String name, Attributes attributes) throws SAXException;
    
    protected void setDelegate(EJCorePropertiesTagHandler delegateHandler)
    {
        _delegateHandler = delegateHandler;
    }
    
    protected EJCorePropertiesTagHandler getDelegate()
    {
        return _delegateHandler;
    }
    
    /**
     * handle end of a tag value string or CDATA</name>
     * 
     * Implementation should assign value or untrimmedValue for tags it handles
     * itself and call quitAsDelegate() if its main tag ends
     * 
     * @param value
     * @param untrimmedValue
     * @param name
     */
    abstract void endLocalElement(String name, String value, String untrimmedValue) throws SAXException;
    
    protected void quitAsDelegate()
    {
        _wantsToQuit = true;
    }
    
    /**
     * clean up after a Delegate, which was installed using setDelegate() in
     * startLocalElement(), has finished its job
     * 
     * @param name
     * @param currentDelegate
     */
    protected void cleanUpAfterDelegate(String name, EJCorePropertiesTagHandler currentDelegate)
    {
    }
    
    // ----------------------------- ContentHandler Interface implementation
    /**
     * called by SAX parser when element starts: "<name attributes="value">"
     */
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
    {
        if(name!=null)
            name = name.intern();
        // reset content buffer
        _characterBuffer.setLength(0);
        
        // no current subHandler: handle locally or install a subHandler
        if (_delegateHandler == null)
        {
            startLocalElement(name, attributes);
        }
        
        // delegate to subHandler
        if (_delegateHandler != null)
        {
            _delegateHandler.startElement(uri, localName, name, attributes);
        }
    }
    
    /**
     * called by SAX parser when element ends: "</name>"
     */
    public void endElement(String uri, String localName, String name) throws SAXException
    {
        if(name!=null)
            name = name.intern();
        // delegate to subHandler
        if (_delegateHandler != null)
        {
            _delegateHandler.endElement(uri, localName, name);
            
            // cleanup after subHandler
            if (_delegateHandler._wantsToQuit)
            {
                cleanUpAfterDelegate(name, _delegateHandler);
                _delegateHandler = null;
            }
            return;
        }
        
        // handle locally
        _untrimmedValue = _characterBuffer.toString();
        _value = _untrimmedValue.trim().intern();
        
        endLocalElement(name, _value, _untrimmedValue);
    }
    
    /**
     * called by SAX parser when element content is read: "<element>content may
     * be CDATA</element>" CDATA content is automatically handled by SAX,
     * characters() might be called multiple times
     */
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        // delegate to subHandler
        if (_delegateHandler != null)
        {
            _delegateHandler.characters(ch, start, length);
            return;
        }
        
        // handle locally
        _characterBuffer.append(ch, start, length);
    }
}
