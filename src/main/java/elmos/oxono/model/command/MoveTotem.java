package elmos.oxono.model.command;

import elmos.oxono.model.Game;
import elmos.oxono.model.PieceType;
import elmos.oxono.model.Totem;

public class MoveTotem implements Command {

    Game game;
    private Totem totem;
    private int currentX;
    private int currentY;
    private int previousX;
    private int previousY;
    public MoveTotem(Game game, Totem totem, int x, int y){
        this.game = game;
        this.totem = totem;
        this.previousX = this.totem.getCurrentX();
        this.previousY = this.totem.getCurrentY();
        this.currentX = x;
        this.currentY = y;
    }
    @Override
    public PieceType execute(){
        this.game.moveTotem(totem, currentX, currentY);
        return totem.getSymbol();
    }
    @Override
    public PieceType unexecute() {
        this.game.moveBackTotem(totem, previousX, previousY);
        return totem.getSymbol();
    }
}
