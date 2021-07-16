#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 position;
    vec2 texture;
    vec3 normal;
} vertexData;

uniform sampler2D emissiveTexture;

//fragment shader output
out vec4 color;

void main()
{
    vec4 white = vec4(0.8f,0f,1f,1f);

    color = texture(emissiveTexture, vertexData.texture) + (white * 0.8f) * vec4(vertexData.normal, 1.0f);

}