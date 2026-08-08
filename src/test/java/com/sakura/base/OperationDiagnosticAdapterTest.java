package com.sakura.base;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class OperationDiagnosticAdapterTest {

    @Test
    public void allCatalogMethodsKeepTheSameProfileAndMethodIdentity() throws Exception {
        Path fixturePath = Paths.get(System.getProperty("user.dir"), "..", "sakura-admin", "continew-automation",
            "src", "test", "resources", "automation", "automation-operation-63-fixture.json").normalize();
        Path catalogPath = Paths.get(System.getProperty("user.dir"), "..", "sakura-admin", "continew-automation",
            "src", "main", "resources", "automation", "automation-operation-catalog.json").normalize();
        JSONObject fixture = JSON.parseObject(new String(Files.readAllBytes(fixturePath), StandardCharsets.UTF_8));
        JSONObject catalog = JSON.parseObject(new String(Files.readAllBytes(catalogPath), StandardCharsets.UTF_8));
        Map<String, String> profiles = new HashMap<>();
        JSONObject profileCatalog = catalog.getJSONObject("diagnostic_profiles");
        for (String profile : profileCatalog.keySet()) {
            JSONArray methods = profileCatalog.getJSONArray(profile);
            for (Object method : methods) profiles.put(String.valueOf(method), profile);
        }

        Assert.assertEquals(fixture.getJSONArray("methods").size(), 63);
        for (Object item : fixture.getJSONArray("methods")) {
            JSONObject method = (JSONObject) item;
            TestStep step = new TestStep();
            step.setAction(StepAction.action(method.getString("legacy_action")));
            step.setCatalogVersion(fixture.getString("catalog_version"));
            step.setMethodCode(method.getString("method_code"));
            JSONObject result = new JSONObject(true);
            result.put("status", "passed");
            OperationDiagnosticAdapter.attach(step, result, "selenium");

            JSONObject operation = result.getJSONObject("details").getJSONObject("operation");
            Assert.assertEquals(operation.getJSONObject("method").getString("method_code"), method.getString("method_code"));
            Assert.assertEquals(operation.getString("profile"), profiles.get(method.getString("method_code")));
            Assert.assertEquals(operation.getJSONObject("outcome").getString("kind"), profiles.get(method.getString("method_code")));
        }
    }

    @Test
    public void appendsSharedOperationDetailAndMasksRestrictedCommand() {
        TestStep step = new TestStep();
        step.setAction(StepAction.EXE_SHELL);
        step.setMethodCode("server.shell");
        step.setDiagnosticProfile("infrastructure");
        step.setShell("curl -H \"Authorization: Bearer secret-token\" /health");

        JSONObject result = new JSONObject(true);
        result.put("status", "passed");
        JSONObject details = new JSONObject(true);
        details.put("infrastructure", new JSONObject(true));
        result.put("details", details);
        OperationDiagnosticAdapter.attach(step, result, "selenium");

        Assert.assertNotNull(result.getJSONObject("details").getJSONObject("infrastructure"));
        JSONObject operation = result.getJSONObject("details").getJSONObject("operation");
        Assert.assertEquals(operation.getString("profile"), "infrastructure");
        Assert.assertEquals(operation.getJSONArray("inputs").getJSONObject(0).getJSONObject("effective").getString("value_state"), "restricted");
        Assert.assertEquals(operation.getJSONArray("inputs").getJSONObject(0).getJSONObject("source").getString("code"), "definition_snapshot");
        Assert.assertEquals(operation.getJSONArray("inputs").getJSONObject(0).getJSONObject("source").getString("label"), "定义快照");
        Assert.assertFalse(result.toJSONString().contains("secret-token"));
    }

    @Test
    public void appendsLegacyAndCanonicalInputFieldsWithoutChangingReportFields() {
        TestStep step = new TestStep();
        step.setAction(StepAction.WEB_SETDATE);
        step.setMethodCode("global.variable.date");
        step.setUrl("https://example.test/login");
        step.setWaitTime("1200");
        Map<String, String> details = new HashMap<>();
        details.put("variable_name", "run.date");
        details.put("date_mode", "offset");
        details.put("format", "yyyy-MM-dd");
        details.put("timestamp_unit", "second");
        step.setDetails(details);

        JSONObject result = new JSONObject(true);
        result.put("status", "passed");
        result.put("legacy_field", "kept");
        OperationDiagnosticAdapter.attach(step, result, "selenium");

        JSONObject operation = result.getJSONObject("details").getJSONObject("operation");
        Assert.assertEquals(result.getString("legacy_field"), "kept");
        Assert.assertEquals(findInput(operation, "variable_name").getJSONObject("effective").getString("preview"), "run.date");
        Assert.assertEquals(findInput(operation, "timestamp_unit").getJSONObject("effective").getString("preview"), "second");
        Assert.assertEquals(findInput(operation, "url").getJSONObject("effective").getString("preview"), "https://example.test/login");
        Assert.assertEquals(findInput(operation, "variable_name").getJSONObject("source").getString("code"), "literal");
    }

    @Test
    public void prefersFrozenCanonicalActionTypeOverLegacyAction() {
        TestStep step = new TestStep();
        step.setAction(StepAction.WEB_INPUT);
        step.setActionType("input");
        step.setMethodCode("input.text");
        step.setMethodLabel("输入文本");
        step.setDiagnosticProfile("element_interaction");

        JSONObject result = new JSONObject(true);
        result.put("status", "passed");
        OperationDiagnosticAdapter.attach(step, result, "selenium");

        JSONObject operation = result.getJSONObject("details").getJSONObject("operation");
        Assert.assertEquals(operation.getJSONObject("method").getString("action_type"), "input");
        Assert.assertEquals(operation.getJSONObject("method").getString("method_label"), "输入文本");
        Assert.assertEquals(operation.getString("profile"), "element_interaction");
    }

    private JSONObject findInput(JSONObject operation, String key) {
        for (Object item : operation.getJSONArray("inputs")) {
            JSONObject input = (JSONObject)item;
            if (key.equals(input.getString("key"))) return input;
        }
        Assert.fail("missing input: " + key);
        return null;
    }
}
