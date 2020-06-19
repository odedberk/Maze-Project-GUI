package View;


import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class MyViewController implements IView, Observer {
    private int[] size = new int[3];
    ;
    public MazeDisplayer mazeDisplayer;
    public MyViewModel viewModel;
    public Button solveBtn;
    public Button saveBtn;

    public void setViewModel(MyViewModel viewModel) { this.viewModel = viewModel; }

    public void loadGame(ActionEvent actionEvent) {
//        System.out.println("load");
//        FileChooser fileChooser = new FileChooser();
//        File f= fileChooser.showOpenDialog(((Node)actionEvent.getSource()).getScene().getWindow());
//        System.out.println(f.getName());
    }

    public void saveGame(ActionEvent actionEvent) {
    }

    public void showAbout(ActionEvent actionEvent) {
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyViewModel) {
            if (arg instanceof Maze) {
                mazeDisplayer.setDisable(false);
                solveBtn.setDisable(false);
                saveBtn.setDisable(false);
                mazeDisplayer.drawMaze((Maze) arg);
                mazeDisplayer.getScene().setOnScroll(event ->
                {
                    if (event.isControlDown()) {
                        mazeDisplayer.getScene().getWindow().setHeight(mazeDisplayer.getScene().getWindow().getHeight() * (event.getDeltaY() > 0 ? 1.08 : 0.94));
                        mazeDisplayer.getScene().getWindow().setWidth(mazeDisplayer.getScene().getWindow().getWidth() * (event.getDeltaY() > 0 ? 1.08 : 0.94));
                    }
                });
            }

            if (arg instanceof Solution) {
                System.out.println("solved!");
                //TODO - SHOW SOLUTION ON CANVAS
            }
            if (arg instanceof int[]) { //updated player position
                mazeDisplayer.set_player_position((int[]) arg);
                if (isGoalPosition((int[])arg)) {
                    gameWon();
                }
            }
        }
    }

    private void gameWon() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("YOU WON!!\nnow feed me.");
        Image image = null;
        try {
            image = new Image(new FileInputStream("resources/Images/happy.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);
        alert.showAndWait();
//        new Alert(Alert.AlertType.INFORMATION,"You Won!!").show();
        mazeDisplayer.setDisable(true);
        solveBtn.setDisable(true);
    }

    private boolean isGoalPosition(int[] arg) {
        return mazeDisplayer.isGoal(arg);
    }

    public void exitProgram(){
        viewModel.closeProgram();
    }

    public void generateMaze(){
        viewModel.generateMaze(size[1],size[2]);
    }

    public void solveMaze(){viewModel.solveMaze();}

    public void keyPressed(KeyEvent keyEvent) {
        if (mazeDisplayer.gotMaze()){
            viewModel.moveCharacter(keyEvent);
            keyEvent.consume();
        }
    }

    public void getSettings(ActionEvent actionEvent) {
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
        GeneratorView generator = fxml.getController();
        size[0]=0; //flag indicating new game is wanted
        generator.setSize(size);
        settings.setTitle("Set maze size");
        settings.setScene(new Scene(root));
        settings.initModality(Modality.WINDOW_MODAL);
        settings.initOwner( ((Node)actionEvent.getSource()).getScene().getWindow() );
        settings.showAndWait();

        //GENERATE MAZE WITH INPUT VALUES
        System.out.println(size[1]+ ", " + size[2]);
        if (size[0]==1)
            generateMaze();
    }

    public void showProperties(ActionEvent actionEvent) {
        StringBuilder show = new StringBuilder("");
        Set<String> properties = Configurations.getAllProperties();
        for (String key:properties) {
            show.append(key+" : "+Configurations.getProperty(key)+"\n");
        }
        Alert alert= new Alert(Alert.AlertType.INFORMATION, show.toString());
//        alert.setTitle("Configurations:");
        alert.setHeaderText("Configurations:");
        alert.show();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }


}
