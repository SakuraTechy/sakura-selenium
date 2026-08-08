package com.sakura.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Test;

public class ExecutionScopedMapTest {

    @Test
    public void shouldIsolateVariablesForConcurrentCaseScopes() throws Exception {
        ExecutionScopedMap variables = new ExecutionScopedMap("selenium");
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch release = new CountDownLatch(1);
        AtomicReference<String> first = new AtomicReference<>();
        AtomicReference<String> second = new AtomicReference<>();

        Thread firstThread = new Thread(() -> runScoped(variables, "first", ready, release, first));
        Thread secondThread = new Thread(() -> runScoped(variables, "second", ready, release, second));
        firstThread.start();
        secondThread.start();
        ready.await();
        release.countDown();
        firstThread.join();
        secondThread.join();

        Assert.assertEquals("first", first.get());
        Assert.assertEquals("second", second.get());
    }

    @Test
    public void shouldClearCaseVariablesAfterScopeEnds() {
        ExecutionScopedMap variables = new ExecutionScopedMap("selenium");
        ExecutionScopedMap.beginCaseScope();
        variables.put("token", "case-value");
        ExecutionScopedMap.endCaseScope();

        Assert.assertNull(variables.get("token"));
    }

    private void runScoped(ExecutionScopedMap variables,
                           String value,
                           CountDownLatch ready,
                           CountDownLatch release,
                           AtomicReference<String> result) {
        ExecutionScopedMap.beginCaseScope();
        try {
            variables.put("same_name", value);
            ready.countDown();
            release.await();
            result.set(String.valueOf(variables.get("same_name")));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            ExecutionScopedMap.endCaseScope();
        }
    }
}
