package View;

import com.sun.javafx.binding.StringFormatter;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.text.NumberFormat;

public class GeneratorView {
    public Slider rowSlider;
    public Slider colSlider;
    public TextField rowsField;
    public TextField colsField;
    public int[] size;

    public void initialize() {
        if (size!=null) {
            rowSlider.setValue(size[1]);
            colSlider.setValue(size[2]);
        }
        rowSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            rowsField.setText(String.valueOf(newValue.intValue()));
        });

        rowsField.textProperty().addListener((observable, oldValue, newValue) -> {
            rowSlider.setValue(!newValue.isEmpty()? Double.valueOf(newValue): rowSlider.getMin());
        });

        colSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            colsField.setText(String.valueOf(newValue.intValue()));
        });
        colsField.textProperty().addListener((observable, oldValue, newValue) -> {
            colSlider.setValue(!newValue.isEmpty()? Double.valueOf(newValue): colSlider.getMin());
        });

    }

    public void sendValues(ActionEvent actionEvent) {
        size[0] = 1;
        size[1] = Integer.parseInt(!rowsField.getText().isEmpty()? rowsField.getText() : "2");
        size[2] = Integer.parseInt(!colsField.getText().isEmpty()? colsField.getText() : "2");
        if (size[1]<=1 || size[2]<=1) {
            new Alert(Alert.AlertType.WARNING, "Rows and columns has to be at least 2!").show();
            colsField.setText("10");
            rowsField.setText("10");
            return;
        }
        if (size[1]>500 || size[2]>500) {
            new Alert(Alert.AlertType.WARNING, "Rows and columns can not exceed 500!").show();
            colsField.setText("10");
            rowsField.setText("10");
            return;
        }

        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void setSize(int[] size){
        this.size=size;
    }
}
