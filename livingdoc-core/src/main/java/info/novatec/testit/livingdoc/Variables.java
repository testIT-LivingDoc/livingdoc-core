package info.novatec.testit.livingdoc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Variables implements ExecutionContext {
    private final Map<String, Object> variables = new HashMap<String, Object>();

    @Override
    public void setVariable(String symbol, Object value) {
        variables.put(symbol, value);
    }

    @Override
    public Object getVariable(String symbol) {
        return variables.get(symbol);
    }

    @Override
    public Map<String, Object> getAllVariables() {
        return Collections.unmodifiableMap(variables);
    }
}
