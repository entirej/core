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

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJMessage;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.EJParameterList;
import org.entirej.framework.core.data.EJDataForm;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.interfaces.EJMessenger;
import org.entirej.framework.core.properties.EJCoreFormProperties;

public class EJFormControllerFactory implements Serializable
{
    private EJFrameworkManager _frameworkManager;
    
    public EJFormControllerFactory(EJFrameworkManager frameworkManager)
    {
        _frameworkManager = frameworkManager;
    }
    
    public EJFormController createController(String formName, EJParameterList parameterList)
    {
        if (formName == null)
        {
            throw new EJApplicationException("The formName passed to createForm is null");
        }
        if (formName.trim().length() == 0)
        {
            throw new EJApplicationException("Invalid form name passed to createForm. FormName: ''");
        }
        
        EJCoreFormProperties formProperties = getFormProperties(formName);
        
        return createController(formProperties, parameterList);
    }
    
    /**
     * Returns the form properties for the named form
     * 
     * @param formName
     *            - The form name
     * @return a {@link EJCoreFormProperties} object containing the translated
     *         from properties
     */
    public EJCoreFormProperties getFormProperties(String formName)
    {
        EJCoreFormProperties formProperties = null;
        
        formProperties = _frameworkManager.getFormPropertiesFactory().createFormProperties(formName);
        if (formProperties != null)
        {
            _frameworkManager.getTranslationController().translateForm(formProperties, _frameworkManager);
        }
        return formProperties;
    }
    
    /**
     * Creates a <code>FormController</code> with the given name and
     * <code>IMessenger</code>
     * 
     * @param formProperties
     *            The properties of the form to create
     * @param parameterList
     *            The list of properties for the form
     * @return The controller of the newly created form
     */
    public EJFormController createController(EJCoreFormProperties formProperties, EJParameterList parameterList)
    {
        if (formProperties == null)
        {
            EJMessage message = EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_FORM_CONTROLLER_PASSED_TO_FORM_PROPS);
            throw new EJApplicationException(message);
        }
        
        EJDataForm dataForm = new EJDataForm(formProperties);
        
        // Create the controller and add the messenger to it
        EJMessenger messenger = _frameworkManager.getApplicationManager().getApplicationMessenger();
        EJFormController formController = new EJFormController(_frameworkManager, dataForm, messenger);
        
        // Now copy parameter values from the parameter list given to the
        // forms parameter list
        if (parameterList != null)
        {
            for (EJFormParameter parameter : parameterList.getAllParameters())
            {
                if (formController.getParameterList().contains(parameter.getName()))
                {
                    formController.getParameterList().getParameter(parameter.getName()).setValue(parameter.getValue());
                }
                else if (parameter.getValue() != null)
                {
                    EJFormParameter param = new EJFormParameter(parameter.getName(), parameter.getValue().getClass());
                    param.setValue(parameter.getValue());
                    formController.getParameterList().addParameter(param);
                }
            }
        }
        return formController;
        
    }
}
