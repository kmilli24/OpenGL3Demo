
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

package com.drazisil.opengl3demo;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GLContext;

import java.io.File;
import java.nio.FloatBuffer;

import static java.lang.System.exit;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static utility.ShaderLoader.loadShaderPair;

public class OpenGL3Demo {

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

        InitializeVertexBuffer();
        InitializeProgram();
        glUseProgram(shader);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT);

        glDrawArrays(GL_TRIANGLES, 0, 3);

        glDisableVertexAttribArray(0);

    }

    private void InitializeProgram() {
        shader = loadShaderPair("resources/shaders/triangle.vert", "resources/shaders/triangle.frag");


    }

    private void InitializeVertexBuffer() {
        float[] vertexDataFloat = {
                0.0f,    0.5f, 0.0f, 1.0f,
                0.5f, -0.366f, 0.0f, 1.0f,
                -0.5f, -0.366f, 0.0f, 1.0f,
                1.0f,    0.0f, 0.0f, 1.0f,
                0.0f,    1.0f, 0.0f, 1.0f,
                0.0f,    0.0f, 1.0f, 1.0f,        };
        // vertex vertexDataFloat

        int amountOfVertices = 3;
        int vertexSize = vertexDataFloat.length;

        /**
         * These next three lines are tricky.
         * They are unique to LWJGL and are referenced in only a few places
         * They are explained in https://github.com/LWJGL/lwjgl3-wiki/wiki/2.6.5-Basics-of-modern-OpenGL-%28Part-II%29#rendering-with-buffers
         */
        vertexData = BufferUtils.createFloatBuffer(vertexSize + amountOfVertices);
        vertexData.put(vertexDataFloat);
        vertexData.flip();

        int positionBufferObject = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 48);

    }

    public static void main(String[] argv) {
        System.setProperty("org.lwjgl.librarypath", new File("natives/windows/x86").getAbsolutePath());
        OpenGL3Demo openGL3Demo = new OpenGL3Demo();
        openGL3Demo.run();
    }


}