package com.yucl.learndemo;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TestGraalvmJs {
    public static void main(String[] args) {
        ScriptEngine graalEngine = new ScriptEngineManager().getEngineByName("graal.js");
        try {
            graalEngine.eval("print('Hello World!');");
            try (Context context = Context.newBuilder()
                    .allowAllAccess(true)
                    .build()) {
                java.math.BigDecimal v = context.eval("js",
                                "var BigDecimal = Java.type('java.math.BigDecimal');" +
                                        "BigDecimal.valueOf(10).pow(20)")
                        .asHostObject();
                assert v.toString().equals("100000000000000000000");
            }

            try (Context context = Context.create()) {
                ComputedArray arr = new ComputedArray();
                context.getBindings("js").putMember("arr", arr);
                long result = context.eval("js",
                                "arr[1] + arr[1000000000]")
                        .asLong();
                assert result == 2000000002L;
            }
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    static class ComputedArray implements ProxyArray {
        public Object get(long index) {
            return index * 2;
        }
        public void set(long index, Value value) {
            throw new UnsupportedOperationException();
        }
        public long getSize() {
            return Long.MAX_VALUE;
        }
    }
}
