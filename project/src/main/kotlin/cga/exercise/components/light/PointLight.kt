package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f

open class PointLight(startPosition : Vector3f, var LightColor: Vector3f = Vector3f(1f, 1f, 1f), val lightDecay : Vector3f) : IPointLight, Transformable()
{
    init
    {
           translateGlobal(startPosition)
    }

    override fun bind(shaderProgram: ShaderProgram, name: String)
    {
        shaderProgram.setUniform("pointLightPosition",  getWorldPosition())       // Update posiition to shader
        shaderProgram.setUniform("pointLightColor", LightColor)   // Update color to shader
        shaderProgram.setUniform("lightDecayPointLight", lightDecay)
    }
}