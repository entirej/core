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

public interface EJFrameworkHelper extends EJMessenger , Serializable
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
    
    public EJTranslatorHelper getTranslatorHelper();
}
