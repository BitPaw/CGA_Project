#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 Position;
    vec2 TexturePosition;
    vec3 Normal;
} vertexData;

//fragment shader output
out vec4 color;

uniform vec3 materialColor;
uniform sampler2D emissiveTexture;

void main()
{
    float red =  abs(vertexData.Normal.x);
    float green =  abs(vertexData.Normal.y);
    float blue = abs(vertexData.Normal.z);

    vec3 mixedColor = materialColor * vec3(red, green, blue);

    color = texture(emissiveTexture, vertexData.TexturePosition) + vec4(mixedColor, 1.0f);
}