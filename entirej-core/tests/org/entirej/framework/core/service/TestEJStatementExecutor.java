package org.entirej.framework.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.interfaces.EJFrameworkConnection;
import org.junit.jupiter.api.Test;

public class TestEJStatementExecutor
{
    @Test
    void invalidConnectionDoesNotMaskTheCauseWithNullPointerException()
    {
        EJApplicationException failure = assertThrows(EJApplicationException.class,
                () -> new EJStatementExecutor().executeDelete(frameworkConnection(null), "TEST_TABLE", null));

        assertInstanceOf(EJApplicationException.class, failure.getCause());
        assertTrue(failure.getCause().getMessage().contains("JDBC Connection"));
    }

    @Test
    void preparedStatementIsClosedWhenExecutionFails()
    {
        AtomicBoolean closed = new AtomicBoolean();
        PreparedStatement statement = proxy(PreparedStatement.class, (proxy, method, args) -> {
            if (method.getName().equals("executeUpdate"))
            {
                throw new SQLException("update failed");
            }
            if (method.getName().equals("close"))
            {
                closed.set(true);
            }
            return defaultValue(method.getReturnType());
        });
        Connection connection = proxy(Connection.class, (proxy, method, args) -> {
            if (method.getName().equals("prepareStatement"))
            {
                return statement;
            }
            return defaultValue(method.getReturnType());
        });

        EJApplicationException failure = assertThrows(EJApplicationException.class,
                () -> new EJStatementExecutor().executeDelete(frameworkConnection(connection), "TEST_TABLE", null));

        assertInstanceOf(SQLException.class, failure.getCause());
        assertTrue(closed.get());
    }

    @Test
    void callableStatementIsClosedWhenExecutionFails()
    {
        AtomicBoolean closed = new AtomicBoolean();
        CallableStatement statement = proxy(CallableStatement.class, (proxy, method, args) -> {
            if (method.getName().equals("execute"))
            {
                throw new SQLException("procedure failed");
            }
            if (method.getName().equals("close"))
            {
                closed.set(true);
            }
            return defaultValue(method.getReturnType());
        });
        Connection connection = proxy(Connection.class, (proxy, method, args) -> {
            if (method.getName().equals("prepareCall"))
            {
                return statement;
            }
            return defaultValue(method.getReturnType());
        });

        EJApplicationException failure = assertThrows(EJApplicationException.class,
                () -> new EJStatementExecutor().executeStoredProcedure(frameworkConnection(connection), "{call test_proc()}"));

        assertInstanceOf(SQLException.class, failure.getCause());
        assertTrue(closed.get());
    }

    @Test
    void queryCriteriaIsInsertedBeforeOrderByAndParametersStayInSqlOrder()
    {
        SqlCapture capture = sqlCapture();
        EJQueryCriteria criteria = new EJQueryCriteria();
        criteria.add(EJRestrictions.between("ID", 10, null));

        new EJStatementExecutor().executeQuery(frameworkConnection(capture.connection()),
                "SELECT ID, '?' AS MARKER FROM TEST_TABLE WHERE TYPE = ? ORDER BY CASE WHEN ? = 1 THEN ID END", criteria,
                new EJStatementParameter("TYPE"), new EJStatementParameter(1));

        assertEquals("SELECT ID, '?' AS MARKER FROM TEST_TABLE WHERE TYPE = ? AND ID > ? ORDER BY CASE WHEN ? = 1 THEN ID END",
                normalize(capture.sql()));
        assertEquals(List.of("TYPE", 10, 1), capture.values());
    }

    @Test
    void clauseDetectionIgnoresLiteralsCommentsAndNestedQueries()
    {
        SqlCapture capture = sqlCapture();
        EJQueryCriteria criteria = new EJQueryCriteria();
        criteria.add(EJRestrictions.equals("CODE", 7));
        criteria.add(EJQuerySort.DESC("CODE"));

        new EJStatementExecutor().executeQuery(frameworkConnection(capture.connection()),
                "SELECT 'WHERE ORDER BY ?' AS TXT FROM (SELECT ID FROM INNER_T WHERE FLAG = ?) X "
                        + "/* ORDER BY ignored */ ORDER BY ID;",
                criteria, new EJStatementParameter("Y"));

        String sql = normalize(capture.sql());
        assertTrue(sql.contains("/* ORDER BY ignored */ WHERE CODE = ? ORDER BY ID , CODE DESC;"));
        assertEquals(List.of("Y", 7), capture.values());
    }

