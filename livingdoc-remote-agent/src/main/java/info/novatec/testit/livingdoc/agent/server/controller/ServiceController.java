package info.novatec.testit.livingdoc.agent.server.controller;

import info.novatec.testit.livingdoc.server.domain.*;
import info.novatec.testit.livingdoc.server.rest.requests.*;
import info.novatec.testit.livingdoc.server.rest.responses.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServiceController {

    @PostMapping("/execute")
    public ResponseEntity<ExecutionResponse> execute(@RequestBody ExecutionRequest executionRequest) {
        Runner runner = executionRequest.runner;

        //   To prevent call forwarding
        runner.setServerName(null);
        runner.setServerPort(null);

        SystemUnderTest systemUnderTest = executionRequest.sut;
        Specification specification = executionRequest.specification;

        Execution execution = runner.execute(specification, systemUnderTest, executionRequest.implemented,
                executionRequest.section, executionRequest.locale);

        ExecutionResponse executionResponse = new ExecutionResponse(execution);

        return new ResponseEntity<ExecutionResponse>(executionResponse, HttpStatus.OK);
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody String param) {
        return new ResponseEntity<String>("Test Connection. Params: " + param, HttpStatus.OK);
    }

}
