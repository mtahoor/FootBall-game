package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import au.edu.uts.ap.javafx.Controller;
import model.Player;
import model.Team;

public class ManageTeamController extends Controller<Team> {

    @FXML
    private Label teamNameLabel;

    @FXML
    private TableView<Player> teamTableView;

    @FXML
    private TableColumn<Player, String> nameColumn;

    @FXML
    private TableColumn<Player, Double> creditColumn;

    @FXML
    private TableColumn<Player, Integer> ageColumn;

    @FXML
    private TableColumn<Player, Integer> noColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button saveAndCloseButton;




    private Team selectedTeam;

    @FXML
    public void initialize(Team team) {
        this.selectedTeam = team;
        teamNameLabel.setText(selectedTeam.getName());

        // Disable update and delete buttons initially
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        // Enable add and save and close buttons initially
        addButton.setDisable(false);

        // Populate table with players for the selected team
        populateTable();
    }

    private void populateTable() {
        ObservableList<Player> players = selectedTeam.getPlayers().getPlayersList();
        teamTableView.setItems(players);

        // Bind columns to player properties
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        creditColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayerCreditProperty().asObject());
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayerAgeProperty().asObject());
        noColumn.setCellValueFactory(cellData -> cellData.getValue().getPlayerNoProperty().asObject());

        // Listen for selection changes and update buttons accordingly
        teamTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Enable update and delete buttons when a player is selected
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                // Disable add button when a player is selected
                addButton.setDisable(true);
            } else {
                // Disable update and delete buttons when no player is selected
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
                // Enable add button when no player is selected
                addButton.setDisable(false);
            }
        });
    }

    @FXML
    private void addPlayer() {
        try {
            // Get the selected team from the table view
            Team selectedTeam = this.selectedTeam;
            
            // Load the PlayerUpdateView.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PlayerUpdateView.fxml"));
            Parent root = loader.load();
    
            // Get the controller for the PlayerUpdateView
            PlayerUpdateController playerUpdateController = loader.getController();
    
            // Pass the selected team to the PlayerUpdateController's initialize method
            playerUpdateController.initialize(selectedTeam);
    
            // Disable the Update button in the PlayerUpdateView
            playerUpdateController.updateButton.setDisable(true);
    
            // Create a new stage for the player update window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Adding New Player");
            stage.initModality(Modality.APPLICATION_MODAL); // Make the window modal
            stage.showAndWait(); // Show the window and wait for it to be closed
        } catch (IOException ex) {
            ex.printStackTrace(); // Handle the exception appropriately
        }
    }
    

    @FXML
    private void deletePlayer() {
        // Get the selected player from the table view
        Player selectedPlayer = teamTableView.getSelectionModel().getSelectedItem();
        
        if (selectedPlayer != null) {
            // Remove the selected player from the table view
            teamTableView.getItems().remove(selectedPlayer);
            
            // Remove the selected player from the data model
            selectedTeam.getPlayers().removePlayer(selectedPlayer);
        }
    }

    @FXML
    private void updatePlayer() {
        try {
            // Get the selected player from the table view
            Player selectedPlayer = teamTableView.getSelectionModel().getSelectedItem();
    
            if (selectedPlayer == null) {
                // Show an error message or prompt the user to select a player
                return;
            }
    
            // Load the PlayerUpdateView.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PlayerUpdateView.fxml"));
            Parent root = loader.load();
    
            // Get the controller for the PlayerUpdateView
            PlayerUpdateController playerUpdateController = loader.getController();
    
            // Pass the selected player's information to the PlayerUpdateController
            playerUpdateController.selectedPlayer(selectedPlayer);
    
            // Disable the Add button in the PlayerUpdateView
            playerUpdateController.addButton.setDisable(true);
    
            // Create a new stage for the player update window
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Player");
            stage.initModality(Modality.APPLICATION_MODAL); // Make the window modal
            stage.showAndWait(); // Show the window and wait for it to be closed
        } catch (IOException ex) {
            ex.printStackTrace(); // Handle the exception appropriately
        }
    }
    

    @FXML
    private void saveAndClose() {
        Stage stage = (Stage) saveAndCloseButton.getScene().getWindow();
        stage.close();
    }
}
