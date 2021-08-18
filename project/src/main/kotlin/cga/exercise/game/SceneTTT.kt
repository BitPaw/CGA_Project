package cga.exercise.game

import Resource.Pillar
import Resource.PlayerMoveMenu
import Resource.Skybox
import Resource.SpriteFont.SpriteFont
import Resource.Text
import TTT.*
import TTT.Event.*
import cga.exercise.components.camera.TTTCamera
import cga.exercise.components.geometry.Material
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.framework.GLError
import cga.framework.GameWindow
import cga.framework.ModelLoader
import org.joml.*
import org.joml.Math.toRadians
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL33C

class SceneTTT(private val window: GameWindow) : Scene, TTTGameListener
{
    //-----<Variables>--------------------------------------------------------------------------------------------------
    private val _game = TTTGame(this)
    private val _camera = TTTCamera(90f, window.windowWidth.toFloat()/window.windowHeight.toFloat())

    private val cube =  ModelLoader.loadModel("assets/TTT/Model/Pillar.obj", 0f, toRadians(0f),0f)!!
    private val _blockPlaceable = ModelLoader.loadModel("assets/TTT/Model/Placeable.obj", 0f, toRadians(0f),0f)!!
    private val rectangle = ModelLoader.loadModel("assets/TTT/Model/Recangle.obj", 0f, toRadians(0f),0f)!!

    private val missingTexture =  Texture2D("assets/TTT/Texture/MissingTexture.png", true)
    private val brickTexture = Texture2D("assets/TTT/Texture/Brick.png", true)
    private val _crossTexture = Texture2D("assets/TTT/Texture/Cross.png", true)

    private val shaderHUD: ShaderProgram = ShaderProgram("assets/TTT/Shader/HUD_Vertex.glsl", "assets/TTT/Shader/HUD_Fragment.glsl")
    private val shaderWorld: ShaderProgram = ShaderProgram("assets/TTT/Shader/WorldShader_Vertex.glsl", "assets/TTT/Shader/WorldShader_Fragment.glsl")

    private val _cross = Renderable(rectangle.MeshList)

    private val mousePositionCurrent = Vector2d(0.0, 0.0)
    private val mousePositionDelta = Vector2d(0.0, 0.0)
    //------------------------------------------------------------------------------------------------------------------

    //-----<Map>--------------------------------------------------------------------------------------------------------
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

    private val fieldList = mutableListOf<Pillar>()
    //------------------------------------------------------------------------------------------------------------------

    //-----<UI>---------------------------------------------------------------------------------------------------------
    private val _font = SpriteFont("assets/TTT/Font/segoe.fnt")
    private val _score = Text()
    private val _currentTurn = Text()
    //------------------------------------------------------------------------------------------------------------------

    //-----<Menu>----------------------------------------------------------------------------------------------
    private val _x0 = Texture2D("assets/TTT/Texture/X.png", true, false)
    private val _x1 = Texture2D("assets/TTT/Texture/X_1.png", true, false)
    private val _x2 = Texture2D("assets/TTT/Texture/X_2.png", true, false)
    private val _x3 = Texture2D("assets/TTT/Texture/X_3.png", true, false)
    private val _x4 = Texture2D("assets/TTT/Texture/X_4.png", true, false)
    private val _x5 = Texture2D("assets/TTT/Texture/X_5.png", true, false)
    private val _x6 = Texture2D("assets/TTT/Texture/X_6.png", true, false)
    private val _playerMenuLeft = PlayerMoveMenu(rectangle, _x0, _x1, _x2, _x3, _x4, _x5, _x6)

    private val _o0 = Texture2D("assets/TTT/Texture/O.png", true, false)
    private val _o1 = Texture2D("assets/TTT/Texture/O_1.png", true, false)
    private val _o2 = Texture2D("assets/TTT/Texture/O_2.png", true, false)
    private val _o3 = Texture2D("assets/TTT/Texture/O_3.png", true, false)
    private val _o4 = Texture2D("assets/TTT/Texture/O_4.png", true, false)
    private val _o5 = Texture2D("assets/TTT/Texture/O_5.png", true, false)
    private val _o6 = Texture2D("assets/TTT/Texture/O_6.png", true, false)
    private val _playerMenuRight = PlayerMoveMenu(rectangle, _o0, _o1, _o2, _o3, _o4, _o5, _o6)
    //------------------------------------------------------------------------------------------------------------------


