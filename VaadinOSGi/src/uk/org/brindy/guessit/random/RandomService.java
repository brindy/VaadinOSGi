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
package uk.org.brindy.guessit.random;

/**
 * A contrived example of how to reference other OSGi services. We're expecting
 * at least one RandomService provider to be installed.
 * <p/>
 * The main guess it bundle ends up importing and exporting this package (code
 * smell), ideally this package should be in it's own bundle and only exported.
 * 
 * @author brindy
 * 
 */
public interface RandomService {

	/**
	 * @return a random number between 1 and 100.
	 */
	int nextRandomNumber();

}
