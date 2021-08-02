#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 Position;
    vec3 TexturePosition;
} vertexData;

//fragment shader output
out vec4 ResultColor;

uniform samplerCube textureEmissive;

void main()
{
    vec4 texture = texture(textureEmissive, vertexData.TexturePosition);

    ResultColor = texture;
}