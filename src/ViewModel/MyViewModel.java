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
    }

    public Maze getMaze(int row, int col){
        SearchableMaze maze=(SearchableMaze)model.generateGame(row,col);
        return maze.getMaze();
    }

    public void closeProgram() {
        model.closeProgram();
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
