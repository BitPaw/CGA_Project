package Resource

import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.exercise.components.texture.TextureCube
import cga.framework.ModelLoader
import org.lwjgl.opengl.GL33C

class Skybox(
    shaderVertexFilePath : String,
    shaderFragmentFilePath : String,
    cubeModelFilePath : String,
    rightTextureFilePath : String,
    leftTextureFilePath : String,
    topTextureFilePath : String,
    bottomTextureFilePath : String,
    backTextureFilePath : String,
    frontTextureFilePath : String
)
{
    val Texture = TextureCube(
        rightTextureFilePath,
        leftTextureFilePath,
        topTextureFilePath,
        bottomTextureFilePath,
        backTextureFilePath,
        frontTextureFilePath)

    val Cube = ModelLoader.loadModel(cubeModelFilePath, 0f, 0f, 0f)

    val Shader = ShaderProgram(shaderVertexFilePath,shaderFragmentFilePath)

}