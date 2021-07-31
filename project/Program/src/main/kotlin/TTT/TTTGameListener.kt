package TTT

import TTT.Event.*

interface TTTGameListener
{
    // Game
    fun OnGameStateChange(gameStateChange : GameStateChangeEvent)
    fun OnMatchEnd(matchEndEvent : MatchEndEvent)
    fun OnMatchBegin(width : Int, height : Int)

    // Player
    fun OnPlayerInteract(playerPlaceEvent : PlayerPlaceEvent)
    fun OnPlayerTurnChangeEvent(playerTurnChangeEvent : PlayerTurnChangeEvent)
    fun OnPlayerPlace(placeable: Placeable)
}