package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.Server;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.ISearchable;
import algorithms.search.Solution;
import javafx.scene.control.Alert;
import org.apache.log4j.*;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyModel extends Observable implements IModel {
    private Server generatorServer;
    private Server solverServer;
    private int[] charPosition;
    private Maze maze;
    private static Logger logger = Logger.getLogger(String.valueOf(MyModel.class));

    public MyModel (Server generator, Server solver){
        generatorServer=generator;
        solverServer=solver;
        generatorServer.start();
        solverServer.start();
        charPosition=new int[2];
        try {
            logger.addAppender(new FileAppender(new SimpleLayout(), "logs/log.txt"));
        } catch(Exception e) {
            e.printStackTrace();
        }

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String now=formatter.format(date);
        logger.info("\n--------"+now+"--------\n");
        logger.info("Starting servers..");
    }

    @Override
    public void generateGame(int row, int col) {
        final Maze[] temp = new Maze[1];
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() { //TODO - CHANGE PORT?
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])fromServer.readObject();
                        System.out.println(compressedMaze.length);
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[300000]; //max 500X500
                        is.read(decompressedMaze);
                        temp[0] = new Maze(decompressedMaze);
                        Thread.sleep(1000L);
                        temp[0].print();
                    } catch (Exception var10) {

                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        maze=temp[0];
        charPosition[0]=maze.getStartPosition().getRowIndex();
        charPosition[1]=maze.getStartPosition().getColumnIndex();
        setChanged();
        notifyObservers(maze);
        logger.info("new game. maze size "+row +"x"+col);

    }


    @Override
    public void loadGame(String fileName) {
        logger.info("Trying To load maze: "+fileName);
        try {
            Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
            FileInputStream fileInputStream = new FileInputStream(path+"\\resources\\SavedGames\\"+fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.maze=(Maze)objectInputStream.readObject();
            charPosition[0]=maze.getStartPosition().getRowIndex();
            charPosition[1]=maze.getStartPosition().getColumnIndex();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Couldn't load maze file");
            e.printStackTrace();
        }
        logger.info("Maze loaded successfully");
        setChanged();
        notifyObservers(maze);
    }

    @Override
    public String saveGame() {//ISearchable searchable
        Date date = new Date(); // current date value
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
        String now=formatter.format(date);
        String game = "Maze "+maze.getMaze().length+"X"+maze.getMaze()[0].length+" "+now;
        logger.info("trying to save "+game);
        try {//write the solution to file
            Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
            FileOutputStream outputStream = new FileOutputStream(path+"\\resources\\SavedGames\\"+game);
            ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);
            objectOutput.writeObject(maze);
            objectOutput.flush();
            objectOutput.close();
            logger.info("game "+game + " was saved");
            Alert saved = new Alert(Alert.AlertType.INFORMATION, "Saved file name:\n"+game);
            saved.setHeaderText("Maze and player position saved");
            saved.show();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return null;
    }

    public void getSavedGames(){
        logger.info("Trying to load games list");
        LinkedList<String> games = new LinkedList<>();
        Path path = FileSystems.getDefault().getPath("").toAbsolutePath();
        File savedGames = new File(path+"\\resources\\SavedGames");
        String contents[] = savedGames.list();
        for (String s : contents) {
            games.add(s);
        }
        setChanged();
        notifyObservers(games);
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public Properties getAbout() {
        logger.info("get about"+System.currentTimeMillis());
        return null;
    }

    @Override
    public void moveCharacter(Direction direction) {
        switch(direction)
        {
            case UP:
                if(charPosition[0]!=0 && !isWall(charPosition[0]-1,charPosition[1]))
                    charPosition[0]--;
                break;

            case DOWN:
              if(charPosition[0]!=maze.getMaze().length-1 && !isWall(charPosition[0]+1,charPosition[1]))
                  charPosition[0]++;
                break;

            case LEFT:
              if(charPosition[1]!=0 && !isWall(charPosition[0],charPosition[1]-1))
                  charPosition[1]--;
                break;

            case RIGHT:
              if(charPosition[1]!=maze.getMaze()[0].length-1 && !isWall( charPosition[0],charPosition[1]+1))
                  charPosition[1]++;
                break;

            case UP_RIGHT:
                if(charPosition[0]!=0 && charPosition[1]!=maze.getMaze()[0].length-1 && !isWall( charPosition[0]-1,charPosition[1]+1)) {
                    charPosition[0]--;
                    charPosition[1]++;
                }
                break;

            case UP_LEFT:
                if(charPosition[0]!=0 && charPosition[1]!=0 && !isWall( charPosition[0]-1,charPosition[1]-1)) {
                    charPosition[0]--;
                    charPosition[1]--;
                }
                break;

            case DOWN_LEFT:
                if(charPosition[0]!=maze.getMaze().length-1 && charPosition[1]!=0 && !isWall( charPosition[0]+1,charPosition[1]-1)) {
                    charPosition[0]++;
                    charPosition[1]--;
                }
                break;

            case DOWN_RIGHT:
                if(charPosition[0]!=maze.getMaze().length-1 && charPosition[1]!=maze.getMaze()[0].length-1 && !isWall( charPosition[0]+1,charPosition[1]+1)) {
                    charPosition[0]++;
                    charPosition[1]++;
                }
                break;

        }
        maze.setStart(new Position(charPosition[0],charPosition[1])); // update current player's position in maze (for showing relevant solution)
        setChanged();
        notifyObservers(charPosition);
    }

    private boolean isWall(int row, int col) {
        return maze.getMaze()[row][col]==1;
    }

    @Override
    public void solveGame() {
        logger.info("player ask for solution"+System.currentTimeMillis());
        if (maze==null) {
            System.out.println("No maze to solve!");
            setChanged();
            notifyObservers(null);
            return;
        }
        final Solution[] mazeSolution = new Solution[1];
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() { //TODO - CHANGE PORT?
                        @Override
                        public void clientStrategy(InputStream inFromServer,
                                                   OutputStream outToServer) {
                            try {
                                ObjectOutputStream toServer = new
                                        ObjectOutputStream(outToServer);
                                ObjectInputStream fromServer = new
                                        ObjectInputStream(inFromServer);
                                toServer.flush();
                                toServer.writeObject(maze); //send maze to server
                                toServer.flush();
                                mazeSolution[0] = (Solution)fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                                //Print Maze Solution retrieved from the server
                                System.out.println(String.format("Solution steps: %s", mazeSolution[0]));
                                ArrayList<AState> mazeSolutionSteps =  mazeSolution[0].getSolutionPath();
                                for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                                    System.out.println(String.format("%s. %s", i,
                                            mazeSolutionSteps.get(i).toString()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                logger.fatal(e.getMessage());
                            }
                        }
                    });
            client.communicateWithServer();
            logger.info(client.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.fatal(e.getMessage());
        }
        setChanged();
        notifyObservers(mazeSolution[0]);
    }



    @Override
    public void assignObserver(Observer o) {
        addObserver(o);
    }

    @Override
    public void closeProgram() {
        logger.info("Shutting down servers and closing the program!");
        generatorServer.stop();
        solverServer.stop();
        System.exit(0);
    }


}
