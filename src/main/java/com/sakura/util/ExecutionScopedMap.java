package com.sakura.util;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 兼容历史静态 Map API 的用例级变量容器。
 *
 * <p>旧 Handler 直接访问 {@code SeleniumUtil.localmap}，因此不能直接替换字段类型；
 * 此 Map 门面会在用例范围内按线程委派到独立数据，未进入用例范围时仍使用原有回退 Map。</p>
 */
public final class ExecutionScopedMap extends AbstractMap<String, Object> {

    private static final ThreadLocal<Map<String, Map<String, Object>>> CASE_SCOPES = new ThreadLocal<>();

    private final String namespace;
    private final Map<String, Object> legacyFallback = new LinkedHashMap<>();

    public ExecutionScopedMap(String namespace) {
        this.namespace = namespace;
    }

    public static void beginCaseScope() {
        CASE_SCOPES.set(new HashMap<>());
    }

    public static void endCaseScope() {
        CASE_SCOPES.remove();
    }

    @Override
    public Object put(String key, Object value) {
        return delegate().put(key, value);
    }

    @Override
    public Object get(Object key) {
        return delegate().get(key);
    }

    @Override
    public Object remove(Object key) {
        return delegate().remove(key);
    }

    @Override
    public void clear() {
        delegate().clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate().containsKey(key);
    }

    @Override
    public int size() {
        return delegate().size();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return delegate().entrySet();
    }

    private Map<String, Object> delegate() {
        Map<String, Map<String, Object>> scopes = CASE_SCOPES.get();
        if (scopes == null) {
            return legacyFallback;
        }
        return scopes.computeIfAbsent(namespace, ignored -> new LinkedHashMap<>());
    }
}
