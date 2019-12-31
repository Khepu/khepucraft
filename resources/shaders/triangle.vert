#version 460 core

layout (location = 0) in vec3 aPos;

uniform float time;
uniform vec3 move;
uniform mat4 rotationz;
uniform mat4 rotationy;
uniform mat4 rotationx;

out vec3 pos;
out float t;

void main() {
  vec4 paddedPos = vec4(aPos.xyz, 1.0f);
  vec4 finalPos = rotationx * rotationz * rotationy * paddedPos;
  gl_Position = finalPos;

  pos = finalPos.xyz;
  t = time;
}
