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

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJMessage;

class EJGreaterThanExpression implements Serializable
{
    private String _string;
    
    EJGreaterThanExpression(String string)
    {
        if (string == null)
        {
            throw new EJApplicationException(new EJMessage("A GreaterThan expression must contain a value"));
        }
        
        _string = string;
        
    }
    
    public String getExpressionString(String name)
    {
        return " > :" + name;
    }
    
    public int setStatementParams(PreparedStatement pstmt, int startPos) throws SQLException
    {
        pstmt.setObject(startPos, _string);
        return 1;
    }
    
    public Class<?> getBaseType()
    {
        return String.class;
    }
    
    public String toString()
    {
        return "> " + _string;
    }
}
