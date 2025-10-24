package elmos.oxono.model.controller;

import elmos.oxono.model.*;
import elmos.oxono.view.ViewCMD;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Scanner;

public class ControllerCMDTest implements PropertyChangeListener {
    private GameFacade gameFacade;
    private GameState gameState;
    final Scanner IN = new Scanner(System.in);
    String input;
    Player player = null;
    Player bot = null;
    Player currentPlayer = null;

    public ControllerCMDTest(GameFacade gameFacade){
        this.gameFacade = gameFacade;
        this.gameFacade.addObserver(this);
    }
    public void run(){
        do{
            switch (this.gameState){
                case GameState.ColorChoice -> this.choseColor("1");
                case GameState.TotemChose -> this.choseTotem("x");
                case GameState.TotemMove -> this.moveTotem("2 0");
                case GameState.PlacePawn -> this.placePawn("1 0");
                case GameState.PinkWin -> System.out.println("pink player won");
                case GameState.BlackWin -> System.out.println("black player won");
            }
        } while(!(this.gameState.equals(GameState.PinkWin) || this.gameState.equals(GameState.BlackWin)));
    }
    private void choseColor(String input){
        System.out.println("Choose your color:  1 = PINK || 2 = BLACK");

        do{
//            input = IN.nextLine();
            try{
                if(Integer.parseInt(input) == 1){
                    this.player = new Player(PieceColor.PINK);
                    this.bot = new Player(PieceColor.BLACK);
                    this.currentPlayer = player;
                } else if(Integer.parseInt(input) == 2){
                    this.player = new Player(PieceColor.BLACK);
                    this.bot = new Player(PieceColor.PINK);
                    this.currentPlayer = bot;
                } else {
                    System.out.println("enter 1 or 2");
                }
            }catch(Exception e){
                System.out.println("enter valid value");
            }
        }while(player == null);

        this.gameFacade.playerColorChosed(this.currentPlayer.getColor());
    }
    private void choseTotem(String input){
        System.out.println("current player: " + currentPlayer.getColor());
        System.out.println("choose the totem to move: x = Cross || o = circle");

        do{
//            input = IN.nextLine();
            try{
                if(input.equalsIgnoreCase("undo") && this.gameFacade.undo(this.gameState)){
                    this.switchPlayer();
                    break;
                } else if (input.equalsIgnoreCase("redo") && this.gameFacade.redo(this.gameState)) {
                    break;
                } else if(input.equals("x")){
                    this.gameFacade.choseTotem(PieceType.CROSS);
                } else if(input.equals("o")){
                    this.gameFacade.choseTotem(PieceType.CIRCLE);
                } else {
                    System.out.println("enter x or o");
                    break;
                }
            }catch(Exception e){
                System.out.println("enter valid value");
            }
        }while(input != "x" && input != "o");
    }
    private void moveTotem(String input){
        System.out.println("enter position to move totem to (ex: 4 5)");
        boolean totemIsMoved = false;

        do{
//            input = IN.nextLine();
            String[] values = input.split( " ");
            try{
                if(input.equalsIgnoreCase("undo") && this.gameFacade.undo(this.gameState)){
                    break;
                } else if (input.equalsIgnoreCase("redo") && this.gameFacade.redo(this.gameState)) {
                    break;
                }
                if(values.length < 2){
                    throw new IllegalArgumentException("enter 2 values");
                }
                totemIsMoved = this.gameFacade.moveTotem(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                if(!totemIsMoved){
                    System.out.println("given position not available for totem");
                    break;
                }
            } catch (Exception e){
//                System.out.println("enter valid values");
                throw e;
//                break;
            }
        } while(!totemIsMoved);
    }
    private void placePawn(String input){
        System.out.println("enter position to move pawn to (ex:3 1)");
        boolean pawnIsPlaced = false;

        do{
//            input = IN.nextLine();
            String[] values = input.split( " ");
            try{
                if(input.equalsIgnoreCase("undo") && this.gameFacade.undo(this.gameState)){
                    break;
                } else if (input.equalsIgnoreCase("redo") && this.gameFacade.redo(this.gameState)) {
                    this.switchPlayer();
                    break;
                }
                if(values.length < 2){
                    throw new IllegalArgumentException("enter 2 values");
                }
                pawnIsPlaced = this.gameFacade.placePawn(this.currentPlayer.getColor(), Integer.parseInt(values[0]), Integer.parseInt(values[1]));
                if(!pawnIsPlaced){
                    System.out.println("given position not available for pawn");
                    break;
                }
            }catch(Exception e){
                System.out.println("enter valid values");
                break;
            }
        }while(!pawnIsPlaced);
    }
    private void switchPlayer(){
        if (this.currentPlayer.equals(player)) {
            this.currentPlayer = bot;
        } else {
            this.currentPlayer = player;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.gameState = (GameState) evt.getOldValue();
    }

    @Test
    void controllerTest(){
        this.choseColor("1");
        this.choseTotem("undo");
        this.choseTotem("x");
        this.moveTotem("undo");
        this.choseTotem("o");
        this.moveTotem("3 1");
        this.placePawn("4 1");
        this.choseTotem("x");
    }
}
