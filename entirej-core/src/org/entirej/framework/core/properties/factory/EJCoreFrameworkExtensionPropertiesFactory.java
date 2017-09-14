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

import org.entirej.framework.core.extensions.properties.EJCoreFrameworkExtensionProperties;
import org.entirej.framework.core.extensions.properties.EJCoreFrameworkExtensionProperty;
import org.entirej.framework.core.properties.definitions.EJPropertyDefinitionType;
import org.entirej.framework.core.properties.definitions.interfaces.EJFrameworkExtensionProperties;
import org.entirej.framework.core.properties.interfaces.EJBlockProperties;
import org.entirej.framework.core.properties.interfaces.EJFormProperties;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EJCoreFrameworkExtensionPropertiesFactory
{
    private EJFormProperties  _formProperties;
    private EJBlockProperties _blockProperties;
    
    /**
     * this factory should only be used within the Properties package.
     */
    public EJCoreFrameworkExtensionPropertiesFactory(EJFormProperties formProperties, EJBlockProperties blockProperties)
    {
        _formProperties = formProperties;
        _blockProperties = blockProperties;
    }
    
    public EJFrameworkExtensionProperties createExtensionProperties(Node renderingNode)
    {
        if (renderingNode == null)
        {
            throw new NullPointerException("The renderingNode passed to createExtensionProperties was null");
        }
        
        // Create an instance of the extensionProperties. This will then be
        // populated by the properties within the node specified. If the
        // node does not contain ExtensionProperties then an empty
        // object will be returned
        EJCoreFrameworkExtensionProperties extensionProperties = new EJCoreFrameworkExtensionProperties(_formProperties, _blockProperties, "MAIN", null);
        
        EJCoreFrameworkExtensionProperty property;
        
        NodeList children = renderingNode.getChildNodes();
        for (int j = 0; j < children.getLength(); j++)
        {
            Node childNode = children.item(j);
            
            if (childNode.getNodeType() == 1)
            {
                if (childNode.getNodeName().equals("propertyGroup"))
                {
                    addPropertyGroup(extensionProperties, childNode.getAttributes().getNamedItem("name").getNodeValue(), childNode);
                }
                else
                {
                    String propertyName = childNode.getAttributes().getNamedItem("name").getNodeValue();
                    String multilingual = childNode.getAttributes().getNamedItem("multilingual").getNodeValue();
                    String propertyType = childNode.getAttributes().getNamedItem("propertyType").getNodeValue();
                    property = new EJCoreFrameworkExtensionProperty(EJPropertyDefinitionType.valueOf(propertyType), propertyName, Boolean.parseBoolean(multilingual));
                    
                    addPropertyValue(extensionProperties, property, childNode);
                }
            }
        }
        return extensionProperties;
    }
    
    private void addPropertyGroup(EJCoreFrameworkExtensionProperties parentProperties, String name, Node renderNode)
    {
        EJCoreFrameworkExtensionProperties extensionPropertiesGroup = new EJCoreFrameworkExtensionProperties(_formProperties, _blockProperties, name, parentProperties);
        parentProperties.addPropertyGroup(extensionPropertiesGroup);
        
        String groupName;
        EJCoreFrameworkExtensionProperty property;
        
        NodeList children = renderNode.getChildNodes();
        for (int j = 0; j < children.getLength(); j++)
        {
            Node childNode = children.item(j);
            
            if (childNode.getNodeType() == 1)
            {
                if (childNode.getNodeName().equals("propertyGroup"))
                {
                    groupName = childNode.getAttributes().getNamedItem("name").getNodeValue();
                    addPropertyGroup(extensionPropertiesGroup, groupName, childNode);
                }
                else
                {
                    String propertyName = childNode.getAttributes().getNamedItem("name").getNodeValue();
                    String multilingual = childNode.getAttributes().getNamedItem("multilingual").getNodeValue();
                    String propertyType = childNode.getAttributes().getNamedItem("propertyType").getNodeValue();
                    
                    property = new EJCoreFrameworkExtensionProperty(EJPropertyDefinitionType.valueOf(propertyType), propertyName, Boolean.parseBoolean(multilingual));
                    
                    addPropertyValue(extensionPropertiesGroup, property, childNode);
                }
            }
        }
    }
    
    private void addPropertyValue(EJCoreFrameworkExtensionProperties parentProperties, EJCoreFrameworkExtensionProperty property, Node valueNode)
    {
        if (valueNode.getChildNodes().getLength() == 1)
        {
            property.setValue(valueNode.getFirstChild().getNodeValue());
            parentProperties.addProperty(property);
        }
        else
        {
            parentProperties.addProperty(property);
        }
    }
}
