package Main;

import Model.MyModel;
import Server.Server;
import Server.*;
import View.GeneratorViewController;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Server mazeGeneratingServer = new Server(5400, 2000, new ServerStrategyGenerateMaze());
        Server mazeSolvingServer = new Server(5401, 2000, new ServerStrategySolveSearchProblem());
        MyModel myModel= new MyModel(mazeGeneratingServer,mazeSolvingServer);
        primaryStage.setOnCloseRequest(event ->  myModel.closeProgram());

        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(850);
        primaryStage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent root = loader.load();
        MyViewController viewController = loader.getController();
        viewController.setViewModel(new MyViewModel(myModel));
        primaryStage.setTitle("The Maze");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
