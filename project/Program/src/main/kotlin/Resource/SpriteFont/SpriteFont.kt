package Resource.SpriteFont

import java.io.File

class SpriteFont(filePath: String)
{
    // Info Header
    var Face = "Unknown"
    var Size = 0
    var Bold = false
    var Italic = false
    var CharSet = "Unknown"
    var Unicode = false
    var StretchHeight = 100
    var Smooth = false
    var Antilising = false
    var Padding = SpriteFontPadding()
    var Spacing = SpriteFontSpacing()

    // Common Header
    var LineHeight = 0
    var Base = 0
    var ScaleWidth = 0
    var ScaleHeight = 0
    var Pages = 0
    var Packed = false

    // Pages
    val PageList = mutableListOf<SpriteFontPage>()

    init
    {
        SpriteFontFileParse(filePath)
    }

    private fun SpriteFontFileParse(filePath: String)
    {
        var currentPage : SpriteFontPage? = null

        File(filePath).forEachLine {

            val lineElements = it.split(" ")

            when(lineElements[0])
            {
                "info" -> LineInfoParse(it)
                "common" -> LineCommonParse(it)
                "page" -> {
                    currentPage = LinePageParse(it, filePath)
                    PageList.add(currentPage!!)
                }
                "char" -> {
                    currentPage!!.CharacterList.add(LineCharacterParse(it))
                }

                //"chars",
                //else -> // Do nothing
            }
        }
    }

    private fun LineInfoParse(infoLine : String)
    {
        val splitResult = infoLine.split(" ")

        val paddingText = splitResult[10]
        val paddingTextSplitEquals = paddingText.split("=")
        val paddingTextSplittComma = paddingTextSplitEquals[1].split(",")

        val spacingText = splitResult[11]
        val spacingTextSplitEquals = spacingText.split("=")
        val spacingTextSplittComma = spacingTextSplitEquals[1].split(",")

        Face = splitResult[1].split("=")[1].removeSurrounding("\"")
        Size = splitResult[2].split("=")[1].toInt()
        Bold = splitResult[3].split("=")[1].toBoolean()
        Italic = splitResult[4].split("=")[1].toBoolean()
        CharSet = splitResult[5].split("=")[1]
        Unicode =  splitResult[6].split("=")[1].toBoolean()
        StretchHeight = splitResult[7].split("=")[1].toInt()
        Smooth =  splitResult[8].split("=")[1].toBoolean()
        Antilising =  splitResult[9].split("=")[1].toBoolean()

        Padding = SpriteFontPadding(
            paddingTextSplittComma[0].toInt(),
            paddingTextSplittComma[1].toInt(),
            paddingTextSplittComma[2].toInt(),
            paddingTextSplittComma[3].toInt()
        )

        Spacing = SpriteFontSpacing(
            spacingTextSplittComma[0].toInt(),
            spacingTextSplittComma[1].toInt()
        )
    }

    private fun LineCommonParse(commonLine : String)
    {
        val splitResult = commonLine.split(" ")

        LineHeight = splitResult[1].split("=")[1].toInt()
        Base = splitResult[2].split("=")[1].toInt()
        ScaleWidth = splitResult[3].split("=")[1].toInt()
        ScaleHeight = splitResult[4].split("=")[1].toInt()
        Pages = splitResult[5].split("=")[1].toInt()
        Packed = splitResult[6].split("=")[1].toBoolean()
    }

    private fun LinePageParse(pageLine : String, originalFilePath : String) : SpriteFontPage
    {
        val splitResult = pageLine.split(" ")

        val id = splitResult[1].split("=")[1].toInt()
        val name = splitResult[2].split("=")[1].removeSurrounding("\"")

        val lastSlash = originalFilePath.lastIndexOf('/')
        val croppedPath = originalFilePath.substring(0, lastSlash+1)
        val completeImageFilePath = croppedPath + name

        val spriteFontPage = SpriteFontPage(id, completeImageFilePath)

        return spriteFontPage
    }

    private fun LineCharacterParse(characterLine : String) : SpriteFontCharacter
    {
        val lineNoWhiteSpace = characterLine.replace("\\s+".toRegex(), " ") // Remove whitespace
        val splitResult = lineNoWhiteSpace.split(" ")

        val id = splitResult[1].split("=")[1].toInt()
        val x = splitResult[2].split("=")[1].toInt()
        val y = splitResult[3].split("=")[1].toInt()
        val width = splitResult[4].split("=")[1].toInt()
        val height = splitResult[5].split("=")[1].toInt()
        val xOffset = splitResult[6].split("=")[1].toInt()
        val yOffset = splitResult[7].split("=")[1].toInt()
        val xAdvance = splitResult[8].split("=")[1].toInt()
        val pageID = splitResult[9].split("=")[1].toInt()
        val channelID = splitResult[10].split("=")[1].toInt()

        val spriteFontCharacter = SpriteFontCharacter(id, x, y, width, height, xOffset, yOffset, xAdvance, pageID, channelID)

        return spriteFontCharacter
    }
}