package org.entirej.framework.core;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.entirej.framework.core.service.EJPojoProperty;
import org.junit.Test;

public class TestEJPojoHelper
{
    
    @Test
    public void testGetFieldNames()
    {
        EJPojoHelper helper = new EJPojoHelper();
        List<String> fieldNames = EJPojoHelper.getFieldNames(Address.class);
        // test some field names
        assertTrue(fieldNames.contains("CUSTOMER_ID"));
        assertTrue(fieldNames.contains("LINE_3"));
        assertTrue(fieldNames.contains("NAME"));
        assertTrue(fieldNames.contains("ADDRESS_TYPE_ID"));
    }
    
    @Test
    public void testGetDataType()
    {
        EJPojoHelper helper = new EJPojoHelper();
        Class<?> returntype = helper.getDataType(Address.class, "CUSTOMER_ID");
        assertEquals(returntype, String.class);
        
        returntype = helper.getDataType(Address.class, "ADDRESS_TYPE_ID");
        assertEquals(returntype, BigDecimal.class);
    }
    
    @Test
    public void testSetFieldValue()
    {
        EJPojoHelper helper = new EJPojoHelper();
        Address address = new Address();
        String val1 = "VALUE1";
        
        helper.setFieldValue("CUSTOMER_ID", address, val1);
        assertEquals(val1, address.getCustomerId());
        
        String val2 = "VALUE2";
        helper.setFieldValue("CUSTOMER_ID", address, val2);
        assertEquals(val2, address.getCustomerId());
    }
    
    // test POJO class
    public static class Address
    {
        private EJPojoProperty<String>     _customerId;
        private EJPojoProperty<String>     _line3;
        private EJPojoProperty<String>     _name;
        private EJPojoProperty<BigDecimal> _addressTypeId;
        private EJPojoProperty<String>     _line1;
        private EJPojoProperty<BigDecimal> _id;
        private EJPojoProperty<String>     _line4;
        private EJPojoProperty<BigDecimal> _countryCodeId;
        private EJPojoProperty<String>     _line2;
        private EJPojoProperty<String>     _postCode;
        
        @EJFieldName("CUSTOMER_ID")
        public String getCustomerId()
        {
            return EJPojoProperty.getPropertyValue(_customerId);
        }
        
        @EJFieldName("CUSTOMER_ID")
        public void setCustomerId(String customerId)
        {
            _customerId = EJPojoProperty.setPropertyValue(_customerId, customerId);
        }
        
        @EJFieldName("CUSTOMER_ID")
        public String getInitialCustomerId()
        {
            return EJPojoProperty.getPropertyInitialValue(_customerId);
        }
        
        @EJFieldName("LINE_3")
        public String getLine3()
        {
            return EJPojoProperty.getPropertyValue(_line3);
        }
        
        @EJFieldName("LINE_3")
        public void setLine3(String line3)
        {
            _line3 = EJPojoProperty.setPropertyValue(_line3, line3);
        }
        
        @EJFieldName("LINE_3")
        public String getInitialLine3()
        {
            return EJPojoProperty.getPropertyInitialValue(_line3);
        }
        
        @EJFieldName("NAME")
        public String getName()
        {
            return EJPojoProperty.getPropertyValue(_name);
        }
        
        @EJFieldName("NAME")
        public void setName(String name)
        {
            _name = EJPojoProperty.setPropertyValue(_name, name);
        }
        
        @EJFieldName("NAME")
        public String getInitialName()
        {
            return EJPojoProperty.getPropertyInitialValue(_name);
        }
        
        @EJFieldName("ADDRESS_TYPE_ID")
        public BigDecimal getAddressTypeId()
        {
            return EJPojoProperty.getPropertyValue(_addressTypeId);
        }
        
        @EJFieldName("ADDRESS_TYPE_ID")
        public void setAddressTypeId(BigDecimal addressTypeId)
        {
            _addressTypeId = EJPojoProperty.setPropertyValue(_addressTypeId, addressTypeId);
        }
        
