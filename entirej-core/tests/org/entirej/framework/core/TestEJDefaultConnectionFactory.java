package org.entirej.framework.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Test;

public class TestEJDefaultConnectionFactory
{
    @Test
    void configuredConnectionIsReturnedWithoutClosing()
            throws SQLException
    {
        AtomicBoolean autoCommitDisabled = new AtomicBoolean();
        AtomicBoolean closed = new AtomicBoolean();
        Connection connection = connection((proxy, method, args) -> {
            if (method.getName().equals("setAutoCommit"))
            {
                autoCommitDisabled.set(Boolean.FALSE.equals(args[0]));
            }
            else if (method.getName().equals("close"))
            {
                closed.set(true);
            }
            return defaultValue(method.getReturnType());
        });

        assertSame(connection, EJDefaultConnectionFactory.configureConnection(connection));
        assertTrue(autoCommitDisabled.get());
        assertEquals(false, closed.get());
    }

    @Test
    void connectionIsClosedWhenAutoCommitConfigurationFails()
    {
        AtomicBoolean closed = new AtomicBoolean();
        SQLException configurationFailure = new SQLException("autocommit failed");
        Connection connection = connection((proxy, method, args) -> {
            if (method.getName().equals("setAutoCommit"))
            {
                throw configurationFailure;
            }
            if (method.getName().equals("close"))
            {
                closed.set(true);
            }
            return defaultValue(method.getReturnType());
        });

        assertSame(configurationFailure,
                assertThrows(SQLException.class, () -> EJDefaultConnectionFactory.configureConnection(connection)));
        assertTrue(closed.get());
    }

    @Test
    void closeFailureIsSuppressedByTheConfigurationFailure()
    {
        SQLException configurationFailure = new SQLException("autocommit failed");
        SQLException closeFailure = new SQLException("close failed");
        Connection connection = connection((proxy, method, args) -> {
            if (method.getName().equals("setAutoCommit"))
            {
                throw configurationFailure;
            }
            if (method.getName().equals("close"))
            {
                throw closeFailure;
            }
            return defaultValue(method.getReturnType());
        });

        SQLException failure = assertThrows(SQLException.class,
                () -> EJDefaultConnectionFactory.configureConnection(connection));

        assertSame(configurationFailure, failure);
        assertEquals(1, failure.getSuppressed().length);
        assertSame(closeFailure, failure.getSuppressed()[0]);
    }

    private static Connection connection(java.lang.reflect.InvocationHandler handler)
    {
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class<?>[] { Connection.class }, handler);
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
