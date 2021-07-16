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
    vec2 texture;
    vec3 normal;
} vertexData;

void main()
{
    vec4 pos = projection_matrix * view_matrix * model_matrix * vec4(position, 1.0f);

    gl_Position = pos;

    vertexData.position = pos.xyz;
    vertexData.texture = texture * tcMultiplier;
    vertexData.normal  = vec3(inverse(transpose(view_matrix * model_matrix)) * vec4(normal, 1.0f));
}
