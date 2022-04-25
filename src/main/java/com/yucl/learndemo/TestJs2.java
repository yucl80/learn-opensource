package com.yucl.learndemo;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import org.apache.commons.jexl3.*;
import org.camunda.feel.FeelEngine;
import org.camunda.feel.syntaxtree.ParsedExpression;
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.IExpressionEvaluator;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;
import org.mvel2.templates.CompiledTemplate;
import org.mvel2.templates.TemplateCompiler;
import org.mvel2.templates.TemplateRuntime;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import scala.util.Either;

import javax.script.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

public class TestJs2 {
    static final int COUNT = 10000000;


    public static void main(String[] args) throws Exception {
        try {
            System.out.println("start");
            //System.in.read();


            // f2();
            for (int i = 0; i < 10; i++) {
                f2();

            }
            System.gc();
            System.out.println();
            for (int i = 0; i < 10; i++) {

                f1();
            }
            System.gc();
            System.out.println();
            for (int i = 0; i < 10; i++) {

                f6();
            }
            System.gc();
            System.out.println();
           /* for (int i = 0; i <10;i++){
                f5();
            }*/

            for (int i = 0; i < 10; i++) {
                f3();
            }
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void f8(){
        String exp = "javaObj.id == 42  && javaObj.text == \"42\"  && javaObj.ret42()  == 42  && javaObj.list[1]  == 42"; //

        JexlEngine jexl = new JexlBuilder().create();

        JexlExpression e = jexl.createExpression( exp );

        // Create a context and add data
        JexlContext jc = new MapContext();
        jc.set("javaObj",new MyClass());

        Object obj = null ;
        long l = System.currentTimeMillis();
        for(int i=0; i<COUNT;i++) {
            obj =e.evaluate(jc);
        }
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(obj);

    }

    private static void f6() {
        String template = "@{javaObj.id  == 42  && javaObj.text  == \"42\" && javaObj.arr[1]  == 42 && javaObj.ret42()  == 42}";

        CompiledTemplate compiled = TemplateCompiler.compileTemplate(template);

        Boolean output = null;
        long l = System.currentTimeMillis();
        Map vars1 = new HashMap();
        vars1.put("javaObj", new com.yucl.learndemo.MyClass());
        for (int i = 0; i < COUNT; i++) {
            output = (Boolean) TemplateRuntime.execute(compiled, vars1);
        }
        System.out.println(System.currentTimeMillis() - l);
        //System.out.println(output);
    }

    private static void f5() throws Exception {
        SpelParserConfiguration config = new SpelParserConfiguration(SpelCompilerMode.IMMEDIATE,
                Thread.currentThread().getContextClassLoader());

        SpelExpressionParser parser = new SpelExpressionParser(config);

        Expression expr = parser.parseExpression("javaObj.id  == 42  && javaObj.text  == \"42\" && javaObj.arr[1]  == 42 && javaObj.ret42()  == 42");


        Object payload = expr.getValue(new WJ());
        long l = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            payload = expr.getValue(new WJ());
        }
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(payload);
    }

    static class WJ {
        private MyClass javaObj = new MyClass();

        public MyClass getJavaObj() {
            return javaObj;
        }

        public void setJavaObj(MyClass javaObj) {
            this.javaObj = javaObj;
        }
    }

    private static void f1() throws Exception {
        IExpressionEvaluator ee = CompilerFactoryFactory.getDefaultCompilerFactory(Thread.currentThread().getContextClassLoader()).newExpressionEvaluator();
        // ee.setExpressionType(MyClass.class);
        ee.setDefaultImports("com.yucl.learndemo.*", "java.util.concurrent.Callable");
        ee.setParameters(new String[]{"javaObj"}, new Class[]{MyClass.class});
        // ee.setThrownExceptions(thrownExceptions);
        String expression = "javaObj.id  == 42  && javaObj.text  == \"42\" && javaObj.arr[1]  == 42 && javaObj.ret42()  == 42";

        ee.cook(expression);
        long l = System.currentTimeMillis();
        Object valid = null;
        for (int i = 0; i < COUNT; i++) {
            valid = ee.evaluate(new MyClass());
        }
        long e = System.currentTimeMillis();
        assert (Boolean) valid == true;
        System.out.println(e - l);
    }

    private static void f2() {
        try (Context context = Context.newBuilder()
                .allowAllAccess(true)
                .build()) {
            //context.getBindings("js").putMember("javaObj", new MyClass());

            Value eval = context.eval("js",
                    "  javaObj =>  javaObj.id         == 42" +
                            " && javaObj.text       == '42'" +
                            " && javaObj.arr[1]     == 42" +
                            " && javaObj.ret42()    == 42");

            //context.getBindings("js").get
            for (int i = 0; i < 10; i++) {
                eval.execute(new MyClass());
            }


            long l = System.currentTimeMillis();
            Object valid = false;

            for (int i = 0; i < COUNT; i++) {
                valid = eval.execute(new MyClass());
            }
            long e = System.currentTimeMillis();
            assert (Boolean) valid == true;
            //System.out.println(valid);
            System.out.println(e - l);
        }
    }

    private static void f4() throws Exception {
        ScriptEngine engine = new ScriptEngineManager()
                .getEngineByName("graal.js");
        engine.eval(" print(Graal.isGraalRuntime());print(Graal.versionJS);print(Graal.versionGraalVM); ");
    }

    private static void f3() throws Exception {
        ScriptEngine engine = new ScriptEngineManager()
                .getEngineByName("graal.js");
        //  GraalJSScriptEngine engine= (GraalJSScriptEngine)eng;
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("polyglot.js.allowHostAccess", true);
        bindings.put("polyglot.js.allowHostClassLookup", (Predicate<String>) s -> true);


        Object fn = engine.eval("function test(javaObj) {   return javaObj.id  == 42 && javaObj.text  == '42' && javaObj.arr[1] == 42 && javaObj.ret42() == 42; }");
        Invocable inv = (Invocable) engine;
        long l = System.currentTimeMillis();
        Object valid = false;

        for (int i = 0; i < COUNT; i++) {

            valid = inv.invokeFunction("test", new MyClass());
        }
        long e = System.currentTimeMillis();
        assert (Boolean) valid == true;
        System.out.println(e - l);
        // System.out.println(valid);

    }

    private static void f7() {
        String exp = "javaObj.id = 42  and javaObj.text = \"42\"  and javaObj.ret42()  = 42  and javaObj.list[2]  = 42"; //

        FeelEngine engine = new FeelEngine.Builder().enableExternalFunctions(true).build();
        Either<FeelEngine.Failure, ParsedExpression> ev = engine.parseExpression(exp);


        Map<String, Object> map = new HashMap<>();
        map.put("javaObj", new MyClass());
        Object obj = null;
        long l = System.currentTimeMillis();
        for (int i = 0; i < COUNT; i++) {
            obj = engine.eval(ev.toOption().get(), map);
        }
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(obj);
    }


}
