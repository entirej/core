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
package org.entirej.framework.core.data.controllers;

import java.io.Serializable;
import java.util.Locale;

import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJManagedFrameworkConnection;
import org.entirej.framework.core.EJTranslatorHelper;
import org.entirej.framework.core.enumerations.EJScreenType;
import org.entirej.framework.core.extensions.properties.EJCoreFrameworkExtensionProperty;
import org.entirej.framework.core.interfaces.EJTranslator;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreCanvasProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreInsertScreenItemProperties;
import org.entirej.framework.core.properties.EJCoreItemGroupProperties;
import org.entirej.framework.core.properties.EJCoreItemProperties;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.EJCoreLovMappingProperties;
import org.entirej.framework.core.properties.EJCoreMainScreenItemProperties;
import org.entirej.framework.core.properties.EJCoreMenuLeafActionProperties;
import org.entirej.framework.core.properties.EJCoreMenuLeafBranchProperties;
import org.entirej.framework.core.properties.EJCoreMenuLeafContainer;
import org.entirej.framework.core.properties.EJCoreMenuLeafFormProperties;
import org.entirej.framework.core.properties.EJCoreMenuLeafProperties;
import org.entirej.framework.core.properties.EJCoreMenuProperties;
import org.entirej.framework.core.properties.EJCoreQueryScreenItemProperties;
import org.entirej.framework.core.properties.EJCoreStackedPageProperties;
import org.entirej.framework.core.properties.EJCoreTabPageProperties;
import org.entirej.framework.core.properties.EJCoreUpdateScreenItemProperties;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJCanvasProperties;
import org.entirej.framework.core.properties.interfaces.EJItemGroupProperties;
import org.entirej.framework.core.properties.interfaces.EJScreenItemProperties;
import org.entirej.framework.core.properties.interfaces.EJStackedPageProperties;
import org.entirej.framework.core.properties.interfaces.EJTabPageProperties;

/**
 * The TranslationController is responsible for translating all application
 * texts by using the framework assigned Translator
 */
public class EJTranslationController implements Serializable
{
    private EJFrameworkManager _frameworkManager;
    private EJTranslator       _appTranslator;
    private Locale             _currentLocale;
    
    public EJTranslationController(EJFrameworkManager frameworkManager, EJTranslator appTranslator, Locale locale)
    {
        _frameworkManager = frameworkManager;
        _appTranslator = appTranslator;
        setLocale(locale);
    }
    
    public void setLocale(Locale locale)
    {
        if (locale == null)
        {
            throw new NullPointerException("The Locale passed to the TranslationController is null");
        }
        
        _currentLocale = locale;
    }
    
    /**
     * Translates a given text to the current application <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textToTranslate
     *            The value to translate
     * @return The translated text for the given key
     * 
     * @see #translateText(String, Locale)
     */
    public String translateText(String textToTranslate)
    {
        return translateText(textToTranslate, _currentLocale);
    }
    
