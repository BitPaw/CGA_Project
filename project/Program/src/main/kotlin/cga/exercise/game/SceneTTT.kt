package cga.exercise.game

import TTT.Event.GameStateChangeEvent
import TTT.Event.MatchEndEvent
import TTT.Event.PlayerPlaceEvent
import TTT.Event.PlayerTurnChangeEvent
import TTT.Placeable
import TTT.TTTGame
import TTT.TTTGameListener
import cga.exercise.components.camera.TTTCamera
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Math.toRadians
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL33C

class SceneTTT(private val window: GameWindow) : Scene, TTTGameListener
{
    private val _game = TTTGame(this)
    private val _camera = TTTCamera(90f, window.windowWidth.toFloat()/window.windowHeight.toFloat())

    private val shaderHUD: ShaderProgram = ShaderProgram("assets/TTT/Shader/HUD_Vertex.glsl", "assets/TTT/Shader/HUD_Fragment.glsl")
    private val shaderWorld: ShaderProgram = ShaderProgram("assets/TTT/Shader/WorldShader_Vertex.glsl", "assets/TTT/Shader/WorldShader_Fragment.glsl")

    private val cube =  ModelLoader.loadModel("assets/TTT/Model/Cube.obj", 0f, toRadians(0f),0f)!!;
    private val rectangle  =ModelLoader.loadModel("assets/TTT/Model/Recangle.obj", 0f, toRadians(0f),0f)!!;

    // Actual, Scene
    private val fieldList = mutableListOf<Renderable>()



    init
    {
        _game.Start(3,3)

        //glClearColor(0.6f, 1.0f, 1.0f, 1.0f); GLError.checkThrow()
        GL11.glClearColor(0.10f, 0.10f, 0.10f, 1.0f); GLError.checkThrow()
        GL11.glEnable(GL11.GL_CULL_FACE); GLError.checkThrow()
        GL11.glFrontFace(GL11.GL_CCW); GLError.checkThrow()
        GL11.glCullFace(GL11.GL_BACK); GLError.checkThrow()
        GL11.glEnable(GL11.GL_DEPTH_TEST); GLError.checkThrow()
        GL11.glDepthFunc(GL11.GL_LESS); GLError.checkThrow()

        rectangle.translateLocal(Vector3f(-1f, -1f, -1f))
        rectangle.scaleLocal(0.3f);
        //rectangle.meshList[0].RenderMode = GL33C.GL_QUADS
       // rectangle.meshList[0].material?.emit = Texture2D("assets/TTT/Texture/X.png", true)

        _camera.translateGlobal(Vector3f(0f, 3f, 6f))
        _camera.rotateLocal(toRadians(-35f), 0f, 0f)
    }

    //-----<Engine internal>--------------------------------------------------------------------------------------------

    override fun render(dt: Float, t: Float)
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        //-----<Skybox>-------------------------------------------------------------------------------------------------
        // Todo: Add Skybox
        //--------------------------------------------------------------------------------------------------------------

        //-----<WORLD>--------------------------------------------------------------------------------------------------
        shaderWorld.use()
        _camera.ThirdDimension = true
        _camera.bind(shaderWorld)
        fieldList.forEach{element -> element.render(shaderWorld)}
        //--------------------------------------------------------------------------------------------------------------

        //-----<HUD>----------------------------------------------------------------------------------------------------
        shaderHUD.use()
        _camera.ThirdDimension = false;
        _camera.bind(shaderHUD)
        rectangle.render(shaderHUD)
        //--------------------------------------------------------------------------------------------------------------
    }

    override fun update(dt: Float, t: Float)
    {

    }

    override fun onKey(key: Int, scancode: Int, action: Int, mode: Int)
    {
        // Todo: Add UserInput
    }

    override fun onMouseMove(xpos: Double, ypos: Double)
    {
        // Todo: Add UserInput
    }

    override fun cleanup()
    {

    }

    //------------------------------------------------------------------------------------------------------------------

    //-----<Game Events>------------------------------------------------------------------------------------------------

    override fun OnGameStateChange(gameStateChange: GameStateChangeEvent)
    {
        TODO("Not yet implemented")
    }

    override fun OnMatchEnd(matchEndEvent: MatchEndEvent)
    {
        TODO("Not yet implemented")
    }

    override fun OnMatchBegin(width : Int, height : Int)
    {
        println("<Event: Match begin> Width:$width Height:$height")

        //-----<Build Field>--------------------------------------------------------------------------------------------
        val gapBetween = 1.5f
        val offset = Vector3f(width/2f, 0f, height/2f)

        for (y in 0 until height)
        {
            for (x in 0 until width)
            {
                val newObject = Renderable(cube.meshList)
                val newPosition = Vector3f(x.toFloat() * gapBetween - offset.x, 0f, y.toFloat() * gapBetween - offset.y)

                newObject.translateLocal(newPosition)

                fieldList.add(newObject)
            }
        }
        //------------------------------------------------------------------------------------------------------------------
    }

    override fun OnPlayerInteract(playerPlaceEvent: PlayerPlaceEvent)
    {
        TODO("Not yet implemented")
    }

    override fun OnPlayerTurnChangeEvent(playerTurnChangeEvent: PlayerTurnChangeEvent)
    {
        TODO("Not yet implemented")
    }

    override fun OnPlayerPlace(placeable: Placeable)
    {
        TODO("Not yet implemented")
    }

    //------------------------------------------------------------------------------------------------------------------
}