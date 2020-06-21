package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;

import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;

    public MyViewModel(IModel model) {

        this.model = model;
        model.assignObserver(this);
    }


    public void generateMaze(int row,int col){
        model.generateGame(row,col);
    }

    public void closeProgram() {
        model.closeProgram();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof IModel){
            if (arg instanceof Maze){
                setChanged();
                notifyObservers(arg);
            }

            else if (arg instanceof Solution){
                setChanged();
                notifyObservers(arg);
            }

            else if(arg instanceof List){
                setChanged();
                notifyObservers(arg);
            }
            if (arg instanceof int[]){
                setChanged();
                notifyObservers(arg);
            }


        }
    }

    public void solveMaze() {
        model.solveGame();
    }
    public void LoadGame(String game){
        this.model.loadGame(game);
    }

    public void getSavedGames(){
        ((MyModel)this.model).getSavedGames();
    }
    public void saveGame(){
        this.model.saveGame();
    }

    public void moveCharacter(KeyEvent keyEvent) {
        MyModel.Direction direction = MyModel.Direction.NONE;
        switch (keyEvent.getCode()){
            case UP:
                direction = IModel.Direction.UP;
                break;
            case DOWN:
                direction = IModel.Direction.DOWN;
                break;
            case LEFT:
                direction = IModel.Direction.LEFT;
                break;
            case RIGHT:
                direction = IModel.Direction.RIGHT;
                break;
        }

        model.moveCharacter(direction);
    }
}
