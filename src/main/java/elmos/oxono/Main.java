package elmos.oxono;

import elmos.oxono.controller.Controller;
import elmos.oxono.model.GameFacade;
import elmos.oxono.view.ViewCMD;

import java.lang.reflect.InvocationTargetException;

public class Main{
    public static void main(String[] args){
        GameFacade gameFacade = new GameFacade();
        ViewCMD view = new ViewCMD(gameFacade);
        Controller controller = new Controller(gameFacade);
        controller.run();
    }
}
