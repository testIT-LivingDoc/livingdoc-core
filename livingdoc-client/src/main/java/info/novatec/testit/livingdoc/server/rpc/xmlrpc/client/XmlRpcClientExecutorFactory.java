/**
 * Copyright (c) 2009 Pyxis Technologies inc.
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
package info.novatec.testit.livingdoc.server.rpc.xmlrpc.client;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import info.novatec.testit.livingdoc.server.LivingDocServerErrorKey;
import info.novatec.testit.livingdoc.util.ClassUtils;


/**
 * This factory provides a proper XmlRpc client implementation since Confluence
 * only supports the Apache XmlRpc v2.
 */
public class XmlRpcClientExecutorFactory {

    private static Class< ? > xmlRpcClientImplClass;

    public static XmlRpcClientExecutor newExecutor(String url) throws XmlRpcClientExecutorException {

        if (xmlRpcClientImplClass == null) {
            Class< ? > clientClass;
            // If version 2 is not detected, we'll choose the implementation of
            // v3
            if (loadClass("org.apache.xmlrpc.XmlRpcClient") == null) {
                // v3
                clientClass = loadClass("info.novatec.testit.livingdoc.server.rpc.xmlrpc.client.XmlRpcV3ClientImpl");
            } else {
                // v2
                clientClass = loadClass("info.novatec.testit.livingdoc.server.rpc.xmlrpc.client.XmlRpcV2ClientImpl");
            }
            xmlRpcClientImplClass = clientClass;
        }

        try {
            Constructor< ? > c = xmlRpcClientImplClass.getConstructor(new Class[] { String.class });
            return ( XmlRpcClientExecutor ) c.newInstance(url);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            throw new XmlRpcClientExecutorException(LivingDocServerErrorKey.GENERAL_ERROR, ex);
        }
    }

    private static Class< ? > loadClass(String className) {

        try {
            return ClassUtils.loadClass(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
