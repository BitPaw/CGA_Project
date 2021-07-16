package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL33C

/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */
class Mesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>, val material: Material? = null)
{
    private var data = vertexdata;
    private var index = indexdata;
    private var vao = 0
    private var vbo = 0
    private var ibo = 0
    private var indexcount = 0

    init {
        indexcount = index.size

        //---[ Create vertex array object (VAO) ]------------------------------
        vao = GL33C.glGenVertexArrays()
        GL33C.glBindVertexArray(vao)
        //---------------------------------------------------------------------

        //---[ Create vertex buffer object (VBO) ]-----------------------------
        vbo = GL33C.glGenBuffers()
        GL33C.glBindBuffer(GL33C.GL_ARRAY_BUFFER, vbo)
        GL33C.glBufferData(GL33C.GL_ARRAY_BUFFER, data, GL33C.GL_DYNAMIC_DRAW)
        //---------------------------------------------------------------------

        for ((attributeCounter, attribute) in attributes.withIndex())
        {
            GL33C.glEnableVertexAttribArray(attributeCounter)
            GL33C.glVertexAttribPointer(attributeCounter, attribute.n, attribute.type, false, attribute.stride, attribute.offset.toLong())
        }

        /*
        //---[ Position ]------------------------------------------------------
        GL33C.glEnableVertexAttribArray(0)
        GL33C.glVertexAttribPointer(0, vertexDataSize, GL33C.GL_FLOAT, false, blockSize, 0)
        //---------------------------------------------------------------------

        //---[ Color ]---------------------------------------------------------
        GL33C.glEnableVertexAttribArray(1)
        GL33C.glVertexAttribPointer(1, indexDataSize, GL33C.GL_FLOAT, false, blockSize, (vertexDataSize * sizeOfFloat).toLong())
        //---------------------------------------------------------------------
        */

        //---[ Create Index data ]---------------------------------------------
        ibo = GL33C.glGenBuffers();
        GL33C.glBindBuffer(GL33C.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL33C.glBufferData(GL33C.GL_ELEMENT_ARRAY_BUFFER, index, GL33C.GL_DYNAMIC_DRAW)
        //---------------------------------------------------------------------

        //GL33C.glBindBuffer(GL33C.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL33C.glBindVertexArray(0)
    }

    /**
     * renders the mesh
     */
    fun render(shaderProgram: ShaderProgram)
    {
        var drawMode = GL33C.GL_TRIANGLES

        //drawMode = GL33C.GL_LINE_LOOP
       // GL33C.glLineWidth(10f);

        GL33C.glBindVertexArray(vao)
        material?.bind(shaderProgram)
        GL33C.glDrawElements(drawMode, indexcount, GL33C.GL_UNSIGNED_INT, 0)
        GL33C.glBindVertexArray(0)
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (ibo != 0) GL15.glDeleteBuffers(ibo)
        if (vbo != 0) GL15.glDeleteBuffers(vbo)
        if (vao != 0) GL30.glDeleteVertexArrays(vao)
    }
}