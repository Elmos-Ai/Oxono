package elmos.oxono.view;

import elmos.oxono.controller.ControllerFX;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.Optional;

public class WinView {
    private BorderPane winCTN = new BorderPane();
    private HBox winChoice;
    private Button seeBoardButton;
    private Button newGameButton;
    private ControllerFX controllerFX;
    public WinView(ControllerFX controllerFX){
        this.controllerFX = controllerFX;
    }

    public void setup(){
        seeBoardButton = new Button("see board");
        seeBoardButton.getStyleClass().add("seeBoard");
        seeBoardButton.setOnAction(this.controllerFX);
        newGameButton = new Button("new game");
        newGameButton.getStyleClass().add("newGame");
        newGameButton.setOnAction(this.controllerFX);
        winChoice = new HBox(seeBoardButton, newGameButton);
        winChoice.setAlignment(Pos.CENTER);
        winCTN.setCenter(winChoice);
    }

    public void show(Scene scene, String playerWin){
//        Label winDialog = new Label(playerWin);
//        this.winCTN.setTop(winDialog);
//        winDialog.setAlignment(Pos.BASELINE_CENTER);
////        winDialog.layoutXProperty().bind(winCTN.widthProperty().subtract(winDialog.widthProperty()).divide(2));
//        winDialog.setFont(new Font(60));
//        scene.setRoot(this.winCTN);
        System.out.println("win view setup to the window ");
        ButtonType newGameButton = new ButtonType("new game", ButtonBar.ButtonData.OK_DONE);
        ButtonType seeBoardButton= new ButtonType("see board", ButtonBar.ButtonData.CANCEL_CLOSE);

        System.out.println(newGameButton.getButtonData());
        Alert alert = new Alert(Alert.AlertType.WARNING,
                playerWin,
                newGameButton,
                seeBoardButton);
        alert.setTitle("game end");
        Optional<ButtonType> result = alert.showAndWait();
        controllerFX.handleAlertClick(result.get().getButtonData());
    }
}
