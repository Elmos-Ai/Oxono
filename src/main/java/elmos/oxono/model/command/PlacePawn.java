package elmos.oxono.model.command;

import elmos.oxono.model.Game;
import elmos.oxono.model.PieceColor;
import elmos.oxono.model.PieceType;
import elmos.oxono.model.Totem;

public class PlacePawn implements Command {
    Game game;
    private int x;
    private int y;
    private PieceType pawnSymbol;
    private PieceColor playerColor;

    public PlacePawn(Game game,PieceColor playerColor, PieceType totemSymbol, int x, int y){
        this.x = x;
        this.y = y;
        this.game = game;
        this.pawnSymbol = totemSymbol;
        this.playerColor = playerColor;
    }
    @Override
    public PieceType execute() {
        this.game.placePawn(this.pawnSymbol, this.playerColor, this.x, this.y);
        return this.pawnSymbol;
    }
    @Override
    public PieceType unexecute() {
        this.game.removePawn(x, y);
        return this.pawnSymbol;
    }
}