    @Test
    void updateBindsOneSidedBetweenAfterUpdateValues()
    {
        SqlCapture capture = sqlCapture();
        EJStatementCriteria criteria = new EJStatementCriteria();
        criteria.add(EJRestrictions.between("ID", 10, null));

        int updated = new EJStatementExecutor().executeUpdate(frameworkConnection(capture.connection()), "TEST_TABLE", criteria,
                new EJStatementParameter("NAME", String.class, "updated"));

        assertEquals(1, updated);
        assertEquals("UPDATE TEST_TABLE SET NAME = ? WHERE ID > ?", normalize(capture.sql()));
        assertEquals(List.of("updated", 10), capture.values());
    }

    @Test
    void deleteBindsTheUpperValueOfOneSidedBetween()
    {
        SqlCapture capture = sqlCapture();
        EJStatementCriteria criteria = new EJStatementCriteria();
        criteria.add(EJRestrictions.between("ID", null, 20));

        int deleted = new EJStatementExecutor().executeDelete(frameworkConnection(capture.connection()), "TEST_TABLE", criteria);

        assertEquals(1, deleted);
        assertEquals("DELETE FROM TEST_TABLE WHERE ID < ?", normalize(capture.sql()));
        assertEquals(List.of(20), capture.values());
    }

    @Test
    void updateRejectsAnEmptyRestrictionBeforePreparingTheStatement()
    {
        SqlCapture capture = sqlCapture();
        EJStatementCriteria criteria = new EJStatementCriteria();
        criteria.add(EJRestrictions.between("ID", null, null));

        EJApplicationException failure = assertThrows(EJApplicationException.class,
                () -> new EJStatementExecutor().executeUpdate(frameworkConnection(capture.connection()), "TEST_TABLE", criteria,
                        new EJStatementParameter("NAME", String.class, "updated")));

        assertTrue(failure.getMessage().contains("empty restriction predicate"));
        assertNull(capture.sql());
    }

    @Test
    void deleteRejectsAnEmptyRestrictionBeforePreparingTheStatement()
    {
        SqlCapture capture = sqlCapture();
        EJStatementCriteria criteria = new EJStatementCriteria();
        criteria.add(EJRestrictions.between("ID", null, null));

        EJApplicationException failure = assertThrows(EJApplicationException.class,
                () -> new EJStatementExecutor().executeDelete(frameworkConnection(capture.connection()), "TEST_TABLE", criteria));

        assertTrue(failure.getMessage().contains("empty restriction predicate"));
        assertNull(capture.sql());
    }

    @Test
    void emptyInRemainsAValidPredicateBeforeOrderBy()
    {
        SqlCapture capture = sqlCapture();
        EJQueryCriteria criteria = new EJQueryCriteria();
        criteria.add(EJRestrictions.in("ID", List.of()));

        new EJStatementExecutor().executeQuery(frameworkConnection(capture.connection()),
                "SELECT ID FROM TEST_TABLE ORDER BY ID", criteria);

        assertEquals("SELECT ID FROM TEST_TABLE WHERE 1 = 0 ORDER BY ID", normalize(capture.sql()));
    }

    @Test
    void trailingCommentsCannotSwallowCriteriaOrSorts()
    {
        SqlCapture capture = sqlCapture();
        EJQueryCriteria criteria = new EJQueryCriteria();
        criteria.add(EJRestrictions.equals("ID", 7));
        criteria.add(EJQuerySort.ASC("ID"));

        new EJStatementExecutor().executeQuery(frameworkConnection(capture.connection()),
                "SELECT ID FROM TEST_TABLE WHERE NAME = 'tail' -- ignored ORDER BY\n", criteria);

        assertEquals("SELECT ID FROM TEST_TABLE WHERE NAME = 'tail' AND ID = ? ORDER BY ID ASC -- ignored ORDER BY",
                normalize(capture.sql()));
        assertEquals(List.of(7), capture.values());
    }

    @Test
    void actualDatabaseQueriesHonorRangeBoundariesAndPlaceholderOrder() throws SQLException
    {
        try (Connection connection = fixtureDatabase())
        {
            EJFrameworkConnection framework = frameworkConnection(connection);
            EJStatementExecutor executor = new EJStatementExecutor();
            EJQueryCriteria criteria = new EJQueryCriteria();
            criteria.add(EJRestrictions.between("ID", 10, 20));
            assertEquals(List.of(10, 15, 20), ids(executor.executeQuery(framework,
                    "SELECT ID FROM TEST_TABLE ORDER BY ID", criteria)));

            criteria = new EJQueryCriteria();
            criteria.add(EJRestrictions.between("ID", 10, null));
            assertEquals(List.of(15, 20), ids(executor.executeQuery(framework,
                    "SELECT ID FROM TEST_TABLE WHERE ID <= ? ORDER BY CASE WHEN ? = 1 THEN ID END", criteria,
                    new EJStatementParameter(20), new EJStatementParameter(1))));

            criteria = new EJQueryCriteria();
            criteria.add(EJRestrictions.between("ID", null, 20));
            assertEquals(List.of(10, 15), ids(executor.executeQuery(framework,
                    "SELECT ID FROM TEST_TABLE ORDER BY ID -- trailing comment", criteria)));

            criteria = new EJQueryCriteria();
            criteria.add(EJRestrictions.in("ID", List.of()));
            assertEquals(List.of(), ids(executor.executeQuery(framework,
                    "SELECT ID FROM TEST_TABLE ORDER BY ID", criteria)));
        }
    }

