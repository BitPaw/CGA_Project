package Resource.SpriteFont

import cga.exercise.components.texture.Texture2D

class SpriteFontPage(
    val PageID: Int,
    val TextureFileName: String,
    val CharacterList: MutableList<SpriteFontCharacter> = mutableListOf()
)
{
    val Texture = Texture2D(TextureFileName, true)
}