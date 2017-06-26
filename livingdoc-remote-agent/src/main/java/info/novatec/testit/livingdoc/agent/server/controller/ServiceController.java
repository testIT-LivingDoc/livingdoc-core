package info.novatec.testit.livingdoc.agent.server.controller;

import info.novatec.testit.livingdoc.server.domain.Execution;
import info.novatec.testit.livingdoc.server.domain.Runner;
import info.novatec.testit.livingdoc.server.domain.Specification;
import info.novatec.testit.livingdoc.server.domain.SystemUnderTest;
import info.novatec.testit.livingdoc.server.rest.requests.ExecutionRequest;
import info.novatec.testit.livingdoc.server.rest.responses.ExecutionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> execute(@RequestBody String param) {
        return new ResponseEntity<String>("Mi ejecucion", HttpStatus.OK);
    }


}
