package info.novatec.testit.livingdoc.reflect;

@SuppressWarnings("serial")
public class TypeNotFoundException extends RuntimeException {
    private final String name;

    public TypeNotFoundException(String name) {
        super();

        this.name = name;
    }

    @Override
    public String getMessage() {
        return name + " does not match any known type";
    }
}
