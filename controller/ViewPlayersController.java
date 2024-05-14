package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Player;
import model.Teams;
import model.Team;

public class ViewPlayersController {
    
    @FXML
    private TableView<Player> playerTableView;
    
    @FXML
    private TableColumn<Player, String> teamColumn;
    
    @FXML
    private TableColumn<Player, String> playerNameColumn;
    
    @FXML
    private TableColumn<Player, Double> playerCreditColumn;
    
    @FXML
    private TableColumn<Player, Integer> playerAgeColumn;
    
    @FXML
    private TableColumn<Player, Integer> playerNoColumn;
    
    @FXML
    private TableColumn<Player, String> playerLevelColumn;
    
    @FXML
    private TextField filterNameTextField;
    
    @FXML
    private TextField filterLevelTextField;
    
    @FXML
    private TextField filterAgeFromTextField;
    
    @FXML
    private TextField filterAgeToTextField;

    private Teams teams;
    private Teams filteredTeams;

    public void initialize(Teams teams) {
        this.teams = teams;
        this.filteredTeams = new Teams();
        
        // Initialize table columns
        teamColumn.setCellValueFactory(data -> data.getValue().getTeam().nameProperty());
        playerNameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        playerCreditColumn.setCellValueFactory(data -> data.getValue().getPlayerCreditProperty().asObject());
        playerAgeColumn.setCellValueFactory(data -> data.getValue().getPlayerAgeProperty().asObject());
        playerNoColumn.setCellValueFactory(data -> data.getValue().getPlayerNoProperty().asObject());
        playerLevelColumn.setCellValueFactory(data -> data.getValue().levelProperty());
        
        // Populate table with player data
        playerTableView.setItems(filteredTeams.allPlayersList());
        
        // Add event handlers to text fields for filtering
        filterNameTextField.textProperty().addListener((observable, oldValue, newValue) -> filterPlayers());
        filterLevelTextField.textProperty().addListener((observable, oldValue, newValue) -> filterPlayers());
        filterAgeFromTextField.textProperty().addListener((observable, oldValue, newValue) -> filterPlayers());
        filterAgeToTextField.textProperty().addListener((observable, oldValue, newValue) -> filterPlayers());
    }

    private void filterPlayers() {
        String name = filterNameTextField.getText().toLowerCase();
        String level = filterLevelTextField.getText().toLowerCase();
        int ageFrom = parseTextFieldToInt(filterAgeFromTextField.getText());
        int ageTo = parseTextFieldToInt(filterAgeToTextField.getText());
    
        // Clear the previous filtered list
        teams.currentTeams().forEach(team ->{

            filteredTeams.remove(team);
        });
        System.out.println(filteredTeams.allPlayersList().size()+" intial size");
        // Iterate over all teams in the original data
        teams.currentTeams().forEach(team -> {
            Team filteredTeam = new Team(team.getName()); // Create a new team to hold filtered players
    
            // Filter players based on the entered criteria
            team.getPlayers().filterList(name, level, ageFrom, ageTo);
    
            // Clear the current players list of the filtered team
            filteredTeam.getCurrentPlayers().clear();

            System.out.println(filteredTeam.getCurrentPlayers().size()+"first");
    
            // Add filtered players to the new team
            filteredTeam.getCurrentPlayers().addAll(team.getPlayers().getFilteredPlayers());
            System.out.println(filteredTeam.getCurrentPlayers().size()+"end");
            
            // Add the new team to the list of filtered teams
            filteredTeams.currentTeams().add(filteredTeam);
        });
        System.out.println(filteredTeams.allPlayersList().size()+" last size");

    
        // Update the table with the filtered data
        playerTableView.setItems(filteredTeams.allPlayersList());
    }
    
    

    private int parseTextFieldToInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0; // Return a default value if parsing fails
        }
    }
    
}
