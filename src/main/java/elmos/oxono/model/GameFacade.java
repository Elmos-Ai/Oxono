package elmos.oxono.model;

import elmos.oxono.model.command.ChoseTotem;
import elmos.oxono.model.command.CommandManager;
import elmos.oxono.model.command.MoveTotem;
import elmos.oxono.model.command.PlacePawn;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The facade for the game, providing high-level methods to manage the game's state and actions.
 */
public class GameFacade {
    private CommandManager commandManager;
    private Totem currentTotem = new Totem(PieceType.NONE);
    private PieceType previousTotem = PieceType.NONE;
    private Bag bag;
    private Board board;
    private Game game;
    private final PropertyChangeSupport pcs;
    private PieceColor currentPlayer;

    /**
     * Initializes the game by setting up the board, bag, and command manager.
     */
    public GameFacade() {
        this.bag = new Bag();
        this.board = new Board(this.bag.getTotemCircle(), this.bag.getTotemCross());
        this.game = new Game(this.board, this.bag);
        this.commandManager = new CommandManager();
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * Notifies observers that the color choice state is active.
     */
    public void choseColor() {
        notifyValueChanged(GameState.ColorChoice);
    }

    /**
     * Sets the current player's color and notifies observers that the totem selection state is active.
     *
     * @param currentPlayerColor the color chosen by the current player
     */
    public void playerColorChosed(PieceColor currentPlayerColor) {
        this.currentPlayer = currentPlayerColor;
        notifyValueChanged(GameState.TotemChose);
    }

    /**
     * Advances the game to the next round, notifying observers of the totem selection state.
     */
    public void nextRound() {
        notifyValueChanged(GameState.TotemChose);
    }

    /**
     * Sets the current totem based on the chosen symbol and notifies observers to move the totem.
     *
     * @param totemSymbol the symbol of the chosen totem
     */
    public void choseTotem(PieceType totemSymbol) {
        this.setCurrentTotem(this.commandManager.Do(new ChoseTotem(totemSymbol, previousTotem)));
        notifyValueChanged(GameState.TotemMove);
    }

    /**
     * Sets the current totem based on the provided piece symbol.
     *
     * This method updates the current totem of the object based on the value of the
     * {@code totemSymbol} parameter. If the piece symbol is equal to {@link PieceType#NONE},
     * a new totem with the symbol {@link PieceType#NONE} is created. If the symbol is
     * {@link PieceType#CROSS}, the corresponding cross totem is retrieved from the
     * {@code bag}. Otherwise, the corresponding circle totem is retrieved from the {@code bag}.
     *
     * @param totemSymbol The piece symbol to set the current totem.
     *                    This parameter can be {@link PieceType#NONE},
     *                    {@link PieceType#CROSS}, or {@link PieceType#CIRCLE}.
     *
     * @throws NullPointerException If the {@code totemSymbol} parameter is {@code null}.
     */
    public void setCurrentTotem(PieceType totemSymbol) {
        if(totemSymbol == PieceType.NONE){
            this.currentTotem = new Totem(PieceType.NONE);
        }else if(totemSymbol.equals(PieceType.CROSS)){
            this.currentTotem = this.bag.getTotemCross();
        } else {
            this.currentTotem = this.bag.getTotemCircle();
        }
    }

    /**
     * Moves the current totem to the specified position if the move is valid.
     *
     * @param x the x-coordinate of the target position
     * @param y the y-coordinate of the target position
     * @return true if the totem was successfully moved, false otherwise
     */
    public boolean moveTotem(int x, int y) {
        if (this.game.totemPositionIsAvailable(this.currentTotem, x, y)) {
            this.commandManager.Do(new MoveTotem(this.game, this.currentTotem, x, y));
            notifyValueChanged(GameState.PlacePawn);
            return true;
        }
        return false;
    }

    /**
     * Places a pawn on the board and checks for game conditions such as a win or draw.
     *
     * @param currentPlayerColor the color of the current player
     * @param x the x-coordinate of the target position
     * @param y the y-coordinate of the target position
     * @return true if a win condition was met, false otherwise
     */
    public boolean placePawn(PieceColor currentPlayerColor, int x, int y) {
        if (totemIsStuck(currentTotem.getSymbol()) && this.game.stuckTotemPawnPositionIsAvailable(this.currentTotem, x, y)) {
            this.commandManager.Do(new PlacePawn(this.game, currentPlayerColor, this.currentTotem.getSymbol(), x, y));
            this.previousTotem = this.currentTotem.getSymbol();
            if (this.getWinner(currentPlayerColor, x, y)) {
                return true;
            }
            if (gameIsDraw()) {
                notifyValueChanged(GameState.GameDraw);
            } else {
                notifyValueChanged(GameState.WaitForNextRound);
            }
            return true;
        } else if (this.game.pawnPositionIsAvailable(this.currentTotem, x, y)) {
            this.commandManager.Do(new PlacePawn(this.game, currentPlayerColor, this.currentTotem.getSymbol(), x, y));
            this.previousTotem = this.currentTotem.getSymbol();
            if (this.getWinner(currentPlayerColor, x, y)) {
                return true;
            } else if (gameIsDraw()) {
                notifyValueChanged(GameState.GameDraw);
            } else {
                notifyValueChanged(GameState.WaitForNextRound);
            }
            return true;
        }
        return false;
    }

    /**
     * Undoes the last action and updates the game state accordingly.
     *
     * @param gameState the current game state
     * @return true if the undo operation was successful, false otherwise
     */
    public boolean undo(GameState gameState) {
        PieceType undoResult = this.commandManager.undo();
        if (undoResult != null) {
            if (gameState.equals(GameState.WaitForNextRound)) {
                this.notifyValueChanged(GameState.PlacePawn);
            }
            if (gameState.equals(GameState.TotemChose)) {
                this.setCurrentTotem(undoResult);
                if (undoResult != PieceType.NONE) {
                    this.notifyValueChanged(GameState.PlacePawn);
                }
            } else if (gameState.equals(GameState.TotemMove)) {
                this.setCurrentTotem(undoResult);
                this.notifyValueChanged(GameState.TotemChose);
            } else if (gameState.equals(GameState.PlacePawn)) {
                this.notifyValueChanged(GameState.TotemMove);
            }
            return true;
        }
        return false;
    }

    /**
     * Redoes the last undone action and updates the game state accordingly.
     *
     * @param gameState the current game state
     * @return true if the redo operation was successful, false otherwise
     */
    public boolean redo(GameState gameState) {
        PieceType redoResult = this.commandManager.redo();
        if (redoResult != null) {
            if (gameState.equals(GameState.TotemChose)) {
                this.setCurrentTotem(redoResult);
                this.notifyValueChanged(GameState.TotemMove);
            } else if (gameState.equals(GameState.TotemMove)) {
                this.notifyValueChanged(GameState.PlacePawn);
            } else if (gameState.equals(GameState.PlacePawn)) {
                this.notifyValueChanged(GameState.TotemChose);
            }
            return true;
        }
        return false;
    }

    /**
     * Resets the game to its initial state and notifies observers.
     */
    public void resetGame() {
        this.bag = new Bag();
        this.board = new Board(this.bag.getTotemCircle(), this.bag.getTotemCross());
        this.game = new Game(this.board, this.bag);
        this.commandManager = new CommandManager();
        this.currentTotem = new Totem(PieceType.NONE);
        this.previousTotem = PieceType.NONE;
        notifyValueChanged(GameState.GameModChoice);
    }

    /**
     * Checks if the specified player has won the game.
     *
     * @param currentPlayer the color of the current player
     * @param x the x-coordinate of the last move
     * @param y the y-coordinate of the last move
     * @return true if the player has won, false otherwise
     */
    public boolean getWinner(PieceColor currentPlayer, int x, int y) {
        boolean playerWon = this.game.getWinner(x, y);
        if (playerWon) {
            if (currentPlayer.equals(PieceColor.PINK)) {
                notifyValueChanged(GameState.PinkWin);
            } else {
                notifyValueChanged(GameState.BlackWin);
            }
        }
        return playerWon;
    }

    /**
     * Retrieves the piece located at the specified position on the board.
     *
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     * @return the piece at the specified position
     */
    public Piece getBoardPieceAt(int x, int y) {
        return this.board.getBoard()[x][y];
    }

    /**
     * Checks if the specified totem is stuck.
     *
     * @param totemSymbol the symbol of the totem to check
     * @return true if the totem is stuck, false otherwise
     */
    public boolean totemIsStuck(PieceType totemSymbol) {
        if (totemSymbol.equals(PieceType.CROSS)) {
            return this.game.totemIsStuck(this.bag.getTotemCross());
        } else {
            return this.game.totemIsStuck(this.bag.getTotemCircle());
        }
    }

    /**
     * Adds an observer to the game.
     *
     * @param observer the observer to add
     */
    public void addObserver(PropertyChangeListener observer) {
        pcs.addPropertyChangeListener(observer);
        pcs.firePropertyChange("SETUP", GameState.GameModChoice, null);
    }

    /**
     * Removes an observer from the game.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(PropertyChangeListener observer) {
        pcs.removePropertyChangeListener(observer);
    }

    /**
     * Checks if the game has reached a draw state.
     *
     * @return true if the game is a draw, false otherwise
     */
    public boolean gameIsDraw() {
        return this.getBlackCirclePawnsCount() == 0
                && this.getBlackCrossPawnsCount() == 0
                && this.getPinkCrossPawnsCount() == 0
                && this.getPinkCirclePawnsCount() == 0;
    }

    /**
     * Displays the current state of the board.
     */
    public void showBoard() {
        notifyValueChanged(GameState.ShowBoard);
    }

    /**
     * Gets the current player.
     *
     * @return the current player
     */
    public PieceColor getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Sets the current player.
     *
     * @param currentPlayer the current player
     */
    public void setCurrentPlayer(PieceColor currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Retrieves the count of pink circle pawns remaining.
     *
     * @return the count of pink circle pawns
     */
    public int getPinkCirclePawnsCount() {
        return this.bag.getPinkCirclePawnsCount();
    }

    /**
     * Retrieves the count of pink cross pawns remaining.
     *
     * @return the count of pink cross pawns
     */
    public int getPinkCrossPawnsCount() {
        return this.bag.getPinkCrossPawnsCount();
    }

    /**
     * Retrieves the count of black circle pawns remaining.
     *
     * @return the count of black circle pawns
     */
    public int getBlackCirclePawnsCount() {
        return this.bag.getBlackCirclePawnsCount();
    }

    /**
     * Retrieves the count of black cross pawns remaining.
     *
     * @return the count of black cross pawns
     */
    public int getBlackCrossPawnsCount() {
        return this.bag.getBlackCrossPawnCount();
    }

    /**
     * Retrieves the symbol of the current totem.
     *
     * @return the symbol of the current totem
     */
    public PieceType getCurrentTotem() {
        return this.currentTotem.getSymbol();
    }

    /**
     * Retrieves the symbol of the previous totem.
     *
     * @return the symbol of the previous totem
     */
    public PieceType getPreviousTotem() {
        return this.previousTotem;
    }

    // Private methods (no Javadoc added)

    private void notifyValueChanged(GameState gameState) {
        pcs.firePropertyChange("VALUE_CHANGE", gameState, this.currentTotem.getSymbol());
    }
}
