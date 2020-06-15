package View;


import Model.MyModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MyViewController implements IView{

    public void loadGame(ActionEvent actionEvent) {
    }

    public void saveGame(ActionEvent actionEvent) {
    }

    public void showProperties(ActionEvent actionEvent) {
    }

    public void showAbout(ActionEvent actionEvent) {
    }

    public void exitProgram(){
        System.exit(0);
    }

    public void generatorSettings(ActionEvent actionEvent) {
        Stage settings = new Stage();
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("GeneratorView.fxml"));
        Parent root = fxml.getRoot();
        try {
            root = fxml.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Cannot open window!");
            e.printStackTrace();
        }
        GeneratorViewController generator = fxml.getController();
        int[] size = {10,10};
        generator.setSize(size);
        settings.setTitle("Set maze size");
        settings.setScene(new Scene(root));
        settings.initModality(Modality.WINDOW_MODAL);
        settings.initOwner( ((Node)actionEvent.getSource()).getScene().getWindow() );
        settings.showAndWait();

        System.out.println(size[0]+ ", " + size[1]);


    }

}
