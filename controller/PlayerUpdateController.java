package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import au.edu.uts.ap.javafx.Controller;
import model.Player;
import model.Team;

public class PlayerUpdateController extends Controller<Team> {

    @FXML
    public Button updateButton;

    @FXML
    public Button addButton;

    @FXML
    private Button close;

    @FXML
    private TextField playerNameField;

    @FXML
    private TextField playerCreditField;

    @FXML
    private TextField playerAgeField;

    @FXML
    private TextField playerNoField;

    private Team selectedTeam;
    
    private Player selectedPlayer;

    @FXML
    public void initialize(Team team) {
        this.selectedTeam = team;
    }

    public void selectedPlayer(Player player){
        this.selectedPlayer=player;
        playerNameField.setText(player.getName());
        playerCreditField.setText(String.valueOf(player.getCredit()));
        playerAgeField.setText(String.valueOf(player.getAge()));
        playerNoField.setText(String.valueOf(player.getNo()));
    }

    @FXML
private void updatePlayer() {
    try {
        // Get updated player details from the input fields
        String name = playerNameField.getText();
        String creditText = playerCreditField.getText();
        String ageText = playerAgeField.getText();
        String noText = playerNoField.getText();

        // Validate player details using the Validator class
        Validator validator = new Validator();
        validator.generateErrors(name, creditText, ageText, noText);

        // Check if there are any validation errors
        if (!validator.errors().isEmpty()) {
            // Display error messages
            String errorMessage = String.join("\n", validator.errors());
            showErrorDialog(errorMessage);
            return;
        }

        // Convert credit, age, and no to appropriate data types
        double credit = Double.parseDouble(creditText);
        int age = Integer.parseInt(ageText);
        int no = Integer.parseInt(noText);

        // Check if credit and number are non-negative
        if (credit < 0 || no < 0) {
            showErrorDialog("Player credit and number cannot be negative");
            return;
        }

        // Update the selected player's information
        selectedPlayer.setName(name);
        selectedPlayer.setCredit(credit);
        selectedPlayer.setAge(age);
        selectedPlayer.setNo(no);

        // Close the window
        Stage stage = (Stage) playerNameField.getScene().getWindow();
        stage.close();

        // Optionally, update the table view to reflect the changes
        // You can implement this based on your application's design
    } catch (NumberFormatException ex) {
        // Handle number format exception (e.g., invalid credit, age, or number)
        showErrorDialog("Invalid number format for credit, age, or number");
    }
}


    @FXML
    private void addPlayer() {
        try {
            // Get player details from the input fields
            String name = playerNameField.getText();
            String creditText = playerCreditField.getText();
            String ageText = playerAgeField.getText();
            String noText = playerNoField.getText();

            // Validate player details using the Validator class
            Validator validator = new Validator();
            validator.generateErrors(name, creditText, ageText, noText);
            
            // Check if there are any validation errors
            if (!validator.errors().isEmpty()) {
                // Display error messages
                String errorMessage = String.join("\n", validator.errors());
                showErrorDialog(errorMessage);
                return;
            }

            // Convert credit, age, and no to appropriate data types
            double credit = Double.parseDouble(creditText);
            int age = Integer.parseInt(ageText);
            int no = Integer.parseInt(noText);

            // Check if credit and number are non-negative
            if (credit < 0 || no < 0) {
                showErrorDialog("Player credit and number cannot be negative");
                return;
            }

            // Create a new player with the provided details
            Player newPlayer = new Player(name, credit, age, no);

            // Add the new player to the team's list of players
            selectedTeam.getPlayers().addPlayer(newPlayer);

            Stage stage = (Stage) playerNameField.getScene().getWindow();
            stage.close();

            // Optionally, update the table view to reflect the changes
            // You can implement this based on your application's design
        } catch (NumberFormatException ex) {
            // Handle number format exception (e.g., invalid credit, age, or number)
            showErrorDialog("Invalid number format for credit, age, or number");
        }
    }

    private void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Errors");
        alert.setHeaderText("Input validation failed");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    @FXML
    private void close() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

    
}
