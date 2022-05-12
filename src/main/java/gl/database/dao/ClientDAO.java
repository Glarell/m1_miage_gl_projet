package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public static boolean isEmailAlreadyExist(String email) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Client where email = ?");
            stmt.setString(1,email);
            ResultSet res = stmt.executeQuery();
            return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Client registrerClient(Client client) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Client (nom,prenom,adresse,telephone,email,mdp,carte) VALUES (?,?,?,?,?,?,?)");
        getClientAttributes(stmt, client);
        stmt.executeUpdate();
        return client;
    }

    /**
     * Création d'un nouveau client dans la BDD avec un id spécifique
     *
     * @param client le nouveau client
     * @throws SQLException renvoie une exception
     */
    public static void registrerClientWithId(Client client) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Client (nom,prenom,adresse,telephone,email,mdp,carte,id_client) VALUES (?,?,?,?,?,?,?,?)");
        getClientAttributes(stmt, client);
        stmt.setInt(8, client.getId_client());
        stmt.executeUpdate();
    }

    /**
     * Suppression d'un client de la BDD
     *
     * @param id_client l'id du client à supprimer
     * @throws SQLException renvoie une exception
     */
    public static void deleteClient(int id_client) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Client WHERE id_client = ?");
        stmt.setInt(1, id_client);
        stmt.executeUpdate();
    }

    public static Client getClientByEmailPwd(String email, String password) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        Client client = new Client();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Client where email=? AND mdp=?");
            stmt.setString(1,email);
            stmt.setString(2,password);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setClientAttributes(res, client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    public static Client getClient(Client client) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Client where id_client=?");
            stmt.setInt(1,client.getId_client());
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
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

    /**
     * Affectation des données dans la requête
     *
     * @param stmt   la requête
     * @param client le client
     * @throws SQLException renvoie une exception
     */
    private static void getClientAttributes(PreparedStatement stmt, Client client) throws SQLException {
        stmt.setString(1, client.getNom());
        stmt.setString(2, client.getPrenom());
        stmt.setString(3, client.getAdresse());
        stmt.setString(4, client.getTelephone());
        stmt.setString(5, client.getEmail());
        stmt.setString(6, client.getMdp());
        stmt.setString(7, client.getCarte());
    }
}
