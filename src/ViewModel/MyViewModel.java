package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;

import algorithms.search.SearchableMaze;

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
        }
    }
}
