package elmos.oxono.view;

import elmos.oxono.controller.ControllerFX;
import elmos.oxono.model.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.awt.*;

public class BoardView {
    private GridPane boardFX;
    private int[][] boardFXIndex;
    private ControllerFX controllerFX;
    String TOTEM_CLASS = "totem";
    String PAWN_CLASS = "pawn";
    String CIRCLE_CLASS = "o";
    String CROSS_CLASS = "x";
    String PINK_CLASS = "pink";
    String BLACK_CLASS = "black";
    String NONE_CLASS = "none";
    String AVAILABLE_CLASS = "available";
    double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public BoardView(ControllerFX controllerFX){
        this.controllerFX = controllerFX;
        this.boardFX = new GridPane();
    }
    public void setup(Board board, HBox gamePane){
        this.boardFXIndex = new int[board.getBoard().length][board.getBoard()[0].length];
        int index = 0;
        for (int i = 0; i < board.getBoard().length; i++) {
            for (int j = 0; j < board.getBoard()[0].length; j++) {
                this.boardFXIndex[i][j] = index;
                Button button = new Button();
                button.setBackground(null);
                button.setOnAction(this.controllerFX);
                boardFX.add(button, j, i);
                this.update(board.getBoard()[i][j], i, j);
                index += 1;
            }
        }

        BackgroundSize backgroundSize = new BackgroundSize(screenHeight/2,screenHeight/2 ,false,false,false, false);
        BackgroundImage backgroundImage = new BackgroundImage(Images.BOARD,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);
        this.boardFX.setAlignment(Pos.CENTER);
        this.boardFX.setBackground(new Background(backgroundImage));
        this.boardFX.setMinWidth(screenHeight/2);
        gamePane.getChildren().add(this.boardFX);
    }

    public void update(Piece piece, int x, int y){
        ImageView imageView = new ImageView();
        imageView.setFitHeight(screenHeight/18);
        imageView.setFitWidth(screenHeight/18);
        Button nodeToUpdate = (Button) this.boardFX.getChildren().get(this.boardFXIndex[x][y]);
        if(nodeToUpdate.getStyleClass().size() < 6){
            for (int i = 1; i < 7; i++) {
                nodeToUpdate.getStyleClass().add(NONE_CLASS);
            }
        }
        if(piece == null){
            nodeToUpdate.setText("");
            nodeToUpdate.getStyleClass().set(1,NONE_CLASS);
            nodeToUpdate.getStyleClass().set(2,NONE_CLASS);
            nodeToUpdate.getStyleClass().set(3,NONE_CLASS);
            nodeToUpdate.getStyleClass().set(4,Integer.toString(x));
            nodeToUpdate.getStyleClass().set(5,Integer.toString(y));
            nodeToUpdate.setGraphic(imageView);
        }else if (piece.getSymbol().equals(PieceType.AVAILABLE)){
            nodeToUpdate.getStyleClass().set(1, AVAILABLE_CLASS);
            nodeToUpdate.getStyleClass().set(4, Integer.toString(x));
            nodeToUpdate.getStyleClass().set(5, Integer.toString(y));
            imageView.setImage(Images.AVAILABLE_POSITION);
        }else if(piece instanceof Totem){
            nodeToUpdate.getStyleClass().set(1,TOTEM_CLASS);
            if(piece.getSymbol().equals(PieceType.CIRCLE)){
                nodeToUpdate.setText("");
                nodeToUpdate.getStyleClass().set(2,CIRCLE_CLASS);
                imageView.setImage(Images.CIRCLE_TOTEM);
            } else {
                nodeToUpdate.setText("");
                nodeToUpdate.getStyleClass().set(2,CROSS_CLASS);
                imageView.setImage(Images.CROSS_TOTEM);
            }
        } else if(piece instanceof Pawn){
            nodeToUpdate.getStyleClass().set(1,PAWN_CLASS);
            if(((Pawn) piece).getColor().equals(PieceColor.PINK)){
                nodeToUpdate.getStyleClass().set(2,PINK_CLASS);
                if(((Pawn) piece).getSymbol().equals(PieceType.CIRCLE)){
                    nodeToUpdate.getStyleClass().set(3,CIRCLE_CLASS);
                    imageView.setImage(Images.PINK_CIRCLE_PAWN);
                }else{
                    nodeToUpdate.getStyleClass().set(3,CROSS_CLASS);
                    imageView.setImage(Images.PINK_CROSS_PAWN);
                }
            }else{
                nodeToUpdate.getStyleClass().set(2,BLACK_CLASS);
                if(((Pawn) piece).getSymbol().equals(PieceType.CIRCLE)){
                    nodeToUpdate.getStyleClass().set(3,CIRCLE_CLASS);
                    imageView.setImage(Images.BLACK_CIRCLE_PAWN);
                }else{
                    nodeToUpdate.getStyleClass().set(3,CROSS_CLASS);
                    imageView.setImage(Images.BLACK_CROSS_PAWN);
                }
            }
            nodeToUpdate.setText("");
            nodeToUpdate.getStyleClass().set(4,Integer.toString(x));
            nodeToUpdate.getStyleClass().set(5,Integer.toString(y));
        }
        nodeToUpdate.setGraphic(imageView);
    }
}
