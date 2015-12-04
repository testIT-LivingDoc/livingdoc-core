package info.novatec.testit.livingdoc.server;

public class LivingDocServerException extends Exception {
    private static final long serialVersionUID = 1L;
    private String id = "";

    public LivingDocServerException() {
        super();
    }

    public LivingDocServerException(Throwable th) {
        super(th);
    }

    public LivingDocServerException(String id, String msg) {
        super(msg);
        this.id = id;
    }

    public LivingDocServerException(String id, String msg, Throwable th) {
        super(msg, th);
        this.id = id;
    }

    public LivingDocServerException(String id, Throwable th) {
        super(th);
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
