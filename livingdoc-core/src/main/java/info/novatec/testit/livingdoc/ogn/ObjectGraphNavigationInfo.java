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

public class ObjectGraphNavigationInfo {
    private final boolean isGetter;
    private final String target;
    private final String methodName;

    public ObjectGraphNavigationInfo(String methodName) {
        this(false, null, methodName);
    }

    public ObjectGraphNavigationInfo(String target, String methodName) {
        this(false, target, methodName);
    }

    public ObjectGraphNavigationInfo(boolean isGetter, String target, String methodName) {
        this.isGetter = isGetter;
        this.target = target;
        this.methodName = methodName;
    }

    public boolean isGetter() {
        return isGetter;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        if (getTarget() != null) {
            sb.append(getTarget());
        }

        sb.append('#').append(getMethodName());

        return sb.toString();
    }
}
