package TTT.Event

import TTT.GameField
import TTT.RoundResult

class RoundEndEvent(val RoundResult: RoundResult, val AffectedFields: List<GameField> = emptyList())
{

}