#version 460


layout (location=0) in vec3 inPosition;
layout (location=1) in vec3 inNormal;
layout (location=2) in vec2 inUV;


out vec3 outPosition;
out vec3 outNormal;
out vec2 outUV;

uniform mat4 uProjection;
uniform mat4 uCamera;
uniform mat4 uObject;

void main()
{
    mat4 modelViewMatrix = uCamera * uObject;
    vec4 mvPosition =  modelViewMatrix * vec4(inPosition, 1.0);
    gl_Position = uProjection * mvPosition;
    outPosition = mvPosition.xyz;
    outNormal = normalize(modelViewMatrix * vec4(inNormal, 0.0)).xyz;
    outUV = inUV;
}
