package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.ReservationDAO;
import gl.database.model.Reservation;

import java.sql.SQLException;
import java.util.Scanner;

public class GoAwayChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Partir de sa réservation------]");
        int res = Application.RETURN_SUCCESS;

        Reservation reservation = GoAwayChoice.getReservation();
        GoAwayChoice.generateReservationCost(reservation);

        return res;
    }

    /**
     * Récupération de la réservation selectionnée par l'utilisateur
     *
     * @return la réservation
     */
    public static Reservation getReservation() {
        Reservation reservation = new Reservation();
        try {
            ReservationDAO.updateArriveeReservation(reservation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ReservationDAO.getReservationById(reservation.getId_reservation());
    }

    public static void generateReservationCost(Reservation reservation) {

    }
}
