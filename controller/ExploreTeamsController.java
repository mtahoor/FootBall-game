package controller;

import au.edu.uts.ap.javafx.ViewLoader;
import au.edu.uts.ap.javafx.Controller;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import model.Teams;

public class ExploreTeamsController extends Controller<Teams> {


    @FXML
    private Button closeButton;

    
    public Teams getTeamsModel() {
        return this.model;
    }

    @FXML
    public void teamsMenu() {
        try {
            Stage stage = new Stage();
            stage.setX(ViewLoader.X + 601);
            stage.setY(ViewLoader.Y);
            stage.getIcons().add(new Image("/view/nba.png"));
            ViewLoader.showStage(getTeamsModel(), "/view/Teamstable.fxml", "Teams Menu", stage);


        } catch (IOException ex) {
            Logger.getLogger(AssociationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void viewPlayers() {
        try {
            // Create a new stage
            Stage stage = new Stage();
            stage.setX(ViewLoader.X + 601);
            stage.setY(ViewLoader.Y);
            stage.getIcons().add(new Image("/view/nba.png"));
            
            // Load the PlayersView.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PlayersView.fxml"));
            Parent root = loader.load();
            
            // Get the controller associated with the PlayersView.fxml file
            ViewPlayersController controller = loader.getController();
            
            // Pass the current Teams model to the controller
            controller.initialize(getTeamsModel());
            
            // Show the stage with the loaded PlayersView.fxml file
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Teams Menu");
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AssociationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
}
