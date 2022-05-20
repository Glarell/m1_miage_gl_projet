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

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class VerifDispoChoice extends ChoicesAbstract {

    private final Logger logger = Logger.getLogger(VerifDispoChoice.class.getName());

    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Disponibilité des bornes------]");
        Reservation reservation = this.initReservationValue();
        //Vérification de l'intervalle saisie par l'utilisateur
        if (reservation.getDebut_intervalle().compareTo(reservation.getFin_intervalle()) >= 0) {
            System.out.println("Les intervalles ne sont pas bien renseignés !");
            return Application.RETURN_FAILED;
        }

        //Récupération des bornes disponibles pour les données saisies par l'utilisateur
        ArrayList<Integer> bornes_dispos = (ArrayList<Integer>) BorneDAO.getAllBorneFromDateDispo(reservation.getDate_reservation(),
                reservation.getDebut_intervalle(), reservation.getFin_intervalle());

        //Recherche d'une réservation existante
        if (ReservationDAO.hasExistingReservationFromSameUser(Application.currentClient.getId_client(), reservation)) {
            try {
                return VerifDispoChoice.doesUserWantToMergeReservation(reservation) ? Application.RETURN_SUCCESS : Application.RETURN_FAILED;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (bornes_dispos.size() > 0) {
            System.out.println("Voici la ou les borne(s) disponible(s) :");
            bornes_dispos.forEach(x -> System.out.printf("-- Borne N°%d%n", x));
            reserver(bornes_dispos, reservation);
        } else {
            System.out.println("Aucune borne n'est disponible, essayez sur d'autres créneaux :)");
        }
        return Application.RETURN_SUCCESS;
    }

    /**
     * Initialisation des valeurs de base d'une nouvelle réservation de l'utilisateur
     *
     * @return la nouvelle réservation
     */
    private Reservation initReservationValue() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Reservation reservation = new Reservation();
        reservation.setDate_reservation(getUserDataUtils.getDate());
        reservation.setDebut_intervalle(getUserDataUtils.getDebutIntervalle(formatter.format(reservation.getDate_reservation())));
        reservation.setFin_intervalle(getUserDataUtils.getFinIntervalle(formatter.format(reservation.getDate_reservation())));
        reservation.setNb_prolongement(0);
        reservation.setSupplement(false);
        return reservation;
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
     * Demande à l'utilisateur s'il souhaite fusionner ses réservations
     *
     * @param newReservation la nouvelle réservation de l'utilisateur
     * @return vrai si l'utilisateur souhaite fusionner 2 réservations
     * @throws SQLException renvoie une exception
     */
    public static boolean doesUserWantToMergeReservation(Reservation newReservation) throws SQLException {
        System.out.println("Il existe une réservation à votre nom une heure précédant la réservation que vous souhaitez effectuer");

        //Récupération du choix de l'utilisateur
        String doingMerge = Application.askForLine("Souhaitez vous fusionner vos réservation (oui/non) : ");
        while (!doingMerge.matches("^(?:oui|non)$")) {
            doingMerge = Application.askForLine("Mauvaise saisie !\nSouhaitez vous fusionner vos réservation (oui/non) : ");
        }
        boolean userDecision = doingMerge.equals("oui");

        if (userDecision) {
            Reservation oldReservation = ReservationDAO.getExistingReservationFromSameUser(Application.currentClient.getId_client(), newReservation);
            if (!(ReservationDAO.hasExistingReservation(newReservation.getDate_reservation(), oldReservation.getFin_intervalle(),
                    newReservation.getFin_intervalle(), oldReservation.getId_borne()))) {
                ReservationDAO.updateMergeReservation(oldReservation, newReservation.getFin_intervalle());
            } else {
                System.out.println("La borne ou vous avez réservé est occupé pendant l'intervalle " + oldReservation.getFin_intervalle() + " - " + newReservation.getFin_intervalle());
                userDecision = false;
            }
        }
        return userDecision;
    }

    /**
     * Génération de la réservation du client
     */
    public void generateNewReservation(Reservation reservation) {
        try {
            ReservationDAO.registrerReservation(reservation);
            System.out.println("Ajout réussi de la réservation");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur d'insertion de la réservation");
        }
    }

    public int reserver(ArrayList<Integer> bornes, Reservation reservation) {
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
                            reservation.setId_borne(choix);
                            //Réservation avec une voiture empruntée ou louée
                            if (this.isVoitureEmprunteOuLoue()) {
                                reservation.setId_estAssocie(generateNewEstAssocieTemporaire());
                                this.generateNewReservation(reservation);
                            } else {
                                ArrayList<EstAssocie> estAssocies = (ArrayList<EstAssocie>) EstAssocieDAO.getEstAssocieByClient(Application.currentClient.getId_client());
                                if (estAssocies.size() == 0) {
                                    return Application.RETURN_SUCCESS;
                                } else {
                                    System.out.println("Choissisez la plaque avec laquelle réserver :");
                                    HashMap<String, EstAssocie> mapEstAssocieFromCurrentUser = new HashMap<>();
                                    for (EstAssocie estAssocy : estAssocies) {
                                        System.out.println(estAssocy.getId_plaque());
                                        mapEstAssocieFromCurrentUser.put(estAssocy.getId_plaque(), estAssocy);
                                    }
                                    String plaque_choice = Application.askForLine("Saissisez le nom de la plaque");
                                    while (mapEstAssocieFromCurrentUser.getOrDefault(plaque_choice, null) == null) {
                                        System.out.println("Vous n'avez pas saisi une plaque valide, veuillez recommencer");
                                        plaque_choice = Application.askForLine("Saissisez le nom de la plaque");
                                    }
                                    reservation.setId_estAssocie(mapEstAssocieFromCurrentUser.get(plaque_choice).getId_estAssocie());
                                    this.generateNewReservation(reservation);
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
