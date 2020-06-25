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

    public static LinkedList<String> games =new LinkedList<>();//static list that can be update

    /**
     * update the View List with values of games list
     *
     */
    private void addGames(){
        list.removeAll();
        list.addAll(games);
        loadList.getItems().addAll(list);
    }
    public static void setList(LinkedList<String> gList){//update games list
        if(games != null)
            games=gList;
    }
    public void setChooseGame(String[] chooseGame) {//update the chooseGame array
        ChooseGame = chooseGame;
    }

    /**
     * called when ths play button is clicked.
     * checks if the was a value that been choose in the view list
     * @param actionEvent
     */
    public void Choose(ActionEvent actionEvent) {
        String game = loadList.getSelectionModel().getSelectedItem();
        if(ChooseGame!=null)
            ChooseGame[0]=game;
        if(game==null){//checks if the was a value that been choose in the view list
            Alert notChoose = new Alert(Alert.AlertType.WARNING);
            notChoose.setContentText("You must choose a game");
            notChoose.show();
        }
        else
             ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addGames();//to build the ViewList
    }


}
