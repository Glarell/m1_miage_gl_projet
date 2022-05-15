package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.BorneDAO;
import gl.database.dao.EstAssocieDAO;
import gl.database.dao.PlaqueDAO;
import gl.database.dao.ReservationDAO;
import gl.database.model.EstAssocie;
import gl.database.model.Reservation;


import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class VerifDispoChoice extends ChoicesAbstract {

    private final Logger logger = Logger.getLogger(VerifDispoChoice.class.getName());

    private String dateString;
    private Date date;
    private Time int1;
    private Time int2;

    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Disponibilité des bornes------]");
        getDate();
        getInt1();
        getInt2();
        if (int1.compareTo(int2) >= 0) {
            System.out.println("Les intervalles ne sont pas bien renseignés !");
            return Application.RETURN_FAILED;
        }
        ArrayList<Integer> bornes_dispos = (ArrayList<Integer>) BorneDAO.getAllBorneFromDateDispo(date, int1, int2);
        if (bornes_dispos.size() > 0) {
            System.out.println("Voici la ou les borne(s) disponible(s) :");
            bornes_dispos.forEach(x -> System.out.printf("-- Borne N°%d%n", x));
            reserver(bornes_dispos, date, int1, int2);
        } else {
            System.out.println("Aucune borne n'est disponible, essayez sur d'autres créneaux :)");
        }
        return Application.RETURN_SUCCESS;
    }

    public void getDate() {
        this.dateString = Application.askForLine("Saisir la date (dd-mm-yyyy) : ");
        boolean condition = true;
        while (condition) {
            if (dateString.matches("\\d{2}-\\d{2}-\\d{4}")) {
                try {
                    this.date = new Date(new SimpleDateFormat("dd-MM-yyyy").parse(dateString).getTime());
                    condition = false;
                } catch (ParseException e) {
                    System.out.println("La date n'est pas valide");
                    dateString = Application.askForLine("Saisir la date (dd-mm-yyyy) : ");
                }
            } else {
                System.out.println("La date saisie ne respecte pas le format dd-mm-yyyy");
                dateString = Application.askForLine("Saisir la date (dd-mm-yyyy) : ");
            }
        }
    }

    public void getInt1() {
        String int1 = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
        boolean condition = true;
        while (condition) {
            if (int1.matches("\\d{2}:\\d{2}")) {
                try {
                    this.int1 = new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateString + " " + int1).getTime());
                    condition = false;
                } catch (ParseException e) {
                    System.out.println("L'intervalle n'est pas valide");
                    int1 = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
                }
            } else {
                System.out.println("L'intervalle saisi ne respecte pas le format hh:mm");
                int1 = Application.askForLine("Saisir l'intervalle de début (hh:mm) : ");
            }
        }
    }

    public void getInt2() {
        String int2 = Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ");
        boolean condition = true;
        while (condition) {
            if (int2.matches("\\d{2}:\\d{2}")) {
                try {
                    this.int2 = new Time(new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateString + " " + int2).getTime());
                    condition = false;
                } catch (ParseException e) {
                    System.out.println("L'intervalle n'est pas valide");
                    int2 = Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ");
                }
            } else {
                System.out.println("L'intervalle saisi ne respecte pas le format hh:mm");
                int2 = Application.askForLine("Saisir l'intervalle de fin (hh:mm) : ");
            }
        }
    }

    /**
     * @return vrai si l'utilisateur souhaite réserver avec une voiture louée ou empruntée
     */
    public boolean isVoitureEmprunteOuLoue() {
        String isEmprunte = Application.askForLine("Réservation avec voiture louée ou empruntée (oui/non) : ");
        while (!isEmprunte.matches("^(?:oui|non)$")) {
            isEmprunte = Application.askForLine("Mauvaise saisie !\nRéservation avec voiture louée ou empruntée (oui/non) :");
        }
        return isEmprunte.equals("oui");
    }

    /**
     * Génération d'une nouvelle plaque et création de l'association temporaire
     *
     * @return l'id de la nouvelle association temporaire
     */
    public int generateNewEstAssocieTemporaire() {
        int new_id_estAssocie = -1;
        String plaque_id = Application.askForLine("Saisir numéro d'immatriculation : ");
        while (!plaque_id.matches("[A-Z]{2}-\\d{3}-[A-Z]{2}")) {
            plaque_id = Application.askForLine("Mauvaise saisie !\nSaisir numéro d'immatriculation :");
        }

        //Ajout de la nouvelle plaque dans la BDD et de l'association temporaire
        try {
            PlaqueDAO.insertNewPlaque(plaque_id);
            EstAssocie estAssocie = new EstAssocie(Application.currentClient.getId_client(), plaque_id);
            EstAssocieDAO.insertNewEstAssocieTemporaire(estAssocie);
            new_id_estAssocie = EstAssocieDAO.getEstAssocieTemporaire(Application.currentClient.getId_client(), plaque_id).getId_estAssocie();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return new_id_estAssocie;
    }

    /**
     * Génération de la réservation du client
     */
    public void generateNewReservation(Date date, Time int1, Time int2, int id_estAssocie, int borne) {
        Reservation reserv = new Reservation(date, int1, int2, 0, false, id_estAssocie, borne);
        try {
            ReservationDAO.registrerReservation(reserv);
            System.out.println("Ajout réussi de la réservation");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur d'insertion de la réservation");
        }
    }

    public int reserver(ArrayList<Integer> bornes, Date date, Time int1, Time int2) {
        AtomicBoolean condition = new AtomicBoolean(true);
        while (condition.get()) {
            String choice = Application.askForLine("Saissisez STOP pour ne pas réserver, sinon saissisez le nom de la borne à réserver !");
            if (choice.equals("STOP")) {
                condition.set(false);
            } else {
                try {
                    int choix = Integer.parseInt(choice);
                    for (Integer borne : bornes) {
                        if (borne == choix) {
                            condition.set(false);
                            //Réservation avec une voiture empruntée ou louée
                            if (this.isVoitureEmprunteOuLoue()) {
                                this.generateNewReservation(date, int1, int2, generateNewEstAssocieTemporaire(), choix);
                            } else {
                                ArrayList<EstAssocie> estAssocies = (ArrayList<EstAssocie>) EstAssocieDAO.getEstAssocieByClient(Application.currentClient.getId_client());
                                if (estAssocies.size() == 0) {
                                    return Application.RETURN_SUCCESS;
                                } else {
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
                                    this.generateNewReservation(date, int1, int2, map.get(plaque_choice).getId_estAssocie(), choix);
                                    return Application.RETURN_SUCCESS;
                                }
                            }
                        }
                    }
                    System.out.println("Aucune borne associé à ce numéro");
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez saisir un entier correspondant à une borne ou STOP.");
                }
            }
        }
        return Application.RETURN_SUCCESS;
    }
}
