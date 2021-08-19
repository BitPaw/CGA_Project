package Resource

import TTT.GameField
import TTT.PlayerSymbol
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import org.joml.Vector3f

class Pillar(val Field: GameField, val PillarObject: Renderable, val BlockObject: Renderable)
{
    var Selected = false
    val HitBoxAncerPoint = Vector3f()

    fun Render(shaderProgram: ShaderProgram, blockTexture : Texture2D?)
    {
        var color = Vector3f(1f, 1f, 1f)

        if(Selected)
        {
            color.set(0.5f,0.5f,0f)
        }

        PillarObject.MeshList[0].material.color.set(color)
        BlockObject.MeshList[0].material.color.set(color)
        PillarObject.render(shaderProgram)

        if(blockTexture != null)
        {
            BlockObject.MeshList[0].material.emit = blockTexture
            BlockObject.render(shaderProgram)
        }

    }
}