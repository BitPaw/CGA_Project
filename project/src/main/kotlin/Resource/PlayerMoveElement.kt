package Resource

import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector3f

class PlayerMoveElement(val Element: Renderable, val Texture : Texture2D)
{
    private val _colorSelected = Vector3f(1f, 1f, 0f)
    private val _colorDefault = Vector3f(1f, 1f, 1f)
    private val _colorInActive = Vector3f(-0.5f, -0.5f, -0.5f)

    var UsedUp = false
    var Locked = false
    var Selected = false

    fun Render(shaderProgram: ShaderProgram)
    {
        Element.MeshList[0].material.color.set(Color())
        Element.MeshList[0].material.emit = Texture
        Element.render(shaderProgram)
    }

    fun Color() : Vector3f
    {
        if(Locked || UsedUp)
        {
            return _colorInActive
        }

        if(Selected)
        {
            return  _colorSelected
        }

        return _colorDefault
    }

    fun Move()
    {

    }

    fun Scale()
    {

    }
}