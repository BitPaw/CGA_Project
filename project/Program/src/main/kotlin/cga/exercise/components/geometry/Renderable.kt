package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f

class Renderable(meshList : MutableList<Mesh> = mutableListOf(), modelMatrix: Matrix4f = Matrix4f(), parent: Transformable? = null): Transformable(modelMatrix, parent), IRenderable
{
    var MeshList = mutableListOf<Mesh>()

    init
    {
        meshList.forEach{mesh ->  MeshList.add(mesh)}
    }

    override fun render(shaderProgram: ShaderProgram)
    {
        shaderProgram.use()

        shaderProgram.setUniform("model_matrix", getWorldModelMatrix(), false)

        MeshList.forEach{mesh -> mesh.render(shaderProgram) }
    }

    fun addToMeshList(addableMeshList : List<Mesh>)
    {
        addableMeshList.forEach { item -> MeshList.add(item) }
    }

    // Clear all data and unregister!
    fun Reset()
    {

    }
}