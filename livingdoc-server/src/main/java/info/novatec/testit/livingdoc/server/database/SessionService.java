package info.novatec.testit.livingdoc.server.database;

import org.hibernate.Session;


/**
 * <p/>
 * Copyright (c) 2005 Pyxis technologies inc. All Rights Reserved.
 *
 * @author gcarey
 */
public interface SessionService {
    /**
     * Starts the database session
     */
    void startSession();

    /**
     * Retrieves the database session
     *
     * @return the database session
     */
    Session getSession();

    /**
     * Ends the database session
     */
    void closeSession();

    /**
     * Starts a transaction
     */
    void beginTransaction();

    /**
     * Commits the transaction.
     */
    void commitTransaction();

    /**
     * Rollbacks the transaction.
     */
    void rollbackTransaction();
}
