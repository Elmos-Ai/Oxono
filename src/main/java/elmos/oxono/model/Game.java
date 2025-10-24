package elmos.oxono.model;

import java.util.List;

public class Game {
    private final Board board;
    private final Bag bag;

    /**
     * Creates a new game with the specified board and bag.
     *
     * @param board the game board
     * @param bag the bag containing pieces for the game
     */
    public Game(Board board, Bag bag) {
        this.board = board;
        this.bag = bag;
    }

    /**
     * Checks if there is a winner based on the most recent move at the given position.
     *
     * @param x the x-coordinate of the move
     * @param y the y-coordinate of the move
     * @return true if there is a winner, false otherwise
     */
    public boolean getWinner(int x, int y) {
        return (getWinnerDirection(Direction.HORIZONTAL, x, y)
                || getWinnerDirection(Direction.VERTICAL, x, y));
    }

    private boolean getWinnerDirection(Direction direction, int x, int y) {
        int sameColorCount = 1;
        int sameSymbolCount = 1;
        Piece previousPiece;
        Piece currentPiece;

        if (direction.equals(Direction.HORIZONTAL)) {
            previousPiece = this.board.getBoard()[x][0];
        } else {
            previousPiece = this.board.getBoard()[0][y];
        }
        for (int i = 1; i < this.board.getBoard().length; i++) {
            if (direction.equals(Direction.HORIZONTAL)) {
                currentPiece = this.board.getBoard()[x][i];
            } else {
                currentPiece = this.board.getBoard()[i][y];
            }
            if (previousPiece instanceof Pawn && currentPiece instanceof Pawn) {
                sameSymbolCount =
                        currentPiece.getSymbol().equals(previousPiece.getSymbol()) ?
                                sameSymbolCount + 1 : 1;
                sameColorCount =
                        ((Pawn) currentPiece).getColor().equals(((Pawn) previousPiece).getColor()) ?
                                sameColorCount + 1 : 1;
            } else {
                sameSymbolCount = 1;
                sameColorCount = 1;
            }
            if (sameColorCount == 4 || sameSymbolCount == 4) {
                return true;
            }
            previousPiece = currentPiece;
        }
        return false;
    }

    /**
     * Moves a totem to a new position on the board.
     *
     * @param totem the totem to move
     * @param x the target x-coordinate
     * @param y the target y-coordinate
     * @return true if the move is successful
     */
    public boolean moveTotem(Totem totem, int x, int y) {
        this.board.getBoard()[x][y] = totem;
        this.board.getBoard()[totem.getCurrentX()][totem.getCurrentY()] = null;
        totem.setCurrentPosition(x, y);
        return true;
    }

    /**
     * Moves a totem back to its previous position.
     *
     * @param totem the totem to move back
     * @param previousX the previous x-coordinate
     * @param previousY the previous y-coordinate
     */
    public void moveBackTotem(Totem totem, int previousX, int previousY) {
        this.board.getBoard()[previousX][previousY] = totem;
        this.board.getBoard()[totem.getCurrentX()][totem.getCurrentY()] = null;
        totem.setCurrentPosition(previousX, previousY);
    }

    /**
     * Places a pawn on the board if it is available in the bag.
     *
     * @param pawnSymbol the symbol of the pawn to place
     * @param playerColor the color of the player placing the pawn
     * @param x the x-coordinate to place the pawn
     * @param y the y-coordinate to place the pawn
     * @return true if the pawn was successfully placed, false otherwise
     */
    public boolean placePawn(PieceType pawnSymbol, PieceColor playerColor, int x, int y) {
        boolean pawnExists;
        if (playerColor.equals(PieceColor.PINK)) {
            pawnExists = this.bag.removePinkPawn(pawnSymbol);
        } else {
            pawnExists = this.bag.removeBlackPawn(pawnSymbol);
        }
        if (pawnExists) {
            this.board.getBoard()[x][y] = new Pawn(pawnSymbol, playerColor);
        }
        return (pawnExists);
    }

    /**
     * Removes a pawn from the board and adds it back to the bag.
     *
     * @param x the x-coordinate of the pawn to remove
     * @param y the y-coordinate of the pawn to remove
     */
    public void removePawn(int x, int y) {
        Pawn pawn = (Pawn) this.board.getBoard()[x][y];

        if (pawn.getColor().equals(PieceColor.PINK)) {
            this.bag.addPinkPawn(pawn.getSymbol());
        } else {
            this.bag.addBlackPawn(pawn.getSymbol());
        }
        this.board.getBoard()[x][y] = null;
    }

    /**
     * Checks if a totem can be moved to a specified position.
     *
     * @param totem the totem to move
     * @param x the target x-coordinate
     * @param y the target y-coordinate
     * @return true if the position is available for the totem
     */
    public boolean totemPositionIsAvailable(Totem totem, int x, int y) {
        List<int[]> availableTotemPositions;
        if (totemIsStuck(totem)) {
            availableTotemPositions = this.board.getAvailableStuckTotemPositions(totem.getCurrentX(), totem.getCurrentY());
        } else {
            availableTotemPositions = this.board.getAvailableTotemPositions(totem.getCurrentX(), totem.getCurrentY());
        }
        for (int[] pos : availableTotemPositions) {
            if (pos[0] == x && pos[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a pawn can be moved to a specified position.
     *
     * @param totem the totem to check from
     * @param x the target x-coordinate
     * @param y the target y-coordinate
     * @return true if the position is available for the pawn
     */
    public boolean pawnPositionIsAvailable(Totem totem, int x, int y) {
        List<int[]> availablePawnPositions = this.board.getAvailablePawnPositions(totem.getCurrentX(), totem.getCurrentY());
        for (int[] availablePosition : availablePawnPositions) {
            if (availablePosition[0] == x && availablePosition[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a pawn can be moved to a specified position when a totem is stuck.
     *
     * @param totem the totem to check from
     * @param x the target x-coordinate
     * @param y the target y-coordinate
     * @return true if the position is available for the pawn
     */
    public boolean stuckTotemPawnPositionIsAvailable(Totem totem, int x, int y) {
        List<int[]> availablePawnPositions = this.board.getAvailableStuckTotemPawnPositions(totem.getCurrentX(), totem.getCurrentY());
        for (int[] availablePosition : availablePawnPositions) {
            if (availablePosition[0] == x && availablePosition[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a totem is stuck and cannot move.
     *
     * @param totem the totem to check
     * @return true if the totem is stuck, false otherwise
     */
    public boolean totemIsStuck(Totem totem) {
        return this.board.getAvailablePawnPositions(totem.getCurrentX(), totem.getCurrentY()).isEmpty();
    }
}
