package TTT

class GameBoard
{


    fun Setup(width: Int, height: Int)
    {

    }

    fun IsInField(x : Int, y : Int) : Boolean
    {
        return true
    }

    fun Get(x : Int, y : Int) : Placeable?
    {


        return null
    }

    fun Set(x : Int, y : Int, placeable : Placeable)
    {

    }
    
    fun CheckWinningCondition() : List<GameField>
    {
        val winningPositions = listOf<GameField>()

        return winningPositions
    }
}