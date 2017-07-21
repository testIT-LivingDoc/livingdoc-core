package info.novatec.testit.livingdoc.server.rest.requests;

import java.util.List;

/**
 * Created by FSU on 09.06.2017.
 */
public class SaveExecutionResultRequest {
    private List<?> args;

    public SaveExecutionResultRequest() {
    }

    public SaveExecutionResultRequest(List<?> args) {
        this.args = args;
    }
}
