#version 460

const int MAX_POINT_LIGHT_COUNT = 5;
const int MAX_SPOT_LIGHT_COUNT = 5;
const int CASCADE_COUNT = 3;
const float SPECULAR_POWER = 1;
const float SHADOW_BIAS = 0.001;
const float SHADOW_INTENSITY = 0.75;

in vec3 outPosition;
in vec3 outNormal;
in vec2 outUV;
in vec3 outViewPosition;
in vec4 outWorldPosition;

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
    vec3 position;
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

struct CascadeShadow {
    mat4 lightView;
    float splitDistance;
};

uniform sampler2D uDiffuseSampler;
uniform Material uMaterial;
uniform AmbientLight uAmbientLight;
uniform DirectionalLight uDirectionalLight;
uniform PointLight uPointLights[MAX_POINT_LIGHT_COUNT];
uniform SpotLight uSpotLights[MAX_SPOT_LIGHT_COUNT];
uniform sampler2D uShadowMaps[CASCADE_COUNT];
uniform CascadeShadow uCascadeShadows[CASCADE_COUNT];


	/**
	* Attenuates the color of a light given its distance from the view point and attenuation profile.
	* vec3 lightDirection		: vector representing the direction and distance of the light from the view point
	* vec4 lightColor			: RGBA color of the light
	* Attenuation attenuation	: attenuation profile of the light
	*
	* returns vec4: the attenuated light color
	*/
vec4 attenuate(vec3 lightDirection, vec4 lightColor, Attenuation attenuation) {
	float distance = length(lightDirection);
    float attenuationInv = (
    	attenuation.constant + attenuation.linear * distance + attenuation.exponent * distance * distance
	);
    return lightColor / attenuationInv;
}

	/**
	* Applies light to a fragment given its diffuse and specular by reflecting the light off of the surface.
	* vec4 targetDiffuse		: target fragment with diffuse color already applied to it
	* vec4 targetSpecular		: target fragment with specular color already applied to it
	* float targetReflectance	: value between 0-1 representing the factor by which the reflected light will be multiplied
	*							  (i.e. how well the surface reflects light)
	* vec3 lightColor			: RGB color of the light
	* float lightIntensity		: factor applied to the light color and specular fragment to simulate brightness of the light
	* vec3 position				: position of the fragment (typically handed from the vertex shader)
	* vec3 lightDirection		: vector representing the direction of the light from the view point
	* vec3 normal				: normal of the fragment's surface (typically handed from the vertex shader)
	* Attenuation attenuation	: attenuation profile of the light
	*
	* return vec4: fragment with light applied to its diffuse and specular forms
	*/
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

	/**
	* Applies a point light effect to a fragment using applyLighting() (see applyLighting) attenuated by the attenuation
	* profile of the light.
	* vec4 targetDiffuse		: target fragment with diffuse color already applied to it
	* vec4 targetSpecular		: target fragment with specular color already applied to it
	* float targetReflectance	: value between 0-1 representing the factor by which the reflected light will be multiplied
	*							  (i.e. how well the surface reflects light)
	* PointLight light			: object representing the properties of the point light including attenuation
	* float lightIntensity		: factor applied to the light color and specular fragment to simulate brightness of the light
	* vec3 position				: position of the fragment (typically handed from the vertex shader)
	* vec3 normal				: normal of the fragment's surface (typically handed from the vertex shader)
	*
	* return vec4: fragment with a point light effect applied to it, unless the effect has been completely attenuated
	*/
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

	/**
	* Applies ambient light to a fragment given the ambient light's profile.
	* AmbientLight ambientLight	: profile of the ambient light that will be applied to the fragment
	* vec4 target				: target fragment to apply the light to
	*
	* return vec4: fragment with ambient light applied to it
	*/
vec4 applyAmbientLight(AmbientLight ambientLight, vec4 target) {
    return vec4(ambientLight.color * ambientLight.intensity, 1) * target;
}

	/**
	* Applies a point light effect to a fragment using applyLighting() (see applyLighting).
	* vec4 targetDiffuse		: target fragment with diffuse color already applied to it
	* vec4 targetSpecular		: target fragment with specular color already applied to it
	* float targetReflectance	: value between 0-1 representing the factor by which the reflected light will be multiplied
	*							  (i.e. how well the surface reflects light)
	* DirectionalLight light	: object representing the properties of the directional light
	* vec3 position				: position of the fragment (typically handed from the vertex shader)
	* vec3 normal				: normal of the fragment's surface (typically handed from the vertex shader)
	*
	* return vec4: fragment with directional light applied to its diffuse and specular forms
	*/
vec4 applyDirectionalLight(
	vec4 targetDiffuse, 
	vec4 targetSpecular, 
	float targetReflectance, 
	DirectionalLight light, 
	vec3 position, 
	vec3 normal
) {
    return applyLighting(
    	targetDiffuse, 
    	targetSpecular, 
    	targetReflectance, 
    	light.color, 
    	light.intensity, 
    	position, 
    	normalize(light.position), 
    	normal
	);
}

	/**
	* Calculates the factor of a cascade shadow given the position of the fragment in world-space and the index of the
	* cascade. The factor of the shadow will be used to multiply the color of a fragment to make it darker.
	* vec4 worldPosition: position of the fragment that the shadow will be applied to in world-space
	* vec4 cascadeIndex	: index of the shadow cascade whose intensity is being calculated
	*
	* return float: the shadow factor used to darken the fragment
	*/
float calculateShadowFactor(vec4 worldPosition, int cascadeIndex) {
    vec4 shadowMapPosition = uCascadeShadows[cascadeIndex].lightView * worldPosition;
    vec4 shadowCoordinate = (shadowMapPosition / shadowMapPosition.w) * 0.5 + 0.5;
    float shadow = 1.0;
    vec2 offset = vec2(0, 0);

    if( shadowCoordinate.z > -1.0 && shadowCoordinate.z < 1.0 ) {
        float distance = texture(uShadowMaps[cascadeIndex], vec2(shadowCoordinate.xy + offset)).r;
        
        if( shadowCoordinate.w > 0 && distance < shadowCoordinate.z - SHADOW_BIAS ) {
            shadow = SHADOW_INTENSITY;
        }
    }
    
    return shadow;
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
    
    int cascadeIndex = 0;
    for( int i = 0; i < CASCADE_COUNT - 1; i++ ) {
        if( outViewPosition.z < uCascadeShadows[i].splitDistance ) {
            cascadeIndex = i + 1;
        }
    }

    float shadowFactor = calculateShadowFactor(outWorldPosition, cascadeIndex);

		// Apply directional light
    vec4 fragDiffuseSpecular = applyDirectionalLight(
    	fragDiffuse, fragSpecular, uMaterial.reflectance, uDirectionalLight, outViewPosition, outNormal
	);

		// Apply point lights
    for( int i = 0; i < MAX_POINT_LIGHT_COUNT; i++ ) {
        if( uPointLights[i].intensity > 0 ) {
            fragDiffuseSpecular += applyPointLight(
            	fragDiffuse, fragSpecular, uMaterial.reflectance, uPointLights[i], outViewPosition, outNormal
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
    fragColor.rgb *= shadowFactor;
}
