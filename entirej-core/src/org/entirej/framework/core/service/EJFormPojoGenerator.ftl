package ${package_name};


<#list imports as import>
import ${import};
</#list>
import java.util.HashMap;



public class ${class_name}
{
    private HashMap<FieldNames<?>, Object>    _initialValues      = new HashMap<FieldNames<?>, Object>();
  
<#list columns as column>
    private ${column.data_type} _${column.var_name};
</#list>

    

<#list columns as column>
    @EJFieldName("${column.name}")
    public ${column.data_type} get${column.method_name}()
    {
      return _${column.var_name};
    }
    
    @EJFieldName("${column.name}")
    public void set${column.method_name}(${column.data_type} ${column.var_name})
    {
        _${column.var_name} = ${column.var_name};
        if (!_initialValues.containsKey(FieldNames.${column.name}))
        {
            _initialValues.put(FieldNames.${column.name}, ${column.var_name});
        }
    }
    
</#list>


    @SuppressWarnings("unchecked")
    public <T> T getInitialValue(FieldNames<T> fieldName)
    {
        if (_initialValues.containsKey(fieldName))
        {
            return (T) _initialValues.get(fieldName);
        }
        else
        {
        
            <#list columns as column>
        		if(fieldName.equals(FieldNames.${column.name}))
				{
					return (T)get${column.method_name}();
				}
    		</#list>
           
			        
        
            return null;
        }
    }
    
    public void clearInitialValues()
    {
       _initialValues.clear();
        <#list columns as column>
        
       _initialValues.put(FieldNames.${column.name}, _${column.var_name});
    	</#list>
    }

    public static class FieldNames<T>
    {
    
    <#list columns as column>
        public static final FieldNames<${column.data_type_with_pkg}> ${column.name} = new FieldNames<>();
    </#list>
        T type;
    }

}
