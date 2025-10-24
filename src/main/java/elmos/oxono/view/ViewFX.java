package elmos.oxono.view;

import elmos.oxono.controller.ControllerFX;
import elmos.oxono.model.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class ViewFX implements PropertyChangeListener {
    private Board boardCopy = new Board();
    private ControllerFX controllerFX;
    private BorderPane gameView = new BorderPane();
    private ColorChoiceView colorChoiceView;
    private Scene scene;
    private ViewCMD CMDView;
    private GameFacade gameFacade;
    private PinkPawnsView pinkPawnsView = new PinkPawnsView();
    private BlackPawnsView blackPawnsView = new BlackPawnsView();
    private BoardView boardView;
    private WinView winView;
    private GameManagementView gameManagementView;
    private GameModeChoiceView gameModeChoiceView;
    private Stage stage;
    private HBox gameElementCTN = new HBox();
    private VBox gameViewCTN = new VBox();
    private Button nextRoundButton;
    private MenuBar menuBar = new MenuBar();
    private double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public ViewFX(ViewCMD CMDView, GameFacade gameFacade, Stage stage) {
        this.stage = stage;
        this.CMDView = CMDView;
        this.gameFacade = gameFacade;
        this.controllerFX = new ControllerFX(this.gameFacade);
        this.boardView = new BoardView(this.controllerFX);
        this.winView = new WinView(this.controllerFX);
        this.colorChoiceView = new ColorChoiceView(this.controllerFX);
        this.gameManagementView = new GameManagementView(this.controllerFX);
        this.gameModeChoiceView = new GameModeChoiceView(this.controllerFX);
        this.setup();
        this.scene = new Scene(this.gameView, 500, 500);
        stage.setScene(this.scene);
        this.gameFacade.addObserver(this);
    }

    private void setup(){
        for (int i = 0; i < this.boardCopy.getBoard().length; i++) {
            for (int j = 0; j < this.boardCopy.getBoard()[0].length; j++) {
                this.boardCopy.getBoard()[i][j] = this.gameFacade.getBoardPieceAt(i, j);
            }
        }
        nextRoundButton = new Button("NEXT ROUND");
        nextRoundButton.setOnAction(this.controllerFX);
        nextRoundButton.getStyleClass().add("nextRound");
        nextRoundButton.setFont(new Font(screenHeight/48));
        nextRoundButton.setStyle("-fx-background-radius: 27px; " + "-fx-background-color: #ffcc00; ");

        Menu menu = new Menu("Menu");
        MenuItem giveUpMenuItem = new MenuItem("give up");
        giveUpMenuItem.setOnAction((e) -> {
            controllerFX.handleGiveUp();
        });
        MenuItem openPinkPlayersPawns = new MenuItem("pink players pawn");
        openPinkPlayersPawns.setOnAction((e) -> {
            controllerFX.handleOpenPinkPlayersPawns();
        });
        MenuItem openBlackPlayersPawns = new MenuItem("black players pawns");
        openBlackPlayersPawns.setOnAction((e) -> {
            controllerFX.handleOpenBlackPlayersPawns();
        });
        menu.getItems().add(openPinkPlayersPawns);
        menu.getItems().add(openBlackPlayersPawns);
        menu.getItems().add(giveUpMenuItem);
        this.menuBar.getMenus().add(menu);
        this.gameViewCTN.getChildren().add(this.menuBar);

        this.gameViewCTN.getChildren().add(nextRoundButton);
        this.gameViewCTN.setAlignment(Pos.TOP_CENTER);
        this.gameViewCTN.setSpacing(screenHeight/14);

        this.pinkPawnsView.setup(gameElementCTN, this.gameFacade.getPinkCrossPawnsCount(), this.gameFacade.getPinkCirclePawnsCount());
        this.boardView.setup(this.boardCopy, gameElementCTN);
        this.blackPawnsView.setup(gameElementCTN, this.gameFacade.getBlackCrossPawnsCount(), this.gameFacade.getBlackCirclePawnsCount());

        this.gameElementCTN.setMinHeight(screenHeight/2);
        this.gameElementCTN.setAlignment(Pos.CENTER);

        this.gameViewCTN.getChildren().add(gameElementCTN);
        this.gameManagementView.setup(gameViewCTN);

        this.colorChoiceView.setup();
        this.winView.setup();

        this.gameModeChoiceView.setup();
        this.gameView.setCenter(this.gameViewCTN);
        BackgroundSize backgroundSize = new BackgroundSize(1, 1,true,true,false, false);
        BackgroundImage backgroundImage = new BackgroundImage(Images.BACKGROUND_IMAGE,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);
        this.gameView.setBackground(new Background(backgroundImage));
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        GameState gameState = (GameState) evt.getOldValue();
        if(!Objects.equals(evt.getPropertyName(), "SETUP")){
            for (int i = 0; i < this.boardCopy.getBoard().length; i++) {
                for (int j = 0; j < this.boardCopy.getBoard()[0].length; j++) {
                    Piece boardPiece = this.CMDView.getBoardPieceAt(i, j);
                    if(boardPiece != this.boardCopy.getBoard()[i][j]){
                        this.boardCopy.getBoard()[i][j] = this.CMDView.getBoardPieceAt(i, j);
                        this.boardView.update(boardPiece, i, j);
                    }
                }
            }
            this.pinkPawnsView.update(this.gameFacade.getPinkCrossPawnsCount(), this.gameFacade.getPinkCirclePawnsCount(), this.gameFacade.getCurrentPlayer());
            this.blackPawnsView.update(this.gameFacade.getBlackCrossPawnsCount(), this.gameFacade.getBlackCirclePawnsCount(), this.gameFacade.getCurrentPlayer());

            switch(gameState){
                case GameState.GameModChoice -> {
                    this.gameModeChoiceView.show(this.scene, this.stage);
                }
                case GameState.PinkWin -> {
                    this.winView.show(this.scene, "pink player won!");
                }
                case GameState.BlackWin -> {
                    this.winView.show(this.scene, "black player won!");
                }
                case GameState.ShowBoard -> {
                    this.scene.setRoot(this.gameView);
                }
                case GameState.ColorChoice -> {
                    this.colorChoiceView.show(this.scene);
                }
                case GameState.PlacePawn, GameState.TotemChose -> {
                    if(evt.getNewValue() == PieceType.NONE){
                        this.scene.setRoot(gameView);
                        this.stage.setMaximized(true);
                    }
                }
                case GameState.NewGame -> {

                }
            }
        } else {
            this.gameModeChoiceView.show(this.scene, this.stage);
        }
    }
}
