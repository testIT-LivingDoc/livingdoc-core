package info.novatec.testit.livingdoc.server.domain;

import info.novatec.testit.livingdoc.systemunderdevelopment.*;
import org.apache.commons.lang3.*;

import javax.persistence.*;
import java.util.*;

import static info.novatec.testit.livingdoc.server.rpc.xmlrpc.XmlRpcDataMarshaller.*;


/**
 * SystemUnderTest Class.
 * <p>
 * Copyright (c) 2006 Pyxis technologies inc. All Rights Reserved.
 *
 * @author JCHUET
 */

@Entity
@Table(name = "SUT", uniqueConstraints = {@UniqueConstraint(columnNames = {"NAME", "PROJECT_ID"})})
@SuppressWarnings("serial")
public class SystemUnderTest extends AbstractUniqueEntity implements Comparable<SystemUnderTest> {
    private static final transient String DEFAULT_FIXTURE_FACTORY = DefaultSystemUnderDevelopment.class.getName();

    private String name;
    private Project project;
    private Runner runner;
    private Set<String> sutClasspaths = new HashSet<String>();
    private Set<String> fixtureClasspaths = new HashSet<String>();

    private String fixtureFactory;
    private String fixtureFactoryArgs;

    private byte selected;

    private String projectDependencyDescriptor;

    public static SystemUnderTest newInstance(String name) {
        SystemUnderTest sut = new SystemUnderTest();
        sut.setName(name);
        return sut;
    }

    @Basic
    @Column(name = "NAME", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "RUNNER_ID")
    public Runner getRunner() {
        return runner;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "PROJECT_ID")
    public Project getProject() {
        return project;
    }

    @ElementCollection
    @JoinTable(name = "SUT_FIXTURE_CLASSPATHS", joinColumns = {@JoinColumn(name = "SUT_ID")})
    @Column(name = "elt", nullable = true, length = 255)
    public Set<String> getFixtureClasspaths() {
        return fixtureClasspaths;
    }

    @ElementCollection
    @JoinTable(name = "SUT_CLASSPATHS", joinColumns = {@JoinColumn(name = "SUT_ID")})
    @Column(name = "elt", nullable = true, length = 255)
    public Set<String> getSutClasspaths() {
        return sutClasspaths;
    }

    @Basic
    @Column(name = "FIXTURE_FACTORY", nullable = true, length = 255)
    public String getFixtureFactory() {
        return fixtureFactory;
    }

    @Basic
    @Column(name = "FIXTURE_FACTORY_ARGS", nullable = true, length = 255)
    public String getFixtureFactoryArgs() {
        return fixtureFactoryArgs;
    }

    @Basic
    @Column(name = "SELECTED")
    public byte getSelected() {
        return selected;
    }

    @Basic
    @Column(name = "PROJECT_DEPENDENCY_DESCRIPTOR", nullable = true, length = 255)
    public String getProjectDependencyDescriptor() {
        return projectDependencyDescriptor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setFixtureClasspaths(Set<String> fixturesClasspaths) {
        this.fixtureClasspaths = fixturesClasspaths;
    }

    public void setSutClasspaths(Set<String> sutClasspaths) {
        this.sutClasspaths = sutClasspaths;
    }

    public void setFixtureFactory(String fixtureFactory) {
        this.fixtureFactory = fixtureFactory;
    }

    public void setFixtureFactoryArgs(String fixtureFactoryArgs) {
        this.fixtureFactoryArgs = fixtureFactoryArgs;
    }

    public void setSelected(byte selected) {
        this.selected = selected;
    }

    public void setProjectDependencyDescriptor(String projectDependencyDescriptor) {
        this.projectDependencyDescriptor = projectDependencyDescriptor;
    }

    @Transient
    public boolean isDefault() {
        return selected == (byte) 1;
    }

    public void setIsDefault(boolean isSelected) {
        this.selected = isSelected ? (byte) 1 : (byte) 0;
    }

    public Execution execute(Specification specification, boolean implementedVersion, String sections, String locale) {
        return runner.execute(specification, this, implementedVersion, sections, locale);
    }

    public String fixtureFactoryCmdLineOption() {
        if (StringUtils.isEmpty(fixtureFactory)) {
            return DEFAULT_FIXTURE_FACTORY;
        }
        if (StringUtils.isEmpty(fixtureFactoryArgs)) {
            return fixtureFactory;
        }
        return fixtureFactory + ";" + fixtureFactoryArgs;
    }

    @Override
    public List<Object> marshallize() {
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(SUT_NAME_IDX, name);
        parameters.add(SUT_PROJECT_IDX, project.marshallize());
        parameters.add(SUT_CLASSPATH_IDX, new ArrayList<String>(sutClasspaths));
        parameters.add(SUT_FIXTURE_CLASSPATH_IDX, new ArrayList<String>(fixtureClasspaths));
        parameters.add(SUT_FIXTURE_FACTORY_IDX, StringUtils.stripToEmpty(fixtureFactory));
        parameters.add(SUT_FIXTURE_FACTORY_ARGS_IDX, StringUtils.stripToEmpty(fixtureFactoryArgs));
        parameters.add(SUT_IS_DEFAULT_IDX, isDefault());
        parameters.add(SUT_RUNNER_IDX, runner != null ? runner.marshallize() : Runner.newInstance("N/A").marshallize());
        parameters.add(SUT_PROJECT_DEPENDENCY_DESCRIPTOR_IDX, StringUtils.stripToEmpty(projectDependencyDescriptor));
        return parameters;
    }

    public SystemUnderTest marshallizeRest() {
        SystemUnderTest sut = SystemUnderTest.newInstance(this.getName());
        sut.setSutClasspaths(this.getSutClasspaths());
        sut.setFixtureClasspaths(this.getFixtureClasspaths());
        sut.setFixtureFactory(this.getFixtureFactory());
        sut.setFixtureFactoryArgs(this.getFixtureFactoryArgs());
        sut.setIsDefault(this.isDefault());
        sut.setRunner(this.getRunner());
        sut.setProject(this.getProject() != null ? this.getProject().marshallizeRest() : Project.newInstance(""));
        sut.setProjectDependencyDescriptor(this.getProjectDependencyDescriptor());
        sut.setVersion(this.getVersion());
        sut.setUUID(this.getUUID());
        sut.setId(this.getId());
        return sut;
    }


    @Override
    public int compareTo(SystemUnderTest o) {
        if (isDefault()) {
            return -1;
        }
        if (o.isDefault()) {
            return 1;
        }
        return name.compareTo(o.name);
    }

    private boolean isNameEqualsTo(String nameToCompare) {
        return name != null && name.equals(nameToCompare);
    }

    private boolean isProjectEqualsTo(Project projectToCompare) {
        return project != null && project.equals(projectToCompare);
    }

    public boolean equalsTo(Object o) {
        if (o == null || !(o instanceof SystemUnderTest)) {
            return false;
        }

        SystemUnderTest sutCompared = (SystemUnderTest) o;

        return isNameEqualsTo(sutCompared.getName()) && isProjectEqualsTo(sutCompared.getProject());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof SystemUnderTest)) {
            return false;
        }

        return super.equals(o);
    }
}
