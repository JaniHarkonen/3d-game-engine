#version 460

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;


out vec2 outTextCoord;

uniform mat4 uProjection;
uniform mat4 uCamera;
uniform mat4 uModel;

void main()
{
    gl_Position = uProjection * uCamera * uModel * vec4(position, 1.0);
    outTextCoord = texCoord;
}
