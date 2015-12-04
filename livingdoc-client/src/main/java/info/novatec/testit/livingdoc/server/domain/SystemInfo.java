package info.novatec.testit.livingdoc.server.domain;

import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "SYSTEM_INFO")
@SuppressWarnings("serial")
public class SystemInfo extends AbstractUniqueEntity {
    private String serverVersion;

    @Basic
    @Column(name = "SERVERVERSION")
    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    @Override
    public Vector<Object> marshallize() {
        return new Vector<Object>();
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            return o instanceof SystemInfo;
        }

        return false;
    }
}
