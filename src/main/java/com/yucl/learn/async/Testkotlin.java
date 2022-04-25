package com.yucl.learn.async;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Testkotlin {
    public static void main(String[] args) {
        Deferred<String> deferred = BuildersKt.async(
                GlobalScope.INSTANCE,
                GlobalScope.INSTANCE.getCoroutineContext(),
                CoroutineStart.DEFAULT, // CoroutineStart.LAZY, or other strategies
               (coroutineScope, continuation) -> {
                    // do your stuff
                   return httpGet();
               }
        );
        System.out.println("hello");



        Job job = BuildersKt.launch(
                GlobalScope.INSTANCE,
                GlobalScope.INSTANCE.getCoroutineContext(),
                CoroutineStart.DEFAULT, // CoroutineStart.LAZY, or other strategies
                (Function2<CoroutineScope, Continuation<? super Unit>, Unit>) (coroutineScope, continuation) -> {
                   String data =  httpGet();
                   System.out.println(data);
                    return Unit.INSTANCE;
                }
        );





        try {
            // Usually used to bridge regular blocking code to libraries using suspend,
            // to be used in main functions like from tests or similar.
            String result = BuildersKt.runBlocking(
                    GlobalScope.INSTANCE.getCoroutineContext(),
                    (Function2<CoroutineScope, Continuation<? super String>, String>) (coroutineScope, continuation) -> {
                        // do your stuff
                        return httpGet();
                    }
            );
            System.out.println(result);
        } catch (InterruptedException e) {
            // If this blocked thread is interrupted, then the coroutine job is cancelled and
            // * this runBlocking invocation throws InterruptedException.
            // *
            // Do something with the interruption error
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
