package cga.exercise.game

import TTT.Event.GameStateChangeEvent
import TTT.Event.MatchEndEvent
import TTT.Event.PlayerPlaceEvent
import TTT.Event.PlayerTurnChangeEvent
import TTT.Placeable
import TTT.TTTGame
import TTT.TTTGameListener
import cga.framework.GameWindow

class SceneTTT(private val window: GameWindow) : Scene, TTTGameListener
{
    private val _game = TTTGame(this)

    init
    {
        _game.Start(3,3)
    }

    //-----<Engine internal>--------------------------------------------------------------------------------------------

    override fun render(dt: Float, t: Float)
    {
        TODO("Not yet implemented")
    }

    override fun update(dt: Float, t: Float)
    {
        TODO("Not yet implemented")
    }

    override fun onKey(key: Int, scancode: Int, action: Int, mode: Int)
    {
        TODO("Not yet implemented")
    }

    override fun onMouseMove(xpos: Double, ypos: Double)
    {
        TODO("Not yet implemented")
    }

    override fun cleanup()
    {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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