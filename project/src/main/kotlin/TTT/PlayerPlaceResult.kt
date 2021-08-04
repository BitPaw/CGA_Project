package TTT

enum class PlayerPlaceResult
{
    InvalidAction,

    Successful,
    SuccessfulOverride,
    NotYourTurn,
    NotInField,
    Occupied,

    AlreadyUsed
}