/*
 * Copyright (c) 2008 Pyxis Technologies inc.
 *
 * This is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA,
 * or see the FSF site: http://www.fsf.org.
 */
package info.novatec.testit.livingdoc.samples;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Example of using 'constant' variables with an expression evaluator (with BeanShell).
 * The value of variable are declared in a setup page.
 * <p/>
 * [Fine Rate Setup]
 * <pre>
| set | DefaultLevel | value to | 80 |
| set | FineIncrease | value to | 2 |
 * </pre>
 * [Fine Rate]
 * <pre>
|| Import ||
| info.novatec.testit.livingdoc.samples |

|| Do With || FineRate ||

{livingdoc-include:pageTitle=Fine Rate Setup}

| verify that fine rate value of | myValue | is equals to | DefaultLevel
| do a fine increase of | myValue
| verify that fine rate value of | myValue | is equals to | DefaultLevel + FineIncrease
| do a fine increase of | myValue |
| verify that fine rate value of | myValue | is equals to | DefaultLevel + FineIncrease * 2
| check | value | unknown1 | equals | null |
| check | value | unknown2 | equals | nothing |
 * </pre>
 */
public class FineRateFixture
{
	private static final String FINEINCREASE_NAME = "FineIncrease";
	private static final String DEFAULTLEVEL_NAME = "DefaultLevel";
	
	private final Interpreter interpreter = new Interpreter();

    public boolean setValueTo(String name, Integer value)
	{
		setValue(name, value);
		return true;
	}

	public boolean verifyThatFineRateValueOfIsEqualsTo(String name, String expression)
	{
		Integer value = getValue(name, getValue( DEFAULTLEVEL_NAME ));
		Integer fineValue = getValue(expression);
		return value.equals(fineValue);
	}
	
	public void doAFineIncreaseOf(String name)
	{
		Integer value = getValue(name);
		Integer increase = getValue(FINEINCREASE_NAME);
		setValue(name, value + increase);
	}

	public Integer valueEquals(String name)
	{
		return getValue(name);
	}

	private Integer getValue(String name)
	{
		try
		{
			return (Integer)interpreter.eval(name);
		}
		catch (EvalError e)
		{
			throw new RuntimeException("Retrieving the value of '" + name + "'", e);
		}
	}

	private Integer getValue(String name, Integer defaultValue)
	{
		Integer value = getValue(name);
		
		if (value == null)
		{
			value = defaultValue;
			setValue(name, defaultValue);
		}
		
		return value;
	}
	
	private void setValue(String name, Integer value)
	{
		try 
		{
			interpreter.set(name, value);
		}
		catch (EvalError e)
		{
			throw new RuntimeException("Setting the value of '" + name + "' to '" + value + "'", e);
		}
	}
}
