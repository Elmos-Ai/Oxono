package elmos.oxono.view;

import elmos.oxono.model.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ViewCMD implements PropertyChangeListener {
    GameFacade gameFacade;
    Board boardCopy;
    public ViewCMD(GameFacade gameFacade){
        this.gameFacade = gameFacade;
        this.boardCopy = new Board();
        this.gameFacade.addObserver(this);
    }
    public void showBoard(){
        this.showBoard(boardCopy.getBoard());
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
    public void showAvailablePositions(List<int[]> availablePositions){
        Piece[][] board = addAvailablePositionsToGameBoard(availablePositions);
        showBoard(board);
    }
    public void showAvailableStuckTotemPositions(PieceType totemSymbol){
       int[] totemposition = getTotemPosition(totemSymbol);
       showAvailablePositions(this.boardCopy.getAvailableStuckTotemPositions(totemposition[0], totemposition[1]));
    }
    public void showAvailableStuckTotemPawnPosition(PieceType totemSymbol){
        int[] totemposition = getTotemPosition(totemSymbol);
        showAvailablePositions(this.boardCopy.getAvailableStuckTotemPawnPositions(totemposition[0], totemposition[1]));
    }
    public void showAvailableTotemPositions(PieceType totemSymbol){
        int[] totemposition = getTotemPosition(totemSymbol);
        showAvailablePositions(this.boardCopy.getAvailableTotemPositions(totemposition[0], totemposition[1]));
    }
    public void showAvailablePawnPositions(PieceType totemSymbol){
        int[] totemposition = getTotemPosition(totemSymbol);
        showAvailablePositions(this.boardCopy.getAvailablePawnPositions(totemposition[0], totemposition[1]));
    }
    private int[] getTotemPosition(PieceType totemSymbol){
        int currentX = - 1;
        int currentY = - 1;

        for (int i = 0; i < boardCopy.getBoard().length; i++) {
            for (int j = 0; j < boardCopy.getBoard().length; j++) {
                if(boardCopy.getBoard()[i][j] instanceof Totem && boardCopy.getBoard()[i][j].getSymbol().equals(totemSymbol)){
                    currentX = i;
                    currentY = j;
                }
            }
        }
        return new int[]{currentX,currentY};
    }
    private Piece[][] addAvailablePositionsToGameBoard(List<int[]> availablePositions){
        for (int[] pos: availablePositions){
            boardCopy.getBoard()[pos[0]][pos[1]] = new Piece(PieceType.AVAILABLE);
        }
        return boardCopy.getBoard();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (int i = 0; i < boardCopy.getBoard().length; i++) {
            for (int j = 0; j < boardCopy.getBoard().length; j++) {
                this.boardCopy.getBoard()[i][j] = this.gameFacade.getBoardPieceAt(i, j);
            }
        }
        PieceType totem = (PieceType) evt.getNewValue();

        switch ((GameState) evt.getOldValue()){
            case GameState.ShowBoard:
                this.showBoard();
            case GameState.TotemChose:
                System.out.println("totemChose");
                this.showBoard();
                break;
            case GameState.TotemMove:
                System.out.println("totemMove");
                if(this.gameFacade.totemIsStuck(totem)){
                    this.showAvailableStuckTotemPositions(totem);
                } else{
                    this.showAvailableTotemPositions(totem);
                }
                break;
            case GameState.PlacePawn:
                System.out.println("placePAWN");
                if(gameFacade.totemIsStuck(totem)){
                    this.showAvailableStuckTotemPawnPosition(totem);
                }
                this.showAvailablePawnPositions((totem));
                break;
            case GameState.WaitForNextRound:
                this.showBoard();
                break;
        }
    }

    public Piece getBoardPieceAt(int x, int y){
        return this.boardCopy.getBoard()[x][y];
    }
}
