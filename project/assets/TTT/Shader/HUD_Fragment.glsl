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

    if(recolor.xyz == vec3(1f, 1f, 1f))
    {
        discard;
    }

    if(materialColor != vec3(1f, 1f, 1f))
    {
        recolor = vec4(materialColor + recolor.xyz, recolor.w);
    }

    ResultColor = recolor;
}