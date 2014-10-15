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

/**
 * @author Christophe Pollet
 */
public abstract class BaseEffect implements Effect {
	private volatile boolean shouldStop;
	private volatile boolean paused;
	private Thread thread;
	private boolean executeOneStep;

	@Override
	public Effect start() {
		if (isRunning()) {
			return this;
		}

		shouldStop = false;
		paused = false;

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
				executeOneStep = false;
			}
		}
	}

	void init() {
		// empty
	}

	abstract void step();

	@Override
	public Effect next() {
		executeOneStep = true;
		return this;
	}

	void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
