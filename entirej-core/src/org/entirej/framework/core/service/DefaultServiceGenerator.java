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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJPojoHelper;

public class DefaultServiceGenerator implements EJServiceContentGenerator
{
    
    protected final static String PACKAGE     = "package ";
    protected final static String IMPORT      = "import ";
    protected final static String PUBLIC      = "public ";
    protected final static String PRIVATE     = "private ";
    protected final static String CLASS       = "class ";
    protected final static String IMPLEMENTS  = "implements ";
    protected final static String NEW_LINE    = "\n";
    protected final static String SEMICOLON   = ";";
    protected final static String COMMA       = ",";
    protected final static String OC_BRACKETS = "{";
    protected final static String O_BRACKETS  = "(";
    protected final static String CC_BRACKETS = "}";
    protected final static String C_BRACKETS  = ")";
    protected final static String QUOTATION   = "\"";
    protected final static String EMPTY       = " ";
    protected final static String UNDERSCORE  = "_";
    protected final static String VOID        = "void ";
    protected final static String RETURN      = "return ";
    protected final static String FALSE       = " false ";
    protected final static String TRUE        = " true ";
    protected final static String SET         = " set";
    protected final static String GET         = " get";
    protected final static String EQUALS      = " = ";
    protected final static String OVERRIDE    = "@Override ";
    
    protected List<String> getAdditionalImports(EJServiceGeneratorType type)
    {
        return Collections.<String> emptyList();
    }
    
    private List<String> getSystemImports(EJServiceGeneratorType type)
    {
        List<String> system = new ArrayList<String>();
        system.add(EJForm.class.getName());
        system.add(EJApplicationException.class.getName());
        system.add(EJBlockService.class.getName());
        system.add(EJQueryCriteria.class.getName());
        system.add(EJStatementCriteria.class.getName());
        system.add(EJRestrictions.class.getName());
        system.add(EJStatementExecutor.class.getName());
        system.add(EJStatementParameter.class.getName());
        system.add(ArrayList.class.getName());
        system.add(List.class.getName());
        system.add(type.getPojo().getName());
        
        return system;
    }
    
