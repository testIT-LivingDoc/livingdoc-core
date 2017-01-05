package info.novatec.testit.livingdoc.server.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * Abstract Entity Class. All POJOS have to exntend it. Provides the versioning
 * and the Primary key.
 * <p>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 * 
 * @author JCHUET
 */

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractEntity implements Serializable, Marshalizable {
    private Long id;

    @Id
    @GeneratedValue
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
