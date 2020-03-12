#version 460 core

layout (location = 0) in vec3 aPos;

uniform float time;
uniform vec3 move;
uniform mat4 rotx;
uniform mat4 roty;
uniform mat4 rotz;

out vec3 pos;
out float t;

void main() {
  mat4 perspective;

  float fov = 3.14;
  float n = 0;
  float f = 1;
  float d = 1 / tan(fov / 2);
  float a = 1;

  perspective[0] = vec4(d/a, 0, 0,             0);
  perspective[1] = vec4(0,   d, 0,             0);
  perspective[2] = vec4(0,   0, (n+f) / (n-f), 2*n*f / (n - f));
  perspective[3] = vec4(0,   0, -1,            0);

  vec4 paddedPos = vec4(aPos.xyz, 1.0f);
  vec4 finalPos = rotx * roty * rotz * paddedPos;
  gl_Position = finalPos;

  pos = finalPos.xyz;
  t = time;
}
