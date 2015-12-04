package info.novatec.testit.livingdoc.maven.plugin;

import com.google.inject.Inject;


public class GuiceFixture {
    public String msg;
    private Foo foo;

    public GuiceFixture() {
        this.foo = new Foo();
    }

    @Inject
    public GuiceFixture(Foo foo) {
        this.foo = foo;
    }

    public String echo() {
        return msg;
    }

    public Foo getFoo() {
        return foo;
    }

    public static class Foo {
        public String getMsg() {
            return "We love Guice.";
        }
    }
}
