package elmos.oxono.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class StuckTotemTest {
    GameFacade gameFacade = new GameFacade();
    Bag bag = new Bag();
    Board board = new Board();
    Game game = new Game(board, bag);


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
    public List<int[]> getAvailableStuckTotemPositions(int x, int y){
        List<int[]> availablePositions = new ArrayList<>();
        int directionCount = 0;
        int i = 1;
        int currentY = y;
        int currentX = x;

        do{
            if(directionCount == 0 &&  isInBoard(currentX, y - i)){
                currentY = y - i;
            } else if (directionCount == 1 &&  isInBoard(currentX, y + i)){
                currentY = y + i;
            } else if (directionCount == 2 && isInBoard(x - i, currentY)){
                currentX = x - i;
            } else if (directionCount == 3 && isInBoard(x + i, currentY)){
                currentX = x + i;
            } else {
                directionCount+= 1;
                i = 1;
                currentX = x;
                currentY = y;
                continue;
            }

            if(this.board.getBoard()[currentX][currentY] == null){
                System.out.println("position is available");
                availablePositions.add(new int[]{currentX, currentY});
                directionCount += 1;
                i = 1;
                currentX = x;
                currentY = y;
                continue;
            }

            i += 1;
        } while(directionCount < 4);
        return availablePositions;
    }

    public boolean isInBoard(int x, int y){
        return x < this.board.getBoard().length && x >= 0 && y < this.board.getBoard().length && y >= 0;
    }
    @Test
    void stuckTotem(){
        game.moveTotem(this.bag.getTotemCircle(), 0, 4);
        game.placePawn(PieceType.CROSS, PieceColor.PINK, 0, 3);
        game.placePawn(PieceType.CROSS, PieceColor.PINK, 0, 5);
        game.placePawn(PieceType.CROSS, PieceColor.PINK, 1, 4);
//        game.placePawn(PieceType.CROSS, PieceColor.PINK, 3, 2);
        showBoard(this.board.getBoard());
        List<int[]> boardPos = getAvailableStuckTotemPositions(0, 4);
        for (int[] avb: boardPos){
            System.out.println(avb[0] + " " + avb[1]);
        }
    }
}
