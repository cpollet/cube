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

package net.cpollet.ledcube.geometry;

import javax.media.opengl.GL2;

import static javax.media.opengl.GL2GL3.GL_QUADS;

/**
 * @author Christophe Pollet
 */
public class Cube implements Shape {
	private Position position;
	private float size;
	private Color color;

	public Cube(Position position, float size, Color color) {
		this.position = position;
		this.size = size;
		this.color = color;
	}

	@Override
	public void draw(GL2 gl) {
		gl.glPushMatrix();

		float lx = position.getX() - size;
		float hx = position.getX() + size;
		float ly = position.getY() - size;
		float hy = position.getY() + size;
		float lz = position.getZ() - size;
		float hz = position.getZ() + size;

		gl.glBegin(GL_QUADS);

		gl.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

		// front
		gl.glVertex3f(hx, hy, hz);
		gl.glVertex3f(hx, ly, hz);
		gl.glVertex3f(lx, ly, hz);
		gl.glVertex3f(lx, hy, hz);

		// back
		gl.glVertex3f(hx, hy, lz);
		gl.glVertex3f(hx, ly, lz);
		gl.glVertex3f(lx, ly, lz);
		gl.glVertex3f(lx, hy, lz);

		// left
		gl.glVertex3f(lx, hy, hz);
		gl.glVertex3f(lx, ly, hz);
		gl.glVertex3f(lx, ly, lz);
		gl.glVertex3f(lx, hy, lz);

		// right
		gl.glVertex3f(hx, hy, hz);
		gl.glVertex3f(hx, ly, hz);
		gl.glVertex3f(hx, ly, lz);
		gl.glVertex3f(hx, hy, lz);

		// top
		gl.glVertex3f(hx, hy, lz);
		gl.glVertex3f(hx, hy, hz);
		gl.glVertex3f(lx, hy, hz);
		gl.glVertex3f(lx, hy, lz);

		// bottom
		gl.glVertex3f(hx, ly, lz);
		gl.glVertex3f(hx, ly, hz);
		gl.glVertex3f(lx, ly, hz);
		gl.glVertex3f(lx, ly, lz);

		gl.glEnd();

		gl.glPopMatrix();
	}
}
