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
public class Rain implements Runnable {
	private final CubeScene scene;

	public Rain(CubeScene scene) {
		this.scene = scene;
	}

	@SuppressWarnings("InfiniteLoopStatement")
	@Override
	public void run() {
		boolean[][][] buffer = new boolean[8][8][8];

		initDrops(buffer);
		scene.setBuffer(buffer);

		while (true) {
			for (int i = 0; i < 8; i++) {
				for (int k = 0; k < 8; k++) {
					for (int j = 0; j < 7; j++) {
						buffer[i][j][k] = buffer[i][j + 1][k];
					}
					buffer[i][7][k] = false;
				}
			}
			initDrops(buffer);

			scene.setBuffer(buffer);

			try {
				Thread.sleep(60);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void initDrops(boolean[][][] buffer) {
		long drops = Math.round(Math.random() * 3);
		for (int i = 0; i < drops; i++) {
			int x = (int) Math.floor(Math.random() * 8);
			int z = (int) Math.floor(Math.random() * 8);

			buffer[x][7][z] = true;
		}
	}
}
