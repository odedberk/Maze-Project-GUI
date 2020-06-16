package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.ISearchable;
import algorithms.search.SearchableMaze;

public class MyViewModel {
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
}
