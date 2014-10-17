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


import com.jogamp.opengl.util.FPSAnimator;
import net.cpollet.ledcube.effects.Effect;
import net.cpollet.ledcube.effects.Rain;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Christophe Pollet
 */
public class Main {
	private static String TITLE = "LED-cube sim";
	private static final int CANVAS_WIDTH = 1024;
	private static final int CANVAS_HEIGHT = 768;
	private static final int FPS = 60;

	private static final int SIZE = 8;

	public static void main(String[] args) throws InterruptedException {
		final CubeScene scene = new CubeScene(SIZE);

		final Effect effect = new Rain(scene)//
				.withSpeed(100)//
				.start();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GLProfile glp = GLProfile.getDefault();
				GLCapabilities caps = new GLCapabilities(glp);

				final GLCanvas canvas = new GLCanvas(caps);
				canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
				canvas.addGLEventListener(scene);
				canvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						switch (e.getKeyChar()) {
							case ' ':
								scene.toggleRotation();
								break;
							case 'g':
								scene.toggleAxis();
								break;
							case 's':
								effect.stop();
								break;
							case 'S':
								effect.start();
								break;
							case 'p':
								effect.togglePause();
								break;
							case 'n':
								effect.next();
								break;
							case '0':
								scene.resetAngles();
								break;
							case 'X':
								scene.incrementXAngleBy(1.0f);
								break;
							case 'x':
								scene.incrementXAngleBy(-1.0f);
								break;
							case 'Y':
								scene.incrementYAngleBy(1.0f);
								break;
							case 'y':
								scene.incrementYAngleBy(-1.0f);
								break;
							case 'Z':
								scene.incrementZAngleBy(1.0f);
								break;
							case 'z':
								scene.incrementZAngleBy(-1.0f);
								break;
							case 'f':
								scene.toggleFps();
								break;
							case 'w':
								scene.incrementZPosition(-1.0f);
								break;
							case 'W':
								scene.incrementZPosition(1.0f);
								break;
						}
					}
				});

				final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
				animator.start();

				Frame frame = new Frame(TITLE);
				frame.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				frame.add(canvas);
				frame.setVisible(true);
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowOpened(WindowEvent e) {
						canvas.requestFocus();
					}

					public void windowClosing(WindowEvent e) {
						new Thread() {
							@Override
							public void run() {
								if (animator.isStarted()) {
									animator.stop();
								}
								System.exit(0);
							}
						}.start();
					}
				});
				frame.pack();
			}
		});
	}
}
