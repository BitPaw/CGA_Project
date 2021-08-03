package Resource

import Resource.SpriteFont.SpriteFont
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.framework.OBJLoader
import org.joml.Vector2f
import org.joml.Vector2i
import org.joml.Vector3f
import org.lwjgl.opengl.GL11
import java.text.Normalizer

class Text : Renderable()
{
    private var _spriteFont : SpriteFont? = null
    private var _content = ""

    private fun Interpolate(minY : Float, maxY : Float, minX : Float, maxX : Float, valueX : Float) : Float
    {
        var value = 0.0f

        value = if(valueX > maxX) maxX else valueX
        value = if(value < minX) minX else value

        return (maxY - minY) * (value - minX) / (maxX - minX) + minY
    }

    private fun UpdateFont()
    {
        MeshList.clear()

        val hasFont = _spriteFont != null

        if(!hasFont)
        {
            return
        }

        val hasData = _spriteFont!!.PageList.size > 0

        if(!hasData)
        {
            return
        }




        val vertexDataList = mutableListOf<Float>()
        val indexDataList = mutableListOf<Int>()
        val fontPage = _spriteFont!!.PageList[0]
        var lastXPosition = 0
        val currentPos = getPosition()
        val ancerPosition = Vector2f(currentPos.x, currentPos.y)
        val fontSize = 0.002f
        val characterSpacing = 1

        var indexCounter = 0

        _content.forEach { character ->

            val fontChar =  fontPage.GetCharacter(character.toInt())
            val hasCharacterInFont = fontChar != null

            if(hasCharacterInFont)
            {
                val charPosition = Vector2i(fontChar!!.X, fontChar!!.Y)
                val charSize = Vector2i(fontChar!!.Width, fontChar!!.Height)
                val charTexturPointPosition = Vector2f(
                    Interpolate(0f,1f, 0f, 512f, charPosition.x.toFloat()),
                    Interpolate(0f,1f, 0f, 512f, charPosition.y.toFloat())
                )
                val charTexturePointSize = Vector2f(
                    Interpolate(0f,1f, 0f, 512f, charSize.x.toFloat()),
                    Interpolate(0f,1f, 0f, 512f, charSize.y.toFloat())
                )

                val vertexContent = Rectangle(
                ancerPosition.x + fontSize * lastXPosition,
                ancerPosition.y +fontSize,
                ancerPosition.x + fontSize * (charSize.x + lastXPosition),
                ancerPosition.y +fontSize * charSize.y
                )

                val texturePositionContent = Rectangle(
                    charTexturPointPosition.x,
                    charTexturPointPosition.y,
                    charTexturPointPosition.x + charTexturePointSize.x,
                    charTexturPointPosition.y + charTexturePointSize.y
                )

                lastXPosition += charSize.x + characterSpacing

                if(character == ' ')
                {
                    lastXPosition += characterSpacing * 10
                }

                val normal = Vector3f(0f, 0f, 1f)

                // Add to vertex data
                val vertexlist = listOf(
                    // Vertex - A
                    vertexContent.x,
                    vertexContent.y,
                    0f,

                    // Texture - D
                    texturePositionContent.x,
                    texturePositionContent.height,

                    normal.x,
                    normal.y,
                    normal.z,

                    // Vertex - B
                    vertexContent.width,
                    vertexContent.y,
                    0f,

                    // Texture - C
                    texturePositionContent.width,
                    texturePositionContent.height,

                    normal.x,
                    normal.y,
                    normal.z,

                    // Vertex - C
                    vertexContent.width,
                    vertexContent.height,
                    0f,

                    // Texture - B
                    texturePositionContent.width,
                    texturePositionContent.y,

                    normal.x,
                    normal.y,
                    normal.z,

                    // Vertex - D
                    vertexContent.x,
                    vertexContent.height,
                    0f,

                    // Texture - A
                    texturePositionContent.x,
                    texturePositionContent.y,

                    normal.x,
                    normal.y,
                    normal.z
                )

                val size = indexCounter

                // Triangle Mode

                indexDataList.add(size)
                indexDataList.add(size+1)
                indexDataList.add(size+2)
                indexDataList.add(size+2)
                indexDataList.add(size+3)
                indexDataList.add(size)


                // Quad mode
                /*
           indexDataList.add(size)
           indexDataList.add(size+1)
           indexDataList.add(size+2)
           indexDataList.add(size+3)
*/

                indexCounter += 4

                vertexlist.forEach { nr -> vertexDataList.add(nr) }
            }
            else
            {
                throw Exception()
            }

            }


        // If data
        val atr1 = VertexAttribute(3, GL11.GL_FLOAT, 8*4, 0)
        val atr2 = VertexAttribute(2, GL11.GL_FLOAT, 8*4, 3 * 4)
        val atr3 = VertexAttribute(3, GL11.GL_FLOAT, 8*4, 5 * 4)
        val vertexAttributes = arrayOf(atr1, atr2, atr3)

        val currentMesh = Mesh(vertexDataList.toFloatArray(), indexDataList.toIntArray(), vertexAttributes)

        currentMesh.RenderMode = GL11.GL_TRIANGLES //GL11.GL_QUADS
        currentMesh.material.emit = fontPage.Texture

        MeshList.add(currentMesh)
    }

    fun FontSet(spriteFont : SpriteFont)
    {
        _spriteFont = spriteFont

        UpdateFont()
    }

    fun TextSet(text : String)
    {
        val sameString = _content == text

        if(!sameString)
        {
            _content = text

            UpdateFont()
        }
    }
}