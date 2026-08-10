package org.entirej.framework.core.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class TestEJXmlParserFactory
{
    private static final String DOCTYPE_XML = """
            <?xml version="1.0"?>
            <!DOCTYPE root [<!ENTITY injected "expanded">]>
            <root>&injected;</root>
            """;

    @Test
    void saxParserAcceptsRegularXml() throws Exception
    {
        EJXmlParserFactory.newSaxParser().parse(xml("<root><value>ok</value></root>"), new DefaultHandler());
    }

    @Test
    void saxParserRejectsDoctypeDeclarations() throws Exception
    {
        assertThrows(SAXException.class,
                () -> EJXmlParserFactory.newSaxParser().parse(xml(DOCTYPE_XML), new DefaultHandler()));
    }

    @Test
    void documentBuilderAcceptsNamespacedXml() throws Exception
    {
        Document document = EJXmlParserFactory.newDocumentBuilder(true)
                .parse(xml("<root xmlns=\"urn:entirej:test\"/>"));

        assertEquals("urn:entirej:test", document.getDocumentElement().getNamespaceURI());
    }

    @Test
    void documentBuilderRejectsDoctypeDeclarations()
    {
        assertThrows(SAXException.class,
                () -> EJXmlParserFactory.newDocumentBuilder(true).parse(xml(DOCTYPE_XML)));
    }

    private static InputStream xml(String value)
    {
        return new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
    }
}
