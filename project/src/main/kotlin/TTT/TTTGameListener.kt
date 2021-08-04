package TTT

import TTT.Event.*

interface TTTGameListener
{
    // Game
    fun OnGameStateChange(gameStateChange : GameStateChangeEvent)

    // Match
    fun OnMatchBegin(width : Int, height : Int)
    fun OnMatchEnd(matchEndEvent : MatchEndEvent)

    // Round
    fun OnRoundBegin()
    fun OnRoundEnd(roundEndEvent: RoundEndEvent)

    // Player
    fun OnPlayerInteract(playerPlaceEvent : PlayerPlaceEvent)
    fun OnPlayerTurnChangeEvent(playerTurnChangeEvent : PlayerTurnChangeEvent)
    fun OnPlayerPlace(gameField: GameField)
}