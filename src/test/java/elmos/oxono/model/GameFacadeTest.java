package elmos.oxono.model;

import command.ChoseTotem;
import command.CommandManager;
import command.MoveTotem;
import command.PlacePawn;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GameFacadeTest {
    CommandManager commandManager;
    Totem currentTotem = new Totem(PieceType.NONE);
    PieceType previousTotem = PieceType.NONE;
    Bag bag;
    Board board;
    Game game;
    private final PropertyChangeSupport pcs;

    public GameFacadeTest() {
        this.bag = new Bag();
        this.board = new Board(this.bag.getTotemCircle(), this.bag.getTotemCross());
        this.game = new Game(this.board, this.bag);
        this.commandManager = new CommandManager();
        this.pcs = new PropertyChangeSupport(this);
    }

    public void choseTotem(PieceType totemSymbol){
        System.out.println("chose totem");
        this.setCurrentTotem(this.commandManager.Do(new ChoseTotem(totemSymbol, previousTotem)));
        System.out.println(this.currentTotem.getSymbol());
    }
    public boolean moveTotem(int x, int y){
        System.out.println("move totem");
        if(this.game.totemPositionIsAvailable(this.currentTotem, x, y)){
            this.commandManager.Do(new MoveTotem(this.game, this.currentTotem, x, y));
            return true;
        }
        return false;
    }
    public boolean placePawn(PieceColor currentPlayerColor, int x, int y){
        System.out.println("place pawn");
        if(this.game.pawnPositionIsAvailable(this.currentTotem, x, y)){
            this.commandManager.Do(new PlacePawn(this.game, currentPlayerColor, this.currentTotem, x, y));
            return true;
        }
        System.out.println("position not available for pawn");
        return false;
    }
    public void undo(GameState gameState){
        System.out.println("undo");
        PieceType undoResult = this.commandManager.undo();
        if(undoResult != null){
            if(gameState.equals(GameState.TotemChose)){
                this.setCurrentTotem(undoResult);
                if(undoResult != PieceType.NONE){
                    this.notifyValueChanged(GameState.PlacePawn);
                }
                System.out.println(this.currentTotem.getSymbol());
            } else if(gameState.equals(GameState.TotemMove)){
                this.setCurrentTotem(undoResult);
                this.notifyValueChanged(GameState.TotemChose);
            } else if(gameState.equals(GameState.PlacePawn)){
                this.notifyValueChanged(GameState.TotemMove);
            }
        } else {
            System.out.println("nothing to undo");
        }
    }
    public void redo(GameState gameState){
        System.out.println("redo");
        PieceType redoResult = this.commandManager.redo();
        if(redoResult != null){
            if(gameState.equals(GameState.TotemChose)){
                this.setCurrentTotem(redoResult);
                this.notifyValueChanged(GameState.TotemMove);
            } else if(gameState.equals(GameState.TotemMove)){
                this.notifyValueChanged(GameState.PlacePawn);
            } else if(gameState.equals(GameState.PlacePawn)){
                this.notifyValueChanged(GameState.TotemChose);
            }
            System.out.println(this.currentTotem.getSymbol());
        } else {
            System.out.println("nothing to redo");
        }
    }
    private void setCurrentTotem(PieceType totemSymbol){
        if(totemSymbol == PieceType.NONE){
            this.currentTotem = new Totem(PieceType.NONE);
        }else if(totemSymbol.equals(PieceType.CROSS)){
            this.currentTotem = this.bag.getTotemCross();
        } else {
            this.currentTotem = this.bag.getTotemCircle();
        }
    }
    public void resetGame(){
        this.bag = new Bag();
        this.board = new Board(this.bag.getTotemCircle(), this.bag.getTotemCross());
        this.game = new Game(this.board, this.bag);
        this.commandManager = new CommandManager();
        this.currentTotem = null;
        this.previousTotem = null;
        notifyValueChanged(GameState.ColorChoice);
    }
    public boolean getWinner(Player currentPlayer, int x, int y){
        boolean playerWon = this.game.getWinner(x, y);
        if(playerWon){
            if(currentPlayer.getColor().equals(PieceColor.PINK)){
                notifyValueChanged(GameState.PinkWin);
            } else{
                notifyValueChanged(GameState.BlackWin);
            }
        }
        return playerWon;
    }

    public Piece getBoardPieceAt(int x, int y){
        return this.board.getBoard()[x][y];
    }

    public boolean totemIsStuck(PieceType totemSymbol){
        if(totemSymbol.equals(PieceType.CROSS)){
            return this.game.totemIsStuck(this.bag.getTotemCross());
        } else {
            return this.game.totemIsStuck(this.bag.getTotemCircle());
        }
    }
    public void addObserver(PropertyChangeListener observer) {
        pcs.addPropertyChangeListener(observer);
        pcs.firePropertyChange("SETUP", GameState.ColorChoice, null);
    }
    public void removeObserver(PropertyChangeListener observer) {
        pcs.removePropertyChangeListener(observer);
    }
    private void notifyValueChanged(GameState gameState) {
        pcs.firePropertyChange("VALUE_CHANGE", gameState, this.currentTotem);
    }
    public PieceType getCurrentTotem(){
        return this.currentTotem.getSymbol();
    }
    public void showBoard(){
        notifyValueChanged(GameState.ShowBoard);
    }

    public int getPinkCirclePawnsCount(){
        return this.bag.getPinkCirclePawnsCount();
    }
    public int getPinkCrossPawnsCount(){
        return this.bag.getPinkCrossPawnsCount();
    }
    public int getBlackCirclePawnsCount(){
        return this.bag.getBlackCirclePawnsCount();
    }
    public int getBlackCrossPawnsCount(){
        return this.bag.getBlackCrossPawnCount();
    }
    private void showBoard(Piece[][] gameBoard){
        System.out.println("    0  1  2  3  4  5");
        for (int i = 0; i < gameBoard.length; i++) {
            System.out.print(i + " |");
            for (int j = 0; j < gameBoard[0].length; j++) {
                if(gameBoard[i][j] == null){
                    System.out.print("  |");
                } else if(gameBoard[i][j] instanceof Totem){
                    if(gameBoard[i][j].getSymbol().equals(PieceType.CIRCLE)){
                        System.out.print(" O|");
                    } else {
                        System.out.print(" X|");
                    }
                } else if(gameBoard[i][j] instanceof Pawn){
                    if(((Pawn) gameBoard[i][j]).getColor().equals(PieceColor.PINK)){
                        if(((Pawn) gameBoard[i][j]).getSymbol().equals(PieceType.CIRCLE)){
                            System.out.print("Po|");
                        }else{
                            System.out.print("Px|");
                        }
                    }else{
                        if(((Pawn) gameBoard[i][j]).getSymbol().equals(PieceType.CROSS)){
                            System.out.print("Bx|");
                        }else{
                            System.out.print("Bo|");
                        }
                    }
                } else if (gameBoard[i][j].getSymbol().equals(PieceType.AVAILABLE)){
                    System.out.print(" a|");
                }
            }
            System.out.println("");
        }
    }
    @Test
    void choseTotemTest(){
//        this.choseTotem(PieceType.CIRCLE);
//        showBoard(this.board.getBoard());
//        this.moveTotem(2, 0);
//        showBoard(this.board.getBoard());
//        this.undo();
//        showBoard(this.board.getBoard());
//        this.undo();
//        showBoard(this.board.getBoard());
//        this.choseTotem(PieceType.CROSS);
//        showBoard(this.board.getBoard());
//        this.moveTotem(3, 0);
//        showBoard(this.board.getBoard());
//        this.placePawn(PieceColor.PINK, 4, 0);
//        showBoard(this.board.getBoard());
//        this.undo();
//        showBoard(this.board.getBoard());
//        this.redo();
//        showBoard(this.board.getBoard());
//        undo();
//        showBoard(this.board.getBoard());
//        undo();
//        showBoard(this.board.getBoard());
//        undo();
//        showBoard(this.board.getBoard());
//        undo();
//        showBoard(this.board.getBoard());
//        undo();
//        showBoard(this.board.getBoard());
//        undo();
//        showBoard(this.board.getBoard());
//        undo();
//        showBoard(this.board.getBoard());
//        undo();
//        showBoard(this.board.getBoard());
    }

}
