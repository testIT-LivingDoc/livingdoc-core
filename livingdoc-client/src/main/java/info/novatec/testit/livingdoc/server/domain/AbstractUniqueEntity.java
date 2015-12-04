package info.novatec.testit.livingdoc.server.domain;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractUniqueEntity extends AbstractVersionedEntity {
    protected String uuid;

    public AbstractUniqueEntity() {
        uuid = UUID.randomUUID().toString();
    }

    @Basic
    @Column(name = "UUID", nullable = false)
    public String getUUID() {
        return this.uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractUniqueEntity other = ( AbstractUniqueEntity ) obj;
        if (uuid == null) {
            if (other.uuid != null) {
                return false;
            }
        } else if ( ! uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( uuid == null ) ? 0 : uuid.hashCode() );
        return result;
    }
}
