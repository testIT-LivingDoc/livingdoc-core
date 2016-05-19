package info.novatec.testit.livingdoc.server.database.hibernate.hsqldb;

import org.hibernate.HibernateException;
import org.hibernate.Session;


/**
 * <p/>
 * Copyright (c) 2005 Pyxis technologies inc. All Rights Reserved.
 *
 * @author gcarey
 */
public interface HibernateMemoryCallback {
    Object execute(Session session) throws HibernateException;
}
