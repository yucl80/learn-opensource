package com.yucl.learn.wf;

import io.serverlessworkflow.api.Workflow;
import io.serverlessworkflow.api.interfaces.State;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestReadWF {
    public static void main(String[] args) throws IOException {
        String str =  new String(Files.readAllBytes(Paths.get("src/main/resources/a.json")));
        Workflow workflow = Workflow.fromSource(str);
        for(  State state :  workflow.getStates()){

            System.out.println(state.getName());
        }
    }
}
