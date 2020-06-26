package Main;

import Model.MyModel;
import Server.Server;
import Server.*;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Server mazeGeneratingServer = new Server(5400, 10000, new ServerStrategyGenerateMaze());
        Server mazeSolvingServer = new Server(5401, 10000, new ServerStrategySolveSearchProblem());
        MyModel myModel= new MyModel(mazeGeneratingServer,mazeSolvingServer);
        primaryStage.setOnCloseRequest(event ->  myModel.closeProgram());
        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(950);
        primaryStage.setResizable(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent root = loader.load();
        MyViewController viewController = loader.getController();
        MyViewModel viewModel = new MyViewModel(myModel);
        viewController.setViewModel(viewModel);
        viewModel.addObserver(viewController);

        viewController.playMusic();
        primaryStage.setTitle("Grumpy Maze");
        primaryStage.setScene(new Scene(root, 350, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
