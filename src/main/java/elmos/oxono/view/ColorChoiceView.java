package elmos.oxono.view;

import elmos.oxono.controller.ControllerFX;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class ColorChoiceView {
    ControllerFX controllerFX;
    private VBox colorChoiceCTN  = new VBox();
    public ColorChoiceView(ControllerFX controllerFX){
        this.controllerFX = controllerFX;
    }
    public void setup(){
        Button pinkButton = new Button("pink");
        pinkButton.getStyleClass().add("pink");
        pinkButton.setOnAction(this.controllerFX);
        pinkButton.setFont(new Font(30));
        pinkButton.setStyle("-fx-background-radius: 25px; " + "-fx-background-color: #ff66c4; " + "-fx-text-fill: white; ");

        Button blackButton = new Button("black");
        blackButton.getStyleClass().add("black");
        blackButton.setOnAction(this.controllerFX);
        blackButton.setFont(new Font(30));
        blackButton.setStyle("-fx-background-radius: 25px; " + "-fx-background-color: #1e263e; " + "-fx-text-fill: white; ");

        HBox buttonsCTN  = new HBox();
        buttonsCTN.setSpacing(50);
        buttonsCTN.setAlignment(Pos.CENTER);
        buttonsCTN.getChildren().addAll(pinkButton, blackButton);

        Label colorChoiceTitle  = new Label("CHOSE YOUR COLOR");
        colorChoiceTitle.setFont(new Font(40));

        colorChoiceCTN.getChildren().add(colorChoiceTitle);
        colorChoiceCTN.getChildren().add(buttonsCTN);
        colorChoiceCTN.setAlignment(Pos.CENTER);
        colorChoiceCTN.setSpacing(60);
    }
    public void show(Scene scene){
        scene.setRoot(this.colorChoiceCTN);
    }
}
