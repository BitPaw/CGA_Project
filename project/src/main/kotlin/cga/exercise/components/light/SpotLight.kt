package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgram
import org.joml.Math.toRadians
import org.joml.Matrix4f
import org.joml.Vector3f

class SpotLight
    (
    startPosition : Vector3f,
    lightColor: Vector3f,
    lightDecay : Vector3f ,
    val innerAngle : Float,
    val outerAngle : Float
    ) : ISpotLight, PointLight(startPosition, lightColor, lightDecay)
{
    init
    {
        super.LightColor = lightColor
    }

    override fun bind(shaderProgram: ShaderProgram, name: String, viewMatrix: Matrix4f)
    {
        shaderProgram.setUniform("spotLightPosition", getWorldPosition())       // Update posiition to shader
        shaderProgram.setUniform("spotLightColor", LightColor)   // Update color to shader
        shaderProgram.setUniform("innerAngle",toRadians(innerAngle))
        shaderProgram.setUniform("outerAngle", toRadians(outerAngle))
        shaderProgram.setUniform("lightDecaySpotLight",  lightDecay)
        shaderProgram.setUniform("coneDirection", getWorldXAxis().add(getWorldZAxis()))
    }
}