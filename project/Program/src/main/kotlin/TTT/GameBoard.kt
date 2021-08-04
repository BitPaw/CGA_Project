package TTT

class GameBoard()
{
    var width:Int = -1
    var height:Int = -1
    private var target:Int = -1
    private var field = Array(0) {Array(0) { GameField(-1, -1) } }

    fun Setup(width: Int,height: Int,winnumber:Int)
    {
        this.width= width
        this.height=height
        target=winnumber
        field = Array(width) {i -> Array(height) { j -> GameField(i, j) } }
    }

    fun Setup(width: Int,height: Int)
    {
        this.width= width
        this.height=height
        field = Array(width) {i -> Array(height) { j -> GameField(i, j) } }
    }

    fun IsInField(x : Int, y : Int) : Boolean
    {
        if(x>width||y>height)
        {
            return false
        }
        return true
    }

    fun Get(x : Int, y : Int) : GameField?
    {

        return field[x][y]
    }

    fun Set(gameField: GameField)
    {
        field[gameField.x][gameField.y] = gameField
    }

    fun CheckWinningCondition() : List<GameField>
    {
        val winningPositions = mutableListOf<GameField>()



        return winningPositions
    }
}