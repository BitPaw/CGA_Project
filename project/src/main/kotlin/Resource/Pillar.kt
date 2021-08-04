package Resource

import TTT.GameField
import TTT.PlayerSymbol
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D

class Pillar(val Field: GameField, val PillarObject: Renderable, val BlockObject: Renderable)
{
    fun Render(shaderProgram: ShaderProgram, x : Texture2D, o : Texture2D)
    {
        PillarObject.render(shaderProgram)

        val texture = when(Field.Symbol)
        {
            PlayerSymbol.None -> return
            PlayerSymbol.X -> x
            PlayerSymbol.O -> o
        }

        BlockObject.MeshList[0].material.emit = texture
        BlockObject.render(shaderProgram)
    }
}