package info.novatec.testit.livingdoc.server.rest;

import java.util.*;

public interface LivingDocRestHelper {

    // TODO would be good to replace Vector by ArrayList

    String getRenderedSpecification(final String username, final String password, ArrayList<?> args);

    List<?> getSpecificationHierarchy(final String username, final String password, ArrayList<?> args);

    String setSpecificationAsImplemented(final String username, final String password, ArrayList<?> args);

    String saveExecutionResult(final String username, final String password, ArrayList<Object> args);
}
