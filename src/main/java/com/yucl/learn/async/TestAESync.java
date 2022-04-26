package com.yucl.learn.async;

import com.ea.async.Async;
import io.vertx.core.Promise;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;


import static com.ea.async.Async.await;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class TestAESync {
    static {
        Async.init();
    }

    public static void main(String[] args) throws IOException {
        TestAESync est = new TestAESync();
        Promise<Object> promise = Promise.promise();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                promise.complete("ok");
            }
        },10000);

        Object v = await(promise.future().toCompletionStage().toCompletableFuture());


        
        System.out.println(v);
        System.out.println("test");
        //Boolean x = await(est.buyItem("dfsd", 11000));
      //  System.out.println(System.nanoTime()+ Thread.currentThread().getName() + " finish" );
        System.in.read();
    }

    public CompletableFuture<Boolean> buyItem(String itemTypeId, int cost)
    {
        await(decrement(cost));

        System.out.println(System.nanoTime()+ Thread.currentThread().getName() + " before giveItem " );
        await(giveItem(itemTypeId));
        System.out.println(System.nanoTime() + Thread.currentThread().getName()+ " after giveItem " );
        return completedFuture(true);
    }

    private CompletableFuture<Boolean> decrement(int cost) {
        CompletableFuture<Boolean> completedFuture = new CompletableFuture<Boolean>();
        System.out.println(System.nanoTime()+ Thread.currentThread().getName() + " call decrement " );
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(System.nanoTime()+ Thread.currentThread().getName() + " set complete " );
                completedFuture.complete(true);
            }
        },10000);


        return completedFuture;
    }

    private CompletableFuture<Boolean> giveItem(String itemTypeId) {
        System.out.println(System.nanoTime()+ Thread.currentThread().getName()  + " call giveItem 1" );
        CompletableFuture<Boolean> completedFuture = new CompletableFuture<Boolean>();
        CompletableFuture.runAsync(()-> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(System.nanoTime()+ Thread.currentThread().getName()  + " call giveItem 2" );
            completedFuture.complete(true);
        });

        return completedFuture;
    }
}
