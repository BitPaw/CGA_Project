package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f

open class Renderable(val MeshList : MutableList<Mesh> = mutableListOf(), modelMatrix: Matrix4f = Matrix4f(), parent: Transformable? = null): Transformable(modelMatrix, parent), IRenderable
{
    override fun render(shaderProgram: ShaderProgram)
    {
        shaderProgram.use()

        shaderProgram.setUniform("model_matrix", getWorldModelMatrix(), false)

        MeshList.forEach{mesh -> mesh.render(shaderProgram) }
    }
}