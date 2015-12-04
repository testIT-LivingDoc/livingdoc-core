package info.novatec.testit.livingdoc.ognl;

import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.systemunderdevelopment.SystemUnderDevelopment;


public class OgnlSystemUnderDevelopment implements SystemUnderDevelopment {
    static {
        ShouldBe.register(OgnlExpectation.class);
    }

    private final SystemUnderDevelopment systemUnderDevelopment;

    public OgnlSystemUnderDevelopment() {
        this(new DefaultSystemUnderDevelopment());
    }

    public OgnlSystemUnderDevelopment(SystemUnderDevelopment systemUnderDevelopment) {
        this.systemUnderDevelopment = systemUnderDevelopment;
    }

    @Override
    public Fixture getFixture(String name, String... params) throws Throwable {
        return new OgnlFixture(systemUnderDevelopment.getFixture(name, params));
    }

    @Override
    public void addImport(String packageName) {
        systemUnderDevelopment.addImport(packageName);
    }

    @Override
    public void onEndDocument(Document document) {
        systemUnderDevelopment.onEndDocument(document);
    }

    @Override
    public void onStartDocument(Document document) {
        systemUnderDevelopment.onStartDocument(document);
    }

    @Override
    public void setClassLoader(ClassLoader classLoader) {

        systemUnderDevelopment.setClassLoader(classLoader);
    }
}
