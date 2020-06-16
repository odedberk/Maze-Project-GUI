package View;


import Model.MyModel;
import ViewModel.MyViewModel;
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
    private int[] size = new int[3];
    ;
    private AMazeGenerator mazeGenerator;
    public MazeDisplayer mazeDisplayer;
    public MyViewModel viewModel;

    public void setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void loadGame(ActionEvent actionEvent) {
    }

    public void saveGame(ActionEvent actionEvent) {
    }

    public void showProperties(ActionEvent actionEvent) {
    }

    public void showAbout(ActionEvent actionEvent) {
    }

    public void exitProgram(){
        viewModel.closeProgram();
    }

    public void createGame(ActionEvent actionEvent) {
        Stage settings = new Stage();
        settings.setMinWidth(250);
        settings.setMinHeight(350);
        settings.setResizable(false);
        settings.setOpacity(0.97);
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("GeneratorView.fxml"));
        Parent root = null;
        try {
            root = fxml.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Cannot open window!");
            e.printStackTrace();
        }
        GeneratorViewController generator = fxml.getController();
        size[0]=0; //flag indicating new game is wanted
//        size[1]=10; //Set default values
//        size[2]=10;
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
        Maze maze = viewModel.getMaze(size[1],size[2]);
        mazeDisplayer.drawMaze(maze);
    }

    public void keyPressed(KeyEvent keyEvent) {
    }
}
