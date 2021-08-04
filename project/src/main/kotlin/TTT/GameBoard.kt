package TTT

class GameBoard
{
    var Width : Int = -1
    var Height : Int = -1
    var AmountInARowToWin : Int = -1
    var FieldList : Array<Array<GameField>> = emptyArray()

    fun Setup(width : Int, height : Int, winnumber : Int = Math.max(width, height))
    {
        Width = width
        Height = height
        AmountInARowToWin = winnumber
        FieldList = Array(width) { i -> Array(height) { j -> GameField(i, j) } }
    }

    fun IsInField(x : Int, y : Int) : Boolean
    {
        return x < Width && y < Height
    }

    fun Get(x : Int, y : Int) : GameField
    {
        return FieldList[x][y]
    }

    fun Set(gameField: GameField)
    {
        FieldList[gameField.x][gameField.y] = gameField
    }

    fun GetWinningFields() : List<GameField>
    {
        val winningPositions = mutableListOf<GameField>()

        // TODO: Get all fields that form a row of atleast (AmountInARowToWin)

        return winningPositions
    }

    fun Reset()
    {
        for(element in FieldList)
        {
            val row = element

            for(x in FieldList.indices)
            {
                row[x].Reset()
            }
        }
    }
}