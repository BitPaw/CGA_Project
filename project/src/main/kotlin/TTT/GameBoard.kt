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

    // will be made dynamically later
    fun GetWinningFields() : List<GameField>
    {
        val winningPositions = mutableListOf<GameField>()

        if(Width==3&&Height==3)
        {
            val first_row = FieldList[0][0].Symbol==FieldList[0][1].Symbol&&FieldList[0][1].Symbol==FieldList[0][2].Symbol&&FieldList[0][0].Symbol!=PlayerSymbol.None
            val second_row = FieldList[1][0].Symbol==FieldList[1][1].Symbol&&FieldList[1][1].Symbol==FieldList[1][2].Symbol&&FieldList[1][0].Symbol!=PlayerSymbol.None
            val third_row = FieldList[2][0].Symbol==FieldList[2][1].Symbol&&FieldList[2][1].Symbol==FieldList[2][2].Symbol&&FieldList[2][0].Symbol!=PlayerSymbol.None
            val first_column = FieldList[0][0].Symbol==FieldList[1][0].Symbol&&FieldList[1][0].Symbol==FieldList[2][0].Symbol&&FieldList[0][0].Symbol!=PlayerSymbol.None
            val second_column = FieldList[0][1].Symbol==FieldList[1][1].Symbol&&FieldList[1][1].Symbol==FieldList[2][1].Symbol&&FieldList[0][1].Symbol!=PlayerSymbol.None
            val third_column= FieldList[0][2].Symbol==FieldList[1][2].Symbol&&FieldList[1][2].Symbol==FieldList[2][2].Symbol&&FieldList[0][2].Symbol!=PlayerSymbol.None
            val diagonal_1 = FieldList[0][0].Symbol==FieldList[1][1].Symbol&&FieldList[1][1].Symbol==FieldList[2][2].Symbol&&FieldList[0][0].Symbol!=PlayerSymbol.None
            val diagonal_2 = FieldList[0][2].Symbol==FieldList[1][1].Symbol&&FieldList[1][1].Symbol==FieldList[2][0].Symbol&&FieldList[1][1].Symbol!=PlayerSymbol.None
            if(first_row)
            {
                winningPositions.add(FieldList[0][0])
                winningPositions.add(FieldList[0][1])
                winningPositions.add(FieldList[0][2])
            }
            if(second_row)
            {
                winningPositions.add(FieldList[1][0])
                winningPositions.add(FieldList[1][1])
                winningPositions.add(FieldList[1][2])
            }
            if(third_row)
            {
                winningPositions.add(FieldList[2][0])
                winningPositions.add(FieldList[2][1])
                winningPositions.add(FieldList[2][2])
            }
            if(first_column)
            {
                winningPositions.add(FieldList[0][0])
                winningPositions.add(FieldList[1][0])
                winningPositions.add(FieldList[2][0])
            }
            if(second_column)
            {
                winningPositions.add(FieldList[0][1])
                winningPositions.add(FieldList[1][1])
                winningPositions.add(FieldList[2][1])
            }
            if(third_column)
            {
                winningPositions.add(FieldList[0][2])
                winningPositions.add(FieldList[1][2])
                winningPositions.add(FieldList[2][2])
            }
            if(diagonal_1)
            {
                winningPositions.add(FieldList[0][0])
                winningPositions.add(FieldList[1][1])
                winningPositions.add(FieldList[2][2])
            }
            if(diagonal_2)
            {
                winningPositions.add(FieldList[0][2])
                winningPositions.add(FieldList[1][1])
                winningPositions.add(FieldList[2][0])
            }
        }

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