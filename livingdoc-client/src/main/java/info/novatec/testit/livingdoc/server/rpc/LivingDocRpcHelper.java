package info.novatec.testit.livingdoc.server.rpc;

import java.util.Vector;


public interface LivingDocRpcHelper {
    String getRenderedSpecification(String username, String password, Vector< ? > args);

    Vector< ? > getSpecificationHierarchy(String username, String password, Vector< ? > args);

    String setSpecificationAsImplemented(String username, String password, Vector< ? > args);

    String saveExecutionResult(String username, String password, Vector< ? > args);
}
