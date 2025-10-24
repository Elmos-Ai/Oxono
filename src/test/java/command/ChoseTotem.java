package command;

import elmos.oxono.model.PieceType;

public class ChoseTotem implements Command {

    private PieceType previousTotem;
    private PieceType currentTotem;
    public ChoseTotem(PieceType totem, PieceType previousTotem){
        this.currentTotem = totem;
        this.previousTotem = previousTotem;
    }
    @Override
    public PieceType execute() {
        return this.currentTotem;
    }

    @Override
    public PieceType unexecute() {
        return this.previousTotem;
    }
}
