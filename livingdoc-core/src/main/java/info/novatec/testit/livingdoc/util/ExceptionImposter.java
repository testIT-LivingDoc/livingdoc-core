package info.novatec.testit.livingdoc.util;

@SuppressWarnings("serial")
public class ExceptionImposter extends RuntimeException {
    private final Throwable imposterized;

    public static RuntimeException imposterize(Throwable t) {
        if (t instanceof RuntimeException) {
            return ( RuntimeException ) t;
        }

        return new ExceptionImposter(t);
    }

    public ExceptionImposter(Throwable e) {
        super(e.getMessage(), e.getCause());
        imposterized = e;
        setStackTrace(e.getStackTrace());
    }

    public Throwable getRealException() {
        return imposterized;
    }

    @Override
    public String toString() {
        return imposterized.toString();
    }

}
