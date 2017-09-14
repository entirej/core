/*******************************************************************************
 * Copyright 2013 CRESOFT AG
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
 *     CRESOFT AG - initial API and implementation
 ******************************************************************************/
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;

import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJParameterList;
import org.entirej.framework.core.internal.EJInternalForm;

public class EJPopupFormController implements Serializable
{
    private EJFormController _parentForm;
    private EJFormController _modalForm;
    
    public EJPopupFormController(EJFrameworkManager frameworkManager, EJFormController parentForm, String modalFormName, EJParameterList parameterList)
    {
        _parentForm = parentForm;
        
        EJFormControllerFactory factory = new EJFormControllerFactory(frameworkManager);
        _modalForm = factory.createController(modalFormName, parameterList);
    }
    
    public EJInternalForm getCallingForm()
    {
        return _parentForm.getInternalForm();
    }
    
    public EJInternalForm getPopupForm()
    {
        return _modalForm.getInternalForm();
    }
    
    public void closePopupForm()
    {
        _parentForm.getInternalForm().popupFormClosed(_modalForm.getInternalForm().getParameterList());
    }
    
}
