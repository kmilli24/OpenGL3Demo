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

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by joseph on 1/11/2015.
 */
public class KeyHandler extends GLFWKeyCallback {
    public long getWindow() {
        return window;
    }

    protected OpenGL3Demo game;
    protected long window;

    public KeyHandler(OpenGL3Demo game, long window) {
        this.game = game;
        this.window = window;
    }

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
        if (window == getWindow()){

            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, GL11.GL_TRUE); // We will detect this in our rendering loop

            if (key == GLFW_KEY_A && action == GLFW_RELEASE) game.setX(game.getX() - 0.35f * game.getDelta());
            if (key == GLFW_KEY_D && action == GLFW_RELEASE) game.setX(game.getX() + 0.35f * game.getDelta());
            if (key == GLFW_KEY_W && action == GLFW_RELEASE) game.setY(game.getY() - 0.35f * game.getDelta());
            if (key == GLFW_KEY_S && action == GLFW_RELEASE) game.setY(game.getY() + 0.35f * game.getDelta());

            if (key == GLFW_KEY_LEFT && action == GLFW_RELEASE) game.setRotation(game.getRotation() + 0.35f * game.getDelta());
            if (key == GLFW_KEY_RIGHT && action == GLFW_RELEASE) game.setRotation(game.getRotation() - 0.35f * game.getDelta());

            if (key == GLFW_KEY_Z && action == GLFW_RELEASE) game.setScale(game.getScale() + 0.35f * game.getDelta());
            if (key == GLFW_KEY_X && action == GLFW_RELEASE) game.setScale(game.getScale() - 0.35f * game.getDelta());

            if ((key == GLFW_KEY_0 || key == GLFW_KEY_KP_0) && action == GLFW_RELEASE) {
                game.setRotation(0f);
                game.setScale(game.getDefaultScale());
            }

        }
    }
}
