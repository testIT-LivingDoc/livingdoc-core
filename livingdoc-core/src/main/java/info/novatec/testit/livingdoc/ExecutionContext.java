package info.novatec.testit.livingdoc;

import java.util.Map;


public interface ExecutionContext {
    void setVariable(String symbol, Object value);

    Object getVariable(String symbol);

    Map<String, Object> getAllVariables();
}
