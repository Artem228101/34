package sample.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.database.ApartmentsData;
import sample.database.DatabaseHandler;
import sample.database.User;
import java.io.IOException;
import java.sql.SQLException;

public class ControllerOpenRecord {
    @FXML private Label idLabel;
    @FXML private Label typeLabel;
    @FXML private Label priceLabel;
    @FXML private Label addressLabel;
    @FXML private Label descriptionLabel;
    @FXML private Button backButton;
    @FXML private Button bookButton;
    DatabaseHandler databaseHandler = new DatabaseHandler();
    @FXML void initialize() throws SQLException, ClassNotFoundException {
        ObservableList<ApartmentsData> apartmentsObservableList = databaseHandler.returnApartments(
                "SELECT * FROM apartments WHERE id=" + User.getEditedRecordId()
        );
        ApartmentsData apartmentsData = apartmentsObservableList.get(0);

        idLabel.setText(String.valueOf(apartmentsData.getId()));
        typeLabel.setText(apartmentsData.getType());
        priceLabel.setText(apartmentsData.getPrice());
        addressLabel.setText(apartmentsData.getAddress());
        descriptionLabel.setText(apartmentsData.getDescription());

        bookButton.setOnAction(actionEvent -> {
            try {databaseHandler.addRequests(User.getEditedRecordId(), User.getUsername());}
            catch (SQLException | ClassNotFoundException throwables){throwables.printStackTrace();}
        });

        backButton.setOnAction(actionEvent -> openDesktopWindow());
    }

    private void openDesktopWindow(){
        backButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/layout/desktop.fxml"));
        try {
            loader.load();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
