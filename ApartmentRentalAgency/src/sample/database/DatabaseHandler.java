package sample.database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

//Этот класс отвечает за взаимодействует с базой данных
public class DatabaseHandler extends Configs{
    Connection dbConnection;

    //В этом методе происходит подключение к БД
    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    //Этот метод авторизирует пользователя
    public boolean signInUser(String username, String password) throws SQLException, ClassNotFoundException {
        String select = "SELECT * FROM users WHERE username=\"" + username + "\" AND password=\"" + password + "\"";

        PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
        try {
            //executeQuery() получает из БД значения и помещает их в resultSet
            //Если происходит ошибка - значит пользователя с введёнными логином или паролем нет
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
        catch (Exception exception){
            return false;
        }
    }

    public boolean signUpUser(String username, String password){
        try {
            String insert = "INSERT INTO users (username, password, role) VALUES(?,?,?)";

            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setBoolean(3, false);

            preparedStatement.executeUpdate();
            return true;
        }
        catch (Exception exception) {return false;}
    }

    public ObservableList<ApartmentsData> returnApartments(String select) throws SQLException, ClassNotFoundException {
        ObservableList<ApartmentsData> apartmentsObservableList = FXCollections.observableArrayList();
        Statement statement = getDbConnection().createStatement();

        ResultSet resultSet = statement.executeQuery(select);
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            String type = resultSet.getString(2);
            String price = resultSet.getString(3);
            String address = resultSet.getString(4);
            String description = resultSet.getString(5);

            apartmentsObservableList.add(new ApartmentsData(id, type, price, address, description));
        }
        return apartmentsObservableList;
    }

    public void updateRecord(ApartmentsData apartmentsData) throws SQLException, ClassNotFoundException {
        String update = "UPDATE apartments set " +
                "type = \"" + apartmentsData.getType() +
                "\", price = \"" + apartmentsData.getPrice() +
                "\", address = \"" + apartmentsData.getAddress() +
                "\", description = \"" + apartmentsData.getDescription() +
                "\" WHERE (id = " + User.getEditedRecordId() + ")";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(update);
        preparedStatement.executeUpdate();
    }

    public void addRecord(ApartmentsData apartmentsData) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO apartments " +
                "(type, price, address, description) " +
                "VALUES ('" +
                apartmentsData.getType() + "', '" + apartmentsData.getPrice() + "', '" +
                apartmentsData.getAddress() + "', '" + apartmentsData.getDescription() + "');";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
        preparedStatement.executeUpdate();
    }

    public Boolean returnRole(String username) throws SQLException, ClassNotFoundException {
        String select = "SELECT role FROM users WHERE username =\"" + username + "\"";

        Statement statement = getDbConnection().createStatement();

        ResultSet resultSet = statement.executeQuery(select);
        resultSet.next();

        return resultSet.getBoolean(1);
    }

    public void deleteRecord(int id){
        try {
            String select = "DELETE FROM apartments WHERE id = \"" + id + "\"";
            getDbConnection().prepareStatement(select).executeUpdate();
        }
        catch (Exception ignored){}
    }

    public void addRequests(int idApartment, String username) throws SQLException, ClassNotFoundException {
        String insert = "INSERT INTO requests " +
                "(id_apartments, username) " +
                "VALUES ('" + idApartment + "', '" + username + "');";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
        preparedStatement.executeUpdate();
    }

    public ObservableList<RequestsData> returnRequests() throws SQLException, ClassNotFoundException {
        String select = "SELECT * FROM requests";

        ObservableList<RequestsData> requestsObservableList = FXCollections.observableArrayList();
        Statement statement = getDbConnection().createStatement();

        ResultSet resultSet = statement.executeQuery(select);
        while (resultSet.next()){
            int id = resultSet.getInt(1);
            int idApartment = resultSet.getInt(2);
            String username = resultSet.getString(3);

            requestsObservableList.add(new RequestsData(id, idApartment, username));
        }
        return requestsObservableList;
    }

    public void deleteRequest(int id){
        try {
            String select = "DELETE FROM requests WHERE id = \"" + id + "\"";
            getDbConnection().prepareStatement(select).executeUpdate();
        }
        catch (Exception ignored){}
    }
}
