#version 460 core

layout (location = 0) in vec3 aPos;

uniform float time;
uniform vec3 move;

out vec3 pos;

void main() {
  gl_Position = vec4(aPos.x + move.x,
                     aPos.y + move.y,
                     aPos.z + move.z,
                     1.0);
  pos = aPos.xyz;
}
