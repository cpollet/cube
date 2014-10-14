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

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

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
import static javax.media.opengl.GL2.GL_QUADS;
import static javax.media.opengl.GL2.GL_SMOOTH;


/**
 * @author Christophe Pollet
 */
public class CubeScene implements GLEventListener {
	private GLU glu;  // for the GL Utility
	private float angleCube = 0;       // rotational angle in degree for cube
	private float speedCube = -0.2f;   // rotational speed for cube
	private double a = 0;
	private double speedA = 1d;
	private boolean enabled;
	private boolean[][][] buffer;

	@Override
	public void init(GLAutoDrawable drawable) {
		enabled = true;

		GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
		glu = new GLU();                         // get GL Utilities
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1f);      // set clear depth value to farthest
		gl.glEnable(GL_DEPTH_TEST); // enables depth testing
		gl.glEnable(GL_BLEND);
		gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
		gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
		gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		//		gl.glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {

	}

	private void drawCube(GL2 gl, float x, float y, float z, boolean on) {
		//		gl.glPushMatrix();

		float lx = x - 0.1f;
		float hx = x + 0.1f;
		float ly = y - 0.1f;
		float hy = y + 0.1f;
		float lz = z - 0.1f;
		float hz = z + 0.1f;

		gl.glBegin(GL_QUADS);

		// front
		//gl.glColor3f(1.0f, 0.0f, 0.0f);
		if (on) {
			gl.glColor3f(.6f, .7f, 1f);
		}
		else {
			gl.glColor4f(.2f, .2f, .2f, .5f);
		}
		gl.glVertex3f(hx, hy, hz);
		gl.glVertex3f(hx, ly, hz);
		gl.glVertex3f(lx, ly, hz);
		gl.glVertex3f(lx, hy, hz);

		// back
		//		gl.glColor3f(0.0f, 1.0f, 0.0f);
		//		gl.glColor3f(.1f, .1f, .1f);
		gl.glVertex3f(hx, hy, lz);
		gl.glVertex3f(hx, ly, lz);
		gl.glVertex3f(lx, ly, lz);
		gl.glVertex3f(lx, hy, lz);

		// left
		//		gl.glColor3f(0.0f, 0.0f, 1.0f);
		//		gl.glColor3f(.1f, .1f, .1f);
		gl.glVertex3f(lx, hy, hz);
		gl.glVertex3f(lx, ly, hz);
		gl.glVertex3f(lx, ly, lz);
		gl.glVertex3f(lx, hy, lz);

		// right
		//		gl.glColor3f(1.0f, 1.0f, 0.0f);
		//		gl.glColor3f(.1f, .1f, .1f);
		gl.glVertex3f(hx, hy, hz);
		gl.glVertex3f(hx, ly, hz);
		gl.glVertex3f(hx, ly, lz);
		gl.glVertex3f(hx, hy, lz);

		// top
		//		gl.glColor3f(1.0f, 0.0f, 1.0f);
		//		gl.glColor3f(.1f, .1f, .1f);
		gl.glVertex3f(hx, hy, lz);
		gl.glVertex3f(hx, hy, hz);
		gl.glVertex3f(lx, hy, hz);
		gl.glVertex3f(lx, hy, lz);

		// bottom
		//		gl.glColor3f(1.0f, 0.0f, 1.0f);
		//		gl.glColor3f(.1f, .1f, .1f);
		gl.glVertex3f(hx, ly, lz);
		gl.glVertex3f(hx, ly, hz);
		gl.glVertex3f(lx, ly, hz);
		gl.glVertex3f(lx, ly, lz);

		gl.glEnd();
		//		gl.glPopMatrix();
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		if (buffer == null) {
			return;
		}

		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

		gl.glLoadIdentity();
		//				gl.glTranslatef((float) Math.sin(a), (float) Math.cos(a), -10.0f); // translate right and into the screen
		gl.glTranslatef(0.0f, 0.0f, -20.0f); // translate right and into the screen
		gl.glRotatef(angleCube, 1.0f, 1.0f, 1.0f); // rotate about the x, y and z-axes

		int min = 0;
		int max = 8;

		for (int i = min; i < max; i++) {
			for (int j = min; j < max; j++) {
				for (int k = min; k < max; k++) {
					float x = i + 0.5f - (float) max / 2;
					float y = j + 0.5f - (float) max / 2;
					float z = k + 0.5f - (float) max / 2;
					//					if (buffer[i][j][k]) {
					drawCube(gl, x, y, z, buffer[i][j][k]);
					//					}
				}
			}
		}

		gl.glLineWidth(1.0f);
		drawAxes(gl);

		if (enabled) {
			angleCube += speedCube;
			a += speedA;
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
		glu.gluPerspective(45.0, aspect, 0.1, 100.0);

		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void toggleRotation() {
		enabled = !enabled;
	}

	public void setBuffer(boolean[][][] buffer) {
		this.buffer = buffer;
	}
}
