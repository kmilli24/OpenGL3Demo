/*
 * Copyright [2015] [Joseph W Becher <jwbecher@drazisil.com>]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.drazisil.freshstart;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import java.io.File;

import static java.lang.System.exit;
import static java.lang.System.out;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created from http://www.glfw.org/docs/latest/quick.html and LWJGL 2.9
 */
public class FreshStart {

    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWWindowCloseCallback closeCallback;
    private GLFWKeyCallback   keyCallback;
    private GLFWFramebufferSizeCallback framebufferSizeCallback;

    // window context
    private long windowContext;

    // width and height
    private int windowWidth = 1152;
    private int windowHeight = 720;


    // Is the application running?
    private boolean isRunning = false;
    private float ratio;


    public static void main(String[] argv) {
        System.setProperty("org.lwjgl.librarypath", new File("natives/windows/x86").getAbsolutePath());
        FreshStart freshStart = new FreshStart();
        freshStart.run();
    }

    private void run() {
        initGLFW();
        initGL();
        loop();
        destroyGWFL();
    }

    private void initGL() {
        GLFW.glfwSetFramebufferSizeCallback(windowContext, framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
            /**
             * Will be called when the framebuffer of the specified window is resized.
             *
             * @param window the window whose framebuffer was resized
             * @param width  the new width, in pixels, of the framebuffer
             * @param height the new height, in pixels, of the framebuffer
             */
            @Override
            public void invoke(long window, int width, int height) {
                GL11.glViewport(0, 0, width, height);
            }
        });
    }

    private void loop() {
        GLContext.createFromCurrent();

        isRunning = true;

        while (isRunning)
        {
            double time = glfwGetTime();

            ratio = windowWidth / (float) windowHeight;
            GL11.glClear(GL_COLOR_BUFFER_BIT);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(-ratio, ratio, -1.f, 1.f, 1.f, -1.f);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();
            glRotatef((float) glfwGetTime() * 50.f, 0.f, 0.f, 1.f);
            glBegin(GL_TRIANGLES);
            glColor3f(1.f, 0.f, 0.f);
            glVertex3f(-0.6f, -0.4f, 0.f);
            glColor3f(0.f, 1.f, 0.f);
            glVertex3f(0.6f, -0.4f, 0.f);
            glColor3f(0.f, 0.f, 1.f);
            glVertex3f(0.f, 0.6f, 0.f);
            glEnd();

            GLFW.glfwSwapBuffers(windowContext);

            GLFW.glfwWaitEvents();
        }
    }

    private void destroyGWFL() {
        GLFW.glfwDestroyWindow(windowContext);
        GLFW.glfwTerminate();
        exit(0);
    }

    private void initGLFW() {
        /**
         * Set error callback
         */
        GLFW.glfwSetErrorCallback(errorCallback = new GLFWErrorCallback() {
            /**
             * Will be called with an error code and a human-readable description when a GLFW error occurs.
             *
             * @param error       the error code
             * @param description a pointer to a UTF-8 encoded string describing the error
             */
            @Override
            public void invoke(int error, long description) {
                out.println("error: " + error + " - " + description);
                exit(-1);
            }
        });

        // Init GWFL
        if (GLFW.glfwInit() == GL_FALSE){
            exit(-1);
        }

        // Create the GWFL window and context
        CharSequence windowTitle = "Testing";
        windowContext = GLFW.glfwCreateWindow(windowWidth, windowHeight, windowTitle, 0, 0);

        // Make context current
        GLFW.glfwMakeContextCurrent(windowContext);

        // Set the window close callback
        GLFW.glfwSetWindowCloseCallback(windowContext, closeCallback = new GLFWWindowCloseCallback() {
            /**
             * Will be called when the user attempts to close the specified window, for example by clicking the close widget in the title bar.
             *
             * @param window the window that the user attempted to close
             */
            @Override
            public void invoke(long window) {
                isRunning = false;
            }
        });

        GLFW.glfwSetKeyCallback(windowContext, keyCallback = new GLFWKeyCallback() {
            /**
             * Will be called when a key is pressed, repeated or released.
             *
             * @param window   the window that received the event
             * @param key      the keyboard key that was pressed or released
             * @param scancode the system-specific scancode of the key
             * @param action   the key action. One of:<br>{@link org.lwjgl.glfw.GLFW#GLFW_PRESS}, {@link org.lwjgl.glfw.GLFW#GLFW_RELEASE}, {@link org.lwjgl.glfw.GLFW#GLFW_REPEAT}
             * @param mods     bitfield describing which modifiers keys were held down
             */
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
                    isRunning = false;
            }
        });

    }


}
