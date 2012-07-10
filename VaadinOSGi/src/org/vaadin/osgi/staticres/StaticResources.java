/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vaadin.osgi.staticres;

import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.vaadin.Application;

/**
 * This class runs as an OSGi component and serves the themes and widgetsets
 * directly from the core Vaadin bundle.
 * <p/>
 * 
 * To add your own theme or widget set create a fragment which contains your
 * theme/widgetset files and export those as packages. The
 * <code>Fragment-Host</code> should be set to the Vaadin core bundle. The
 * fragment containing your theme/widgetset resources will be added to the core
 * Vaadin bundle dynamically.
 * <p/>
 * 
 * Of course static resources should really be deployed separately to a web
 * server that proxies servlet requests on to the container.
 * <p/>
 * 
 * Implementation updated to suggestion by zdilla in <a
 * href="https://github.com/brindy/VaadinOSGi/issues/1">Issue 1</a>
 * <p/>
 * 
 * @author brindy, zdilla
 */
@Component(properties = { "http.alias=/VAADIN" }, immediate=true)
public class StaticResources implements HttpContext {

	private static final String RESOURCE_BASE = "/VAADIN";

	@SuppressWarnings("unused")
	@Reference(unbind = "unsetHttpService", dynamic = true, optional = true, multiple = true)
	private void setHttpService(final HttpService httpService)
			throws NamespaceException {
		httpService.registerResources(RESOURCE_BASE, RESOURCE_BASE,
				StaticResources.this);
	}

	@SuppressWarnings("unused")
	private void unsetHttpService(final HttpService httpService) {
		httpService.unregister(RESOURCE_BASE);
	}

	@Override
	public boolean handleSecurity(final HttpServletRequest request,
			final HttpServletResponse response) throws IOException {
		return true;
	}

	@Override
	public URL getResource(final String name) {
		return Application.class.getResource("/" + name);
	}

	@Override
	public String getMimeType(final String name) {
		// TODO create better implementation
		try {
			final URL url = Application.class.getResource("/" + name);
			return url == null ? null : url.openConnection().getContentType();
		} catch (final IOException e) {
			return null;
		}
	}

}
