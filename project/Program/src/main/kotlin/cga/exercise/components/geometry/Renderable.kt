package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f

class Renderable(val meshList : MutableList<Mesh> = mutableListOf(), modelMatrix: Matrix4f = Matrix4f(), parent: Transformable? = null): Transformable(modelMatrix, parent), IRenderable
{
    override fun render(shaderProgram: ShaderProgram)
    {
        shaderProgram.use()

        shaderProgram.setUniform("model_matrix", getWorldModelMatrix(), false)

        meshList.forEach{mesh -> mesh.render(shaderProgram) }
    }

    fun addToMeshList(addableMeshList : List<Mesh>)
    {
        addableMeshList.forEach { item -> meshList.add(item) }
    }
}