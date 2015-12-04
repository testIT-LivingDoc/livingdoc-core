package info.novatec.testit.livingdoc.reflect;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public class DuplicateAnnotatedFixturesFoundException extends RuntimeException {

    private final List<Class< ? >> foundFixtures = new ArrayList<Class< ? >>();
    private final List<Class< ? >> foundAliases = new ArrayList<Class< ? >>();

    public DuplicateAnnotatedFixturesFoundException(List<Class< ? >> foundAnnotatedClasses,
        List<Class< ? >> foundAliasClasses) {
        super();

        foundFixtures.addAll(foundAnnotatedClasses);
        foundAliases.addAll(foundAliasClasses);
    }

    public DuplicateAnnotatedFixturesFoundException(List<Class< ? >> matches) {
        foundAliases.addAll(matches);
    }

    @Override
    public String getMessage() {
        String message = "";

        if (foundFixtures.size() > 1) {
            message = message + "Following annotated fixtures have been found in doubles: \n";
            for (Class< ? > str : foundFixtures) {
                message = message + str.getName() + "\n";
            }
        }
        if (foundAliases.size() > 1) {
            message = message + "Following aliases of annotated fixtures have been found in doubles: \n";
            for (Class< ? > str : foundAliases) {
                message = message + str.getName() + "\n";
            }
        }
        return message;
    }
}
