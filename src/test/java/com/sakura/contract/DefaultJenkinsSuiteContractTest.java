package com.sakura.contract;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DefaultJenkinsSuiteContractTest {

    @Test
    public void shouldResolveDefaultExtentSuiteToExecutableTestClass() throws Exception {
        Path projectRoot = Paths.get("").toAbsolutePath().normalize();
        Document extentSuite = parse(projectRoot.resolve("src/test/java/TestRunXml/ExtentReport.xml"));
        Element suiteFile = (Element)XPathFactory.newInstance()
            .newXPath()
            .evaluate("/suite/suite-files/suite-file", extentSuite, XPathConstants.NODE);
        Assert.assertNotNull("默认 Extent suite-file 不能为空", suiteFile);

        Path testngSuitePath = projectRoot.resolve(suiteFile.getAttribute("path")).normalize();
        Assert.assertTrue("默认 TestNG suite 文件不存在", Files.isRegularFile(testngSuitePath));
        Document testngSuite = parse(testngSuitePath);

        Element browser = (Element)XPathFactory.newInstance()
            .newXPath()
            .evaluate("/suite/test/parameter[@name='browser']", testngSuite, XPathConstants.NODE);
        Element testClass = (Element)XPathFactory.newInstance()
            .newXPath()
            .evaluate("/suite/test/classes/class", testngSuite, XPathConstants.NODE);
        Element dependency = (Element)XPathFactory.newInstance()
            .newXPath()
            .evaluate("/suite/test/groups/dependencies/group", testngSuite, XPathConstants.NODE);
        Assert.assertEquals("chrome", browser.getAttribute("value"));
        Assert.assertEquals("BD_001", dependency.getAttribute("depends-on"));
        Assert.assertEquals("BD_002", dependency.getAttribute("name"));

        String className = testClass.getAttribute("name");
        Path sourcePath = projectRoot
            .resolve("src/test/java")
            .resolve(className.replace('.', '/') + ".java")
            .normalize();
        Assert.assertTrue("默认 TestNG 测试类不存在", Files.isRegularFile(sourcePath));
        String source = Files.readString(sourcePath, StandardCharsets.UTF_8);
        String compactSource = source.replaceAll("\\s+", "");
        Assert.assertTrue(compactSource.contains("@Parameters({\"browser\",\"profile\"})"));
        Assert.assertTrue(compactSource.contains("groups={\"BD_001\"}"));
    }

    private Document parse(Path path) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        try (InputStream input = Files.newInputStream(path)) {
            return factory.newDocumentBuilder().parse(input);
        }
    }
}
