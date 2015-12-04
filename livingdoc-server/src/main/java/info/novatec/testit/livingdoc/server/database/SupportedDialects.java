/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
 * http://www.fsf.org.
 */
package info.novatec.testit.livingdoc.server.database;

import org.hibernate.dialect.DerbyDialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServerDialect;


public enum SupportedDialects {
    MySQL ( MySQLDialect.class.getName() ),
    Derby ( DerbyDialect.class.getName() ),
    HSQL ( HSQLDialect.class.getName() ),
    Oracle ( Oracle8iDialect.class.getName() ),
    PostgreSQL ( PostgreSQLDialect.class.getName() ),
    SQLServer ( SQLServerDialect.class.getName() );

    private final String className;

    private SupportedDialects(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }
}
