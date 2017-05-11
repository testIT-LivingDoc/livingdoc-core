/*
 * Copyright (c) 2008 Pyxis Technologies inc.
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
package info.novatec.testit.livingdoc.server.rpc.runner;

import info.novatec.testit.livingdoc.server.LivingDocServerException;
import info.novatec.testit.livingdoc.server.domain.*;
import info.novatec.testit.livingdoc.server.rest.LivingDocRestClient;


public class RestRemoteRunner {

    private final LivingDocRestClient client;

    public RestRemoteRunner(final String url, final String user, final String password) {
        client = new LivingDocRestClient(url, user, password);
    }

    public DocumentNode getSpecificationHierarchy(Repository repository, SystemUnderTest systemUnderTest)
        throws LivingDocServerException {
        return client.getSpecificationHierarchy(repository, systemUnderTest, getIdentifier());
    }

    public Execution runSpecification(String projectName, String sutName, String repositoryId, String specificationName,
        boolean implementedVersion, String locale) throws LivingDocServerException {
        SystemUnderTest sut = SystemUnderTest.newInstance(sutName);
        sut.setProject(Project.newInstance(projectName));
        sut.setRunner(Runner.newInstance(""));

        Specification specification = Specification.newInstance(specificationName);
        specification.setRepository(Repository.newInstance(repositoryId));

        return runSpecification(sut, specification, implementedVersion, locale);
    }

    public Execution runSpecification(SystemUnderTest sut, Specification specification, boolean implementedVersion,
        String locale) throws LivingDocServerException {
        if (sut.getProject() == null) {
            throw new IllegalArgumentException("Missing Project in SystemUnderTest");
        }

        if (specification.getRepository() == null) {
            throw new IllegalArgumentException("Missing Repository in Specification");
        }

        return client.runSpecification(sut, specification, implementedVersion, locale, getIdentifier());
    }

    public Reference runReference(String projectName, String sutName, String requirementRepositoryId, String requirementName,
        String locale) throws LivingDocServerException {
        return runReference(projectName, sutName, requirementRepositoryId, requirementName, null, null, locale);
    }

    public Reference runReference(String projectName, String sutName, String requirementRepositoryId, String requirementName,
        String repositoryId, String specificationName, String locale) throws LivingDocServerException {
        SystemUnderTest sut = SystemUnderTest.newInstance(sutName);
        sut.setProject(Project.newInstance(projectName));

        Specification specification = Specification.newInstance(specificationName == null ? requirementName
            : specificationName);
        specification.setRepository(Repository.newInstance(repositoryId == null ? requirementRepositoryId : repositoryId));

        Requirement requirement = Requirement.newInstance(requirementName);
        requirement.setRepository(Repository.newInstance(requirementRepositoryId));

        Reference reference = Reference.newInstance(requirement, specification, sut);

        return runReference(reference, locale);
    }

    public Reference runReference(SystemUnderTest sut, Specification specification, Requirement requirement, String locale)
        throws LivingDocServerException {
        if (sut.getProject() == null) {
            throw new IllegalArgumentException("Missing Project in SystemUnderTest");
        }

        if (specification.getRepository() == null) {
            throw new IllegalArgumentException("Missing Repository in Specification");
        }

        if (requirement.getRepository() == null) {
            throw new IllegalArgumentException("Missing Repository in Requirement");
        }

        Reference reference = Reference.newInstance(requirement, specification, sut);

        return runReference(reference, locale);
    }

    public Reference runReference(Reference reference, String locale) throws LivingDocServerException {
        return client.runReference(reference, locale, getIdentifier());
    }

    private String getIdentifier() {
        return null;
    }
}
