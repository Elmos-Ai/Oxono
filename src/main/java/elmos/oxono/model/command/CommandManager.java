package elmos.oxono.model.command;

import elmos.oxono.model.PieceType;
import elmos.oxono.model.command.Command;

import java.util.Stack;

public class CommandManager {
    Stack<Command> undoStack;
    Stack<Command> redoStack;
    public CommandManager(){
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }
    public PieceType Do(Command command){
        this.undoStack.push(command);
        this.redoStack.clear();
        return command.execute();
    }
    public PieceType undo(){
        if(!this.undoStack.empty()) {
            Command command = this.undoStack.pop();
            this.redoStack.push(command);
            return command.unexecute();
        }
        return null;
    }
    public PieceType redo(){
        if(!this.redoStack.empty()){
            Command command = this.redoStack.pop();
            this.undoStack.push(command);
            return command.execute();
        }
        return null;
    }
}
