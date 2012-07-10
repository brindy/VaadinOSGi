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
package uk.org.brindy.guessit.view;

import uk.org.brindy.guessit.GuessItApp;
import uk.org.brindy.guessit.Session;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class WelldoneView {

	@SuppressWarnings("serial")
	public WelldoneView(final GuessItApp app) {
		System.out.println("WelldoneView.WelldoneView()");
		final Session session = (Session) app.getUser();
		VerticalLayout vbox = new VerticalLayout();
		vbox.addComponent(new Label("Yay! You guessed it in "
				+ session.getGuessCount() + " guesses"));
		Button button = new Button("Play again");
		button.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				session.startGame();
				new GameView(app);
			}
		});
		vbox.addComponent(button);
		app.getMainWindow().setContent(vbox);
	}

}
