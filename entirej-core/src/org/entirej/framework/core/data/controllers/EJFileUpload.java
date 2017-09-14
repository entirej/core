/*******************************************************************************
 * Copyright 2013 CRESOFT AG
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
 * Contributors: CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.actionprocessor.interfaces.EJFormActionProcessor;

public class EJFileUpload implements Serializable
{
    private String             _id;
    private EJForm             _form;
    private String             _title          = "";
    private boolean            _multiSelection = false;
    private Collection<String> _filePaths;

    /**
     * @param form
     *            The form that initiated the file upload
     * @param id
     *            A unique identified used to identify the file upload process
     *            within
     *            {@link EJFormActionProcessor#filesUploaded(EJFileUpload)}
     */
    public EJFileUpload(EJForm form, String id)
    {
        _form = form;
        _id = id;
    }

    /**
     * @param form
     *            The form that initiated the file upload
     * @param id
     *            A unique identified used to identify the file upload process
     *            within
     *            {@link EJFormActionProcessor#filesUploaded(EJFileUpload)}
     * @param title
     *            The title displayed on the file upload dialog
     */
    public EJFileUpload(EJForm form, String id, String title)
    {
        _form = form;
        _id = id;
        setTitle(title);
    }

    /**
     * @param form
     *            The form that initiated the file upload
     * @param id
     *            A unique identified used to identify the file upload process
     *            within
     *            {@link EJFormActionProcessor#filesUploaded(EJFileUpload)}
     * @param title
     *            The title displayed on the file upload dialog
     * @param multiSelection
     *            <code>true</code> if the user can choose multiple files,
     *            otherwise <code>false</code>
     */
    public EJFileUpload(EJForm form, String id, String title, boolean multiSelection)
    {
        _form = form;
        _id = id;
        setTitle(title);
        this._multiSelection = multiSelection;
    }

    /**
     * The ID is set when creating this file upload and used to identify it
     * within {@link EJFormActionProcessor#filesUploaded(EJFileUpload)}
     * 
     * @return The ID of this file upload
     */
    public String getId()
    {
        return _id;
    }

    /**
     * Returns the form that issued the file upload
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

    /**
     * Indicates if the user will be allowed to choose multiple files from the
     * file upload dialog
     * 
     * @return <code>true</code> if multiple files can be selected, otherwise
     *         <code>false</code>
     */
    public boolean isMultiSelection()
    {
        return _multiSelection;
    }

    /**
     * Indicates if the user is allowed to choose multiple files
     * 
     * @param multiSelection
     *            <code>true</code> if the user can choose multiple files,
     *            otherwise <code>false</code>
     */
    public void setMultiSelection(boolean multiSelection)
    {
        this._multiSelection = multiSelection;
    }

    /**
     * Returns the title of this file upload
     * 
     * @return The title of hte file upload dialog
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

    /**
     * Used by the file uploader to set the paths of the files selected by the
     * user
     * 
     * @param filePaths
     *            A collection of file path names selected by the user
     */
    public void setFilePaths(Collection<String> filePaths)
    {
        _filePaths = filePaths;
    }

    /**
     * Returns the path names of the files selected by the user
     * <p>
     * Use the java.io File class using the file paths returned from this
     * method:
     * <p>
     * <code>new java.io.File(String pathname)</code>
     * 
     * @return A collection of file paths selected by the user or an empty
     *         collection if nothing was selected
     */
    public Collection<String> getFilePaths()
    {
        if (_filePaths == null)
        {
            return Collections.emptyList();
        }
        return _filePaths;
    }
}
