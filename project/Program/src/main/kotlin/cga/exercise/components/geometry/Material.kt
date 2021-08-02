package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL33C

class Material(var diff: Texture2D,
               var emit: Texture2D,
               var specular: Texture2D,
               var shininess: Float = 50.0f,
               var tcMultiplier : Vector2f = Vector2f(1.0f),
                val color : Vector3f = Vector3f(1f,1f,1f)
)

{

    fun bind(shaderProgram: ShaderProgram)
    {

        if(emit.texID > 0)
        {
            emit.bind(0)
        }



        //shaderProgram.setUniform("diffuseTexture", diff);
        //shaderProgram.setUniform("textureEmissive", 0)
        //shaderProgram.setUniform("specularTexture", specular);
        shaderProgram.setUniform("shiny", shininess)
        shaderProgram.setUniform("textureScaling", tcMultiplier)
        shaderProgram.setUniform("materialColor", color)
    }
}