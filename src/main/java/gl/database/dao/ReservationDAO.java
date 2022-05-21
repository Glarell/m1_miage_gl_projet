package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.Reservation;

import java.sql.*;
import java.util.ArrayList;

public class ReservationDAO {


    public static ArrayList<Reservation> getAllReservationByIdClient(int id_client) {
        ArrayList<Reservation> list = new ArrayList<>();
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        Reservation reservation = new Reservation();
        try {
            PreparedStatement stmt = conn.prepareStatement("select id_reservation,date_reservation,debut_intervalle,fin_intervalle,nb_prolongement,issupplement,e.id_estassocie,id_borne from reservation" +
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

    public static void updateReservation(Reservation reservation) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("update reservation set nb_prolongement=nb_prolongement+1, fin_intervalle=? where id_reservation=?");
            stmt.setTime(1, reservation.getFin_intervalle());
            stmt.setInt(2, reservation.getId_reservation());
            stmt.executeUpdate();
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

    public static Reservation registrerReservation(Reservation reservation) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Reservation (date_reservation,debut_intervalle,fin_intervalle,nb_prolongement,isSupplement,id_estAssocie,id_borne) VALUES (?,?,?,?,?,?,?)");
        stmt.setDate(1, reservation.getDate_reservation());
        stmt.setTime(2, reservation.getDebut_intervalle());
        stmt.setTime(3, reservation.getFin_intervalle());
        stmt.setInt(4, reservation.getNb_prolongement());
        stmt.setBoolean(5, reservation.isSupplement());
        stmt.setInt(6, reservation.getId_estAssocie());
        stmt.setInt(7, reservation.getId_borne());
        stmt.executeUpdate();
        return reservation;
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
    }
}
