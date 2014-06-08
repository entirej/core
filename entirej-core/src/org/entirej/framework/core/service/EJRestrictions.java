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

    public static <E> EJRestriction<E> freeText(String propertyName, boolean serviceItem, E value)
    {
        return new FreeText<E>(propertyName, value, serviceItem);
    }
    
    public static <E> EJRestriction<E> equals(String propertyName, E value)
    {
        return new Equals<E>(propertyName, value);
    }

    public static <E> EJRestriction<E> equals(String propertyName, boolean serviceItem, E value)
    {
        return new Equals<E>(propertyName, value, serviceItem);
    }
    
    public static <E> EJRestriction<E> equalsIgnoreCase(String propertyName, E value)
    {
        return new EqualsIgnoreCase<E>(propertyName, value);
    }

    public static <E> EJRestriction<E> equalsIgnoreCase(String propertyName, boolean serviceItem, E value)
    {
        return new EqualsIgnoreCase<E>(propertyName, value, serviceItem);
    }
    
    public static <E> EJRestriction<E> notEquals(String propertyName, E value)
    {
        return new NotEquals<E>(propertyName, value);
    }

    public static <E> EJRestriction<E> notEquals(String propertyName, boolean serviceItem, E value)
    {
        return new NotEquals<E>(propertyName, value, serviceItem);
    }
    
    public static <E> EJRestriction<E> between(String propertyName, E lowValue, E highValue)
    {
        return new Between<E>(propertyName, lowValue, highValue);
    }

    public static <E> EJRestriction<E> between(String propertyName, E lowValue, boolean serviceItem, E highValue)
    {
        return new Between<E>(propertyName, lowValue, highValue, serviceItem);
    }
    
    public static <E> EJRestriction<E> greaterThan(String propertyName, E value)
    {
        return new GreaterThan<E>(propertyName, value);
    }

    public static <E> EJRestriction<E> greaterThan(String propertyName, boolean serviceItem, E value)
    {
        return new GreaterThan<E>(propertyName, value, serviceItem);
    }
    
    public static <E> EJRestriction<E> greaterThanEqualTo(String propertyName, E value)
    {
        return new GreaterThanEqualTo<E>(propertyName, value);
    }

    public static <E> EJRestriction<E> greaterThanEqualTo(String propertyName, boolean serviceItem, E value)
    {
        return new GreaterThanEqualTo<E>(propertyName, value, serviceItem);
    }
    
    public static <E> EJRestriction<E> in(String propertyName, E... value)
    {
        return in(propertyName, true, value);
    }
    
    public static <E> EJRestriction<E> in(String propertyName, boolean serviceItem, E... value)
    {
        List<E> values = new ArrayList<E>();

        for (E val : values)
        {
            values.add(val);
        }

        return new In<E>(propertyName, values, serviceItem);
    }

    public static <E> EJRestriction<E> in(String propertyName, List<E> values)
    {
        return new In<E>(propertyName, values);
    }

    public static <E> EJRestriction<E> in(String propertyName, boolean serviceItem, List<E> values)
    {
        return new In<E>(propertyName, values, serviceItem);
    }
    
    public static <E> EJRestriction<E> isNotNull(String propertyName)
    {
        return new IsNotNull<E>(propertyName);
    }

    public static <E> EJRestriction<E> isNotNull(String propertyName, boolean serviceItem)
    {
        return new IsNotNull<E>(propertyName, serviceItem);
    }
    
    public static <E> EJRestriction<E> isNull(String propertyName)
    {
        return new IsNull<E>(propertyName);
    }

    public static <E> EJRestriction<E> isNull(String propertyName, boolean serviceItem)
    {
        return new IsNull<E>(propertyName, serviceItem);
    }
    
    public static <E> EJRestriction<E> lessThan(String propertyName, E value)
    {
        return new LessThan<E>(propertyName, value);
    }

    public static <E> EJRestriction<E> lessThan(String propertyName, boolean serviceItem, E value)
    {
        return new LessThan<E>(propertyName, value, serviceItem);
    }

    public static <E> EJRestriction<E> lessThanEqualTo(String propertyName, E value)
    {
        return new LessThanEqualTo<E>(propertyName, value);
    }

    public static <E> EJRestriction<E> lessThanEqualTo(String propertyName, boolean serviceItem, E value)
    {
        return new LessThanEqualTo<E>(propertyName, value, serviceItem);
    }
    
    public static <E> EJRestriction<E> like(String propertyName, E value)
    {
        return new Like<E>(propertyName, value);
    }

    public static <E> EJRestriction<E> like(String propertyName, boolean serviceItem, E value)
    {
        return new Like<E>(propertyName, value, serviceItem);
    }
    
    public static <E> EJRestriction<E> likeIgnoreCase(String propertyName, E value)
    {
        return new LikeIgnoreCase<E>(propertyName, value);
    }

    public static <E> EJRestriction<E> likeIgnoreCase(String propertyName, boolean serviceItem, E value)
    {
        return new LikeIgnoreCase<E>(propertyName, value, serviceItem);
    }
    
    public static <E> EJRestriction<E> notIn(String propertyName, E... value)
    {
        return notIn(propertyName, true, value);
    }

    public static <E> EJRestriction<E> notIn(String propertyName, boolean serviceItem, E... value)
    {
        List<E> values = new ArrayList<E>();

        for (E val : value)
        {
            values.add(val);
        }

        return new NotIn<E>(propertyName, values, serviceItem);
    }
    
    public static <E> EJRestriction<E> notIn(String propertyName, List<E> values)
    {
        return new NotIn<E>(propertyName, values);
    }

    public static <E> EJRestriction<E> notIn(String propertyName, boolean serviceItem, List<E> values)
    {
        return new NotIn<E>(propertyName, values, serviceItem);
    }

    public static <E> EJRestriction<E> notLike(String propertyName, E value)
    {
        return new NotLike<E>(propertyName, value);
    }

    public static <E> EJRestriction<E> notLike(String propertyName, boolean serviceItem, E value)
    {
        return new NotLike<E>(propertyName, value, serviceItem);
    }
    
    public static <E> EJRestriction<E> notLikeIgnoreCase(String propertyName, E value)
    {
        return new NotLikeIgnoreCase<E>(propertyName, value);
    }

    public static <E> EJRestriction<E> notLikeIgnoreCase(String propertyName, boolean serviceItem, E value)
    {
        return new NotLikeIgnoreCase<E>(propertyName, value, serviceItem);
    }
    
    static class NotIn<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public NotIn(String blockItemName, List<E> values)
        {
            setBlockItemName(blockItemName);
            setValueList(values);
        }
        
        public NotIn(String blockItemName, List<E> values, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValueList(values);
            this.serviceItem = serviceItem;
        }

        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.NOT_IN;
        }

        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class In<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public In(String blockItemName, List<E> values)
        {
            setBlockItemName(blockItemName);
            setValueList(values);
        }

        public In(String blockItemName, List<E> values, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValueList(values);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.IN;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class NotLike<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public NotLike(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }

        public NotLike(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.NOT_LIKE;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class Like<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public Like(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }
        
        public Like(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.LIKE;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class LikeIgnoreCase<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public LikeIgnoreCase(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }

        public LikeIgnoreCase(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.LIKE_IGNORE_CASE;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class NotLikeIgnoreCase<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public NotLikeIgnoreCase(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }

        public NotLikeIgnoreCase(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.NOT_LIKE_IGNORE_CASE;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class IsNull<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public IsNull(String blockItemName)
        {
            setBlockItemName(blockItemName);
        }

        public IsNull(String blockItemName, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.IS_NULL;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class IsNotNull<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public IsNotNull(String blockItemName)
        {
            setBlockItemName(blockItemName);
        }

        public IsNotNull(String blockItemName, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.IS_NOT_NULL;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class LessThan<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public LessThan(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }

        public LessThan(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.LESS_THAN;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class LessThanEqualTo<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;

        public LessThanEqualTo(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }

        public LessThanEqualTo(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }

        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.LESS_THAN_EQUAL_TO;
        }

        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class GreaterThan<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public GreaterThan(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }

        public GreaterThan(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.GREATER_THAN;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
        
    }

    static class GreaterThanEqualTo<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public GreaterThanEqualTo(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }

        public GreaterThanEqualTo(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.GREATER_THAN_EQUAL_TO;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class NotEquals<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public NotEquals(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }

        public NotEquals(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.NOT_EQUAL;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class Equals<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public Equals(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }

        public Equals(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.EQUAL;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class EqualsIgnoreCase<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public EqualsIgnoreCase(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }

        public EqualsIgnoreCase(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.EQUAL_IGNORE_CASE;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class Between<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public Between(String blockItemName, E loValue, E hiValue)
        {
            setBlockItemName(blockItemName);
            setBetweenHigherValue(hiValue);
            setBetweenLoverValue(loValue);
        }

        public Between(String blockItemName, E loValue, E hiValue, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setBetweenHigherValue(hiValue);
            setBetweenLoverValue(loValue);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.BETWEEN;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }

    static class FreeText<E> extends EJAbstractRestriction<E>
    {
        boolean serviceItem = true;
        
        public FreeText(String blockItemName, E value)
        {
            setBlockItemName(blockItemName);
            setValue(value);
        }

        public FreeText(String blockItemName, E value, boolean serviceItem)
        {
            setBlockItemName(blockItemName);
            setValue(value);
            this.serviceItem = serviceItem;
        }
        
        @Override
        public EJRestrictionType getType()
        {
            return EJRestrictionType.FREE_TEXT;
        }
        
        @Override
        public boolean isServiceItemRestriction()
        {
            return serviceItem;
        }
    }
}
