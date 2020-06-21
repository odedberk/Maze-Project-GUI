package Model;

import algorithms.search.ISearchable;
import algorithms.search.Solution;

import java.util.Observer;
import java.util.Properties;

public interface IModel {
    enum Direction{NONE, UP,DOWN,LEFT,RIGHT,UP_RIGHT,UP_LEFT,DOWN_RIGHT,DOWN_LEFT}
    void generateGame(int row, int col);
    void loadGame(String filePath);
    String saveGame(); //return path?ISearchable searchable
    Properties getProperties();
    Properties getAbout();
    void moveCharacter(Direction direction);
    void solveGame();
    void assignObserver(Observer o);
    void closeProgram();

}
