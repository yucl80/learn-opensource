package com.yucl.learn.wf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;
import io.serverlessworkflow.api.Workflow;
import io.serverlessworkflow.api.actions.Action;
import io.serverlessworkflow.api.end.End;
import io.serverlessworkflow.api.functions.FunctionDefinition;
import io.serverlessworkflow.api.functions.FunctionRef;
import io.serverlessworkflow.api.interfaces.WorkflowDiagram;
import io.serverlessworkflow.api.interfaces.WorkflowValidator;
import io.serverlessworkflow.api.start.Start;
import io.serverlessworkflow.api.states.DefaultState;
import io.serverlessworkflow.api.states.OperationState;
import io.serverlessworkflow.api.states.SleepState;
import io.serverlessworkflow.api.validation.ValidationError;
import io.serverlessworkflow.api.workflow.Functions;
import io.serverlessworkflow.diagram.WorkflowDiagramImpl;
import io.serverlessworkflow.validation.WorkflowValidatorImpl;

import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

public class Wf {
    public static void main(String[] args) {
        Workflow workflow = new Workflow()
                .withId("test-workflow")
                .withName("test-workflow-name")
                .withVersion("1.0")
                .withStart(new Start().withStateName("MyDelayState"))
                .withFunctions(new Functions(Arrays.asList(
                        new FunctionDefinition().withName("${testFunction")
                                .withOperation("testSwaggerDef#testOperationId")))
                )
                .withStates(Arrays.asList(
                                new SleepState().withName("MyDelayState").withType(DefaultState.Type.SLEEP)
                                        .withDuration("PT1M")
                                        .withEnd(new End().withTerminate(true))


                        )
                );

        WorkflowValidator workflowValidator = new WorkflowValidatorImpl();
        List<ValidationError> validationErrors = workflowValidator.setWorkflow(workflow).validate();
        System.out.println(validationErrors);

        WorkflowDiagram workflowDiagram = new WorkflowDiagramImpl();
        workflowDiagram.setWorkflow(workflow);
        //workflowDiagram.setTemplate("custom-template");
        try {
            String svg = workflowDiagram.getSvgDiagram();
            System.out.println(svg);
            FileWriter writer = new FileWriter("d:\\a.svg");
            writer.write(svg);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(Workflow.toJson(workflow));

    }
}
