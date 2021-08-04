package TTT

class Player(val PlayerSymbol : PlayerSymbol)
{
    val Name = "Unnamed"
    var PlaceableOptionsMap = BooleanArray(6)

    fun Reset()
    {
        val amountofElemets = PlaceableOptionsMap.size

        for (i in 0..amountofElemets)
        {
            PlaceableOptionsMap[i] = true
        }
    }

    fun CanUse(strength : Int) : Boolean
    {
        return true
    }
}