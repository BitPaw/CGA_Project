#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 Position;
    vec2 TexturePosition;
    vec3 Normal;
} vertexData;

//fragment shader output
out vec4 ResultColor;

uniform vec3 materialColor;
uniform sampler2D textureEmissive;

void main()
{
   // vec3 mixedColor = materialColor * vertexData.Normal;

    vec4 texture = texture(textureEmissive, vertexData.TexturePosition);

    //vec4 mixedColorX4 = mix(texture, vec4(mixedColor, 1.0f), 0.2f) * 5f;

    ResultColor = texture;
}