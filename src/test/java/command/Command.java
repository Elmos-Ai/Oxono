package command;

import elmos.oxono.model.PieceType;

public interface Command {
    PieceType execute();
    PieceType unexecute();
}
