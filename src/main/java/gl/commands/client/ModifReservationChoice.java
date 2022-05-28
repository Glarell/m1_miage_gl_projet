package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.BorneDAO;
import gl.database.dao.EstAssocieDAO;
import gl.database.dao.ReservationDAO;
import gl.database.model.EstAssocie;
import gl.database.model.Reservation;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModifReservationChoice extends ChoicesAbstract {

    @Override
    public int execute(Scanner scanner, User user) {
        List<Reservation> reservationList = ReservationDAO.getReservationByClientId(Application.getCurrentClientId());
        HashMap<Integer, Reservation> indices = new HashMap<>();
        reservationList.forEach(x -> {
            indices.put(x.getId_reservation(), x);
            System.out.println(x.getId_reservation() + " : Date = " + x.getDate_reservation() + " Debut = " + x.getDebut_intervalle() + " Fin = " + x.getFin_intervalle());
        });
        if (indices.size() > 0) {
            int choice = Application.askForIntegerLine("Veuiller saisir le numéro de la réservation que vous souhaitez modifier : ");
            while (indices.getOrDefault(choice, null) == null) {
                choice = Application.askForIntegerLine("Aucune réservation avec ce numéro, veuillez ré-essayez");
            }
            System.out.println("Que voulez-vous faire avec cette réservation ?");
            System.out.println("1 - Supprimer cette réservation");
            System.out.println("2 - Modifier la date, le début et la fin de la réservation");
            System.out.println("3 - Modifier la plaque associée à la réservation");
            System.out.println("4 - Quitter ce menu");
            int menu_choice = Application.askForIntegerLine("Veuillez saisir le numéro du choix que vous souhaitez effectuer : ");
            while (Map.of(1, 1, 2, 2, 3, 3, 4, 4).getOrDefault(menu_choice, null) == null) {
                menu_choice = Application.askForIntegerLine("Ce choix n'existe pas, veuillez ré-essayez :");
            }
            switch (menu_choice) {
                case 1:
                    ReservationDAO.deleteReservationById(choice);
                    break;
                case 2:
                    modifDateReservation(choice);
                    break;
                case 3:
                    EstAssocie res = modifPlaqueReservation(choice);
                    ReservationDAO.deleteReservationById(choice);
                    Reservation temp = indices.get(choice);
                    temp.setId_estAssocie(res.getId_estAssocie());
                    try {
                        ReservationDAO.registrerReservation(temp);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    return Application.RETURN_SUCCESS;
            }
        }
        return Application.RETURN_SUCCESS;
    }

    public void modifDateReservation(int id) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = GetUserDataUtils.getDate();
        Time int1 = GetUserDataUtils.getDebutIntervalle(formatter.format(date));
        Time int2 = GetUserDataUtils.getFinIntervalle(formatter.format(date));
        while (int1.compareTo(int2) >= 0) {
            System.out.println("Les intervalles ne sont pas correctement renseignés");
            int1 = GetUserDataUtils.getDebutIntervalle(formatter.format(date));
            int2 = GetUserDataUtils.getFinIntervalle(formatter.format(date));
        }
        ArrayList<Integer> bornes_dispos = (ArrayList<Integer>) BorneDAO.getAllBorneFromDateDispoUpdate(date, int1, int2, id);
        if (bornes_dispos.size() > 0) {
            System.out.println("Voici la ou les borne(s) disponible(s) pour le nouveau créneau :");
            bornes_dispos.forEach(x -> System.out.printf("-- Borne N°%d%n", x));
            AtomicBoolean condition = new AtomicBoolean(true);
            while (condition.get()) {
                String choice = Application.askForLine("Saissisez le nom de la borne à réserver !");
                try {
                    int choix = Integer.parseInt(choice);
                    for (Integer borne : bornes_dispos) {
                        if (borne == choix) {
                            condition.set(false);
                            ReservationDAO.updateReservationDate(id, date, int1, int2);
                        }
                    }
                    if (condition.get()) {
                        System.out.println("Aucune borne associé à ce numéro");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez saisir un entier correspondant à une borne ou STOP.");
                }
            }
        }
    }

    public EstAssocie modifPlaqueReservation(int id) {
        while (true) {
            if (VerifDispoChoice.isVoitureEmprunteOuLoue()) {
                return EstAssocieDAO.getEstAssocieById(VerifDispoChoice.generateNewEstAssocieTemporaire());
            } else {
                ArrayList<EstAssocie> estAssocies = (ArrayList<EstAssocie>) EstAssocieDAO.getEstAssocieByClient(Application.getCurrentClientId());
                if (estAssocies.size() > 0) {
                    System.out.println("Choissisez la plaque avec laquelle réserver :");
                    HashMap<String, EstAssocie> map = new HashMap<>();
                    for (EstAssocie estAssocy : estAssocies) {
                        System.out.println(estAssocy.getId_plaque());
                        map.put(estAssocy.getId_plaque(), estAssocy);
                    }
                    String plaque_choice = Application.askForLine("Saissisez le nom de la plaque");
                    while (map.getOrDefault(plaque_choice, null) == null) {
                        System.out.println("Vous n'avez pas saisi une plaque valide, veuillez recommencer");
                        plaque_choice = Application.askForLine("Saissisez le nom de la plaque");
                    }
                    return map.get(plaque_choice);
                }
            }
        }
    }
}