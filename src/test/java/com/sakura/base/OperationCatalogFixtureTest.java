package com.sakura.base;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/** 统一 63 条目录 fixture 的 Selenium legacy action 覆盖契约。 */
public class OperationCatalogFixtureTest {

    @Test
    public void everyFixtureLegacyActionMustHaveSeleniumHandler() throws Exception {
        Path fixture = Paths.get("..", "sakura-admin", "continew-automation", "src", "test", "resources", "automation",
            "automation-operation-63-fixture.json");
        JSONObject root = JSON.parseObject(new String(Files.readAllBytes(fixture), StandardCharsets.UTF_8));
        List<Map<String, Object>> methods = root.getObject("methods", List.class);
        Assert.assertNotNull(methods, "统一目录 fixture 不存在 methods");
        for (Map<String, Object> method : methods) {
            String legacyAction = String.valueOf(method.get("legacy_action"));
            Assert.assertNotNull(StepAction.action(legacyAction), "Selenium 未注册 legacy action: " + legacyAction);
        }
    }
}
