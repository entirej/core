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
package org.entirej.framework.core.properties;

public class EJCoreMenuLeafFormProperties extends EJCoreMenuLeafProperties
{
    private String _formName;
    private String _hint;
    private String _baseHint;
    
    public EJCoreMenuLeafFormProperties(EJCoreMenuProperties menu, EJCoreMenuLeafContainer container)
    {
        super(menu, container);
    }
    
    public String getFormName()
    {
        return _formName;
    }
    
    public void setFormName(String formName)
    {
        _formName = formName;
    }
    
    public String getHint()
    {
        return _hint;
    }
    
    public void setHint(String hint)
    {
        _hint = hint;
    }
    
    /**
     * Returns the untranslated hint for this item
     * 
     * @return The untranslated item hint
     * @see #getHint()
     */
    public String getBaseHint()
    {
        return _baseHint;
    }
    
    /**
     * Used to set the translated hint
     * <p>
     * EntireJ will retrieve the base hint and use the assigned framework
     * translator to translate the text. Once translated, EntireJ will assign
     * the translated text via this method
     * 
     * @param translatedHint
     *            The translated hint
     */
    public void setTranslatedHint(String translatedHint)
    {
        _hint = translatedHint;
    }
    
    /**
     * Sets this items base hint
     * <p>
     * A base hint is an untranslated hint. All hints will be translated using
     * the applications translator. If there is no translator defined for the
     * application then the base hint will be used for the items hint
     * 
     * @param hint
     *            This items base hint
     */
    public void setBaseHint(String hint)
    {
        if (hint != null && hint.trim().length() == 0)
        {
            _baseHint = null;
        }
        else
        {
            _baseHint = hint;
        }
        
        _hint = _baseHint;
    }
}
