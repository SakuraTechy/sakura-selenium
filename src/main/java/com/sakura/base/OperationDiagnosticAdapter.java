package com.sakura.base;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Selenium legacy Step JSON 的统一执行详情附加适配器。
 *
 * <p>只追加 operation，不改变既有 report 字段；受限定义、密码和路径不进入普通摘要。</p>
 */
public final class OperationDiagnosticAdapter {

    private static final Map<String, String> PROFILES = new LinkedHashMap<>();
    private static final List<String> DETAIL_INPUT_KEYS = Arrays.asList(
        "variable_name", "source_type", "read_mode", "regex", "regex_group", "replace_from", "replace_to",
        "date_mode", "format", "datetime", "offset_seconds", "timestamp_unit", "info_type", "ip_prefix",
        "start", "end", "timeout_ms", "profile", "property_key", "expression", "scale",
        "keep_trailing_zeros", "remote_path", "file_ref", "certificate_ref", "path", "target_ref",
        "option", "attribute", "duration_ms", "x", "y"
    );

    static {
        for (String action : Arrays.asList("web-geturl", "web-geturls", "web-refresh", "web-close", "web-quit",
            "switch-Iframe", "return-Iframe", "quit-Iframe", "switch-window", "switch-windows")) {
            PROFILES.put(action.toLowerCase(Locale.ROOT), "navigation");
        }
        for (String action : Arrays.asList("web-click", "web-clicks", "android-click", "select-click", "input-click",
            "web-input", "web-inputdate", "web-inputfile", "web-inputfiles", "web-inputzs", "web-inputclear",
            "windows-keybg", "windows-keybc", "windows-skeybc", "windows-skeybcm", "move-byoffset", "move-toelement",
            "scroll-element")) {
            PROFILES.put(action.toLowerCase(Locale.ROOT), "element_interaction");
        }
        for (String action : Arrays.asList("click-ok", "click-cancel", "click-text")) {
            PROFILES.put(action, "dialog");
        }
        for (String action : Arrays.asList("web-check", "web-notcheck", "web-check-invisible", "web-checkvalue",
            "web-checkjs", "web-checklist", "web-notchecklist", "web-regexchecklist", "web-checksetlist",
            "web-notchecksetlist", "web-fuzzycheck", "web-assert-element-match")) {
            PROFILES.put(action, "assertion");
        }
        for (String action : Arrays.asList("wait-forced", "web-implicit", "web-display", "android-implicit")) {
            PROFILES.put(action, "wait");
        }
        for (String action : Arrays.asList("web-set", "web-setdate", "web-setsysinfo", "web-setusableip",
            "web-setproperties", "web-setcalculationformula", "android-set", "web-getcode")) {
            PROFILES.put(action, "variable");
        }
        PROFILES.put("javascript-executor", "script");
        for (String action : Arrays.asList("windows-cmd", "exe-shell", "free-sftp", "free-sftps", "get-file",
            "get-files", "delete-file", "delete-files", "db-inserta", "db-insertw", "db-deletea", "db-deletew",
            "db-updatea", "db-updatew", "db-querya", "db-queryw", "db-queryws", "db-procedurea", "db-procedurew")) {
            PROFILES.put(action, "infrastructure");
        }
        PROFILES.put("mouse-move", "infrastructure");
    }

    private OperationDiagnosticAdapter() {
    }

    public static JSONObject attach(TestStep step, JSONObject result, String executor) {
        JSONObject operation = new JSONObject(true);
        String legacyAction = step == null || step.getAction() == null ? "custom" : step.getAction().key();
        String action = step == null || text(step.getActionType()).isEmpty() ? legacyAction : text(step.getActionType());
        String normalizedAction = action.toLowerCase(Locale.ROOT);
        String profile = text(step == null ? null : step.getDiagnosticProfile());
        if (profile.isEmpty()) {
            profile = PROFILES.getOrDefault(normalizedAction, PROFILES.getOrDefault(legacyAction.toLowerCase(Locale.ROOT), "generic"));
        }
        operation.put("schema_version", 1);
        putIfText(operation, "catalog_version", step == null ? null : step.getCatalogVersion());
        operation.put("profile", profile);
        operation.put("executor", text(executor).isEmpty() ? "selenium" : executor);

        JSONObject method = new JSONObject(true);
        putIfText(method, "type_code", step == null ? null : step.getTypeCode());
        putIfText(method, "type_label", step == null ? null : step.getTypeLabel());
        putIfText(method, "method_code", step == null ? null : step.getMethodCode());
        if (step != null && step.getMethodVersion() != null) method.put("method_version", step.getMethodVersion());
        putIfText(method, "method_label", step == null ? null : step.getMethodLabel());
        method.put("action_type", action);
        operation.put("method", method);
        operation.put("summary", summary(action));
        operation.put("inputs", inputs(step));

        JSONObject outcome = new JSONObject(true);
        outcome.put("kind", profile);
        outcome.put("status", result == null ? "unknown" : result.getString("status"));
        outcome.put("summary", summary(action));
        JSONArray facts = new JSONArray();
        for (String key : Arrays.asList("exit_code", "affected_rows", "row_count", "wait_duration_ms")) {
            if (result != null && result.containsKey(key)) facts.add(fact(key, result.get(key)));
        }
        outcome.put("facts", facts);
        operation.put("outcome", outcome);
        result.put("details", mergeDetails(result.get("details"), operation));
        return result;
    }

    private static JSONObject mergeDetails(Object raw, JSONObject operation) {
        JSONObject details = raw instanceof JSONObject ? (JSONObject)raw : new JSONObject(true);
        details.put("operation", operation);
        return details;
    }

