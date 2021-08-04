#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texture;
layout(location = 2) in vec3 normal;

//---uniforms ---------------------------
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;
///--------------------------------

out struct VertexData
{
    vec3 Position;
    vec2 TexturePosition;
} vertexData;

uniform vec2 textureScaling;

void main()
{
    // projection_matrix *  view_matrix *
    vec4 pos = model_matrix * vec4(position, 1.0f);

    gl_Position = pos;

    vertexData.Position = pos.xyz;
    vertexData.TexturePosition = texture * textureScaling;
}
