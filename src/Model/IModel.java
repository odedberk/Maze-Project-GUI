package Model;

import algorithms.search.ISearchable;
import algorithms.search.Solution;
import javafx.event.ActionEvent;

import java.util.Properties;

public interface IModel {
    ISearchable generateGame(int row, int col);
    ISearchable loadGame(String filePath);
    String saveGame(ISearchable searchable); //return path?
    Properties getProperties();
    Properties getAbout();
    Solution getSolution(ISearchable searchable);

}
