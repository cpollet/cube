/*
 * Copyright 2014 Christophe Pollet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.cpollet.ledcube.effects;

import net.cpollet.ledcube.CubeScene;

/**
 * @author Christophe Pollet
 */
public class Random extends BaseEffect {
	public Random(CubeScene scene) {
		super(scene);
	}

	@Override
	public void step() {
		for (int i = 0; i < cubeSize(); i++) {
			for (int j = 0; j < cubeSize(); j++) {
				for (int k = 0; k < cubeSize(); k++) {
					setState(i, j, k, Math.random() > 0.9d);
				}
			}
		}

		updateScene();

		sleep(500);
	}
}
