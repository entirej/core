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
package org.entirej.framework.core.actionprocessor;

import org.entirej.framework.core.EJActionProcessorException;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.actionprocessor.interfaces.EJMenuActionProcessor;

public class EJDefaultMenuActionProcessor implements EJMenuActionProcessor
{
    
    @Override
    public void executeActionCommand(String command) throws EJActionProcessorException
    {
    }
    
    @Override
    public void preOpenForm(EJForm form) throws EJActionProcessorException
    {
    }
    
    @Override
    public void formClosed(EJForm form) throws EJActionProcessorException
    {
    }
    
}
