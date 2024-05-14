package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import au.edu.uts.ap.javafx.Controller;
import model.Association;
import model.Season;
import model.Record;

public class RecordController extends Controller<Association> {

    @FXML
    private TableView<Record> recordTableView;

    @FXML
    private TableColumn<Record, Integer> roundColumn;

    @FXML
    private TableColumn<Record, Integer> gameColumn;

    @FXML
    private TableColumn<Record, String> winningColumn;

    @FXML
    private TableColumn<Record, String> losingColumn;

    @FXML
    private Button close;

    public Association getModel() {
        return this.model;
    }

    @FXML
    public void initialize() {
        Season currentSeason = getModel().getSeason();
        if (currentSeason != null) {
            ObservableList<Record> records = currentSeason.record();
            System.out.println(records.size());
            recordTableView.setItems(records);
        }

        // Set cell value factories
        roundColumn.setCellValueFactory(cellData -> cellData.getValue().roundProperty().asObject());
        gameColumn.setCellValueFactory(cellData -> cellData.getValue().gameNumberProperty().asObject());
        winningColumn.setCellValueFactory(cellData -> cellData.getValue().winTeamProperty());
        losingColumn.setCellValueFactory(cellData -> cellData.getValue().loseTeamProperty());
    }

    @FXML
    private void close() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }

}
