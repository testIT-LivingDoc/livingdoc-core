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
package info.novatec.testit.livingdoc.ogn;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import info.novatec.testit.livingdoc.reflect.AbstractFixture;
import info.novatec.testit.livingdoc.reflect.Fixture;
import info.novatec.testit.livingdoc.reflect.Message;
import info.novatec.testit.livingdoc.reflect.PlainOldFixture;


public class ObjectGraphNavigationFixture extends AbstractFixture implements ObjectGraphNavigationMessageResolver {
    public ObjectGraphNavigationFixture(Object target) {
        super(target);
    }

    @Override
    public Fixture fixtureFor(Object paramTarget) {
        return new ObjectGraphNavigationFixture(paramTarget);
    }

    @Override
    protected Message getCheckMessage(String name) {
        PlainOldFixture plainOldFixture = new PlainOldFixture(target);

        if (plainOldFixture.canCheck(name)) {
            return plainOldFixture.check(name);
        }

        return getMessage(name, true, true);
    }

    @Override
    protected Message getSendMessage(String name) {
        PlainOldFixture plainOldFixture = new PlainOldFixture(target);

        if (plainOldFixture.canSend(name)) {
            return plainOldFixture.send(name);
        }

        return getMessage(name, false, true);
    }

    @Override
    public Message resolve(ObjectGraphNavigationInfo info) {
        if (info.getTarget() == null) {
            return resolveWithPlainOldFixture(info);
        }
        Class< ? > clazz = target.getClass();
        String[] splits = info.getTarget().split("\\.");
        LinkedList<ObjectGraphInvocable> invocations = new LinkedList<ObjectGraphInvocable>();

        for (String split : splits) {
            List<Method> methods = getMethods(clazz, split);
            Method method = methods != null && methods.size() > 0 ? methods.get(0) : null;

            if (method == null) {
                method = getGetter(clazz, split);
            }

            if (method == null) {
                Field field = getField(clazz, split);

                if (field == null) {
                    break;
                }
                clazz = field.getType();
                invocations.add(new ObjectGraphFieldInvocation(field, true));
            } else {
                clazz = method.getReturnType();
                invocations.add(new ObjectGraphMethodInvocation(method, info.isGetter()));
            }
        }

        if (invocations.size() == splits.length) {
            ObjectGraphInvocable methodCall = getInvocation(clazz, info.getMethodName(), info.isGetter());

            if (methodCall != null) {
                invocations.add(methodCall);
                return new ObjectGraphNavigationMessage(target, invocations);
            }
        }

        return null;
    }

    private Message resolveWithPlainOldFixture(ObjectGraphNavigationInfo info) {
        PlainOldFixture fixture = new PlainOldFixture(target);

        if (info.isGetter()) {
            if (fixture.canCheck(info.getMethodName())) {
                return fixture.check(info.getMethodName());
            }
        } else {
            if (fixture.canSend(info.getMethodName())) {
                return fixture.send(info.getMethodName());
            }
        }

        return null;
    }

    private Message getMessage(String name, boolean isGetter, boolean checkSut) {
        ObjectGraphNavigation graph = new ObjectGraphNavigation(isGetter, this);

        Message message = graph.resolveMessage(name);

        if (message != null) {
            return message;
        }

        if (checkSut) {
            if (getSystemUnderTest() == null) {
                return null;
            }

            ObjectGraphNavigationFixture fixture = new ObjectGraphNavigationFixture(getSystemUnderTest());

            return isGetter ? fixture.getCheckMessage(name) : fixture.getSendMessage(name);
        }

        return null;
    }

    private ObjectGraphInvocable getInvocation(Class< ? > clazz, String name, boolean isGetter) {
        List<Method> methods = getMethods(clazz, name);
        Method method = methods != null && methods.size() > 0 ? methods.get(0) : null;

        if (method == null) {
            method = isGetter ? getGetter(clazz, name) : getSetter(clazz, name);
        }

        if (method == null) {
            Field field = getField(clazz, name);

            if (field != null) {
                return new ObjectGraphFieldInvocation(field, isGetter);
            }
        } else {
            return new ObjectGraphMethodInvocation(method, isGetter);
        }

        return null;
    }
}
