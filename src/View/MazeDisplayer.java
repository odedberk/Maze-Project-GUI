package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private int[] playerPosition;
    private Solution solution;
    boolean showSolution;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileGoal = new SimpleStringProperty();

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

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }
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
        playerPosition=position;
        draw();
    }


    public void drawMaze(Maze maze)
    {
        this.maze = maze;
        playerPosition = new int[2];
        playerPosition[0]=maze.getStartPosition().getRowIndex();
        playerPosition[1]=maze.getStartPosition().getColumnIndex();
        draw();
    }

    public void draw()
    {
        if( maze!=null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            System.out.println(canvasHeight+","+canvasWidth);
            int row = maze.getMaze().length;
            int col = maze.getMaze()[0].length;
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
            graphicsContext.setFill(Color.RED);
            double w,h;
            //Draw Maze
            Image wallImage = null;
            try {
                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
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

                }
            }

            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            Image playerImage = null;
            try {
                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
            } catch (FileNotFoundException e) {
                System.out.println("There is no player image!");
            }
            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);

            if(playerPosition[0] != maze.getGoalPosition().getRowIndex() || playerPosition[1] != maze.getGoalPosition().getColumnIndex()){
                double h_goal = (maze.getGoalPosition().getRowIndex()) *cellHeight;
                double w_goal = (maze.getGoalPosition().getColumnIndex()) *  cellWidth;
                System.out.println(maze.getGoalPosition());
                Image goalImage = null;
                try {
                    goalImage=new Image(new FileInputStream(getImageFileGoal()));
                } catch (FileNotFoundException e) {
                    System.out.println("There is no goal image!");
                }
                graphicsContext.drawImage(goalImage,w_goal,h_goal,cellWidth, cellHeight);

            }
        }
    }
}
