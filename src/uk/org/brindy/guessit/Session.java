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

public class Session {

	public final String name;

	private final GuessItApp app;

	private Integer number;

	private int lastGuess;

	private int guessCount;

	public Session(GuessItApp app, String name) {
		this.app = app;
		this.name = name;
	}

	public boolean gameInProgress() {
		return number != null;
	}

	public void startGame() {
		guessCount = 0;
		number = app.getRandomService().nextRandomNumber();
		System.out.println("Number : " + number);
	}

	public boolean guess(int guess) {
		lastGuess = guess;
		guessCount++;
		return guess == number;
	}

	public int getGuessCount() {
		return guessCount;
	}

	public Hint getHint() {

		if (0 == guessCount) {
			return Hint.NOGUESSES;
		}

		if (-1 == lastGuess) {
			return Hint.WRONG;
		}

		if (lastGuess > number) {
			return Hint.LOWER;
		}

		return Hint.HIGHER;
	}

	public static enum Hint {
		NOGUESSES, HIGHER, LOWER, WRONG
	}

}
