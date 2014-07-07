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

import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.enumerations.EJMessageLevel;

public class EJMessageFactory implements Serializable
{
    private static final EJMessageFactory _instance;
    
    static
    {
        _instance = new EJMessageFactory();
    }
    
    public static EJMessageFactory getInstance()
    {
        return _instance;
    }
    
    private EJMessageFactory()
    {
        
    }
    
    public EJMessage createMessage(EJFrameworkMessage messageCode)
    {
        return createMessage(messageCode, "");
    }
    
    public EJMessage createMessage(EJFrameworkMessage messageCode, String... replacementStrings)
    {
        switch (messageCode)
        {
        
            case FRAMEWORK_NOT_INITIALISED:
                return new EJMessage("Application has not yet been initialised");
            case ASK_TO_SAVE_CHANGES:
                return new EJMessage("Do you want to save the changes you have made?");
            case DATA_CHANGED_BY_ANOTHER_USER:
                return new EJMessage(EJMessageLevel.WARNING, "The data has been either modified or deleted by another user, re-query to see changes");
                
            case VALUE_CANNOT_BE_CONVERTED_TO_DOUBLE:
                return new EJMessage("The value for the parameter $1 cannot be converted to an double. Value: $2", replacementStrings);
            case VALUE_CANNOT_BE_CONVERTED_TO_BOOLEAN:
                return new EJMessage("The value for the parameter $1 cannot be converted to boolean. Value: $2", replacementStrings);
            case VALUE_CANNOT_BE_CONVERTED_TO_INT:
                return new EJMessage("The value for the parameter $1 cannot be converted to an int. Value: $2", replacementStrings);
            case VALUE_CANNOT_BE_CONVERTED_TO_FLOAT:
                return new EJMessage("The value for the parameter $1 cannot be converted to a float. Value: $2", replacementStrings);
                
            case NULL_PROPERTY_NAMES_PASSED_TO_METHOD:
                return new EJMessage("The property name passed to $1 is null", replacementStrings);
            case NULL_CANVAS_NAME_PASSED_TO_METHOD:
                return new EJMessage("The canvas name passed to $1 is null", replacementStrings);
            case NULL_CANVAS_PAGE_NAME_PASSED_TO_METHOD:
                return new EJMessage("The page name passed to $1 is null", replacementStrings);
            case NULL_FORM_NAME_PASSED_TO_METHOD:
                return new EJMessage("The form name passed to $1 is null", replacementStrings);
            case NULL_LOVDEF_PROPERTIES_PASSED_TO_METHOD:
                return new EJMessage("The lov definition properties passed to $1 is null", replacementStrings);
            case NULL_RECORD_PASSED_TO_METHOD:
                return new EJMessage("The record passed to $1 is null", replacementStrings);
            case NULL_FORM_CONTROLLER_PASSED_TO_FORM_PROPS:
                return new EJMessage("The FormProperties passed to FormController is null");
            case NULL_QUERY_RECORD_PASSED_TO_METHOD:
                return new EJMessage("The query record passed to $1 is null", replacementStrings);
            case NULL_TRANSACTION_PASSED_TO_METHOD:
                return new EJMessage("The transaction passed to $1 is null", replacementStrings);
            case NULL_FORM_PROPERTIES_PASSED_TO_METHOD:
                return new EJMessage("The form properties passed to $1 is null", replacementStrings);
            case NULL_MENU_PROPERTIES_PASSED_TO_METHOD:
                return new EJMessage("The menu properties passed to $1 is null", replacementStrings);
            case NULL_BLOCK_PROPERTIES_PASSED_TO_METHOD:
                return new EJMessage("The block properties passed to $1 is null", replacementStrings);
            case NULL_PROCESSOR_NAME_PASSED_TO_METHOD:
                return new EJMessage("The processor name passed to $1 is null", replacementStrings);
            case NULL_BLOCK_NAME_PASSED_TO_METHOD:
                return new EJMessage("The block name passed to $1 is null", replacementStrings);
            case NULL_LOV_RENDERER_PASSED_TO_METHOD:
                return new EJMessage("The lov renderer name passed to $1 is null", replacementStrings);
            case NULL_MENU_RENDERER_PASSED_TO_METHOD:
                return new EJMessage("The menu renderer name passed to $1 is null", replacementStrings);
            case NULL_APPLICATION_MANAGER_PASSED_TO_METHOD:
                return new EJMessage("The application manager name passed to $1 is null", replacementStrings);
            case NULL_TRANSLATOR_NAME_PASSED_TO_METHOD:
                return new EJMessage("The translator name passed to $1 is null", replacementStrings);
            case NULL_DATA_FORM_PASSED_TO_FORM_CONTROLLER:
                return new EJMessage("Trying to create a FormController with a null DataForm");
            case NULL_QUERY_CRITERIA_PASSED_TO_METHOD:
                return new EJMessage("The query criteria passed to $1 is null", replacementStrings);
            case NULL_DATA_TYPE_PASSED_TO_METHOD:
                return new EJMessage("The data type passed to $1 is null");
                
            case CANNOT_DELETE_WHILE_CHILDREN_EXIST:
                return new EJMessage("Cannot delete record while child records exist.");
            case CANNOT_PERFORM_QUERY_WITH_NO_SERVICE:
                return new EJMessage("Cannot perform query operation when no data service has been defined. Block: $1", replacementStrings);
            case CANNOT_UPDATE_A_BLOCK_WITH_NO_RECORD:
                return new EJMessage("Cannot update a block when there is no current record. Block: $1", replacementStrings);
                
            case UNABLE_TO_FIND_ITEM_ON_BLOCK:
                return new EJMessage("Unable to find item $1 on block $2", replacementStrings);
            case UNABLE_TO_LOCK_RECORD:
                return new EJMessage("Unable to reserve data for modifications, another user is currently modifying the same data");
            case UNABLE_TO_EXECUTE_METHOD:
                return new EJMessage("Unable to execute method: $1", replacementStrings);
            case UNABLE_TO_FIND_METHOD:
                return new EJMessage("Unable to find method: $1", replacementStrings);
            case UNABLE_TO_RETRIEVE_ACCESS_PROCESSOR_PROPERTIES:
                return new EJMessage("Unable to retrieve the access processor properties for block $1 on form $2", replacementStrings);
            case UNABLE_TO_RETRIEVE_ACCESS_PROCESSOR_PROPERTIES_FOR_ITEM:
                return new EJMessage("Unable to retrieve the access processor properties for item $1 on block $2 on form $3", replacementStrings);
            case UNABLE_TO_RETRIEVE_FORM_RENDERER_PROPERTY:
                return new EJMessage("Unable to retrieve the form renderer properties for form $1", replacementStrings);
            case UNABLE_TO_RETRIEVE_LOV_RENDERER_PROPERTY:
                return new EJMessage("Unable to retrieve the lov renderer properties for lov $1 on form $2", replacementStrings);
            case UNABLE_TO_RETRIEVE_BLOCK_RENDERER_PROPERTY:
                return new EJMessage("Unable to retrieve the block renderer properties for block $1 on form $2", replacementStrings);
            case UNABLE_TO_RETRIEVE_ITEM_RENDERER_PROPERTY:
                return new EJMessage("Unable to retrieve the item renderer properties for item $1 of block $2 on form $3", replacementStrings);
            case UNABLE_TO_RETIEVE_MAIN_SCREEN_ITEM_PROPERTIES:
                return new EJMessage("Unable to retrieve the screen item properties for item $1 of block $1 on form $3", replacementStrings);
            case UNABLE_TO_RETIEVE_QUERY_SCREEN_ITEM_PROPERTIES:
                return new EJMessage("Unable to retrieve the query screen item properties for item $1 of block $2 on form $3", replacementStrings);
            case UNABLE_TO_RETIEVE_INSERT_SCREEN_ITEM_PROPERTIES:
                return new EJMessage("Unable to retrieve the insert screen item properties for item $1 of block $2 on form $3", replacementStrings);
            case UNABLE_TO_RETIEVE_UPDATE_SCREEN_ITEM_PROPERTIES:
                return new EJMessage("Unable to retrieve the update screen item properties for item $1 of block $2 on form $3", replacementStrings);
            case UNABLE_TO_CALL_METHOD:
                return new EJMessage("Unable to call method: $1", replacementStrings);
            case UNABLE_TO_CREATE_ACTION_PROCESSOR:
                return new EJMessage("Unable to create action processor $1", replacementStrings);
            case UNABLE_TO_CREATE_DATA_ACCESS_PROCESSOR:
                return new EJMessage("Unable to create data access processor $1", replacementStrings);
            case UNABLE_TO_CREATE_TRANSACTION_FACTORY:
                return new EJMessage("Unable to create transaction factory $1", replacementStrings);
            case UNABLE_TO_CREATE_APPLICATION_TRANSLATOR:
                return new EJMessage("Unable to create application translator $1", replacementStrings);
            case UNABLE_TO_CREATE_APP_MANAGER:
                return new EJMessage("Unable to create application manager $1", replacementStrings);
            case UNABLE_TO_LOAD_FORM_FILE:
                return new EJMessage("Unable to load a file with the name $1 from any of the forms directories", replacementStrings);
            case UNABLE_TO_LOAD_REUSABLE_BLOCK:
                return new EJMessage("Unable to load a file with the name $1 from the reusable block location", replacementStrings);
            case UNABLE_TO_LOAD_OBJECTGROUP:
                return new EJMessage("Unable to load a file with the name $1 from the ObjectGroup location", replacementStrings);
            case UNABLE_TO_LOAD_REUSABLE_LOV:
                return new EJMessage("Unable to load a file with the name $1 from the reusable lov definition location", replacementStrings);
            case UNABLE_TO_LOAD_BLOCK_RENDERER:
                return new EJMessage("Unable to load the block renderer $1 which is assigned to this application with the name: $2", replacementStrings);
            case UNABLE_TO_LOAD_ITEM_RENDERER:
                return new EJMessage("Unable to load the item renderer $1 which is assigned to this application with the name: $2", replacementStrings);
            case UNABLE_TO_LOAD_LOV_RENDERER:
                return new EJMessage("Unable to load the lov renderer $1 which is assigned to this application with the name: $2", replacementStrings);
            case UNABLE_TO_LOAD_MENU_RENDERER:
                return new EJMessage("Unable to load the menu renderer $1 which is assigned to this application with the name: $2", replacementStrings);
            case UNABLE_TO_LOAD_UPDATE_SCREEN_RENDERER:
                return new EJMessage("Unable to load the update screen renderer $1 which is assigned to this application with the name: $2", replacementStrings);
            case UNABLE_TO_LOAD_INSERT_SCREEN_RENDERER:
                return new EJMessage("Unable to load the insert screen renderer $1 which is assigned to this application with the name: $2", replacementStrings);
            case UNABLE_TO_LOAD_QUERY_SCREEN_RENDERER:
                return new EJMessage("Unable to load the query screen renderer $1 which is assigned to this application with the name: $2", replacementStrings);
            case UNABLE_TO_FIND_BLOCK:
                return new EJMessage("Unable to find block $1 in method $2", replacementStrings);
            case UNABLE_TO_SELECT_CURRENT_RECORD:
                return new EJMessage("Unable to select the current record for block $1", replacementStrings);
            case UNABLE_TO_LOAD_RENDERER:
                return new EJMessage("Unable to load renderer $1", replacementStrings);
                
            case INVALID_RECORD_PASSED_TO_PERFORM_DELETE:
                return new EJMessage("The record passed to performDeleteOperation does not exists in the underlying blocks list of records");
            case INVALID_RECORD_PASSED_TO_PERFORM_UPDATE:
                return new EJMessage("The record passed to performUpdateOperation does not exists in the underlying blocks list of records");
            case INVALID_GROUP_NAME_IN_SET_PROPERTY_VALUE:
                return new EJMessage("The name ($1) passed to setPropertyValue contains a group name that does not exists: $1", replacementStrings);
            case INVALID_ACTION_PROCESSOR_FOR_MENU:
                return new EJMessage("There is no menu action processor with the name $1", replacementStrings);
            case INVALID_ACTION_PROCESSOR_FOR_FORM:
                return new EJMessage("There is no action processor with the name $1 on form $2", replacementStrings);
            case INVALID_ACTION_PROCESSOR_FOR_BLOCK:
                return new EJMessage("There is no action processor with the name $1 on block $2", replacementStrings);
            case INVALID_ACTION_PROCESSOR_FOR_LOV:
                return new EJMessage("There is no action processor with the name $1 on lov $2", replacementStrings);
            case INVALID_ACTION_PROCESSOR_NAME:
                return new EJMessage("The class defined by action processor name $1 is not an $2", replacementStrings);
            case INVALID_TRANSACTION_FACTORY_NAME:
                return new EJMessage("The class defined by transaction factory name $1 is not an $2", replacementStrings);
            case INVALID_APPLICATION_MANAGER_NAME:
                return new EJMessage("The class defined by application manager name $1 is not an $2", replacementStrings);
            case INVALID_TRANSLATOR_NAME:
                return new EJMessage("The class defined for translator $1 is not an $2", replacementStrings);
            case INVALID_TRANSACTION_FACTORY:
                return new EJMessage("The transaction factory defined for the application is not a ITransactionFactory", replacementStrings);
            case INVALID_RENDERER_NAME:
                return new EJMessage("The class defined by the renderer name $1 is not an $2", replacementStrings);
            case INVALID_ITEM_NAME_PASSED_TO_METHOD:
                return new EJMessage("The item name passed to $1 is invalid", replacementStrings);
            case INVALID_CANVAS_TYPE:
                return new EJMessage("The canvas type passed to $1 is not of the type: $2", replacementStrings);
                
            case SETTING_BLOCK_NAME_TO_NULL:
                return new EJMessage("Trying to reset a block name to null. Block: $1.$2", replacementStrings);
                
            case INVALID_DATA_TYPE_FOR_ITEM:
                return new EJMessage("Trying to set data item $1 to an incorrect data type. Expecting $2 but received $3", replacementStrings);
            case INVALID_DATA_TYPE_FOR_APP_LEVEL_PARAMETER:
                return new EJMessage("The application level parameter $1, has not been set to the correct data type. Expecting $2 but received $3",
                        replacementStrings);
            case INVALID_SCREEN_ITEM_REFERENCE:
                return new EJMessage("The $1 screen item properties references an item that doesn't exist within block $2. Referenced Item Name: $3",
                        replacementStrings);
            case INVALID_BLOCK_RENDERER:
                return new EJMessage("There is no block renderer called $1 assigned to this application", replacementStrings);
            case INVALID_ITEM_RENDERER:
                return new EJMessage("There is no item renderer called $1 assigned to this application", replacementStrings);
            case INVALID_LOV_RENDERER:
                return new EJMessage("There is no lov renderer called $1 assigned to this application", replacementStrings);
            case INVALID_MENU_RENDERER:
                return new EJMessage("There is no menu renderer called $1 assigned to this application", replacementStrings);
            case INVALID_UPDATE_SCREEN_RENDERER:
                return new EJMessage("There is no update screen renderer called $1 assigned to this application", replacementStrings);
            case INVALID_INSERT_SCREEN_RENDERER:
                return new EJMessage("There is no insert screen renderer called $1 assigned to this application", replacementStrings);
            case INVALID_QUERY_SCREEN_RENDERER:
                return new EJMessage("There is no query screen renderer called $1 assigned to this application", replacementStrings);
                
            case NO_CONNECTION_FACTORY_DEFINED_FOR_APPLICATION:
                return new EJMessage(
                        "A Connection Factory must be assigned to the application. Please go to the EntireJFramework properties and assign the applications Connection Factory");
            case NO_ACTION_PROCESSOR_DEFINED_FOR_FORM:
                return new EJMessage("No action processor defined for form $1", replacementStrings);
            case NO_ACTION_PROCESSOR_DEFINED_FOR_LOV:
                return new EJMessage("No action processor defined for lov $1", replacementStrings);
            case NO_ACTION_PROCESSOR_DEFINED_FOR_MENU:
                return new EJMessage("No action processor defined for menu $1", replacementStrings);
            case NOT_ITEM_ON_MAIN_SCREEN:
                return new EJMessage("There is no item called $1 on the main screen of block $2 on form $3", replacementStrings);
            case NO_RENDERER_DEFINED_FOR:
                return new EJMessage("There is no form renderer defined for $1", replacementStrings);
            case NO_ITEM_ON_FORM:
                return new EJMessage("There is no item called $1 on block $2 on form $3", replacementStrings);
            case NO_ITEM_ON_BLOCK:
                return new EJMessage("There is no item called $1 on block $2", replacementStrings);
            case NO_BLOCK_ON_FORM:
                return new EJMessage("There is no block called $1 on form $2", replacementStrings);
            case NO_PRIMARY_KEY_SET_FOR_BLOCK:
                return new EJMessage("No primary key columns set for block: $1", replacementStrings);
            case NO_FORM_PARAMETER:
                return new EJMessage("No Form Parameter with the name $1 on form $2", replacementStrings);
            case NO_FORM_LEVEL_APP_PROPERTY:
                return new EJMessage("No Form Level Application Property with the name $1 on form $2", replacementStrings);
            case NO_BLOCK_LEVEL_APP_PROPERTY:
                return new EJMessage("No Block Level Application Property with the name $1 on block $2 on form $3", replacementStrings);
            case NO_PROPERTY_IN_FRAMEWORK_EXTENSION:
                return new EJMessage("There is no property with the name $1 within the FrameworkExtenstion: $2", replacementStrings);
            case NO_PROPERTY_IN_FRAMEWORK_EXTENSION_NO_NAME:
                return new EJMessage("There is no property with the name $1 within the FrameworkExtenstion", replacementStrings);
            case NO_APPLICATION_MESSENGER_DEFINED:
                return new EJMessage("There is no application messenger defined. Messages cannot be handled correctly");
            case NO_PRIMARY_KEY_DEFINED:
                return new EJMessage("There is no primary key defined for block $1, no $2 can be performed", replacementStrings);
            case NO_FORM_PROPERTIES_FOR_REUSABLE_BLOCK:
                return new EJMessage("No FormProperties available for reusable block $1", replacementStrings);
            case NO_FORM_PROPERTIES_FOR_OBJECTGROUP:
                return new EJMessage("No FormProperties available for reusable block $1", replacementStrings);
            case NO_FORM_PROPERTIES_FOR_REUSABLE_LOV:
                return new EJMessage("No FormProperties available for ObjectGroup definition $1", replacementStrings);
            case NO_REUSABLE_BLOCK_LOCATION_DEFINED:
                return new EJMessage(
                        "A request has been made to retrieve a reusable block but no reusable block location has been defined within the EntireJ properties");
            case NO_OBJECTGROUP_LOCATION_DEFINED:
                return new EJMessage(
                        "A request has been made to retrieve a ObjectGroup but no ObjectGroup location has been defined within the EntireJ properties");
            case NO_REUSABLE_LOV_LOCATION_DEFINED:
                return new EJMessage(
                        "A request has been made to retrieve a reusable lov definition but no reusable lov definition location has been defined within the EntireJ properties");
            case NO_FORM_RENDERER_DEFINED:
                return new EJMessage("The form $1 has not form renderer defined", replacementStrings);
            case NO_CURRENT_RECORD_AVAILABLE_PLEASE_CREATE:
                return new EJMessage(
                        "There is currently no current record for the selected block. Please ensure that the block is not empty before accessing the current record");
            
            case THE_SERVICE_IS_NOT_A_IBLOCKSERVICE:
                return new EJMessage("The ServiceFactory method $1 does not return an EJBlockService", replacementStrings);
                
            case SERVICE_FACTORY_DOES_NOT_CONTAIN_METHOD:
                return new EJMessage("The ServiceFactory: $1, does not contain the method: $2", replacementStrings);
                
            default:
                return new EJMessage(EJMessageLevel.ERROR, "Internal Error");
        }
    }
}
