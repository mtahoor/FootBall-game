package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import au.edu.uts.ap.javafx.Controller;
import javafx.stage.Stage;
import model.Association;
import model.Season;

public class ErrorController extends Controller<Association> {
    
    @FXML
    private Label errors;

    @FXML
    private Button okayButton;
    
    private Stage stage;

    public Association getModel(){
        return this.model;
    }

    @FXML
    public void initialize() {
        Season currentSeason = getModel().getSeason();
            if (currentSeason != null) {
                String a=currentSeason.playGame();
                setErrorMessage(a);
            }
    }
    
    public void setErrorMessage(String message) {
        errors.setText(message);
    }
    
    public void closeWindow() {
        stage.close();
    }

    @FXML
    private void close() {
        Stage stage = (Stage) okayButton.getScene().getWindow();
        stage.close();
    }
}
