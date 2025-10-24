package elmos.oxono.view;

import elmos.oxono.controller.ControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.*;

public class GameManagementView {
    HBox gameManagementCTN;
    ControllerFX controllerFX;
    Button undoButton;
    Button redoButton;
    Button giveUpButton;

    public GameManagementView(ControllerFX controllerFX) {
        this.gameManagementCTN = new HBox();
        this.controllerFX = controllerFX;
    }

    public void setup(VBox gamePane){
        this.undoButton = new Button();
        this.undoButton.getStyleClass().add("undo");
        this.undoButton.setOnAction(this.controllerFX);
        ImageView undoImage = new ImageView(Images.UNDO_ARROW);
        double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        undoImage.setFitWidth(screenHeight/28);
        undoImage.setFitHeight(screenHeight/28);
        this.undoButton.setGraphic(undoImage);
        this.undoButton.setStyle("-fx-background-radius: 27px; " + "-fx-background-color: #ffcc00; ");

        ImageView redoImage = new ImageView(Images.REDO_ARROW);
        redoImage.setFitWidth(screenHeight/28);
        redoImage.setFitHeight(screenHeight/28);
        this.redoButton = new Button();
        this.redoButton.getStyleClass().add("redo");
        this.redoButton.setOnAction(this.controllerFX);
        this.redoButton.setGraphic(redoImage);
        this.redoButton.setStyle("-fx-background-radius: 27px; " + "-fx-background-color: #ffcc00; ");

        this.gameManagementCTN.getChildren().add(this.undoButton);
        this.gameManagementCTN.getChildren().add(this.redoButton);
        gamePane.getChildren().add(this.gameManagementCTN);
        gameManagementCTN.setAlignment(Pos.CENTER);
        gameManagementCTN.setSpacing(screenHeight/26);
//        gameManagementCTN.setMinHeight();
    }
}
