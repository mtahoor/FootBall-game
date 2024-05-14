package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Association;
import javafx.scene.control.TableColumn;
import au.edu.uts.ap.javafx.Controller;
import model.Team;
import model.Game;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
public class TeamsRoundController extends Controller<Association> {

    @FXML
    private ListView<String> teamListView;

    @FXML
    private Button arrangeSeasonButton;

    @FXML
    private Button advanceRoundButton;

    @FXML
    private TableView<Game> seasonTableView;
    
    @FXML
    private TableColumn<Game, Integer> roundColumn;
    
    @FXML
    private TableColumn<Game, String> team1Column;
    
    @FXML
    private TableColumn<Game, String> team2Column;




    public Association getModel(){
        return this.model;
    }
    
    @FXML
    public void initialize() {
        arrangeSeasonButton.setDisable(true);
        advanceRoundButton.setDisable(true);
        teamListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        // Populate team list only if it's empty
        if (teamListView.getItems().isEmpty()) {
            populateTeamList();
        }
        teamListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<String>) change -> {
            int selectedItemsCount = teamListView.getSelectionModel().getSelectedItems().size();
            advanceRoundButton.setDisable(selectedItemsCount != 2);
        });
        roundColumn.setCellValueFactory(cellData -> cellData.getValue().termProperty().asObject());
        team1Column.setCellValueFactory(cellData -> cellData.getValue().team1());
        team2Column.setCellValueFactory(cellData -> cellData.getValue().team2());

        teamListView.getItems().addListener((ListChangeListener<String>) change -> {
            arrangeSeasonButton.setDisable(!teamListView.getItems().isEmpty());
        });

    }

    private void populateTeamList() {
        ObservableList<String> teamNames = FXCollections.observableArrayList();
        for (Team team : getModel().getSeason().getCurrentTeams()) {
            teamNames.add(team.getName());
        }
        teamListView.setItems(teamNames);
    }

    @FXML
    private void handleAdvanceRound(ActionEvent event) {
        ObservableList<String> selectedTeamNames = teamListView.getSelectionModel().getSelectedItems();

    // Ensure exactly two teams are selected
    if (selectedTeamNames.size() == 2) {
        ObservableList<Team> selectedTeams = FXCollections.observableArrayList();
        
        // Get the actual Team objects from their names
        for (String teamName : selectedTeamNames) {
            selectedTeams.add(getModel().getTeams().getTeam(teamName));
        }
        
        // Add the selected teams to the season
        getModel().getSeason().addTeams(selectedTeams);
        
        // Update the TableView with the new season data
        seasonTableView.setItems(getModel().getSeason().getCurrentSchedule());
        
        // Store the selected items before clearing the selection
        ObservableList<String> selectedItemsToRemove = FXCollections.observableArrayList(selectedTeamNames);
        
        // Clear the selection in the ListView
        teamListView.getSelectionModel().clearSelection();
        
        // Remove selected items from the list
        teamListView.getItems().removeAll(selectedItemsToRemove);
    }
    }

    @FXML
    private void close() {
        Stage stage = (Stage) arrangeSeasonButton.getScene().getWindow();
        stage.close();
    }

}