    init
    {
        _game.Start(3,3, 6)

        GL11.glClearColor(0.6f, 0.6f, 0.6f, 1.0f); GLError.checkThrow()
        //GL11.glClearColor(0.10f, 0.10f, 0.10f, 1.0f); GLError.checkThrow()
        GL11.glEnable(GL11.GL_CULL_FACE); GLError.checkThrow()
        GL11.glFrontFace(GL11.GL_CCW); GLError.checkThrow()
        GL11.glCullFace(GL11.GL_BACK); GLError.checkThrow()
        GL11.glEnable(GL11.GL_DEPTH_TEST); GLError.checkThrow()
        GL11.glDepthFunc(GL11.GL_LESS); GLError.checkThrow()

        //GL33C.glActiveTexture(0);

        GL11.glPointSize(80f);
        GL11.glLineWidth(10f);


       // _score.modelMatrix.scale(1f)
        _score.translateLocal(-0.65f, 0.6f, -1f)
        _score.scaleLocal(0.5f)
        _score.FontSet(_font)
        _score.TextSet("Fight Ligma")

        _playerMenuLeft.Move(-5.0f, -4.5f, 0f)
        _playerMenuRight.Move(3.5f, 3.5f, 0f)

        _currentTurn.translateLocal(0.3f, -0.6f, -1f)
        _currentTurn.scaleLocal(0.5f)
        _currentTurn.FontSet(_font)
        _currentTurn.TextSet("Turn:")


        cube.MeshList[0].material.emit = brickTexture

        rectangle.translateLocal(Vector3f(-1f, -1f, -1f))
        rectangle.scaleLocal(0.3f);
        //rectangle.meshList[0].RenderMode = GL33C.GL_QUADS

        _camera.translateGlobal(Vector3f(0f, 3f, 6f))
        _camera.rotateLocal(-35f, 0f, 0f)

        //cube.translateLocal(0.5f,0.5f,0.5f)
        _skybox.Cube?.scaleLocal(100f);


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

        fieldList.forEach{element ->
            val texture = TextureLookUp(element.Field.Symbol, element.Field.Strength)

            element.Render(shaderWorld, texture)
        }
        //_score.render(shaderWorld)
        //--------------------------------------------------------------------------------------------------------------

        //-----<HUD>----------------------------------------------------------------------------------------------------
        shaderHUD.use()
        _camera.ThirdDimension = false
        _camera.IgnoreTranslation = true
        _camera.bind(shaderHUD)
        _cross.MeshList[0].material.color.set(1f, 1f, 1f)
        _cross.MeshList[0].material.emit = _crossTexture
        _cross.render(shaderHUD)
        _score.render(shaderHUD)
        _currentTurn.render(shaderHUD)
        _playerMenuLeft.Render(shaderHUD)
        _playerMenuRight.Render(shaderHUD)
        //--------------------------------------------------------------------------------------------------------------

        println(DoISeeThat(fieldList))
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

        // needs to be adjusted works for now
        if(window.getKeyState(GLFW.GLFW_KEY_Q))
            if(((t/dt).toInt()%4)==0)
            {
                _camera.translateGlobal(Vector3f(movementspeed,0f,0f))
            }
            else
            {
                _camera.translateGlobal(Vector3f(-2*movementspeed,0f,0f))
            }
    }

