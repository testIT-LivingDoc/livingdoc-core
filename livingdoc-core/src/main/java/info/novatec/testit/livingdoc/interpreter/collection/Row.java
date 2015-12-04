package info.novatec.testit.livingdoc.interpreter.collection;

import static info.novatec.testit.livingdoc.util.ExampleUtil.contentOf;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.ENTRY_WITH_TWO;
import static info.novatec.testit.livingdoc.util.LoggerConstants.EXIT_WITH;
import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.novatec.testit.livingdoc.Call;
import info.novatec.testit.livingdoc.Example;
import info.novatec.testit.livingdoc.expectation.Expectation;
import info.novatec.testit.livingdoc.expectation.ShouldBe;
import info.novatec.testit.livingdoc.interpreter.HeaderForm;
import info.novatec.testit.livingdoc.reflect.Fixture;


public class Row {
    private static final Logger LOG = LoggerFactory.getLogger(Row.class);

    private final Fixture fixture;
    private final Example headers;

    public Row(Fixture fixture, Example headers) {
        this.headers = headers;
        this.fixture = fixture;
    }

    public static Row parse(Fixture fixture, Example headers) {
        LOG.debug(ENTRY_WITH_TWO, fixture.toString(), headers.toString());
        Row newRow = new Row(fixture, headers);
        LOG.debug(EXIT_WITH, newRow.toString());
        return newRow;
    }

    public boolean matches(Example cells) {
        LOG.debug(ENTRY_WITH, cells.toString());
        for (int i = 0; i < cells.remainings(); ++ i) {
            Example cell = cells.at(i);

            if (i < headers.remainings()) {
                if (HeaderForm.parse(contentOf(headers.at(i))).isExpected()) {
                    continue;
                }
                if (StringUtils.isBlank(contentOf(cell))) {
                    continue;
                }

                try {
                    Call call = new Call(fixture.check(contentOf(headers.at(i))));
                    Expectation expectation = ShouldBe.literal(contentOf(cell));

                    Object result = call.execute();
                    if ( ! expectation.meets(result)) {
                        LOG.debug(EXIT_WITH, false);
                        return false;
                    }
                } catch (Exception e) {
                    LOG.error(LOG_ERROR, e);
                    LOG.debug(EXIT_WITH, false);
                    return false;
                }
            }
        }

        LOG.debug(EXIT_WITH, true);
        return true;
    }
}
