package cga.exercise.components.texture

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL33C
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer

class TextureCube(
    rightTextureFilePath : String,
                    leftTextureFilePath : String,
                    topTextureFilePath : String,
                    bottomTextureFilePath : String,
                    backTextureFilePath : String,
                    frontTextureFilePath : String
) : ITexture
{

    var TextureID = -1


    private fun LoadImageData(filePath: String, mode : Int)
    {
        val x = BufferUtils.createIntBuffer(1)
        val y = BufferUtils.createIntBuffer(1)
        val readChannels = BufferUtils.createIntBuffer(1)

        //flip y coordinate to make OpenGL happy
        STBImage.stbi_set_flip_vertically_on_load(true)
        val imageData = STBImage.stbi_load(filePath, x, y, readChannels, 4)
            ?: throw Exception("Image file \"" + filePath + "\" couldn't be read:\n" + STBImage.stbi_failure_reason())

        GL33C.glTexImage2D(GL33C.GL_TEXTURE_CUBE_MAP_POSITIVE_X + mode, 0, GL33C.GL_RGBA, x.get(), y.get(), 0, GL33C.GL_RGBA, GL33C.GL_UNSIGNED_BYTE, imageData)

        STBImage.stbi_image_free(imageData)
    }


    init
    {
        // Creating a cubemap
        TextureID = GL33C.glGenTextures()
        GL33C.glBindTexture(GL33C.GL_TEXTURE_CUBE_MAP, TextureID)

        LoadImageData(rightTextureFilePath, 0)
        LoadImageData(leftTextureFilePath ,1)
        LoadImageData(topTextureFilePath,2)
        LoadImageData(bottomTextureFilePath,3)
        LoadImageData(backTextureFilePath,4)
        LoadImageData(frontTextureFilePath,5)

        GL33C.glTexParameteri(GL33C.GL_TEXTURE_CUBE_MAP, GL33C.GL_TEXTURE_MAG_FILTER, GL33C.GL_LINEAR);
        GL33C.glTexParameteri(GL33C.GL_TEXTURE_CUBE_MAP, GL33C.GL_TEXTURE_MIN_FILTER, GL33C.GL_LINEAR);
        GL33C.glTexParameteri(GL33C.GL_TEXTURE_CUBE_MAP, GL33C.GL_TEXTURE_WRAP_S, GL33C.GL_CLAMP_TO_EDGE);
        GL33C.glTexParameteri(GL33C.GL_TEXTURE_CUBE_MAP, GL33C.GL_TEXTURE_WRAP_T, GL33C.GL_CLAMP_TO_EDGE);
        GL33C.glTexParameteri(GL33C.GL_TEXTURE_CUBE_MAP, GL33C.GL_TEXTURE_WRAP_R, GL33C.GL_CLAMP_TO_EDGE);
    }

    override fun processTexture(imageData: ByteBuffer, width: Int, height: Int, genMipMaps: Boolean)
    {
        TODO("Not yet implemented")
    }

    override fun setTexParams(wrapS: Int, wrapT: Int, minFilter: Int, magFilter: Int)
    {
        TODO("Not yet implemented")
    }

    override fun bind(textureUnit: Int)
    {
        GL33C.glBindTexture(GL33C.GL_TEXTURE_CUBE_MAP, TextureID)
    }

    override fun unbind()
    {
        GL33C.glBindTexture(GL33C.GL_TEXTURE_CUBE_MAP, 0)
    }

    override fun cleanup()
    {
        TODO("Not yet implemented")
    }
}