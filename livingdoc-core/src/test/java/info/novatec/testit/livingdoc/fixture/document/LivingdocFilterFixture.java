package info.novatec.testit.livingdoc.fixture.document;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.document.CommentTableFilter;
import info.novatec.testit.livingdoc.document.Document;
import info.novatec.testit.livingdoc.document.Document.FilteredSpecification;
import info.novatec.testit.livingdoc.document.LivingDocInterpreterSelector;
import info.novatec.testit.livingdoc.document.LivingDocTableFilter;
import info.novatec.testit.livingdoc.document.SectionsTableFilter;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;
import info.novatec.testit.livingdoc.systemunderdevelopment.DefaultSystemUnderDevelopment;
import info.novatec.testit.livingdoc.util.ExampleUtil;
import info.novatec.testit.livingdoc.util.Tables;


/**
 * This class name contains a lower cased "doc" to pass the tests.
 */
@FixtureClass
public class LivingdocFilterFixture {
    private boolean lazyMode;
    private Tables documentContent;
    private String section = "";

    public void setDocumentContent(Tables tables) {
        documentContent = tables;
    }

    public void setExecutionMode(String mode) {
        lazyMode = "lazy".equals(mode);
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String InterpretedElements() {
        List<String> interpreted = new ArrayList<String>();

        Example example = documentContent;
        Document document = Document.text(example);

        document.addFilter(new LivingDocTableFilter(lazyMode));
        document.addFilter(new CommentTableFilter());
        document.addFilter(new SectionsTableFilter(section));

        FilteredSpecification spec = document.new FilteredSpecification(example);

        while (spec.hasMoreExamples()) {
            example = spec.peek();
            interpreted.add(ExampleUtil.contentOf(example.at(0, 0, 0)));
            Interpreter interpreter = new LivingDocInterpreterSelector(new DefaultSystemUnderDevelopment())
                .selectInterpreter(spec.peek());
            interpreter.interpret(spec);
        }

        StringBuilder sb = new StringBuilder();

        for (String s : interpreted) {
            sb.append("[").append(s).append("]");
        }

        return StringUtils.isEmpty(sb.toString()) ? "none" : sb.toString();
    }
}
