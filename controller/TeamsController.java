package controller;

import au.edu.uts.ap.javafx.Controller;
import au.edu.uts.ap.javafx.ViewLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Team;
import model.Teams;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

public class TeamsController extends Controller<Teams> {

    @FXML
    private TableView<Team> teamTableView;

    @FXML
    private TableColumn<Team, String> nameColumn;

    @FXML
    private TableColumn<Team, Number> playerCountColumn;

    @FXML
    private TableColumn<Team, Number> averageCreditColumn;

    @FXML
    private TableColumn<Team, Number> averageAgeColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button updateButton;

    public Teams getTeamsModel() {
        return this.model;
    }

    // This method initializes the TableView and TableColumn
    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        playerCountColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPlayers().getPlayerNumber()));
        averageCreditColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPlayers().CountAvgCredit()));
        averageAgeColumn.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPlayers().CountAvgAge()));

        // Allow editing for name column
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            Team team = event.getRowValue();
            team.setName(event.getNewValue());
        });
        
        deleteButton.setDisable(true);
        updateButton.setDisable(true);
        // Listen for selection changes in the TableView
        teamTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // A team is selected, disable the "Add" button
                addButton.setDisable(true);
                deleteButton.setDisable(false);
                updateButton.setDisable(false);
            } else {
                // No team is selected, enable the "Add" button
                addButton.setDisable(false);
            }
        });

        populateTable();
    }

    // This method populates the TableView with data from the model
    public void populateTable() {
        ObservableList<Team> teams = FXCollections.observableArrayList(getTeamsModel().currentTeams());
        teamTableView.setItems(teams);
    }

    @FXML
    public void createTeam() {
        try {
            // Close the current window
            Stage currentStage = (Stage) teamTableView.getScene().getWindow();
            currentStage.close();

            // Show the add team window
            Stage addTeamStage = new Stage();
            addTeamStage.getIcons().add(new Image("/view/nba.png"));
            ViewLoader.showStage(getTeamsModel(), "/view/AddTeam.fxml", "Add Team", addTeamStage);

            // Set up a listener for when the add team window closes
            addTeamStage.setOnHiding(event -> {
                // Show the current window again when the add team window is closed
                currentStage.show();
                // Refresh the table data
                populateTable();
            });
        } catch (IOException ex) {
            Logger.getLogger(AssociationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void deleteTeam() {
        Team selectedTeam = teamTableView.getSelectionModel().getSelectedItem();
        if (selectedTeam == null) {
            // Show an error message or prompt the user to select a team
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Team");
        alert.setContentText("Are you sure you want to delete this team?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Remove the team from the model
            getTeamsModel().remove(selectedTeam);
            // Update the TableView
            populateTable();
        }
    }
    
    @FXML
    public void manageTeam() {
        try {
            // Get the selected team from the table view
            Team selectedTeam = teamTableView.getSelectionModel().getSelectedItem();
            
            // Load the ManageTeamView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ManageTeamView.fxml"));
            Parent root = loader.load();
            
            // Get the controller for the ManageTeamView
            ManageTeamController controller = loader.getController();
            controller.initialize(selectedTeam);
            
            // Create a new stage for the ManageTeamView
            Stage stage = new Stage();
            stage.setX(ViewLoader.X + 601);
            stage.setY(ViewLoader.Y);
            stage.getIcons().add(new Image("/view/nba.png"));
            stage.setScene(new Scene(root));
            stage.setTitle("Manage Teams: " + selectedTeam.getName());
            
            // Hide the current window
            Stage currentStage = (Stage) teamTableView.getScene().getWindow();
            currentStage.hide();
            
            // Add an event handler to show the current window and populate the table again when the new stage is closed
            stage.setOnCloseRequest(event -> {
                currentStage.show();
                populateTable();
            });
            
            // Show the new stage
            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(AssociationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @FXML
    public void exit() {
        Platform.exit();
    }

}
