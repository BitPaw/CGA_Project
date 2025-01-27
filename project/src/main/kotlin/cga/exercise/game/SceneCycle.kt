package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.light.PointLight
import cga.exercise.components.light.SpotLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector2d
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11

class SceneCycle(private val window: GameWindow) : Scene
{
    private val staticShader: ShaderProgram = ShaderProgram("assets/shaders/tron_vert_light.glsl", "assets/shaders/tron_frag_light.glsl")

    private val currentCursor = Vector2d(0.0, 0.0)

    val tronCamera = TronCamera(aspectRatio = window.windowWidth.toFloat()/window.windowHeight.toFloat())
    //var sphere = loadObjectFromPath("assets/models/sphere.obj");

    val floor =  ModelLoader.loadModel("assets/models/ground.obj", 0f,0f,0f)!!;
    //val lightCycle =  ModelLoader.loadModel("assets/models/sphere.obj", 0f,0f,0f)!!;
    val lightCycle =  ModelLoader.loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj",
        Math.toRadians(-90f),
        Math.toRadians(90f),0f)!!;

    val pointLight = PointLight(
        Vector3f(0f,1f,0f),
        Vector3f(10000f,10000f,10000f),
        Vector3f(50f,50f, 50f)
    )
    val spotLight = SpotLight(
        Vector3f(0f,0f,0f),
        Vector3f(1f,10f,10f),
        Vector3f(0.5f,0.5f,0.5f),
        15f,
        30f
    );

    //scene setup
    init
    {
        //glClearColor(0.6f, 1.0f, 1.0f, 1.0f); GLError.checkThrow()
        GL11.glClearColor(0.10f, 0.10f, 0.10f, 1.0f); GLError.checkThrow()
        GL11.glEnable(GL11.GL_CULL_FACE); GLError.checkThrow()
        GL11.glFrontFace(GL11.GL_CCW); GLError.checkThrow()
        GL11.glCullFace(GL11.GL_BACK); GLError.checkThrow()
        GL11.glEnable(GL11.GL_DEPTH_TEST); GLError.checkThrow()
        GL11.glDepthFunc(GL11.GL_LESS); GLError.checkThrow()

        //pointLight.parent = lightCycle
        spotLight.parent = lightCycle
        tronCamera.parent = lightCycle // lightCycle->Camera


        staticShader.use()

        tronCamera.translateGlobal(Vector3f(0f, 2f, 5f))
        tronCamera.rotateLocal(Math.toRadians(-15f), 0f, 0f)

        spotLight.translateGlobal(Vector3f(0f,2f,-5f))
        spotLight.rotateLocal(Vector3f(Math.toRadians(-15f), Math.toRadians(130f),0f))

        // Load floor---------------------------
        floor.MeshList[0].material?.emit = Texture2D("assets/textures/ground_emit.png", true)
        floor.MeshList[0].material?.emit?.bind(0)
        floor.MeshList[0].material?.tcMultiplier = Vector2f(64f,64f)
        floor.MeshList[0].material?.color?.set(Vector3f(0f,1f,0f))

        floor.scaleLocal(2f)
        //floor.rotateLocal(90f, 0f,0f)
        // ----------------------------------



    }

    override fun render(dt: Float, t: Float)
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        tronCamera.bind(staticShader)
        pointLight.bind(staticShader, "???")
        spotLight.bind(staticShader, "???", tronCamera.getCalculateViewMatrix())

        floor.render(staticShader)
        lightCycle.render(staticShader)
    }

    override fun update(dt: Float, t: Float)
    {
        val moveForward = window.getKeyState(GLFW.GLFW_KEY_W) || window.getKeyState(GLFW.GLFW_KEY_UP)
        val moveLeft = window.getKeyState(GLFW.GLFW_KEY_LEFT)
        val moveRight =window.getKeyState(GLFW.GLFW_KEY_RIGHT)
        val moveBackwards = window.getKeyState(GLFW.GLFW_KEY_S) || window.getKeyState(GLFW.GLFW_KEY_DOWN)
        val rotateRight = window.getKeyState(GLFW.GLFW_KEY_D)
        val rotateLeft = window.getKeyState(GLFW.GLFW_KEY_A)
        val isSpacePressed = window.getKeyState(GLFW.GLFW_KEY_SPACE)
        val isLeftShiftPressed = window.getKeyState(GLFW.GLFW_KEY_LEFT_SHIFT)
        val movementspeed = 10f * dt
        val viewSpeed = 1f * dt
        val movementVector = Vector3f()
        val roationVector = Vector3f()
        val hasMovedLinar =  moveForward || moveBackwards || isLeftShiftPressed || isSpacePressed || moveLeft || moveRight
        val rotate = rotateLeft || rotateRight


        if(moveLeft)
            movementVector.set(-movementspeed,0f, 0f)

        if(moveRight)
            movementVector.set(movementspeed,0f, 0f)

        if(isSpacePressed)
            movementVector.set(0f,movementspeed, 0f)

        if(isLeftShiftPressed)
            movementVector.set(0f,-movementspeed, 0f)

        if(moveForward)
            movementVector.set(0f,0f, -movementspeed)

        if(moveBackwards)
            movementVector.set(0f,0f, movementspeed)

        if(rotateLeft)
            roationVector.set(0f,viewSpeed, 0f)

        if(rotateRight)
            roationVector.set(0f,-viewSpeed, 0f)

        if((hasMovedLinar && rotate) || hasMovedLinar)
        {
            lightCycle.rotateLocal(roationVector)
            lightCycle.translateGlobal(movementVector)
        }


        val oldColor = lightCycle.MeshList[0].material?.color

        oldColor?.x = oldColor?.x?.plus(Math.sin(t) * dt)?.rem(1f)
        oldColor?.y = oldColor?.y?.plus(Math.cos(t) * dt)?.rem(1f)
        oldColor?.z = oldColor?.z?.plus(Math.acos(t) * dt)?.rem(1f)

        lightCycle.MeshList[0].material?.color?.set(oldColor)
    }

    override  fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}
    override fun OnScroll(xoffset: Double, yoffset: Double)
    {
       // TODO("Not yet implemented")
    }

    override  fun onMouseMove(xpos: Double, ypos: Double)
    {
        val viewSpeed = 0.002
        val newCursorPosition = Vector2d(xpos, ypos)
        val oldCursorPosition = Vector2d(currentCursor)
        val difference = oldCursorPosition.sub(newCursorPosition)

        currentCursor.set(newCursorPosition)

        difference.mul(viewSpeed)

        tronCamera.rotateAroundPoint(0f, difference.x.toFloat(), 0f, lightCycle.getWorldPosition())
    }

    override fun onMouseButton(button: Int, action: Int, mode: Int)
    {
        // TODO("Not yet implemented")
    }

    override  fun cleanup() {}
}