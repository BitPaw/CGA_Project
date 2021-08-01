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

void main()
{
    float red =  abs(vertexData.Normal.x);
    float green =  abs(vertexData.Normal.y);
    float blue = abs(vertexData.Normal.z);

    vec3 mixedColor = materialColor * vec3(red, green, blue);

    ResultColor = vec4(mixedColor, 1.0f);
}