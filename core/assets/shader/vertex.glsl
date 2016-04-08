attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;

uniform mat4 u_worldTrans;
uniform mat4 u_projTrans;
uniform mat4 u_viewTrans;

varying vec3 pos_eye;
varying vec3 nor_eye;

void main() {
	pos_eye = vec3 (u_viewTrans * u_worldTrans * vec4 (a_position, 1.0));
	nor_eye = vec3 (u_viewTrans * u_worldTrans * vec4 (a_normal, 0.0));
    gl_Position = u_projTrans * u_worldTrans * vec4(a_position, 1.0);
}