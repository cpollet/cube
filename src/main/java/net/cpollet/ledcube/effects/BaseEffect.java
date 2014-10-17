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
public abstract class BaseEffect implements Effect {
	protected CubeScene scene;
	protected boolean[][][] buffer;

	private volatile boolean shouldStop;
	private volatile boolean paused;
	private volatile boolean executeOneStep;

	private Thread thread;

	private long lastTime;
	private int nbFrames;

	protected BaseEffect(CubeScene cubeScene) {
		this.scene = cubeScene;
	}

	@Override
	public Effect start() {
		if (isRunning()) {
			return this;
		}

		shouldStop = false;
		paused = false;

		lastTime = System.currentTimeMillis();
		nbFrames = 0;

		thread = new Thread(this);
		thread.start();

		return this;
	}

	private boolean isRunning() {
		return thread != null && thread.isAlive();
	}

	@Override
	public Effect stop() {
		shouldStop = true;
		return this;
	}

	@Override
	public Effect togglePause() {
		paused = !paused;
		return this;
	}

	@Override
	public void run() {
		init();

		while (!shouldStop) {
			if (!paused || executeOneStep) {
				step();
				updateScene();
				executeOneStep = false;
				updateFPS();
			}
		}
	}

	private void updateFPS() {
		long currentTime = System.currentTimeMillis();

		if (currentTime - lastTime < 1000) {
			nbFrames++;
			return;
		}

		scene.setEffectFps(nbFrames);
		nbFrames = 0;
		lastTime = System.currentTimeMillis();
	}

	@Override
	public Effect next() {
		executeOneStep = true;
		return this;
	}

	int cubeSize() {
		return scene.getSize();
	}

	void updateScene() {
		scene.setBuffer(buffer);
	}

	void turnOn(int x, int y, int z) {
		buffer[x][y][z] = true;
	}

	void turnOff(int x, int y, int z) {
		buffer[x][y][z] = false;
	}

	boolean getState(int x, int y, int z) {
		return buffer[x][y][z];
	}

	void setState(int x, int y, int z, boolean state) {
		buffer[x][y][z] = state;
	}

	void copyState(int xFrom, int yFrom, int zFrom, int xTo, int yTo, int zTo) {
		buffer[xTo][yTo][zTo] = buffer[xFrom][yFrom][zFrom];
	}

	void init() {
		buffer = new boolean[cubeSize()][cubeSize()][cubeSize()];
	}

	abstract void step();

	void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
