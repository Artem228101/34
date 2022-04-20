package sample.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.database.ApartmentsData;
import sample.database.DatabaseHandler;
import sample.database.User;
import java.io.IOException;
import java.sql.SQLException;

public class ControllerEditRecord {
    @FXML private Label errorLabel;

    @FXML private TextField typeTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField descriptionTextField;

    @FXML private Button applyButton;
    @FXML private Button cancelButton;

    DatabaseHandler databaseHandler = new DatabaseHandler();

    @FXML void initialize() throws SQLException, ClassNotFoundException {
        ObservableList<ApartmentsData> apartmentsObservableList = databaseHandler.returnApartments(
                "SELECT * FROM apartments WHERE id=" + User.getEditedRecordId()
        );
        ApartmentsData apartmentsData = apartmentsObservableList.get(0);

        typeTextField.setText(apartmentsData.getType());
        priceTextField.setText(apartmentsData.getPrice());
        addressTextField.setText(apartmentsData.getAddress());
        descriptionTextField.setText(apartmentsData.getDescription());

        applyButton.setOnAction(actionEvent -> {
            String type = typeTextField.getText();
            String price = priceTextField.getText();
            String address = addressTextField.getText();
            String description = descriptionTextField.getText();

            if (!type.equals("")){
                apartmentsData.setType(type);
            }
            if (!price.equals("")){
                apartmentsData.setPrice(price);
            }
            if (!address.equals("")){
                apartmentsData.setAddress(address);
            }
            if (!description.equals("")){
                apartmentsData.setDescription(description);
            }

            try {databaseHandler.updateRecord(apartmentsData);}
            catch (SQLException | ClassNotFoundException throwables) {
                errorLabel.setText("Не все поля заполнены");
                throwables.printStackTrace();
            }
            openDesktopWindow();
        });

        cancelButton.setOnAction(actionEvent -> openDesktopWindow());
    }

    private void openDesktopWindow(){
        cancelButton.getScene().getWindow().hide();
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