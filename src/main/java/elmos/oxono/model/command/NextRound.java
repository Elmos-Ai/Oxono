package elmos.oxono.model.command;

import elmos.oxono.model.GameFacade;
import elmos.oxono.model.PieceType;

public class NextRound implements Command{
    GameFacade gameFacade;
    public NextRound(GameFacade gameFacade){
        this.gameFacade = gameFacade;
    }
    @Override
    public PieceType execute() {
        return null;
    }

    @Override
    public PieceType unexecute() {
        return null;
    }
}
