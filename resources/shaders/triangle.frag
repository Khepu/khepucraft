#version 460 core
in vec3 pos;
in float t;

out vec4 FragColor;

void main() {
  //FragColor = vec4(1.0, 0.5f, 0.2f, 1.0f);
  FragColor = vec4(vec3(pos.rgb), 1.0f);
}

