package View;


import Model.MyModel;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MyViewController implements IView{
    private int[] size;
    private AMazeGenerator mazeGenerator;
    public MazeDisplayer mazeDisplayer;

//    public MyViewController( AMazeGenerator mazeGenerator, MazeDisplayer mazeDisplayer) {
//        this.mazeGenerator = mazeGenerator;
//        this.mazeDisplayer = mazeDisplayer;
//        this.mazeDisplayer.setImageFileNameWall("resources\\Images\\wall.PNG");
//        this.mazeDisplayer.setImageFileNamePlayer("resources\\Images\\\u200F\u200Fdudu.PNG");
//        this.mazeDisplayer.setImageFileGoal("resources\\Images\\fish.PNG");
//
//    }

    public void loadGame(ActionEvent actionEvent) {
    }

    public void saveGame(ActionEvent actionEvent) {
    }

    public void showProperties(ActionEvent actionEvent) {
    }

    public void showAbout(ActionEvent actionEvent) {
        System.out.println("Git");
        System.out.println("Git");
        System.out.println("Git");


    }

    public void exitProgram(){
        System.exit(0);
    }

    public void createGame(ActionEvent actionEvent) {
        Stage settings = new Stage();
        settings.setMinWidth(250);
        settings.setMinHeight(350);
        settings.setResizable(false);
        settings.setOpacity(0.9);
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("GeneratorView.fxml"));
        Parent root = fxml.getRoot();
        try {
            root = fxml.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Cannot open window!");
            e.printStackTrace();
        }
        GeneratorViewController generator = fxml.getController();
        size = new int[3];
        size[0]=0; //flag indicating new game is wanted
        size[1]=10; //Set default values
        size[2]=10;
        generator.setSize(size);
        settings.setTitle("Set maze size");
        settings.setScene(new Scene(root));
        settings.initModality(Modality.WINDOW_MODAL);
        settings.initOwner( ((Node)actionEvent.getSource()).getScene().getWindow() );
        settings.showAndWait();

        //GENERATE MAZE WITH int[] VALUES
        System.out.println(size[1]+ ", " + size[2]);
        if (size[0]==1)
            generateMaze();

    }

    public void generateMaze(){
        if(mazeGenerator == null)
            mazeGenerator = new MyMazeGenerator();
        int rows = size[1];
        int cols = size[2];
        Maze newMaze = this.mazeGenerator.generate(rows,cols);
        mazeDisplayer.drawMaze(newMaze);

    }


    public void keyPressed(KeyEvent keyEvent) {
    }
}
