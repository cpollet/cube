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
public class Rain extends BaseEffect {
	private long speed = 100;

	public Rain(CubeScene cubeScene) {
		super(cubeScene);
	}

	public Rain withSpeed(long speed) {
		this.speed = speed;
		return this;
	}

	void init() {
		super.init();
		initDrops();
		updateScene();
	}

	public void step() {
		for (int i = 0; i < cubeSize(); i++) {
			for (int k = 0; k < cubeSize(); k++) {
				for (int j = 0; j < cubeSize() - 1; j++) {
					copyState(i, j + 1, k, i, j, k);
				}
				turnOff(i, cubeSize() - 1, k);
			}
		}
		initDrops();

		sleep(speed);
	}

	private void initDrops() {
		long drops = Math.round(Math.random() * 3);
		for (int i = 0; i < drops; i++) {
			int x = (int) Math.floor(Math.random() * cubeSize());
			int z = (int) Math.floor(Math.random() * cubeSize());

			turnOn(x, cubeSize() - 1, z);
		}
	}
}
