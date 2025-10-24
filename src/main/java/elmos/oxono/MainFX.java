package elmos.oxono;

import elmos.oxono.model.GameFacade;
import elmos.oxono.view.ViewCMD;
import elmos.oxono.view.ViewFX;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        GameFacade gameFacade = new GameFacade();
        ViewCMD view = new ViewCMD(gameFacade);
        ViewFX viewFX = new ViewFX(view, gameFacade, stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
