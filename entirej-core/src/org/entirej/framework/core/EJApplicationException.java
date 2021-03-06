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

import org.entirej.framework.core.interfaces.EJException;

/**
 * This exception is the main exception within the <b>EntireJ Framework</b>
 */

public class EJApplicationException extends RuntimeException implements EJException
{
    private boolean           _stopProcessing  = false;
    private EJMessage         _message         = null;
    
    public EJApplicationException()
    {
        _stopProcessing = true;
    }
    
    /**
     * Indicates if this exception was just thrown to stop processing
     * <p>
     * If an empty exception is thrown, then the user wanted to just stop the
     * current processing without giving any user or log message
     * 
     * @return <code>true</code> if this exception was only used to stop
     *         processing
     * 
     */
    public boolean stopProcessing()
    {
        return _stopProcessing;
    }
    
    public EJApplicationException(String message)
    {
        super(message);
        _message = new EJMessage(message);
    }
    
    public EJApplicationException(String message, Throwable cause)
    {
        super(message, cause);
        _message = new EJMessage(message);
    }
    
    public EJApplicationException(EJMessage message)
    {
        super(message.getMessage());
        _message = message;
    }
    
    public EJApplicationException(Throwable cause)
    {
        super(cause);
        if (cause instanceof EJException)
        {
            _stopProcessing = ((EJException)cause).stopProcessing();
        }
        this.setStackTrace(cause.getStackTrace());
        extractMessage(cause);
    }
    
    public EJApplicationException(EJMessage message, Throwable cause)
    {
        super(message.getMessage(), cause);
        this.setStackTrace(cause.getStackTrace());
        _message = message;
    }
    
    private void extractMessage(Throwable cause)
    {
        if (cause instanceof EJException)
        {
            _message = ((EJException) cause).getFrameworkMessage();
        }
        else
        {
            _message = new EJMessage(cause.getMessage());
        }
    }
    
    public EJMessage getFrameworkMessage()
    {
        return _message;
    }
}
