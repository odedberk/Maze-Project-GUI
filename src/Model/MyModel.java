package Model;

import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.ISearchable;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;

import java.util.Properties;

public class MyModel implements IModel {

    @Override
    public ISearchable generateGame(int row, int col) {
        return new SearchableMaze(new MyMazeGenerator().generate(row, col));
    }


    @Override
    public ISearchable loadGame(String filePath) {
        return null;
    }

    @Override
    public String saveGame(ISearchable searchable) {
        return null;
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public Properties getAbout() {
        return null;
    }

    @Override
    public Solution getSolution(ISearchable searchable) {
        return null;
    }
}
