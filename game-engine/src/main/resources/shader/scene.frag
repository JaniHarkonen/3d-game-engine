#version 460

const int MAX_POINT_LIGHT_COUNT = 5;
const int MAX_SPOT_LIGHT_COUNT = 5;
const float SPECULAR_POWER = 1;

in vec3 outPosition;
in vec3 outNormal;
in vec2 outUV;

out vec4 fragColor;

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    float reflectance;
};

struct Attenuation {
    float constant;
    float linear;
    float exponent;
};

struct AmbientLight {
	vec3 color;
	float intensity;
};

struct DirectionalLight {
    vec3 color;
    vec3 direction;
    float intensity;
};

struct PointLight {
    vec3 position;
    vec3 color;
    float intensity;
    Attenuation attenuation;
};

struct SpotLight {
    PointLight pointLight;
    vec3 cone;
    float threshold;
};

uniform sampler2D uDiffuseSampler;
uniform Material uMaterial;
uniform AmbientLight uAmbientLight;
uniform DirectionalLight uDirectionalLight;
uniform PointLight uPointLights[MAX_POINT_LIGHT_COUNT];
uniform SpotLight uSpotLights[MAX_SPOT_LIGHT_COUNT];


vec4 attenuate(vec3 lightDirection, vec4 lightColor, Attenuation attenuation) {
	float distance = length(lightDirection);
    float attenuationInv = (
    	attenuation.constant + attenuation.linear * distance + attenuation.exponent * distance * distance
	);
    return lightColor / attenuationInv;
}

vec4 applyLighting(
	vec4 targetDiffuse, 
	vec4 targetSpecular,
	float targetReflectance, 
	vec3 lightColor, 
	float lightIntensity, 
	vec3 position, 
	vec3 lightDirection, 
	vec3 normal
) {
    	// Diffuse Light
    float diffuseFactor = max(dot(normal, lightDirection), 0.0);
    vec4 fragDiffuse = targetDiffuse * vec4(lightColor, 1.0) * lightIntensity * diffuseFactor;

	    // Specular Light
    vec3 reflectedLight = normalize(reflect(-lightDirection, normal));
    float specularFactor = max(dot(normalize(-position), reflectedLight), 0.0);
    specularFactor = pow(specularFactor, SPECULAR_POWER);
    vec4 fragSpecular = targetSpecular * lightIntensity * specularFactor * targetReflectance * vec4(lightColor, 1.0);

    return (fragDiffuse  + fragSpecular);
}

vec4 applyPointLight(
	vec4 targetDiffuse, 
	vec4 targetSpecular,
	float targetReflectance, 
	PointLight light, 
	vec3 position, 
	vec3 normal
) {
    vec3 lightDirection = light.position - position;
    vec4 fragDiffuseSpecular = applyLighting(
		targetDiffuse, 
		targetSpecular, 
		targetReflectance,
		light.color, 
		light.intensity, 
		position, 
		normalize(lightDirection), 
		normal
	);
    return attenuate(lightDirection, fragDiffuseSpecular, light.attenuation);
}


vec4 applySpotLight(
	vec4 targetDiffuse, 
	vec4 targetSpecular,
	float targetReflectance, 
	SpotLight light, 
	vec3 position, 
	vec3 normal
) {
    vec3 lightDirection = light.pointLight.position - position;
    float alpha = dot(-normalize(lightDirection), normalize(light.cone));
    vec4 fragDiffuseSpecular = vec4(0, 0, 0, 0);

    if( alpha > light.threshold ) {
        fragDiffuseSpecular = applyPointLight(
        	targetDiffuse, targetSpecular, targetReflectance, light.pointLight, position, normal
    	);
        fragDiffuseSpecular *= (1.0 - (1.0 - alpha) / (1.0 - light.threshold));
    }

    return fragDiffuseSpecular;
}

vec4 applyAmbientLight(AmbientLight ambientLight, vec4 target) {
    return vec4(ambientLight.color * ambientLight.intensity, 1) * target;
}

vec4 applyDirectionalLight(vec4 targetDiffuse, vec4 targetSpecular, float targetReflectance, DirectionalLight light, vec3 position, vec3 normal) {
    return applyLighting(targetDiffuse, targetSpecular, targetReflectance, light.color, light.intensity, position, normalize(light.direction), normal);
}

void main() {
		// Sample material textures
    vec4 texDiffuse = texture(uDiffuseSampler, outUV);
    
    	// Apply material colors to diffuse texture
    vec4 fragAmbient = texDiffuse + uMaterial.ambient;
    vec4 fragDiffuse = texDiffuse + uMaterial.diffuse;
    vec4 fragSpecular = texDiffuse + uMaterial.specular;
    
    	// Apply ambient light
    vec4 fragAmbientLight = applyAmbientLight(uAmbientLight, fragAmbient);

		// Apply directional light
    vec4 fragDiffuseSpecular = applyDirectionalLight(
    	fragDiffuse, fragSpecular, uMaterial.reflectance, uDirectionalLight, outPosition, outNormal
	);

		// Apply point lights
    for( int i = 0; i < MAX_POINT_LIGHT_COUNT; i++ ) {
        if( uPointLights[i].intensity > 0 ) {
            fragDiffuseSpecular += applyPointLight(
            	fragDiffuse, fragSpecular, uMaterial.reflectance, uPointLights[i], outPosition, outNormal
        	);
        }
    }

/*
    for( int i = 0; i < MAX_SPOT_LIGHT_COUNT; i++ ) {
        if( uSpotLights[i].pointLight.intensity > 0 ) {
        	fragDiffuseSpecular += calcSpotLight(
            	fragDiffuse, fragSpecular, uMaterial.reflectance, uSpotLights[i], outPosition, outNormal
        	);
        }
    }*/

    fragColor = fragAmbientLight + fragDiffuseSpecular;
}
