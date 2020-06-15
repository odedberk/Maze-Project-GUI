package Model;

import algorithms.search.ISearchable;
import algorithms.search.Solution;

import java.util.Properties;

public class MyModel implements IModel {

    @Override
    public ISearchable generateGame() {
        return null;
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
