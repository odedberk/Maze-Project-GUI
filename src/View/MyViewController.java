package View;


import Model.Configurations;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.nio.file.Paths;

public class MyViewController implements IView, Observer {
    private int[] size = new int[3];
    public MazeDisplayer mazeDisplayer;
    public MyViewModel viewModel;
    public ToggleButton solveBtn;
    public Button saveBtn;
    public MenuItem menuSaveBtn;
    public ToggleButton playBtn;
    public ToggleButton fishBtn;
    public ToggleButton catBtn;
    public MediaPlayer mediaPlayer;
    public MediaPlayer meow;


    public void setViewModel(MyViewModel viewModel) { this.viewModel = viewModel; }

    /**
     * gets actionEvent and build new Load scene
     * @param actionEvent
     */
    public void loadGame(ActionEvent actionEvent) {
        Stage settings = new Stage();
        settings.setMinWidth(250);
        settings.setMinHeight(100);
        settings.setResizable(false);
        settings.setOpacity(0.97);
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("Load.fxml"));
        Parent root = null;
        getLoadGames();//update games list in LoadController
        try {
            root = fxml.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot open window!");//if cannot open the load window
            e.printStackTrace();
        }
        LoadController loadController = fxml.getController();
        String[] game = new String[1];
        loadController.setChooseGame(game);
        settings.setTitle("Load a game");
        settings.setScene(new Scene(root));
        settings.initModality(Modality.APPLICATION_MODAL);
        settings.showAndWait();
        if(game[0]!=null)//if saved game choose
            this.viewModel.LoadGame(game[0]);
    }

    /**
     * get saved games list from resources folder
     */
    private void getLoadGames(){//ask for all the saved games
        viewModel.getSavedGames();
    }

    public void saveGame(ActionEvent actionEvent) {
        this.viewModel.saveGame();
    }

    public void showAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Made by: Oded Berkovich and Eilam Gal.\n ");
        alert.show();
    }

    public void mute(ActionEvent actionEvent) { mediaPlayer.setMute(!mediaPlayer.isMute());mazeDisplayer.requestFocus(); }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyViewModel) {//if the notify is from the MyModelView
            if (arg instanceof Maze) {
                resetMaze();
                mazeDisplayer.drawMaze((Maze) arg);
                mazeDisplayer.getScene().setOnScroll(event ->
                {
                    if (event.isControlDown()) {
                        mazeDisplayer.getScene().getWindow().setHeight(mazeDisplayer.getScene().getWindow().getHeight() * (event.getDeltaY() > 0 ? 1.08 : 0.94));
                        mazeDisplayer.getScene().getWindow().setWidth(mazeDisplayer.getScene().getWindow().getWidth() * (event.getDeltaY() > 0 ? 1.08 : 0.94));
                    }
                });
                mazeDisplayer.requestFocus();
            }

            if (arg instanceof Solution) { //received a new maze solution
                System.out.println("solved!");
                mazeDisplayer.setSolution((Solution) arg);
                mazeDisplayer.draw();
            }
            if (arg instanceof int[]) { //updated player position
                mazeDisplayer.set_player_position((int[]) arg);
                if (mazeDisplayer.showSolution)
                    viewModel.solveMaze();
                if (isGoalPosition((int[])arg)) {
                    playMeow();
                    gameWon();
                }
            }
            if(arg instanceof List){//gets list of saved games
                LoadController.setList((LinkedList<String>)arg);//update games list in LoadController
            }
        }
    }

    /**
     * sets all buttons and settings back to default
     */
    private void resetMaze() {
        mazeDisplayer.showSolution=false;
        mazeDisplayer.highlightCharacter =false;
        mazeDisplayer.highlightGoal=false;
        solveBtn.setSelected(false);
        catBtn.setSelected(false);
        fishBtn.setSelected(false);
        mazeDisplayer.setDisable(false);
        solveBtn.setDisable(false);
        saveBtn.setDisable(false);
        menuSaveBtn.setDisable(false);
        catBtn.setDisable(false);
        fishBtn.setDisable(false);
    }

    /**
     * inform the user that he won the game.
     *
     */
    private void gameWon() {
        mazeDisplayer.setDisable(true);
        solveBtn.setDisable(true);
        catBtn.setDisable(true);
        fishBtn.setDisable(true);
        saveBtn.setDisable(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);//alert to inform the win
        alert.setHeaderText("You won...");
        Image image = null;
        try {
            image = new Image(new FileInputStream("resources/Images/happy.png"));//image in alert
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);
        alert.showAndWait();
    }

    /**
    play music in the background
     */
    public void playMusic() {
        String s = "resources/sounds/background.mp3";
        Media h = new Media(Paths.get(s).toUri().toString());
        mediaPlayer = new MediaPlayer(h);
        mediaPlayer.setVolume(0.7);
        mediaPlayer.setCycleCount(5);
        mediaPlayer.play();
    }

    /**
     * play wining music
     */
    private void playMeow() {
        String s = "resources/sounds/meow.mp3";
        Media h = new Media(Paths.get(s).toUri().toString());
        meow = new MediaPlayer(h);
        meow.setCycleCount(1);
        meow.setVolume(0.3);
        meow.setMute(mediaPlayer.isMute());
        meow.play();
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

    /**
     * when the user ask for solution or to hide an open solution
     */
    public void solveMaze(){
        if (mazeDisplayer.showSolution) {
            mazeDisplayer.showSolution = false;
            mazeDisplayer.draw();
        }
        else
            viewModel.solveMaze();//ask for solution
        mazeDisplayer.requestFocus();

    }

    /**
     * handle movement request from the user
     * @param keyEvent
     */
    public void keyPressed(KeyEvent keyEvent) {
        if (mazeDisplayer.gotMaze()){
            viewModel.moveCharacter(keyEvent);
            keyEvent.consume();
        }
    }

    /**
     * open settings window and after generate a new game with the settings values
     * @param actionEvent
     */
    public void getSettings(ActionEvent actionEvent) {
        Stage settings = new Stage();
        settings.setMinWidth(250);
        settings.setMinHeight(350);
        settings.setResizable(false);
        settings.setOpacity(0.97);
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("Generator.fxml"));
        Parent root = null;
        try {
            root = fxml.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Cannot open window!");
            e.printStackTrace();
        }
        GeneratorController generator = fxml.getController();
        size[0]=0; //flag indicating new game is wanted
        generator.setSize(size);
        settings.setTitle("Set maze size");
        settings.setScene(new Scene(root));
        settings.initModality(Modality.APPLICATION_MODAL);
        settings.initOwner( ((Node)actionEvent.getSource()).getScene().getWindow() );
        settings.showAndWait();
        //GENERATE MAZE WITH INPUT VALUES
        System.out.println(size[1]+ ", " + size[2]);
        if (size[0]==1)//if generate button was kicked
            generateMaze();
    }

    /**
     * open alert with the Properties from Configurations
     * @param actionEvent
     */
    public void showProperties(ActionEvent actionEvent) {
        StringBuilder show = new StringBuilder("");
        Set<String> properties = Configurations.getAllProperties();
        for (String key:properties) {
            show.append(key+" : "+Configurations.getProperty(key)+"\n");
        }
        Alert alert= new Alert(Alert.AlertType.INFORMATION, show.toString());
        alert.setHeaderText("Configurations:");
        alert.show();
    }


    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    /**
     * highlight the fish picture
     * @param actionEvent
     */
    public void showFish(ActionEvent actionEvent) {
        mazeDisplayer.highlightGoal = !mazeDisplayer.highlightGoal;
        mazeDisplayer.draw();
        mazeDisplayer.requestFocus();
    }

    /**
     * highlight the cat picture
     * @param actionEvent
     */
    public void showCat(ActionEvent actionEvent) {
        mazeDisplayer.highlightCharacter = !mazeDisplayer.highlightCharacter;
        mazeDisplayer.draw();
        mazeDisplayer.requestFocus();
    }

    /**
     * handle movement with the mouse
     * @param mouseEvent
     */
    public void dragPlayer(MouseEvent mouseEvent) {
        double playerX = mazeDisplayer.getPlayerX();
        double playerY = mazeDisplayer.getPlayerY();
        double playerHeight = mazeDisplayer.getPlayerHeight();
        double playerWidth = mazeDisplayer.getPlayerWidth();
        if (mazeDisplayer.gotMaze()){
            viewModel.moveCharacter(mouseEvent,playerX, playerY, playerWidth, playerHeight);//sent viewModel the position of the player in the canvas and the mouseEvent
            mouseEvent.consume();
        }
    }

    public void help(ActionEvent actionEvent) {
        Alert help = new Alert(Alert.AlertType.INFORMATION,"Help\n" +
                "The Game:\n" +
                "The goal of the game is to bring the cat to the fish. Surprisingly.\n\n" +
                "To start a new game press 'New Maze' and insert the desired sizes\n"+
                "To load a previous game press 'Load Maze' and select your game\n"+
                "To save the current game (maze and character's position) press ' Save Maze'\n\n" +
                "How to play: \n" +
                "Left:          press left-key or 4\n" +
                "Right:         press right-key or 6\n" +
                "Up:            press up-key or 8\n" +
                "Down:          press down-key or 2\n" +
                "Up Left:       press 7\n" +
                "Up Right:      press 9\n" +
                "Down Left:     press 1\n" +
                "Down Right:    press 3\n" +
                "Or you can simply drag the character with your mouse.\n\n" +
                "MORE BUTTONS!\n"+
                "If you (are weak and) need a solution, press 'Show Solution'.\n"+
                "To highlight the character - press 'Where's the cat??' button.\n" +
                "To highlight the goal      - press 'Where's my FISH??' button.\n" +
                "To mute/unmute all background noises press 'Mute'.");
        help.show();
    }
}
