#version 460

layout (location=0) in vec3 position;

out vec3 outColor;

uniform mat4 projectionMatrix;

void main()
{
    gl_Position = projectionMatrix * vec4(position, 1.0);
}