    override fun onKey(key: Int, scancode: Int, action: Int, mode: Int)
    {

        if(GLFW.GLFW_PRESS == action) // OnPressDown
        {
            val field = Vector2i(-1, -1)

            when(key)
            {
                GLFW.GLFW_KEY_KP_1 -> field.set(0,0)
                GLFW.GLFW_KEY_KP_2 -> field.set(1,0)
                GLFW.GLFW_KEY_KP_3 -> field.set(2,0)
                GLFW.GLFW_KEY_KP_4 -> field.set(0,1)
                GLFW.GLFW_KEY_KP_5 -> field.set(1,1)
                GLFW.GLFW_KEY_KP_6 -> field.set(2,1)
                GLFW.GLFW_KEY_KP_7 -> field.set(0,2)
                GLFW.GLFW_KEY_KP_8 -> field.set(1,2)
                GLFW.GLFW_KEY_KP_9 -> field.set(2,2)
            }

            if(field != Vector2i(-1, -1))
            {
                val playerMenu = GetCurrentPlayerMenu()
                val gameField = GameField(field.x, field.y, _game.GetCurrentPlayerOnTurn(), playerMenu.CurrentlySelected)

                _game.PlayerPlace(gameField)
            }
        }
    }

    private fun GetCurrentPlayerMenu() : PlayerMoveMenu
    {
        return when(_game.GetCurrentPlayerOnTurn())
        {
            PlayerSymbol.None -> TODO()
            PlayerSymbol.X -> _playerMenuLeft
            PlayerSymbol.O -> _playerMenuRight
        }
    }

    private fun RayCastAtCenter() : Vector3f
    {
        val view = Vector3f()
        _camera.getCalculateViewMatrix().getRow(2,view)
        /*val clipcords = Vector4f(0f,0f,-1f,1f)
        val invertedPro = Matrix4f().invert(_camera.getCalculateProjectionMatrix())
        val transformadPro = invertedPro.transform(clipcords)
        val transformatedPro = Vector4f(Vector2f(transformadPro.x,transformadPro.y),-1f,0f)
        val invertedView = Matrix4f().invert(_camera.getCalculateViewMatrix())
        val transformatedView = invertedView.transform(transformatedPro)
        val worldray = Vector3f(transformatedView.x, transformatedView.y,transformatedView.z)
        return worldray*/
        return view
    }

    private fun DoISeeThat(checklist:MutableList<Pillar>): Vector3f {
        val whatISee = RayCastAtCenter()

        var cords = Vector3f()

        checklist.forEach { element ->
            if (InBounds(whatISee,element.BlockObject.getWorldPosition(),Vector3i(2,10,2))) {
                cords = element.BlockObject.getWorldPosition()
                element.BlockObject.MeshList.forEach { it ->
                    it.material.color = Vector3f(0.5f, 0.5f, 0.5f)
                }
            } else if (InBounds(whatISee, element.PillarObject.getWorldPosition(),Vector3i(2,10,2))) {
                cords = element.PillarObject.getWorldPosition()
                element.PillarObject.MeshList.forEach { it ->
                    it.material.color = Vector3f(0.5f, 0.5f, 0.5f)
                }
            }
        }
        return cords
    }

    private fun InBounds(origin:Vector3f,target:Vector3f,range:Vector3i):Boolean
    {
        return ((origin.x<=target.x+range.x)&&(origin.x>=target.x-range.x))&&((origin.y<=target.y+range.y)&&(origin.y>=target.y-range.y))&&((origin.z<=target.z+range.z)&&(origin.z>=target.z-range.z))
    }

    override fun OnScroll(xoffset: Double, yoffset: Double)
    {
        val playerMenu = GetCurrentPlayerMenu()

        if(yoffset > 0)
        {
            playerMenu.SelectUp()
        }
        else
        {
            playerMenu.SelectDown()
        }
    }

