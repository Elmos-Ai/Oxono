package elmos.oxono.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Totem extends Piece {
    private int currentX;
    private int currentY;
    private Piece[][] board;

    public Totem(PieceType symbol) {
        super(symbol);
    }
    public void move(int x, int y){
        this.board[x][y] = this;
        this.board[this.currentX][this.currentY] = null;
        setCurrentPosition(x, y);
    }
    public void move(Piece[][] board, int x, int y){
        this.board = board;
        this.board[x][y] = this;
        setCurrentPosition(x, y);
    }
    public void setCurrentPosition(int x, int y){
        this.currentX = x;
        this.currentY = y;
    }
    public int getCurrentX() {
        return currentX;
    }
    public int getCurrentY() {
        return currentY;
    }
}
