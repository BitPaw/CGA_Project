package Resource

import Resource.SpriteFont.SpriteFont
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.Transformable

class Text
{
    private var _spriteFont : SpriteFont? = null
    private var _content = ""
    private var _renderable = Renderable()

    private fun UpdateFont()
    {
        _renderable.Reset()


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