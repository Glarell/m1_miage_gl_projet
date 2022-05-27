package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.AbonnementDAO;
import gl.database.dao.BorneDAO;
import gl.database.dao.ReservationDAO;
import gl.database.dao.VariableApplicationDAO;
import gl.database.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

public class GoAwayChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Partir de sa réservation------]");
        int res = Application.RETURN_SUCCESS;

        boolean isReservation = isReservationInProgress();
        if (isReservation && !ReservationDAO.getAllReservationInProgressByIdClient(Application.getCurrentClientId()).isEmpty()) {
            Reservation reservation = GoAwayChoice.getReservation();
            GoAwayChoice.generateReservationCost(reservation);
        } else if (!AbonnementDAO.getAbonnementInProgressByClient(Application.getCurrentClientId()).isEmpty()) {
            Abonnement abonnement = GoAwayChoice.getAbonnement();
            GoAwayChoice.generateAbonnementCost(abonnement);
        } else {
            res = Application.RETURN_FAILED;
            System.out.println(isReservation ? "Il n'y a pas de réservation en cours" : "Il n'y a pas d'abonnement en cours");
        }

        return res;
    }

    /**
     * @return vrai si l'utilisateur part d'une réservation
     */
    public static boolean isReservationInProgress() {
        String isEmprunte = Application.askForLine("Vous voulez partir de quoi (abonnement/réservation) : ");
        while (!isEmprunte.matches("^(?:abonnement|réservation)$")) {
            isEmprunte = Application.askForLine("Mauvaise saisie !\nVous voulez partir de quoi (abonnement/réservation) : ");
        }
        return isEmprunte.equals("réservation");
    }

    /**
     * Récupère la réservation selectionnée par l'utilisateur
     *
     * @return la réservation
     */
    public static Reservation selectReservation() {
        List<Reservation> listOfReservation = ReservationDAO.getAllReservationInProgressByIdClient(Application.getCurrentClientId());
        System.out.println("Voici la ou les réservation(s) disponible(s) :");
        listOfReservation.forEach(reservation -> System.out.printf("-- reservation N°%s le %s sur l'intervalle : %s -> %s",
                reservation.getId_reservation(), reservation.getDate_reservation(), reservation.getDebut_intervalle(), reservation.getFin_intervalle()));

        int numReservation = Application.askForIntegerLine("\nSelectionnez le numéro de la réservation : ");
        while (listOfReservationDoesntContainsId(listOfReservation, numReservation)) {
            numReservation = Application.askForIntegerLine("Mauvaise saisie !\nSelectionnez le numéro de la réservation : ");
        }
        return getReservationFromList(listOfReservation, numReservation);
    }

    /**
     * Vérifie si un id est présent dans la liste des réservations en cours
     *
     * @param listOfReservation liste des réservations
     * @param numReservation    l'identifiant de la réservation recherchée
     * @return vrai s'il est présent
     */
    public static boolean listOfReservationDoesntContainsId(List<Reservation> listOfReservation, int numReservation) {
        return listOfReservation.stream()
                .noneMatch(reservation -> reservation.getId_reservation() == numReservation);
    }

    /**
     * Récupère la réservation selectionnée dans la liste des réservations en cours
     *
     * @param listOfReservation liste des réservations
     * @param numReservation    l'identifiant de la réservation recherchée
     * @return la réservation selectionnée
     */
    public static Reservation getReservationFromList(List<Reservation> listOfReservation, int numReservation) {
        return listOfReservation.stream()
                .filter(reservation -> reservation.getId_reservation() == numReservation)
                .findFirst()
                .orElse(null);
    }

    /**
     * Récupère l'abonnement selectionné par l'utilisateur
     *
     * @return l'abonnement
     */
    public static Abonnement selectAbonnement() {
        List<Abonnement> listOfAbonnement = AbonnementDAO.getAbonnementInProgressByClient(Application.getCurrentClientId());
        System.out.println("Voici le ou les abonnement(s) disponible(s) :");
        listOfAbonnement.forEach(abonnement -> System.out.printf("-- abonnement N°%s sur l'intervalle : %s -> %s",
                abonnement.getId_abonnement(), abonnement.getDebut_intervalle(), abonnement.getFin_intervalle()));

        int numAbonnement = Application.askForIntegerLine("\nSelectionnez le numéro de l'abonnement : ");
        while (listOfAbonnementDoesntContainsId(listOfAbonnement, numAbonnement)) {
            numAbonnement = Application.askForIntegerLine("Mauvaise saisie !\nSelectionnez le numéro de l'abonnement : ");
        }
        return getAbonnementFromList(listOfAbonnement, numAbonnement);
    }

    /**
     * Vérifie si un id est présent dans la liste des abonnements en cours
     *
     * @param listOfAbonnement liste des abonnements
     * @param numAbonnement    l'identifiant de l'abonnement recherché
     * @return vrai s'il est présent
     */
    public static boolean listOfAbonnementDoesntContainsId(List<Abonnement> listOfAbonnement, int numAbonnement) {
        return listOfAbonnement.stream()
                .noneMatch(abonnement -> abonnement.getId_abonnement() == numAbonnement);
    }

    /**
     * Récupère l'abonnement selectionné dans la liste des abonnements en cours
     *
     * @param listOfAbonnement liste des abonnements
     * @param numAbonnement    l'identifiant de l'abonnement recherché
     * @return l'abonnement selectionné
     */
    public static Abonnement getAbonnementFromList(List<Abonnement> listOfAbonnement, int numAbonnement) {
        return listOfAbonnement.stream()
                .filter(reservation -> reservation.getId_abonnement() == numAbonnement)
                .findFirst()
                .orElse(null);
    }

    /**
     * Récupération de la réservation selectionnée par l'utilisateur
     *
     * @return la réservation
     */
    public static Reservation getReservation() {
        Reservation reservation = GoAwayChoice.selectReservation();
        try {
            ReservationDAO.updateDepartReservation(reservation);
            BorneDAO.updateStateOfBorne(new Borne(reservation.getId_borne()), EtatBorne.STATE_AVAILABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ReservationDAO.getReservationById(reservation.getId_reservation());
    }

    /**
     * Récupération de l'abonnement selectionné par l'utilisateur
     *
     * @return l'abonnement
     */
    public static Abonnement getAbonnement() {
        Abonnement abonnement = GoAwayChoice.selectAbonnement();
        try {
            AbonnementDAO.updateInProgressAbonnement(abonnement);
            BorneDAO.updateStateOfBorne(new Borne(abonnement.getId_borne()), EtatBorne.STATE_AVAILABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return AbonnementDAO.getAbonnementById(abonnement.getId_abonnement());
    }

    public static void generateReservationCost(Reservation reservation) {
        Time date_arrivee = reservation.getArrivee_client();
        Time date_depart = reservation.getDepart_client();
        Time intervalle_debut = reservation.getDebut_intervalle();
        Time intervalle_fin = reservation.getFin_intervalle();
        long origin_duration = ((intervalle_fin.getTime() - intervalle_debut.getTime())/1000)/60;
        long real_duration = ((date_depart.getTime() - date_arrivee.getTime())/1000)/60;
        long price;
        VariableApplication delay = VariableApplicationDAO.getVariableApplicationByName("délai attente");
        VariableApplication surplus = VariableApplicationDAO.getVariableApplicationByName("frais supplémentaires réservation");
        long surplus_consumed = 0;
        if (real_duration - origin_duration > 0){
            surplus_consumed = real_duration - origin_duration;
        }
        long rest = real_duration - surplus_consumed;
        price = rest + (surplus_consumed * surplus.getValeur());
        try{
            ReservationDAO.updatePriceReservation(reservation, price);
        }catch (SQLException e){
            System.out.println("Cela n'as pas pu aboutir");
        }
    }

    public static void generateAbonnementCost(Abonnement abonnement) {
        LocalTime date_depart = abonnement.getFin_intervalle().toLocalTime();
        Time intervalle_debut = abonnement.getDebut_intervalle();
        Time intervalle_fin = abonnement.getFin_intervalle();
        long origin_duration = ((intervalle_fin.getTime() - intervalle_debut.getTime())/1000)/60;
        long real_duration = ((date_depart.getSecond() - (intervalle_debut.getTime())/1000))/60;
        long price;
        VariableApplication surplus = VariableApplicationDAO.getVariableApplicationByName("frais supplémentaires réservation");
        long surplus_consumed = 0;
        if (real_duration - origin_duration > 0){
            surplus_consumed = real_duration - origin_duration;
        }
        price = (long) abonnement.getPrix() + (surplus_consumed * surplus.getValeur());
        abonnement.setPrix(price);
        try{
            AbonnementDAO.updatePriceAbonnement(abonnement, price);
        } catch(SQLException e){
            System.out.println("Modification du prix non prise en compte");
        }
    }
}