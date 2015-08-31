package org.entirej.framework.core;

import static org.junit.Assert.*;

import org.entirej.framework.core.internal.EJDefaultServicePojoHelper;
import org.junit.Test;

public class TestEJDefaultServicePojoHelper
{

    @Test
    public void testCreateNewPojoFromService() throws Exception
    {
        Class<?> pojoFromService = EJDefaultServicePojoHelper.getPojoFromService(AAbstractBlockService.class);
        assertEquals(pojoFromService, APojo.class);

    }

    @Test
    public void testCreateNewPojoFromServiceSubClass() throws Exception
    {

        Class<?> pojoFromService = EJDefaultServicePojoHelper.getPojoFromService(ABlockService.class);
        assertEquals(pojoFromService, APojo.class);

    }

    @Test
    public void testCreateNewPojoFromServiceIncorrect() throws Exception
    {

        try
        {
            EJDefaultServicePojoHelper.getPojoFromService(APojo.class);
            assertTrue(false);

        }
        catch (EJApplicationException eg)
        {
            assertTrue(true);
        }

    }
}
