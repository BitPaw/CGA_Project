package TTT

import TTT.Event.PlayerPlaceEvent
import TTT.Event.PlayerTurnChangeEvent

class TTTGame(val callback : TTTGameListener)
{
    private val _gameField = GameBoard()
    private val _playerX = Player(PlayerSymbol.X)
    private val _playerO = Player(PlayerSymbol.O)

    private var _currentPlayerOnTurn = PlayerSymbol.X

    val CurrentPlayerOnTurn = _currentPlayerOnTurn;

    private fun PlayerTurnChange()
    {
        _currentPlayerOnTurn = when(CurrentPlayerOnTurn)
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

    fun PlayerPlace(playerSymbol : PlayerSymbol, strength : Int, x : Int, y : Int) : PlayerPlaceResult
    {
        val placeable = Placeable(playerSymbol, strength)
        val isHisTurn = CurrentPlayerOnTurn == playerSymbol
        val playerPlaceResult : PlayerPlaceResult

        if(!isHisTurn)
        {
            playerPlaceResult = PlayerPlaceResult.NotYourTurn
        }
        else
        {
            val isInField = _gameField.IsInField(x, y)

            if(!isInField)
            {
                playerPlaceResult = PlayerPlaceResult.NotInField
            }
            else
            {
                val containedInField = _gameField.Get(x, y)
                val hasSomethingOnField = containedInField != null

                if(hasSomethingOnField)
                {
                    // There is already something, check it
                    val fieldStrength = containedInField!!.Strength
                    val isStronger = fieldStrength< strength
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
            val event = PlayerPlaceEvent(playerSymbol, playerPlaceResult, placeable)

            callback.OnPlayerInteract(event)
            //-----------------------------------------------

            //------<Trigger Placement>---------------------
            if(playerPlaceResult == PlayerPlaceResult.Successful || playerPlaceResult == PlayerPlaceResult.SuccessfulOverride)
            {
                _gameField.Set(x, y, placeable)

                callback.OnPlayerPlace(placeable)

                PlayerTurnChange()
            }
            //-----------------------------------------------
        }

        return playerPlaceResult
    }
}