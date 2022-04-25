package com.yucl.learn.cadence;

import com.uber.cadence.workflow.WorkflowMethod;

public interface GreetingChild {
    @WorkflowMethod
    String composeGreeting(String greeting, String name);
}
