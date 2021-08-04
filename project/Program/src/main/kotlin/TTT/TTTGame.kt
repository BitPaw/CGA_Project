package TTT

import TTT.Event.PlayerPlaceEvent
import TTT.Event.PlayerTurnChangeEvent
import TTT.Event.RoundEndEvent

class TTTGame(val callback : TTTGameListener)
{
    private val _gameField = GameBoard()
    private val _playerX = Player(PlayerSymbol.X)
    private val _playerO = Player(PlayerSymbol.O)

    private var _fieldsCounterCurrent = -1
    private var _fieldsCounterMaximal = -1

    private var _currentPlayerOnTurn = PlayerSymbol.None

    fun GetCurrentPlayerOnTurn() = _currentPlayerOnTurn

    private fun PlayerTurnChange()
    {
        _currentPlayerOnTurn = when(_currentPlayerOnTurn)
        {
            PlayerSymbol.X ->  PlayerSymbol.O
            PlayerSymbol.O ->  PlayerSymbol.X
            PlayerSymbol.None ->  PlayerSymbol.X
        }
        val event = PlayerTurnChangeEvent(_currentPlayerOnTurn)

        callback.OnPlayerTurnChangeEvent(event)
    }

    private fun CanPlayerUseNumber(strength : Int) : Boolean
    {
        return when(_currentPlayerOnTurn)
        {
            PlayerSymbol.X -> _playerX.CanUse(strength)
            PlayerSymbol.O -> _playerO.CanUse(strength)
            PlayerSymbol.None -> false
        }
    }

    fun Start(width: Int, height: Int)
    {
        _fieldsCounterMaximal = width * height

        _gameField.Setup(width, height)

        callback.OnMatchBegin(width, height)

       RoundStart()
        PlayerTurnChange()
    }

    private fun CheckWinningConsition() : RoundEndEvent
    {
        if(_fieldsCounterCurrent == _fieldsCounterMaximal)
        {
            return RoundEndEvent(RoundResult.Draw)
        }

        val affectedFields = _gameField.GetWinningFields()
        val hasPlayerWon = affectedFields.isNotEmpty()

        if(hasPlayerWon)
        {
            return RoundEndEvent(RoundResult.WinInARaw, affectedFields)
        }

        return RoundEndEvent(RoundResult.None)
    }

    private fun RoundStart()
    {
        _fieldsCounterCurrent = 0

        _gameField.Reset()

        callback.OnRoundBegin()
    }

    private fun RoundEnd(roundEndEvent : RoundEndEvent)
    {
        callback.OnRoundEnd(roundEndEvent)

        RoundStart()
    }

    fun PlayerPlace(gameField: GameField) : PlayerPlaceResult
    {
        val isHisTurn = _currentPlayerOnTurn == gameField.Symbol
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
                            // Your placement is nor strong enough
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

                _fieldsCounterCurrent++

                val result = CheckWinningConsition()

                when(result.RoundResult)
                {
                    RoundResult.None -> PlayerTurnChange()
                    RoundResult.Draw -> RoundEnd(result)
                    RoundResult.WinInARaw -> RoundEnd(result)
                }
            }
            //-----------------------------------------------
        }

        return playerPlaceResult
    }
}