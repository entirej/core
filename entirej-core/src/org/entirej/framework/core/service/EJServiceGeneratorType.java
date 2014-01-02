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

import java.util.List;
import java.util.Properties;

public class EJServiceGeneratorType
{
    private Class<?>            _pojo;
    private String              _tableName;
    private String              _serviceName;
    private String              _packageName;
    private boolean             _queryInPages;
    private String              _queryStatement;
    private String              _insertStatement;
    private String              _updateStatement;
    private String              _deleteStatement;
    private String              _selectProcedureName;
    private String              _insertProcedureName;
    private String              _updateProcedureName;
    private String              _deleteProcedureName;
    private List<EJTableColumn> _selectProcedureParameters;
    private List<EJTableColumn> _insertProcedureParameters;
    private List<EJTableColumn> _updateProcedureParameters;
    private List<EJTableColumn> _deleteProcedureParameters;
    private final Properties    _properties = new Properties();
    
    public Class<?> getPojo()
    {
        return _pojo;
    }
    
    public void setPojo(Class<?> pojo)
    {
        _pojo = pojo;
    }
    
    public String getTableName()
    {
        return _tableName;
    }
    
    public void setTableName(String tableName)
    {
        _tableName = tableName;
    }
    
    public String getServiceName()
    {
        return _serviceName;
    }
    
    public void setServiceName(String serviceName)
    {
        _serviceName = serviceName;
    }
    
    public String getPackageName()
    {
        return _packageName;
    }
    
    public void setPackageName(String packageName)
    {
        _packageName = packageName;
    }
    
    public boolean canQueryInPages()
    {
        return _queryInPages;
    }
    
    public void setQueryInPages(boolean queryInPages)
    {
        _queryInPages = queryInPages;
    }
    
    public String getQueryStatement()
    {
        return _queryStatement;
    }
    
    public void setQueryStatement(String queryStatement)
    {
        _queryStatement = queryStatement;
    }
    
    public String getInsertStatement()
    {
        return _insertStatement;
    }
    
    public void setInsertStatement(String insertStatement)
    {
        _insertStatement = insertStatement;
    }
    
    public String getUpdateStatement()
    {
        return _updateStatement;
    }
    
    public void setUpdateStatement(String updateStatement)
    {
        _updateStatement = updateStatement;
    }
    
    public String getDeleteStatement()
    {
        return _deleteStatement;
    }
    
    public void setDeleteStatement(String deleteStatement)
    {
        _deleteStatement = deleteStatement;
    }
    
    public String getSelectProcedureName()
    {
        return _selectProcedureName;
    }
    
    public void setSelectProcedureName(String selectProcedureName)
    {
        _selectProcedureName = selectProcedureName;
    }
    
    public String getInsertProcedureName()
    {
        return _insertProcedureName;
    }
    
    public void setInsertProcedureName(String insertProcedureName)
    {
        _insertProcedureName = insertProcedureName;
    }
    
    public String getUpdateProcedureName()
    {
        return _updateProcedureName;
    }
    
    public void setUpdateProcedureName(String updateProcedureName)
    {
        _updateProcedureName = updateProcedureName;
    }
    
    public String getDeleteProcedureName()
    {
        return _deleteProcedureName;
    }
    
    public void setDeleteProcedureName(String deleteProcedureName)
    {
        _deleteProcedureName = deleteProcedureName;
    }
    
    public List<EJTableColumn> getSelectProcedureParameters()
    {
        return _selectProcedureParameters;
    }
    
    public void setSelectProcedureParameters(List<EJTableColumn> selectProcedureParameters)
    {
        this._selectProcedureParameters = selectProcedureParameters;
    }
    
    public List<EJTableColumn> getInsertProcedureParameters()
    {
        return _insertProcedureParameters;
    }
    
    public void setInsertProcedureParameters(List<EJTableColumn> insertProcedureParameters)
    {
        this._insertProcedureParameters = insertProcedureParameters;
    }
    
    public List<EJTableColumn> getUpdateProcedureParameters()
    {
        return _updateProcedureParameters;
    }
    
    public void setUpdateProcedureParameters(List<EJTableColumn> updateProcedureParameters)
    {
        this._updateProcedureParameters = updateProcedureParameters;
    }
    
    public List<EJTableColumn> getDeleteProcedureParameters()
    {
        return _deleteProcedureParameters;
    }
    
    public void setDeleteProcedureParameters(List<EJTableColumn> deleteProcedureParameters)
    {
        this._deleteProcedureParameters = deleteProcedureParameters;
    }
    
    public String getProperty(String key, String defaultVlaue)
    {
        return _properties.getProperty(key, defaultVlaue);
    }
    
    public String getProperty(String key)
    {
        return _properties.getProperty(key);
    }
    
    public Object setProperty(String key, String vlaue)
    {
        return _properties.setProperty(key, vlaue);
    }
}
