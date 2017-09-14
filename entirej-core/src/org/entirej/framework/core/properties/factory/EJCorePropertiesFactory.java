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
package org.entirej.framework.core.properties.factory;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.properties.EJFileLoader;
import org.entirej.framework.core.properties.reader.EJCoreEntireJPropertiesHandler;

public class EJCorePropertiesFactory
{
    /**
     * Initializes the EntireJ Properties using the given document
     * 
     * @param manager
     *            The framework manager
     * @param entireJPropertiesFileName
     *            The name of the properties file containing the EntireJ
     *            properties
     * @return An <code>EntireJProperties</code> object containing the
     *         properties read from the given document
     * @throws EJApplicationException
     *             When there is a problem reading any of the EntireJ properties
     * @throws NullPointerException
     *             When the <code>Document</code> passed is null
     */
    public static void initialiseEntireJProperties(EJFrameworkManager manager, String entireJPropertiesFileName)
    {
        if (entireJPropertiesFileName == null || entireJPropertiesFileName.trim().length() == 0)
        {
            throw new NullPointerException("The entireJ properties file name passed to createEntireJProperties is either null or of zero length");
        }
        
        try
        {
            InputStream inStream = EJFileLoader.class.getClassLoader().getResourceAsStream(entireJPropertiesFileName);
            
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxPArser = factory.newSAXParser();
            EJCoreEntireJPropertiesHandler handler = manager.getHandlerFactory().createEntireJPropertiesHandler();
            saxPArser.parse(inStream, handler);
        }
        catch (Exception e)
        {
            throw new EJApplicationException(e);
        }
    }
}
