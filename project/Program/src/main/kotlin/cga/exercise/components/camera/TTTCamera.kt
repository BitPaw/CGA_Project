package cga.exercise.components.camera

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Math
import org.joml.Matrix4f

class TTTCamera(var fieldofView : Float = 45f, var aspectRatio: Float = 16f/9f, var nearPlane: Float = 0.1f, var farPlane: Float = 1000f) : ICamera, Transformable()
{
    var ThirdDimension = true

    override fun getCalculateViewMatrix(): Matrix4f
    {
        val view = Matrix4f()

        view.lookAt(getWorldPosition(),getWorldPosition().sub(getWorldZAxis()), getWorldYAxis())

        return view
    }

    override fun getCalculateProjectionMatrix(): Matrix4f
    {
        val projection = Matrix4f()

        if(ThirdDimension)
        {
            projection.perspective(Math.toRadians(fieldofView), aspectRatio, nearPlane, farPlane)
        }
        else
        {
            projection.ortho(0f, 0f,0f,0f, nearPlane, farPlane)
        }

        return projection
    }

    override fun bind(shader: ShaderProgram)
    {
        shader.setUniform("cameraPosition", getPosition())
        shader.setUniform("view_matrix", getCalculateViewMatrix(), false )
        shader.setUniform("projection_matrix", getCalculateProjectionMatrix(), false)
    }
}