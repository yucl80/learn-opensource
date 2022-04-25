package com.yucl.learn.cadence;

import com.uber.cadence.workflow.Async;
import com.uber.cadence.workflow.Promise;
import com.uber.cadence.workflow.Workflow;

public class GreetingWorkflowImpl implements GreetingWorkflow {

    @Override
    public String getGreeting(String name) {
        // Workflows are stateful, so a new stub must be created for each new child.
        GreetingChild child1 = Workflow.newChildWorkflowStub(GreetingChild.class);
        Promise greeting1 = Async.function(child1::composeGreeting, "Hello", name);

        // Both children will run concurrently.
        GreetingChild child2 = Workflow.newChildWorkflowStub(GreetingChild.class);
        Promise  greeting2 = Async.function(child2::composeGreeting, "Bye", name);

        //Workflow.await();

        // Do something else here.
        ///   ...
        return "First: " + greeting1.get() + ", second=" + greeting2.get();
    }

}
