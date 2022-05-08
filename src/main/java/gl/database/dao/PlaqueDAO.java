package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.Plaque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaqueDAO {

    /**
     * Récupération de tous les plaques d'immatriculation présentes dans la BDD
     *
     * @return la liste des plaques d'immatriculation
     */
    public static List<Plaque> getAllPlaque() {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<Plaque> listOfPlaque = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Plaque");
            while (res.next()) {
                Plaque plaque = new Plaque();
                setPlaqueAttributes(res, plaque);
                listOfPlaque.add(plaque);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfPlaque;
    }

    /**
     * Récupération d'une plaque d'immatriculation associée à un id dans la BDD
     *
     * @param id_plaque l'id de la plaque d'immatriculation
     * @return la plaque d'immatriculation
     */
    public static Plaque getPlaqueByID(String id_plaque) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        Plaque plaque = new Plaque();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Plaque WHERE id_plaque = ?");
            getPlaqueAttributes(stmt, id_plaque);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setPlaqueAttributes(res, plaque);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plaque;
    }

    /**
     * Insertion d'une plaque d'immatriculation dans la BDD
     *
     * @param id_plaque la plaque d'immatriculation à ajouter
     * @throws SQLException renvoie une exception
     */
    public static void insertNewPlaque(String id_plaque) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Plaque (id_plaque) VALUES (?)");
        getPlaqueAttributes(stmt, id_plaque);
        stmt.executeUpdate();
    }

    /**
     * Suppression d'une plaque d'immatriculation de la BDD
     *
     * @param id_plaque la plaque d'immatriculation à supprimer
     * @throws SQLException renvoie une exception
     */
    public static void deleteOldPlaque(String id_plaque) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Plaque WHERE id_plaque = ?");
        getPlaqueAttributes(stmt, id_plaque);
        stmt.executeUpdate();
    }

    /**
     * Association des résultats d'une requête à une plaque d'immatriculation
     *
     * @param res    la requête contenant les données
     * @param plaque la plaque d'immatriculation
     * @throws SQLException renvoie une exception
     */
    private static void setPlaqueAttributes(ResultSet res, Plaque plaque) throws SQLException {
        plaque.setId_plaque(res.getString(1));
    }

    /**
     * Affectation des données dans la requête
     *
     * @param stmt      la requête
     * @param id_plaque la plaque d'immatriculation
     * @throws SQLException renvoie une exception
     */
    private static void getPlaqueAttributes(PreparedStatement stmt, String id_plaque) throws SQLException {
        stmt.setString(1, id_plaque);
    }
}
