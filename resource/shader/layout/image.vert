#version 450 core

layout (location = 0) in vec2 inPos;
layout (location = 1) in vec2 inUv;

uniform mat4 projection;
uniform mat4 view;

out vec2 uv;

void main() {
    uv = inUv;
    gl_Position = projection * view * vec4(inPos, 0.0, 1.0);
}