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
package org.entirej.framework.core.properties;

import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJLovDefinitionProperties;

public class EJCoreLovDefinitionProperties implements Comparable<EJCoreLovDefinitionProperties>, EJLovDefinitionProperties
{
    private String                         _name;
    private String                         _referencedLovDefinitionName = "";
    private String                         _actionProcessorClassName;
    private boolean                        _isReferenced                = false;
    private boolean                        _queryAllowed                = true;
    private boolean                        _automaticQuery              = false;
    private boolean                        _automaticRefresh            = true;
    private int                            _width;
    private int                            _height;

    private String                         _lovRendererName             = "";
    private EJFrameworkExtensionProperties _lovRendererProperties;

    private EJCoreBlockProperties          _blockProperties;

    public EJCoreLovDefinitionProperties(String name)
    {
        _name = name;
    }

    /**
     * Sets the name of this Lov Definition
     * <p>
     * The method should only be used within the EntireJ Plugin internal code
     * when renaming the definition
     * 
     * @param name
     *            The new name of this definition
     */
    public void internalSetName(String name)
    {
        _name = name;
    }

    /**
     * Returns the name of this LovDefinition
     * 
     * @return
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Sets the name of the referenced lov definition name
     * 
     * @param referencedName
     *            The reference name
     */
    public void setReferencedLovDefinitionName(String referencedName)
    {
        _referencedLovDefinitionName = referencedName;
    }

    /**
     * Returns the name of the lov definition upon which this definition is
     * based
     * 
     * @return The name of the referenced lov definition name
     */
    public String getReferencedLovDefinitionName()
    {
        return _referencedLovDefinitionName;
    }

    /**
     * The Action Processor is responsible for actions within this lov
     * <p>
     * Actions can include buttons being pressed, check boxes being selected or
     * pre-post query methods etc.
     * 
     * @return The name of the Action Processor responsible for this lov
     */
    public String getActionProcessorClassName()
    {
        return _actionProcessorClassName;
    }

    /**
     * Sets the action processor name for this lov
     * <p>
     * If no action processor is specified for this block, then all action
     * methods will be propogated to the form level action processor
     * 
     * @param processorName
     *            The action processor name for this lov
     */
    public void setActionProcessorClassName(String processorName)
    {
        _actionProcessorClassName = processorName;
    }

    /**
     * Indicates if this LovDefinition references a re-usable definition
     * 
     * @return <code>true</code> if references otherwise <code>false</code>
     */
    public boolean isReferenced()
    {
        return _isReferenced;
    }

    /**
     * If set to <code>true</code> then the lov definition can use the blocks
     * defined query option
     * <p>
     * This could include showing a query screen for hte user to reduce the
     * number of records within the lov thus simplifying the selection of the
     * correct value
     * 
     * @return true if query operations are allowed otherwise false
     */
    public boolean isUserQueryAllowed()
    {
        return _queryAllowed;
    }

    /**
     * Sets the query flag for this LovDefinition
     * 
     * @param queryAllowed
     *            <code>true</code> if query are allowed, <code>false</code> if
     *            they are not
     * @see #isQueryAllowed()
     */
    public void setUserQueryAllowed(boolean queryAllowed)
    {
        _queryAllowed = queryAllowed;
    }

    /**
     * If automatic query is set to true, then the LovDefinition will
     * automatically make a query before the values are shown to the user. If
     * the automatic query is set to false, then an empty screen will be shown
     * and the user must execute a query or use the query screen to display the
     * lov values
     * 
     * @return true if an automatic query will be executed, otherwise false
     */
    public boolean executeAutomaticQuery()
    {
        return _automaticQuery;
    }

    /**
     * Sets the automatic query flag for this LovDefinition
     * 
     * @param automaticQuery
     *            <code>true</code> if an automatic query should be executed
     *            <code>false</code> if it should not
     * @see #automaticAllowed()
     */
    public void setAutomaticQuery(boolean automaticQuery)
    {
        _automaticQuery = automaticQuery;
    }

    /**
     * If automatic refresh has been set, then the LOV Values will be cleared
     * and re-queried each time the LOV is opened, if set to false, then the
     * values will be kept once an initial query has been made
     * 
     * @return true if an automatic refresh has been set, otherwise false
     */
    public boolean refreshAutomatically()
    {
        return _automaticRefresh;
    }

