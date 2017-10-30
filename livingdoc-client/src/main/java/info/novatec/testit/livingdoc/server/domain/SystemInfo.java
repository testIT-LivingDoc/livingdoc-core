package info.novatec.testit.livingdoc.server.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;


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
    public List<Object> marshallize() {
        return new ArrayList<Object>();
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            return o instanceof SystemInfo;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        return hashCode;
    }
}
