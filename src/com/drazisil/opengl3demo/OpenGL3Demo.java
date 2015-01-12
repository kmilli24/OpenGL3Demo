
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
import org.lwjgl.Sys;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import silvertiger.tutorial.lwjgl.Timer;
import utility.ShaderLoader;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.NULL;

public class OpenGL3Demo {

    private static Timer timer;

    //protected static final int TARGET_FPS = 60;
    protected static final int TARGET_UPS = 60;

    // We need to strongly reference callback instances.
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback keyCallback;

    // The window handle
    protected long window;
    // window width
    private int WIDTH;

    // widow height
    private int HEIGHT;
    private String TITLE;

    float delta;
    float accumulator = 0f;
    float interval = 1f / TARGET_UPS;
    float alpha;

    // this will identify our shaders
    private int shaderID;

    // This will identify our vertex buffer
    int vertexbuffer;

    // This will identify our color buffer
    int vertexbuffercolor;
    private int vertexArreyID;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    private float x;
    private float y;


    public void run(){
        System.out.println("Hello LWJGL " + Sys.getVersion() + "!");

        try {
            init();
            loop();

            // Release window and window callbacks
            glfwDestroyWindow(window);
            keyCallback.release();
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            errorCallback.release();
        }
    }

    private void loop() {
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the ContextCapabilities instance and makes the OpenGL
        // bindings available for use.
        GLContext.createFromCurrent();

        initObjects();

        // load the shaders
        shaderID = ShaderLoader.loadShaderPair("resources/shaders/triangle.vs", "resources/shaders/triangle.fs");


        // Set the clear color (RGBA)
        GL11.glClearColor(0f, 0f, 1f, 0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while ( glfwWindowShouldClose(window) == GL11.GL_FALSE ) {

            /* Get delta time and update the accumulator */
            delta = timer.getDelta();
            accumulator += delta;

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            //glfwPollEvents();
            glfwWaitEvents();

            /* Update game and timer UPS if enough time has passed */
            while (accumulator >= interval) {
                update();
                timer.updateUPS();
                accumulator -= interval;
            }

            /* Calculate alpha value for interpolation */
            alpha = accumulator / interval;

            /* Render game and update timer FPS */
            render();
            timer.updateFPS();

            /* Update timer */
            timer.update();
            glfwSetWindowTitle(window, "Game | FPS: " + timer.getFPS()
                    + ", UPS: " + timer.getUPS());

        }
    }

    private void initObjects() {
        // Create and bind the vertex id
        vertexArreyID = glGenVertexArrays();
        glBindVertexArray(vertexArreyID);

        // An array of 3 vectors which represents 3 vertices
        float[] vertexTriangle = {
                x - 0.6f, y -0.4f, 0.f,
                x + 0.6f, y -0.4f, 0.f,
                x + 0.f, y + 0.6f, 0.f,
        };

        // Generate 1 buffer, put the resulting identifier in vertexbuffer
        vertexbuffer = GL15.glGenBuffers();

        // The following commands will talk about our 'vertexbuffer' buffer
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexbuffer);

        // Give our vertices to OpenGL.
        int amountOfVertices = 3;
        int vertexSize = vertexTriangle.length;
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(amountOfVertices * vertexSize);
        vertexData.put(vertexTriangle);
        vertexData.flip();

        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexData, GL15.GL_STATIC_DRAW);

        //Now, the colors

        // One color for each vertex. They were generated randomly.
        float[] g_color_buffer_data = {
                1f,  0f,  0f,
                0f,  1f,  0f,
                0f,  0f,  1f
        };

        // Generate 1 buffer, put the resulting identifier in vertexbuffercolor
        vertexbuffercolor = GL15.glGenBuffers();

        // The following commands will talk about our 'vertexbuffercolor' buffer
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexbuffercolor);

        // Give our vertices to OpenGL.
        FloatBuffer vertexDataColor = BufferUtils.createFloatBuffer(amountOfVertices * vertexSize);
        vertexDataColor.put(g_color_buffer_data);
        vertexDataColor.flip();

        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexDataColor, GL15.GL_STATIC_DRAW);



    }

    private void render() {
        // set the ratio
        float ratio = WIDTH / (float) HEIGHT;
        float rotation = (float) glfwGetTime() * 50.f;

        // set the viewport
        GL11.glViewport(0, 0, WIDTH, HEIGHT);

        // clear the framebuffer
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-ratio, ratio, -1.f, 1.f, 1.f, -1.f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(x, y, 0f);
        GL11.glRotatef(rotation, 0.f, 0.f, 1.f);
        GL11.glTranslatef(-x, -y, 0f);

        // activate the shaders
        GL20.glUseProgram(shaderID);

        // 1rst attribute buffer : vertices
        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexbuffer);
        GL20.glVertexAttribPointer(
                0,                  // attribute 0. No particular reason for 0, but must match the layout in the shader.
                3,                  // size
                GL11.GL_FLOAT,      // type
                false,              // normalized?
                0,                  // stride
                0                   // array buffer offset
        );

        // 1rst attribute buffer : vertices
        GL20.glEnableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexbuffercolor);
        GL20.glVertexAttribPointer(
                1,                  // attribute 0. No particular reason for 0, but must match the layout in the shader.
                3,                  // size
                GL11.GL_FLOAT,      // type
                false,              // normalized?
                0,                  // stride
                0                   // array buffer offset
        );


        // Draw the triangle !
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3); // Starting from vertex 0; 3 vertices total -> 1 triangle

        GL20.glDisableVertexAttribArray(0);

        // swap the color buffers
        glfwSwapBuffers(window);


    }

    private void update() {

    }

    public float getDelta() {
        return 0;
    }

    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GL11.GL_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL11.GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL11.GL_TRUE); // the window will be resizable

        setWIDTH(1152);
        setHEIGHT(720);
        setTITLE("Hello World!");

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new KeyHandler(this, window));

        // Get the resolution of the primary monitor
        ByteBuffer videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        // Center our window
        glfwSetWindowPos(
                window,
                (GLFWvidmode.width(videoMode) - WIDTH) / 2,
                (GLFWvidmode.height(videoMode) - HEIGHT) / 2
        );

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }



    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    public void setHEIGHT(int HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public static void main(String[] argv) {
        System.setProperty("org.lwjgl.librarypath", new File("natives/windows/x86").getAbsolutePath());
        timer = new Timer();
        OpenGL3Demo openGL3Demo = new OpenGL3Demo();
        openGL3Demo.run();
    }


}