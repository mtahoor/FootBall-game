package controller;

import au.edu.uts.ap.javafx.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Teams;
import model.Team;
import javafx.scene.control.Button;

public class AddTeamController extends Controller<Teams> {

    @FXML
    private TextField nameField;

    @FXML
    private Label errorLabel;

    public Teams getTeamsModel() {
        return this.model;
    }

    private TeamsController teamsController;

    public void setTeamsController(TeamsController teamsController) {
        this.teamsController = teamsController;
    }
    
    @FXML
    public void addTeam(ActionEvent event) {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            errorLabel.setText("Team name cannot be empty");
            return;
        }

        // Check for duplicate team names
        if (getTeamsModel().hasTeam(name)) {
            errorLabel.setText("Team already exists");
            return;
        }

        // Add the new team
        getTeamsModel().addTeam(new Team(name));

        // Close the add team window
        Stage addTeamStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        addTeamStage.close();

        // Refresh TableView in TeamsController
        if (teamsController != null) {
            teamsController.populateTable();
        }
    }
}