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
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class DisplayUtils {

    /**
     * Get a list of all possible resolutions
     * from http://forum.lwjgl.org/index.php?topic=5572.msg29504#msg29504
     */
    public static void getDisplayModes() {
        PointerBuffer monitorBuf = GLFW.glfwGetMonitors();
        for (int x = 0; x < monitorBuf.limit(); x++) {
            System.out.println("Monitor " + x);

            IntBuffer buf = BufferUtils.createIntBuffer(1);
            buf.put(10);
            buf.flip();
            ByteBuffer modes = GLFW.glfwGetVideoModes(monitorBuf.get(x), buf);

            for (int i = 0; i < modes.limit(); ) {
                ByteBuffer vidMode = BufferUtils.createByteBuffer(GLFWvidmode.SIZEOF);
                byte[] vidModeData = new byte[GLFWvidmode.SIZEOF];
                modes.get(vidModeData);
                vidMode.put(vidModeData);
                vidMode.flip();
                int width = GLFWvidmode.width(vidMode);
                int height = GLFWvidmode.height(vidMode);
                System.out.println(width + "," + height);
                i = i + GLFWvidmode.SIZEOF;
            }
        }
    }
}
