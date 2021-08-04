#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec3 normal;
    vec2 texturePosition;
} vertexData;

struct Material
{
    vec3 Ambient;
    vec3 Diffuse;
    vec3 Specular;
};

//---<Fragment shader output>------
out vec4 color;
//---------------------------------

//---<Material>---------------------------
uniform sampler2D diffuseTexture;
uniform sampler2D emissiveTexture;
uniform sampler2D specularTexture;
uniform vec3 materialColor;
//-----------------------------------------

//---<Camera>-----------------------------
uniform vec3 cameraPosition;
uniform mat4 view_matrix;
//-----------------------------------------

//---<Point light>-------------------------
uniform vec3 pointLightPosition;
uniform vec3 pointLightColor;
uniform vec3 lightDecayPointLight;
//-----------------------------------------

//---<SpotLight>-----------------------------
uniform float shiny;
uniform float innerAngle;
uniform float outerAngle;
uniform vec3 spotLightPosition;
uniform vec3 spotLightColor;
uniform vec3 lightDecaySpotLight;
uniform vec3 coneDirection;
//-----------------------------------------

void main()
{
    float ambientStrength = 0.4;
    float specularStrength = 0.2;

    //---<Create>-----------
    vec3 normaledNormal = normalize(vertexData.normal);
    vec3 lightDirection = normalize(pointLightPosition - vertexData.position);
    vec3 cameraDirection = normalize(cameraPosition - vertexData.position);
    vec3 reflectionDirection = reflect(-lightDirection, normaledNormal);
    vec3 toCameraDirection = (inverse(view_matrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - vertexData.position.xyz;
    vec3 spotLightDirection = normalize(spotLightPosition - vertexData.position);


    //vec3 ambient = ambientStrength * pointLightColor;
    //vec3 diffuse = max(dot(normaledNormal, lightDirection), 0.0) * pointLightColor;
    //vec3 specular = specularStrength * pow(max(dot(cameraDirection, reflectionDirection), 0.0), 256) * pointLightColor;

    //vec4 materialColorResult = vec4(ambient + diffuse + specular, 1.0f);
    //vec4 result = texture(emissiveTexture, vertexData.texturePosition) + materialColorResult;// * objectColor;


    //-------------------------------------------------------------------------
    float distancePointLight = length(lightDirection);
    float distanceSpotLight = length(spotLightDirection);
    float attenuationFactor = lightDecayPointLight.x +
                            (lightDecayPointLight.y * distancePointLight) +
                            (lightDecayPointLight.z * distancePointLight * distancePointLight);
    //-------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    float nDotl = dot(normaledNormal, lightDirection);
    float brightness = max(nDotl, 0.0);
    float specularFactor = dot(reflectionDirection, cameraDirection);
    specularFactor = max(specularFactor, 0.0);
    float specularWithShiny = pow(specularFactor, shiny);
    float reflectivity = 0.1;
    vec3 finalSpecular = (specularWithShiny * reflectivity * pointLightColor) / attenuationFactor;
    vec3 diffuseLight = (brightness * pointLightColor) / attenuationFactor;
    //-------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    // SpotLight
    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);
    vec3 unitSpotLight = normalize(spotLightDirection);
    float normalDotSpotLight = dot(normaledNormal, spotLightPosition);
    float brightnessSpotLight = max(normalDotSpotLight, 0.0);
    vec3 spotLightDirectionDOT = -unitSpotLight;
    vec3 reflectedSpotLightDirection = reflect(spotLightDirectionDOT, normaledNormal);
    float specularFactorSpotLight = max(dot(reflectedSpotLightDirection, cameraDirection), 0.0);
    float cutOff = cos(innerAngle);
    float outerCutOff = cos(outerAngle);
    float theta = dot(normalize(spotLightDirection), normalize(-coneDirection));
    float epsilon = cutOff - outerCutOff;
    float intensity = clamp((theta - outerCutOff) / epsilon, 0.0, 1.0);
    float attenuationFactorSpotLight = lightDecaySpotLight.x + (lightDecaySpotLight.y * distanceSpotLight) + (lightDecaySpotLight.z * distanceSpotLight * distanceSpotLight);
    //-------------------------------------------------------------------------

    //---<???>-----------------------------------------------------------------
    float specularWithShinyAndSpotLight = pow(specularFactorSpotLight, shiny);
    vec3 finalSpecularSpotLight = (specularWithShinyAndSpotLight * reflectivity * spotLightColor) / attenuationFactorSpotLight;
    vec3 diffuseLightSpotLight = (brightnessSpotLight * spotLightColor) / attenuationFactorSpotLight;
    totalDiffuse = diffuseLight + (diffuseLightSpotLight * intensity);
    totalSpecular = finalSpecular + (finalSpecularSpotLight * intensity);
    totalDiffuse = max(totalDiffuse, 0.0);
    totalSpecular = max(totalSpecular, 0.0);
    //-------------------------------------------------------------------------

    //---<Add Texture>--
    vec3 emmissionTextureColor = texture(emissiveTexture, vertexData.texturePosition).xyz;
    vec3 diffuseTextureColor = texture(diffuseTexture, vertexData.texturePosition).xyz;
    vec3 specularTextureColor = texture(specularTexture, vertexData.texturePosition).xyz;

    color = vec4(emmissionTextureColor * materialColor, 1.0);
    color += vec4(diffuseTextureColor * totalDiffuse, 1.0);
    color += vec4(specularTextureColor * totalSpecular, 1.0);
    //-------------------------------------------------------------------------

    //color += result;
}