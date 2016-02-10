/* Copyright (c) 2006 Pyxis Technologies inc.
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

import java.util.Arrays;
import java.util.List;

import info.novatec.testit.livingdoc.util.CollectionUtil;


public class Option {
    private String name;
    private String shortOpt;
    private String longOpt;
    private String description;
    private String arg;
    private Object value;
    private Converter< ? > converter = CommandLine.converterFor(String.class);
    private Stub stub = new DoNothing();

    public Option(String name) {
        this.name = name;
    }

    public boolean isValid() {
        return shortOpt != null || longOpt != null;
    }

    public void consume(List<String> args) throws WrongOptionUsageException {
        if (wantsArg() && ( args.isEmpty() )) {
            throw new WrongOptionUsageException(this);
        }
        value = wantsArg() ? convert(CollectionUtil.shift(args)) : true;
    }

    private Object convert(String valueToConvert) throws WrongOptionUsageException {
        try {
            return converter.convert(valueToConvert);
        } catch (Exception e) {
            throw new WrongOptionUsageException(this, e);
        }
    }

    public String getName() {
        return name;
    }

    public String getShortForm() {
        return shortOpt;
    }

    public void setShortForm(String shortOpt) {
        this.shortOpt = shortOpt;
    }

    public String getLongForm() {
        return longOpt;
    }

    public void setLongForm(String longOpt) {
        this.longOpt = longOpt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArg() {
        return arg;
    }

    public void setArg(String arg) {
        this.arg = arg;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Converter< ? > getConverter() {
        return converter;
    }

    public void setConverter(Converter< ? > converter) {
        this.converter = converter;
    }

    public boolean wantsArg() {
        return arg != null;
    }

    private void describeTo(StringBuilder sb) {
        if (shortOpt != null) {
            sb.append(shortOpt);
        } else {
            sb.append("  ");
        }

        if (longOpt != null) {
            if (shortOpt != null) {
                sb.append(", ");
            } else {
                sb.append("  ");
            }
            sb.append(longOpt);
        }

        if (wantsArg()) {
            sb.append(' ').append(arg);
        }

        if (description != null) {
            justify(sb);
            sb.append(description);
        }
    }

    private void justify(StringBuilder sb) {
        int padding = 30 - sb.length();
        if (padding <= 0) {
            sb.append('\n');
            padding = 30;
        }
        char[] filler = new char[padding];
        Arrays.fill(filler, ' ');
        sb.append(filler);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        describeTo(sb);
        return sb.toString();
    }

    public void setStub(Stub stub) {
        this.stub = stub;
    }

    public boolean wasGiven() {
        return value != null;
    }

    public void call() {
        stub.call(this);
    }

    public interface Stub {

        void call(Option option);
    }

    public static class DoNothing implements Stub {
        @Override
        public void call(Option option) {
            // No implementation needed.
        }
    }
}
