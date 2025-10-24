package elmos.oxono.controller;

import elmos.oxono.model.*;
import elmos.oxono.view.BlackPawnsView;
import elmos.oxono.view.PinkPawnsView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ControllerFX implements EventHandler<ActionEvent>, PropertyChangeListener {
    private final GameFacade gameFacade;
    private Player currentPlayer;
    private GameState gameState;
    private Player player1;
    private Player player2;
    private String lastButtonClicked;
    Stage pinkPawnsStage = new Stage();
    Stage blackPawnsStage = new Stage();

    public ControllerFX(GameFacade gameFacade){
        this.gameFacade = gameFacade;
        this.gameFacade.addObserver(this);
        this.setup();
    }
    @Override
    public void handle(ActionEvent actionEvent) {
        Button source = (Button) actionEvent.getSource();
        lastButtonClicked = source.getStyleClass().get(1);
        if(source.getStyleClass().get(1).equals("nextRound") && this.gameState.equals(GameState.WaitForNextRound)){
            this.currentPlayer = switchCurrentPlayer();
            gameFacade.setCurrentPlayer(currentPlayer.getColor());
            this.gameFacade.nextRound();
        } else if(source.getStyleClass().get(1).equals("twoPlayer")){
            this.player1 = new Player(PieceColor.PINK);
            this.player2 = new Player(PieceColor.BLACK);
            this.currentPlayer = player1;
            this.gameFacade.playerColorChosed(currentPlayer.getColor());
        } else if(source.getStyleClass().get(1).equals("onePlayer")){
            this.gameFacade.choseColor();
        } else if(source.getStyleClass().get(1).equals("twoBot")) {
            this.player1 = new IA(PieceColor.PINK, this.gameFacade);
            this.player2 = new IA(PieceColor.BLACK, this.gameFacade);
            this.currentPlayer = player1;
            this.gameFacade.playerColorChosed(currentPlayer.getColor());
        }
        if(this.gameState.equals(GameState.PinkWin) || this.gameState.equals(GameState.BlackWin) || this.gameState.equals(GameState.ShowBoard)){
            return;
        }
        if(actionEvent.getSource().getClass() == Button.class){
            switch (source.getStyleClass().get(1)) {
                case "pink" -> {
                    this.player1 = new Player(PieceColor.PINK);
                    this.player2 = new IA(PieceColor.BLACK, this.gameFacade);
                    this.currentPlayer = player1;
                    System.out.println("pink chosed");
                    this.gameFacade.playerColorChosed(currentPlayer.getColor());
                }
                case "black" -> {
                    this.player2 = new IA(PieceColor.PINK, this.gameFacade);
                    this.player1 = new Player(PieceColor.BLACK);
                    this.currentPlayer = player2;
                    System.out.println("black chosed");
                    this.gameFacade.playerColorChosed(currentPlayer.getColor());
                }
                case "undo" -> {
                    if(this.gameState.equals(GameState.TotemChose) && !(gameFacade.getCurrentTotem().equals(PieceType.NONE) || gameFacade.getPreviousTotem().equals(PieceType.NONE))){
                        System.out.println("true, i will switch player");
                        this.currentPlayer = this.switchCurrentPlayer();
                        gameFacade.setCurrentPlayer(this.currentPlayer.getColor());
                        System.out.println(this.currentPlayer);
                    }
                    this.gameFacade.undo(gameState);
//                    System.out.println("game state has changed " + this.gameState);
                }
                case "redo" -> {
                    if(this.gameState.equals(GameState.PlacePawn)){
                        this.currentPlayer = this.switchCurrentPlayer();
                        gameFacade.setCurrentPlayer(this.currentPlayer.getColor());
                    }
                    this.gameFacade.redo(gameState);
                }
                case "totem" -> {
                    if (this.gameState.equals(GameState.TotemChose)) {
                        PieceType totemSymbol = PieceType.of(source.getStyleClass().get(2));
                        if(totemSymbol.equals(PieceType.CROSS)){
                            if(this.currentPlayer.getColor().equals(PieceColor.PINK) && this.gameFacade.getPinkCrossPawnsCount() > 0){
                                this.gameFacade.choseTotem(totemSymbol);
                            } else if(this.currentPlayer.getColor().equals(PieceColor.BLACK) && this.gameFacade.getBlackCrossPawnsCount() > 0 ){
                                this.gameFacade.choseTotem(totemSymbol);
                            } else {
                                System.out.println("no more cross pawns");
                            }
                        } else if(totemSymbol.equals(PieceType.CIRCLE)){
                            if(this.currentPlayer.getColor().equals(PieceColor.PINK) && this.gameFacade.getPinkCirclePawnsCount() > 0){
                                this.gameFacade.choseTotem(totemSymbol);
                            } else if(this.currentPlayer.getColor().equals(PieceColor.BLACK) && this.gameFacade.getBlackCirclePawnsCount() > 0){
                                this.gameFacade.choseTotem(totemSymbol);
                            } else{
                                System.out.println("no more circle pawns");
                            }
                        }
                    }
                }
                case "available" -> {
                    if (this.gameState.equals(GameState.TotemMove)) {
                        this.gameFacade.moveTotem(
                                Integer.parseInt(source.getStyleClass().get(4)),
                                Integer.parseInt(source.getStyleClass().get(5))
                        );
                    } else if (this.gameState.equals(GameState.PlacePawn)) {
                        if (this.gameFacade.placePawn(
                                this.currentPlayer.getColor(),
                                Integer.parseInt(source.getStyleClass().get(4)),
                                Integer.parseInt(source.getStyleClass().get(5))
                        )) {
                            System.out.println("pawn placed");
                            this.gameFacade.getWinner(
                                    this.currentPlayer.getColor(),
                                    Integer.parseInt(source.getStyleClass().get(4)),
                                    Integer.parseInt(source.getStyleClass().get(5)));

                        }
                    }
                }
            }
        }
    }

    public void handleAlertClick(ButtonBar.ButtonData result){
        if(result.equals(ButtonBar.ButtonData.OK_DONE)){
            this.gameFacade.resetGame();
            this.player1 = null;
            this.player2 = null;
            this.currentPlayer = null;
        } else {
            this.gameFacade.showBoard();
        }
    }
    public void handleGiveUp(){
        this.gameFacade.resetGame();
        this.player1 = null;
        this.player2 = null;
        this.currentPlayer = null;
        this.gameFacade.resetGame();
    }
    public void handleOpenPinkPlayersPawns(){
        this.pinkPawnsStage.show();
    }
    public void setup(){
        setupPinkPawnsView();
        setupBlackPawnsView();
    }
    public void setupPinkPawnsView(){
        Scene scene;
        HBox pawnsView = new HBox();
        pawnsView.setAlignment(Pos.CENTER);
        BorderPane pawnsElementsCTN = new BorderPane();
        PinkPawnsView pinkPawnsView = new PinkPawnsView(this.gameFacade);
        pinkPawnsView.setup(pawnsView, this.gameFacade.getPinkCrossPawnsCount(), this.gameFacade.getPinkCirclePawnsCount());
        pawnsElementsCTN.setCenter(pawnsView);
        scene = new Scene(pawnsElementsCTN);
        pinkPawnsStage.setScene(scene);
    }
    public void setupBlackPawnsView(){
        System.out.println("black players pawns clicked");
        Scene scene;
        HBox pawnsView = new HBox();
        pawnsView.setAlignment(Pos.CENTER);
        BorderPane pawnsElementsCTN = new BorderPane();
        BlackPawnsView blackPawnsView = new BlackPawnsView(this.gameFacade);
        blackPawnsView.setup(pawnsView, this.gameFacade.getBlackCrossPawnsCount(), this.gameFacade.getBlackCirclePawnsCount());
        pawnsElementsCTN.setCenter(pawnsView);
        scene = new Scene(pawnsElementsCTN);
        blackPawnsStage.setScene(scene);
    }
    public void handleOpenBlackPlayersPawns(){

        blackPawnsStage.show();
    }
    public Player switchCurrentPlayer(){
        if(currentPlayer.equals(player1)){
            return this.player2;
        } else {
            return this.player1;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.gameState = (GameState) evt.getOldValue();

        if(this.gameState.equals(GameState.PinkWin) || this.gameState.equals(GameState.BlackWin)){
            System.out.println("win");
        }else if(this.gameState.equals(GameState.GameDraw)){
            System.out.println("game draw");
        }else if( !(lastButtonClicked ==null) && !(lastButtonClicked.equals("undo") || lastButtonClicked.equals("redo"))) {
            if (this.gameState.equals(GameState.TotemChose) && currentPlayer instanceof IA && !(player1 instanceof IA)) {
                ((IA) currentPlayer).playTurn();
                this.currentPlayer = this.switchCurrentPlayer();
                gameFacade.setCurrentPlayer(currentPlayer.getColor());
                this.gameFacade.nextRound();
            } else if (this.player1 instanceof IA && this.player2 instanceof IA && gameState.equals(GameState.TotemChose)) {
                ((IA) this.currentPlayer).playTurn();
            }
        }
    }
}
