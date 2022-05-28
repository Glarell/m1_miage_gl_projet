package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.BorneDAO;
import gl.database.dao.EstAssocieDAO;
import gl.database.dao.ReservationDAO;
import gl.database.model.Borne;
import gl.database.model.EstAssocie;
import gl.database.model.Reservation;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class WithoutReservationChoice extends ChoicesAbstract {

    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("--- [ Recharger sans réserver ] ---");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(System.currentTimeMillis());
        Time endTime = GetUserDataUtils.getFinIntervalle(formatter.format(date));
        Time startTime = new Time(date.getTime());
        List<Borne> bornes_dispos = BorneDAO.getAllBorneAtInstant(date, startTime, endTime);
        if (bornes_dispos.size() == 0) {
            System.out.println("Aucunes bornes n'est disponible actuellement, veuillez réessayez plus tard !");
        } else {
            Borne final_borne = bornes_dispos.get(0);
            System.out.println("La borne " + final_borne + " est disponible !");
            String plaque_choice = Application.askForLine("Veuillez saisir la plaque d'immatriculation de votre véhicule");
            while (!plaque_choice.matches("[A-Z]{2}-\\d{3}-[A-Z]{2}")) {
                plaque_choice = Application.askForLine("Mauvaise saisie !\nSaisir numéro d'immatriculation :");
            }
            List<EstAssocie> assocations = EstAssocieDAO.getEstAssocieByClientAndPlaque(Application.getCurrentClientId(), plaque_choice);
            if (assocations.size() == 0){
                System.out.println("Aucunes plaques pour ce client et ce numéro de plaque, association temporaire non permise sans réservation, veuillez revenir plus tard");
            }else{
                EstAssocie estAssocie = assocations.get(0);
                Reservation reservation = new Reservation(date, startTime, endTime, 0, false, estAssocie.getId_estAssocie(), final_borne.getId_borne());
                try{
                    int id_reserv = ReservationDAO.registrerReservation(reservation);
                    reservation.setId_reservation(id_reserv);
                    ReservationDAO.updateArriveeReservation(reservation);
                    BorneDAO.updateStateOfBorne(final_borne, "occupée");
                    System.out.println("Vous pouvez dorénavant recharger, veillez à signaler votre départ pour cette réservation");
                }catch (SQLException e){
                    System.out.println("La création de votre réservation n'as pas pu aboutir");
                }
            }
        }
        return Application.RETURN_SUCCESS;
    }
}
