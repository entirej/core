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
package org.entirej.framework.core.properties;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class EJFileLoader implements Serializable
{
    /**
     * Loads a given fully qualified file from either the Classpath or an
     * absolute path
     * <p>
     * First the <code>FileLoader</code> will check the CLASSPATH for the file
     * using
     * <code>FileLoader.class.getClassLoader().getResourcesAsStream()</code> and
     * if this is not successfull, then <code>FileLoader</code> will try to
     * create a new <code>File</code> instance using the file name given. If the
     * file is found then the file will be read into a DOM tree and the DOM
     * document will be returned. If the file is not found then
     * <code>null</code> will be returned.
     * 
     * @param fileName
     *            The file required
     * @return The file contents as a DOM tree or null if the file was not found
     * @throws IllegalArgumentException
     *             if there were problems incurred when opening required file
     */
    public static InputStream loadFile(String fileName)
    {
        return loadFileFromClasspath(fileName);
    }
    
    /**
     * Used to check if a given file exists
     * <p>
     * <p>
     * First the <code>FileLoader</code> will check the CLASSPATH for the file
     * using
     * <code>FileLoader.class.getClassLoader().getResourcesAsStream()</code> and
     * if this is not successfull, then <code>FileLoader</code> will try to
     * create a new <code>File</code> instance using the file name given.
     * 
     * @param fileName
     *            The name of the file to check
     * @return <code>true</code> if the file exists otherwise <code>false</code>
     */
    public static boolean fileExists(String fileName)
    {
        // First check the classpath for the file
        InputStream inStream = EJFileLoader.class.getClassLoader().getResourceAsStream(fileName);
        
        if (inStream != null)
        {
            try
            {
                inStream.close();
            }
            catch (IOException e)
            {
                // Ignore any IOExceptio upon closing the file.
            }
            return true;
        }
        else
        {
            // The file was not found on the class path, so try on an absolute
            // path
            File file = new File(fileName);
            
            if (file.exists())
            {
                file = null;
                return true;
            }
            else
            {
                return false;
            }
        }
    }
    
    private static InputStream loadFileFromClasspath(String fileName)
    {
        InputStream inStream = EJFileLoader.class.getClassLoader().getResourceAsStream(fileName);
        if (inStream == null)
        {
            return null;
        }
        else
        {
            return inStream;
        }
        
    }
}