    override fun onMouseMove(xpos: Double, ypos: Double)
    {
        val firstMove = mousePositionDelta.x == -1.0 && mousePositionDelta.y == -1.0
        val newPos = Vector2d(xpos, ypos)
        val viewSpeed = 0.3f

        mousePositionDelta.set(newPos)
        mousePositionDelta.sub(mousePositionCurrent)

        mousePositionCurrent.set(newPos)

        if(!firstMove)
        {
            val viewMoveVector = Vector2f(mousePositionDelta.x.toFloat(), mousePositionDelta.y.toFloat())

            viewMoveVector.mul(-viewSpeed)

            _camera.rotateLocalCappedYZ(viewMoveVector.y, viewMoveVector.x, 0f)
        }
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

    override fun OnRoundBegin()
    {
        _score.TextSet("Move: New Round")

        _playerMenuLeft.ElementResetAll()
        _playerMenuRight.ElementResetAll()

        fieldList.forEach{element -> element.Field.Reset() }
    }

    override fun OnRoundEnd(roundEndEvent: RoundEndEvent)
    {
        _score.TextSet(roundEndEvent.RoundResult.toString())
    }

    override fun OnMatchBegin(width : Int, height : Int)
    {
        println("[Event] Match begin> Width:$width Height:$height")

        //-----<Build Field>--------------------------------------------------------------------------------------------
        val gapBetween = 3.5f
        val offset = Vector3f(width/2f, 0f, height/2f)

        for (y in 0 until height)
        {
            for (x in 0 until width)
            {
                val newObject = Renderable(cube.MeshList)
                val newBlock =  Renderable(_blockPlaceable.MeshList)
                val newPosition = Vector3f(x.toFloat() * gapBetween - offset.x, 0f, y.toFloat() * gapBetween - offset.y)
                val gameField = GameField(x, y)

                val rr = Pillar(gameField, newObject,newBlock)

                newObject.translateLocal(newPosition)
                newBlock.translateLocal(newPosition)
                newBlock.translateLocal(0f, 3.5f, 0f)
                newBlock.rotateLocal(180f, 180f, 0f)

                fieldList.add(rr)
            }
        }
        //------------------------------------------------------------------------------------------------------------------
    }

    override fun OnPlayerInteract(playerPlaceEvent: PlayerPlaceEvent)
    {
        _score.TextSet("Move: " + playerPlaceEvent.result.toString())

        /*

        when(playerPlaceEvent.result)
        {
            PlayerPlaceResult.InvalidAction -> TODO()
            PlayerPlaceResult.Successful -> TODO()
            PlayerPlaceResult.SuccessfulOverride -> TODO()
            PlayerPlaceResult.NotYourTurn -> TODO()
            PlayerPlaceResult.NotInField -> TODO()
            PlayerPlaceResult.Occupied -> TODO()
            PlayerPlaceResult.AlreadyUsed -> TODO()
        }

         */
    }

    override fun OnPlayerTurnChangeEvent(playerTurnChangeEvent: PlayerTurnChangeEvent)
    {
        _currentTurn.TextSet("Turn: " + playerTurnChangeEvent.playerSymbol.toString())

        when(playerTurnChangeEvent.playerSymbol)
        {
            PlayerSymbol.None -> TODO()
            PlayerSymbol.X ->
            {
                _playerMenuLeft.SetActive(true)
                _playerMenuRight.SetActive( false)
            }
            PlayerSymbol.O ->
            {
                _playerMenuLeft.SetActive( false)
                 _playerMenuRight.SetActive(true)

            }
        }
    }

    private fun TextureLookUp(symbol: PlayerSymbol, strength : Int) : Texture2D?
    {
        return when(symbol)
        {
            PlayerSymbol.None -> null
            PlayerSymbol.X ->
            {
                when(strength)
                {
                    0-> null
                    1-> _x1
                    2-> _x2
                    3-> _x3
                    4-> _x4
                    5-> _x5
                    6-> _x6
                    else -> throw Exception("$strength")
                }
            }
            PlayerSymbol.O ->
            {
                when(strength)
                {
                    0-> null
                    1-> _o1
                    2-> _o2
                    3-> _o3
                    4-> _o4
                    5-> _o5
                    6-> _o6
                    else -> throw Exception("$strength")
                }
            }
        }
    }

    override fun OnPlayerPlace(gameField: GameField)
    {
        val index = gameField.x + (gameField.y * 3)

        fieldList[index].Field.Symbol = gameField.Symbol
        fieldList[index].Field.Strength = gameField.Strength

        when(gameField.Symbol)
        {
            PlayerSymbol.None -> TODO()
            PlayerSymbol.X -> _playerMenuLeft.ElementLock(gameField.Strength)
            PlayerSymbol.O -> _playerMenuRight.ElementLock(gameField.Strength)
        }
    }

    //------------------------------------------------------------------------------------------------------------------

}