
/*
 * Copyright 2015 Joseph W Becher <jwbecher@drazisil.com>
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

package com.drazisil.opengl3demo;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GLContext;

import java.io.File;
import java.nio.FloatBuffer;

import static java.lang.System.exit;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static utility.ShaderLoader.loadShaderPair;

public class OpenGL3Demo {

    private static VBO vbo;
    // GLFW error callback
    private GLFWErrorCallback error_callback;
    // GLFW key callback
    private GLFWKeyCallback callback;
    // window context
    private long window;
    private int width = 640;
    private int height = 480;

    private FloatBuffer vertexData;
    private int shader;

    public void run(){

        // Set error callback
        glfwSetErrorCallback(error_callback = new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                System.err.println(description);
            }
        });

        // Init GLFW
        if (glfwInit() == 0){
            exit(-1);

        }

        // create the window
        window = glfwCreateWindow(640, 480, "My Title", 0, 0);

        // Check if window was created
        if (window == 0)
        {
            glfwTerminate();
            exit(-1);
        }

        glfwSetKeyCallback(window, callback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
                    glfwSetWindowShouldClose(window, 1);
            }
        });

        // make context current
        glfwMakeContextCurrent(window);

        // run the main loop
        loop();

        // destroy window
        glfwDestroyWindow(window);

        // terminate GLFW
        glfwTerminate();
    }

    private void loop() {
        // It is required to have an active OpenGL context
        GLContext.createFromCurrent();

        // set the viewport
        glViewport(0, 0, width, height);

        // set the timer
        double time = glfwGetTime();

        while (glfwWindowShouldClose(window) == 0)
        {
            renderGL();

            // wait for key events
            glfwWaitEvents();

            // swap the buffers
            glfwSwapBuffers(window);
        }
    }

    private void renderGL() {

        vbo.init();
        InitializeProgram();
        // tell OpenGL what shader id we are using
        glUseProgram(shader);

        // clear the screen
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        vbo.render();

        // disable the attribute array
        glDisableVertexAttribArray(0);

    }


    private void InitializeProgram() {
        // TODO: Make own shader loader
        shader = loadShaderPair("resources/shaders/triangle.vert", "resources/shaders/triangle.frag");


    }

    public static void main(String[] argv) {
        System.setProperty("org.lwjgl.librarypath", new File("natives/windows/x86").getAbsolutePath());
        vbo = new VBO();
        OpenGL3Demo openGL3Demo = new OpenGL3Demo();
        openGL3Demo.run();
    }


}