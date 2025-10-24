package command;

import elmos.oxono.model.Game;
import elmos.oxono.model.PieceColor;
import elmos.oxono.model.PieceType;
import elmos.oxono.model.Totem;

public class PlacePawn implements Command {
    Game game;
    private Totem totem;
    private int x;
    private int y;
    private PieceType pawnSymbol;
    private PieceColor playerColor;

    public PlacePawn(Game game,PieceColor playerColor, Totem totem, int x, int y){
        this.x = x;
        this.y = y;
        this.game = game;
        this.pawnSymbol = totem.getSymbol();
        this.playerColor = playerColor;
        this.totem = totem;
    }
    @Override
    public PieceType execute() {
        this.game.placePawn(this.pawnSymbol, this.playerColor, this.x, this.y);
        return this.totem.getSymbol();
    }
    @Override
    public PieceType unexecute() {
        this.game.removePawn(x, y);
        return this.totem.getSymbol();
    }
}
