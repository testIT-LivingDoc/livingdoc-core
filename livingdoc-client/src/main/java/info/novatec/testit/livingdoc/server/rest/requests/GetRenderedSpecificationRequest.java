package info.novatec.testit.livingdoc.server.rest.requests;

import java.util.List;


public class GetRenderedSpecificationRequest {

    public List<?> arguments;

    public GetRenderedSpecificationRequest() {
    }

    public GetRenderedSpecificationRequest(List<?> args) {
        this.arguments = args;
    }
}
