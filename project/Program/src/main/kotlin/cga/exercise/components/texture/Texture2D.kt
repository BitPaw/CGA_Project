package cga.exercise.components.texture

import cga.framework.GLError.checkEx
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.*
import org.lwjgl.stb.STBImage
import java.nio.ByteBuffer


/**
 * Created by Fabian on 16.09.2017.
 */
class Texture2D(imageData: ByteBuffer, width: Int, height: Int, genMipMaps: Boolean): ITexture{
    var texID: Int = -1




    init {
        try {
            processTexture(imageData, width, height, genMipMaps)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
    companion object {
        //create texture from file
        //don't support compressed textures for now
        //instead stick to pngs
        operator fun invoke(path: String, genMipMaps: Boolean): Texture2D {

            val x = BufferUtils.createIntBuffer(1)
            val y = BufferUtils.createIntBuffer(1)
            val readChannels = BufferUtils.createIntBuffer(1)
            //flip y coordinate to make OpenGL happy
            STBImage.stbi_set_flip_vertically_on_load(true)
            val imageData = STBImage.stbi_load(path, x, y, readChannels, 4)
                    ?: throw Exception("Image file \"" + path + "\" couldn't be read:\n" + STBImage.stbi_failure_reason())


            try {
                return Texture2D(imageData, x.get(), y.get(), genMipMaps)
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                throw ex
            } finally {
                STBImage.stbi_image_free(imageData)
            }
        }
    }

    override fun processTexture(imageData: ByteBuffer, width: Int, height: Int, genMipMaps: Boolean)
    {
        val mode = GL33C.GL_TEXTURE_2D

        texID = GL33C.glGenTextures()
        GL33C.glBindTexture(mode, texID);

        GL33C.glTexImage2D(mode, 0, GL33C.GL_RGBA, width, height, 0, GL33C.GL_RGBA, GL33C.GL_UNSIGNED_BYTE, imageData);

        setTexParams(GL33C.GL_CLAMP_TO_BORDER, GL33C.GL_CLAMP_TO_EDGE, GL33C.GL_NEAREST , GL33C.GL_NEAREST)

        if(genMipMaps)
        {
            GL33C.glGenerateMipmap(mode)
        }

    }

    override fun setTexParams(wrapS: Int, wrapT: Int, minFilter: Int, magFilter: Int)
    {
        val mode = GL33C.GL_TEXTURE_2D

        GL33C.glBindTexture(mode, texID);

        GL33C.glTexParameteri(mode, GL33C.GL_TEXTURE_WRAP_S, wrapS);
        GL33C.glTexParameteri(mode, GL33C.GL_TEXTURE_WRAP_T, wrapT);

        GL33C.glTexParameteri(mode, GL33C.GL_TEXTURE_MIN_FILTER, minFilter);
        GL33C.glTexParameteri(mode, GL33C.GL_TEXTURE_MAG_FILTER, magFilter);

       // GL33C.glTexParameterf(GL33C.GL_TEXTURE_2D,EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT,16.0f)



        GL33C.glBindTexture(mode, 0)
    }

    override fun bind(textureUnit: Int)  // ??
    {
        //GL33C.glActiveTexture(GL33C.GL_TEXTURE0 + textureUnit);
        GL33C.glBindTexture(GL33C.GL_TEXTURE_2D, texID)
        //GL33C.glEnable(GL33C.GL_TEXTURE_2D)
    }

    override fun unbind()
    {
        GL33C.glBindTexture(GL33C.GL_TEXTURE_2D,0)
    }

    override fun cleanup() {
        unbind()
        if (texID != 0) {
            GL11.glDeleteTextures(texID)
            texID = 0
        }
    }
}