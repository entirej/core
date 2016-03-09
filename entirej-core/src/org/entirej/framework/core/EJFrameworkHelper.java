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
package org.entirej.framework.core;

import java.io.Serializable;
import java.util.Locale;

import org.entirej.framework.core.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.core.interfaces.EJMessenger;

public interface EJFrameworkHelper extends EJMessenger, Serializable
{
    public EJManagedFrameworkConnection getConnection();

    /**
     * Retrieves a global value with the given name
     * <p>
     * If there is no parameter with the given name then an exception will be
     * thrown
     * 
     * @param valueName
     *            The name of the required global value
     * @return The application level parameter
     * @throws EJApplicationException
     *             If there is no application level parameter with the given
     *             name
     */
    public EJApplicationLevelParameter getApplicationLevelParameter(String valueName);

    /**
     * Used to set an application level parameter
     * <p>
     * A application level parameter has been defined within the EntireJ
     * Properties
     * 
     * value is not used by the EntireJFramework but allows the user to store
     * values within the form which can be retrieved when needed
     * 
     * @param valueName
     *            The name of the value
     * @param value
     *            The value
     */
    public void setApplicationLevelParameter(String valueName, Object value);

    /**
     * Returns the {@link Locale} that is currently set for this application
     * <p>
     * The {@link Locale} is used internally within EntireJ and within the
     * applications translator. The application can change the {@link Locale} as
     * required
     * 
     * @return The {@link Locale} specified for this application
     */
    public Locale getCurrentLocale();

    /**
     * Used to set the current locale of the application
     * <p>
     * EntireJ stores a locale that is used by various item renderers for
     * example the NumberItemRenderer. It is used for the formatting of the
     * number etc. The default for the locale is {@link Locale.ENGLISH} but can
     * be changed via this method
     * 
     * @param locale
     *            The locale to use for this application
     */
    public void changeLocale(Locale locale);

    /**
     * Used to open the form with the given name
     * <p>
     * If the form is set to blocking, then the form will be opened in a modal
     * form. This means that the application is blocked until the form is
     * closed. This enables the form to pass values back to the calling form. To
     * do this, the calling form can pass its form manager to the form as a
     * global parameter. The form which is opened can then set global parameters
     * 
     * in the calling form
     * 
     * @param formName
     *            The name of the form to be opened
     * @param parameterList
     *            An optional list of parameters to be passed to the form being
     *            opened
     * @param blocking
     *            Indicates if the form should be opened in blocking or non
     *            blocking mode
     */
    public void openForm(String formName, EJParameterList parameterList, boolean blocking);

    /**
     * Used to open the form with the given name
     * <p>
     * If the form is set to blocking, then the form will be opened in a modal
     * form. This means that the application is blocked until the form is
     * closed. This enables the form to pass values back to the calling form. To
     * do this, the calling form can pass its form manager to the form as a
     * global parameter. The form which is opened can then set global parameters
     * 
     * in the calling form
     * 
     * @param formName
     *            The name of the form to be opened
     * @param parameterList
     *            An optional list of parameters to be passed to the form being
     *            opened
     */
    public void openForm(String formName, EJParameterList parameterList);

    /**
     * Used to open the form with the given name
     * <p>
     * If the form is set to blocking, then the form will be opened in a modal
     * form. This means that the application is blocked until the form is
     * closed. This enables the form to pass values back to the calling form. To
     * do this, the calling form can pass its form manager to the form as a
     * global parameter. The form which is opened can then set global parameters
     * 
     * in the calling form
     * 
     * @param formName
     *            The name of the form to be opened
     */
    public void openForm(String formName);

    public void runReport(String reportName, EJParameterList parameterList);

    public void runReport(String reportName);
    
    public String generateReport(String reportName, EJParameterList parameterList);
    
    public String generateReport(String reportName);
    
    
    public void runReportAsync(String reportName, EJParameterList parameterList);
    
    public void runReportAsync(String reportName, EJParameterList parameterList,EJMessage completedMessage);

    public void runReportAsync(String reportName,EJMessage completedMessage);
    
    public void runReportAsync(String reportName);

    public EJTranslatorHelper getTranslatorHelper();
}
