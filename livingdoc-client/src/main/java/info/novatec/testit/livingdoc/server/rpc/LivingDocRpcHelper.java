package info.novatec.testit.livingdoc.server.rpc;

import java.util.List;


public interface LivingDocRpcHelper {
    String getRenderedSpecification(String username, String password, List< ? > args);

    List< ? > getSpecificationHierarchy(String username, String password, List< ? > args);

    String setSpecificationAsImplemented(String username, String password, List< ? > args);

    String saveExecutionResult(String username, String password, List<Object> args);
}
