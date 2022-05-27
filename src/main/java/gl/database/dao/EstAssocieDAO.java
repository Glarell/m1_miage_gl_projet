package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.EstAssocie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstAssocieDAO {

    public static EstAssocie getEstAssocieById(int id){
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        EstAssocie temp = new EstAssocie();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EstAssocie WHERE id_estassocie = ?");
            stmt.setInt(1,id);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                EstAssocie estAssocie = new EstAssocie();
                setEstAssocieAttributes(res, estAssocie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * Récupération de toutes les associations client - plaque présentes dans la BDD
     *
     * @return la liste des associations client - plaque
     */
    public static List<EstAssocie> getAllEstAssocie() {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<EstAssocie> listOfEstAssocie = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM EstAssocie");
            while (res.next()) {
                EstAssocie estAssocie = new EstAssocie();
                setEstAssocieAttributes(res, estAssocie);
                listOfEstAssocie.add(estAssocie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfEstAssocie;
    }

    /**
     * Récupération de toutes les associations client - plaque pour une plaque donnée dans la BDD
     *
     * @param id_plaque l'id de la plaque recherché
     * @return la liste des associations client - plaque
     */
    public static List<EstAssocie> getEstAssocieByPlaque(String id_plaque) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<EstAssocie> listOfEstAssocie = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EstAssocie WHERE id_plaque = ?");
            stmt.setString(1, id_plaque);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                EstAssocie estAssocie = new EstAssocie();
                setEstAssocieAttributes(res, estAssocie);
                listOfEstAssocie.add(estAssocie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfEstAssocie;
    }

    /**
     * Récupération de toutes les associations client - plaque pour un client donné dans la BDD
     *
     * @param id_client l'id du client recherché
     * @return la liste des associations client - plaque
     */
    public static List<EstAssocie> getEstAssocieByClient(int id_client) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<EstAssocie> listOfEstAssocie = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EstAssocie WHERE id_client = ?");
            stmt.setInt(1, id_client);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                EstAssocie estAssocie = new EstAssocie();
                setEstAssocieAttributes(res, estAssocie);
                listOfEstAssocie.add(estAssocie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfEstAssocie;
    }

    /**
     * Récupération de toutes les associations client - plaque pour un client donné et une plaque dans la BDD
     *
     * @param id_client l'id du client recherché
     * @return la liste des associations client - plaque
     */
    public static List<EstAssocie> getEstAssocieByClientAndPlaque(int id_client, String plaque_client) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<EstAssocie> listOfEstAssocie = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EstAssocie WHERE id_client = ? and id_plaque= ?");
            stmt.setInt(1, id_client);
            stmt.setString(2, plaque_client);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                EstAssocie estAssocie = new EstAssocie();
                setEstAssocieAttributes(res, estAssocie);
                listOfEstAssocie.add(estAssocie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfEstAssocie;
    }

    /**
     * Récupération de l'association client - plaque temporaire dans la BDD
     *
     * @param id_client l'id du client recherché
     * @param id_plaque l'id de la plaque recherché
     * @return l'association client - plaque
     */
    public static EstAssocie getEstAssocieTemporaire(int id_client, String id_plaque) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        EstAssocie estAssocie = new EstAssocie();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM EstAssocie WHERE id_client = ? and id_plaque = ? and isTemporaire = true");
            stmt.setInt(1, id_client);
            stmt.setString(2, id_plaque);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setEstAssocieAttributes(res, estAssocie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estAssocie;
    }

    /**
     * Insertion d'une association client - plaque dans la BDD
     *
     * @param estAssocie l'association client - plaque
     * @throws SQLException renvoie une exception
     */
    public static void insertNewEstAssocie(EstAssocie estAssocie) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO EstAssocie (id_client, id_plaque) VALUES (?,?)");
        getEstAssocieAttributes(stmt, estAssocie);
        stmt.executeUpdate();
    }

    /**
     * Insertion d'une association client - plaque temporaire dans la BDD
     *
     * @param estAssocie l'association client - plaque
     * @throws SQLException renvoie une exception
     */
    public static void insertNewEstAssocieTemporaire(EstAssocie estAssocie) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO EstAssocie (id_client, id_plaque, isTemporaire) VALUES (?,?,?)");
        getEstAssocieAttributes(stmt, estAssocie);
        stmt.setBoolean(3, true);
        stmt.executeUpdate();
    }

    /**
     * Suppression d'une association client - plaque temporaire dans la BDD
     *
     * @param id_client l'id du client recherché
     * @param id_plaque l'id de la plaque recherchée
     * @throws SQLException renvoie une exception
     */
    public static void deleteOldEstAssocie(int id_client, String id_plaque) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM EstAssocie WHERE id_client = ? AND id_plaque = ?");
        stmt.setInt(1, id_client);
        stmt.setString(2, id_plaque);
        stmt.executeUpdate();
    }

    /**
     * Association des résultats d'une requête à une association client - plaque
     *
     * @param res        la requête contenant les données
     * @param estAssocie l'association client - plaque
     * @throws SQLException renvoie une exception
     */
    private static void setEstAssocieAttributes(ResultSet res, EstAssocie estAssocie) throws SQLException {
        estAssocie.setId_estAssocie(res.getInt(1));
        estAssocie.setTemporaire(res.getBoolean(2));
        estAssocie.setId_client(res.getInt(3));
        estAssocie.setId_plaque(res.getString(4));
    }

    /**
     * Affectation des données dans la requête
     *
     * @param stmt       la requête
     * @param estAssocie l'association client - plaque
     * @throws SQLException renvoie une exception
     */
    private static void getEstAssocieAttributes(PreparedStatement stmt, EstAssocie estAssocie) throws SQLException {
        stmt.setInt(1, estAssocie.getId_client());
        stmt.setString(2, estAssocie.getId_plaque());
    }
}
