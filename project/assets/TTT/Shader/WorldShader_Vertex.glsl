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
    vec3 Normal;
} vertexData;

uniform vec2 textureScaling;

void main()
{
    vec4 pos = projection_matrix * view_matrix * model_matrix * vec4(position, 1.0f);

    gl_Position = pos;

    vertexData.Position = pos.xyz;
    vertexData.TexturePosition = texture * textureScaling;
    vertexData.Normal  = vec3(inverse(transpose(view_matrix * model_matrix)) * vec4(normal, 1.0f));
}
