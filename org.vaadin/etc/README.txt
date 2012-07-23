
Contents:

- README.txt - this file

- org.vaadin.osgi_1.0.1.jar - Allows you to register a Vaadin Application as an OSGi component and automatically creates a servlet entry point for your application.

- support/org.vaadin.osgi.staticres_1.0.1.jar - Proxies resources from the Vaadin bundle.  If you want to add your own resources create an OSGi fragment and attach it to the Vaadin bundle.  However, it's recommended that you deploy static resources to a web-server to keep unnecessary load off the server.

- sample/uk.org.brindy.guessit_1.0.1.jar - A sample Vaadin application as an OSGi bundle.  The application itself depends on an OSGi service (RandomService) to demonstrate how the application will be injected with services.

- sample/uk.org.brindy.guessit.support_1.0.1.jar - Provides an implementation of the RandomService to demonstrate injection via declarative services.


Installation:

You need an OSGi container with the following services/bundles provided:
- HTTP Service
- Configuration Management
- Declarative Services (SCR)
- Vaadin 

Deploy all the bundles above and if your environment is configured correctly you will be able to access the app at a URL like http://localhost:8080/guessit/


Source: All source is included in the respective bundle in the OPT-OSGI folder.


License: Apache Licenses 2.0

