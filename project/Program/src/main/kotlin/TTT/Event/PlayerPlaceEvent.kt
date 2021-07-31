package TTT.Event

import TTT.Placeable
import TTT.PlayerPlaceResult
import TTT.PlayerSymbol

class PlayerPlaceEvent(val playerSymbol: PlayerSymbol, val result : PlayerPlaceResult, val placeable: Placeable)
{

}