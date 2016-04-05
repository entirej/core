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
package org.entirej.framework.core.renderers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.internal.EJInternalForm;
import org.entirej.framework.core.renderers.interfaces.EJFormRenderer;

public class EJManagedFormRendererWrapper implements Serializable
{
    private EJFrameworkManager _frameworkManager;
    private EJFormRenderer     _renderer;

    public EJManagedFormRendererWrapper(EJFrameworkManager manager, EJFormRenderer renderer)
    {
        _frameworkManager = manager;
        _renderer = renderer;
    }

    public EJFormRenderer getUnmanagedRenderer()
    {
        return _renderer;
    }

    public void formCleared()
    {
        try
        {
            _renderer.formCleared();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    public void formClosed()
    {
        try
        {
            _renderer.formClosed();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    public EJInternalForm getForm()
    {
        try
        {
            return _renderer.getForm();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }

    public void initialiseForm(EJInternalForm form)
    {
        try
        {
            _renderer.initialiseForm(form);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    public void refreshFormRendererProperty(String propertyName)
    {
        try
        {
            _renderer.refreshFormRendererProperty(propertyName);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    public void savePerformed()
    {
        try
        {
            _renderer.savePerformed();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Forces the form to gain initial focus
     * <p>
     * The initial focus of the form should be the first navigable item on the
     * first available canvas. If the first available canvas is a tab page or a
     * stacked page, then the focus will be sent to the item defined by the
     * First navigational block and the First navigational item properties
     * 
     */
    public void gainInitialFocus()
    {
        try
        {
            _renderer.gainInitialFocus();
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    public Object getGuiComponent()
    {
        try
        {
            return _renderer.getGuiComponent();
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }

    private void handleException(Exception e)
    {
        if (e instanceof EJApplicationException)
        {
            _frameworkManager.handleException((EJApplicationException) e);
        }
        else
        {
            _frameworkManager.handleException(new EJApplicationException(e));
        }
    }

    /**
     * Indicates to the form that you want to open the Open File dialog
     * <p>
     * This will inform the Client Framework to open its file browser so that
     * the user can search and choose a file to load
     * 
     * @param title
     *            The title to display on the File Dialog
     * @return The fully qualified path name where the file is stored
     */
    public String promptFileUpload(String title)
    {
        try
        {
            return _renderer.promptFileUpload(title);
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }
    }

    /**
     * Indicates to the form that you want to open the Open File dialog to
     * select multiple files
     * <p>
     * This will inform the Client Framework to open its file browser so that
     * the user can search and choose one or more files to load
     * 
     * @param title
     *            The title to display on the File Dialog
     * @return A list containingThe fully qualified path names of the chosen
     *         files
     */
    public List<String> promptMultipleFileUpload(String title)
    {
        try
        {
            return _renderer.promptMultipleFileUpload(title);
        }
        catch (Exception e)
        {
            handleException(e);
            return new ArrayList<String>();
        }
    }
}
