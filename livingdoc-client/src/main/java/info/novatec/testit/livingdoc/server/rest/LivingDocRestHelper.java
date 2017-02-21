package info.novatec.testit.livingdoc.server.rest;

import java.util.Vector;

public interface LivingDocRestHelper {

    // TODO would be good to replace Vector by ArrayList

    String getRenderedSpecification(final String username, final String password, Vector< ? > args);

    Vector< ? > getSpecificationHierarchy(final String username, final String password, Vector< ? > args);

    String setSpecificationAsImplemented(final String username, final String password, Vector< ? > args);

    String saveExecutionResult(final String username, final String password, Vector<Object> args);
}
