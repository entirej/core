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
import java.util.List;

public class EJRestrictions
{
    public static <E> EJRestriction<E> freeText(String propertyName, E value)
    {
        return new FreeText<E>(propertyName, value);
    }
    
    public static <E> EJRestriction<E> equals(String propertyName, E value)
    {
        return new Equals<E>(propertyName, value);
    }
    
    public static <E> EJRestriction<E> equalsIgnoreCase(String propertyName, E value)
    {
        return new EqualsIgnoreCase<E>(propertyName, value);
    }
    
    public static <E> EJRestriction<E> notEquals(String propertyName, E value)
    {
        return new NotEquals<E>(propertyName, value);
    }
    
    public static <E> EJRestriction<E> between(String propertyName, E lowValue, E highValue)
    {
        return new Between<E>(propertyName, lowValue, highValue);
    }
    
    public static <E> EJRestriction<E> greaterThan(String propertyName, E value)
    {
        return new GreaterThan<E>(propertyName, value);
    }
    
    public static <E> EJRestriction<E> greaterThanEqualTo(String propertyName, E value)
    {
        return new GreaterThanEqualTo<E>(propertyName, value);
    }
    
    public static <E> EJRestriction<E> in(String propertyName, E... value)
    {
        List<E> values = new ArrayList<E>();
        
        for (E val : values)
        {
            values.add(val);
        }
        
        return new In<E>(propertyName, values);
    }
    
    public static <E> EJRestriction<E> in(String propertyName, List<E> values)
    {
        return new In<E>(propertyName, values);
    }
    
    public static <E> EJRestriction<E> isNotNull(String propertyName)
    {
        return new IsNotNull<E>(propertyName);
    }
    
    public static <E> EJRestriction<E> isNull(String propertyName)
    {
        return new IsNull<E>(propertyName);
    }
    
    public static <E> EJRestriction<E> lessThan(String propertyName, E value)
    {
        return new LessThan<E>(propertyName, value);
    }
    
    public static <E> EJRestriction<E> lessThanEqualTo(String propertyName, E value)
    {
        return new LessThanEqualTo<E>(propertyName, value);
    }
    
    public static <E> EJRestriction<E> like(String propertyName, E value)
    {
        return new Like<E>(propertyName, value);
    }
    
    public static <E> EJRestriction<E> likeIgnoreCase(String propertyName, E value)
    {
        return new LikeIgnoreCase<E>(propertyName, value);
    }
    
    public static <E> EJRestriction<E> notIn(String propertyName, E... value)
    {
        List<E> values = new ArrayList<E>();
        
        for (E val : value)
        {
            values.add(val);
        }
        
        return new NotIn<E>(propertyName, values);
    }
    
    public static <E> EJRestriction<E> notIn(String propertyName, List<E> values)
    {
        return new NotIn<E>(propertyName, values);
    }
    
    public static <E> EJRestriction<E> notLike(String propertyName, E value)
    {
        return new NotLike<E>(propertyName, value);
    }
    
    public static <E> EJRestriction<E> notLikeIgnoreCase(String propertyName, E value)
    {
        return new NotLikeIgnoreCase<E>(propertyName, value);
    }
    
    static class NotIn<E> extends EJAbstractRestriction<E>
    {
        public NotIn(String blockItemName, List<E> values)
        {
            setBlockItemName(blockItemName);
            setValueList(values);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.NOT_IN;
        }
    }
    
    static class In<E> extends EJAbstractRestriction<E>
    {
        public In(String blockItemName, List<E> values)
        {
            setBlockItemName(blockItemName);
            setValueList(values);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.IN;
        }
    }
    
    static class NotLike<E> extends EJAbstractRestriction<E>
    {
        public NotLike(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.NOT_LIKE;
        }
    }
    
    static class Like<E> extends EJAbstractRestriction<E>
    {
        public Like(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.LIKE;
        }
    }
    
    static class LikeIgnoreCase<E> extends EJAbstractRestriction<E>
    {
        public LikeIgnoreCase(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.LIKE_IGNORE_CASE;
        }
    }
    
    static class NotLikeIgnoreCase<E> extends EJAbstractRestriction<E>
    {
        public NotLikeIgnoreCase(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.NOT_LIKE_IGNORE_CASE;
        }
    }
    
    static class IsNull<E> extends EJAbstractRestriction<E>
    {
        public IsNull(String blockItemName)
        {
            setBlockItemName(blockItemName);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.IS_NULL;
        }
    }
    
    static class IsNotNull<E> extends EJAbstractRestriction<E>
    {
        public IsNotNull(String blockItemName)
        {
            setBlockItemName(blockItemName);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.IS_NOT_NULL;
        }
    }
    
    static class LessThan<E> extends EJAbstractRestriction<E>
    {
        public LessThan(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.LESS_THAN;
        }
    }
    
    static class LessThanEqualTo<E> extends EJAbstractRestriction<E>
    {
        public LessThanEqualTo(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.LESS_THAN_EQUAL_TO;
        }
    }
    
    static class GreaterThan<E> extends EJAbstractRestriction<E>
    {
        public GreaterThan(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.GREATER_THAN;
        }
    }
    
    static class GreaterThanEqualTo<E> extends EJAbstractRestriction<E>
    {
        public GreaterThanEqualTo(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.GREATER_THAN_EQUAL_TO;
        }
    }
    
    static class NotEquals<E> extends EJAbstractRestriction<E>
    {
        public NotEquals(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.NOT_EQUAL;
        }
    }
    
    static class Equals<E> extends EJAbstractRestriction<E>
    {
        public Equals(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.EQUAL;
        }
    }
    
    static class EqualsIgnoreCase<E> extends EJAbstractRestriction<E>
    {
        public EqualsIgnoreCase(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.EQUAL_IGNORE_CASE;
        }
    }
    
    static class Between<E> extends EJAbstractRestriction<E>
    {
        public Between(String blockItemName, E loValue, E hiValue)
        {
            setBlockItemName(blockItemName);
            setBetweenHigherValue(hiValue);
            setBetweenLoverValue(loValue);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.BETWEEN;
        }
    }
    
    static class FreeText<E> extends EJAbstractRestriction<E>
    {
        public FreeText(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.FREE_TEXT;
        }
    }
}
