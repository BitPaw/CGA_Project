package cga.exercise.components.geometry

import org.joml.*
import org.joml.Math.cos
import org.joml.Math.sin
import java.lang.Math.toRadians
import javax.swing.text.Position

open class Transformable(var modelMatrix: Matrix4f = Matrix4f(), var parent: Transformable? = null)
{
    /**
     * Rotates object around its own origin.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     */
    fun rotateLocal(pitch: Float, yaw: Float, roll: Float)
    {
        modelMatrix.rotateXYZ(pitch, yaw, roll)
    }
    fun rotateLocal(rotation : Vector3f)
    {
        modelMatrix.rotateXYZ(rotation.x, rotation.y, rotation.z)
    }


    /**
     * Rotates object around given rotation center.
     * @param pitch radiant angle around x-axis ccw
     * @param yaw radiant angle around y-axis ccw
     * @param roll radiant angle around z-axis ccw
     * @param altMidpoint rotation center
     */
    fun rotateAroundPoint(pitch: Float, yaw: Float, roll: Float, altMidpoint: Vector3f)
    {
        val currentPoition = getPosition();

        /*
        val revserseCurrentPoition = Vector3f(currentPoition).mul(-1f)
        val comaeraTurnAngle = Vector3f(pitch, yaw, roll)
        val cameraOffset =  Vector3f(currentPoition).sub(altMidpoint)

        cameraOffset.mul(comaeraTurnAngle)

        val newPosition = Vector3f(currentPoition).add(cameraOffset)
        */

        val rotation = Vector3f(pitch, yaw, roll)
        //val origin = Vector3f(altMidpoint.add(currentPoition))

        rotation.mul(0.1f)

        val quant = Quaternionf(rotation.x, rotation.y, rotation.z, 1f)


        modelMatrix.rotateAround(quant, currentPoition.x, currentPoition.y, currentPoition.z)




        //translateLocal(revserseCurrentPoition)
        //translateLocal(Vector3f(newPosition))

        //modelMatrix.lookAt(getWorldPosition(),getWorldPosition().sub(altMidpoint), getWorldYAxis())

       // modelMatrix(pitch)

      //  rotateLocal(Vector3f(pitch, 0f, roll))

       // [OK] translateLocal(Vector3f(pitch.toDouble().toFloat(), roll.toDouble().toFloat(), 0f))




    }

    /**
     * Translates object based on its own coordinate system.
     * @param deltaPos delta positions
     */
    fun translateLocal(deltaPos: Vector3f)
    {
        modelMatrix.translate(deltaPos) // M*T
    }

    fun translateLocal(x : Float, y : Float, z : Float)
    {
        translateLocal(Vector3f(x, y, z)) // M*T
    }

    /**
     * Translates object based on its parent coordinate system.
     * Hint: global operations will be left-multiplied
     * @param deltaPos delta positions (x, y, z)
     */
    fun translateGlobal(deltaPos: Vector3f) // T*M, fix
    {
        val hasParent = parent != null

        if(hasParent)
        {
            modelMatrix = parent!!.getLocalModelMatrix().translateLocal(deltaPos)
        }
        else
        {
            translateLocal(deltaPos)
        }
    }

    /**
     * Scales object related to its own origin
     * @param scale scale factor (x, y, z)
     */
    fun scaleLocal(scale: Vector3f)
    {
        modelMatrix.scale(scale)
    }

    fun scaleLocal(scaleFactor : Float)
    {
        scaleLocal(Vector3f(scaleFactor, scaleFactor, scaleFactor))
    }

    /**
     * Returns position based on aggregated translations.
     * Hint: last column of model matrix
     * @return position
     */
    fun getPosition(): Vector3f
    {
        val result = Vector3f()

        modelMatrix.getColumn(3,result)

        return result
    }

    /**
     * Returns position based on aggregated translations incl. parents.
     * Hint: last column of world model matrix
     * @return position
     */
    fun getWorldPosition(): Vector3f
    {
        val result = Vector3f()

        getWorldModelMatrix().getColumn(3,result)

        return result
    }

    /**
     * Returns x-axis of object coordinate system
     * Hint: first normalized column of model matrix
     * @return x-axis
     */
    fun getXAxis(): Vector3f
    {
        val result = Vector3f()

        modelMatrix.getColumn(0,result).normalize()

        return result
    }

    /**
     * Returns y-axis of object coordinate system
     * Hint: second normalized column of model matrix
     * @return y-axis
     */
    fun getYAxis(): Vector3f
    {
        val result = Vector3f()

        modelMatrix.getColumn(1,result).normalize()

        return result
    }

    /**
     * Returns z-axis of object coordinate system
     * Hint: third normalized column of model matrix
     * @return z-axis
     */
    fun getZAxis(): Vector3f
    {
        val result = Vector3f()

        modelMatrix.getColumn(2,result).normalize()

        return result
    }

    /**
     * Returns x-axis of world coordinate system
     * Hint: first normalized column of world model matrix
     * @return x-axis
     */
    fun getWorldXAxis(): Vector3f
    {
        val result = Vector3f()

        getWorldModelMatrix().getColumn(0,result).normalize()

        return result
    }

    /**
     * Returns y-axis of world coordinate system
     * Hint: second normalized column of world model matrix
     * @return y-axis
     */
    fun getWorldYAxis(): Vector3f
    {
        val result = Vector3f()

        getWorldModelMatrix().getColumn(1, result).normalize()

        return result
    }

    /**
     * Returns z-axis of world coordinate system
     * Hint: third normalized column of world model matrix
     * @return z-axis
     */
    fun getWorldZAxis(): Vector3f
    {
        val result = Vector3f()

        getWorldModelMatrix().getColumn(2, result).normalize()

        return result
    }

    /**
     * Returns multiplication of world and object model matrices.
     * Multiplication has to be recursive for all parents.
     * Hint: scene graph
     * @return world modelMatrix
     */
    fun getWorldModelMatrix(): Matrix4f
    {
        var matrix = Matrix4f()

        if(parent != null)
        {
            matrix = (parent!!.getWorldModelMatrix())
        }

        matrix.mul(modelMatrix)

        return matrix
    }

    /**
     * Returns object model matrix
     * @return modelMatrix
     */
    fun getLocalModelMatrix(): Matrix4f
    {
        return Matrix4f(modelMatrix)
    }

    override fun toString(): String
    {
        return "X:" + getPosition().x.toInt()  +
        "Y:" + getPosition().y.toInt()   +
        "Z:" + getPosition().z.toInt()
    }
}