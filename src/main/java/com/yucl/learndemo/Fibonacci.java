package com.yucl.learndemo;

import org.graalvm.polyglot.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Fibonacci {
    public static void main(String[] args)
            throws ScriptException, IOException, URISyntaxException, NoSuchMethodException {

        Path jsPath = Paths.get(Fibonacci.class.getResource("/test.js").toURI());

         try{
             f5(jsPath);
         }catch (Exception e){
             e.printStackTrace();
         }



    }

    private static  void f5(Path jsPath) throws Exception {
       // System.setProperty("polyglot.js.nashorn-compat", "true");

        ScriptEngine graalEngine = new ScriptEngineManager().getEngineByName("graal.js");
        try (BufferedReader reader = Files.newBufferedReader(jsPath)) {
            graalEngine.eval(reader);

           // Invocable invocable = (Invocable) graalEngine;
          //  Object result = invocable.invokeFunction("factorialize", 5);

          //  System.out.println(result);
          //  System.out.println(result.getClass());
        }
    }


    private static void f3(){
        try (Context context = Context.newBuilder().allowExperimentalOptions(true).option("js.nashorn-compat", "true").build()) {
            context.eval("js", "print(__LINE__)");
        }
    }

    private static  void f1(Path jsPath) throws Exception {
         System.setProperty("polyglot.js.nashorn-compat", "true");

        ScriptEngine graalEngine = new ScriptEngineManager().getEngineByName("graal.js");
        try (BufferedReader reader = Files.newBufferedReader(jsPath)) {
            graalEngine.eval(reader);

            Invocable invocable = (Invocable) graalEngine;
            Object result = invocable.invokeFunction("factorialize", 5);

            System.out.println(result);
            System.out.println(result.getClass());
        }
    }

    private static void f2(Path jsPath) throws  Exception {
        // Nashorn
        ScriptEngine nashornEngine = new ScriptEngineManager().getEngineByName("nashorn");
        try (BufferedReader reader = Files.newBufferedReader(jsPath)) {
            nashornEngine.eval(reader);

            Invocable invocable = (Invocable) nashornEngine;
            Object result = invocable.invokeFunction("fibonacci", 1_000);

            System.out.println(result);
        }

        // Graal
        ScriptEngine graalEngine = new ScriptEngineManager().getEngineByName("graal.js");
        try (BufferedReader reader = Files.newBufferedReader(jsPath)) {
            graalEngine.eval(reader);

            Invocable invocable = (Invocable) graalEngine;
            Object result = invocable.invokeFunction("fibonacci", 1_000);

            System.out.println(result);
        }
    }
}
