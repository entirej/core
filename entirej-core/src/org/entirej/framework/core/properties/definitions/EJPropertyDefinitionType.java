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
package org.entirej.framework.core.properties.definitions;

public enum EJPropertyDefinitionType
{
    
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to only allow
     * <code>Integer</code> values
     */
    INTEGER,
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to only allow
     * <code>Float</code> values
     */
    FLOAT,
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to only allow
     * <code>String</code> values
     */
    STRING,
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to only allow
     * <code>String</code> values and falg this will use as ACTION COMMAND
     */
    ACTION_COMMAND,
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to only allow
     * <code>Boolean</code> values
     */
    BOOLEAN,
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to only allow a
     * <code>VisualAttribite</code> as the value
     */
    VISUAL_ATTRIBUTE,
    
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to only allow a
     * <code>LovDefinition</code> which has been defined within the form as the
     * value
     */
    LOV_DEFINITION,
    
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to only allow a
     * <code>LovDefinition</code> which has been defined within the form as the
     * value. There will also be a second combo box displayed that will hold the
     * items of the lov definition. The actual property value will be created as
     * follows:
     * <p>
     * <code>
     *   lovDefinitionName.lovDefinitionItemName
     * </code>
     */
    LOV_DEFINITION_WITH_ITEMS,
    
    /**
     * Indicates that this property must be a valid <code>EJItemRenderer</code>
     */
    ITEM_RENDERER,
    /**
     * Indicates that this property must be a valid <code>EJBlockRenderer</code>
     */
    BLOCK_RENDERER,
    /**
     * Indicates that this property must be a valid <code>EJFormRenderer</code>
     */
    FORM_RENDERER,
    
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to provide a list of item
     * names that belong to the block that is currently being edited
     */
    BLOCK_ITEM,
    
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to provide a project file
     * chooser and only allow class files located in the developers project
     */
    PROJECT_CLASS_FILE,
    
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to provide a project file
     * chooser and allows the chooser to choose any file available within the
     * developers project
     */
    PROJECT_FILE,
    
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to provide a menu group
     * chooser and allows the chooser to choose any menu group available within
     * the developers project
     */
    MENU_GROUP,
    
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to provide a form id chooser
     * and allows the chooser to choose any form available within the developers
     * project
     */
    FORM_ID,
    
    /**
     * Informs the <b>EntireJ Framework Plugin</b> to provide an application parameters chooser
     * and allows the chooser to choose any application parameter available within the developers
     * project
     */
    APPLICATION_PARAMETER
}
