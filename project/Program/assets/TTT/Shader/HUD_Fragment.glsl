#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 Position;
    vec2 TexturePosition;
} vertexData;

//fragment shader output
out vec4 ResultColor;

uniform vec3 materialColor;
uniform sampler2D textureEmissive;

void main()
{
    vec4 recolor = texture(textureEmissive, vertexData.TexturePosition);

    if(recolor.xyz == vec3(175/255f, 0f, 1f) ||recolor.xyz == vec3(0f, 0f, 0f))
    {
        discard;
    }

    ResultColor = recolor;
}