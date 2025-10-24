package command;

import elmos.oxono.model.PieceType;

import java.util.Stack;

public class CommandManager {
    Stack<command.Command> undoStack;
    Stack<command.Command> redoStack;
    public CommandManager(){
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }
    public PieceType Do(command.Command command){
        this.undoStack.push(command);
        return command.execute();
    }
    public PieceType undo(){
        if(!this.undoStack.empty()) {
            System.out.println("there is things to undo");
            command.Command command = this.undoStack.pop();
            this.redoStack.push(command);
            return command.unexecute();
        }
        System.out.println("nothing to undo");
        return null;
    }
    public PieceType redo(){
        if(!this.redoStack.empty()){
            System.out.println("there is things to redo");
            Command command = this.redoStack.pop();
            this.undoStack.push(command);
            return command.execute();
        }
        System.out.println("nothing to undo");
        return null;
    }
}
