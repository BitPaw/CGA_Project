package cga.exercise.game

interface Scene
{
    fun render(dt: Float, t: Float)
    fun update(dt: Float, t: Float)
    fun onKey(key: Int, scancode: Int, action: Int, mode: Int)
    fun OnScroll(xoffset : Double, yoffset : Double)
    fun onMouseMove(xpos: Double, ypos: Double)
    fun onMouseButton(button: Int, action: Int, mode: Int)
    fun cleanup()
}
