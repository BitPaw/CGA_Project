package TTT

import TTT.Event.PlayerPlaceEvent
import TTT.Event.PlayerTurnChangeEvent

class TTTGame(val callback : TTTGameListener)
{
    private val _gameField = GameBoard()
    private val _playerX = Player(PlayerSymbol.X)
    private val _playerO = Player(PlayerSymbol.O)

    var CurrentPlayerOnTurn = PlayerSymbol.X

    private fun PlayerTurnChange()
    {
        CurrentPlayerOnTurn = when(CurrentPlayerOnTurn)
        {
            PlayerSymbol.X ->  PlayerSymbol.O
            PlayerSymbol.O ->  PlayerSymbol.X
            PlayerSymbol.None ->  PlayerSymbol.X
        }
        val event = PlayerTurnChangeEvent(CurrentPlayerOnTurn)

        callback.OnPlayerTurnChangeEvent(event)
    }

    private fun CanPlayerUseNumber(strength : Int) : Boolean
    {
        return when(CurrentPlayerOnTurn)
        {
            PlayerSymbol.X -> _playerX.CanUse(strength)
            PlayerSymbol.O -> _playerO.CanUse(strength)
            PlayerSymbol.None -> false
        }
    }

    fun Start(width: Int, height: Int)
    {
        _gameField.Setup(width, height)
        callback.OnMatchBegin(width, height)
    }

    fun PlayerPlace(gameField: GameField) : PlayerPlaceResult
    {
        val isHisTurn = CurrentPlayerOnTurn == gameField.Symbol
        val playerPlaceResult : PlayerPlaceResult

        if(!isHisTurn)
        {
            playerPlaceResult = PlayerPlaceResult.NotYourTurn
        }
        else
        {
            val isInField = _gameField.IsInField(gameField.x, gameField.x)

            if(!isInField)
            {
                playerPlaceResult = PlayerPlaceResult.NotInField
            }
            else
            {
                val containedInField = _gameField.Get(gameField.x, gameField.y)
                val hasSomethingOnField = containedInField != null

                if(hasSomethingOnField)
                {
                    // There is already something, check it
                    val fieldStrength = containedInField!!.Strength
                    val isStronger = fieldStrength < gameField.Strength
                    val canPlayerUseIt = CanPlayerUseNumber(fieldStrength)

                    if(!canPlayerUseIt)
                    {
                        playerPlaceResult = PlayerPlaceResult.AlreadyUsed
                    }
                    else
                    {
                        if(isStronger)
                        {
                            // You can override now
                            playerPlaceResult = PlayerPlaceResult.SuccessfulOverride
                        }
                        else
                        {
                            // Your placement is nor strong enogh
                            playerPlaceResult =  PlayerPlaceResult.Occupied
                        }
                    }
                }
                else
                {
                    // Nothign here, just place it
                    playerPlaceResult =  PlayerPlaceResult.Successful
                }
            }


            //-----<Trigger Event Player Interact>----------
            val event = PlayerPlaceEvent(gameField, playerPlaceResult)

            callback.OnPlayerInteract(event)
            //-----------------------------------------------

            //------<Trigger Placement>---------------------
            if(playerPlaceResult == PlayerPlaceResult.Successful || playerPlaceResult == PlayerPlaceResult.SuccessfulOverride)
            {
                _gameField.Set(gameField)

                callback.OnPlayerPlace(gameField)

                PlayerTurnChange()
            }
            //-----------------------------------------------
        }

        return playerPlaceResult
    }
}