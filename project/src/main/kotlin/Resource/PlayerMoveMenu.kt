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
    val Element0 = Renderable(rectangle.MeshList)
    val Element1 = Renderable(rectangle.MeshList)
    val Element2 = Renderable(rectangle.MeshList)
    val Element3 = Renderable(rectangle.MeshList)
    val Element4 = Renderable(rectangle.MeshList)
    val Element5 = Renderable(rectangle.MeshList)
    val Element6 = Renderable(rectangle.MeshList)

    var IsActive = false
    var CurrentlySelected = 1;

    init
    {
        val scale = 0.15f
        val scaleMegre = 0.35f

        //Element0.translateLocal(0.3f,-1f)
        Element1.translateLocal(0.3f * scaleMegre,-0.5f* scaleMegre)
        Element2.translateLocal(0.6f* scaleMegre,0f* scaleMegre)
        Element3.translateLocal(0.3f* scaleMegre,0.5f* scaleMegre)
        Element4.translateLocal(-0.3f* scaleMegre,0.5f* scaleMegre)
        Element5.translateLocal(-0.6f* scaleMegre,0f* scaleMegre)
        Element6.translateLocal(-0.3f* scaleMegre,-0.5f* scaleMegre)


        Element0.scaleLocal(scale)
        Element1.scaleLocal(scale)
        Element2.scaleLocal(scale)
        Element3.scaleLocal(scale)
        Element4.scaleLocal(scale)
        Element5.scaleLocal(scale)
        Element6.scaleLocal(scale)
    }

    fun Move(x : Float, y : Float, z : Float)
    {
        Element0.translateLocal(x, y, z)
        Element1.translateLocal(x, y, z)
        Element2.translateLocal(x, y, z)
        Element3.translateLocal(x, y, z)
        Element4.translateLocal(x, y, z)
        Element5.translateLocal(x, y, z)
        Element6.translateLocal(x, y, z)
    }

    fun Render(shaderProgram: ShaderProgram)
    {
        SelectColor(0)
        Element0.MeshList[0].material.emit = Zero
        Element0.render(shaderProgram)

        SelectColor(1)
        Element1.MeshList[0].material.emit = One
        Element1.render(shaderProgram)

        SelectColor(2)
        Element2.MeshList[0].material.emit = Two
        Element2.render(shaderProgram)

        SelectColor(3)
        Element3.MeshList[0].material.emit = Three
        Element3.render(shaderProgram)

        SelectColor(4)
        Element4.MeshList[0].material.emit = Four
        Element4.render(shaderProgram)

        SelectColor(5)
        Element5.MeshList[0].material.emit = Five
        Element5.render(shaderProgram)

        SelectColor(6)
        Element6.MeshList[0].material.emit = Six
        Element6.render(shaderProgram)
    }

    private fun GetElement(index : Int) : Renderable
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

    private val colorSelected = Vector3f(1f, 1f, 0f)
    private val colorDefault = Vector3f(1f, 1f, 1f)
    private val colorInActive = Vector3f(-0.5f, -0.5f, -0.5f)


    private fun SelectColor(index : Int)
    {
        val highlightColor = CurrentlySelected == index

        if(IsActive)
        {
            if(highlightColor)
            {
                GetElement(index).MeshList[0].material.color.set(colorSelected)
            }
            else
            {
                GetElement(index).MeshList[0].material.color.set(colorDefault)
            }
        }
        else
        {
            GetElement(index).MeshList[0].material.color.set(colorInActive)
        }
    }

    fun SelectUp()
    {
        CurrentlySelected = (++CurrentlySelected % 7)
        if(CurrentlySelected == 0)
        {
            CurrentlySelected++
        }

    }

    fun SelectDown()
    {
        CurrentlySelected = (--CurrentlySelected % 7)

        if(CurrentlySelected < 1)
        {
            CurrentlySelected = 6
        }

    }
}