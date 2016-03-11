package info.novatec.testit.livingdoc.document;

import static info.novatec.testit.livingdoc.LivingDoc.canContinue;

import java.io.PrintWriter;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.Interpreter;
import info.novatec.testit.livingdoc.Statistics;
import info.novatec.testit.livingdoc.TimeStatistics;


public class Document {
    private final String type;
    private final Example start;
    private final Statistics stats;
    private final TimeStatistics timeStats;
    private final CompositeFilter filters;
    private String[] sections;
    private final String name;
    private final String externalLink;

    private SpecificationListener listener = new NullSpecificationListener();

    public static Document html(Example example) {
        return new Document("html", example);
    }

    public static Document html(Example example, String name, String externalLink) {
        return new Document("html", example, name, externalLink);
    }

    public static Document text(Example example) {
        return new Document("txt", example);
    }

    public Document(String type, Example example) {
        this(type, example, null, null);
    }

    public Document(String type, Example example, String name, String externalLink) {
        this.type = type;
        this.start = example;
        this.name = name;
        this.externalLink = externalLink;
        this.stats = new Statistics();
        this.timeStats = new TimeStatistics();
        this.filters = new CompositeFilter();
    }

    public String getType() {
        return type;
    }

    public String[] getSections() {
        return sections;
    }

    public void setSections(String[] sections) {
        this.sections = sections.clone();
    }

    public String getName() {
        return name;
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setSpecificationListener(SpecificationListener listener) {
        this.listener = listener;
    }

    public void execute(InterpreterSelector interpreterSelector) {
        AbstractSpecification spec = new FilteredSpecification(start);

        while (spec.hasMoreExamples() && canContinue(stats)) {
            Interpreter interpreter = interpreterSelector.selectInterpreter(spec.peek());
            interpreter.interpret(spec);
        }
    }

    public void print(PrintWriter writer) {
        start.print(writer);
    }

    public void tally(Statistics statistics) {
        stats.tally(statistics);
    }

    public class FilteredSpecification extends AbstractSpecification {
        public FilteredSpecification(Example start) {
            super();

            setStart(start);
        }

        @Override
        public Example peek() {
            return new EagerFilter(filters).filter(cursor.nextSibling());
        }

        @Override
        public void exampleDone(Statistics statistics) {
            tally(statistics);
            listener.exampleDone(cursor, statistics);
        }
    }

    public void done() {
        listener.specificationDone(start, stats);
    }

    public void addFilter(ExampleFilter filter) {
        filters.add(filter);
    }

    public Statistics getStatistics() {
        return stats;
    }

    public TimeStatistics getTimeStatistics() {
        return timeStats;
    }
}