    @Test
    void actualDatabaseUpdatesAndDeletesOnlyTheMatchingRows() throws SQLException
    {
        try (Connection connection = fixtureDatabase())
        {
            EJFrameworkConnection framework = frameworkConnection(connection);
            EJStatementExecutor executor = new EJStatementExecutor();
            EJStatementCriteria criteria = new EJStatementCriteria();
            criteria.add(EJRestrictions.between("ID", 10, null));
            assertEquals(2, executor.executeUpdate(framework, "TEST_TABLE", criteria,
                    new EJStatementParameter("NAME", String.class, "changed")));
            assertEquals(List.of(15, 20), ids(executor.executeQuery(framework,
                    "SELECT ID FROM TEST_TABLE WHERE NAME = ? ORDER BY ID", new EJStatementParameter("changed"))));

            criteria = new EJStatementCriteria();
            criteria.add(EJRestrictions.between("ID", null, 15));
            assertEquals(1, executor.executeDelete(framework, "TEST_TABLE", criteria));
            assertEquals(List.of(15, 20), ids(executor.executeQuery(framework, "SELECT ID FROM TEST_TABLE ORDER BY ID")));
        }
    }

    private static Connection fixtureDatabase() throws SQLException
    {
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:" + java.util.UUID.randomUUID());
        try (var statement = connection.createStatement())
        {
            statement.execute("CREATE TABLE TEST_TABLE (ID INTEGER PRIMARY KEY, NAME VARCHAR(30))");
            statement.execute("INSERT INTO TEST_TABLE VALUES (10, 'first'), (15, 'middle'), (20, 'last')");
        }
        return connection;
    }

    private static List<Object> ids(List<EJSelectResult> rows)
    {
        return rows.stream().map(row -> row.getItemValue("ID")).toList();
    }

    private static EJFrameworkConnection frameworkConnection(Object connectionObject)
    {
        return new EJFrameworkConnection()
        {
            @Override
            public Object getConnectionObject()
            {
                return connectionObject;
            }

            @Override
            public void commit()
            {
            }

            @Override
            public void rollback()
            {
            }

            @Override
            public void close()
            {
            }
        };
    }

    private static SqlCapture sqlCapture()
    {
        AtomicReference<String> sql = new AtomicReference<String>();
        Map<Integer, Object> values = new TreeMap<Integer, Object>();
        ResultSetMetaData metadata = proxy(ResultSetMetaData.class,
                (proxy, method, args) -> defaultValue(method.getReturnType()));
        ResultSet resultSet = proxy(ResultSet.class, (proxy, method, args) -> {
            if (method.getName().equals("getMetaData"))
            {
                return metadata;
            }
            return defaultValue(method.getReturnType());
        });
        PreparedStatement statement = proxy(PreparedStatement.class, (proxy, method, args) -> {
            if (method.getName().equals("setObject"))
            {
                values.put((Integer) args[0], args[1]);
            }
            else if (method.getName().equals("setNull"))
            {
                values.put((Integer) args[0], null);
            }
            else if (method.getName().equals("executeQuery"))
            {
                return resultSet;
            }
            else if (method.getName().equals("executeUpdate"))
            {
                return 1;
            }
            return defaultValue(method.getReturnType());
        });
        Connection connection = proxy(Connection.class, (proxy, method, args) -> {
            if (method.getName().equals("prepareStatement"))
            {
                sql.set((String) args[0]);
                return statement;
            }
            return defaultValue(method.getReturnType());
        });
        return new SqlCapture(connection, sql, values);
    }

    private static String normalize(String sql)
    {
        return sql.trim().replaceAll("\\s+", " ");
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, java.lang.reflect.InvocationHandler handler)
    {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, handler);
    }

    private static Object defaultValue(Class<?> type)
    {
        if (!type.isPrimitive())
        {
            return null;
        }
        if (type == boolean.class)
        {
            return false;
        }
        if (type == char.class)
        {
            return '\0';
        }
        return 0;
    }

    private record SqlCapture(Connection connection, AtomicReference<String> statement, Map<Integer, Object> parameters)
    {
        String sql()
        {
            return statement.get();
        }

        List<Object> values()
        {
            return new ArrayList<Object>(parameters.values());
        }
    }
}
