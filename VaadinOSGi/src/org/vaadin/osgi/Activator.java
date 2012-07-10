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
package org.vaadin.osgi;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

import com.vaadin.Application;

/**
 * Activator for this bundle which opens a service tracker which looks for
 * {@link ComponentFactory} registrations with the name
 * <code>com.vaadin.Application/*</code> where * is the alias under which to
 * register the Vaadin application.
 * 
 * <p/>
 * In order to turn production mode on a configuration must be provided for the
 * application. The PID to use is
 * <code>com.vaadin.Application.<em>alias</em></code>.
 * 
 * <p/>
 * An easy way to provide this configuration is to use FileInstall and create a
 * file of the same name as the PID but with the extension .cfg. e.g.
 * <code>com.vaadin.Application/guessit</code> would require a file called
 * <code>com.vaadin.Application.guessit.cfg</code>. The contents of this file
 * would contain the property <code>productionMode=true</code> and any other
 * parameters that would normally passed to the Vaadin servlet as init
 * parameters.
 * 
 * @author brindy (with help from Neil Bartlett)
 */
public class Activator implements BundleActivator {

	private VaadinAppTracker tracker;

	@Override
	public void start(BundleContext context) throws Exception {
		tracker = new VaadinAppTracker(context);
		tracker.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		tracker.close();
		tracker = null;
	}

}

/**
 * This is the tracker which looks for the {@link ComponentFactory}
 * registrations. For each {@link ComponentFactory} found it creates a tracker
 * for {@link HttpService}.
 * 
 * @author brindy
 */
class VaadinAppTracker extends ServiceTracker {

	private static final String PREFIX = "com.vaadin.Application";

	private Map<ServiceReference, HttpServiceTracker> trackers = new IdentityHashMap<ServiceReference, HttpServiceTracker>();

	public VaadinAppTracker(BundleContext ctx) throws InvalidSyntaxException {
		super(ctx, ctx
				.createFilter("(component.factory=com.vaadin.Application/*)"),
				null);
	}

	@Override
	public Object addingService(ServiceReference reference) {
		Object o = super.addingService(reference);

		if (o instanceof ComponentFactory) {
			ComponentFactory factory = (ComponentFactory) o;
			String name = (String) reference.getProperty("component.factory");

			String alias = name.substring(PREFIX.length());

			HttpServiceTracker tracker = new HttpServiceTracker(this.context,
					factory, alias);
			tracker.open();

			HttpServiceTracker oldTracker = trackers.put(reference, tracker);

			if (oldTracker != null) {
				oldTracker.close();
				oldTracker = null;
			}

			tracker.open();
		}

		return o;
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {

		HttpServiceTracker tracker = trackers.remove(reference);
		if (tracker != null) {
			tracker.close();
			tracker = null;
		}

	}

}

/**
 * This tracker takes a {@link ComponentFactory} and then creates a
 * {@link AppRegistration} class which is then registered as a
 * {@link ManagedService} to receive configuration for that specific
 * application.
 * 
 * @author brindy
 */
class HttpServiceTracker extends ServiceTracker {

	protected ComponentFactory factory;

	protected String alias;

	private Map<HttpService, AppRegistration> configs = new IdentityHashMap<HttpService, AppRegistration>();

	public HttpServiceTracker(BundleContext ctx, ComponentFactory factory,
			String alias) {
		super(ctx, HttpService.class.getName(), null);
		this.factory = factory;
		this.alias = alias;
	}

	@Override
	public Object addingService(ServiceReference reference) {
		HttpService http = (HttpService) super.addingService(reference);
		AppRegistration config = new AppRegistration(http, factory, alias);

		// save it for later
		configs.put(http, config);

		// register as a managed service so that alternative properties can
		// be provided
		Hashtable<String, String> properties = new Hashtable<String, String>();
		properties
				.put(Constants.SERVICE_PID, "com.vaadin.Application." + alias);
		context.registerService(ManagedService.class.getName(), config,
				properties);

		return http;
	}

	@Override
	public void removedService(ServiceReference reference, Object service) {
		configs.remove(service).kill();
		super.removedService(reference, service);
	}

}

/**
 * This class is responsible for registering the {@link ComponentFactory} as a
 * vaadin {@link Application}. It is a {@link ManagedService} so that it can
 * receive properties which are then passed in to the {@link VaadinOSGiServlet}
 * as init parameters, e.g. to enable production mode.
 * 
 * @author brindy
 */
class AppRegistration implements ManagedService {

	private final HttpService http;

	private final ComponentFactory factory;

	private final String alias;

	private VaadinOSGiServlet servlet;

	public AppRegistration(HttpService http, ComponentFactory factory,
			String alias) {
		super();
		this.http = http;
		this.factory = factory;
		this.alias = alias;
	}

	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties)
			throws ConfigurationException {
		kill();
		servlet = new VaadinOSGiServlet(factory);
		try {
			http.registerServlet(alias, servlet, properties, null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void kill() {
		if (servlet != null) {
			http.unregister(alias);
			servlet.destroy();
			servlet = null;
		}
	}

}
