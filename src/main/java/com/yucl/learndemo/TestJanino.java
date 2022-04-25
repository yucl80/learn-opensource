package com.yucl.learndemo;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.codehaus.commons.compiler.CompilerFactoryFactory;
import org.codehaus.commons.compiler.ICompiler;
import org.codehaus.commons.compiler.ICompilerFactory;
import org.codehaus.commons.compiler.util.ResourceFinderClassLoader;
import org.codehaus.commons.compiler.util.resource.MapResourceCreator;
import org.codehaus.commons.compiler.util.resource.MapResourceFinder;
import org.codehaus.commons.compiler.util.resource.Resource;
import org.codehaus.commons.compiler.util.resource.StringResource;
import org.codehaus.janino.JavaSourceClassLoader;
import org.springframework.util.Assert;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

public class TestJanino {

    public static void main(String[] args) {
        try {
            f4();

           /* for (ICompilerFactory fact : CompilerFactoryFactory.getAllCompilerFactories(Thread.currentThread().getContextClassLoader())) {
               System.out.println(fact);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void f4() throws InstantiationException, IllegalAccessException {
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString"))
                .intercept(FixedValue.value("Hello World!"))
                .make()
                .load(Thread.currentThread().getContextClassLoader())
                .getLoaded();
        System.out.println(dynamicType.newInstance().toString());
    }

    public static void f3() {
        JavaCompiler compiler =  ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null,null,null,"srcdir/pkg1/A.java","srcdir/pkg2/B.java");
        System.out.println(result);
    }

    public static void f2() throws Exception {
        ClassLoader cl = new JavaSourceClassLoader(
                Thread.currentThread().getContextClassLoader(),  // parentClassLoader
                new File[]{new File("srcdir")}, // optionalSourcePath
                (String) null                     // optionalCharacterEncoding

        );

// Load class A from "srcdir/pkg1/A.java", and also its superclass
// B from "srcdir/pkg2/B.java":
        Object o = cl.loadClass("pkg1.A").getDeclaredConstructor().newInstance();

// Class "B" implements "Runnable", so we can cast "o" to
// "Runnable".
        ((Runnable) o).run(); // Prints "HELLO" to "System.out".
    }

    private static void f1() throws Exception {
        ICompilerFactory compilerFactory = CompilerFactoryFactory.getDefaultCompilerFactory(Thread.currentThread().getContextClassLoader());

        ICompiler compiler = compilerFactory.newCompiler();

// Store generated .class files in a Map:
        Map<String, byte[]> classes = new HashMap<String, byte[]>();
        compiler.setClassFileCreator(new MapResourceCreator(classes));

// Now compile two units from strings:
        compiler.compile(new Resource[]{
                new StringResource(
                        "pkg1/A.java",
                        "package pkg1; public class A { public static int meth() { return pkg2.B.meth(); } }"
                ),
                new StringResource(
                        "pkg2/B.java",
                        "package pkg2; import com.yucl.learndemo.MyAnno; @MyAnno \n public class B { public static int meth() { return 77;            } }"
                ),
        });

// Set up a class loader that uses the generated classes.
        ClassLoader cl = new ResourceFinderClassLoader(
                new MapResourceFinder(classes),    // resourceFinder
                ClassLoader.getSystemClassLoader() // parent
        );
        Class<?> clz = cl.loadClass("pkg2.B");

        MyAnno zx = clz.getAnnotation(MyAnno.class);
        if (zx != null) {
            System.out.println(zx);
        }

        System.out.println(cl.loadClass("pkg1.A").getDeclaredMethod("meth").invoke(null));
    }
}
