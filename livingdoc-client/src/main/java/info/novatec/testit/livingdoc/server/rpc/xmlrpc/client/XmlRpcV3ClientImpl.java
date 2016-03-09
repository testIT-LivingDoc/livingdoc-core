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

import info.novatec.testit.livingdoc.util.ClientUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcLiteHttpTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Vector;

import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.CALL_FAILED;
import static info.novatec.testit.livingdoc.server.LivingDocServerErrorKey.XML_RPC_URL_NOTFOUND;


public class XmlRpcV3ClientImpl implements XmlRpcClientExecutor {

    private final XmlRpcClient client;
    private static final Logger logger = LoggerFactory.getLogger(XmlRpcV3ClientImpl.class);

    public XmlRpcV3ClientImpl(String url) throws XmlRpcClientExecutorException {

        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(url));

            this.client = new XmlRpcClient();

            client.setTransportFactory(new XmlRpcLiteHttpTransportFactory(client));
            client.setConfig(config);
        } catch (MalformedURLException ex) {
            logger.error("Error executing XML RPC call", ex);
            ex.printStackTrace();
            throw new XmlRpcClientExecutorException(XML_RPC_URL_NOTFOUND, ex);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object execute(String method, List< ? > params) throws XmlRpcClientExecutorException {
        try {
            Object result = client.execute(method, params);

            List<Object> execParams;

            // Patch xmlrpc 3.x+
            if (result.getClass().isArray()) {
                Object[] resultArray = ( Object[] ) result;
                execParams = ClientUtils.vectorizeDeep(resultArray);
            } else if (result instanceof Vector) {
                execParams = ( Vector<Object> ) result;
            } else {
                execParams = new Vector<Object>();
                execParams.add(result);
            }

            return execParams;
        } catch (XmlRpcException ex) {
            throw new XmlRpcClientExecutorException(CALL_FAILED, ex);
        }
    }

}
