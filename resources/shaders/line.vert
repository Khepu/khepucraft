#version 460
layout (location = 0) in vec3 aPos;

out vec3 pos;

void main(){
  gl_Position = vec4(aPos.xyz, 1.0f);
  pos = aPos;
}
