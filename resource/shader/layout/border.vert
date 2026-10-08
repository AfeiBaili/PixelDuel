#version 450 core

layout (location = 0) in vec2 inPos;
layout (location = 1) in vec4 inColor;

uniform mat4 projection;
uniform mat4 view;

out vec4 color;

void main() {
    color = inColor;
    gl_Position = projection * view * vec4(inPos, 0.0, 1.0);
}