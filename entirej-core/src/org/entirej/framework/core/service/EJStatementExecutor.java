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
package org.entirej.framework.core.service;

import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJPojoHelper;
import org.entirej.framework.core.interfaces.EJFrameworkConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJStatementExecutor implements Serializable
{
    final Logger logger = LoggerFactory.getLogger(EJStatementExecutor.class);
    
    public int executeDelete(EJForm form, String tableName, EJStatementCriteria criteria, EJStatementParameter... parameters)
    {
        if (form == null)
        {
            throw new NullPointerException("Form passed to executeDelete cannot be null");
        }
        return executeDelete(form.getConnection(), tableName, criteria, parameters);
    }
    
    public int executeDelete(EJFrameworkConnection connection, String tableName, EJStatementCriteria criteria, EJStatementParameter... parameters)
    {
        if(logger.isInfoEnabled())
            logger.info("Executing delete for {}", tableName);
        
        StringBuilder stmt = new StringBuilder();
        stmt.append("\nDELETE FROM ");
        stmt.append(tableName);
        stmt.append(" ");
        
        return executeStatement(connection, stmt.toString(), criteria, parameters);
    }
    
    public int executeInsert(EJForm form, String tableName, EJStatementParameter... parameters)
    {
        if (form == null)
        {
            throw new NullPointerException("Form passed to executeInsert cannot be null");
        }
        
        return executeInsert(form.getConnection(), tableName, parameters);
    }
    
    public int executeInsert(EJFrameworkConnection connection, String tableName, EJStatementParameter... parameters)
    {
        if(logger.isInfoEnabled())
            logger.info("Executing insert for {}", tableName);
        
        StringBuilder stmt = new StringBuilder();
        stmt.append("\nINSERT INTO ");
        stmt.append(tableName);
        stmt.append("\n(");
        
        StringBuilder valuesStmt = new StringBuilder();
        valuesStmt.append("\nVALUES (\n");
        
        boolean isFirst = true;
        for (EJStatementParameter param : parameters)
        {
            if (!isFirst)
            {
                stmt.append("\n, ");
                valuesStmt.append(",");
            }
            stmt.append(param.getFieldName());
            valuesStmt.append("?");
            isFirst = false;
        }
        stmt.append(")");
        valuesStmt.append(")");
        stmt.append(valuesStmt.toString());
        
        if(logger.isInfoEnabled())
            logger.info("insertStatment:\n{}", stmt.toString());
        
        return executeStatement(connection, stmt.toString(), null, parameters);
    }
    
    public int executeUpdate(EJForm form, String tableName, EJStatementCriteria criteria, EJStatementParameter... parameters)
    {
        if (form == null)
        {
            throw new NullPointerException("Form passed to executeUpdate cannot be null");
        }
        
        return executeUpdate(form.getConnection(), tableName, criteria, parameters);
    }
    
    public int executeUpdate(EJFrameworkConnection fwkConnection, String tableName, EJStatementCriteria criteria, EJStatementParameter... parameters)
    {
        if(logger.isInfoEnabled())
            logger.info("Executing update for {}", tableName);
        
        StringBuilder stmt = new StringBuilder();
        stmt.append("\nUPDATE ");
        stmt.append(tableName);
        stmt.append(" SET \n");
        
        boolean isFirst = true;
        for (EJStatementParameter param : parameters)
        {
            if (!isFirst)
            {
                stmt.append("\n, ");
            }
            stmt.append(param.getFieldName());
            stmt.append(" = ?");
            isFirst = false;
        }
        
        return executeStatement(fwkConnection, stmt.toString(), criteria, parameters);
    }
    
    private int executeStatement(EJFrameworkConnection fwkConnection, String statement, EJStatementCriteria criteria, EJStatementParameter... parameters)
    {
        if (fwkConnection == null)
        {
            throw new NullPointerException("No EJFrameworkConnection passed to the EJStatementExecutor");
        }
        
        StringBuilder stmt = new StringBuilder(statement);
        ArrayList<EJStatementParameter> updateParameters = new ArrayList<EJStatementParameter>();
        
        PreparedStatement pstmt = null;
        final boolean infoEnabled = logger.isInfoEnabled();
        try
        {
            Object conObj = fwkConnection.getConnectionObject();
            if (conObj == null)
            {
                throw new EJApplicationException(
                        "The StatementExecutor requires the ConnectionFactory to return a JDBC Connection but the type returned was null");
            }
            
            if (!(conObj instanceof Connection))
            {
                throw new EJApplicationException(
                        "The StatementExecutor requires the ConnectionFactory to return a JDBC Connection but another type was returned");
            }
            
            if (criteria != null)
            {
                for (EJRestriction<?> restriction : criteria.getBlockServiceItemRestrictions())
                {
                    if (restriction.isServiceItemRestriction())
                    {
                        stmt.append(addWhere(stmt.toString(), EJExpressionBuilder.buildExpression(restriction, updateParameters)));
                    }
                }
            }
            
            if(infoEnabled)
                logger.info("Statement to be executed after adding where: {}", stmt.toString());
            
            pstmt = ((Connection) conObj).prepareStatement(stmt.toString());
            int pos = 1;
            
            for (EJStatementParameter parameter : parameters)
            {
                if (parameter.getValue() == null)
                {
                    if(infoEnabled)
                        logger.info("Statement parameter at index {} being set to NULL", pos);
                    pstmt.setNull(pos++, parameter.getJdbcType());
                }
                else
                {
                    if(infoEnabled)
                        logger.info("Statement parameter at index {} being set to {}", pos, parameter.getValue());
                    pstmt.setObject(pos++, parameter.getValue());
                }
            }
            
            // Now add the update criteria values
            for (EJStatementParameter parameter : updateParameters)
            {
                if (parameter.getValue() == null)
                {
                    if(infoEnabled)
                        logger.info("Statement parameter at index {} being set to NULL", pos);
                    pstmt.setNull(pos++, parameter.getJdbcType());
                }
                else
                {
                    if(infoEnabled)
                        logger.info("Statement parameter at index {} being set to {}", pos, parameter.getValue());
                    pstmt.setObject(pos++, parameter.getValue());
                }
            }
            
            int updatedRecordCount = pstmt.executeUpdate();
            
            return updatedRecordCount;
            
        }
        catch (Exception e)
        {
            if(infoEnabled)
                logger.info("Error executing statement", e);
            e.printStackTrace();
            try
            {
                pstmt.close();
            }
            catch (SQLException e2)
            {
            }
            throw new EJApplicationException("Error executing update statement: " + e.getMessage(), e);
        }
        finally
        {
            try
            {
                pstmt.close();
            }
            catch (SQLException e)
            {
            }
        }
    }
    
    public int executeStoredProcedure(EJForm form, String procedureStatement, EJStoredProcedureStatementParameter... parameters)
    {
        if (form == null)
        {
            throw new NullPointerException("Form passed to executeStoredProcedure cannot be null");
        }
        
        return executeStoredProcedure(form.getConnection(), procedureStatement, parameters);
    }
    
    public int executeStoredProcedure(EJFrameworkConnection fwkConnection, String procedureStatement, EJStoredProcedureStatementParameter... parameters)
    {
        if (fwkConnection == null)
        {
            throw new NullPointerException("The EJFrameworkConnection passed to the EJStatementExecutor is null");
        }
        
        final boolean infoEnabled = logger.isInfoEnabled();
        if(infoEnabled)
            logger.info("Executing stored procedure for {}", procedureStatement);
        
        CallableStatement proc = null;
        try
        {
            Object conObj = fwkConnection.getConnectionObject();
            if (conObj == null || !(conObj instanceof Connection))
            {
                throw new EJApplicationException(
                        "The StatementExecutor requires the ConnectionFactory to return a JDBC Connection but another type was returned");
            }
            
            proc = ((Connection) conObj).prepareCall(procedureStatement);
            int pos = 0;
            
            for (EJStatementParameter parameter : parameters)
            {
                pos++;
                switch (parameter.getParameterType())
                {
                    case IN:
                        if(infoEnabled)
                            logger.info("Statement IN parameter being registered at index {} and being set to {}", pos, parameter.getValue());
                        proc.setObject(pos, parameter.getValue());
                        break;
                    case INOUT:
                        if(infoEnabled)
                            logger.info("Statement INOUT parameter being registered at index {} and being set to {}", pos, parameter.getValue());
                        parameter.setPosition(pos);
                        proc.setObject(pos, parameter.getValue());
                        proc.registerOutParameter(pos, parameter.getJdbcType());
                        break;
                    case OUT:
                    case RETURN:
                        if(infoEnabled)
                            logger.info("Statement OUT/RETURN parameter being regestered at index {}", pos);
                        proc.registerOutParameter(pos, parameter.getJdbcType());
                        parameter.setPosition(pos);
                        break;
                }
            }
            
            if(infoEnabled)
                logger.info("Executing Statement");
            proc.execute();
            if(infoEnabled)
                logger.info("Statement Completed");
            
            for (EJStatementParameter parameter : parameters)
            {
                switch (parameter.getParameterType())
                {
                    case INOUT:
                    case OUT:
                    case RETURN:
                        Object value = proc.getObject(parameter.getPosition());
                        if(infoEnabled)
                            logger.info("Retrieving OUT/RETURN parameter at index {}, value = {}", parameter.getPosition(), value);
                        parameter.setValue(value);
                        break;
                }
            }
            
        }
        catch (Exception e)
        {
            if(infoEnabled)
                logger.info("Error executing statement", e);
            e.printStackTrace();
            try
            {
                proc.close();
            }
            catch (SQLException e2)
            {
            }
            throw new EJApplicationException("Error executing stored procedure", e);
        }
        finally
        {
            try
            {
                proc.close();
            }
            catch (SQLException e)
            {
            }
        }
        
        return 0;
    }
    
    public <T> List<T> executeQuery(Class<T> pojoType, EJForm form, String selectStatement, EJQueryCriteria queryCriteria)
    {
        return executeQuery(pojoType, form.getConnection(), selectStatement, queryCriteria);
    }
    
    public <T> List<T> executeQuery(Class<T> pojoType, EJFrameworkConnection fwkConnection, String selectStatement, EJQueryCriteria queryCriteria)
    {
        final boolean infoEnabled = logger.isInfoEnabled();
        if(infoEnabled)
            logger.info("Executing query to return a list of {}\n{}", pojoType, selectStatement);
        
        StringBuffer stmt = new StringBuffer(selectStatement);
        
        ArrayList<EJStatementParameter> queryValues = new ArrayList<EJStatementParameter>();
        for (EJRestriction<?> restriction : queryCriteria.getAllRestrictions())
        {
            if (restriction.isServiceItemRestriction())
            {
                stmt.append(addWhere(stmt.toString(), EJExpressionBuilder.buildExpression(restriction, queryValues)));
            }
        }
        
        if(infoEnabled)
            logger.info("Added the QueryCriteria expressions {}", stmt.toString());
        
        for (EJQuerySort sort : queryCriteria.getSorts())
        {
            stmt.append(addOrderBy(stmt.toString(), sort));
        }
        
        if(infoEnabled)
            logger.info("Added the Order By expressions {}", stmt.toString());
        
        EJStatementParameter[] valuesArray = new EJStatementParameter[queryValues.size()];
        return executeQuery(pojoType, fwkConnection, stmt.toString(), queryCriteria, queryValues.toArray(valuesArray));
    }
    
    private <T> List<T> executeQuery(Class<T> pojoType, EJFrameworkConnection fwkConnection, String selectStatement, EJQueryCriteria queryCriteria,
            EJStatementParameter... parameters)
    {
        EJPojoHelper helper = new EJPojoHelper();
        
        ArrayList<T> results = new ArrayList<T>();
        PreparedStatement pstmt = null;
        final boolean infoEnabled = logger.isInfoEnabled();
        try
        {
            Object conObj = fwkConnection.getConnectionObject();
            if (conObj == null || !(conObj instanceof Connection))
            {
                throw new EJApplicationException(
                        "The StatementExecutor requires the ConnectionFactory to return a JDBC Connection but another type was returned");
            }
            
            // I can only add paging to a select if it has been set within the
            // query criteria. If not query criteria has been set, then no paging
            // is possible
            if (queryCriteria != null)
            {
                pstmt = ((Connection) conObj).prepareStatement(wrapSelectForPaging(selectStatement, queryCriteria));
            }
            else
            {
                pstmt = ((Connection) conObj).prepareStatement(selectStatement);
            }
            
            int pos = 1;
            
            for (EJStatementParameter parameter : parameters)
            {
                if(infoEnabled)
                    logger.info("Statement parameter at index {} being set to {}", pos, parameter.getValue());
                pstmt.setObject(pos++, parameter.getValue());
            }
            
            if(infoEnabled)
                logger.info("Executing Query");
            ResultSet rset = pstmt.executeQuery();
            ResultSetMetaData metaData = rset.getMetaData();
            if(infoEnabled)
                logger.info("Query Executed");
            try
            {
                while (rset.next())
                {
                    T result = pojoType.newInstance();
                    
                    for (int i = 1; i <= metaData.getColumnCount(); i++)
                    {
                        helper.setFieldValue(metaData.getColumnLabel(i), result, rset.getObject(i));
                    }
                    results.add(result);
                }
                
                if(infoEnabled)
                    logger.info("Query retrieved {} results", results.size());
            }
            catch (InstantiationException e)
            {
                throw new EJApplicationException("Error creating pojo instance", e);
            }
            catch (IllegalAccessException e)
            {
                throw new EJApplicationException("Error creating pojo instance", e);
            }
            return results;
            
        }
        catch (SQLException e)
        {
            if(infoEnabled)
                logger.info("Error Executing Query", e);
            e.printStackTrace();
            try
            {
                pstmt.close();
            }
            catch (SQLException e2)
            {
            }
            throw new EJApplicationException("Error executing block query", e);
        }
        finally
        {
            try
            {
                if (pstmt != null)
                {
                    pstmt.close();
                }
            }
            catch (SQLException e)
            {
            }
        }
    }
    
    public List<EJSelectResult> executeQuery(EJForm form, String selectStatement, EJQueryCriteria queryCriteria, EJStatementParameter... parameters)
    {
        if (form == null)
        {
            throw new NullPointerException("Form passed to executeQuery cannot be null");
        }
        return executeQuery(form.getConnection(), selectStatement, queryCriteria, parameters);
    }
    
    public List<EJSelectResult> executeQuery(EJForm form, String selectStatement, EJStatementParameter... parameters)
    {
        if (form == null)
        {
            throw new NullPointerException("Form passed to executeQuery cannot be null");
        }
        return executeQuery(form.getConnection(), selectStatement, null, parameters);
    }
    
    public List<EJSelectResult> executeQuery(EJFrameworkConnection fwkConnection, String selectStatement, EJStatementParameter... parameters)
    {
        return executeQuery(fwkConnection, selectStatement, null, parameters);
    }
    
    public List<EJSelectResult> executeQuery(EJFrameworkConnection fwkConnection, String selectStatement, EJQueryCriteria queryCriteria,
            EJStatementParameter... parameters)
    {
        if (fwkConnection == null)
        {
            throw new NullPointerException("No EJFrameworkConnection passed to EJStatementExecutor");
        }
        
        boolean infoEnabled = logger.isInfoEnabled();
        if(infoEnabled)
            logger.info("Executing generic query {}", selectStatement);
        
        PreparedStatement pstmt = null;
        try
        {
            Object conObj = fwkConnection.getConnectionObject();
            if (conObj == null || !(conObj instanceof Connection))
            {
                throw new EJApplicationException(
                        "The StatementExecutor requires the ConnectionFactory to return a JDBC Connection but another type was returned");
            }
            ArrayList<EJStatementParameter> allParameters = new ArrayList<EJStatementParameter>(Arrays.asList(parameters));
            if (queryCriteria != null)
            {
                StringBuffer stmt = new StringBuffer(selectStatement);
                
                for (EJRestriction<?> restriction : queryCriteria.getAllRestrictions())
                {
                    if (restriction.isServiceItemRestriction())
                    {
                        stmt.append(addWhere(stmt.toString(), EJExpressionBuilder.buildExpression(restriction, allParameters)));
                    }
                }
                
                if(infoEnabled)
                    logger.info("Added the QueryCriteria expressions {}", stmt.toString());
                
                for (EJQuerySort sort : queryCriteria.getSorts())
                {
                    stmt.append(addOrderBy(stmt.toString(), sort));
                }
                
                selectStatement = stmt.toString();
            }
            // I can only add paging to a select if it has been set within the
            // query criteria. If not query criteria has been set, then no
            // paging
            // is possible
            if (queryCriteria != null)
            {
                pstmt = ((Connection) conObj).prepareStatement(wrapSelectForPaging(selectStatement, queryCriteria));
            }
            else
            {
                pstmt = ((Connection) conObj).prepareStatement(selectStatement);
            }
            int pos = 1;
            
            for (EJStatementParameter parameter : allParameters)
            {
                if(infoEnabled)
                    logger.info("Statement parameter at index {} being set to {}", pos, parameter.getValue());
                pstmt.setObject(pos++, parameter.getValue());
            }
            
            if(infoEnabled)
                logger.info("Executing Query");
            ResultSet rset = pstmt.executeQuery();
            ResultSetMetaData metaData = rset.getMetaData();
            if(infoEnabled)
                logger.info("Query Executed");
            
            ArrayList<EJSelectResult> results = new ArrayList<EJSelectResult>();
            while (rset.next())
            {
                EJSelectResult result = new EJSelectResult();
                
                for (int i = 1; i <= metaData.getColumnCount(); i++)
                {
                    result.addItem(metaData.getColumnLabel(i), rset.getObject(i));
                }
                
                results.add(result);
            }
            
            if(infoEnabled)
                logger.info("Query retrieved {} results", results.size());
            return results;
            
        }
        catch (SQLException e)
        {
            if(infoEnabled)
                logger.info("Error executing query", e);
            e.printStackTrace();
            try
            {
                pstmt.close();
            }
            catch (SQLException e2)
            {
            }
            throw new EJApplicationException("Error executing query", e);
        }
        finally
        {
            try
            {
                if (pstmt != null)
                {
                    pstmt.close();
                }
            }
            catch (SQLException e)
            {
            }
        }
    }
    
    private String addWhere(String statement, String whereClause)
    {
        if (containsWhere(statement))
        {
            return " AND " + whereClause;
        }
        else
        {
            return " WHERE " + whereClause;
        }
    }
    
    private String addOrderBy(String statement, EJQuerySort sort)
    {
        if (containsOrderBy(statement))
        {
            return " , " + sort.getSort() + " " + (sort.getType() == EJQuerySortType.ASCENDING ? "ASC" : "DESC");
        }
        else
        {
            return " ORDER BY " + sort.getSort() + " " + (sort.getType() == EJQuerySortType.ASCENDING ? "ASC" : "DESC");
        }
    }
    
    private boolean containsWhere(String statement)
    {
        int nestingLevel = 0;
        Pattern pattern = Pattern.compile("(\\()|(\\))|(\\s+where\\s+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(" " + statement);
        while (matcher.find())
        {
            if (matcher.start(1) != -1)
            {
                nestingLevel++;
            }
            if (matcher.start(2) != -1)
            {
                nestingLevel--;
            }
            if (matcher.start(3) != -1 && nestingLevel == 0)
            {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean containsOrderBy(String statement)
    {
        int nestingLevel = 0;
        Pattern pattern = Pattern.compile("(\\()|(\\))|(\\s+order\\s+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(" " + statement);
        while (matcher.find())
        {
            if (matcher.start(1) != -1)
            {
                nestingLevel++;
            }
            if (matcher.start(2) != -1)
            {
                nestingLevel--;
            }
            if (matcher.start(3) != -1 && nestingLevel == 0)
            {
                return true;
            }
        }
        return false;
    }
    
    public String wrapSelectForPaging(String selectStatement, EJQueryCriteria queryCriteria)
    {
        return selectStatement;
    }
}
