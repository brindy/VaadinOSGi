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
package uk.org.brindy.guessit;

import uk.org.brindy.guessit.random.RandomService;
import uk.org.brindy.guessit.view.MainView;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.vaadin.Application;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
@Component(factory = "vaadin.app", name = "guessit")
public class GuessItApp extends Application {

	private RandomService random;

	private Window mainWindow = new Window("Guess It!");

	@Override
	public void init() {
		setMainWindow(mainWindow);
		mainWindow.setWidth("300px");
		new MainView(this);
	}

	@Reference
	public void setRandomService(RandomService service) {
		random = service;
	}

	public RandomService getRandomService() {
		return random;
	}

}
