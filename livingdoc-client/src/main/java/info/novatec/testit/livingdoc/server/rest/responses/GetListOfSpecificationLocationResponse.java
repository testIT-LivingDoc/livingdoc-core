package info.novatec.testit.livingdoc.server.rest.responses;

import java.util.*;

public class GetListOfSpecificationLocationResponse {

    public List<List<String>> definitions;

    public GetListOfSpecificationLocationResponse() {
    }

    public GetListOfSpecificationLocationResponse(List<List<String>> definitions) {
        this.definitions = definitions;
    }
}
