#version 460 core

layout (location = 0) in vec3 aPos;

uniform float time;
uniform vec3 move;

uniform mat4 rotx;
uniform mat4 roty;
uniform mat4 rotz;
uniform mat4 perspective;
uniform mat4 transform;
uniform mat4 view;

out vec3 pos;
out float t;

void main() {
  mat4 model = rotx * roty * rotz * transform;
  vec4 finalPos = vec4(aPos.xyz, 1.0f) * model * view * perspective;
  gl_Position = finalPos;

  //output to frag
  pos = aPos;
  t = time;
}

