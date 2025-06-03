#version 460

layout (location=0) in vec3 inPosition;
layout (location=1) in vec3 inNormal;
layout (location=2) in vec2 inUV;
layout (location=3) in ivec4 inBoneIDs;
layout (location=4) in vec4 inBoneWeights;

const int MAX_BONE_WEIGHT_COUNT = 4;
const int MAX_BONE_COUNT = 150;

out vec3 outPosition;
out vec3 outNormal;
out vec2 outUV;

uniform mat4 uProjection;
uniform mat4 uCamera;
uniform mat4 uObject;
uniform mat4 uBoneMatrices[MAX_BONE_COUNT];

void main() {
	vec4 fixedPosition = vec4(0, 0, 0, 0);
    vec4 fixedNormal = vec4(0, 0, 0, 0);
    int boneCount = 0;
    
    for( int i = 0; i < MAX_BONE_WEIGHT_COUNT; i++ ) {
        float weight = inBoneWeights[i];
        
        if( weight > 0 ) {
            int boneID = inBoneIDs[i];
            mat4 boneMatrix = uBoneMatrices[boneID];
            fixedPosition += weight * (boneMatrix * vec4(inPosition, 1.0));
            fixedNormal += weight * (boneMatrix * vec4(inNormal, 0.0));
            boneCount++;
        }
    }
    
    if( boneCount == 0 ) {
        fixedPosition = vec4(inPosition, 1.0);
        fixedNormal = vec4(inNormal, 0.0);
    }
    
    mat4 modelViewMatrix = uCamera * uObject;
    vec4 mvPosition = modelViewMatrix * fixedPosition;
    gl_Position = uProjection * mvPosition;
    outPosition = mvPosition.xyz;
    outNormal = normalize(modelViewMatrix * fixedNormal).xyz;
    outUV = inUV;
}
