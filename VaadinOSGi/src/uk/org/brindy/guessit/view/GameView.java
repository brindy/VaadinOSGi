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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class GameView {

	@SuppressWarnings("serial")
	public GameView(final GuessItApp app) {
		System.out.println("GameView.GameView()");
		final Session session = (Session) app.getUser();
		if (!session.gameInProgress()) {
			session.startGame();
		}

		VerticalLayout vbox = new VerticalLayout();
		vbox.addComponent(new Label("Hello " + session.name));
		vbox.addComponent(new Label("Guess the number between 1 and 100"));

		final TextField number = new TextField();
		vbox.addComponent(number);

		Button button = new Button("Guess");
		button.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				try {
					if (session.guess(Integer.parseInt((String) number
							.getValue()))) {
						new WelldoneView(app);
					} else {
						new GameView(app);
					}
				} catch (NumberFormatException e) {
					session.guess(-1);
					new GameView(app);
				}
			}
		});

		switch (session.getHint()) {
		case NOGUESSES:
			// do nothing
			break;

		case HIGHER:
			vbox.addComponent(new Label("Try higher..."));
			break;

		case LOWER:
			vbox.addComponent(new Label("Not that high!"));
			break;

		case WRONG:
			vbox.addComponent(new Label("I didn't quite get that."));
			break;
		}

		vbox.addComponent(button);

		vbox.addComponent(new Label("You have had " + session.getGuessCount()
				+ " guesses"));

		app.getMainWindow().setContent(vbox);
	}

}
