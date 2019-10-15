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
package org.entirej.framework.core;

import java.util.HashMap;
import java.util.Locale;

import org.entirej.framework.core.data.controllers.EJApplicationLevelParameter;
import org.entirej.framework.core.data.controllers.EJEmbeddedFormController;
import org.entirej.framework.core.data.controllers.EJFileUpload;
import org.entirej.framework.core.data.controllers.EJFormController;
import org.entirej.framework.core.data.controllers.EJFormControllerFactory;
import org.entirej.framework.core.data.controllers.EJInternalQuestion;
import org.entirej.framework.core.data.controllers.EJPopupFormController;
import org.entirej.framework.core.data.controllers.EJQuestion;
import org.entirej.framework.core.data.controllers.EJTranslationController;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.interfaces.EJApplicationManager;
import org.entirej.framework.core.interfaces.EJMessenger;
import org.entirej.framework.core.internal.EJInternalForm;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreProperties;
import org.entirej.framework.core.properties.factory.EJCoreFormPropertiesFactory;
import org.entirej.framework.core.properties.factory.EJCorePropertiesFactory;
import org.entirej.framework.core.properties.interfaces.EJFormPropertiesFactory;
import org.entirej.framework.core.properties.reader.EJCorePropertiesHandlerFactory;
import org.entirej.framework.core.service.EJBlockServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EJFrameworkManager implements EJMessenger, EJFrameworkHelper
{
    private final Logger                                 LOGGER         = LoggerFactory.getLogger(this.getClass());

    private EJConnectionRetriever                        _connectionRetriever;
    private EJSystemConnectionRetriever                  _systemConnectionRetriever;
    private Locale                                       _currentLocale = Locale.ENGLISH;
    private EJCorePropertiesHandlerFactory               _handlerFactory;
    private EJFormPropertiesFactory                      _formPropertiesFactory;
    private EJFormControllerFactory                      _formControllerFactory;
    private EJTranslationController                      _translationController;
    private EJApplicationManager                         _applicationManager;
    private EJBlockServiceFactory                        _blockServiceFactory;

    private HashMap<String, EJApplicationLevelParameter> _applicationLevelParameters;

    protected EJFrameworkManager(String entireJPropertiesFileName)
    {

        _applicationLevelParameters = new HashMap<String, EJApplicationLevelParameter>();
        _formPropertiesFactory = createFormPropertiesFactory();
        _formControllerFactory = createFormControllerFactory();
        _handlerFactory = createPropertiesHandlerFactory();
        _blockServiceFactory = createBlockServiceFactory();

        initialiseCore(entireJPropertiesFileName);
    }

    public synchronized EJManagedFrameworkConnection getConnection()
    {
        if (_connectionRetriever == null || _connectionRetriever.isClosed())
        {
            _connectionRetriever = new EJConnectionRetriever(this);
            return new EJManagedFrameworkConnection(_connectionRetriever, true);
        }
        else
        {
            return new EJManagedFrameworkConnection(_connectionRetriever, false);
        }

    }
    public synchronized EJManagedFrameworkConnection getSystemConnection()
    {
        if (_systemConnectionRetriever == null || _systemConnectionRetriever.isClosed())
        {
            _systemConnectionRetriever = new EJSystemConnectionRetriever(this);
            return new EJManagedFrameworkConnection(_systemConnectionRetriever, true);
        }
        else
        {
            return new EJManagedFrameworkConnection(_systemConnectionRetriever, false);
        }
        
    }

    protected EJFormPropertiesFactory createFormPropertiesFactory()
    {
        return new EJCoreFormPropertiesFactory(this);
    }

    protected EJFormControllerFactory createFormControllerFactory()
    {
        return new EJFormControllerFactory(this);
    }

    protected EJCorePropertiesHandlerFactory createPropertiesHandlerFactory()
    {
        return new EJCorePropertiesHandlerFactory(this);
    }

    protected EJBlockServiceFactory createBlockServiceFactory()
    {
        return new EJBlockServiceFactory();
    }

    public EJCorePropertiesHandlerFactory getHandlerFactory()
    {
        return _handlerFactory;
    }

    public EJBlockServiceFactory getBlockServiceFactory()
    {
        return _blockServiceFactory;
    }

    /**
     * Called once from the Application Renderer when the core framework must be
     * initialized
     * <p>
     * The EntireJProperties file passed as a parameter will be read and the
     * properties added to {@link EJCoreProperties}. EntireJ Core Framework will
     * automatically cache all forms, lookups and color properties.
     * <p>
     * 
     * @param eintireJPropertiesFileName
     *            The fully qualified name of the
     *            <code>EntireJ.properties</code> file.
     * @throws EJApplicationException
     *             If there was an error initializing the Framework
     */
    private void initialiseCore(String entireJPropertiesFileName)
    {
        if (entireJPropertiesFileName == null || entireJPropertiesFileName.trim().length() == 0)
        {
            throw new EJApplicationException("The entireJProperitesFileName passed to FrameworkInitialise.initialiseCore is either null or of zero length");
        }

        EJCorePropertiesFactory.initialiseEntireJProperties(this, entireJPropertiesFileName);

        _translationController = new EJTranslationController(this, EJCoreProperties.getInstance().getApplicationTranslator(), _currentLocale);

        EJCoreProperties.getInstance().copyApplicationLevelParameters(this);
        setApplicationManagerClassName(EJCoreProperties.getInstance().getApplicationManagerClassName());

    }

    private void setApplicationManagerClassName(String className)
    {
        if (className == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NULL_APPLICATION_MANAGER_PASSED_TO_METHOD,
                    "EntireJCoreProperties.setApplicationManagerClassName"));
        }

        try
        {
            Class<?> rendererClass = Class.forName(className);
            Object obj = rendererClass.newInstance();

            if (obj instanceof EJApplicationManager)
            {
                _applicationManager = (EJApplicationManager) obj;
                _applicationManager.setFrameworkManager(this);
            }
            else
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.INVALID_APPLICATION_MANAGER_NAME));
            }
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_CREATE_APP_MANAGER, className), e);
        }
        catch (InstantiationException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_CREATE_APP_MANAGER, className), e);
        }
        catch (IllegalAccessException e)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_CREATE_APP_MANAGER, className), e);
        }
    }

    /**
     * Returns the framework translator
     * <p>
     * The translation controller can be used to translate texts and messages
     * used within the application. The applications implementation of the
     * translator is defined within the <code>EntireJFramework.properties</code>
     * 
     * @return The frameworks translator
     */
    public EJTranslationController getTranslationController()
    {
        return _translationController;
    }

    /**
     * Returns the framework translator
     * <p>
     * The translation controller can be used to translate texts and messages
     * used within the application. The applications implementation of the
     * translator is defined within the <code>EntireJFramework.properties</code>
     * 
     * @return The frameworks translator
     */
    public EJTranslatorHelper getTranslatorHelper()
    {
        return new EJTranslatorHelper(this);
    }

    /**
     * Changes the {@link Locale} of the application
     * <p>
     * All texts and messages will be translated using the given {@link Locale}.
     * If no other {@link Locale} has been set, then the default of
     * <code>Locale.ENGLISH</code> will be used
     * 
     * @param locale
     *            The new {@link Locale} for the application
     */
    public void changeLocale(Locale locale)
    {
        if (locale != null)
        {
            _currentLocale = locale;
            _translationController.setLocale(_currentLocale);
        }
    }

    /**
     * Returns the {@link Locale} that is currently set for this application
     * <p>
     * The {@link Locale} is used internally within EntireJ and within the
     * applications translator. The application can change the {@link Locale} as
     * required
     * 
     * @return The {@link Locale} specified for this application
     */
    public Locale getCurrentLocale()
    {
        return _currentLocale;
    }

    /**
     * Returns the application manager for this application
     * 
     * @return The application manager
     */
    public EJApplicationManager getApplicationManager()
    {
        return _applicationManager;
    }

    public void askInternalQuestion(EJInternalQuestion question)
    {
        if (_applicationManager != null)
        {
            _applicationManager.askInternalQuestion(question);
        }
        else
        {
            LOGGER.error(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.FRAMEWORK_NOT_INITIALISED).getMessage());
        }
    }

    public void askQuestion(EJQuestion question)
    {
        if (_applicationManager != null)
        {
            _applicationManager.askQuestion(question);
        }
        else
        {
            LOGGER.error(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.FRAMEWORK_NOT_INITIALISED).getMessage());
        }

    }
    
    @Override
    public void uploadFile(EJFileUpload fileUpload)
    {
        if (_applicationManager != null)
        {
            _applicationManager.uploadFile(fileUpload);
        }
        else
        {
            LOGGER.error(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.FRAMEWORK_NOT_INITIALISED).getMessage());
        }
        
    }

    public void handleMessage(EJMessage message)
    {
        if (_applicationManager != null)
        {
            _applicationManager.handleMessage(message);
        }
        else
        {
            LOGGER.error(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.FRAMEWORK_NOT_INITIALISED).getMessage());
        }
    }

    public void handleException(Exception exception)
    {
        handleException(exception, true);
    }

    public void handleException(Exception exception, boolean displayUserMessage)
    {
        if (exception instanceof EJApplicationException)
        {
            if (((EJApplicationException) exception).stopProcessing())
            {
                // If the user has thrown an empty exception then nothing should
                // be done as they only want to stop processing
                return;
            }
        }

        if (_applicationManager != null)
        {
            _applicationManager.handleException(exception, displayUserMessage);
        }
        LOGGER.error(exception.getMessage(), exception);
    }

    /**
     * Creates a <code>Form</code> with the given name
     * 
     * @param formName
     *            The name of the form to create
     * 
     * @return The newly created form
     */
    public EJForm createForm(String formName)
    {
        try
        {
            return createForm(formName, null);
        }
        catch (Exception e)
        {
            handleException(e);
            return null;
        }

    }

    /**
     * Returns the {@link EJForm} object for the given form name
     * <p>
     * returns <code>null</code> if there is no form opened with the given name
     * 
     * @param formName
     *            The name of the form to return
     * @return the {@link EJForm} for the given form
     */
    public EJForm getForm(String formName)
    {
        EJInternalForm form = _applicationManager.getForm(formName);
        if (form == null)
        {
            return null;
        }

        return new EJForm(form);
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
        return _formControllerFactory.getFormProperties(formName);
    }

    /**
     * Used to open a specific form
     * <p>
     * It is possible to pass parameters by adding global parameters to the form
     * controller before calling this method. The form can read these parameters
     * within the <code>whenNewFormInstance</code> action processor method and
     * act accordingly
     * <p>
     * If the form is set to blocking, then the form will be opened in a modal
     * form. This means that the application is blocked until the form is
     * closed. This enables the form to pass values back to the calling form. To
     * do this, the calling form can pass its form manager to the form as a
     * global parameter. The form which is opened can then set global parameters
     * in the calling form
     * 
     * @param form
     *            The form to be opened
     * @param blocking
     *            If set to <code>true</code> then the form will be opened in
     *            blocking/modal mode, otherwise the form will be opened as a
     *            standard form
     */
    public void openForm(EJInternalForm form, boolean blocking)
    {
        // First call the preFormOpened action controller method. This will
        // allow users to stop the opening of the form if they so wish
        form.getActionController().getUnmanagedController().preFormOpened(new EJForm(form));

        getApplicationManager().addFormToContainer(form, blocking);
        
        form.getActionController().getUnmanagedController().postFormOpened(new EJForm(form));
    }
    
    
    public void runReport(String reportName, EJParameterList parameterList){
        getApplicationManager().runReport(reportName, parameterList);
    }

    public void runReport(String reportName){
        getApplicationManager().runReport(reportName);
    }
    public String generateReport(String reportName, EJParameterList parameterList){
        return getApplicationManager().generateReport(reportName, parameterList);
    }
    
    public String generateReport(String reportName){
       return getApplicationManager().generateReport(reportName);
    }
    
    
    @Override
    public void runReportAsync(String reportName)
    {
        runReportAsync(reportName,null,null);
        
    }
    @Override
    public void runReportAsync(String reportName, EJMessage completedMessage)
    {
        runReportAsync(reportName,null,completedMessage);
        
    }
    
    @Override
    public void runReportAsync(String reportName, EJParameterList parameterList)
    {
        runReportAsync(reportName,parameterList,null);
        
    }
    
    @Override
    public void runReportAsync(String reportName, EJParameterList parameterList, EJMessage completedMessage)
    {
        getApplicationManager().runReportAsync(reportName, parameterList, completedMessage);
        
    }
    
    
    @Override
    public EJTabLayoutComponent getTabLayoutComponent(String name)
    {
        return new EJTabLayoutComponent(getApplicationManager(), name);
    }

    /**
     * Informs the application manager to open a popup form
     * <p>
     * A popup form is a normal form that will be opened in a modal window or as
     * part of the current form. The modal form normally has a direct connection
     * to this form and may receive or return values to or from the calling form
     * 
     * @param popupFormController
     *            The controller holding all required values to open the popup
     *            form
     */
    public void openPopupForm(EJPopupFormController popupFormController)
    {
        // First call the preFormOpened action controller method. This will
        // allow users to stop the opening of the form if they so wish
        popupFormController.getPopupForm().getActionController().getUnmanagedController().preFormOpened(new EJForm(popupFormController.getPopupForm()));
        
        getApplicationManager().openPopupForm(popupFormController);
        
        popupFormController.getPopupForm().getActionController().getUnmanagedController().postFormOpened(new EJForm(popupFormController.getPopupForm()));

    }

    /**
     * Informs the application manager to open an embedded form
     * <p>
     * An embedded form is a normal form that will be opened within another form
     * on a form canvas. The embedded form runs as a stand alone form, but using
     * another form as its container
     * 
     * @param embeddedFormController
     *            The controller holding all required values to open the embedded
     *            form
     */
    public void openEmbeddedForm(EJEmbeddedFormController embeddedFormController)
    {
        // First call the preFormOpened action controller method. This will
        // allow users to stop the opening of the form if they so wish
        embeddedFormController.getEmbeddedForm().getActionController().preFormOpened(new EJForm(embeddedFormController.getEmbeddedForm()));

        getApplicationManager().openEmbeddedForm(embeddedFormController);

        embeddedFormController.getEmbeddedForm().getActionController().postFormOpened(new EJForm(embeddedFormController.getEmbeddedForm()));
    }


    /**
     * Informs the application manager to close an embedded form
     * <p>
     * An embedded form is a normal form that will be opened within another form
     * on a form canvas. The embedded form runs as a stand alone form, but using
     * another form as its container
     * 
     * @param embeddedFormController
     *            The controller holding all required values to close the embedded
     *            form
     */
    public void closeEmbeddedForm(EJEmbeddedFormController embeddedFormController)
    {
        EJParameterList paramList = embeddedFormController.getEmbeddedForm().getParameterList();
        
        getApplicationManager().closeEmbeddedForm(embeddedFormController);
        
        embeddedFormController.getEmbeddedForm().getActionController().embeddedFormClosed(paramList);
    }

    
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
    public void openForm(String formName, EJParameterList parameterList, boolean blocking)
    {
        try
        {
            EJInternalForm form = createInternalForm(formName, parameterList);
            openForm(form, blocking);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

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
    public void openForm(String formName, EJParameterList parameterList)
    {
        try
        {
            EJInternalForm form = createInternalForm(formName, parameterList);
            openForm(form, false);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

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
    public void openForm(String formName)
    {
        try
        {
            EJInternalForm form = createInternalForm(formName, null);
            openForm(form, false);
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * Creates a <code>Form</code> with the given name
     * <p>
     * 
     * @param formName
     *            The name of the form to create
     * @param parameterList
     *            A parameter list containing parameters for the form to open.
     * 
     * @return The newly created form
     */
    private EJForm createForm(String formName, EJParameterList parameterList)
    {
        EJFormController controller = _formControllerFactory.createController(formName, parameterList);
        if (controller != null)
        {
            return controller.getEJForm();
        }
        else
        {
            return null;
        }
    }

    /**
     * Creates a <code>EJInternalForm</code> with the given name
     * <p>
     * <b>This method should only used within the Applicaton Framework and not
     * within any of the action processors</b>
     * 
     * @param formName
     *            The name of the form to create
     * @param parameterList
     *            A parameter list containing parameters for the form to open.
     * 
     * @return The newly created form
     */
    public EJInternalForm createInternalForm(String formName, EJParameterList parameterList)
    {
        EJFormController controller = _formControllerFactory.createController(formName, parameterList);
        if (controller != null)
        {
            return controller.getInternalForm();
        }
        else
        {
            return null;
        }
    }

    /**
     * Retrieve the application instance of the
     * <code>FormPropertiesFactory</code>
     * 
     * @return The application instance of the
     *         <code>FormPropertiesFactory</code>
     */
    public EJFormPropertiesFactory getFormPropertiesFactory()
    {
        return _formPropertiesFactory;
    }

    /**
     * Adds an Application Level Parameter to this application
     * 
     * @param parameter
     *            The parameter to add
     */
    public void addApplicationLevelParameter(EJApplicationLevelParameter parameter)
    {
        if (parameter != null)
        {
            _applicationLevelParameters.put(parameter.getName(), parameter);
        }
    }

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
    public void setApplicationLevelParameter(String valueName, Object value)
    {
        EJApplicationLevelParameter parameter = _applicationLevelParameters.get(valueName);

        if (parameter != null)
        {
            parameter.setValue(value);
        }
        else
        {
            throw new EJApplicationException(new EJMessage("Trying to set an application level parameter with the name " + valueName
                    + ", but there is no parameter with this name. All parameters are defiined within the EntireJ.properties file"));
        }
    }

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
    public EJApplicationLevelParameter getApplicationLevelParameter(String valueName)
    {
        if (valueName == null)
        {
            throw new EJApplicationException("Trying to retrieve an application level parameter without specifying a name");
        }

        if (_applicationLevelParameters.containsKey(valueName))
        {
            return _applicationLevelParameters.get(valueName);
        }
        else
        {
            throw new EJApplicationException("Trying to get an application level parameter value with the name " + valueName
                    + ", but there is no parameter with this name. All parameters are defiined within the EntireJ.properties file");
        }
    }

    /**
     * Checks to see if there is a parameter with the specified name
     * <p>
     * If there is a parameter with the given name, then this method will return
     * <code>true</code>, otherwise <code>false</code>
     * 
     * @param parameterName
     *            The parameter name to check for
     * @return <code>true</code> if there is a parameter with the specified
     *         name, otherwise <code>false</code>
     */
    public boolean applicationLevelParameterExists(String parameterName)
    {
        return _applicationLevelParameters.containsKey(parameterName);
    }
}
