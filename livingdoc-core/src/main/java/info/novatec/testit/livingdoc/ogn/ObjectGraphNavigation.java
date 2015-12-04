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

import info.novatec.testit.livingdoc.reflect.Message;


public class ObjectGraphNavigation {

    private final boolean isGetter;
    private final ObjectGraphNavigationMessageResolver resolver;
    private Message message;

    public ObjectGraphNavigation(boolean isGetter, ObjectGraphNavigationMessageResolver resolver) {
        this.isGetter = isGetter;
        this.resolver = resolver;
    }

    public Message resolveMessage(String name) {
        String normalizedName = name.replaceAll("\\(\\)", "");
        normalizedName = normalizedName.replaceAll("\\.", " ");

        visit(null, new String[0], normalizedName.split("\\s"));

        return message;
    }

    private void visit(String superTarget, String[] targets, String[] methods) {
        if (message != null) {
            return;
        }

        String target = toString(targets, 0, targets.length);
        String method = toString(methods, 0, methods.length);

        message = resolve(new ObjectGraphNavigationInfo(isGetter, toTarget(superTarget, target, false), method));

        if (message == null) {
            for (int i = methods.length - 1; i > 0; i -- ) {
                String newSuperTarget = toTarget(superTarget, target, true);
                String[] left = new String[i];
                String[] right = new String[methods.length - i];

                System.arraycopy(methods, 0, left, 0, left.length);
                System.arraycopy(methods, i, right, 0, right.length);

                visit(newSuperTarget, left, right);
            }
        }
    }

    private String toString(String[] splits, int start, int end) {
        StringBuffer sb = new StringBuffer();

        for (int i = start; i < end; i ++ ) {
            sb.append(splits[i]);
        }

        if (sb.length() == 0) {
            return null;
        }
        return sb.toString();
    }

    private String toTarget(String superTarget, String target, boolean returnNullIfEmpty) {
        if (superTarget != null && target != null) {
            return superTarget + '.' + target;
        } else if (target != null) {
            return target;
        } else if (returnNullIfEmpty) {
            return null;
        } else {
            return null;
        }
    }

    private Message resolve(ObjectGraphNavigationInfo info) {
        return resolver.resolve(info);
    }
}
