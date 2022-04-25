package com.yucl.learndemo;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class TestGroovy {
    public static void main(String[] args) {
        ScriptEngineManager engineManger = new ScriptEngineManager();;
        ScriptEngine groovyEngine = engineManger.getEngineByName("groovy");
        System.out.println(groovyEngine);
    }

}
