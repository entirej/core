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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public abstract class EJAbstractRestriction<E> implements EJRestriction<E>
{
    private String                                           _blockItemName;
    private String                                           _fieldName;
    private String                                           _alias;
    private E                                                _value;
    private E                                                _valueLo;
    private E                                                _valueHi;
    private List<E>                                          _valueList;
    
    private List<Entry<EJRestrictionJoin, EJRestriction<?>>> _restrictions = new ArrayList<Entry<EJRestrictionJoin, EJRestriction<?>>>();
    
    @Override
    public abstract EJRestrictionType getType();
    
    protected void setBlockItemName(String blockItemName)
    {
        _blockItemName = blockItemName;
    }
    
    public void setFieldName(String fieldName)
    {
        _fieldName = fieldName;
    }
    
    public void setAlias(String alias)
    {
        if (alias == null || alias.trim().length() == 0)
        {
            _alias = null;
        }
        else
        {
            _alias = alias;
        }
    }
    
    public String getAlias()
    {
        return _alias;
    }
    
    protected void setBetweenHigherValue(E value)
    {
        _valueHi = value;
    }
    
    protected void setBetweenLoverValue(E value)
    {
        _valueLo = value;
    }
    
    protected void setValue(E value)
    {
        if (value instanceof java.sql.Timestamp)
        {
            // convert to Timestamp without fractional seconds
            Timestamp ts = (Timestamp) value;
            ts.setNanos(0);
        }
        
        _value = value;
    }
    
    protected void setValueList(List<E> valueList)
    {
        _valueList = valueList;
    }
    
    /**
     * Returns the block item name for which this restriction was constructed
     * 
     * @return The property name for to which this restriction applies
     */
    @Override
    public String getBlockItemName()
    {
        return _blockItemName;
    }
    
    /**
     * Returns the field name of the block item name for which this restriction
     * was constructed. If no field name has been set, the name specified when
     * creating this restriction will be returned
     * 
     * @return The field name of the block item for to which this restriction
     *         applies
     */
    @Override
    public String getFieldName()
    {
        return _fieldName == null ? _blockItemName : _fieldName;
    }
    
    /**
     * If the restriction is of type {@link EJRestrictionType#BETWEEN} then
     * there will be a low value and a high value, this method returns the high
     * value.
     * <p>
     * Note: This method will return <code>null</code> for all restrictions
     * other then <code>Expression.BETWEEN</code>
     * 
     * @return The high value of the between expression
     */
    @Override
    public E getBetweenHigherValue()
    {
        return _valueHi;
    }
    
    /**
     * If the restriction is of type {@link EJRestrictionType#BETWEEN} then
     * there will be a low value and a high value, this method returns the low
     * value.
     * <p>
     * Note: This method will return <code>null</code> for all restriction other
     * then <code>{@link EJRestrictionType#BETWEEN}</code>
     * 
     * @return The low value of the between expression
     */
    @Override
    public E getBetweenLowerValue()
    {
        return _valueLo;
    }
    
    /**
     * Returns the list of values required for a restriction of type
     * {@link EJRestrictionType#IN} or {@link EJRestrictionType#NOT_IN}
     * 
     * @return The values required for <code>IN</code> and </code>NOT_IN</code>
     *         types
     */
    @Override
    public List<E> getValueList()
    {
        return _valueList;
    }
    
    /**
     * Returns the value for this restriction
     * 
     * @return the restriction value
     */
    @Override
    public E getValue()
    {
        return _value;
    }
    
    public String getCacheKey()
    {
        StringBuilder builder = new StringBuilder();
        
        builder.append(getType()).append("|");
        builder.append(getFieldName()).append("|");
        builder.append(getAlias()).append("|");
        
        
        switch (getType())
        {
            case EQUAL:
            case NOT_EQUAL:
            case EQUAL_IGNORE_CASE:
            case FREE_TEXT:
            case GREATER_THAN:
            case GREATER_THAN_EQUAL_TO:
            case LESS_THAN:
            case LESS_THAN_EQUAL_TO:
            case LIKE:
            case LIKE_IGNORE_CASE:
            case NOT_LIKE:
            case NOT_LIKE_IGNORE_CASE:
                builder.append(getValue()).append("|");
                break;
            case BETWEEN:    
                builder.append(getBetweenHigherValue()).append("|");
                builder.append(getBetweenLowerValue()).append("|");
                break;
            case IN:
            case NOT_IN:
                builder.append("[(");
                if (_valueList != null && _valueList.size() > 0)
                {
                    for (int i = 0; i < _valueList.size(); i++)
                    {
                        if (i != 0)
                        {
                            builder.append(",");
                        }
                        builder.append(_valueList.get(i) == null ? "null" : _valueList.get(i));
                    }
                }
                builder.append(")]");
                break;

            default:
                break;
        }
        
       
       
        
        return builder.toString();
    }
    
    public void add(EJRestrictionJoin join, EJRestriction<?> restriction)
    {
        
        _restrictions.add(new JoinEntry<EJRestrictionJoin, EJRestriction<?>>(join, restriction));
        
    }
    
    public List<Entry<EJRestrictionJoin, EJRestriction<?>>> getSubRestrictions()
    {
        return new ArrayList<Entry<EJRestrictionJoin, EJRestriction<?>>>(_restrictions);
    }
    
    private static class JoinEntry<K, V> implements Entry<K, V>
    {
        
        private K k;
        private V v;
        
        public JoinEntry(K k, V v)
        {
            this.k = k;
            this.v = v;
        }
        
        @Override
        public K getKey()
        {
            return k;
        }
        
        @Override
        public V getValue()
        {
            return v;
        }
        
        @Override
        public V setValue(V value)
        {
            return v = value;
        }
        
    }
}
