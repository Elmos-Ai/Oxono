package elmos.oxono.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a game board containing pieces that can be moved according to specific rules.
 */
public class Board {

    private Piece[][] board;

    /**
     * Creates a new board and initializes it with totems.
     *
     * @param totemCircle the circle-shaped totem to be placed on the board
     * @param totemCross the cross-shaped totem to be placed on the board
     */
    public Board(Totem totemCircle, Totem totemCross) {
        this.board = new Piece[6][6];
        this.initTotems(totemCircle, totemCross);
    }

    /**
     * Creates a new empty board.
     */
    public Board() {
        this.board = new Piece[6][6];
    }

    private void initTotems(Totem totemCircle, Totem totemCross) {
        Random rd = new Random();
        if (rd.nextBoolean()) {
            totemCross.move(this.board, 3, 3);
            totemCircle.move(this.board, 2, 2);
        } else {
            totemCross.move(this.board, 2, 2);
            totemCircle.move(this.board, 3, 3);
        }
    }

    /**
     * Returns the current state of the board.
     *
     * @return a 2D array representing the board and its pieces
     */
    public Piece[][] getBoard() {
        return board;
    }

    /**
     * Checks if the specified coordinates are within the bounds of the board.
     *
     * @param x the x-coordinate to check
     * @param y the y-coordinate to check
     * @return true if the coordinates are within the board, false otherwise
     */
    public boolean isInBoard(int x, int y) {
        return x < this.board.length && x >= 0 && y < this.board.length && y >= 0;
    }

    /**
     * Gets the available positions for placing a stuck totem or pawn starting from a given position.
     *
     * @param x the x-coordinate of the starting position
     * @param y the y-coordinate of the starting position
     * @return a list of available positions represented as int arrays [x, y]
     */
    public List<int[]> getAvailableStuckTotemPawnPositions(int x, int y) {
        List<int[]> availablePositions = new ArrayList<>();
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                if (this.board[i][j] == null) {
                    availablePositions.add(new int[]{i, j});
                }
            }
        }
        return availablePositions;
    }

    /**
     * Gets the available positions for moving a totem from its current position.
     *
     * @param currentX the current x-coordinate of the totem
     * @param currentY the current y-coordinate of the totem
     * @return a list of available positions represented as int arrays [x, y]
     */
    public List<int[]> getAvailableTotemPositions(int currentX, int currentY) {
        List<int[]> availablePositions;
        List<int[]> availableXPositions = new ArrayList<>();
        List<int[]> availableYPositions = new ArrayList<>();
        boolean stopYIteration = false;
        boolean stopXIteration = false;

        for (int i = 0; i < this.board.length; i++) {
            if (!stopYIteration) {
                if (this.board[currentX][i] == null) {
                    availableYPositions.add(new int[]{currentX, i});
                } else if (i > currentY) {
                    stopYIteration = true;
                } else if (i < currentY) {
                    availableYPositions = new ArrayList<>();
                }
            }
            if (!stopXIteration) {
                if (this.board[i][currentY] == null) {
                    availableXPositions.add(new int[]{i, currentY});
                } else if (i > currentX) {
                    stopXIteration = true;
                } else if (i < currentX) {
                    availableXPositions = new ArrayList<>();
                }
            }
        }
        availablePositions = new ArrayList<>(availableYPositions);
        availablePositions.addAll(availableXPositions);
        return availablePositions;
    }

    /**
     * Gets the available positions for moving a stuck totem from its current position.
     *
     * @param x the current x-coordinate of the stuck totem
     * @param y the current y-coordinate of the stuck totem
     * @return a list of available positions represented as int arrays [x, y]
     */
    public List<int[]> getAvailableStuckTotemPositions(int x, int y) {
        List<int[]> availablePositions = new ArrayList<>();
        int directionCount = 0;
        int i = 1;
        int currentY = y;
        int currentX = x;

        do {
            if (directionCount == 0 && isInBoard(currentX, y - i)) {
                currentY = y - i;
            } else if (directionCount == 1 && isInBoard(currentX, y + i)) {
                currentY = y + i;
            } else if (directionCount == 2 && isInBoard(x - i, currentY)) {
                currentX = x - i;
            } else if (directionCount == 3 && isInBoard(x + i, currentY)) {
                currentX = x + i;
            } else {
                directionCount += 1;
                i = 1;
                currentX = x;
                currentY = y;
                continue;
            }

            if (this.board[currentX][currentY] == null) {
                availablePositions.add(new int[]{currentX, currentY});
                directionCount += 1;
                i = 1;
                currentX = x;
                currentY = y;
                continue;
            }

            i += 1;
        } while (directionCount < 4);

        if (availablePositions.isEmpty()) {
            return this.getAvailableStuckTotemPawnPositions(x, y);
        }
        return availablePositions;
    }

    /**
     * Gets the available positions for moving a pawn from its current position.
     *
     * @param currentX the current x-coordinate of the pawn
     * @param currentY the current y-coordinate of the pawn
     * @return a list of available positions represented as int arrays [x, y]
     */
    public List<int[]> getAvailablePawnPositions(int currentX, int currentY) {
        List<int[]> availablePositions = new ArrayList<>();
        availablePositions.add(new int[]{currentX - 1, currentY});
        availablePositions.add(new int[]{currentX, currentY - 1});
        availablePositions.add(new int[]{currentX, currentY + 1});
        availablePositions.add(new int[]{currentX + 1, currentY});

        availablePositions.removeIf(
                availablePos -> (
                        availablePos[0] > this.board.length - 1 || availablePos[0] < 0
                                || availablePos[1] > this.board.length - 1 || availablePos[1] < 0
                                || this.board[availablePos[0]][availablePos[1]] != null
                )
        );

        return availablePositions;
    }
}
