package elmos.oxono.view;

import elmos.oxono.controller.ControllerFX;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameModeChoiceView {
    Button twoPlayerButton = new Button();
    Button onePlayerButton = new Button();
    Button twoBotButton = new Button();
    VBox gameMenuCTN;
    HBox imagesCTN;
    Label gameTitle;
    Label prompt = new Label("CHOSE YOUR GAMEMOD");
    ControllerFX controllerFX;

    public GameModeChoiceView(ControllerFX controllerFX){
        this.controllerFX = controllerFX;
    }

    public void setup(){
        this.twoPlayerSetup();
        this.onePlayerSetup();
        this.twoBotSetup();
        this.gameTitleSetup();
        this.gameMenuCTN = new VBox(gameTitle, prompt, twoPlayerButton, onePlayerButton, twoBotButton);
        this.gameMenuCTN.setAlignment(Pos.CENTER);
        this.gameMenuCTN.setSpacing(20);
    }

    private void gameTitleSetup(){
        gameTitle = new Label("OXONO");
        gameTitle.setFont(new Font(50));
        gameTitle.setMinHeight(50);
    }
    private void twoPlayerSetup(){
        ImageView playerImage = new ImageView(Images.PLAYER);
        ImageView playerImage2 = new ImageView(Images.PLAYER);
        playerImage2.setFitWidth(50);
        playerImage2.setFitHeight(50);
        playerImage.setFitWidth(50);
        playerImage.setFitHeight(50);
        imagesCTN = new HBox(playerImage, playerImage2);
        imagesCTN.setSpacing(30);
        twoPlayerButton.setGraphic(imagesCTN);
        twoPlayerButton.getStyleClass().add("twoPlayer");
        twoPlayerButton.setOnAction(this.controllerFX);
        this.twoPlayerButton.setPadding(new Insets(10, 50, 10, 50));
    }
    private void onePlayerSetup(){
        ImageView playerImage = new ImageView(Images.PLAYER);
        ImageView botImage = new ImageView(Images.BOT);
        botImage.setFitWidth(50);
        botImage.setFitHeight(50);
        playerImage.setFitWidth(50);
        playerImage.setFitHeight(50);
        imagesCTN = new HBox(playerImage, botImage);
        imagesCTN.setSpacing(30);
        onePlayerButton.setGraphic(imagesCTN);
        onePlayerButton.getStyleClass().add("onePlayer");
        onePlayerButton.setOnAction(this.controllerFX);
        this.onePlayerButton.setPadding(new Insets(10, 50, 10, 50));
    }
    private void twoBotSetup(){
        ImageView botImage = new ImageView(Images.BOT);
        ImageView botImage1 = new ImageView(Images.BOT);
        botImage.setFitWidth(50);
        botImage.setFitHeight(50);
        botImage1.setFitWidth(50);
        botImage1.setFitHeight(50);
        imagesCTN = new HBox(botImage1, botImage);
        imagesCTN.setSpacing(30);
        twoBotButton.setGraphic(imagesCTN);
        twoBotButton.getStyleClass().add("twoBot");
        twoBotButton.setOnAction(this.controllerFX);
        this.twoBotButton.setPadding(new Insets(10, 50, 10, 50));
    }
    public void show(Scene scene, Stage stage){
        scene.setRoot(this.gameMenuCTN);
        stage.setMinHeight(460);
        stage.setMinWidth(300);
    }
}
