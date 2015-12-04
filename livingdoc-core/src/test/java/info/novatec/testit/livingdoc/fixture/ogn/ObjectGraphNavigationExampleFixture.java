/* Copyright (c) 2007 Pyxis Technologies inc.
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
package info.novatec.testit.livingdoc.fixture.ogn;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import info.novatec.testit.livingdoc.ogn.ObjectGraphNavigationFixture;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.SystemUnderDevelopmentException;
import info.novatec.testit.livingdoc.reflect.annotation.FixtureClass;


@FixtureClass
public class ObjectGraphNavigationExampleFixture {
    public String objectGraph;
    private ObjectGraphNavigationFixture fixture;
    private Stack<String> stack = new Stack<String>();

    private Job job = new Job(new Status(), new Title());

    public ObjectGraphNavigationExampleFixture() {
        fixture = new ObjectGraphNavigationFixture(this);
    }

    public String getMethodName() throws IllegalArgumentException, InvocationTargetException, IllegalAccessException,
        SystemUnderDevelopmentException {
        stack.clear();

        Message message = fixture.check(objectGraph);
        message.send();

        String s = stack.toString();

        return s.substring(1, s.length() - 1);
    }

    public String getEmployeeNameFirst() {
        stack.add("Fixture#getEmployeeNameFirst");
        return null;
    }

    public Job getJob() {
        stack.add("Fixture#getJob");
        return job;
    }

    public class Job {
        private Status status;
        private Title title;

        public Job(Status status, Title title) {
            this.status = status;
            this.title = title;
        }

        public Status getStatus() {
            stack.add("Job#getStatus");
            return status;
        }

        public String getTitleShortDescription() {
            stack.add("Job#getTitleShortDescription");
            return null;
        }

        public Title getTitle() {
            stack.add("Job#getTitle");
            return title;
        }
    }

    public class Status {
        public Status() {
        }

        public String getId() {
            stack.add("Status#getId");
            return null;
        }
    }

    public class Title {
        public Title() {
        }

        public String getShortDescription() {
            stack.add("Title#getShortDescription");
            return null;
        }

        public String getLongDescription() {
            stack.add("Title#getLongDescription");
            return null;
        }
    }
}
