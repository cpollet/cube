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
	private final CubeScene scene;
	private long speed = 100;
	private int cubeSize = 8;

	private boolean[][][] buffer;

	public Rain(CubeScene scene) {
		this.scene = scene;
	}

	public Rain withSpeed(long speed) {
		this.speed = speed;
		return this;
	}

	public Rain withCubeSize(int cubeSize) {
		this.cubeSize = cubeSize;
		return this;
	}

	void init() {
		buffer = new boolean[cubeSize][cubeSize][cubeSize];

		initDrops(buffer);
		scene.setBuffer(buffer);
	}

	public void step() {
		for (int i = 0; i < cubeSize; i++) {
			for (int k = 0; k < cubeSize; k++) {
				for (int j = 0; j < cubeSize - 1; j++) {
					buffer[i][j][k] = buffer[i][j + 1][k];
				}
				buffer[i][cubeSize - 1][k] = false;
			}
		}
		initDrops(buffer);

		scene.setBuffer(buffer);

		sleep(speed);
	}

	private void initDrops(boolean[][][] buffer) {
		long drops = Math.round(Math.random() * 3);
		for (int i = 0; i < drops; i++) {
			int x = (int) Math.floor(Math.random() * cubeSize);
			int z = (int) Math.floor(Math.random() * cubeSize);

			buffer[x][cubeSize - 1][z] = true;
		}
	}
}
