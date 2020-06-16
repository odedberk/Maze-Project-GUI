package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.ISearchable;
import algorithms.search.Solution;

import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public interface IModel {
    void generateGame(int row, int col);
    void loadGame(String filePath);
    String saveGame(ISearchable searchable); //return path?
    Properties getProperties();
    Properties getAbout();
    Solution getSolution(ISearchable searchable);
    void assignObserver(Observer o);

    void closeProgram();
}
