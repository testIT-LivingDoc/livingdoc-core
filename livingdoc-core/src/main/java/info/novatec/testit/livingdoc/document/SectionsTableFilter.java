package info.novatec.testit.livingdoc.document;

import static info.novatec.testit.livingdoc.util.ExampleUtil.content;
import static info.novatec.testit.livingdoc.util.ExampleUtil.contentOf;

import java.util.Collection;
import java.util.HashSet;

import info.novatec.testit.livingdoc.Example;


public class SectionsTableFilter extends AbstractTableFilter {
    private final Sections filter;

    public SectionsTableFilter(String... sections) {
        super("section");
        this.filter = new Sections(sections);
    }

    @Override
    public Example doFilter(Example example) {
        if (filter.areIncluded(content(example.at(0, 1, 0)))) {
            return example.nextSibling();
        }
        return goToNextSection(example.nextSibling());
    }

    protected Example goToNextSection(Example example) {
        for (Example table = example; table != null; table = table.nextSibling()) {
            if ("section".equalsIgnoreCase(contentOf(table.at(0, 0, 0)))) {
                return table;
            }
        }
        return null;
    }

    public void includeSections(String... tags) {
        filter.allowSections(tags);
    }

    public static class Sections {
        private Collection<String> includedSections;

        public Sections(String... sections) {
            this.includedSections = new HashSet<String>();
            allowSections(sections);
        }

        public boolean areIncluded(String... sections) {
            if (includedSections.isEmpty()) {
                return true;
            }

            for (String section : sections) {
                if ( ! includedSections.contains(format(section))) {
                    return false;
                }
            }

            return true;
        }

        public void allowSections(String... tags) {
            for (String tag : tags) {
                allowTag(tag);
            }
        }

        private String format(String tag) {
            return tag.trim().toUpperCase();
        }

        private void allowTag(String tag) {
            includedSections.add(format(tag));
        }
    }
}
