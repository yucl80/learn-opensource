package com.yucl.learn.async;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.io.FilesKt;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.*;

import java.io.File;

public class TestA {
    public static void main(String[] args) {
        Deferred<String> deferred = BuildersKt.async(
                GlobalScope.INSTANCE,
                GlobalScope.INSTANCE.getCoroutineContext(),
                CoroutineStart.DEFAULT, // CoroutineStart.LAZY, or other strategies
               (coroutineScope, continuation) -> {
                    // do your stuff
                    return "Some result";
                }
        );

        Job job = BuildersKt.launch(
                GlobalScope.INSTANCE,
                GlobalScope.INSTANCE.getCoroutineContext(),
                CoroutineStart.DEFAULT, // CoroutineStart.LAZY, or other strategies
                (Function2<CoroutineScope, Continuation<? super Unit>, Unit>) (coroutineScope, continuation) -> {
                    // do your stuff
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
                        return "Some result";
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
}
