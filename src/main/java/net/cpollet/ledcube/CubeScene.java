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

package net.cpollet.ledcube;

import com.jogamp.opengl.util.awt.TextRenderer;
import net.cpollet.ledcube.geometry.Color;
import net.cpollet.ledcube.geometry.Cube;
import net.cpollet.ledcube.geometry.Position;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import java.awt.Font;
import java.util.concurrent.CountDownLatch;

import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LEQUAL;
import static javax.media.opengl.GL.GL_LINES;
import static javax.media.opengl.GL.GL_NICEST;
import static javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static javax.media.opengl.GL.GL_SRC_ALPHA;
import static javax.media.opengl.GL2.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL2.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL2.GL_MODELVIEW;
import static javax.media.opengl.GL2.GL_PERSPECTIVE_CORRECTION_HINT;
import static javax.media.opengl.GL2.GL_PROJECTION;
import static javax.media.opengl.GL2.GL_SMOOTH;


/**
 * @author Christophe Pollet
 */
public class CubeScene implements GLEventListener {
	private final static Color COLOR_ON = new Color(.6f, .7f, 1f);
	private final static Color COLOR_OFF = new Color(.3f, .3f, .3f, .2f);
	private final static float speedCube = -0.2f;

	private GLU glu;

	private boolean[][][] buffer;

	private float angleX = 0.0f;
	private float angleY = 0.0f;
	private float angleZ = 0.0f;
	private float positionZ = -20.0f;
	private float angleCube = 0.0f;
	private boolean rotationEnabled;

	private boolean axisEnabled;
	private boolean fpsEnabled;

	private int size;
	private TextRenderer textRenderer;

	private int effectFps;
	private int fps;
	private long lastTime;
	private int nbFrames;

	private CountDownLatch countDownLatch;

	public CubeScene(int size) {
		this.countDownLatch = new CountDownLatch(1);
		this.size = size;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		rotationEnabled = true;
		axisEnabled = false;
		fpsEnabled = false;

		glu = new GLU();

		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1f);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glDepthFunc(GL_LEQUAL);
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		gl.glShadeModel(GL_SMOOTH);

		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		textRenderer = new TextRenderer(new Font(Font.MONOSPACED, Font.PLAIN, 10));

		lastTime = System.currentTimeMillis();
		nbFrames = 0;
		fps = 0;
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		startFrame();

		if (buffer == null) {
			return;
		}

		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, positionZ);

		gl.glRotatef(angleCube, 1.0f, 1.0f, 1.0f);
		gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f);
		gl.glRotatef(angleZ, 0.0f, 0.0f, 1.0f);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) {
					float x = i + 0.5f - (float) size / 2;
					float y = j + 0.5f - (float) size / 2;
					float z = k + 0.5f - (float) size / 2;

					new Cube(new Position(x, y, z), 0.1f, buffer[i][j][k] ? COLOR_ON : COLOR_OFF).draw(gl);
				}
			}
		}

		if (axisEnabled) {
			gl.glLineWidth(1.0f);
			drawAxes(gl);
		}

		if (rotationEnabled) {
			angleCube += speedCube;
		}

		updateFPS();
		displayFPS(drawable);
	}

	private void startFrame() {
		countDownLatch.countDown();
	}

	private void updateFPS() {
		long currentTime = System.currentTimeMillis();

		if (currentTime - lastTime < 1000) {
			nbFrames++;
			return;
		}

		fps = nbFrames;
		nbFrames = 0;
		lastTime = System.currentTimeMillis();
	}

	private void displayFPS(GLAutoDrawable drawable) {
		if (fpsEnabled) {
			if (fps > 0) {
				textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
				textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				textRenderer.draw("scene  " + String.valueOf(1000 / fps) + "ms (" + fps + "fps)", 10, 10);
				textRenderer.endRendering();
			}
			if (effectFps > 0) {
				textRenderer.beginRendering(drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
				textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
				textRenderer.draw("effect " + String.valueOf(1000 / effectFps) + "ms (" + effectFps + "fps)", 10, 20);
				textRenderer.endRendering();
			}
		}

	}

	private void drawAxes(GL2 gl) {
		gl.glBegin(GL_LINES);
		gl.glColor3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(1.0f, 0.0f, 0.0f);

		gl.glColor3f(0.0f, 1.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 1.0f, 0.0f);

		gl.glColor3f(0.0f, 0.0f, 1.0f);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(0.0f, 0.0f, 1.0f);
		gl.glEnd();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();

		if (height == 0) {
			height = 1;
		}
		float aspect = (float) width / height;

		gl.glViewport(0, 0, width, height);

		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(40.0, aspect, 0.1, 100.0);

		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void setBuffer(boolean[][][] buffer) {
		waitNextFrame();
		this.buffer = buffer;
	}

	private void waitNextFrame() {
		try {
			countDownLatch.await();
			countDownLatch = new CountDownLatch(1);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public int getSize() {
		return size;
	}

	public void toggleAxis() {
		axisEnabled = !axisEnabled;
	}

	public void toggleRotation() {
		rotationEnabled = !rotationEnabled;
	}

	public void incrementXAngleBy(float angle) {
		angleX += angle;
	}

	public void incrementYAngleBy(float angle) {
		angleY += angle;
	}

	public void incrementZAngleBy(float angle) {
		angleZ += angle;
	}

	public void resetAngles() {
		angleX = 0f;
		angleY = 0f;
		angleZ = 0f;
		angleCube = 0f;
	}

	public void toggleFps() {
		fpsEnabled = !fpsEnabled;
	}

	public void incrementZPosition(float position) {
		positionZ += position;
	}

	public void setEffectFps(int effectFps) {
		this.effectFps = effectFps;
	}
}
