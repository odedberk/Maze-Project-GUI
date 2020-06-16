package Model;

import Server.Server;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.ISearchable;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;

import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public class MyModel extends Observable implements IModel {
    private Server generatorServer;
    private Server solverServer;

    public MyModel (Server generator, Server solver){
        generatorServer=generator;
        solverServer=solver;

        generatorServer.start();
        solverServer.start();
    }

    @Override
    public ISearchable generateGame(int row, int col) {
        return new SearchableMaze(new MyMazeGenerator().generate(row, col));
//        return null;
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

    @Override
    public void closeProgram() {
        generatorServer.stop();
        solverServer.stop();
        System.exit(0);
    }


}
