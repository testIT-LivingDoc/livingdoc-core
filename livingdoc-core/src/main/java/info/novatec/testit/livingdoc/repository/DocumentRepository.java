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

package info.novatec.testit.livingdoc.repository;

import java.util.List;

import info.novatec.testit.livingdoc.document.Document;


/**
 * Retrieving of test specifications is done through the Repository interface.
 */
public interface DocumentRepository {
    Document loadDocument(String location) throws Exception;

    /**
     * Consider renaming to listAllDocumentsUnder( String location ) and having
     * a method listDocumentsAt( String location ) that would return the child
     * specs exactly at the location (i.e. no recursivity)
     */
    List<String> listDocuments(String location) throws Exception;

    List<Object> listDocumentsInHierarchy() throws Exception;

    /**
     * TODO We should extract this method into its own interface and let
     * Repository implementations decide to implement the new interface or not.
     * The eclipse plugin could check for the presence of that interface to
     * decide whether to show the Set As Implemented context action on pages.
     */
    void setDocumentAsImplemented(String location) throws Exception;
}
