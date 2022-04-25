package com.yucl.learn;


import java.util.*;

import com.slack.api.model.view.ViewState;
import com.slack.api.model.workflow.*;
import static com.slack.api.model.workflow.WorkflowSteps.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

    static String extract(Map<String, Map<String, ViewState.Value>> stateValues, String blockId, String actionId) {
        return stateValues.get(blockId).get(actionId).getValue();
    }

    public static void test(){

      /*  WorkflowStep step = WorkflowStep.builder()
                .callbackId("copy_review")
                .edit((req, ctx) -> { return ctx.ack(); })
                .save((req, ctx) -> {
                    Map<String, Map<String, ViewState.Value>> stateValues = req.getPayload().getView().getState().getValues();
                    Map<String, WorkflowStepInput> inputs = new HashMap<>();
                    inputs.put("taskName", stepInput(i -> i.value(extract(stateValues, "task_name_input", "task_name"))));
                    inputs.put("taskDescription", stepInput(i -> i.value(extract(stateValues, "task_description_input", "task_description"))));
                    inputs.put("taskAuthorEmail", stepInput(i -> i.value(extract(stateValues, "task_author_input", "task_author"))));
                    List<WorkflowStepOutput> outputs = asStepOutputs(
                            stepOutput(o -> o.name("taskName").type("text").label("Task Name")),
                            stepOutput(o -> o.name("taskDescription").type("text").label("Task Description")),
                            stepOutput(o -> o.name("taskAuthorEmail").type("text").label("Task Author Email"))
                    );
                    ctx.update(inputs, outputs);
                    return ctx.ack();
                })
                .execute((req, ctx) -> { return ctx.ack(); })
                .build();

        app.step(step);*/
    }
}
