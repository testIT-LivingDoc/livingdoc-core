package info.novatec.testit.livingdoc.server.domain;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_TYPE_NAME_FORMAT_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_TYPE_NAME_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_TYPE_REPOCLASS_IDX;
import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.REPOSITORY_TYPE_URI_FORMAT_IDX;

import java.net.URI;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

import info.novatec.testit.livingdoc.util.URIUtil;


/**
 * RepositoryType Class. Known types: CONFLUENCE / FILE ...
 * <p>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 */

@Entity
@Table(name = "REPOSITORY_TYPE")
@SuppressWarnings("serial")
public class RepositoryType extends AbstractVersionedEntity implements Comparable<RepositoryType> {
    private String name;
    private String documentUrlFormat;
    private String testUrlFormat;
    private String className;

    public static RepositoryType newInstance(String name) {
        RepositoryType type = new RepositoryType();
        type.setName(name);
        return type;
    }

    @Basic
    @Column(name = "NAME", unique = true, nullable = false, length = 255)
    public String getName() {
        return name;
    }

    @Basic
    @Column(name = "DOCUMENT_URL_FORMAT", nullable = true, length = 255)
    public String getDocumentUrlFormat() {
        return documentUrlFormat;
    }

    @Basic
    @Column(name = "TEST_URL_FORMAT", nullable = true, length = 255)
    public String getTestUrlFormat() {
        return testUrlFormat;
    }

    @Basic
    @Column(name = "CLASS_NAME", nullable = false, length = 255)
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDocumentUrlFormat(String documentUrlFormat) {
        this.documentUrlFormat = StringUtils.stripToNull(documentUrlFormat);
    }

    public void setTestUrlFormat(String testUrlFormat) {
        this.testUrlFormat = StringUtils.stripToNull(testUrlFormat);
    }

    public String resolveName(Document document) {
        if ( ! StringUtils.isEmpty(documentUrlFormat)) {
            return String.format(documentUrlFormat, document.getRepository().getBaseRepositoryUrl(), document.getName());
        }

        return null;
    }

    public String resolveUri(Document document) {
        if ( ! StringUtils.isEmpty(testUrlFormat)) {
            return String.format(testUrlFormat, document.getRepository().getBaseTestUrl(), document.getName());
        }

        return null;
    }

    public String asFactoryArguments(Repository repository, boolean withStyle, String user, String pwd) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.className).append(';').append(withStyle || name.equals("FILE") ? repository.getBaseTestUrl()
            : withNoStyle(repository.getBaseTestUrl()));

        if (user == null) {
            if ( ! StringUtils.isEmpty(repository.getUsername())) {
                sb.append(';').append(repository.getUsername()).append(';').append(StringUtils.replaceEach(repository
                    .getPassword(), new String[] { ";" }, new String[] { "%3B" }));
            }
        } else {
            sb.append(';').append(user).append(';').append(StringUtils.replaceEach(pwd, new String[] { ";" }, new String[] {
                    "%3B" }));
        }

        return sb.toString();
    }

    @Override
    public Vector<Object> marshallize() {
        Vector<Object> parameters = new Vector<Object>();
        parameters.add(REPOSITORY_TYPE_NAME_IDX, StringUtils.stripToEmpty(name));
        parameters.add(REPOSITORY_TYPE_REPOCLASS_IDX, className);
        parameters.add(REPOSITORY_TYPE_NAME_FORMAT_IDX, StringUtils.stripToEmpty(getDocumentUrlFormat()));
        parameters.add(REPOSITORY_TYPE_URI_FORMAT_IDX, StringUtils.stripToEmpty(getTestUrlFormat()));
        return parameters;
    }

    @Override
    public int compareTo(RepositoryType o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || ! ( o instanceof RepositoryType )) {
            return false;
        }

        RepositoryType typeCompared = ( RepositoryType ) o;
        return getName().equals(typeCompared.getName());
    }

    @Override
    public int hashCode() {
        return getName() == null ? 0 : getName().hashCode();
    }

    private String withNoStyle(String location) {
        URI uri = URI.create(URIUtil.raw(location));
        StringBuilder sb = new StringBuilder(23);
        if (uri.getScheme() != null) {
            sb.append(uri.getScheme()).append("://");
        }
        if (uri.getAuthority() != null) {
            sb.append(uri.getAuthority());
        }
        if (uri.getPath() != null) {
            sb.append(uri.getPath());
        }

        String query = uri.getQuery();
        if (query == null) {
            sb.append("?\"?includeStyle=false\"");
        } else {
            sb.append("?" + query + "&includeStyle=false");
        }

        if (uri.getFragment() != null) {
            sb.append('#').append(uri.getFragment());
        }

        return sb.toString();
    }
}
