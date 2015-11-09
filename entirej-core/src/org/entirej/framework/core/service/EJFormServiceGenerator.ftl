package ${package_name};


<#list imports as import>
import ${import};
</#list>
import java.util.HashMap;



public class ${service_name} implements EJBlockService<${pojo_name}>
{
<#if query_statement != "">
  private final EJStatementExecutor _statementExecutor   = new EJStatementExecutor();
  private String   				    _selectStatement = ${query_statement};
</#if>


    @Override
    public List<${pojo_name}> executeQuery(EJForm form, EJQueryCriteria queryCriteria)
    {
<#if query_statement != "">
        return _statementExecutor.executeQuery(${pojo_name}.class, form, _selectStatement, queryCriteria);
</#if>
<#if query_statement == "" >
        return new java.util.ArrayList<${pojo_name}>(0);
</#if>         
    }    
    

    @Override
    public void executeInsert(EJForm form, List<${pojo_name}> newRecords)
    {
    
<#if table_name != "">   
        List<EJStatementParameter> parameters = new ArrayList<EJStatementParameter>();
        int recordsProcessed = 0;
        for (${pojo_name} record : newRecords)
        {
            // Initialise the value list
          
<#list fields as field >
			 parameters.add(new EJStatementParameter("${field.name}", ${field.data_type}.class, record.get${field.method_name}()));
</#list>
            EJStatementParameter[] paramArray = new EJStatementParameter[parameters.size()];
            recordsProcessed += _statementExecutor.executeInsert(form,"${table_name}", parameters.toArray(paramArray));
            record.clearInitialValues();
        }
        if (recordsProcessed != newRecords.size())
        {
            throw new EJApplicationException("Unexpected amount of records processed in insert. Expected: " + newRecords.size() + ". Inserted: "
                    + recordsProcessed);
        }
</#if>        
    }


    @Override
    public void executeUpdate(EJForm form, List<${pojo_name}> updateRecords)
    {
<#if table_name != "">      
        List<EJStatementParameter> parameters = new ArrayList<EJStatementParameter>();

        int recordsProcessed = 0;
        for (${pojo_name} record : updateRecords)
        {
            parameters.clear();

            // First add the new values
<#list fields as field >
			 parameters.add(new EJStatementParameter("${field.name}", ${field.data_type}.class, record.get${field.method_name}()));
</#list>

            EJStatementCriteria criteria = new EJStatementCriteria();
            
<#list fields as field >
			if (record.getInitialValue(${pojo_name}.FieldNames.${field.name}) == null)
            {
                criteria.add(EJRestrictions.isNull("${field.name}"));
            }
            else
            {
                criteria.add(EJRestrictions.equals("${field.name}", record.getInitialValue(${pojo_name}.FieldNames.${field.name})));
            }
</#list>            
            
            
            EJStatementParameter[] paramArray = new EJStatementParameter[parameters.size()];
            recordsProcessed += _statementExecutor.executeUpdate(form, "${table_name}", criteria, parameters.toArray(paramArray));
            record.clearInitialValues();
        }
        if (recordsProcessed != updateRecords.size())
        {
            throw new EJApplicationException("Unexpected amount of records processed in update. Expected: " + updateRecords.size() + ". Updated: "
                    + recordsProcessed);
        }  
</#if>              
    }
    
    
    @Override
    public void executeDelete(EJForm form, List<${pojo_name}> recordsToDelete)
    {
<#if table_name != "">     
        ArrayList<EJStatementParameter> parameters = new ArrayList<EJStatementParameter>();

        int recordsProcessed = 0;
        for (${pojo_name} record : recordsToDelete)
        {

            EJStatementCriteria criteria = new EJStatementCriteria();


<#list fields as field >
			if (record.getInitialValue(${pojo_name}.FieldNames.${field.name}) == null)
            {
                criteria.add(EJRestrictions.isNull("${field.name}"));
            }
            else
            {
                criteria.add(EJRestrictions.equals("${field.name}", record.getInitialValue(${pojo_name}.FieldNames.${field.name})));
            }
</#list>

            EJStatementParameter[] paramArray = new EJStatementParameter[parameters.size()];
            recordsProcessed += _statementExecutor.executeDelete(form, "${table_name}", criteria, parameters.toArray(paramArray));
            record.clearInitialValues();
        }
        if (recordsProcessed != recordsToDelete.size())
        {
            throw new EJApplicationException("Unexpected amount of records processed in delete. Expected: " + recordsToDelete.size() + ". Deleted: "
                    + recordsProcessed);
        }
</#if>         
    }
    
    
    @Override
    public boolean canQueryInPages()
    {
        return ${queryInPages};
    }
}
