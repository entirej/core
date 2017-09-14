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

class EJBetweenExpression implements Serializable
{
    private EJGreaterThanExpression _greaterThan;
    private EJLessThanExpression    _lessThan;
    private String                  _stringFrom;
    private String                  _stringTo;
    
    EJBetweenExpression(String stringFrom, String stringTo)
    {
        if (stringFrom == null && stringTo == null)
        {
            throw new EJApplicationException(new EJMessage("A Between expression must have at least a value from or a value to"));
        }
        
        if (stringFrom == null && stringTo != null)
        {
            _lessThan = new EJLessThanExpression(stringTo);
        }
        else if (stringFrom != null & stringTo == null)
        {
            _greaterThan = new EJGreaterThanExpression(stringFrom);
        }
        else
        {
            _stringFrom = stringFrom;
            _stringTo = stringTo;
        }
    }
    
    public String getExpressionString(String name)
    {
        if (_greaterThan != null)
        {
            return _greaterThan.getExpressionString(name);
        }
        else if (_lessThan != null)
        {
            return _lessThan.getExpressionString(name);
        }
        else
        {
            return " BETWEEN :" + name + " AND :1";
        }
    }
    
    public int setStatementParams(PreparedStatement pstmt, int startPos) throws SQLException
    {
        if (_greaterThan != null)
        {
            return _greaterThan.setStatementParams(pstmt, startPos);
        }
        else if (_lessThan != null)
        {
            return _lessThan.setStatementParams(pstmt, startPos);
        }
        else
        {
            pstmt.setObject(startPos, _stringFrom);
            pstmt.setObject(startPos + 1, _stringTo);
            
            return 2;
        }
    }
    
    public Class<?> getBaseType()
    {
        return String.class;
    }
    
    public String toString()
    {
        return "BETWEEN " + _stringFrom + " AND " + _stringTo;
    }
}
