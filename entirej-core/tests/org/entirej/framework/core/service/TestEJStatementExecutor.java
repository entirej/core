package org.entirej.framework.core.service;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

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
}
