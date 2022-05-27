package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.ReservationDAO;
import gl.database.model.Reservation;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ProlongerReservationChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Prolonger ma réservation------]");
        ArrayList<Reservation> list = ReservationDAO.getAllReservationByIdClient(Application.getCurrentClientId());
        System.out.println(list.stream().map(Reservation::toString).reduce("\n", String::concat));
        if (list.size() == 0) {
            System.out.println("Aucune réservation...");
            return Application.RETURN_FAILED;
        }
        int id = Application.askForIntegerLine("Entrer l'identifiant de la réservation :");
        ArrayList<Integer> listOfId = (ArrayList<Integer>) list.stream().map(Reservation::getId_reservation).collect(Collectors.toList());
        while (!listOfId.contains(id)) {
            id = Application.askForIntegerLine("Entrer l'identifiant de la réservation :");
        }
        Reservation reservation = ReservationDAO.getReservationById(id);
        System.out.println(reservation);
        boolean verify=true;
        Time tmp = reservation.getFin_intervalle();
        while (verify) {
            String duree = "";
            while (!duree.matches("\\d{2}:\\d{2}")) {
                duree = Application.askForLine("Saisir durée supplémentaire (hh:mm) : ");
            }
            try {
                reservation.setFin_intervalle(new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse("01-01-1971 "+ duree).getTime()));
                if (tmp.toLocalTime().compareTo(reservation.getFin_intervalle().toLocalTime()) < 0) {
                    verify=false;
                } else {
                    System.out.println("L'intervalle n'est pas valide");
                }
            } catch (ParseException e) {
                System.out.println("L'intervalle n'est pas valide");
            }
        }
        System.out.println(reservation);
        ReservationDAO.updateReservation(reservation);
        return Application.RETURN_SUCCESS;
    }
}

