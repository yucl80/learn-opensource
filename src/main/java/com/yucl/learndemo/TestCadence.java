package com.yucl.learndemo;

import com.uber.cadence.workflow.Async;
import com.uber.cadence.workflow.Functions;
import com.uber.cadence.workflow.Promise;

public class TestCadence {
    public static void main(String[] args) {
        Promise<String> localNamePromise = Async.function(new Functions.Func<String>() {
            @Override
            public String apply() {
                return "hello";
            }
        });

        localNamePromise.thenApply(n ->{
            System.out.println(n);
            return null;
        });
    }
}
