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
import net.cpollet.ledcube.effects.Rain;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Christophe Pollet
 */
public class Main {
	// Define constants for the top-level container
	private static String TITLE = "LED-cube sim";  // window's title
	private static final int CANVAS_WIDTH = 800;  // width of the drawable
	private static final int CANVAS_HEIGHT = 600; // height of the drawable
	private static final int FPS = 60; // animator's target frames per second

	public static void main(String[] args) {
		final CubeScene scene = new CubeScene();

		new Thread(new Rain(scene)).start();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GLProfile glp = GLProfile.getDefault();
				GLCapabilities caps = new GLCapabilities(glp);

				GLCanvas canvas = new GLCanvas(caps);
				final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
				animator.start();

				canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
				canvas.addGLEventListener(scene);
				canvas.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						scene.toggleRotation();
					}
				});
				canvas.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyChar() == ' ') {
							scene.toggleRotation();
						}
					}
				});

				Frame frame = new Frame(TITLE);
				frame.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				frame.add(canvas);
				frame.setVisible(true);
				frame.addWindowListener(new WindowAdapter() {
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
