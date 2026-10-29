package com.sakura.util;

import java.io.StringReader;
import java.lang.reflect.Method;
import javax.xml.parsers.DocumentBuilderFactory;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;
import com.sakura.base.TestStep;
import com.sakura.service.WebXmlParseService;

/** 只验证旧 XML、替换和变量引用契约，不连接服务器或启动浏览器。 */
public class ExecuteShellResultContractTest {
    @Test
    public void shouldPreserveEmptyReplacementThroughRealXmlParser() throws Exception {
        TestStep step = parse("<step key=\"[\\r\\n]+\" value=\"\" details=\"key:shell_fixture_disk\"/>");
        Assert.assertEquals("", step.getValue());
        String result = StringUtil.replaceAllBlank("0012\r\n", step.getKey(), step.getValue());
        Assert.assertEquals("0012", result);
        Object previous = SeleniumUtil.localmap.get("shell_fixture_disk");
        try {
            SeleniumUtil.localmap.put(step.getDetails().get("key"), result);
            Assert.assertEquals("0012", SeleniumUtil.parseStringHasEls("${shell_fixture_disk}"));
        } finally {
            if (previous == null) SeleniumUtil.localmap.remove("shell_fixture_disk");
            else SeleniumUtil.localmap.put("shell_fixture_disk", previous);
        }
    }

    @Test
    public void shouldKeepWhitespaceAndCaptureReferences() throws Exception {
        TestStep whitespace = parse("<step key=\"[, ]\" value=\"&#x9;&#xA;\" details=\"key:disk\"/>");
        Assert.assertEquals("\t\n", whitespace.getValue());
        Assert.assertEquals("a\t\nb", StringUtil.replaceAllBlank("a,b", whitespace.getKey(), whitespace.getValue()));
        TestStep group = parse("<step key=\"^size=(\\d+)$\" value=\"$1\" details=\"key:disk\"/>");
        Assert.assertEquals("0012", StringUtil.replaceAllBlank("size=0012", group.getKey(), group.getValue()));
    }

    private TestStep parse(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        NamedNodeMap attributes = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)))
            .getDocumentElement().getAttributes();
        Method method = WebXmlParseService.class.getDeclaredMethod("initByAttributes", NamedNodeMap.class, Object.class);
        method.setAccessible(true);
        return (TestStep)method.invoke(null, attributes, new TestStep());
    }
}