    /**
     * Translates a given text to the given <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textToTranslate
     *            The value to translate
     * @return The translated text for the given key
     * 
     * @see #translateText(String)
     */
    public String translateText(String textToTranslate, Locale locale)
    {
        if (textToTranslate == null)
        {
            return null;
        }
        
        EJManagedFrameworkConnection connection = _frameworkManager.getConnection();
        try
        {
            if (_appTranslator != null)
            {
                String translatedText = _appTranslator.translateText(new EJTranslatorHelper(_frameworkManager), textToTranslate);
                if (translatedText == null || translatedText.trim().length() == 0)
                {
                    return textToTranslate;
                }
                else
                {
                    return translatedText;
                }
            }
            else
            {
                return textToTranslate;
            }
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
    }
    
    /**
     * Translates a given message text to the current application
     * <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textToTranslate
     *            The value to translate
     * @return The translated text for the given key
     */
    public String translateMessageText(String textToTranslate)
    {
        return translateMessageText(textToTranslate, _currentLocale);
    }
    
    /**
     * Translates a given message text to the given <code>{@link Locale}
     * </code> using the applications <code>{@link EJTranslator}</code>
     * <p>
     * If the application has no translator defined then this message will just
     * return the same text as the one passed
     * 
     * @param textToTranslate
     *            The value to translate
     * @param locale
     *            The current <code>{@link Locale}</code> to use for the
     *            translation
     * @return The translated text for the given key
     */
    public String translateMessageText(String textToTranslate, Locale locale)
    {
        if (textToTranslate == null)
        {
            return null;
        }
        
        EJManagedFrameworkConnection connection = _frameworkManager.getConnection();
        try
        {
            if (_appTranslator != null)
            {
                String translatedText = _appTranslator.translateMessageText(new EJTranslatorHelper(_frameworkManager), textToTranslate);
                if (translatedText == null || translatedText.trim().length() == 0)
                {
                    return textToTranslate;
                }
                else
                {
                    return translatedText;
                }
            }
            else
            {
                return textToTranslate;
            }
            
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
    }
    
    public EJDateHelper createDateHelper()
    {
        return new EJDateHelper(_currentLocale == null ? Locale.ENGLISH : _currentLocale);
    }
    
    public void translateForm(EJCoreFormProperties formToTranslate, EJFrameworkManager fwManager)
    {
        EJManagedFrameworkConnection connection = fwManager.getConnection();
        
        try
        {
            // Translate the form properties
            formToTranslate.setTranslatedTitle(translateText(formToTranslate.getBaseTitle()));
            translateFrameworkExtensionProperties(formToTranslate.getFormRendererProperties());
            
            // Translate the forms blocks
            for (EJCoreBlockProperties blockProps : formToTranslate.getBlockContainer().getAllBlockProperties())
            {
                translateBlockProperties(blockProps);
            }
            
            // Translate the forms LOV's
            for (EJCoreLovDefinitionProperties lovDefProps : formToTranslate.getLovDefinitionContainer().getAllLovDefinitionProperties())
            {
                translateFrameworkExtensionProperties(lovDefProps.getLovRendererProperties());
                translateBlockProperties(lovDefProps.getBlockProperties());
            }
            
            for (EJCanvasProperties canvasProperties : formToTranslate.getCanvasContainer().getAllCanvasProperties())
            {
                EJCoreCanvasProperties canvas = (EJCoreCanvasProperties) canvasProperties;
                
                translateCanvasProperties(canvas);
            }
        }
        finally
        {
            if (connection != null)
            {
                connection.close();
            }
        }
        
    }
    
    private void translateBlockProperties(EJCoreBlockProperties blockProperties)
    {
        translateFrameworkExtensionProperties(blockProperties.getBlockRendererProperties());
        translateFrameworkExtensionProperties(blockProperties.getInsertScreenRendererProperties());
        translateFrameworkExtensionProperties(blockProperties.getUpdateScreenRendererProperties());
        translateFrameworkExtensionProperties(blockProperties.getQueryScreenRendererProperties());
        
        blockProperties.getMainScreenProperties().setTranslatedFrameTitle(translateText(blockProperties.getMainScreenProperties().getBaseFrameTitle()));
        
        for (EJCoreItemProperties itemProperties : blockProperties.getItemPropertiesContainer().getAllItemProperties())
        {
            translateBlockItemProperties(itemProperties);
        }
        
        for (EJItemGroupProperties itemGroupProperties : blockProperties.getScreenItemGroupContainer(EJScreenType.MAIN).getAllItemGroupProperties())
        {
            translateItemGroupProperties((EJCoreItemGroupProperties) itemGroupProperties);
        }
        for (EJItemGroupProperties itemGroupProperties : blockProperties.getScreenItemGroupContainer(EJScreenType.INSERT).getAllItemGroupProperties())
        {
            translateItemGroupProperties((EJCoreItemGroupProperties) itemGroupProperties);
        }
        for (EJItemGroupProperties itemGroupProperties : blockProperties.getScreenItemGroupContainer(EJScreenType.UPDATE).getAllItemGroupProperties())
        {
            translateItemGroupProperties((EJCoreItemGroupProperties) itemGroupProperties);
        }
        for (EJItemGroupProperties itemGroupProperties : blockProperties.getScreenItemGroupContainer(EJScreenType.QUERY).getAllItemGroupProperties())
        {
            translateItemGroupProperties((EJCoreItemGroupProperties) itemGroupProperties);
        }
        for (EJCoreLovMappingProperties mapping : blockProperties.getLovMappingContainer().getAllLovMappingProperties())
        {
            mapping.setTranslatedLovDisplayName(translateText(mapping.getBaseLovDisplayName()));
        }
    }
    
    private void translateCanvasProperties(EJCoreCanvasProperties canvasProperties)
    {
        canvasProperties.setTranslatedPopupPageTitle(translateText(canvasProperties.getBasePopupPageTitle()));
        canvasProperties.setTranslatedGroupFrameTitle(translateText(canvasProperties.getBaseGroupFrameTitle()));
        canvasProperties.setTranslatedButtonOneText(translateText(canvasProperties.getBaseButtonOneText()));
        canvasProperties.setTranslatedButtonTwoText(translateText(canvasProperties.getBaseButtonTwoText()));
        canvasProperties.setTranslatedButtonThreeText(translateText(canvasProperties.getBaseButtonThreeText()));
        
        for (EJTabPageProperties tabPageProperties : canvasProperties.getTabPageContainer().getAllTabPageProperties())
        {
            EJCoreTabPageProperties pageProperties = (EJCoreTabPageProperties) tabPageProperties;
            
            pageProperties.setTranslatedPageTitle(translateText(pageProperties.getBasePageTitle()));
            
            for (EJCanvasProperties containedCanvas : pageProperties.getContainedCanvases().getAllCanvasProperties())
            {
                translateCanvasProperties((EJCoreCanvasProperties) containedCanvas);
            }
        }
        
        for (EJStackedPageProperties stackedPageProperties : canvasProperties.getStackedPageContainer().getAllStackedPageProperties())
        {
            EJCoreStackedPageProperties pageProperties = (EJCoreStackedPageProperties) stackedPageProperties;
            for (EJCanvasProperties containedCanvas : pageProperties.getContainedCanvases().getAllCanvasProperties())
            {
                translateCanvasProperties((EJCoreCanvasProperties) containedCanvas);
            }
        }
        
        for (EJCanvasProperties containedCanvas : canvasProperties.getPopupCanvasContainer().getAllCanvasProperties())
        {
            translateCanvasProperties((EJCoreCanvasProperties) containedCanvas);
        }
        for (EJCanvasProperties containedCanvas : canvasProperties.getGroupCanvasContainer().getAllCanvasProperties())
        {
            translateCanvasProperties((EJCoreCanvasProperties) containedCanvas);
        }
        
    }
    
    public void translateMenuProperties(EJCoreMenuProperties menu)
    {
        for (EJCoreMenuLeafProperties leaf : menu.getLeaves())
        {
            leaf.setTranslatedDisplayName(translateText(leaf.getBaseDisplayName()));
            
            if (leaf instanceof EJCoreMenuLeafActionProperties)
            {
                ((EJCoreMenuLeafActionProperties) leaf).setTranslatedHint(translateText(((EJCoreMenuLeafActionProperties) leaf).getBaseHint()));
            }
            else if (leaf instanceof EJCoreMenuLeafFormProperties)
            {
                ((EJCoreMenuLeafFormProperties) leaf).setTranslatedHint(translateText(((EJCoreMenuLeafFormProperties) leaf).getBaseHint()));
            }
            else if (leaf instanceof EJCoreMenuLeafBranchProperties)
            {
                translateMenuLeafProperties((EJCoreMenuLeafBranchProperties) leaf);
            }
        }
    }
    
    private void translateMenuLeafProperties(EJCoreMenuLeafContainer container)
    {
        for (EJCoreMenuLeafProperties leaf : container.getLeaves())
        {
            leaf.setTranslatedDisplayName(translateText(leaf.getBaseDisplayName()));
            
            if (leaf instanceof EJCoreMenuLeafActionProperties)
            {
                ((EJCoreMenuLeafActionProperties) leaf).setTranslatedHint(translateText(((EJCoreMenuLeafActionProperties) leaf).getBaseHint()));
            }
            else if (leaf instanceof EJCoreMenuLeafFormProperties)
            {
                ((EJCoreMenuLeafFormProperties) leaf).setTranslatedHint(translateText(((EJCoreMenuLeafFormProperties) leaf).getBaseHint()));
            }
            else if (leaf instanceof EJCoreMenuLeafBranchProperties)
            {
                translateMenuLeafProperties((EJCoreMenuLeafBranchProperties) leaf);
            }
        }
    }
    
    private void translateBlockItemProperties(EJCoreItemProperties itemProperties)
    {
        translateFrameworkExtensionProperties(itemProperties.getItemRendererProperties());
    }
    
    private void translateItemGroupProperties(EJCoreItemGroupProperties itemGroupProperties)
    {
        itemGroupProperties.setTranslatedFrameTitle(translateText(itemGroupProperties.getBaseFrameTitle()));
        
        for (EJScreenItemProperties screenItem : itemGroupProperties.getAllItemProperties())
        {
            if (screenItem instanceof EJCoreMainScreenItemProperties)
            {
                EJCoreMainScreenItemProperties mainScreenItem = (EJCoreMainScreenItemProperties) screenItem;
                mainScreenItem.setTranslatedLabel(translateText(mainScreenItem.getBaseLabel()));
                mainScreenItem.setTranslatedHint(translateText(mainScreenItem.getBaseHint()));
                
                if (mainScreenItem.getBlockRendererRequiredProperties() != null)
                {
                    translateFrameworkExtensionProperties(mainScreenItem.getBlockRendererRequiredProperties());
                }
                
                if (mainScreenItem.getLovRendererRequiredProperties() != null)
                {
                    translateFrameworkExtensionProperties(mainScreenItem.getLovRendererRequiredProperties());
                }
            }
            else if (screenItem instanceof EJCoreQueryScreenItemProperties)
            {
                EJCoreQueryScreenItemProperties queryScreenItem = (EJCoreQueryScreenItemProperties) screenItem;
                queryScreenItem.setTranslatedLabel(translateText(queryScreenItem.getBaseLabel()));
                queryScreenItem.setTranslatedHint(translateText(queryScreenItem.getBaseHint()));
                
                translateFrameworkExtensionProperties(queryScreenItem.getQueryScreenRendererProperties());
                
            }
            else if (screenItem instanceof EJCoreInsertScreenItemProperties)
            {
                EJCoreInsertScreenItemProperties insertScreenItem = (EJCoreInsertScreenItemProperties) screenItem;
                insertScreenItem.setTranslatedLabel(translateText(insertScreenItem.getBaseLabel()));
                insertScreenItem.setTranslatedHint(translateText(insertScreenItem.getBaseHint()));
                
                translateFrameworkExtensionProperties(insertScreenItem.getInsertScreenRendererProperties());
            }
            else if (screenItem instanceof EJCoreUpdateScreenItemProperties)
            {
                EJCoreUpdateScreenItemProperties updateScreenItem = (EJCoreUpdateScreenItemProperties) screenItem;
                updateScreenItem.setTranslatedLabel(translateText(updateScreenItem.getBaseLabel()));
                updateScreenItem.setTranslatedHint(translateText(updateScreenItem.getBaseHint()));
                
                translateFrameworkExtensionProperties(updateScreenItem.getUpdateScreenRendererProperties());
            }
        }
        
        // Now translate the child item groups
        if (itemGroupProperties.getChildItemGroupContainer() != null)
        {
            for (EJItemGroupProperties childItemGroupProperties : itemGroupProperties.getChildItemGroupContainer().getAllItemGroupProperties())
            {
                translateItemGroupProperties((EJCoreItemGroupProperties) childItemGroupProperties);
            }
        }
    }
    
    private void translateFrameworkExtensionProperties(EJFrameworkExtensionProperties properties)
    {
        for (EJCoreFrameworkExtensionProperty property : properties.getAllProperties().values())
        {
            if (property.isMultilingual() && property.getValue() != null && property.getValue().trim().length() > 0)
            {
                property.setTranslatedValue(translateText(property.getBaseValue()));
            }
        }
        
        for (EJFrameworkExtensionProperties propertyGroup : properties.getAllPropertyGroups())
        {
            translateFrameworkExtensionProperties(propertyGroup);
        }
    }
}
