#version 460

in vec2 outTextCoord;

out vec4 fragColor;

struct Material
{
    vec4 diffuse;
};

uniform sampler2D uDiffuseSampler;
uniform Material uMaterial;

void main()
{
    fragColor = texture(uDiffuseSampler, outTextCoord) + uMaterial.diffuse;
}
