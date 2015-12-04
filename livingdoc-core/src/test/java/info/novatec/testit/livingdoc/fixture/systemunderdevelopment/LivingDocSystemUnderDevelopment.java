package info.novatec.testit.livingdoc.fixture.systemunderdevelopment;

import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;


public class LivingDocSystemUnderDevelopment extends DefaultSystemUnderDevelopment {

    public LivingDocSystemUnderDevelopment() {
        addImports();
    }

    @SuppressWarnings("unused")
    public LivingDocSystemUnderDevelopment(String... params) {
        this();
    }

    private void addImports() {
        addImport("info.novatec.testit.livingdoc.document");
        addImport("info.novatec.testit.livingdoc.interpreter");
        addImport("info.novatec.testit.livingdoc.interpreter.flow.dowith");
        addImport("info.novatec.testit.livingdoc.interpreter.flow.action");
        addImport("info.novatec.testit.livingdoc.interpreter.seeds");
        addImport("info.novatec.testit.livingdoc.interpreter.seeds.action");
        addImport("info.novatec.testit.livingdoc.interpreter.seeds.comparison");
        addImport("info.novatec.testit.livingdoc.ogn");
        addImport("info.novatec.testit.livingdoc.fixture.document");
        addImport("info.novatec.testit.livingdoc.fixture.interpreter");
        addImport("info.novatec.testit.livingdoc.fixture.interpreter.flow.dowith");
        addImport("info.novatec.testit.livingdoc.fixture.interpreter.flow.action");
        addImport("info.novatec.testit.livingdoc.fixture.interpreter.seeds");
        addImport("info.novatec.testit.livingdoc.fixture.interpreter.seeds.action");
        addImport("info.novatec.testit.livingdoc.fixture.interpreter.seeds.comparison");
        addImport("info.novatec.testit.livingdoc.fixture.ogn");
    }
}
