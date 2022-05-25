package gl.database.dao;

import gl.database.ConnectionPostgre;
import gl.database.model.Notification;
import gl.database.model.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    /**
     * Récupération de toutes les notifications présentes dans la BDD
     *
     * @return la liste des notifications
     */
    public static List<Notification> getAllNotification() {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<Notification> listOfNotification = new ArrayList<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM Notification");
            while (res.next()) {
                Notification notification = new Notification();
                setNotificationAttributes(res, notification);
                listOfNotification.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfNotification;
    }

    /**
     * Récupération de toutes les notifications pour un client donné dans la BDD
     *
     * @param id_client l'id du client recherché
     * @return la liste des notifications
     */
    public static List<Notification> getNotificationByClient(int id_client) {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        List<Notification> listOfNotification = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Notification WHERE id_client = ?");
            stmt.setInt(1, id_client);
            ResultSet res = stmt.executeQuery();
            while (res.next()) {
                Notification notification = new Notification();
                setNotificationAttributes(res, notification);
                listOfNotification.add(notification);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfNotification;
    }

    public static void createNotificationReservation(Reservation reservation) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("select e.id_client from reservation inner join estassocie e on e.id_estassocie = reservation.id_estassocie\n" +
                "        where id_reservation=?");
        stmt.setInt(1,reservation.getId_reservation());
        ResultSet res = stmt.executeQuery();
        //recuperer id
        while (res.next()) {
            int id = res.getInt(1);
            Notification notification = new Notification(String.format("[Notification-Réservation]\nL'identifiant de votre réservation est le suivant : %s%n",reservation.getId_reservation()),id,"Mail");
            NotificationDAO.insertNewNotification(notification);
            notification.setId_typeNotification("SMS");
            NotificationDAO.insertNewNotification(notification);
            System.out.println(notification);
        }

    }

    /**
     * Insertion d'une notification dans la BDD
     *
     * @param notification la notification
     * @throws SQLException renvoie une exception
     */
    public static void insertNewNotification(Notification notification) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Notification (contenu, id_client, id_typeNotification) VALUES (?,?,?)");
        getNotificationAttributes(stmt, notification);
        stmt.executeUpdate();
    }

    /**
     * Insertion d'une notification avec un id dans la BDD
     *
     * @param notification la notification
     * @throws SQLException renvoie une exception
     */
    public static void insertNewNotificationWithId(Notification notification) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Notification (contenu, id_client, id_typeNotification, id_notification) VALUES (?,?,?,?)");
        getNotificationAttributes(stmt, notification);
        stmt.setInt(4, notification.getId_notification());
        stmt.executeUpdate();
    }

    /**
     * Suppression d'une notification dans la BDD
     *
     * @param id_notification l'id de la notification à supprimer
     * @throws SQLException renvoie une exception
     */
    public static void deleteOldNotification(int id_notification) throws SQLException {
        Connection conn = ConnectionPostgre.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Notification WHERE id_notification = ?");
        stmt.setInt(1, id_notification);
        stmt.executeUpdate();
    }

    /**
     * Association des résultats d'une requête à une notification
     *
     * @param res          la requête contenant les données
     * @param notification la notification
     * @throws SQLException renvoie une exception
     */
    private static void setNotificationAttributes(ResultSet res, Notification notification) throws SQLException {
        notification.setId_notification(res.getInt(1));
        notification.setContenu(res.getString(2));
        notification.setId_client(res.getInt(3));
        notification.setId_typeNotification(res.getString(4));
    }

    /**
     * Affectation des données dans la requête
     *
     * @param stmt         la requête
     * @param notification la notification
     * @throws SQLException renvoie une exception
     */
    private static void getNotificationAttributes(PreparedStatement stmt, Notification notification) throws SQLException {
        stmt.setString(1, notification.getContenu());
        stmt.setInt(2, notification.getId_client());
        stmt.setString(3, notification.getId_typeNotification());
    }
}
