package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.Borne;
import gl.database.model.EtatBorne;
import gl.database.model.Notification;
import gl.database.model.VariableApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
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
     * Vérifie si une borne est à un état souhaité
     *
     * @param id_borne     l'identifiant de la borne actuelle
     * @param id_etatBorne l'état de borne souhaité
     * @return vrai si la borne est disponible
     */
    public static boolean isBorneWithEtatBorne(int id_borne, String id_etatBorne) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        boolean isDisponible = false;
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT id_etatborne FROM Borne WHERE id_borne = ?");
            stmt.setInt(1, id_borne);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                isDisponible = res.getString(1).equals(id_etatBorne);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDisponible;
    }

    /**
     * Récupération de toutes les bornes disponibles à une date et un intervalle donné
     *
     * @param date             la date de recherche
     * @param debut_intervalle intervalle de début
     * @param fin_intervalle   intervalle de fin
     */
    public static List<Integer> getAllBorneFromDateDispo(Date date, Time debut_intervalle, Time fin_intervalle) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<Integer> listOfBorneFromReservation = new ArrayList<>();
        List<Integer> listOfBorneFromAbonnement = new ArrayList<>();
        try {
            PreparedStatement stmtReservation = conn.prepareStatement("SELECT id_borne FROM Borne " +
                    "EXCEPT SELECT id_borne FROM Reservation where date_reservation = ? and not ((? > fin_intervalle and ? > fin_intervalle) " +
                    "or (? < debut_intervalle and ? < debut_intervalle))");
            BorneDAO.getAttributesForDateDispo(stmtReservation, date, debut_intervalle, fin_intervalle);
            ResultSet resReservation = stmtReservation.executeQuery();
            while (resReservation.next()) {
                listOfBorneFromReservation.add(resReservation.getInt(1));
            }

            PreparedStatement stmtAbonnement = conn.prepareStatement("SELECT id_borne FROM Borne " +
                    "EXCEPT SELECT id_borne FROM Abonnement where date_abonnement = ? and not ((? > fin_intervalle and ? > fin_intervalle) " +
                    "or (? < debut_intervalle and ? < debut_intervalle))");
            BorneDAO.getAttributesForDateDispo(stmtAbonnement, date, debut_intervalle, fin_intervalle);
            ResultSet resAbonnement = stmtAbonnement.executeQuery();
            while (resAbonnement.next()) {
                listOfBorneFromAbonnement.add(resAbonnement.getInt(1));
            }
            listOfBorneFromReservation.retainAll(listOfBorneFromAbonnement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfBorneFromReservation;
    }

    /**
     * Récupération d'une borne disponible à l'instant présent pour une réservation sur le moment
     *
     * @param date             la date de recherche
     * @param debut_intervalle intervalle de début
     * @param fin_intervalle   intervalle de fin
     */
    public static List<Borne> getAllBorneAtInstant(Date date, Time debut_intervalle, Time fin_intervalle) {
        VariableApplication delay = VariableApplicationDAO.getVariableApplicationByName("délai attente");
        Calendar cal = Calendar.getInstance();
        cal.setTime(debut_intervalle);
        cal.add(Calendar.MINUTE, delay.getValeur());
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<Borne> listOfBorneFromReservation = new ArrayList<>();
        List<Borne> listOfBorneFromAbonnement = new ArrayList<>();
        try {
            PreparedStatement stmtReservation = conn.prepareStatement("SELECT * FROM Borne WHERE id_etatborne = 'disponible'" +
                    "EXCEPT SELECT borne.id_borne, borne.id_etatborne FROM Borne inner join reservation r on borne.id_borne = r.id_borne where date_reservation = ? and not ((? > debut_intervalle and ? > debut_intervalle ) " +
                    "or (? < debut_intervalle and ? < debut_intervalle))");
            BorneDAO.getAttributesForDateDispo(stmtReservation, date, debut_intervalle, new Time(cal.getTimeInMillis()));
            ResultSet resReservation = stmtReservation.executeQuery();
            while (resReservation.next()) {
                listOfBorneFromReservation.add(new Borne(resReservation.getInt(1), resReservation.getString(2)));
            }

            PreparedStatement stmtAbonnement = conn.prepareStatement("SELECT * FROM Borne WHERE id_etatborne = 'disponible'" +
                    "EXCEPT SELECT borne.id_borne, borne.id_etatborne FROM Borne INNER JOIN abonnement a on borne.id_borne = a.id_borne where date_abonnement = ? and not ((? > debut_intervalle and ? > debut_intervalle ) " +
                    "or (? < debut_intervalle and ? < debut_intervalle))");
            BorneDAO.getAttributesForDateDispo(stmtAbonnement, date, debut_intervalle, new Time(cal.getTimeInMillis()));
            ResultSet resAbonnement = stmtAbonnement.executeQuery();
            while (resAbonnement.next()) {
                listOfBorneFromAbonnement.add(new Borne(resAbonnement.getInt(1), resAbonnement.getString(2)));
            }
            boolean condition = false;
            List<Borne> final_bornes = new ArrayList<>();
            for (Borne borne : listOfBorneFromAbonnement){
                for (Borne borne1 : listOfBorneFromReservation){
                    if (borne.getId_borne() == borne1.getId_borne()){
                        final_bornes.add(borne);
                    }
                }
            }
            return final_bornes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return List.of();
    }

    public static List<Integer> getAllBorneFromDateDispoUpdate(Date date, Time int1, Time int2, int id) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<Integer> listOfBorne = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT id_borne FROM Reservation EXCEPT SELECT id_borne FROM Reservation where id_reservation != ? and date_reservation = ? and not ((? > fin_intervalle and ? > fin_intervalle) or (? < debut_intervalle and ? < debut_intervalle));");
            stmt.setInt(1, id);
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

    /**
     * Affectation des données dans la requête
     *
     * @param stmt             la requête
     * @param date             la date de la recherche
     * @param debut_intervalle le début de l'intervalle de la recherche
     * @param fin_intervalle   la fin de l'intervalle de la recherche
     * @throws SQLException renvoie une exception
     */
    private static void getAttributesForDateDispo(PreparedStatement stmt, Date date, Time debut_intervalle, Time fin_intervalle) throws SQLException {
        stmt.setDate(1, date);
        stmt.setTime(2, debut_intervalle);
        stmt.setTime(3, fin_intervalle);
        stmt.setTime(4, debut_intervalle);
        stmt.setTime(5, fin_intervalle);
    }

    public static void deleteAllReservationsToABorne(int id_borne) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select id_client,id_reservation from reservation inner join EstAssocie EA on EA.id_estAssocie = Reservation.id_estAssocie where id_borne=?");
        stmt.setInt(1,id_borne);
        ResultSet res = stmt.executeQuery();
        while (res.next()) {
            int id_client = res.getInt(1);
            int id_reservation = res.getInt(2);
            Notification notification = new Notification(String.format("Votre reservation [%s] sur la borne [%s], a été annulée suite à des problèmes techniques...\nVeuillez nous excuser pour le désagrément :)",id_reservation, id_borne), id_client, "Mail");
            NotificationDAO.insertNewNotification(notification);
            notification = new Notification(String.format("Votre reservation [%s] sur la borne [%s], a été annulée suite à des problèmes techniques...\nVeuillez nous excuser pour le désagrément :)",id_reservation, id_borne), id_client, "SMS");
            NotificationDAO.insertNewNotification(notification);
        }
        stmt = conn.prepareStatement("delete from reservation where id_borne=?");
        stmt.setInt(1,id_borne);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("delete from reservation where id_borne=?");
        stmt.setInt(1,id_borne);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("update borne set id_etatBorne='disponible' where id_borne=?");
        stmt.setInt(1,id_borne);
        stmt.executeUpdate();
    }
}
