package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    int looking=-1;
    private int[] playerPosition;
    private Solution solution;
    private Set<Pair<Integer,Integer>> solutionPath = new LinkedHashSet<Pair<Integer, Integer>>();
    boolean showSolution;
    boolean highlightChararcter;
    boolean highlightGoal;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileGoal = new SimpleStringProperty();
    StringProperty imageTreat = new SimpleStringProperty("resources/Images/treats.png");
    double playerX;
    double playerY;
    double playerWidth;
    double playerHeight;

    public double getPlayerWidth() {
        return playerWidth;
    }

    public void setPlayerWidth(double playerWidth) {
        this.playerWidth = playerWidth;
    }

    public double getPlayerHeight() {
        return playerHeight;
    }

    public void setPlayerHeight(double playerHeight) {
        this.playerHeight = playerHeight;
    }


    public double getPlayerX() {
        return playerX;
    }

    public void setPlayerX(double playerX) {
        this.playerX = playerX;
    }

    public double getPlayerY() {
        return playerY;
    }

    public void setPlayerY(double playerY) {
        this.playerY = playerY;
    }

    @Override
    public boolean isResizable() {
        return true;
    }
    @Override
    public double maxHeight(double width) {
        return 10000;
    }
    @Override
    public double maxWidth(double height) { return 10000; }
    @Override
    public double minWidth(double height) {
        return 0;
    }

    public boolean gotMaze(){
        return maze!=null;
    }

    @Override
    public double minHeight(double width) {
        return 0;
    }

    @Override
    public void resize(double width, double height)
    {
        super.setWidth(width);
        super.setHeight(height);
        draw();
    }


    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) { this.imageFileNamePlayer.set(imageFileNamePlayer); }

    public String getImageFileGoal() {
        return imageFileGoal.get();
    }

    public void setImageFileGoal(String imageFileGoal) {
        this.imageFileGoal.set(imageFileGoal);
    }

    public int[] get_player_position() {
        return playerPosition;
    }

    public void set_player_position(int[] position){
        setImageFileNamePlayer("resources/Images/cat"+looking+".png");
        playerPosition=position;
        draw();
        looking*=-1;
    }
    private double getRow_player() {return playerPosition[0]; }

    private double getCol_player() {return playerPosition[1]; }

    public void setSolution(Solution solution){
        showSolution=true;
        solutionPath.clear();
        ArrayList<AState> path = solution.getSolutionPath();
        for (AState pos : path){
            String place = pos.getState();
            int row = Integer.parseInt(place.substring(1,place.indexOf(",")));
            int col = Integer.parseInt(place.substring(place.indexOf(",")+1,place.length()-1));
            solutionPath.add(new Pair<>(row,col));
        }
        System.out.println("solution built");
    }


    public void drawMaze(Maze maze)
    {
        this.maze = maze;
        playerPosition = new int[2];
        playerPosition[0]=maze.getStartPosition().getRowIndex();
        playerPosition[1]=maze.getStartPosition().getColumnIndex();
        draw();
    }

    public boolean isGoal(int[] arg) {
        return arg[0]==maze.getGoalPosition().getRowIndex() && arg[1]==maze.getGoalPosition().getColumnIndex();
    }

    public void draw()
    {
        if( maze!=null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
//            System.out.println(canvasHeight+","+canvasWidth);
            int row = maze.getMaze().length;
            int col = maze.getMaze()[0].length;
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
//            graphicsContext.setEffect(new DropShadow(6, 2, 2, Color.BLACK));
            graphicsContext.setFill(Color.RED);
            double w,h;
            //Draw Maze
            Image wallImage = null;
            Image solPath=null;
            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
                solPath = new Image(new FileInputStream(imageTreat.get()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no wall image!");
            }
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    if(maze.getMaze()[i][j] == 1) // Wall
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null){
                            graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                        }else{
                            graphicsContext.drawImage(wallImage,w,h,cellWidth,cellHeight);
                        }
                    }
                    else if (showSolution && solutionPath.contains(new Pair<>(i,j))){
                        if (solPath==null || maze.getMaze().length>30 ||maze.getMaze()[0].length>30 ) // no picture OR large maze
                            graphicsContext.fillOval(j * cellWidth,i * cellHeight,cellWidth,cellHeight);
                        else
                            graphicsContext.drawImage(solPath,j * cellWidth,i * cellHeight,cellWidth,cellHeight);

                    }

                }
            }

            double h_player = getRow_player() * cellHeight;
            setPlayerY(h_player+cellHeight/2); //center Y position
            setPlayerHeight(cellHeight/2); //half cell height (to calculate bounds)

            double w_player = getCol_player() * cellWidth;
            setPlayerX(w_player+cellWidth/2); //center X position
            setPlayerWidth(cellWidth/2); //half cell width

            Image playerImage = null;
            try {
                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no player image!");
            }
            if (highlightChararcter)
                graphicsContext.fillOval(w_player,h_player,cellWidth,cellHeight);
            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);

            if(playerPosition[0] != maze.getGoalPosition().getRowIndex() || playerPosition[1] != maze.getGoalPosition().getColumnIndex()){
                double h_goal = (maze.getGoalPosition().getRowIndex()) *cellHeight;
                double w_goal = (maze.getGoalPosition().getColumnIndex()) *  cellWidth;
//                System.out.println(maze.getGoalPosition());
                Image goalImage = null;
                try {
                    goalImage=new Image(new FileInputStream(getImageFileGoal()));
                } catch (FileNotFoundException e) {
                    System.out.println("There is no goal image!");
                }
                if (highlightGoal)
                    graphicsContext.fillOval(w_goal,h_goal,cellWidth, cellHeight);
                graphicsContext.drawImage(goalImage,w_goal,h_goal,cellWidth, cellHeight);
            }
            graphicsContext.applyEffect(new DropShadow(10, 2, 2, Color.BLACK));
        }

    }





}
