package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.Abonnement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementDAO {

    public static Abonnement getAbonnementById(int id) {
        Abonnement abonnement = new Abonnement();
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Abonnement where id_abonnement = ?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setAbonnementAttributes(res, abonnement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return abonnement;
    }

    /**
     * Récupération des abonnements liés à un client
     *
     * @param id_client l'id du client
     * @return la liste des abonnements
     */
    public static List<Abonnement> getAbonnementByClient(int id_client) {
        List<Abonnement> listOfAbonnement = new ArrayList<>();
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Abonnement where id_client = ?");
            stmt.setInt(1, id_client);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                Abonnement abonnement = new Abonnement();
                setAbonnementAttributes(res, abonnement);
                listOfAbonnement.add(abonnement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfAbonnement;
    }

    public static Abonnement registrerAbonnement(Abonnement abonnement) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Abonnement (date_abonnement,debut_intervalle,fin_intervalle,id_client,id_borne) VALUES (?,?,?,?,?)");
        stmt.setDate(1, abonnement.getDate_abonnement());
        stmt.setTime(2, abonnement.getDebut_intervalle());
        stmt.setTime(3, abonnement.getFin_intervalle());
        stmt.setInt(4, abonnement.getId_client());
        stmt.setInt(5, abonnement.getId_borne());
        stmt.executeUpdate();
        return abonnement;
    }

    /**
     * Recherche d'abonnements existants du même client une heure précédant le début de l'abonnement
     *
     * @param id_client  l'identifiant du client
     * @param abonnement l'abonnement de référence
     * @return vrai s'il existe un abonnement
     */
    public static boolean hasExistingAbonnementFromSameUser(int id_client, Abonnement abonnement) {
        boolean result = false;
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT count(*) FROM Abonnement" +
                    " WHERE id_client = ? AND date_abonnement = ? AND fin_intervalle >= ? AND fin_intervalle <= ?");
            stmt.setInt(1, id_client);
            stmt.setDate(2, abonnement.getDate_abonnement());
            stmt.setTime(3, Time.valueOf(abonnement.getDebut_intervalle().toLocalTime().minusHours(1)));
            stmt.setTime(4, abonnement.getDebut_intervalle());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                result = res.getInt(1) != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Recherche de l'abonnement du même client une heure précédant le début de l'abonnement
     *
     * @param id_client  l'identifiant du client
     * @param abonnement l'abonnement de référence
     * @return l'abonnement
     */
    public static Abonnement getExistingAbonnementFromSameUser(int id_client, Abonnement abonnement) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        Abonnement existing_abonnement = new Abonnement();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT Abonnement.* FROM Abonnement" +
                    " WHERE id_client = ? AND date_abonnement = ? AND fin_intervalle >= ? AND fin_intervalle <= ?");
            stmt.setInt(1, id_client);
            stmt.setDate(2, abonnement.getDate_abonnement());
            stmt.setTime(3, Time.valueOf(abonnement.getDebut_intervalle().toLocalTime().minusHours(1)));
            stmt.setTime(4, abonnement.getDebut_intervalle());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setAbonnementAttributes(res, existing_abonnement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing_abonnement;
    }

    /**
     * Recherche d'abonnements existants pour une date donnée, un intervalle donné et une borne donnée
     *
     * @param date_reservation la date de recherche
     * @param debut_intervalle le début de l'intervalle de recherche
     * @param fin_intervalle   la fin de l'intervalle de recherche
     * @return vrai s'il existe un abonnement
     */
    public static boolean hasExistingAbonnement(Date date_reservation, Time debut_intervalle, Time fin_intervalle, int id_borne) {
        boolean result = false;
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT count(*) FROM Abonnement" +
                    " WHERE date_abonnement = ? AND debut_intervalle >= ? AND debut_intervalle <= ? AND id_borne = ?");
            stmt.setDate(1, date_reservation);
            stmt.setTime(2, debut_intervalle);
            stmt.setTime(3, fin_intervalle);
            stmt.setInt(4, id_borne);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                result = res.getInt(1) != 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Mise à jour d'un abonnement après une fusion
     *
     * @param abonnement     l'abonnement à modifier
     * @param fin_intervalle la nouvelle fin de la abonnement
     * @throws SQLException renvoie une exception
     */
    public static void updateMergeAbonnement(Abonnement abonnement, Time fin_intervalle) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Abonnement SET fin_intervalle = ? WHERE id_abonnement = ?");
        stmt.setTime(1, fin_intervalle);
        stmt.setInt(2, abonnement.getId_abonnement());
        stmt.executeUpdate();
    }

    /**
     * Suppression d'un abonnement dans la BDD
     *
     * @param id_client l'id du client
     * @throws SQLException renvoie une exception
     */
    public static void deleteOldAbonnementByClient(int id_client) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Abonnement WHERE id_client = ?");
        stmt.setInt(1, id_client);
        stmt.executeUpdate();
    }

    private static void setAbonnementAttributes(ResultSet res, Abonnement abonnement) throws SQLException {
        abonnement.setId_abonnement(res.getInt(1));
        abonnement.setDate_abonnement(res.getDate(2));
        abonnement.setDebut_intervalle(res.getTime(3));
        abonnement.setFin_intervalle(res.getTime(4));
        abonnement.setId_client(res.getInt(5));
        abonnement.setId_borne(res.getInt(6));
    }
}
