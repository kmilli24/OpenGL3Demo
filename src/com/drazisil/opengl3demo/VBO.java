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

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class VBO {

    // rotational offset
    private float xOffset;
    private float yOffset;

    public void init(){
        ComputePositionOffsets();
        /**
         * This is a combined float[]
         * The first half contains the position
         * The second half contains the colors
         */
        float[] vertexDataFloat = {
                0.0f,    0.5f, 0.0f, 1.0f,
                0.5f, -0.366f, 0.0f, 1.0f,
                -0.5f, -0.366f, 0.0f, 1.0f,
                1.0f,    0.0f, 0.0f, 1.0f,
                0.0f,    1.0f, 0.0f, 1.0f,
                0.0f,    0.0f, 1.0f, 1.0f,
        };

        float[] vertexColorsFloat = {
                1.0f,    0.0f, 0.0f, 1.0f,
                0.0f,    1.0f, 0.0f, 1.0f,
                0.0f,    0.0f, 1.0f, 1.0f,
        };


        // 3 vertices for the triangle
        int amountOfVertices = 3;
        int vertexSize = vertexDataFloat.length;

        /**
         * These next three lines are tricky.
         * They are unique to LWJGL and are referenced in only a few places
         * They are explained in https://github.com/LWJGL/lwjgl3-wiki/wiki/2.6.5-Basics-of-modern-OpenGL-%28Part-II%29#rendering-with-buffers
         */
        FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexSize + amountOfVertices);
        vertexData.put(vertexDataFloat);
        vertexData.flip();

        // prep the colors
        FloatBuffer vertexColors = BufferUtils.createFloatBuffer(vertexSize + amountOfVertices);
        vertexColors.put(vertexColorsFloat);
        vertexColors.flip();

        int positionBufferObject = glGenBuffers();

        int colorBufferObject = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STREAM_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, colorBufferObject);
        glBufferData(GL_ARRAY_BUFFER, vertexColors, GL_STREAM_DRAW);


        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindBuffer(GL_ARRAY_BUFFER, positionBufferObject);
        glEnableVertexAttribArray(0);
        //glEnableVertexAttribArray(1);
        glVertexAttribPointer(0, 4, GL_FLOAT, false, 0, 0);
        //glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 48);
        glBindBuffer(GL_ARRAY_BUFFER, colorBufferObject);
        glEnableVertexAttribArray(1);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

    }

    private void ComputePositionOffsets(){
        float loopDuration = 5.0f;
        float scale = 3.14159f * 2.0f / loopDuration;

        float elapsedTime = (float) (glfwGetTime() / 1000.0f);

        float fCurrTimeThroughLoop = elapsedTime % loopDuration;

        xOffset = (float) (Math.cos(fCurrTimeThroughLoop * scale) * 0.5f);
        yOffset = (float) (Math.cos(fCurrTimeThroughLoop * scale) * 0.5f);
    }



    public void render() {
        // actually draw the triangle
        glDrawArrays(GL_TRIANGLES, 0, 3);


    }


}
