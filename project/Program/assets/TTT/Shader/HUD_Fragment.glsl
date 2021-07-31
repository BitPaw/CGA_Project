#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec3 normal;
} vertexData;

//fragment shader output
out vec4 color;

void main()
{
    float red =  abs(vertexData.normal.x);
    float green =  abs(vertexData.normal.y);
    float blue = abs(vertexData.normal.z);

    color = vec4(red, green, blue, 1.0f);
}