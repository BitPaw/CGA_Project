#version 330 core

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texture;
layout(location = 2) in vec3 normal;

//---uniforms ---------------------------
uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;

uniform vec2 tcMultiplier;
///--------------------------------

out struct VertexData
{
    vec3 position;
    vec3 normal;
    vec2 texturePosition;
} vertexData;

void main()
{
    vec4 position4x = vec4(position, 1.0f);
    vec4 pos = projection_matrix * view_matrix * model_matrix * position4x;

    gl_Position = pos;

    vertexData.position =  vec3(model_matrix * position4x);
    vertexData.normal  = (model_matrix * vec4(normal, 1.0f)).xyz;
    vertexData.texturePosition = texture * tcMultiplier;
}
