package elmos.oxono.model;

import elmos.oxono.model.command.MoveTotem;

import java.util.List;
import java.util.Random;

public class IA extends Player {
    private Board boardCopy;
    private GameFacade gameFacade;
    private Game game;
    private int totemX = -1;
    private int totemY = -1;
    private PieceType totem;
    private Bag bag;
    private Random rd = new Random();

    /**
     * Initializes an AI player with the specified color and game facade.
     *
     * @param color The color of the AI player (either {@link PieceColor#PINK} or {@link PieceColor#BLACK}).
     * @param gameFacade The game facade used to interact with the game state.
     */
    public IA(PieceColor color, GameFacade gameFacade) {
        super(color);
        this.gameFacade = gameFacade;
        this.boardCopy = new Board();
        this.bag = new Bag();
        this.update();
    }

    /**
     * Makes the AI player take its turn. This includes choosing a totem, moving it,
     * and placing a pawn.
     */
    public void playTurn() {
        this.update();
        this.totem = this.choseTotem();
        this.gameFacade.choseTotem(this.totem);
        int[] moveTotemTo = this.moveTotem(totem);
        this.gameFacade.moveTotem(moveTotemTo[0], moveTotemTo[1]);
        this.update();
        totemX = moveTotemTo[0];
        totemY = moveTotemTo[1];
        int[] placePawnIn = this.placePawn(totem);
        this.gameFacade.placePawn(color, placePawnIn[0], placePawnIn[1]);
    }

    private void update() {
        for (int i = 0; i < boardCopy.getBoard().length; i++) {
            for (int j = 0; j < boardCopy.getBoard().length; j++) {
                this.boardCopy.getBoard()[i][j] = this.gameFacade.getBoardPieceAt(i, j);
            }
        }
        this.game = new Game(this.boardCopy, this.bag);
    }

    private PieceType choseTotem() {
        boolean choice = rd.nextBoolean();
        if (choice) {
            if (this.color.equals(PieceColor.PINK) && this.gameFacade.getPinkCrossPawnsCount() > 0) {
                return PieceType.CROSS;
            } else if (this.color.equals(PieceColor.BLACK) && this.gameFacade.getBlackCrossPawnsCount() > 0) {
                return PieceType.CROSS;
            }
        } else {
            if (this.color.equals(PieceColor.PINK) && this.gameFacade.getPinkCirclePawnsCount() > 0) {
                return PieceType.CIRCLE;
            } else if (this.color.equals(PieceColor.BLACK) && this.gameFacade.getBlackCirclePawnsCount() > 0) {
                return PieceType.CIRCLE;
            }
        }
        if (choice) {
            return PieceType.CIRCLE;
        } else {
            return PieceType.CROSS;
        }
    }

    private int[] moveTotem(PieceType totem) {
        for (int i = 0; i < boardCopy.getBoard().length; i++) {
            for (int j = 0; j < boardCopy.getBoard().length; j++) {
                if (this.gameFacade.getBoardPieceAt(i, j) instanceof Totem && this.gameFacade.getBoardPieceAt(i, j).getSymbol() == totem) {
                    totemX = i;
                    totemY = j;
                    break;
                }
            }
        }
        List<int[]> availablePos;
        if (this.gameFacade.totemIsStuck(this.totem)) {
            availablePos = this.boardCopy.getAvailableStuckTotemPositions(totemX, totemY);
        } else {
            availablePos = this.boardCopy.getAvailableTotemPositions(totemX, totemY);
        }
        int listIndex = rd.nextInt(availablePos.size());
        return availablePos.get(listIndex);
    }

    private int[] placePawn(PieceType totem) {
        List<int[]> availablePos;
        if (this.gameFacade.totemIsStuck(totem)) {
            availablePos = this.boardCopy.getAvailableStuckTotemPawnPositions(totemX, totemY);
        } else {
            availablePos = this.boardCopy.getAvailablePawnPositions(totemX, totemY);
        }
        if (availablePos.isEmpty()) {
            System.out.println("no available positions");
        }
        int listIndex = rd.nextInt(availablePos.size());
        this.totemX = -1;
        this.totemY = -1;
        return availablePos.get(listIndex);
    }
}
