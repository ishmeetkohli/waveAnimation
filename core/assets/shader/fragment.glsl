#ifdef GL_ES 
precision mediump float;
#endif


varying vec3 pos_eye;
varying vec3 nor_eye;
uniform samplerCube u_environmentCubemap;
uniform mat4 u_viewTrans;
uniform mat4 u_inverseViewTrans;

void main () {
  vec3 incident_eye = normalize (pos_eye);
  vec3 normal = normalize (nor_eye);

  vec3 reflected = reflect (incident_eye, normal);
  reflected = vec3 (u_inverseViewTrans * vec4 (reflected, 0.0));

  vec3 color = textureCube(u_environmentCubemap, reflected).rgb;
  gl_FragColor = vec4(color.r, color.g, color.b, 0.7);
}