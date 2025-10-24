package elmos.oxono.view;

import elmos.oxono.model.GameFacade;
import elmos.oxono.model.PieceColor;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class BlackPawnsView implements PropertyChangeListener {
    VBox pawnsCtn;
    Label crossPawnsCount;
    Label circlePawnsCount;
    ImageView crossPawnsImage = new ImageView(Images.BLACK_CROSS_PAWN);
    ImageView circlePawnsImage = new ImageView(Images.BLACK_CIRCLE_PAWN);
    Image boardImage =  Images.WHITE_RIGHT_PAWNS;
    double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    GameFacade gameFacade;

    BackgroundSize backgroundSize = new BackgroundSize(screenHeight/4,screenHeight/2,false,false,false, false);
    public BlackPawnsView(){
        this.crossPawnsCount = new Label();
        this.circlePawnsCount = new Label();
        this.pawnsCtn = new VBox();
    }

    public BlackPawnsView(GameFacade gameFacade){
        this.crossPawnsCount = new Label();
        this.circlePawnsCount = new Label();
        this.pawnsCtn = new VBox();
        this.gameFacade = gameFacade;
        gameFacade.addObserver(this);
    }
    public void setup(HBox gamePane, int initialCrossPawnsCount, int initialCirclePawnsCount){
        this.crossPawnsImage.setFitHeight(screenHeight/7);
        this.crossPawnsImage.setFitWidth(screenHeight/7);
        this.circlePawnsImage.setFitHeight(screenHeight/7);
        this.circlePawnsImage.setFitWidth(screenHeight/7);
        this.crossPawnsCount.setText(Integer.toString(initialCrossPawnsCount));
        this.crossPawnsCount.setFont(new Font(screenHeight/28));
        this.circlePawnsCount.setFont(new Font(screenHeight/28));
        this.circlePawnsCount.setText(Integer.toString(initialCirclePawnsCount));
        this.pawnsCtn.getChildren().addAll(crossPawnsImage, crossPawnsCount, circlePawnsImage, circlePawnsCount);
        this.pawnsCtn.setAlignment(Pos.CENTER);

        BackgroundImage backgroundImage = new BackgroundImage(boardImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);
        this.pawnsCtn.setBackground(new Background(backgroundImage));
        this.pawnsCtn.setMinWidth(screenHeight/4);
        gamePane.getChildren().add(this.pawnsCtn);
    }

    public void update(int crossPawnsCount, int circlePawnsCount, PieceColor playerColor){
        this.crossPawnsCount.setText(Integer.toString(crossPawnsCount));
        this.circlePawnsCount.setText(Integer.toString(circlePawnsCount));
        if(playerColor == null){
            this.boardImage = Images.WHITE_RIGHT_PAWNS;
        } else if(playerColor.equals(PieceColor.PINK)){
            this.boardImage = Images.WHITE_RIGHT_PAWNS;
        } else {
            this.boardImage= Images.BLACK_RIGHT_PAWNS;
        }
        BackgroundImage backgroundImage = new BackgroundImage(boardImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize);
        this.pawnsCtn.setBackground(new Background(backgroundImage));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.update(this.gameFacade.getBlackCrossPawnsCount(), this.gameFacade.getBlackCirclePawnsCount(), null);
    }
}
