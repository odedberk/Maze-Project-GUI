package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private int[] playerPosition;
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileGoal = new SimpleStringProperty();


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
        this.imageFileNameWall.set(imageFileGoal);
    }

    public int getRow_player() {
        return playerPosition[0];
    }

    public int getCol_player() {
        return playerPosition[1];
    }

    public void set_player_position(int row, int col){
        playerPosition = new int[2];
        this.playerPosition[0] = row;
        this.playerPosition[1] = col;
        draw();
    }


    public void drawMaze(Maze maze)
    {
        this.maze = maze;
        setImageFileNameWall("C:\\Users\\eilam gal\\IdeaProjects\\ATP-Project-PartC\\resources\\Images\\wall.PNG");
        setImageFileNamePlayer("C:\\Users\\eilam gal\\IdeaProjects\\ATP-Project-PartC\\resources\\Images\\\u200F\u200Fdudu.PNG");
        setImageFileGoal("C:\\Users\\eilam gal\\IdeaProjects\\ATP-Project-PartC\\resources\\Images\\fish.PNG");
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
                System.out.println("There is no file....");
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
                System.out.println("There is no Image player....");
            }
            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);

            if(playerPosition[0] != maze.getGoalPosition().getRowIndex() || playerPosition[1] != maze.getGoalPosition().getColumnIndex()-1){
                double h_goal = (maze.getMaze().length-1) *cellHeight;
                double w_goal = (maze.getMaze()[0].length-1) *  cellWidth;
                Image goalImage = null;
                try {
                    goalImage=new Image(new FileInputStream(getImageFileGoal()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                graphicsContext.drawImage(goalImage,w_goal,h_goal,cellWidth, cellHeight);

            }
        }
    }
}
