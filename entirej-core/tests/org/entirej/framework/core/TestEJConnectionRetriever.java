package org.entirej.framework.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.entirej.framework.core.interfaces.EJFrameworkConnection;
import org.junit.jupiter.api.Test;

public class TestEJConnectionRetriever
{
    @Test
    void commitFailureStillClosesAndReleasesTheConnection()
    {
        EJConnectionRetriever retriever = new EJConnectionRetriever(null);
        AtomicBoolean closed = new AtomicBoolean();
        EJApplicationException commitFailure = new EJApplicationException("commit failed");
        retriever._frameworkConnection = connection(commitFailure, null, closed);

        RuntimeException thrown = assertThrows(RuntimeException.class, retriever::close);

        assertSame(commitFailure, thrown);
        assertTrue(closed.get());
        assertNull(retriever._frameworkConnection);
    }

    @Test
    void closeFailureIsSuppressedWhenCommitAlreadyFailed()
    {
        EJConnectionRetriever retriever = new EJConnectionRetriever(null);
        AtomicBoolean closed = new AtomicBoolean();
        EJApplicationException commitFailure = new EJApplicationException("commit failed");
        EJApplicationException closeFailure = new EJApplicationException("close failed");
        retriever._frameworkConnection = connection(commitFailure, closeFailure, closed);

        RuntimeException thrown = assertThrows(RuntimeException.class, retriever::close);

        assertSame(commitFailure, thrown);
        assertEquals(1, thrown.getSuppressed().length);
        assertSame(closeFailure, thrown.getSuppressed()[0]);
        assertTrue(closed.get());
        assertNull(retriever._frameworkConnection);
    }

    private static EJFrameworkConnection connection(RuntimeException commitFailure, RuntimeException closeFailure, AtomicBoolean closed)
    {
        return new EJFrameworkConnection()
        {
            @Override
            public Object getConnectionObject()
            {
                return null;
            }

            @Override
            public void commit()
            {
                if (commitFailure != null)
                {
                    throw commitFailure;
                }
            }

            @Override
            public void rollback()
            {
            }

            @Override
            public void close()
            {
                closed.set(true);
                if (closeFailure != null)
                {
                    throw closeFailure;
                }
            }
        };
    }
}
