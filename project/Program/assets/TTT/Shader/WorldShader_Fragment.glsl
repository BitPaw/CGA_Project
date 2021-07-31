#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec3 normal;
} vertexData;

//fragment shader output
out vec4 ResultColor;

uniform vec3 materialColor;

void main()
{
    float red =  abs(vertexData.normal.x);
    float green =  abs(vertexData.normal.y);
    float blue = abs(vertexData.normal.z);

    vec3 mixedColor = materialColor * vec3(red, green, blue);

    ResultColor = vec4(mixedColor, 1.0f);
}