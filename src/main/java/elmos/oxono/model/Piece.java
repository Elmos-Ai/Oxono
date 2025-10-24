package elmos.oxono.model;

public class Piece {
    private PieceType symbol;

    /**
     * Initializes a new Piece with the given symbol.
     *
     * @param symbol The type of the piece (e.g., {@link PieceType#CROSS} or {@link PieceType#CIRCLE}).
     */
    public Piece(PieceType symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the symbol of the piece.
     *
     * @return the symbol of the piece (e.g., {@link PieceType#CROSS} or {@link PieceType#CIRCLE}).
     */
    public PieceType getSymbol() {
        return this.symbol;
    }
}

