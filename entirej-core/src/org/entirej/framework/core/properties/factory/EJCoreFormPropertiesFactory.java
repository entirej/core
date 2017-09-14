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
package org.entirej.framework.core.properties.factory;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.entirej.framework.core.EJApplicationException;
import org.entirej.framework.core.EJConstants;
import org.entirej.framework.core.EJForm;
import org.entirej.framework.core.EJFrameworkManager;
import org.entirej.framework.core.EJMessageFactory;
import org.entirej.framework.core.enumerations.EJCanvasType;
import org.entirej.framework.core.enumerations.EJFrameworkMessage;
import org.entirej.framework.core.properties.EJCoreBlockProperties;
import org.entirej.framework.core.properties.EJCoreCanvasProperties;
import org.entirej.framework.core.properties.EJCoreFormProperties;
import org.entirej.framework.core.properties.EJCoreLovDefinitionProperties;
import org.entirej.framework.core.properties.EJCoreProperties;
import org.entirej.framework.core.properties.EJFileLoader;
import org.entirej.framework.core.properties.containers.EJCoreBlockPropertiesContainer;
import org.entirej.framework.core.properties.interfaces.EJCanvasProperties;
import org.entirej.framework.core.properties.interfaces.EJFormPropertiesFactory;
import org.entirej.framework.core.properties.reader.EJCoreFormPropertiesHandler;

public class EJCoreFormPropertiesFactory implements EJFormPropertiesFactory
{
    private EJFrameworkManager _frameworkManager;

    public EJCoreFormPropertiesFactory(EJFrameworkManager frameworkManager)
    {
        _frameworkManager = frameworkManager;
    }

    /**
     * 
     * @param formName
     * @return
     */
    public EJCoreFormProperties createFormProperties(String formName)
    {
        if (formName == null)
        {
            throw new NullPointerException("The formName passed to createFormProperties is null");
        }

        try
        {
            InputStream inStream = getFormPropertiesDocument(formName);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxPArser = factory.newSAXParser();
            EJCoreFormPropertiesHandler handler = _frameworkManager.getHandlerFactory().createFormHandler(formName, false, false);
            saxPArser.parse(inStream, handler);

            EJCoreFormProperties formProperties = handler.getFormProperties();
            if (formProperties.getFirstNavigableBlock() == null || formProperties.getFirstNavigableBlock().isEmpty())
            {
                EJCoreBlockPropertiesContainer blockContainer = formProperties.getBlockContainer();
                BLOCK: for (EJCoreBlockProperties block : blockContainer.getAllBlockProperties())
                {
                    if (block.getCanvasName() != null && !block.getCanvasName().isEmpty())
                    {
                        EJCanvasProperties canvasProperties = formProperties.getCanvasProperties(block.getCanvasName());
                        while (canvasProperties != null)
                        {
                            canvasProperties = getParentCanvas(formProperties, canvasProperties);
                            if (canvasProperties != null && canvasProperties.getType() == EJCanvasType.POPUP)
                            {
                                continue BLOCK;
                            }

                        }

                        formProperties.setFirstNavigableBlock(block.getName());
                        break;

                    }
                }

            }
            return formProperties;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new EJApplicationException(e);
        }
    }

    public EJCanvasProperties getParentCanvas(EJCoreFormProperties formProperties, EJCanvasProperties canvas)
    {
        if (canvas != null)
        {
            String parentCanvasName = canvas.getContentCanvasName();
            if (parentCanvasName != null)
            {
                return formProperties.getCanvasProperties(parentCanvasName);
            }
        }
        return null;
    }

