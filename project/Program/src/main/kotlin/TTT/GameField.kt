package TTT

class GameField(var x : Int, var y : Int, var Symbol : PlayerSymbol = PlayerSymbol.None, var Strength : Int = 0)
{
    fun Reset()
    {
        Symbol = PlayerSymbol.None
        Strength = 0
    }
}