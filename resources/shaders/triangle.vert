#version 330 core
in  vec3 vertex;
in  vec3 in_Color;
out vec3 ex_Color;

uniform mat4 gl_ModelViewMatrix;
uniform mat4 gl_ProjectionMatrix;

void main(void) {
	gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * vec4(vertex, 1.0);
    //gl_Position = vec4(vertex, 1.0);
	ex_Color = in_Color;
}