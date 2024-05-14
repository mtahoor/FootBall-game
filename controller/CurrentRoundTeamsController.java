package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.Game;
import model.Association;
import au.edu.uts.ap.javafx.Controller;
import model.Season;
public class CurrentRoundTeamsController extends Controller<Association>  {

    @FXML
    private TableView<Game> gameTableView;

    @FXML
    private TableColumn<Game, String> team1Column;

    @FXML
    private TableColumn<Game, String> vsColumn;

    @FXML
    private TableColumn<Game, String> team2Column;


    @FXML
    private Button closeButton;

    public Association getModel(){
        return this.model;
    }

    public void setModel(Association model) {
        
        if (model != null) {
            this.model = model;
            updateTableView();
        } else {
            System.out.println("Association model is null.");
        }
    }

    @FXML
    public void initialize() {
        setModel(getModel());
    
        // Set up the columns
        if (team1Column != null && vsColumn != null && team2Column != null) {
            team1Column.setCellValueFactory(cellData -> cellData.getValue().team1());
            vsColumn.setCellValueFactory(cellData -> cellData.getValue().vsProperty());
            team2Column.setCellValueFactory(cellData -> cellData.getValue().team2());
        } else {
            System.out.println("One or more TableColumn instances are null.");
        }
    }

    private void updateTableView() {
        if (gameTableView != null) {
            Season currentSeason = getModel().getSeason();
            if (currentSeason != null) {
                ObservableList<Game> currentSchedule = currentSeason.getCurrentSchedule();
                if (currentSchedule != null && !currentSchedule.isEmpty()) {
                    gameTableView.setItems(currentSchedule);
                } else {
                    System.out.println("Current schedule is empty or null.");
                }
            } else {
                System.out.println("Current season is null.");
            }
        } else {
            System.out.println("TableView instance is null.");
        }
    }


    @FXML
    private void close() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
