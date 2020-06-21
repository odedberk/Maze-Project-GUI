package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class LoadController  implements Initializable {
    @FXML
    private ListView<String> loadList = new ListView<>();
    @FXML
    private Button choose = new Button();

    private String[] ChooseGame;

    private ObservableList list = FXCollections.observableArrayList();

    public static LinkedList<String> games =new LinkedList<>();

    public LoadController() {
        ChooseGame=null;
    }

    public LoadController(String[] s) {
        ChooseGame=s;
    }



    public void ChooseFile(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File("SavedGames"));

        File fileChoose= chooser.showOpenDialog(null);
    }
    private void addGame(){
        list.removeAll(list);
        list.addAll(games);
        loadList.getItems().addAll(list);
    }
    public static void setList(LinkedList<String> gList){
        if(games != null)
            games=gList;
    }
    public void setChooseGame(String[] chooseGame) {
        ChooseGame = chooseGame;
    }

    public void Choose(ActionEvent actionEvent) {
        String game = loadList.getSelectionModel().getSelectedItem();
        if(ChooseGame!=null)
            ChooseGame[0]=game;
        if(game==null){
            Alert notChoose = new Alert(Alert.AlertType.WARNING);
            notChoose.setContentText("you didnt choose game");
            notChoose.show();
        }
        else
             ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addGame();
    }


}
