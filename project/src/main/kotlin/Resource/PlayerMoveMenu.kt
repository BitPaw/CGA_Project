package Resource

import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector2d
import org.joml.Vector3f
import java.lang.Exception

class PlayerMoveMenu(
    rectangle: Renderable,
    val Zero: Texture2D,
    val One: Texture2D,
    val Two: Texture2D,
    val Three: Texture2D,
    val Four: Texture2D,
    val Five: Texture2D,
    val Six: Texture2D
    )
{
    val Element0 = PlayerMoveElement(Renderable(rectangle.MeshList), Zero)
    val Element1 = PlayerMoveElement(Renderable(rectangle.MeshList), One)
    val Element2 = PlayerMoveElement(Renderable(rectangle.MeshList), Two)
    val Element3 = PlayerMoveElement(Renderable(rectangle.MeshList), Three)
    val Element4 = PlayerMoveElement(Renderable(rectangle.MeshList), Four)
    val Element5 = PlayerMoveElement(Renderable(rectangle.MeshList), Five)
    val Element6 = PlayerMoveElement(Renderable(rectangle.MeshList), Six)

    private var _isActive = false
    var CurrentlySelected = 0

    init
    {
        val scale = 0.15f
        val scaleMegre = 0.35f

        //Element0.translateLocal(0.3f,-1f)
        Element1.Element.translateLocal(0.3f * scaleMegre,-0.5f* scaleMegre)
        Element2.Element.translateLocal(0.6f* scaleMegre,0f* scaleMegre)
        Element3.Element.translateLocal(0.3f* scaleMegre,0.5f* scaleMegre)
        Element4.Element.translateLocal(-0.3f* scaleMegre,0.5f* scaleMegre)
        Element5.Element.translateLocal(-0.6f* scaleMegre,0f* scaleMegre)
        Element6.Element.translateLocal(-0.3f* scaleMegre,-0.5f* scaleMegre)

        // maybe use Parents?

        Element0.Element.scaleLocal(scale)
        Element1.Element.scaleLocal(scale)
        Element2.Element.scaleLocal(scale)
        Element3.Element.scaleLocal(scale)
        Element4.Element.scaleLocal(scale)
        Element5.Element.scaleLocal(scale)
        Element6.Element.scaleLocal(scale)
    }

    fun Move(x : Float, y : Float, z : Float)
    {
        Element0.Element.translateLocal(x, y, z)
        Element1.Element.translateLocal(x, y, z)
        Element2.Element.translateLocal(x, y, z)
        Element3.Element.translateLocal(x, y, z)
        Element4.Element.translateLocal(x, y, z)
        Element5.Element.translateLocal(x, y, z)
        Element6.Element.translateLocal(x, y, z)
    }

    fun SetActive(mode : Boolean)
    {
        Element0.Locked = !mode
        Element1.Locked = !mode
        Element2.Locked = !mode
        Element3.Locked = !mode
        Element4.Locked = !mode
        Element5.Locked = !mode
        Element6.Locked = !mode

        if(mode)
        {
            SelectUp()
        }
    }

    fun Render(shaderProgram: ShaderProgram)
    {
        Element0.Render(shaderProgram)
        Element1.Render(shaderProgram)
        Element2.Render(shaderProgram)
        Element3.Render(shaderProgram)
        Element4.Render(shaderProgram)
        Element5.Render(shaderProgram)
        Element6.Render(shaderProgram)
    }

    fun ElementLock(index : Int)
    {
        GetElement(index).UsedUp = true
    }

    fun ElementResetAll()
    {
        Element0.Selected = false
        Element1.Selected = false
        Element2.Selected = false
        Element3.Selected = false
        Element4.Selected = false
        Element5.Selected = false
        Element6.Selected = false

        Element0.UsedUp = false
        Element1.UsedUp = false
        Element2.UsedUp = false
        Element3.UsedUp = false
        Element4.UsedUp = false
        Element5.UsedUp = false
        Element6.UsedUp = false

        CurrentlySelected = 0

        SelectUp()
    }

    private fun GetElement(index : Int) : PlayerMoveElement
    {
        return when(index)
        {
            0 -> Element0
            1 -> Element1
            2 -> Element2
            3 -> Element3
            4 -> Element4
            5 -> Element5
            6 -> Element6
            else -> throw Exception("$index")
        }
    }

    fun SelectUp()
    {
        GetElement(CurrentlySelected).Selected = false

        while(true)
        {
            CurrentlySelected = (++CurrentlySelected % 7)

            if(CurrentlySelected == 0)
            {
                CurrentlySelected++
            }

            if(!GetElement(CurrentlySelected).UsedUp)
            {
                break
            }
        }

        GetElement(CurrentlySelected).Selected = true
    }

    fun SelectDown()
    {
        GetElement(CurrentlySelected).Selected = false

        while(true)
        {
            CurrentlySelected = (--CurrentlySelected % 7)

            if(CurrentlySelected < 1)
            {
                CurrentlySelected = 6
            }

            if(!GetElement(CurrentlySelected).UsedUp)
            {
                break
            }
        }

        GetElement(CurrentlySelected).Selected = true
    }
}