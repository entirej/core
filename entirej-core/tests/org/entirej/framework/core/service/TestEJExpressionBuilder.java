package org.entirej.framework.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestEJExpressionBuilder
{
    @Test
    void betweenWithBothBoundsBindsLowerThenUpper()
    {
        Expression expression = build(EJRestrictions.between("AMOUNT", 10, 20));

        assertEquals("AMOUNT BETWEEN ? AND ?", expression.sql());
        assertEquals(List.of(10, 20), expression.values());
    }

    @Test
    void betweenWithOnlyLowerBoundBindsTheLowerValue()
    {
        Expression expression = build(EJRestrictions.between("AMOUNT", 10, null));

        assertEquals("AMOUNT > ?", expression.sql());
        assertEquals(List.of(10), expression.values());
    }

    @Test
    void betweenWithOnlyUpperBoundBindsTheUpperValue()
    {
        Expression expression = build(EJRestrictions.between("AMOUNT", null, 20));

        assertEquals("AMOUNT < ?", expression.sql());
        assertEquals(List.of(20), expression.values());
    }

    @Test
    void betweenWithNoBoundsProducesNoPredicateOrParameters()
    {
        Expression expression = build(EJRestrictions.between("AMOUNT", null, null));

        assertEquals("", expression.sql());
        assertEquals(List.of(), expression.values());
    }

    @Test
    void oneSidedBetweenUsesAliasAndPreservesJoinedParameterOrder()
    {
        EJRestriction<Integer> first = EJRestrictions.between("AMOUNT", 10, null);
        first.setAlias("T.AMOUNT");
        first.add(EJRestrictionJoin.OR, EJRestrictions.between("LIMIT", null, 20));

        Expression expression = build(first);

        assertEquals("( T.AMOUNT > ? OR LIMIT < ? )", expression.sql());
        assertEquals(List.of(10, 20), expression.values());
    }

    @Test
    void oneSidedBetweenSupportsDateAndTimestampValues()
    {
        Date date = Date.valueOf("2026-10-01");
        Timestamp timestamp = Timestamp.valueOf("2026-10-26 10:15:30");

        assertEquals(List.of(date), build(EJRestrictions.between("BUSINESS_DATE", date, null)).values());
        assertEquals(List.of(timestamp), build(EJRestrictions.between("CREATED_AT", null, timestamp)).values());
    }

    @Test
    void serviceItemOverloadRetainsItsFlag()
    {
        EJRestriction<Integer> restriction = EJRestrictions.between("AMOUNT", 10, false, null);

        assertFalse(restriction.isServiceItemRestriction());
        assertEquals(List.of(10), build(restriction).values());
    }

    @Test
    void emptyOrNullInIsAlwaysFalseWithoutParameters()
    {
        assertConstant(EJRestrictions.in("STATUS", List.<String>of()), "1 = 0");
        assertConstant(EJRestrictions.in("STATUS", (List<String>) null), "1 = 0");
        assertConstant(EJRestrictions.<String> in("STATUS"), "1 = 0");
        assertConstant(EJRestrictions.<String> in("STATUS", (String[]) null), "1 = 0");
    }

    @Test
    void emptyOrNullNotInIsAlwaysTrueWithoutParameters()
    {
        assertConstant(EJRestrictions.notIn("STATUS", List.<String>of()), "1 = 1");
        assertConstant(EJRestrictions.notIn("STATUS", (List<String>) null), "1 = 1");
        assertConstant(EJRestrictions.<String> notIn("STATUS"), "1 = 1");
        assertConstant(EJRestrictions.<String> notIn("STATUS", (String[]) null), "1 = 1");
    }

    @Test
    void nonEmptyInPreservesValuesIncludingNull()
    {
        Expression expression = build(EJRestrictions.in("STATUS", List.of("OPEN", "CLOSED")));

        assertEquals("STATUS IN (?,?)", expression.sql());
        assertEquals(List.of("OPEN", "CLOSED"), expression.values());

        Expression withNull = build(EJRestrictions.in("STATUS", new String[] { "OPEN", null }));
        assertEquals("STATUS IN (?,?)", withNull.sql());
        assertEquals(2, withNull.values().size());
        assertEquals("OPEN", withNull.values().get(0));
        assertEquals(null, withNull.values().get(1));
    }

    private static void assertConstant(EJRestriction<?> restriction, String expected)
    {
        Expression expression = build(restriction);
        assertEquals(expected, expression.sql());
        assertEquals(List.of(), expression.values());
    }

    private static Expression build(EJRestriction<?> restriction)
    {
        ArrayList<EJStatementParameter> parameters = new ArrayList<EJStatementParameter>();
        String sql = normalize(EJExpressionBuilder.buildExpression(restriction, parameters));
        List<Object> values = parameters.stream().map(EJStatementParameter::getValue).toList();
        return new Expression(sql, values);
    }

    private static String normalize(String sql)
    {
        return sql.trim().replaceAll("\\s+", " ");
    }

    private record Expression(String sql, List<Object> values)
    {
    }
}
