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
package org.entirej.framework.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class EJFormPojoGenerator implements EJPojoContentGenerator
{

    @Override
    public String getTemplate()
    {
        return asString("EJFormPojoGenerator.ftl");
    }

    public static String asString(String resourceNmae)
    {

        InputStream stream = EJFormPojoGenerator.class.getResourceAsStream(resourceNmae);
        if (stream == null)
        {
            return "";
        }
        Scanner scanner = new Scanner(stream);
        try
        {

            return scanner.useDelimiter("\\A").next();
        }
        finally
        {
            try
            {
                stream.close();
                scanner.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
