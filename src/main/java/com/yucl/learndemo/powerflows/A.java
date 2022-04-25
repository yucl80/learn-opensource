package com.yucl.learndemo.powerflows;

import org.powerflows.dmn.engine.DecisionEngine;
import org.powerflows.dmn.engine.configuration.DefaultDecisionEngineConfiguration;
import org.powerflows.dmn.engine.model.decision.Decision;
import org.powerflows.dmn.engine.model.decision.EvaluationMode;
import org.powerflows.dmn.engine.model.decision.HitPolicy;
import org.powerflows.dmn.engine.model.decision.expression.ExpressionType;
import org.powerflows.dmn.engine.model.decision.field.ValueType;
import org.powerflows.dmn.engine.model.evaluation.result.DecisionResult;
import org.powerflows.dmn.engine.model.evaluation.variable.DecisionVariables;
import org.powerflows.dmn.io.yaml.YamlDecisionReader;

import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class A {
    public static void main(String[] args) throws Exception {
        DecisionEngine decisionEngine = new DefaultDecisionEngineConfiguration().configure();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Serializable> variables = new HashMap<>();
        variables.put("age", 18);
        variables.put("activeLoansNumber", 0);
        variables.put("startDate", format.parse("2019-01-05"));
        DecisionVariables decisionVariables = new DecisionVariables(variables);
        Decision decision = test();

        InputStream loanQualifierInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("loan-qualifier.yml");
        Optional<Decision> loanQualifierDecision = new YamlDecisionReader().read(loanQualifierInputStream);
        if(loanQualifierDecision.isPresent()) {
            Decision decision2 = loanQualifierDecision.get();
            long l = System.currentTimeMillis();
            for(int i=0;i<1000000;i++) {
                DecisionResult decisionResult = decisionEngine.evaluate(decision2, decisionVariables);
            }
            System.out.println((System.currentTimeMillis() -l));
           // System.out.println(decisionResult);
        }

        DecisionResult decisionResult = decisionEngine.evaluate(decision, decisionVariables);
        System.out.println(decisionResult);
    }
    public static Decision test() {
        Decision decision = Decision.fluentBuilder()
                .id("loan_qualifier")
                .name("Loan qualifier")
                .hitPolicy(HitPolicy.COLLECT)
                .expressionType(ExpressionType.FEEL)
                .withInputs()
                .name("age")
                .type(ValueType.INTEGER)
                .next()
                .name("activeLoansNumber")
                .description("Number of active loans on user's account")
                .type(ValueType.INTEGER)
                .withExpression()
                .type(ExpressionType.LITERAL)
                .and()
                .next()
                .name("startDate")
                .type(ValueType.DATE)
                .end()
                .withOutputs()
                .name("loanAmount")
                .description("Loan amount in Euro")
                .type(ValueType.DOUBLE)
                .next()
                .name("loanTerm")
                .description("Loan term in months")
                .type(ValueType.INTEGER)
                .end()
                .withRules()
                .description("Loan for 18 years")
                .withInputEntries()
                .name("age")
                .withExpression()
                .type(ExpressionType.FEEL)
                .value(18)
                .and()
                .next()
                .name("activeLoansNumber")
                .evaluationMode(EvaluationMode.INPUT_COMPARISON)
                .withExpression()
                .type(ExpressionType.LITERAL)
                .value(0)
                .and()
                .next()
                .name("startDate")
                .withExpression()
                .type(ExpressionType.FEEL)
                .value("[date and time(\"2019-01-01T12:00:00\")..date and time(\"2019-12-31T12:00:00\")]")
                .and()
                .end()
                .withOutputEntries()
                .name("loanAmount")
                .withExpression()
                .type(ExpressionType.LITERAL)
                .value(10000)
                .and()
                .next()
                .name("loanTerm")
                .withExpression()
                .type(ExpressionType.LITERAL)
                .value(12)
                .and()
                .end()
                .next()
                .withInputEntries()
                .name("age")
                .evaluationMode(EvaluationMode.INPUT_COMPARISON)
                .withExpression()
                .type(ExpressionType.LITERAL)
                .value(18)
                .and()
                .next()
                .name("startDate")
                .withExpression()
                .type(ExpressionType.FEEL)
                .value("[date and time(\"2019-03-01T12:00:00\")..date and time(\"2019-03-31T12:00:00\")]")
                .and()
                .end()
                .withOutputEntries()
                .name("loanAmount")
                .withExpression()
                .type(ExpressionType.LITERAL)
                .value(15000)
                .           and()
                .next()
                .name("loanTerm")
                .withExpression()
                .type(ExpressionType.LITERAL)
                .value(6)
                .and()
                .end()
                .next()
                .withInputEntries()
                .name("age")
                .withExpression()
                .type(ExpressionType.FEEL)
                .value(">18")
                .and()
                .end()
                .withOutputEntries()
                .name("loanAmount")
                .withExpression()
                .type(ExpressionType.LITERAL)
                .value(20000)
                .and()
                .next()
                .name("loanTerm")
                .withExpression()
                .type(ExpressionType.LITERAL)
                .value(12)
                .and()
                .end()
                .end()
                .build();
        return decision;
    }

    private Decision test2(){
        return Decision.builder()
                .id("loan_qualifier")
                .name("Loan qualifier")
                .hitPolicy(HitPolicy.COLLECT)
                .expressionType(ExpressionType.FEEL)
                .withInput(in -> in
                        .name("age")
                        .type(ValueType.INTEGER)
                        .build())
                .withInput(in -> in
                        .name("activeLoansNumber")
                        .description("Number of active loans on user's account")
                        .type(ValueType.INTEGER)
                        .withExpression(ex -> ex
                                .type(ExpressionType.LITERAL)
                                .build())
                        .build())
                .withInput(in -> in
                        .name("startDate")
                        .type(ValueType.DATE)
                        .build())
                .withOutput(out -> out
                        .name("loanAmount")
                        .description("Loan amount in Euro")
                        .type(ValueType.DOUBLE)
                        .build())
                .withOutput(out -> out
                        .name("loanTerm")
                        .description("Loan term in months")
                        .type(ValueType.INTEGER)
                        .build())
                .withRule(rule -> rule
                        .description("Loan for 18 years")
                        .withInputEntry(in -> in
                                .name("age")
                                .evaluationMode(EvaluationMode.INPUT_COMPARISON)
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(18)
                                        .build())
                                .build())
                        .withInputEntry(in -> in
                                .name("activeLoansNumber")
                                .evaluationMode(EvaluationMode.INPUT_COMPARISON)
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(0)
                                        .build())
                                .build())
                        .withInputEntry(in -> in
                                .name("startDate")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.FEEL)
                                        .value("[date and time(\"2019-01-01T12:00:00\")..date and time(\"2019-12-31T12:00:00\")]")
                                        .build())
                                .build())
                        .withOutputEntry(out -> out
                                .name("loanAmount")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(10000)
                                        .build())
                                .build())
                        .withOutputEntry(out -> out
                                .name("loanTerm")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(12)
                                        .build())
                                .build())
                        .build())
                .withRule(rule -> rule
                        .withInputEntry(in -> in
                                .name("age")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.FEEL)
                                        .value(18)
                                        .build())
                                .build())
                        .withInputEntry(in -> in
                                .name("startDate")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.FEEL)
                                        .value("[date and time(\"2019-03-01T12:00:00\")..date and time(\"2019-03-31T12:00:00\")]")
                                        .build())
                                .build())
                        .withOutputEntry(out -> out
                                .name("loanAmount")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(15000)
                                        .build())
                                .build())
                        .withOutputEntry(out -> out
                                .name("loanTerm")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(6)
                                        .build())
                                .build())
                        .build())
                .withRule(rule -> rule
                        .withInputEntry(in -> in
                                .name("age")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.FEEL)
                                        .value(">18")
                                        .build())
                                .build())
                        .withOutputEntry(out -> out
                                .name("loanAmount")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(20000)
                                        .build())
                                .build())
                        .withOutputEntry(out -> out
                                .name("loanTerm")
                                .withExpression(ex -> ex
                                        .type(ExpressionType.LITERAL)
                                        .value(12)
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
