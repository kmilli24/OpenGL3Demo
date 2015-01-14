
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

import java.io.File;

public class OpenGL3Demo {

    public void run(){
    }

    public static void main(String[] argv) {
        System.setProperty("org.lwjgl.librarypath", new File("natives/windows/x86").getAbsolutePath());
        OpenGL3Demo openGL3Demo = new OpenGL3Demo();
        openGL3Demo.run();
    }


}