    @Override
    public String generateContent(EJServiceGeneratorType type)
    {
        try
        {
            String pojoName = type.getPojo().getSimpleName();
            
            StringBuilder fileBuilder = new StringBuilder();
            
            Set<String> imports = new TreeSet<String>();
            // read system and additional imports
            imports.addAll(getSystemImports(type));
            imports.addAll(getAdditionalImports(type));
            
            ArrayList<Class<?>> types = EJPojoHelper.getDataTypes(type.getPojo());
            
            for (Class<?> dataType : types)
            {
                if ((!dataType.getName().startsWith("java.lang")))
                {
                    imports.add(dataType.getName());
                }
            }
            
            // build package name
            fileBuilder.append(PACKAGE).append(type.getPackageName()).append(SEMICOLON);
            fileBuilder.append(NEW_LINE).append(NEW_LINE).append(NEW_LINE);
            // add imports
            for (String clazz : imports)
            {
                fileBuilder.append(IMPORT).append(clazz).append(SEMICOLON).append(NEW_LINE);
            }
            
            fileBuilder.append(NEW_LINE).append(PUBLIC).append(CLASS).append(type.getServiceName()).append(EMPTY).append(IMPLEMENTS).append("EJBlockService<")
                    .append(pojoName).append(">").append(NEW_LINE).append(OC_BRACKETS).append(NEW_LINE);
            
            String baseTableName = type.getTableName();
            
            boolean hasTable = !(baseTableName == null || baseTableName.trim().length() == 0);
            boolean hasQuery = !(type.getQueryStatement() == null || type.getQueryStatement().trim().length() == 0);
            
            if (hasTable || hasQuery)
            {
                fileBuilder.append(PRIVATE);
                fileBuilder.append("final EJStatementExecutor _statementExecutor").append(SEMICOLON).append(NEW_LINE);
                fileBuilder.append(PRIVATE);
                fileBuilder.append("String _selectStatement").append(EQUALS)
                        .append(hasQuery ? splitStatementToStrBuilder(type.getQueryStatement()) : buildSelectStatement(type))
                        .append(SEMICOLON).append(NEW_LINE);
                
                fileBuilder.append(PUBLIC).append(type.getServiceName()).append(O_BRACKETS).append(C_BRACKETS).append(OC_BRACKETS).append(NEW_LINE);
                fileBuilder.append("_statementExecutor");
                fileBuilder.append(EQUALS);
                fileBuilder.append("new EJStatementExecutor").append(O_BRACKETS).append(C_BRACKETS).append(SEMICOLON).append(NEW_LINE);
                fileBuilder.append(CC_BRACKETS).append(NEW_LINE);
                
            }
            
            fileBuilder.append(OVERRIDE).append(NEW_LINE);
            fileBuilder.append(PUBLIC);
            fileBuilder.append("boolean canQueryInPages").append(O_BRACKETS).append(C_BRACKETS).append(OC_BRACKETS).append(NEW_LINE);
            fileBuilder.append(getCanQueryInPagesMethodBody());
            fileBuilder.append(CC_BRACKETS).append(NEW_LINE);
            
            fileBuilder.append(OVERRIDE).append(NEW_LINE);
            fileBuilder.append(PUBLIC);
            
            fileBuilder.append("List<").append(pojoName).append("> executeQuery(EJForm form, EJQueryCriteria queryCriteria").append(C_BRACKETS)
                    .append(OC_BRACKETS).append(NEW_LINE);
            if (hasTable || hasQuery)
            {
                fileBuilder.append(RETURN).append("_statementExecutor.executeQuery(" + pojoName + ".class, form, _selectStatement, queryCriteria")
                        .append(C_BRACKETS).append(SEMICOLON).append(NEW_LINE);
                
            }
            else
            {
                
                fileBuilder.append(RETURN).append("new ArrayList<").append(pojoName).append(">").append(O_BRACKETS).append(C_BRACKETS).append(SEMICOLON);
            }
            
            fileBuilder.append(CC_BRACKETS).append(NEW_LINE);
            
            fileBuilder.append(OVERRIDE).append(NEW_LINE);
            fileBuilder.append(PUBLIC).append(VOID);
            fileBuilder.append("executeInsert(EJForm form, List<").append(pojoName).append("> newRecords").append(C_BRACKETS).append(OC_BRACKETS)
                    .append(NEW_LINE);
            Class<?> datatype;
            String typeName;
            Map<String, Class<?>> props = EJPojoHelper.getGettersAndDatatypes(type.getPojo());
            if (hasTable)
            {
                fileBuilder.append("List<EJStatementParameter> parameters = new ArrayList<EJStatementParameter>").append(O_BRACKETS).append(C_BRACKETS)
                        .append(SEMICOLON).append(NEW_LINE);
                fileBuilder.append("int recordsProcessed = 0;\n");
                fileBuilder.append("for (").append(pojoName).append(" record : newRecords)\n");
                fileBuilder.append(OC_BRACKETS).append(NEW_LINE);
                fileBuilder.append("// Initialise the value list\n");
                fileBuilder.append("parameters.clear").append(O_BRACKETS).append(C_BRACKETS).append(SEMICOLON).append(NEW_LINE);
                
                for (String methodName : props.keySet())
                {
                    if (methodName.contains("Initial"))
                    {
                        continue;
                    }
                    
                    String fieldName = EJPojoHelper.getFieldName(type.getPojo(), methodName);
                    
                    datatype = props.get(methodName);
                    typeName = datatype.getName().substring(datatype.getName().lastIndexOf('.') + 1);
                    fileBuilder.append("parameters.add(new EJStatementParameter(\"").append(fieldName).append("\", ").append(typeName)
                            .append(".class, record.");
                    fileBuilder.append(methodName).append("()));\n");
                }
                
                fileBuilder.append("EJStatementParameter[] paramArray = new EJStatementParameter[parameters.size()];\n");
                fileBuilder.append("recordsProcessed += _statementExecutor.executeInsert(form, \"").append(type.getTableName()).append("\", parameters.toArray(paramArray));\n");
                fileBuilder.append("record.clearInitialValues();\n");
                fileBuilder.append("}\n");
            }
            
            fileBuilder.append("if (recordsProcessed != newRecords.size()) {");
            fileBuilder.append("throw new EJApplicationException(\"Unexpected amount of records processed in insert. Expected: \"+newRecords.size()+\". Inserted: \"+recordsProcessed);");
            
            fileBuilder.append("} }\n\n");
            
            fileBuilder.append("@Override\n");
            fileBuilder.append("public void executeUpdate(EJForm form, List<").append(pojoName).append("> updateRecords)\n");
            fileBuilder.append("{\n");
            if (hasTable)
            {
                fileBuilder.append("List<EJStatementParameter> parameters = new ArrayList<EJStatementParameter>();\n\n");
                fileBuilder.append("int recordsProcessed = 0;");
                fileBuilder.append("for (").append(pojoName).append(" record : updateRecords)\n");
                fileBuilder.append("{\n");
                fileBuilder.append("parameters.clear();\n\n");
                fileBuilder.append("// First add the new values\n");
                
                for (String methodName : props.keySet())
                {
                    if (methodName.contains("Initial"))
                    {
                        continue;
                    }
                    datatype = props.get(methodName);
                    String fieldName = EJPojoHelper.getFieldName(type.getPojo(), methodName);
                    typeName = datatype.getName().substring(datatype.getName().lastIndexOf('.') + 1);
                    fileBuilder.append("parameters.add(new EJStatementParameter(\"").append(fieldName).append("\",").append(typeName).append(".class, record.");
                    fileBuilder.append(methodName).append("()));\n");
                }
                
                fileBuilder.append("\nEJStatementCriteria criteria = new EJStatementCriteria();\n");
                
                for (String methodName : props.keySet())
                {
                    if (!methodName.contains("Initial"))
                    {
                        continue;
                    }
                    
                    fileBuilder.append("if (record.").append(methodName).append("() == null)\n");
                    fileBuilder.append("{\n");
                    fileBuilder.append("criteria.add(EJRestrictions.isNull(\"").append(EJPojoHelper.getFieldName(type.getPojo(), methodName)).append("\"));\n");
                    fileBuilder.append("}\n");
                    fileBuilder.append("else\n");
                    fileBuilder.append("{\n");
                    fileBuilder.append("criteria.add(EJRestrictions.equals(\"").append(EJPojoHelper.getFieldName(type.getPojo(), methodName))
                            .append("\", record.").append(methodName).append("()));\n");
                    fileBuilder.append("}\n");
                }
                
                fileBuilder.append("EJStatementParameter[] paramArray = new EJStatementParameter[parameters.size()];\n");
                fileBuilder.append("recordsProcessed += _statementExecutor.executeUpdate(form, \"").append(type.getTableName())
                        .append("\", criteria, parameters.toArray(paramArray));\n");
                fileBuilder.append("record.clearInitialValues();\n");
                fileBuilder.append("}\n");
            }
            fileBuilder.append("if (recordsProcessed != updateRecords.size()) {");
            fileBuilder.append("throw new EJApplicationException(\"Unexpected amount of records processed in update. Expected: \"+updateRecords.size()+\". Updated: \"+recordsProcessed);");
            
            fileBuilder.append("} }\n\n");
            
            
            fileBuilder.append("@Override\n");
            fileBuilder.append("public void executeDelete(EJForm form, List<").append(pojoName).append("> recordsToDelete)\n");
            fileBuilder.append("{\n");
            if (hasTable)
            {
                fileBuilder.append("ArrayList<EJStatementParameter> parameters = new ArrayList<EJStatementParameter>();\n\n");
                fileBuilder.append("int recordsProcessed = 0;");
                fileBuilder.append("for (").append(pojoName).append(" record : recordsToDelete)\n");
                fileBuilder.append("{\n");
                fileBuilder.append("parameters.clear();\n\n");
                
                fileBuilder.append("EJStatementCriteria criteria = new EJStatementCriteria();\n\n");
                for (String methodName : props.keySet())
                {
                    if (!methodName.contains("Initial"))
                    {
                        continue;
                    }
                    
                    fileBuilder.append("if (record.").append(methodName).append("() == null)\n");
                    fileBuilder.append("{\n");
                    fileBuilder.append("criteria.add(EJRestrictions.isNull(\"").append(EJPojoHelper.getFieldName(type.getPojo(), methodName)).append("\"));\n");
                    fileBuilder.append("}\n");
                    fileBuilder.append("else\n");
                    fileBuilder.append("{\n");
                    fileBuilder.append("criteria.add(EJRestrictions.equals(\"").append(EJPojoHelper.getFieldName(type.getPojo(), methodName))
                            .append("\", record.").append(methodName).append("()));\n");
                    fileBuilder.append("}\n");
                }
                
                fileBuilder.append("EJStatementParameter[] paramArray = new EJStatementParameter[parameters.size()];\n");
                fileBuilder.append("recordsProcessed +=  _statementExecutor.executeDelete(form, \"").append(type.getTableName())
                        .append("\", criteria, parameters.toArray(paramArray));\n");
                fileBuilder.append("record.clearInitialValues();\n");
                fileBuilder.append("}\n");
            }
            fileBuilder.append("if (recordsProcessed != recordsToDelete.size()) {");
            fileBuilder.append("throw new EJApplicationException(\"Unexpected amount of records processed in delete. Expected: \"+recordsToDelete.size()+\". Deleted: \"+recordsProcessed);");
            
            fileBuilder.append("} }\n\n");
            
            fileBuilder.append("\n}");
            
            return fileBuilder.toString();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    protected String getCanQueryInPagesMethodBody()
    {
        
        return new StringBuilder().append(RETURN).append(FALSE).append(SEMICOLON).toString();
    }
    
    public static String buildSelectStatement(EJServiceGeneratorType generatorType)
    {
        String baseTableName = generatorType.getTableName();
        
        if (baseTableName == null || baseTableName.trim().length() == 0)
        {
            return "";
        }
        
        // Only create a statement if there has been no select statement defined
        // for the block
        StringBuffer selectStatementBuffer = new StringBuffer();
        selectStatementBuffer.append(QUOTATION);
        selectStatementBuffer.append("SELECT ");
        
        // Add select columns
        int col = 0;
        for (String fieldName : EJPojoHelper.getFieldNames(generatorType.getPojo()))
        {
            col++;
            if (col > 1)
            {
                selectStatementBuffer.append(COMMA);
            }
            selectStatementBuffer.append(fieldName);
            
        }
        selectStatementBuffer.append(" FROM ");
        selectStatementBuffer.append(baseTableName);
        selectStatementBuffer.append(QUOTATION);
        return selectStatementBuffer.toString();
    }
    
    private String splitStatementToStrBuilder(String stmt)
    {
        
        StringBuilder builder = new StringBuilder();
        
        builder.append("new StringBuilder()");
        
        String[] splits = stmt.split(NEW_LINE);
        
        for (String split : splits)
        {
            builder.append(".append(\"").append("\\n").append(split.replace('\n', ' ').trim()).append(" \")");
        }
        
        builder.append(".toString()");
        
        return builder.toString();
        
    }
    
}
