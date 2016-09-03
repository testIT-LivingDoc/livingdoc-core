package info.novatec.testit.livingdoc.interpreter.flow.dowith;

import static info.novatec.testit.livingdoc.LivingDoc.isAnInterpreter;
import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.TypeLoaderChain;
import info.novatec.testit.livingdoc.annotation.Annotations;
import info.novatec.testit.livingdoc.interpreter.flow.Row;
import info.novatec.testit.livingdoc.interpreter.flow.RowSelector;
import info.novatec.testit.livingdoc.interpreter.flow.workflow.DefaultRow;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Type;
import info.novatec.testit.livingdoc.reflect.TypeLoader;
import info.novatec.testit.livingdoc.util.AliasLoader;
import info.novatec.testit.livingdoc.util.ExampleUtil;

@Deprecated
public class DoWithRowSelector implements RowSelector {
    private static final Logger LOG = LoggerFactory.getLogger(DoWithRowSelector.class);

    protected final Fixture fixture;

    private TypeLoader<Row> typeLoader;

    public DoWithRowSelector(Fixture fixture) {
        this.fixture = fixture;
        this.typeLoader = new TypeLoaderChain<Row>(Row.class);
        searchPackage(getClass().getPackage().getName());
        addSuffix("Row");
    }

    public void searchPackage(String name) {
        typeLoader.searchPackage(name);
    }

    public void addSuffix(String suffix) {
        typeLoader.addSuffix(suffix);
    }

    /* (non-Javadoc)
     * 
     * @see info.novatec.testit.livingdoc.interpreter.dowith.RowSelector#select(
     * info.novatec.testit.livingdoc.Example) */
    @Override
    public Row select(Example example) {
        if (isARow(identifier(example))) {
            return instantiateRow(example);
        }

        if (isAnInterpreter(identifier(example))) {
            return new InterpretRow(fixture);
        }

        return new DefaultRow(fixture);
    }

    private boolean isARow(String name) {
        Type<Row> type = loadRowType(name);
        return type != null && ! type.getUnderlyingClass().equals(Row.class);
    }

    private Type<Row> loadRowType(String name) {
        return typeLoader.loadType(name);
    }

    private String identifier(Example row) {
        String possibleAliasIdentifier = ExampleUtil.contentOf(row.firstChild());
        String aliasIdentifier = AliasLoader.get().getKeywordForAlias(possibleAliasIdentifier);
        return aliasIdentifier == null ? possibleAliasIdentifier : aliasIdentifier;
    }

    private Row instantiateRow(Example row) {
        Type<Row> rowClass = loadRowType(identifier(row));
        try {
            return rowClass.newInstance(fixture);
        } catch (Throwable throwable) {
            LOG.error(LOG_ERROR, throwable);
            row.firstChild().annotate(Annotations.exception(throwable));
            return new SkipRow();
        }
    }

    public void addSuffixes(Collection<String> suffixes) {
        for (String suffix : suffixes) {
            addSuffix(suffix);
        }
    }

    public void addPackages(Collection<String> packages) {
        for (String aPackage : packages) {
            searchPackage(aPackage);
        }
    }
}
