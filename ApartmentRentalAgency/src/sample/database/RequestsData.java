package sample.database;

public class RequestsData {
    private int id;
    private int idApartment;
    private String username;

    public RequestsData(int id, int idApartment, String username) {
        this.id = id;
        this.idApartment = idApartment;
        this.username = username;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public int getIdApartment() {return idApartment;}
    public void setIdApartment(int idApartment) {this.idApartment = idApartment;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
}
