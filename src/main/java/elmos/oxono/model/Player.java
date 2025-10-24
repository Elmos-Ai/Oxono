package elmos.oxono.model;

import java.util.List;

public class Player {
    protected PieceColor color;

    /**
     * Initializes a new Player with the given color.
     *
     * @param color The color of the player (e.g., {@link PieceColor#PINK} or {@link PieceColor#BLACK}).
     */
    public Player(PieceColor color) {
        this.color = color;
    }

    /**
     * Returns the color of the player.
     *
     * @return the color of the player ({@link PieceColor#PINK} or {@link PieceColor#BLACK}).
     */
    public PieceColor getColor() {
        return this.color;
    }

    /**
     * Compares this player with another player to check if they have the same color.
     *
     * @param player the player to compare with.
     * @return true if both players have the same color, false otherwise.
     */
    public boolean equals(Player player) {
        return this.getColor().equals(player.getColor());
    }
}

