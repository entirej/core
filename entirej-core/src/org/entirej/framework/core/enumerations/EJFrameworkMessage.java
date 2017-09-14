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
package org.entirej.framework.core.enumerations;

public enum EJFrameworkMessage
{

    ASK_TO_SAVE_CHANGES,
    
    FRAMEWORK_NOT_INITIALISED,

    CANNOT_DELETE_WHILE_CHILDREN_EXIST,
    CANNOT_PERFORM_QUERY_WITH_NO_SERVICE,
    CANNOT_UPDATE_A_BLOCK_WITH_NO_RECORD,

    DATA_CHANGED_BY_ANOTHER_USER,

    NULL_FORM_CONTROLLER_PASSED_TO_FORM_PROPS,
    NULL_QUERY_RECORD_PASSED_TO_METHOD,
    NULL_TRANSACTION_PASSED_TO_METHOD,
    NULL_RECORD_PASSED_TO_METHOD,
    NULL_QUERY_CRITERIA_PASSED_TO_METHOD,
    NULL_FORM_PROPERTIES_PASSED_TO_METHOD,
    NULL_MENU_PROPERTIES_PASSED_TO_METHOD,
    NULL_APPLIACTION_ACTION_PROCESSOR_PASSED_TO_METHOD,
    NULL_LOVDEF_PROPERTIES_PASSED_TO_METHOD,
    NULL_BLOCK_PROPERTIES_PASSED_TO_METHOD,
    NULL_CANVAS_NAME_PASSED_TO_METHOD,
    NULL_CANVAS_PAGE_NAME_PASSED_TO_METHOD,
    NULL_PROCESSOR_NAME_PASSED_TO_METHOD,
    NULL_FORM_NAME_PASSED_TO_METHOD,
    NULL_BLOCK_NAME_PASSED_TO_METHOD,
    NULL_APPLICATION_MANAGER_PASSED_TO_METHOD,
    NULL_TRANSLATOR_NAME_PASSED_TO_METHOD,
    NULL_LOV_RENDERER_PASSED_TO_METHOD,
    NULL_APP_COMPONENT_RENDERER_PASSED_TO_METHOD,
    NULL_MENU_RENDERER_PASSED_TO_METHOD,
    NULL_DATA_FORM_PASSED_TO_FORM_CONTROLLER,
    NULL_PROPERTY_NAMES_PASSED_TO_METHOD,
    NULL_DATA_TYPE_PASSED_TO_METHOD,
    
    RECORD_CREATED_WITHOUT_BLOCK,

    SETTING_BLOCK_NAME_TO_NULL,

    INVALID_DATA_TYPE_FOR_ITEM,
    INVALID_ITEM_RENDERER,
    INVALID_ITEM_NAME_PASSED_TO_METHOD,
    INVALID_RECORD_PASSED_TO_PERFORM_DELETE,
    INVALID_RECORD_PASSED_TO_PERFORM_UPDATE,
    INVALID_GROUP_NAME_IN_SET_PROPERTY_VALUE,
    INVALID_ACTION_PROCESSOR_FOR_BLOCK,
    INVALID_ACTION_PROCESSOR_FOR_FORM,
    INVALID_ACTION_PROCESSOR_FOR_LOV,
    INVALID_ACTION_PROCESSOR_FOR_MENU,
    INVALID_ACTION_PROCESSOR_FOR_APPLIACTION,
    INVALID_ACTION_PROCESSOR_NAME,
    INVALID_TRANSACTION_FACTORY_NAME,
    INVALID_DATA_TYPE_FOR_APP_LEVEL_PARAMETER,
    INVALID_APPLICATION_MANAGER_NAME,
    INVALID_TRANSLATOR_NAME,
    INVALID_TRANSACTION_FACTORY,
    INVALID_SCREEN_ITEM_REFERENCE,
    INVALID_BLOCK_RENDERER,
    INVALID_BLOCK_FOR_RECORD,
    INVALID_LOV_RENDERER,
    INVALID_MENU_RENDERER,
    INVALID_APP_COMPONENT_RENDERER,
    INVALID_UPDATE_SCREEN_RENDERER,
    INVALID_INSERT_SCREEN_RENDERER,
    INVALID_QUERY_SCREEN_RENDERER,
    INVALID_RENDERER_NAME,
    INVALID_CANVAS_TYPE,
    INVALID_FORM_OR_CANVAS_NAME,

