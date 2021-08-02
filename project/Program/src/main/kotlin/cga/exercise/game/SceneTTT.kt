package cga.exercise.game

import Resource.Skybox
import TTT.*
import TTT.Event.GameStateChangeEvent
import TTT.Event.MatchEndEvent
import TTT.Event.PlayerPlaceEvent
import TTT.Event.PlayerTurnChangeEvent
import cga.exercise.components.camera.TTTCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.Math.toRadians
import org.joml.Vector2d
import org.joml.Vector2f
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
    private val rectangle = ModelLoader.loadModel("assets/TTT/Model/Recangle.obj", 0f, toRadians(0f),0f)!!;

    // Actual, Scene
    private val fieldList = mutableListOf<Renderable>()

    private val missingTexture =  Texture2D("assets/TTT/Texture/MissingTexture.png", true)
    private val brickTexture = Texture2D("assets/TTT/Texture/Brick.png", true)
    private val playerX = Texture2D("assets/TTT/Texture/X.png", true)
    private val playerO = Texture2D("assets/TTT/Texture/O.png", true)

    private val _cross = Renderable(rectangle.MeshList)

    private val _skybox = Skybox(
        "assets/TTT/Shader/SkyBox_Vertex.glsl",
        "assets/TTT/Shader/SkyBox_Fragment.glsl",
        "assets/TTT/Model/Cube.obj",
        "assets/TTT/Texture/SkyBox/Right.png",
        "assets/TTT/Texture/SkyBox/Left.png",
        "assets/TTT/Texture/SkyBox/Top.png",
        "assets/TTT/Texture/SkyBox/Bottom.png",
        "assets/TTT/Texture/SkyBox/Back.png",
        "assets/TTT/Texture/SkyBox/Front.png"
    )

    init
    {
        _game.Start(3,3)

        GL11.glClearColor(0.6f, 0.6f, 0.6f, 1.0f); GLError.checkThrow()
        //GL11.glClearColor(0.10f, 0.10f, 0.10f, 1.0f); GLError.checkThrow()
        GL11.glEnable(GL11.GL_CULL_FACE); GLError.checkThrow()
        GL11.glFrontFace(GL11.GL_CCW); GLError.checkThrow()
        GL11.glCullFace(GL11.GL_BACK); GLError.checkThrow()
        GL11.glEnable(GL11.GL_DEPTH_TEST); GLError.checkThrow()
        GL11.glDepthFunc(GL11.GL_LESS); GLError.checkThrow()

        //GL33C.glActiveTexture(0);

        GL11.glPointSize(40f);
        GL11.glLineWidth(5f);


        rectangle.translateLocal(Vector3f(-1f, -1f, -1f))
        rectangle.scaleLocal(0.3f);
        //rectangle.meshList[0].RenderMode = GL33C.GL_QUADS
        rectangle.MeshList[0].material?.emit = Texture2D("assets/TTT/Texture/X.png", true)

        _camera.translateGlobal(Vector3f(0f, 3f, 6f))
        _camera.rotateLocal(toRadians(-35f), 0f, 0f)

        //cube.translateLocal(0.5f,0.5f,0.5f)
        _skybox.Cube?.scaleLocal(100f);


        _cross.MeshList[0].material?.emit = Texture2D("assets/TTT/Texture/Cross.png", true)
        _cross.scaleLocal(00.1f)
        _cross.translateLocal(-0.5f, -0.5f, -2f)
    }

    //-----<Engine internal>--------------------------------------------------------------------------------------------

    override fun render(dt: Float, t: Float)
    {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT)

        //-----<Skybox>-------------------------------------------------------------------------------------------------
        GL33C.glDepthMask(false)
        GL11.glFrontFace(GL11.GL_CW)
        _skybox.Shader.use()
        _camera.ThirdDimension = true;
        //_camera.IgnoreTranslation = true
        _camera.bind(_skybox.Shader)
        _skybox.Texture.bind(0)
        //_skybox.Cube?.meshList?.get(0)!!.RenderMode = GL11.GL_POINTS
        //_skybox.Cube?.render(_skybox.Shader)
        //_skybox.Cube?.meshList?.get(0)!!.RenderMode = GL11.GL_TRIANGLES
        _skybox.Cube?.render(_skybox.Shader)
        GL11.glFrontFace(GL11.GL_CCW)
        GL33C.glDepthMask(true)
        //--------------------------------------------------------------------------------------------------------------

        //-----<WORLD>--------------------------------------------------------------------------------------------------
        shaderWorld.use()
        _camera.ThirdDimension = true
        _camera.IgnoreTranslation = false
        _camera.bind(shaderWorld)
        fieldList.forEach{element -> element.render(shaderWorld)}
        //--------------------------------------------------------------------------------------------------------------

        //-----<HUD>----------------------------------------------------------------------------------------------------
        shaderHUD.use()
        _camera.ThirdDimension = false
        _camera.IgnoreTranslation = true
        _camera.bind(shaderHUD)
        rectangle.render(shaderHUD)
        _cross.render(shaderHUD)
        //--------------------------------------------------------------------------------------------------------------
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

        if((rotate) || hasMovedLinar)
        {
           // _camera.rotateLocal(roationVector)
            _camera.translateGlobal(movementVector)
        }



        val gamefield = HandleFieldInput()




    }

    fun HandleFieldInput()
    {
        val field1 = window.getKeyState(GLFW.GLFW_KEY_KP_1)
        val field2 = window.getKeyState(GLFW.GLFW_KEY_KP_2)
        val field3 = window.getKeyState(GLFW.GLFW_KEY_KP_3)
        val field4 = window.getKeyState(GLFW.GLFW_KEY_KP_4)
        val field5 = window.getKeyState(GLFW.GLFW_KEY_KP_5)
        val field6 = window.getKeyState(GLFW.GLFW_KEY_KP_6)
        val field7 = window.getKeyState(GLFW.GLFW_KEY_KP_7)
        val field8 = window.getKeyState(GLFW.GLFW_KEY_KP_8)
        val field9 = window.getKeyState(GLFW.GLFW_KEY_KP_9)

        var x = -1
        var y = -1

        if(field1)
        {
            x = 0
            y = 0
        }
        else if(field2)
        {
            x = 1
            y = 0
        }
        else if(field3)
        {
            x = 2
            y = 0
        }
        else if(field4)
        {
            x = 0
            y = 1
        }
        else if(field5)
        {
            x = 1
            y = 1
        }
        else if(field6)
        {
            x = 2
            y = 1
        }
        else if(field7)
        {
            x = 0
            y = 2
        }
        else if(field8)
        {
            x = 1
            y = 2
        }
        else if(field9)
        {
            x = 2
            y = 2
        }

        if(x != -1 && y != -1)
        {
            val gameField = GameField(x, y, _game.CurrentPlayerOnTurn, 10)

            _game.PlayerPlace(gameField)
        }
    }

    override fun onKey(key: Int, scancode: Int, action: Int, mode: Int)
    {
        // Todo: Add UserInput
    }

    val mousePositionCurrent = Vector2d(0.0, 0.0)
    val mousePositionDelta = Vector2d(0.0, 0.0)

    override fun onMouseMove(xpos: Double, ypos: Double)
    {
        val newPos = Vector2d(xpos, ypos)

        mousePositionDelta.set(newPos)
        mousePositionDelta.sub(mousePositionCurrent)

        mousePositionCurrent.set(newPos)

        val viewMoveVector = Vector2f(mousePositionDelta.x.toFloat(), mousePositionDelta.y.toFloat())

        viewMoveVector.mul(-0.005f)

        _camera.rotateLocalCappedYZ(viewMoveVector.y, viewMoveVector.x, 0f)
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
        val gapBetween = 2.5f
        val offset = Vector3f(width/2f, 0f, height/2f)

        for (y in 0 until height)
        {
            for (x in 0 until width)
            {
                val newObject = Renderable(cube.MeshList)
                val newPosition = Vector3f(x.toFloat() * gapBetween - offset.x, 0f, y.toFloat() * gapBetween - offset.y)

                newObject.translateLocal(newPosition)


                newObject.MeshList.forEach{element ->  element.material = Material(brickTexture) }

                fieldList.add(newObject)
            }
        }
        //------------------------------------------------------------------------------------------------------------------
    }

    private fun SetBlockOwnerShip(gameField: GameField)
    {
        val index = gameField.x + (gameField.y + 2)

        val texture = when(gameField.Symbol)
        {
            PlayerSymbol.None -> throw Exception()
            PlayerSymbol.X -> playerX
            PlayerSymbol.O -> playerO
        }

        fieldList[index].MeshList[0].material?.emit = texture
    }

    override fun OnPlayerInteract(playerPlaceEvent: PlayerPlaceEvent)
    {
        when(playerPlaceEvent.result)
        {
            PlayerPlaceResult.InvalidAction -> TODO()
            PlayerPlaceResult.Successful -> SetBlockOwnerShip(playerPlaceEvent.gameField)
            PlayerPlaceResult.SuccessfulOverride -> SetBlockOwnerShip(playerPlaceEvent.gameField)
            PlayerPlaceResult.NotYourTurn -> TODO()
            PlayerPlaceResult.NotInField -> TODO()
            PlayerPlaceResult.Occupied -> SetBlockOwnerShip(playerPlaceEvent.gameField)
            PlayerPlaceResult.AlreadyUsed -> SetBlockOwnerShip(playerPlaceEvent.gameField)
        }
    }

    override fun OnPlayerTurnChangeEvent(playerTurnChangeEvent: PlayerTurnChangeEvent)
    {

    }

    override fun OnPlayerPlace(gameField: GameField)
    {

    }

    //------------------------------------------------------------------------------------------------------------------
}