    /**
     * Retrieves a reusable block from its definition file
     * <p>
     * All reusable blocks are stored using the same XML format as a form. This
     * allows EntireJ to use the standard FormHandler to read the form
     * properties. Once the form properties have been read the required block
     * properties will be retrieved and returned to the caller. Once a reusable
     * block has been integrated into the form, it works as any of the
     * non-referenced blocks
     * 
     * @param referencedBlockName
     *            The name of the required referenced block
     * @return The <code>BlockProperties</code> for the required block
     * @throws EJApplicationException
     *             If there is no reusable block with the given name or there
     *             was an error reading the reusable block definition file
     */
    public EJCoreBlockProperties createReferencedBlockProperties(EJCoreFormProperties formProperties, String referencedBlockName, String blockName)
    {
        if (referencedBlockName == null)
        {
            throw new NullPointerException("The referencedBlockName passed to createReferencedBlockProperties is null");
        }

        try
        {
            InputStream inStream = getFormPropertiesDocumentForReusableBlock(referencedBlockName);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxPArser = factory.newSAXParser();
            EJCoreFormPropertiesHandler handler = _frameworkManager.getHandlerFactory().createFormHandler(referencedBlockName, false, true);
            saxPArser.parse(inStream, handler);

            // The form properties within the handler are not the same as those
            // past as a parameter
            // The parameter is the actual form to which these
            if (handler.getFormProperties() == null)
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_FORM_PROPERTIES_FOR_REUSABLE_BLOCK,
                        referencedBlockName));
            }
            else
            {

                for (EJCoreLovDefinitionProperties lovDef : handler.getFormProperties().getLovDefinitionContainer().getAllLovDefinitionProperties())
                {
                    lovDef.internalSetName(String.format("%s.%s", blockName, lovDef.getName()));
                    formProperties.addLovDefinitionProperties(lovDef);
                }

                EJCoreBlockProperties blockProperties = handler.getFormProperties().getBlockContainer().getBlockProperties(referencedBlockName);
                if (blockProperties == null && !handler.getFormProperties().getBlockContainer().getAllBlockProperties().isEmpty())
                {
                    blockProperties = handler.getFormProperties().getBlockContainer().getAllBlockProperties().iterator().next();
                }
                blockProperties.setReferencedBlockName(referencedBlockName);
                return blockProperties;
            }
        }
        catch (Exception e)
        {
            throw new EJApplicationException(e);
        }
    }

    /**
     * Retrieves a ObjectGroup from its definition file
     * <p>
     * All ObjectGroup are stored using the same XML format as a form. This
     * allows EntireJ to use the standard FormHandler to read the form
     * properties. Once the form properties have been read the ObjectGroup
     * properties will be retrieved and returned to the caller. Once a
     * ObjectGroup has been integrated into the form, it works as any of the
     * from elements
     * 
     * @param referencedBlockName
     *            The name of the required referenced block
     * @return The <code>EJCoreFormProperties</code> for the ObjectGroup
     * @throws EJApplicationException
     *             If there is no reusable block with the given name or there
     *             was an error reading the reusable block definition file
     */
    public EJCoreFormProperties createObjectGroupProperties(String objectGroupName)
    {
        if (objectGroupName == null)
        {
            throw new NullPointerException("The objectGroupName passed to createObjectGroupProperties is null");
        }

        try
        {
            InputStream inStream = getFormPropertiesDocumentForObjectGroup(objectGroupName);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxPArser = factory.newSAXParser();
            EJCoreFormPropertiesHandler handler = _frameworkManager.getHandlerFactory().createFormHandler(objectGroupName, false, true);
            saxPArser.parse(inStream, handler);

            // The form properties within the handler are not the same as those
            // past as a parameter
            // The parameter is the actual form to which these
            EJCoreFormProperties objectGroup = handler.getFormProperties();
            if (objectGroup == null)
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_FORM_PROPERTIES_FOR_OBJECTGROUP,
                        objectGroupName));
            }
            else
            {

                return objectGroup;
            }
        }
        catch (Exception e)
        {
            throw new EJApplicationException(e);
        }
    }

    private InputStream getFormPropertiesDocument(String formName)
    {
        // Try to find the required form within one of the formProperties
        // directory from the EntireJProperties
        // If the form cannot be found within any of the directories, throw an
        // exception
        String directory;
        InputStream inStream = null;

        Iterator<String> formDirectories = EJCoreProperties.getInstance().getFormPackageNames().iterator();
        while (formDirectories.hasNext())
        {
            directory = formDirectories.next();
            if (EJFileLoader.fileExists(directory + EJConstants.DIRECTORY_SEPERATOR + formName + EJConstants.FORM_PROPERTIES_FILE_SUFFIX))
            {

                inStream = EJFileLoader.loadFile(directory + EJConstants.DIRECTORY_SEPERATOR + formName + EJConstants.FORM_PROPERTIES_FILE_SUFFIX);
                break;
            }
        }

        if (inStream == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_FORM_FILE, formName));
        }

        return inStream;
    }

    /**
     * Retrieves a reusable lov definition from its definition file
     * <p>
     * All reusable lov definitions are stored using the same XML format as a
     * form. This allows EntireJ to use the standard FormHandler to read the
     * form properties. Once the form properties have been read the required lov
     * definition properties will be retrieved and returned to the caller. Once
     * a reusable lov definition has been integrated into the form, it works as
     * any of the non-referenced lov definitions
     * 
     * @param referencedLovDefName
     *            The name of the required referenced lov definition
     * @return The <code>LovDefinitionProperties</code> for the required lov
     *         definition
     */
    public EJCoreLovDefinitionProperties createReferencedLovDefinitionProperties(String referencedLovDefName)
    {
        if (referencedLovDefName == null)
        {
            throw new NullPointerException("The referencedLovDefName passed to createReferencedLovDefinitionProperties is null");
        }

        try
        {
            InputStream inStream = getFormPropertiesDocumentForReusableLovDefinition(referencedLovDefName);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxPArser = factory.newSAXParser();
            EJCoreFormPropertiesHandler handler = _frameworkManager.getHandlerFactory().createFormHandler(referencedLovDefName, true, false);
            saxPArser.parse(inStream, handler);

            EJCoreFormProperties formProperties = handler.getFormProperties();
            if (formProperties == null)
            {
                throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_FORM_PROPERTIES_FOR_REUSABLE_LOV,
                        referencedLovDefName));
            }
            else
            {
                EJCoreLovDefinitionProperties lovDefinitionProperties = formProperties.getLovDefinitionProperties(referencedLovDefName);
                if (lovDefinitionProperties != null && !formProperties.getLovDefinitionContainer().getAllLovDefinitionProperties().isEmpty())
                {
                    lovDefinitionProperties = formProperties.getLovDefinitionContainer().getAllLovDefinitionProperties().iterator().next();
                }
                return lovDefinitionProperties;
            }
        }
        catch (Exception e)
        {
            throw new EJApplicationException(e);
        }
    }

    private InputStream getFormPropertiesDocumentForReusableBlock(String blockName)
    {
        // The reusable blocks are stored as a single block within a standard
        // form properties document. This enables
        // me to use the standard form reader to read the properties of the
        // reusable block and then retrieve the
        // required block properties from it

        InputStream inStream = null;

        String reusableBlockLoc = EJCoreProperties.getInstance().getReusableBlocksLocation();
        if (reusableBlockLoc == null || reusableBlockLoc.trim().length() == 0)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_REUSABLE_BLOCK_LOCATION_DEFINED));
        }

        if (EJFileLoader.fileExists(reusableBlockLoc + EJConstants.DIRECTORY_SEPERATOR + blockName + EJConstants.REUSABLE_BLOCK_PROPERTIES_FILE_SUFFIX))
        {
            inStream = EJFileLoader
                    .loadFile(reusableBlockLoc + EJConstants.DIRECTORY_SEPERATOR + blockName + EJConstants.REUSABLE_BLOCK_PROPERTIES_FILE_SUFFIX);
        }

        if (inStream == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_REUSABLE_BLOCK,
                    blockName + EJConstants.REUSABLE_BLOCK_PROPERTIES_FILE_SUFFIX));
        }

        return inStream;
    }

    private InputStream getFormPropertiesDocumentForObjectGroup(String blockName)
    {
        // The reusable objectgroups are stored standard
        // form properties document. This enables
        // me to use the standard form reader to read the properties of the
        // objectgroups and then retrieve the
        // required elements from it

        InputStream inStream = null;

        String objectgroupsLoc = EJCoreProperties.getInstance().getObjectGroupDefinitionLocation();
        if (objectgroupsLoc == null || objectgroupsLoc.trim().length() == 0)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_OBJECTGROUP_LOCATION_DEFINED));
        }

        if (EJFileLoader.fileExists(objectgroupsLoc + EJConstants.DIRECTORY_SEPERATOR + blockName + EJConstants.OBJECTGROUP_PROPERTIES_FILE_SUFFIX))
        {
            inStream = EJFileLoader.loadFile(objectgroupsLoc + EJConstants.DIRECTORY_SEPERATOR + blockName + EJConstants.OBJECTGROUP_PROPERTIES_FILE_SUFFIX);
        }

        if (inStream == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_OBJECTGROUP,
                    blockName + EJConstants.OBJECTGROUP_PROPERTIES_FILE_SUFFIX));
        }

        return inStream;
    }

    private InputStream getFormPropertiesDocumentForReusableLovDefinition(String lovDefName)
    {
        // The reusable lov definitions are stored as a single definition within
        // a standard form properties document. This enables me to use the
        // standard form reader to read the properties of the reusable lov
        // definition and then retrieve the required lov definition properties
        // from it

        InputStream inStream = null;

        String reusableDefLoc = EJCoreProperties.getInstance().getReusableLovDefinitionLocation();
        if (reusableDefLoc == null || reusableDefLoc.trim().length() == 0)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.NO_REUSABLE_LOV_LOCATION_DEFINED));
        }

        if (EJFileLoader.fileExists(reusableDefLoc + EJConstants.DIRECTORY_SEPERATOR + lovDefName + EJConstants.REUSABLE_LOVDEF_PROPERTIES_FILE_SUFFIX))
        {
            inStream = EJFileLoader
                    .loadFile(reusableDefLoc + EJConstants.DIRECTORY_SEPERATOR + lovDefName + EJConstants.REUSABLE_LOVDEF_PROPERTIES_FILE_SUFFIX);
        }

        if (inStream == null)
        {
            throw new EJApplicationException(EJMessageFactory.getInstance().createMessage(EJFrameworkMessage.UNABLE_TO_LOAD_REUSABLE_LOV,
                    lovDefName + EJConstants.REUSABLE_LOVDEF_PROPERTIES_FILE_SUFFIX));
        }

        return inStream;
    }
}