    /**
     * Sets the automatic refresh flag for this LovDefinition
     * 
     * @param automaticRefresh
     *            <code>true</code> if the lov data will be cleared each time the lov is used
     *            <code>false</code> if it should not
     * @see #refreshAutomatically()
     */
    public void setAutomaticRefresh(boolean automaticRefresh)
    {
        _automaticRefresh = automaticRefresh;
    }

    /**
     * Sets the height of the renderer used to display this LovDefinition
     * 
     * @param height
     *            The height, defined in pixels
     */
    public void setHeight(int height)
    {
        _height = height;
    }

    /**
     * Returns the height of renderer used to display this LovDefinition
     * 
     * @return The height, in pixels, that the renderer will use to display this
     *         LovDefinition
     */
    public int getHeight()
    {
        return _height;
    }

    /**
     * Sets the width of the renderer used to display this LovDefinition
     * 
     * @param width
     *            The width, defined in pixels
     */
    public void setWidth(int width)
    {
        _width = width;
    }

    /**
     * Returns the width of renderer used to display this LovDefinition
     * 
     * @return The width, in pixels, that the renderer will use to display this
     *         LovDefinition
     */
    public int getWidth()
    {
        return _width;
    }

    /**
     * The name of the renderer used for the display of this lov
     * <p>
     * All renderers are defined within the <b>EntireJ Properties</b>
     * 
     * @return The renderer for this lov
     */
    public String getLovRendererName()
    {
        return _lovRendererName;
    }

    /**
     * Sets the rendering properties for this lov definition
     * <p>
     * These properties are not used internally within the EntireJ Core
     * Framework but can be used by the applications rendering engine to display
     * the lov
     * <p>
     * 
     * @param renderingProperties
     *            The rendering properties for this lov definition
     */
    public void setLovRendererProperties(EJFrameworkExtensionProperties renderingProperties)
    {
        _lovRendererProperties = renderingProperties;
    }

    /**
     * Returns the rendering properties for this lov definition
     * <p>
     * These properties are not used internally within the EntireJ Core
     * Framework but can be used by the applications rendering engine to display
     * the lov
     * <p>
     * 
     * @return The lov rendering properties
     */
    public EJFrameworkExtensionProperties getLovRendererProperties()
    {
        return _lovRendererProperties;
    }

    /**
     * Sets the name of the lov renderer that is responsible for displaying this
     * lov
     * 
     * @param lovRendererName
     *            the name of the lov Renderer
     */
    public void setLovRendererName(String lovRendererName)
    {
        _lovRendererName = lovRendererName;
    }

    /**
     * Sets the BlockProperties of the block that will contain the data for this
     * lov definition
     * 
     * @param blockProperties
     *            The properties of the block for this lov definition
     */
    public void setBlockProperties(EJCoreBlockProperties blockProperties)
    {
        _blockProperties = blockProperties;
        if (_blockProperties != null)
        {
            _blockProperties.setName(getName());
        }
    }

    /**
     * Returns the <code>BlockProperties</code> for this lov definition
     * 
     * @return the lov definition block properties
     */
    public EJCoreBlockProperties getBlockProperties()
    {
        return _blockProperties;
    }

    public int compareTo(EJCoreLovDefinitionProperties def)
    {
        return this.getName().compareTo(def.getName());
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("LovDefinitionProperties: ");
        buffer.append(_name);
        buffer.append("\n  ReferencedLovDefinitionName: ");
        buffer.append(_referencedLovDefinitionName);
        buffer.append("\n  isReferenced: ");
        buffer.append(_isReferenced);
        buffer.append("\n  queryAllowed: ");
        buffer.append(_queryAllowed);
        buffer.append("\n  automaticQuery: ");
        buffer.append(_automaticQuery);
        buffer.append("\n  automaticRefresh: ");
        buffer.append(_automaticRefresh);
        buffer.append("\n  lovRendererName: ");
        buffer.append(_lovRendererName);
        buffer.append("\n  rendererProperties:\n");
        buffer.append(_lovRendererProperties);
        buffer.append("\n  blockProperties:\n");
        buffer.append(_blockProperties);

        return buffer.toString();
    }
}