    private static JSONArray inputs(TestStep step) {
        JSONArray inputs = new JSONArray();
        if (step == null) return inputs;
        addInput(inputs, "url", step.getUrl(), "input", step);
        addInput(inputs, "target_ref", step.getLocator(), "target", step);
        addInput(inputs, "value", step.getValue(), "input", step);
        addInput(inputs, "expect", step.getExpect(), "expected", step);
        addInput(inputs, "read_mode", step.getRead_mode(), "control", step);
        addInput(inputs, "match_mode", step.getMatch_mode(), "control", step);
        addInput(inputs, "key", step.getKey(), "input", step);
        addInput(inputs, "modifier", step.getKeys(), "input", step);
        addInput(inputs, "duration_ms", step.getWaitTime(), "control", step);
        addInput(inputs, "timeout_ms", step.getTimeout(), "control", step);
        addInput(inputs, "regex", step.getRegex(), "expected", step);
        addInput(inputs, "path", step.getLocalpath(), "input", step);
        addInput(inputs, "file_ref", step.getCatalogue(), "input", step);
        addInput(inputs, "remote_path", step.getRemotepath(), "input", step);
        addInput(inputs, "database", step.getDatabase(), "target", step);
        addInput(inputs, "server", step.getServer(), "target", step);
        addInput(inputs, "command", step.getShell(), "definition", step);
        addInput(inputs, "sql", step.getSql(), "definition", step);
        addInput(inputs, "script", step.getScript(), "definition", step);
        if (step.getDetails() != null) {
            for (String key : DETAIL_INPUT_KEYS) {
                addInput(inputs, key, step.getDetails().get(key), inputRole(key), step);
            }
        }
        return inputs;
    }

    private static void addInput(JSONArray inputs, String key, String value, String role, TestStep step) {
        if (value == null || value.trim().isEmpty()) return;
        for (Object existing : inputs) {
            if (existing instanceof JSONObject && key.equals(((JSONObject)existing).getString("key"))) return;
        }
        JSONObject input = new JSONObject(true);
        input.put("key", key);
        input.put("role", role);
        JSONObject display = display(key, value, step);
        input.put("effective", display);
        input.put("source", source(key, value));
        inputs.add(input);
    }

    private static JSONObject source(String key, String value) {
        String code;
        String label;
        if (value != null && value.matches(".*\\$\\{[^{}]+}.*")) {
            code = "variable_reference";
            String reference = value.replaceFirst(".*\\$\\{([^{}]+)}.*", "$1");
            label = "引用变量：" + reference;
        } else if (Arrays.asList("command", "sql", "script").contains(key)) {
            code = "definition_snapshot";
            label = "定义快照";
        } else {
            code = "literal";
            label = "固定值";
        }
        JSONObject source = new JSONObject(true);
        source.put("code", code);
        source.put("label", label);
        return source;
    }

    private static String inputRole(String key) {
        if (Arrays.asList("target_ref", "database", "server").contains(key)) return "target";
        if (Arrays.asList("expect", "regex", "attribute").contains(key)) return "expected";
        if (Arrays.asList("command", "sql", "script").contains(key)) return "definition";
        if ("variable_name".equals(key)) return "binding";
        if (Arrays.asList("duration_ms", "timeout_ms", "index").contains(key)) return "control";
        return "input";
    }

    private static JSONObject display(String key, String value, TestStep step) {
        JSONObject display = new JSONObject(true);
        if (isSensitive(key, step)) {
            display.put("value_state", "masked");
        } else if (Arrays.asList("command", "sql", "script").contains(key)) {
            display.put("value_state", "restricted");
        } else if (key.contains("path")) {
            display.put("value_state", "visible");
            display.put("preview", basename(value));
        } else {
            display.put("value_state", "visible");
            display.put("preview", limit(value));
        }
        return display;
    }

    private static boolean isSensitive(String key, TestStep step) {
        String locator = step == null ? "" : text(step.getLocator()).toLowerCase(Locale.ROOT);
        return key.matches("(?i).*(password|passwd|pwd|token|secret|authorization|credential).*" )
            || ("value".equals(key) && locator.matches(".*(password|passwd|pwd|token|secret).*"));
    }

    private static JSONObject fact(String key, Object value) {
        JSONObject fact = new JSONObject(true);
        fact.put("key", key);
        JSONObject display = new JSONObject(true);
        display.put("value_state", "visible");
        display.put("preview", limit(value));
        fact.put("value", display);
        return fact;
    }

    private static String summary(String action) {
        Map<String, String> labels = new LinkedHashMap<>();
        labels.put("web-geturl", "页面导航完成");
        labels.put("web-click", "点击完成");
        labels.put("web-input", "输入完成");
        labels.put("web-check", "断言检查通过");
        labels.put("wait-forced", "固定等待完成");
        labels.put("javascript-executor", "脚本执行完成");
        labels.put("windows-cmd", "服务器命令执行完成");
        labels.put("db-querya", "数据库查询完成");
        return labels.getOrDefault(action, "动作 " + action + " 执行完成");
    }

    private static String basename(String value) {
        String[] parts = value.split("[\\\\/]");
        return parts.length == 0 ? value : parts[parts.length - 1];
    }

    private static String limit(Object value) {
        String text = String.valueOf(value).replaceAll("[\\r\\n]+", " ");
        return text.length() > 512 ? text.substring(0, 512) + "…" : text;
    }

    private static String text(String value) {
        return value == null ? "" : value.trim();
    }

    private static void putIfText(JSONObject target, String key, String value) {
        if (!text(value).isEmpty()) target.put(key, text(value));
    }
}
