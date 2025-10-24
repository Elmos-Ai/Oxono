package elmos.oxono.model;

public class Pawn extends Piece {
    private PieceType symbol;
    private PieceColor color;

    /**
     * Initializes a new Pawn with the given symbol and color.
     *
     * @param symbol The type of the pawn (either {@link PieceType#CROSS} or {@link PieceType#CIRCLE}).
     * @param color The color of the pawn (either {@link PieceColor#PINK} or {@link PieceColor#BLACK}).
     */
    public Pawn(PieceType symbol, PieceColor color) {
        super(symbol);
        this.color = color;
    }

    /**
     * Returns the color of the pawn.
     *
     * @return the color of the pawn ({@link PieceColor#PINK} or {@link PieceColor#BLACK}).
     */
    public PieceColor getColor() {
        return this.color;
    }

    /**
     * Places the pawn on the specified position in the board.
     *
     * @param board The game board where the pawn will be placed.
     * @param x The x-coordinate where the pawn will be placed.
     * @param y The y-coordinate where the pawn will be placed.
     */
    public void place(Piece[][] board, int x, int y) {
        board[x][y] = this;
    }
}