    NO_ITEM_ON_FORM,
    NO_ITEM_ON_BLOCK,
    NO_BLOCK_ON_FORM,
    NO_PRIMARY_KEY_SET_FOR_BLOCK,
    NO_ACTION_PROCESSOR_DEFINED_FOR_FORM,
    NO_ACTION_PROCESSOR_DEFINED_FOR_APPLIACTION,
    NO_ACTION_PROCESSOR_DEFINED_FOR_MENU,
    NO_ACTION_PROCESSOR_DEFINED_FOR_LOV,
    NO_CONNECTION_FACTORY_DEFINED_FOR_APPLICATION,
    NO_FORM_PARAMETER,
    NO_BLOCK_LEVEL_APP_PROPERTY,
    NO_FORM_LEVEL_APP_PROPERTY,
    NO_FORM_PROPERTIES_FOR_REUSABLE_BLOCK,
    NO_FORM_PROPERTIES_FOR_OBJECTGROUP,
    NO_FORM_PROPERTIES_FOR_REUSABLE_LOV,
    NO_REUSABLE_BLOCK_LOCATION_DEFINED,
    NO_OBJECTGROUP_LOCATION_DEFINED,
    NO_REUSABLE_LOV_LOCATION_DEFINED,
    NO_FORM_RENDERER_DEFINED,
    NO_RENDERER_DEFINED_FOR,
    NO_PROPERTY_IN_FRAMEWORK_EXTENSION,
    NO_PROPERTY_IN_FRAMEWORK_EXTENSION_NO_NAME,
    NO_APPLICATION_MESSENGER_DEFINED,
    NO_PRIMARY_KEY_DEFINED,
    NO_CURRENT_RECORD_AVAILABLE_PLEASE_CREATE,
    
    NOT_ITEM_ON_MAIN_SCREEN,

    UNABLE_TO_LOCK_RECORD,
    UNABLE_TO_FIND_METHOD,
    UNABLE_TO_FIND_BLOCK,
    UNABLE_TO_FIND_ITEM_ON_BLOCK,
    UNABLE_TO_EXECUTE_METHOD,
    UNABLE_TO_LOAD_ITEM_RENDERER,
    UNABLE_TO_RETRIEVE_ACCESS_PROCESSOR_PROPERTIES,
    UNABLE_TO_RETRIEVE_ACCESS_PROCESSOR_PROPERTIES_FOR_ITEM,
    UNABLE_TO_RETRIEVE_FORM_RENDERER_PROPERTY,
    UNABLE_TO_RETRIEVE_ITEM_RENDERER_PROPERTY,
    UNABLE_TO_RETRIEVE_LOV_RENDERER_PROPERTY,
    UNABLE_TO_RETRIEVE_BLOCK_RENDERER_PROPERTY,
    UNABLE_TO_RETIEVE_MAIN_SCREEN_ITEM_PROPERTIES,
    UNABLE_TO_RETIEVE_QUERY_SCREEN_ITEM_PROPERTIES,
    UNABLE_TO_RETIEVE_INSERT_SCREEN_ITEM_PROPERTIES,
    UNABLE_TO_RETIEVE_UPDATE_SCREEN_ITEM_PROPERTIES,
    UNABLE_TO_SELECT_CURRENT_RECORD,
    UNABLE_TO_LOAD_RENDERER,
    UNABLE_TO_CALL_METHOD,
    UNABLE_TO_CREATE_ACTION_PROCESSOR,
    UNABLE_TO_CREATE_DATA_ACCESS_PROCESSOR,
    UNABLE_TO_CREATE_TRANSACTION_FACTORY,
    UNABLE_TO_CREATE_APP_MANAGER,
    UNABLE_TO_CREATE_APPLICATION_TRANSLATOR,
    UNABLE_TO_LOAD_FORM_FILE,
    UNABLE_TO_LOAD_REUSABLE_BLOCK,
    UNABLE_TO_LOAD_OBJECTGROUP,
    UNABLE_TO_LOAD_REUSABLE_LOV,
    UNABLE_TO_LOAD_BLOCK_RENDERER,
    UNABLE_TO_LOAD_LOV_RENDERER,
    UNABLE_TO_LOAD_APP_COMPONENT,
    UNABLE_TO_LOAD_MENU_LOV_RENDERER,
    UNABLE_TO_LOAD_UPDATE_SCREEN_RENDERER,
    UNABLE_TO_LOAD_INSERT_SCREEN_RENDERER,
    UNABLE_TO_LOAD_QUERY_SCREEN_RENDERER,
    UNABLE_TO_LOAD_MENU_RENDERER,

    VALUE_CANNOT_BE_CONVERTED_TO_BOOLEAN,
    VALUE_CANNOT_BE_CONVERTED_TO_INT,
    VALUE_CANNOT_BE_CONVERTED_TO_FLOAT,
    VALUE_CANNOT_BE_CONVERTED_TO_DOUBLE,
    THE_SERVICE_IS_NOT_A_IBLOCKSERVICE,
    SERVICE_FACTORY_DOES_NOT_CONTAIN_METHOD
    
}
