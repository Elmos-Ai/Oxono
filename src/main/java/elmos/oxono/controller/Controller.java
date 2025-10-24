package elmos.oxono.controller;

import elmos.oxono.model.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class Controller implements PropertyChangeListener {
    private GameFacade gameFacade;
    private GameState gameState;
    final Scanner IN = new Scanner(System.in);
    String input;
    Player player1 = null;
    Player player2 = null;
    Player currentPlayer = null;

    public Controller(GameFacade gameFacade){
        this.gameFacade = gameFacade;
        this.gameFacade.addObserver(this);
    }
    public void run(){
        do{
            System.out.println("current game State " + this.gameState);
            if(currentPlayer instanceof IA){
                this.IAPlayTurn();
                continue;
            }
            switch (this.gameState){
                case GameState.GameModChoice -> this.choseGameMod();
                case GameState.ColorChoice -> this.choseColor();
                case GameState.TotemChose -> this.choseTotem();
                case GameState.TotemMove -> this.moveTotem();
                case GameState.PlacePawn ->{
                    this.placePawn();
                    this.gameFacade.nextRound();
                }
                case GameState.PinkWin -> System.out.println("pink player won");
                case GameState.BlackWin -> System.out.println("black player won");
            }
        } while(!(this.gameState.equals(GameState.PinkWin) || this.gameState.equals(GameState.BlackWin)));
    }
    private void choseColor(){
        System.out.println("Choose your color:  1 = PINK || 2 = BLACK");

        do{
            input = IN.nextLine();
            try{
                if(Integer.parseInt(input) == 1){
                    this.player1 = new Player(PieceColor.PINK);
                    this.player2 = new IA(PieceColor.BLACK, this.gameFacade);
                    this.currentPlayer = player1;
                } else if(Integer.parseInt(input) == 2){
                    this.player1 = new Player(PieceColor.BLACK);
                    this.player2 = new IA(PieceColor.PINK, this.gameFacade);
                    this.currentPlayer = player2;
                } else {
                    System.out.println("enter 1 or 2");
                }
            }catch(Exception e){
                System.out.println("enter valid value");
            }
        }while(player1 == null);

        this.gameFacade.playerColorChosed(currentPlayer.getColor());
    }
    private void choseTotem(){
        System.out.println("current player: " + currentPlayer.getColor());
        System.out.println("choose the totem to move: x = Cross || o = circle");

        do{
            input = IN.nextLine();
            try{
                if(input.equalsIgnoreCase("undo") && this.gameFacade.undo(this.gameState)){
                    this.switchPlayer();
                    break;
                } else if (input.equalsIgnoreCase("redo") && this.gameFacade.redo(this.gameState)) {
                    break;
                } else if(input.equals("x")){
                    if(this.currentPlayer.getColor().equals(PieceColor.PINK) && this.gameFacade.getPinkCrossPawnsCount() > 0){
                        this.gameFacade.choseTotem(PieceType.CROSS);
                    } else if(this.currentPlayer.getColor().equals(PieceColor.BLACK) && this.gameFacade.getBlackCrossPawnsCount() > 0){
                        this.gameFacade.choseTotem(PieceType.CROSS);
                    } else {
                        System.out.println("no more cross pawns");
                    }
                } else if(input.equals("o")){
                    if(this.currentPlayer.getColor().equals(PieceColor.PINK) && this.gameFacade.getPinkCirclePawnsCount() > 0){
                        this.gameFacade.choseTotem(PieceType.CIRCLE);
                    } else if(this.currentPlayer.getColor().equals(PieceColor.BLACK) && this.gameFacade.getBlackCirclePawnsCount() > 0){
                        this.gameFacade.choseTotem(PieceType.CIRCLE);
                    } else{
                        System.out.println("no more circle pawns");
                    }
                    this.gameFacade.choseTotem(PieceType.CIRCLE);
                } else {
                    System.out.println("enter x or o");
                }
            }catch(Exception e){
                System.out.println("enter valid value");
            }
            System.out.println(input);
        }while(!Objects.equals(input, "x") && !Objects.equals(input, "o"));
        System.out.println("totem chosed");
    }
    private void moveTotem(){
        System.out.println("enter position to move totem to (ex: 4 5)");
        boolean totemIsMoved = false;

        do{
            input = IN.nextLine();
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
                }
            } catch (Exception e){
                System.out.println("enter valid values");
            }
        } while(!totemIsMoved);
    }
    private void placePawn(){
        System.out.println("enter position to move pawn to (ex:3 1)");
        boolean pawnIsPlaced = false;

        do{
            input = IN.nextLine();
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
                }
            }catch(Exception e){
                System.out.println("enter valid values");
            }
        }while(!pawnIsPlaced);
        this.switchPlayer();
    }
    private void switchPlayer(){
        if (this.currentPlayer.equals(player1)) {
            this.currentPlayer = player2;
        } else {
            this.currentPlayer = player1;
        }
    }

    private void choseGameMod(){
        System.out.println("chose game mod");
        System.out.println("1: 2 players || 2: 1player 1bot || 3: 2 bots");
        boolean gameModChosen = false;
        do{
            input = IN.nextLine();

            try{
               Integer chosenGameMod = Integer.parseInt(input);

                if(chosenGameMod == 1){
                    this.player1 = new Player(PieceColor.PINK);
                    this.player2 = new Player(PieceColor.BLACK);
                    this.currentPlayer = player1;
                    this.gameFacade.playerColorChosed(currentPlayer.getColor());
                    gameModChosen = true;
                }else if (chosenGameMod == 2){
                    this.gameFacade.choseColor();
                    gameModChosen = true;
                } else if(chosenGameMod == 3){
                    this.player1 = new IA(PieceColor.PINK, this.gameFacade);
                    this.player2 = new IA(PieceColor.BLACK, this.gameFacade);
                    this.currentPlayer = player1;
                    this.gameFacade.playerColorChosed(currentPlayer.getColor());
                    gameModChosen = true;
                }
            }catch(Exception e){
                System.out.println("enter valid values");
            }
        }while(!gameModChosen);
    }
    private void IAPlayTurn(){
        ((IA) currentPlayer).playTurn();
        this.switchPlayer();
        System.out.println("enter next to go to next round");
        boolean roundNext = false;
        do{
            input = IN.nextLine();
            if(input.equalsIgnoreCase("undo") && this.gameFacade.undo(this.gameState)){
                break;
            } else if (input.equalsIgnoreCase("redo") && this.gameFacade.redo(this.gameState)) {
                this.switchPlayer();
                break;
            }
            if(input.equalsIgnoreCase("next")){
                this.gameFacade.nextRound();
                roundNext = true;
            } else{
                System.out.println("enter next please");
            }
        }while(!roundNext);
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        this.gameState = (GameState) evt.getOldValue();
    }
}
