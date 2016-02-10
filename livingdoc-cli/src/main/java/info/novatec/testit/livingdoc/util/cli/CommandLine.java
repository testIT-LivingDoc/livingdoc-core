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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class CommandLine {
    private static final Map<Class< ? >, Converter< ? >> converters = new HashMap<Class< ? >, Converter< ? >>();

    static {
        registerConverter(String.class, new StringConverter());
        registerConverter(Integer.class, new IntegerConverter());
        registerConverter(int.class, new IntegerConverter());
        registerConverter(Locale.class, new LocaleConverter());
        registerConverter(File.class, new FileConverter());
        registerConverter(Class.class, new ClassConverter<Object>());
    }

    public static void registerConverter(Class< ? > type, Converter< ? > converter) {
        converters.put(type, converter);
    }

    public static Converter< ? > converterFor(Class< ? > type) {
        if ( ! converters.containsKey(type)) {
            throw new IllegalArgumentException("Don't know type: " + type);
        }

        return converters.get(type);
    }

    private final List<String> arguments;
    private final List<Option> options;

    private CommandLineParser parser = new PosixParser();
    private String banner = "";

    public CommandLine() {
        arguments = new ArrayList<String>();
        options = new ArrayList<Option>();
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public void setMode(CommandLineParser parser) {
        this.parser = parser;
    }

    public OptionBuilder buildOption(String name, String... buildOptions) {
        OptionBuilder option = OptionBuilder.create(name);
        parser.build(option, buildOptions);
        return option;
    }

    public void defineOption(OptionBuilder option) {
        define(option.make());
    }

    public void define(Option option) {
        options.add(option);
    }

    public String[] parse(String... args) throws ParseException {
        parser.parse(this, args);
        callStubs();
        return getArguments();
    }

    public void addArgument(String arg) {
        arguments.add(arg);
    }

    public int getArgumentCount() {
        return arguments.size();
    }

    public String[] getArguments() {
        return arguments.toArray(new String[arguments.size()]);
    }

    public String getArgument(int index) {
        if (index >= arguments.size()) {
            return null;
        }
        return arguments.get(index);
    }

    public boolean hasOptionValue(String name) {
        return getOptionValues().containsKey(name);
    }

    public Map<String, ? extends Object> getOptionValues() {
        Map<String, Object> opts = new HashMap<String, Object>();
        for (Option opt : options) {
            if (opt.wasGiven()) {
                opts.put(opt.getName(), opt.getValue());
            }
        }

        return opts;
    }

    public Object getOptionValue(String name) {
        if ( ! hasOptionValue(name)) {
            throw new IllegalArgumentException("Don't know option: " + name);
        }
        return getOptionValues().get(name);
    }

    public String usage() {
        StringBuilder sb = new StringBuilder("Usage: ").append(banner);
        if (options.isEmpty()) {
            return sb.toString();
        }

        sb.append("\n\n").append("Options:");
        for (Option option : options) {
            sb.append('\n').append(option);
        }

        return sb.toString();
    }

    public Option getOptionWithShortForm(String shortForm) throws InvalidOptionException {
        for (Option option : options) {
            if (shortForm.equals(option.getShortForm())) {
                return option;
            }
        }

        throw new InvalidOptionException(shortForm);
    }

    public Option getOptionWithLongForm(String longForm) throws InvalidOptionException {
        for (Option option : options) {
            if (longForm.equals(option.getLongForm())) {
                return option;
            }
        }

        throw new InvalidOptionException(longForm);
    }

    private void callStubs() {
        for (Option option : options) {
            if (option.wasGiven()) {
                option.call();
            }
        }
    }
}
