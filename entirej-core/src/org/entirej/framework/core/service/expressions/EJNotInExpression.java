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
package org.entirej.framework.core.service.expressions;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;

class EJNotInExpression implements Serializable
{
    private String[] _strings;
    
    EJNotInExpression(String[] strings)
    {
        _strings = strings;
    }
    
    public String getExpressionString(String name)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" NOT IN (");
        for (int i = 0; i < _strings.length; i++)
        {
            if (i == 0)
            {
                buffer.append(":" + name);
            }
            else
            {
                buffer.append(",:" + i);
            }
        }
        
        buffer.append(") ");
        return buffer.toString();
    }
    
    public int setStatementParams(PreparedStatement pstmt, int startPos) throws SQLException
    {
        int paramCounter = 0;
        for (int i = 0; i < _strings.length; i++)
        {
            paramCounter++;
            pstmt.setObject(startPos + i, _strings[i]);
        }
        
        return paramCounter;
    }
    
    public Class<?> getBaseType()
    {
        return String.class;
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" NOT IN (");
        for (int i = 0; i < _strings.length; i++)
        {
            if (i == 0)
            {
                buffer.append(":" + _strings[i]);
            }
            else
            {
                buffer.append(",:" + _strings[i]);
            }
        }
        
        buffer.append(") ");
        return buffer.toString();
    }
}
