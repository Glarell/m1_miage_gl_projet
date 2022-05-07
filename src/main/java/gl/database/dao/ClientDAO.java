package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.Client;
import gl.database.model.EtatBorne;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public static boolean isEmailAlreadyExist(String email){
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Client where email = '"+email+"'");
            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Client registrerClient(Client client) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("INSERT INTO Client (nom,prenom,adresse,telephone,email,mdp,carte)" + "VALUES ("+client.toString()+")");
        return client;
    }

    public static Client getClientByEmailPwd(String email, String password) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        Client client = new Client();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Client where email = '"+email+"' AND mdp = '"+password+"'");
            while (res.next()) {
                setClientAttributes(res, client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    public static List<Client> getListClient() {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<Client> listOfClient = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Client");
            while (res.next()) {
                Client client = new Client();
                setClientAttributes(res, client);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfClient;
    }

    private static void setClientAttributes(ResultSet res, Client client) throws SQLException {
        client.setId_client(res.getInt(1));
        client.setNom(res.getString(2));
        client.setPrenom(res.getString(3));
        client.setAdresse(res.getString(4));
        client.setTelephone(res.getString(5));
        client.setEmail(res.getString(6));
        client.setMdp(res.getString(7));
        client.setCarte(res.getString(8));
    }
}
