/*******************************************************************************
 * Copyright 2013 Mojave Innovations GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors: Mojave Innovations GmbH - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;

import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.actionprocessor.EJDefaultFormActionProcessor;

/**
 * 
 */
public class EJFileUpload implements Serializable
{
    private String   _id;
    private EJForm   _form;
    private String   _title = "";
    private boolean  _multiSelection;
    private String[] _files;

    public EJFileUpload(EJForm form, String id)
    {
        _form = form;
        _id = id;
    }

    public EJFileUpload(EJForm form, String id, String title)
    {
        _form = form;
        _id = id;
        setTitle(title);
    }

    public EJFileUpload(EJForm form, String id, String title, boolean multiSelection)
    {
        _form = form;
        _id = id;
        setTitle(title);
        this._multiSelection = multiSelection;
    }

    public String getId()
    {
        return _id;
    }

    /**
     * Returns the form that the
     * <code>{@link EJDefaultFormActionProcessor}</code> will need
     * 
     * @return The form that issued the file upload
     */
    public EJForm getForm()
    {
        return _form;
    }

    /**
     * Sets the title of the file upload
     * 
     * @param title
     *            The title of the file upload
     */
    public void setTitle(String title)
    {
        if (title != null)
        {
            _title = _form.translateText(title);
        }
        else
        {
            _title = title;
        }
    }

    public boolean isMultiSelection()
    {
        return _multiSelection;
    }

    public void setMultiSelection(boolean multiSelection)
    {
        this._multiSelection = multiSelection;
    }

    /**
     * Returns the title of this file upload
     * 
     * @return This file upload title
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * The action controller that must be called after the file upload has been
     * answered
     * 
     * @return The action controller to call
     */
    public EJActionController getActionProcessor()
    {
        return _form.getActionController().getUnmanagedController();
    }

    public void setFiles(String[] files)
    {
        this._files = files;
    }

    public String[] getFiles()
    {
        return _files;
    }
}