        @EJFieldName("ADDRESS_TYPE_ID")
        public BigDecimal getInitialAddressTypeId()
        {
            return EJPojoProperty.getPropertyInitialValue(_addressTypeId);
        }
        
        @EJFieldName("LINE_1")
        public String getLine1()
        {
            return EJPojoProperty.getPropertyValue(_line1);
        }
        
        @EJFieldName("LINE_1")
        public void setLine1(String line1)
        {
            _line1 = EJPojoProperty.setPropertyValue(_line1, line1);
        }
        
        @EJFieldName("LINE_1")
        public String getInitialLine1()
        {
            return EJPojoProperty.getPropertyInitialValue(_line1);
        }
        
        @EJFieldName("ID")
        public BigDecimal getId()
        {
            return EJPojoProperty.getPropertyValue(_id);
        }
        
        @EJFieldName("ID")
        public void setId(BigDecimal id)
        {
            _id = EJPojoProperty.setPropertyValue(_id, id);
        }
        
        @EJFieldName("ID")
        public BigDecimal getInitialId()
        {
            return EJPojoProperty.getPropertyInitialValue(_id);
        }
        
        @EJFieldName("LINE_4")
        public String getLine4()
        {
            return EJPojoProperty.getPropertyValue(_line4);
        }
        
        @EJFieldName("LINE_4")
        public void setLine4(String line4)
        {
            _line4 = EJPojoProperty.setPropertyValue(_line4, line4);
        }
        
        @EJFieldName("LINE_4")
        public String getInitialLine4()
        {
            return EJPojoProperty.getPropertyInitialValue(_line4);
        }
        
        @EJFieldName("COUNTRY_CODE_ID")
        public BigDecimal getCountryCodeId()
        {
            return EJPojoProperty.getPropertyValue(_countryCodeId);
        }
        
        @EJFieldName("COUNTRY_CODE_ID")
        public void setCountryCodeId(BigDecimal countryCodeId)
        {
            _countryCodeId = EJPojoProperty.setPropertyValue(_countryCodeId, countryCodeId);
        }
        
        @EJFieldName("COUNTRY_CODE_ID")
        public BigDecimal getInitialCountryCodeId()
        {
            return EJPojoProperty.getPropertyInitialValue(_countryCodeId);
        }
        
        @EJFieldName("LINE_2")
        public String getLine2()
        {
            return EJPojoProperty.getPropertyValue(_line2);
        }
        
        @EJFieldName("LINE_2")
        public void setLine2(String line2)
        {
            _line2 = EJPojoProperty.setPropertyValue(_line2, line2);
        }
        
        @EJFieldName("LINE_2")
        public String getInitialLine2()
        {
            return EJPojoProperty.getPropertyInitialValue(_line2);
        }
        
        @EJFieldName("POST_CODE")
        public String getPostCode()
        {
            return EJPojoProperty.getPropertyValue(_postCode);
        }
        
        @EJFieldName("POST_CODE")
        public void setPostCode(String postCode)
        {
            _postCode = EJPojoProperty.setPropertyValue(_postCode, postCode);
        }
        
        @EJFieldName("POST_CODE")
        public String getInitialPostCode()
        {
            return EJPojoProperty.getPropertyInitialValue(_postCode);
        }
        
        public void clearInitialValues()
        {
            EJPojoProperty.clearInitialValue(_customerId);
            EJPojoProperty.clearInitialValue(_line3);
            EJPojoProperty.clearInitialValue(_name);
            EJPojoProperty.clearInitialValue(_addressTypeId);
            EJPojoProperty.clearInitialValue(_line1);
            EJPojoProperty.clearInitialValue(_id);
            EJPojoProperty.clearInitialValue(_line4);
            EJPojoProperty.clearInitialValue(_countryCodeId);
            EJPojoProperty.clearInitialValue(_line2);
            EJPojoProperty.clearInitialValue(_postCode);
        }
        
    }
}
