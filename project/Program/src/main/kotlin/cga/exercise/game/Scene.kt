package cga.exercise.game

interface Scene
{
    fun render(dt: Float, t: Float)
    fun update(dt: Float, t: Float)
    fun onKey(key: Int, scancode: Int, action: Int, mode: Int)
    fun onMouseMove(xpos: Double, ypos: Double)
    fun cleanup()
}
