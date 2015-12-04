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
package info.novatec.testit.livingdoc.converter;

import static info.novatec.testit.livingdoc.util.LoggerConstants.LOG_ERROR;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <code>TypeConverter</code> implementation that converts strings to
 * <code>Date</code> objects.
 * 
 * @version $Revision: $ $Date: $
 */
public class DateConverter extends AbstractTypeConverter {
    private static final Logger LOG = LoggerFactory.getLogger(DateConverter.class);

    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected Object doConvert(String value) {
        try {
            return format.parse(value);
        } catch (ParseException ex) {
            LOG.error(LOG_ERROR, ex);
            throw new IllegalArgumentException("Unsupported date format", ex);
        }
    }

    @Override
    public boolean canConvertTo(Class< ? > type) {
        return Date.class.isAssignableFrom(type);
    }
}
