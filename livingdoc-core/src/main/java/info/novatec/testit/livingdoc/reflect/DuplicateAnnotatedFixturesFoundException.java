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
        StringBuilder message = new StringBuilder();

        if (foundFixtures.size() > 1) {
            message.append("Following annotated fixtures have been found in doubles: \n");
            for (Class< ? > str : foundFixtures) {
                message.append(str.getName()).append('\n');
            }
        }
        if (foundAliases.size() > 1) {
            message.append("Following aliases of annotated fixtures have been found in doubles: \n");
            for (Class< ? > str : foundAliases) {
                message.append(str.getName()).append('\n');
            }
        }
        return message.toString();
    }
}
