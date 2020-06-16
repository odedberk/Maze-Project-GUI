package View;

import javafx.event.ActionEvent;

public interface IView {
    void exitProgram();
    void loadGame(ActionEvent actionEvent);
    void saveGame(ActionEvent actionEvent);
    void showProperties(ActionEvent actionEvent);
    void showAbout(ActionEvent actionEvent);
    void getSettings(ActionEvent actionEvent);
}
