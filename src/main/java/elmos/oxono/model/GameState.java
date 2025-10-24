package elmos.oxono.model;

/**
 * Enum representing the possible states of the game.
 * This enum defines various states that can occur during the game, such as when a player wins, chooses a totem,
 * or when the game progresses to the next round.
 */
public enum GameState {

    /**
     * State representing that the Pink player has won the game.
     */
    PinkWin,

    /**
     * State representing that the Black player has won the game.
     */
    BlackWin,

    /**
     * State when the totem selection phase is active.
     */
    TotemChose,

    /**
     * State when the totem movement phase is active.
     */
    TotemMove,

    /**
     * State when the color choice phase is active for a player.
     */
    ColorChoice,

    /**
     * State when the pawn placement phase is active.
     */
    PlacePawn,

    /**
     * State when the board is shown.
     */
    ShowBoard,

    /**
     * State when the game mode selection phase is active.
     */
    GameModChoice,

    /**
     * State representing a draw in the game.
     */
    GameDraw,

    /**
     * State when the game is waiting for the next round to begin.
     */
    WaitForNextRound,

    /**
     * State when a new game is being started.
     */
    NewGame;
}
