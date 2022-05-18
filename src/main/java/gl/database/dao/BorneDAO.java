package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.Borne;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorneDAO {

    /**
     * Récupération de toutes les bornes présentes dans la BDD
     *
     * @return la liste des bornes
     */
    public static List<Borne> getAllBorne() {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<Borne> listOfBorne = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Borne ORDER BY id_borne");
            while (res.next()) {
                Borne borne = new Borne();
                setBorneAttributes(res, borne);
                listOfBorne.add(borne);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfBorne;
    }

    /**
     * Récupération de toutes les bornes associées à un état dans la BDD
     *
     * @param id_etatBorne l'état recherché
     * @return la liste des bornes
     */
    public static List<Borne> getAllBorneFromState(String id_etatBorne) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<Borne> listOfBorne = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Borne WHERE id_etatBorne = ? ORDER BY id_borne");
            stmt.setString(1, id_etatBorne);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                Borne borne = new Borne();
                setBorneAttributes(res, borne);
                listOfBorne.add(borne);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfBorne;
    }

    /**
     * Récupération de toutes les bornes disponibles à une date et un intervalle donné
     *
     * @param date la date de recherche
     * @param int1 intervalle de début
     * @param int2 intervalle de fin
     */
    public static List<Integer> getAllBorneFromDateDispo(Date date, Time int1, Time int2) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<Integer> listOfBorne = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT id_borne FROM Reservation EXCEPT SELECT id_borne FROM Reservation where date_reservation = ? and not ((? > fin_intervalle and ? > fin_intervalle) or (? < debut_intervalle and ? < debut_intervalle));");
            stmt.setDate(1, date);
            stmt.setTime(2, int1);
            stmt.setTime(3, int2);
            stmt.setTime(4, int1);
            stmt.setTime(5, int2);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                listOfBorne.add(res.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfBorne;
    }

    public static List<Integer> getAllBorneFromDateDispoUpdate(Date date, Time int1, Time int2,int id) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<Integer> listOfBorne = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT id_borne FROM Reservation EXCEPT SELECT id_borne FROM Reservation where id_reservation != ? and date_reservation = ? and not ((? > fin_intervalle and ? > fin_intervalle) or (? < debut_intervalle and ? < debut_intervalle));");
            stmt.setInt(1,id);
            stmt.setDate(2, date);
            stmt.setTime(3, int1);
            stmt.setTime(4, int2);
            stmt.setTime(5, int1);
            stmt.setTime(6, int2);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                listOfBorne.add(res.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfBorne;
    }

    /**
     * Mise à jour de l'état d'une borne dans la BDD
     *
     * @param borne        la borne à modifier
     * @param id_etatBorne le nouvel état de la borne
     * @throws SQLException renvoie une exception
     */
    public static void updateStateOfBorne(Borne borne, String id_etatBorne) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Borne SET id_etatBorne = ? WHERE id_borne = ?;");
        getBorneAttributes(stmt, borne, id_etatBorne);
        stmt.executeUpdate();
    }

    /**
     * Association des résultats d'une requête à une borne
     *
     * @param res   la requête contenant les données
     * @param borne la borne
     * @throws SQLException renvoie une exception
     */
    private static void setBorneAttributes(ResultSet res, Borne borne) throws SQLException {
        borne.setId_borne(res.getInt(1));
        borne.setId_etatBorne(res.getString(2));
    }

    /**
     * Affectation des données dans la requête
     *
     * @param stmt         la requête
     * @param borne        la borne
     * @param id_etatBorne l'état de la borne
     * @throws SQLException renvoie une exception
     */
    private static void getBorneAttributes(PreparedStatement stmt, Borne borne, String id_etatBorne) throws SQLException {
        stmt.setString(1, id_etatBorne);
        stmt.setInt(2, borne.getId_borne());
        borne.setId_etatBorne(id_etatBorne);
    }

}
