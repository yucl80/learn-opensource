package com.yucl.learn.async;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Testkotlin {
    public static void main(String[] args) {
        Deferred<String> deferred = BuildersKt.async(
                GlobalScope.INSTANCE,
                GlobalScope.INSTANCE.getCoroutineContext(),
                CoroutineStart.DEFAULT, // CoroutineStart.LAZY, or other strategies
               (coroutineScope, continuation) -> {
                    // do your stuff
                   System.out.println("1"+Thread.currentThread().getName());
                   return httpGet();
               }
        );
        List<Deferred> list =new ArrayList<>();

        AwaitKt.awaitAll(new Deferred[]{deferred}, new Continuation<List<?>>() {
            @NotNull
            @Override
            public CoroutineContext getContext() {
                return  GlobalScope.INSTANCE.getCoroutineContext();
            }

            @Override
            public void resumeWith(@NotNull Object o) {

            }
        });



      Object vv =   deferred.await(new Continuation<String>() {
            @NotNull
            @Override
            public CoroutineContext getContext() {
                return   GlobalScope.INSTANCE.getCoroutineContext();
            }

            @Override
            public void resumeWith(@NotNull Object o) {
                System.out.println("gggg"+Thread.currentThread().getName());
                System.out.println(o);
            }
        });

        System.out.println("vv" + vv);
        System.out.println(Thread.currentThread().getName());



        Job job = BuildersKt.launch(
                GlobalScope.INSTANCE,
                GlobalScope.INSTANCE.getCoroutineContext(),
                CoroutineStart.DEFAULT, // CoroutineStart.LAZY, or other strategies
                (Function2<CoroutineScope, Continuation<? super Unit>, Unit>) (coroutineScope, continuation) -> {
                   String data =  httpGet();
                   System.out.println("result2");
                    System.out.println("2"+Thread.currentThread().getName());
                    return Unit.INSTANCE;
                }
        );




        System.out.println("after job");

        job.invokeOnCompletion(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                System.out.println("invoke");
                return null;
            }
        });





        try {
            // Usually used to bridge regular blocking code to libraries using suspend,
            // to be used in main functions like from tests or similar.
            String result = BuildersKt.runBlocking(
                    GlobalScope.INSTANCE.getCoroutineContext(),
                    (Function2<CoroutineScope, Continuation<? super String>, String>) (coroutineScope, continuation) -> {
                        // do your stuff
                        System.out.println("3"+Thread.currentThread().getName());
                        return httpGet();
                    }
            );


            System.out.println("result3");
        } catch (InterruptedException e) {
            // If this blocked thread is interrupted, then the coroutine job is cancelled and
            // * this runBlocking invocation throws InterruptedException.
            // *
            // Do something with the interruption error
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String httpGet() {
        try (final CloseableHttpClient httpclient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://httpbin.org/get");
            System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getURI());

            final String responseBody = httpclient.execute(httpget, new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                    return EntityUtils.toString(httpResponse.getEntity());
                }
            });
            return responseBody;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Some result";
    }
}
