/* Copyright (c) 2007 Pyxis Technologies inc.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org. */

package info.novatec.testit.livingdoc.util.cli;

public class OptionBuilder {
    public static OptionBuilder create(String name) {
        return new OptionBuilder(name);
    }

    private final Option option;

    public OptionBuilder(String name) {
        option = new Option(name);
    }

    public OptionBuilder wantsArgument(String value) {
        if (option.wantsArg()) {
            throw new IllegalArgumentException("Argument pattern given twice");
        }
        option.setArg(value);
        return this;
    }

    public OptionBuilder withShortForm(String shortForm) {
        if (option.getShortForm() != null) {
            throw new IllegalArgumentException("Short form given twice");
        }
        option.setShortForm(shortForm);
        return this;
    }

    public OptionBuilder withDescription(String text) {
        if (option.getDescription() != null) {
            throw new IllegalArgumentException("Description given twice");
        }
        option.setDescription(text);
        return this;
    }

    public OptionBuilder withLongForm(String longForm) {
        if (option.getLongForm() != null) {
            throw new IllegalArgumentException("Long form given twice");
        }
        option.setLongForm(longForm);
        return this;
    }

    public OptionBuilder defaultingTo(Object value) {
        option.setValue(value);
        return this;
    }

    public OptionBuilder asType(Class< ? > type) {
        return convertedWith(CommandLine.converterFor(type));
    }

    public OptionBuilder convertedWith(Converter< ? > converter) {
        option.setConverter(converter);
        return this;
    }

    public Option make() {
        if ( ! option.isValid()) {
            throw new IllegalArgumentException("no switch given");
        }
        return option;
    }

    public OptionBuilder whenPresent(Option.Stub stub) {
        option.setStub(stub);
        return this;
    }
}
