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
package org.entirej.framework.core.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;

/**
 * The <code>DataForm</code> is the actual class that will hold the forms data.
 * The <code>DataForm</code> contains <code>DataBlock</code>s which contain the
 * data per block. The data blocks contain <code>DataRecord</code>s and these
 * records hold </code>DataItems</code>.
 * <p>
 * If you would compare the data classes to a database then the form would be
 * the actual application the block would represent a database table, the data
 * records would represent the records of the table and the data items would
 * represent the actual table columns.
 */
public class EJDataForm implements Serializable
{
    private  EJCoreFormProperties             _formProperties;
    private HashMap<String, EJDataBlock> _dataBlocks;

    /**
     * Creates an instance of a <code>DataForm</code> using the form properties
     * given.
     *
     * @param formProperties
     *            The properties that will be used to initialize this data form
     */
    public EJDataForm(EJCoreFormProperties formProperties)
    {
        _formProperties = formProperties;
        _dataBlocks = new HashMap<String, EJDataBlock>();

        initialiseDataForm(formProperties);
    }

    public EJCoreFormProperties getProperties()
    {
        return _formProperties;
    }

    /**
     * Clears the block of all records and returns the block to its initial
     * state. Any changes that have been made to the blocks records will be
     * lost.
     *
     * @param blockName
     *            The name of the block to clear. If the name of the block
     *            doesn't exist then nothing will be done
     * @param clearChanges
     *            Indicates if the dirty records contained within this block
     *            should also be cleared
     */
    public void clearBlock(String blockName, boolean clearChanges)
    {
        if (blockName == null || blockName.trim().length() == 0)
        {
            return;
        }
        EJDataBlock block = getBlock(blockName);
        if (block != null)
        {
            block.clearBlock(clearChanges);
        }
    }

    /**
     * Initializes this form object using the properties within the given
     * <code>EJCoreFormProperties</code> object
     *
     * @param formProps
     *            The form properties object
     */
    private void initialiseDataForm(EJCoreFormProperties formProperties)
    {
        for (EJCoreBlockProperties blockProperties : formProperties.getBlockContainer().getAllBlockProperties())
        {
            EJDataBlock block = new EJDataBlock(blockProperties);
            _dataBlocks.put(blockProperties.getName(), block);
        }
    }

    /**
     * Retrieve a collection of this forms data blocks.
     *
     * @return A <code>Collection</code> containing the list of blocks that this
     *         form contains.
     */
    public Collection<EJDataBlock> getAllBlocks()
    {
        return _dataBlocks.values();
    }

    /**
     * Return the block with the specified name, if there is no block with the
     * given name or the name is null, then <code><b>null</b></code> will be
     * returned
     *
     * @param blockName
     *            The name of the required block
     *
     * @return The required block or <code>null</code> if there is no block with
     *         the given name
     */
    public EJDataBlock getBlock(String blockName)
    {
        if (blockName == null || blockName.trim().length() == 0)
        {
            return null;
        }
        if (_dataBlocks == null)
        {
            return null;
        }
        return (EJDataBlock) _dataBlocks.get(blockName);
    }
}
