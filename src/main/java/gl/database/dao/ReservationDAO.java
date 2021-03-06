package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.Notification;
import gl.database.model.Reservation;

import java.sql.*;
import java.util.ArrayList;

public class ReservationDAO {

    public static double[] getReleveMensuel() throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY,ResultSet.HOLD_CURSORS_OVER_COMMIT);
        ResultSet res = stmt.executeQuery("select sum(prix),count(*) from reservation where (SELECT EXTRACT(MONTH FROM current_timestamp)=DATE_PART('month',date_reservation))");
        res.first();
        double[] array = new double[2];
        array[0] = res.getInt(1);
        array[1] = res.getInt(2);
        return array;
    }

    public static ArrayList<Reservation> getAllReservationByIdClient(int id_client) {
        ArrayList<Reservation> list = new ArrayList<>();
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        Reservation reservation = new Reservation();
        try {
            PreparedStatement stmt = conn.prepareStatement("select * from reservation" +
                    " inner join estassocie e on e.id_estassocie = reservation.id_estassocie" +
                    " where e.id_client = ?" +
                    " AND (reservation.date_reservation>=CURRENT_DATE)" +
                    " AND (nb_prolongement<3)" +
                    " AND (now()::time<(fin_intervalle::time-('00:30:00')::time)::time)");
            stmt.setInt(1, id_client);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setReservationAttributes(res, reservation);
                list.add(reservation);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    /**
     * Récupération de toutes les réservations associées à un client qui sont actuellement en cours
     *
     * @param id_client l'identifiant de client
     * @return la liste des réservations
     */
    public static ArrayList<Reservation> getAllReservationInProgressByIdClient(int id_client) {
        ArrayList<Reservation> list = new ArrayList<>();
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        Reservation reservation = new Reservation();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT Reservation.* FROM Reservation" +
                    " INNER JOIN estassocie e ON e.id_estassocie = reservation.id_estassocie" +
                    " WHERE e.id_client = ?" +
                    " AND arrivee_client IS NOT NULL AND depart_client IS NULL");
            stmt.setInt(1, id_client);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setReservationAttributes(res, reservation);
                list.add(reservation);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    public static void deleteReservationById(int id) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM reservation where id_reservation = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateReservationDate(int id, Date date, Time int1, Time int2) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE Reservation SET date_reservation = ?, debut_intervalle = ?, fin_intervalle = ? where id_reservation = ?");
            stmt.setDate(1, date);
            stmt.setTime(2, int1);
            stmt.setTime(3, int2);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateReservationEstAssocie(int id, int est_associe) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE Reservation SET id_estassocie = ? where id_reservation = ?");
            stmt.setInt(1, est_associe);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Reservation getReservationById(int id) {
        Reservation reservation = new Reservation();
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Reservation where id_reservation=?");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setReservationAttributes(res, reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    /**
     * Insertion d'une réservation dans la BDD
     *
     * @param reservation la réservation
     * @throws SQLException renvoie une exception
     */
    public static void registrerReservationWithId(Reservation reservation) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Reservation (date_reservation, debut_intervalle," +
                " fin_intervalle, nb_prolongement, isSupplement, id_estAssocie, id_borne, id_reservation) VALUES (?,?,?,?,?,?,?,?)");
        ReservationDAO.getReservationAttributes(stmt, reservation);
        stmt.setInt(8, reservation.getId_reservation());
        stmt.executeUpdate();
        NotificationDAO.createNotificationReservation(reservation);
    }

    /**
     * Recherche de réservations existantes du même client une heure précédant le début de la réservation
     *
     * @param id_client   l'identifiant du client
     * @param reservation la réservation de référence
     * @return vrai s'il existe une réservation
     */
    public static boolean hasExistingReservationFromSameUser(int id_client, Reservation reservation) {
        boolean result = false;
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT count(*) FROM reservation" +
                    " INNER JOIN estassocie e ON Reservation.id_estAssocie = e.id_estassocie" +
                    " WHERE id_client = ? AND date_reservation = ? AND fin_intervalle >= ? AND fin_intervalle <= ?");
            stmt.setInt(1, id_client);
            stmt.setDate(2, reservation.getDate_reservation());
            stmt.setTime(3, Time.valueOf(reservation.getDebut_intervalle().toLocalTime().minusHours(1)));
            stmt.setTime(4, reservation.getDebut_intervalle());
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
     * Récupération de la réservation à la date courante
     *
     * @param id_client l'id du client qui a fait la réservation
     * @return la réservation correspondant
     */
    public static Reservation getReservationFromCurrentDate(int id_client) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        Reservation reservation = new Reservation();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT Reservation.* FROM Reservation" +
                    " INNER JOIN estassocie e ON Reservation.id_estAssocie = e.id_estassocie" +
                    " WHERE id_client = ? AND date_reservation = current_date AND debut_intervalle <= current_time AND current_time <= fin_intervalle AND arrivee_client IS NULL");
            stmt.setInt(1, id_client);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setReservationAttributes(res, reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    /**
     * Recherche de la réservation du même client une heure précédant le début de la réservation
     *
     * @param id_client   l'identifiant du client
     * @param reservation la réservation de référence
     * @return la réservation
     */
    public static Reservation getExistingReservationFromSameUser(int id_client, Reservation reservation) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        Reservation existing_reservation = new Reservation();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT reservation.* FROM reservation" +
                    " INNER JOIN estassocie e ON Reservation.id_estAssocie = e.id_estassocie" +
                    " WHERE id_client = ? AND date_reservation = ? AND fin_intervalle >= ? AND fin_intervalle <= ?");
            stmt.setInt(1, id_client);
            stmt.setDate(2, reservation.getDate_reservation());
            stmt.setTime(3, Time.valueOf(reservation.getDebut_intervalle().toLocalTime().minusHours(1)));
            stmt.setTime(4, reservation.getDebut_intervalle());
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                setReservationAttributes(res, existing_reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing_reservation;
    }

    /**
     * Recherche de réservations existantes pour une date donnée, un intervalle donné et une borne donnée
     *
     * @param date_reservation la date de recherche
     * @param debut_intervalle le début de l'intervalle de recherche
     * @param fin_intervalle   la fin de l'intervalle de recherche
     * @return vrai s'il existe une réservation
     */
    public static boolean hasExistingReservation(Date date_reservation, Time debut_intervalle, Time fin_intervalle, int id_borne) {
        boolean result = false;
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT count(*) FROM reservation" +
                    " WHERE date_reservation = ? AND debut_intervalle >= ? AND debut_intervalle <= ? AND id_borne = ?");
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
     * Mise à jour d'une réservation après une fusion
     *
     * @param reservation    la réservation à modifier
     * @param fin_intervalle la nouvelle fin de la réservation
     * @throws SQLException renvoie une exception
     */
    public static void updateMergeReservation(Reservation reservation, Time fin_intervalle) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Reservation SET fin_intervalle = ? WHERE id_reservation = ?;");
        stmt.setTime(1, fin_intervalle);
        stmt.setInt(2, reservation.getId_reservation());
        stmt.executeUpdate();
    }

    /**
     * Mise à jour du prix de la réservation
     *
     * @param reservation la réservation à modifier
     * @param newPrix     la nouveau prix de la réservation
     * @throws SQLException renvoie une exception
     */
    public static void updatePriceReservation(Reservation reservation, float newPrix) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Reservation SET prix = ? WHERE id_reservation = ?;");
        stmt.setFloat(1, newPrix);
        stmt.setInt(2, reservation.getId_reservation());
        stmt.executeUpdate();
    }

    /**
     * Mise à jour d'une réservation après l'arrivée d'un client
     *
     * @param reservation la réservation à modifier
     * @throws SQLException renvoie une exception
     */
    public static void updateArriveeReservation(Reservation reservation) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Reservation SET arrivee_client = current_time WHERE id_reservation = ?;");
        stmt.setInt(1, reservation.getId_reservation());
        stmt.executeUpdate();
    }

    /**
     * Mise à jour d'une réservation après le départ d'un client
     *
     * @param reservation la réservation à modifier
     * @throws SQLException renvoie une exception
     */
    public static void updateDepartReservation(Reservation reservation) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("UPDATE Reservation SET depart_client = current_time WHERE id_reservation = ?;");
        stmt.setInt(1, reservation.getId_reservation());
        stmt.executeUpdate();
    }

    /**
     * Suppression d'une réservation dans la BDD
     *
     * @param id_reservation l'id de la réservation à supprimer
     * @throws SQLException renvoie une exception
     */
    public static void deleteOldReservation(int id_reservation) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Reservation WHERE id_reservation = ?");
        stmt.setInt(1, id_reservation);
        stmt.executeUpdate();
    }

    public static void updateReservation(Reservation reservation) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("update reservation set nb_prolongement=nb_prolongement+1, fin_intervalle=? where id_reservation=?");
            stmt.setTime(1, reservation.getFin_intervalle());
            stmt.setInt(2, reservation.getId_reservation());
            stmt.executeUpdate();
            NotificationDAO.createNotificationReservation(reservation);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static ArrayList<Reservation> getReservationByClientId(int id) {
        ArrayList<Reservation> reservations = new ArrayList<>();
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Reservation INNER JOIN EstAssocie EA on Reservation.id_estAssocie = EA.id_estAssocie where EA.id_client = ?;");
            stmt.setInt(1, id);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                Reservation temp = new Reservation();
                setReservationAttributes(res, temp);
                reservations.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public static int registrerReservation(Reservation reservation) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Reservation (date_reservation, debut_intervalle," +
                " fin_intervalle, nb_prolongement, isSupplement, id_estAssocie, id_borne) VALUES (?,?,?,?,?,?,?)");
        ReservationDAO.getReservationAttributes(stmt, reservation);
        NotificationDAO.createNotificationReservation(reservation);
        stmt.executeUpdate();
        PreparedStatement stmt2= conn.prepareStatement("SELECT * FROM Reservation where id_reservation = (SELECT Max(id_reservation) FROM Reservation);");
        ResultSet res = stmt2.executeQuery();
        res.next();
        return res.getInt(1);

    }


    private static void setReservationAttributes(ResultSet res, Reservation reservation) throws SQLException {
        reservation.setId_reservation(res.getInt(1));
        reservation.setDate_reservation(res.getDate(2));
        reservation.setDebut_intervalle(res.getTime(3));
        reservation.setFin_intervalle(res.getTime(4));
        reservation.setNb_prolongement(res.getInt(5));
        reservation.setSupplement(res.getBoolean(6));
        reservation.setId_estAssocie(res.getInt(7));
        reservation.setId_borne(res.getInt(8));
        reservation.setArrivee_client(res.getTime(9));
        reservation.setDepart_client(res.getTime(10));
        reservation.setPrix(res.getFloat(11));
    }

    /**
     * Affectation des données dans la requête
     *
     * @param stmt        la requête
     * @param reservation la réservation
     * @throws SQLException renvoie une exception
     */
    private static void getReservationAttributes(PreparedStatement stmt, Reservation reservation) throws SQLException {
        stmt.setDate(1, reservation.getDate_reservation());
        stmt.setTime(2, reservation.getDebut_intervalle());
        stmt.setTime(3, reservation.getFin_intervalle());
        stmt.setInt(4, reservation.getNb_prolongement());
        stmt.setBoolean(5, reservation.isSupplement());
        stmt.setInt(6, reservation.getId_estAssocie());
        stmt.setInt(7, reservation.getId_borne());
    }
}
