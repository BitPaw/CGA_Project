package TTT

class GameBoard(width: Int = 3, height : Int = 3, private val target : Int = minOf(width,height))
{
    val field = Array(width) {Array(height) { Placeable() } }

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
        val winningPositions = mutableListOf<GameField>()

        var winning = false

        for(i in 0 until field.size-2)
        {
            for(j in field[i].indices)
            {
                if(field[i][j].Symbol != PlayerSymbol.None)
                {
                    val symbol = field[i][j].Symbol
                    winningPositions.add(GameField(i,j,field[i][j]))
                    for(combination in 1 until 4)
                    {
                        winning=true
                        for(times in 1 until target)
                        {
                            when(combination)
                            {
                                1 -> {
                                    if(field[i+times][j].Symbol!=symbol)
                                    {
                                        winning=false
                                    }
                                }
                                2 -> {
                                    if(field[i][j+times].Symbol!=symbol)
                                    {
                                    winning=false
                                    }
                                }
                                3 -> {
                                    if(field[i+times][j+times].Symbol!=symbol)
                                    {
                                        winning=false
                                    }
                                }
                                4 -> {
                                    if(field[i+times][j-times].Symbol!=symbol)
                                    {
                                        winning=false
                                    }
                                }

                            }
                            if(winning)
                            {

                            }
                        }
                    }

                }

            }
        }
        return winningPositions
    }
}