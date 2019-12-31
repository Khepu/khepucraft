#version 460 core

in vec3 pos;

uniform vec3 color;

out vec4 FragColor;

void main() {
  FragColor = vec4(color.rgb, 1.0f);
}
