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

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.entirej.framework.core.common.utils.EJParameterChecker;
import org.entirej.framework.core.interfaces.EJConnectionFactory;
import org.entirej.framework.core.interfaces.EJFrameworkConnection;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EJDefaultConnectionFactory implements EJConnectionFactory
{
    private XPathFactory _xpathFactory;
    private XPath        _xpath;

    private String       _connectionUrl = "";
    private String       _username;
    private String       _password;

    public EJDefaultConnectionFactory()
    {
        config();
    }
    
    @Override
    public EJFrameworkConnection createConnection(EJFrameworkManager frameworkManager)
    {
       return new EJDefaultFrameworkConnection(this);
        
    }
    
    Connection createInternalConnection()
    {
       
        Properties userProps = new Properties();
        if (_username != null)
        {
            userProps.put("user", _username);
        }
        if (_password != null)
        {
            userProps.put("password", _password);
        }
        Connection con = null;
        try
        {
            con = DriverManager.getConnection(_connectionUrl, userProps);

            // Need to set autocommit false to allow EntireJ to handle
            // transactions
            con.setAutoCommit(false);
        }
        catch (SQLException e)
        {
            throw new EJApplicationException(new EJMessage(e.getMessage()), e);
        }
        return con;
    }
    
    private void config()
    {
        /*
         * Load the properties file to get the connection information from the
         * Connection.properties file
         */
        Properties prop = loadParams();

        try
        {
            Class.forName(prop.getProperty("driverClassName"));
        }
        catch (ClassNotFoundException e)
        {
            throw new EJApplicationException(new EJMessage("The driver class name entered within the Connection.properties file cannot be found"), e);
        }

        /* Set Host name */
        _connectionUrl = prop.getProperty("url");

        /* Set User name */
        _username = prop.getProperty("username");

        /* Set Password */
        _password = prop.getProperty("password");
    }

    private Properties loadParams()
    {
        Properties properties = new Properties();
        Document document = loadFileFromClasspath("Connection.properties");

        String driverClassNameStmt = "/propertyList/property[@name=\"driverClassName\"]/text()";
        String urlStmt = "/propertyList/property[@name=\"url\"]/text()";
        String usernameStmt = "/propertyList/property[@name=\"username\"]/text()";
        String passwordStmt = "/propertyList/property[@name=\"password\"]/text()";

        try
        {
            if (driverClassNameStmt == null || urlStmt == null || usernameStmt == null || passwordStmt == null)
            {
                StringBuilder builder = new StringBuilder();
                builder.append("The Connection.properties file is mallformed. Please check the format and ensure it is as follows:\n\n");
                builder.append("    <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                builder.append("        <propertyList>\n");
                builder.append("        <property name=\"driverClassName\"></property>\n");
                builder.append("        <property name=\"url\"></property>\n");
                builder.append("        <property name=\"username\"></property>\n");
                builder.append("        <property name=\"password\"></property>\n");
                builder.append("    </propertyList>");

                throw new EJApplicationException(new EJMessage(builder.toString()));
            }

            NodeList nodeList = (NodeList) getXpath().evaluate(driverClassNameStmt.toString(), document, XPathConstants.NODESET);
            String nodeValue;

            if (nodeList.getLength() == 0)
            {
                throw new EJApplicationException(new EJMessage("Please enter a value for the Driver Name within your Connection.properties file"));
            }
            nodeValue = nodeList.item(0).getNodeValue();
            properties.put("driverClassName", nodeValue);

            nodeList = (NodeList) getXpath().evaluate(urlStmt.toString(), document, XPathConstants.NODESET);
            nodeValue = nodeList.item(0).getNodeValue();
            properties.put("url", nodeValue);

            nodeList = (NodeList) getXpath().evaluate(usernameStmt.toString(), document, XPathConstants.NODESET);
            if (nodeList != null && nodeList.item(0) != null)
            {
                nodeValue = nodeList.item(0).getNodeValue();
                properties.put("username", nodeValue);
            }

            nodeList = (NodeList) getXpath().evaluate(passwordStmt.toString(), document, XPathConstants.NODESET);
            if (nodeList != null && nodeList.item(0) != null)
            {
                nodeValue = nodeList.item(0).getNodeValue();
                properties.put("password", nodeValue);
            }
        }
        catch (XPathExpressionException e)
        {
            throw new IllegalStateException("The Connection.properties file is not of a correct XML format.");
        }

        return properties;
    }

    public Document loadFileFromClasspath(String fileName)
    {
        EJParameterChecker.checkNotZeroLength(fileName, "loadFileFromClasspath", "fileName");

        InputStream inStream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (inStream == null)
        {
            throw new IllegalArgumentException("The file name passed to loadFileFromClasspath is invalid and cannot be found. FileName: " + fileName);
        }

        try
        {
            Document document = null;
            // Initiate DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // To get a validating parser
            factory.setValidating(false);
            // To get one that understands namespaces
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parse and load into memory the Document
            document = builder.parse(inStream);

            return document;
        }
        catch (IOException e)
        {
            throw new EJApplicationException(new EJMessage(
                    "Unable to validate the contents of the given file contents passed to loadFileFromClasspath. FileName: " + fileName), e);
        }
        catch (ParserConfigurationException e)
        {
            throw new EJApplicationException(new EJMessage(
                    "Unable to validate the contents of the given file contents passed to loadFileFromClasspath. FileName: " + fileName), e);
        }
        catch (SAXException e)
        {
            throw new EJApplicationException(new EJMessage(
                    "Unable to validate the contents of the given file contents passed to loadFileFromClasspath. FileName: " + fileName), e);
        }
    }

    public XPath getXpath()
    {
        if (_xpath == null)
        {
            try
            {
                _xpathFactory = XPathFactory.newInstance(XPathFactory.DEFAULT_OBJECT_MODEL_URI);
                _xpath = _xpathFactory.newXPath();
            }
            catch (XPathFactoryConfigurationException e)
            {
                throw new EJApplicationException(new EJMessage("Unable to initialise the XPath Factory needed to read the Properties"), e);
            }
        }
        return _xpath;
    }
}
