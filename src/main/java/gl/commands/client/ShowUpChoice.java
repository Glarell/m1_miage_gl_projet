package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.AbonnementDAO;
import gl.database.dao.BorneDAO;
import gl.database.dao.ReservationDAO;
import gl.database.model.Abonnement;
import gl.database.model.Borne;
import gl.database.model.EtatBorne;
import gl.database.model.Reservation;

import java.sql.SQLException;
import java.util.Scanner;

public class ShowUpChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Se présenter à sa réservation------]");
        int res = Application.RETURN_SUCCESS;
        if (ReservationDAO.getReservationFromCurrentDate(Application.getCurrentClientId()).getId_reservation() != -1) {
            res = manageExistingReservation(res);
        } else if (AbonnementDAO.getAbonnementFromCurrentDate(Application.getCurrentClientId()).getId_abonnement() != -1) {
            res = manageExistingAbonnement(res);
        } else {
            System.out.println("Vous n'avez pas de réservation ou d'abonnement actif actuellement !");
            System.out.println("Veuillez utiliser la fonctionnalité 'Se présenter sans réservation'");
            res = Application.RETURN_FAILED;
        }
        return res;
    }

    /**
     * Gestion du cas ou l'utilisateur possède une réservation
     *
     * @param res le retour de l'application
     * @return le retour de l'application
     */
    private int manageExistingReservation(int res) {
        Reservation reservation = ReservationDAO.getReservationFromCurrentDate(Application.getCurrentClientId());
        if (isBorneDisponible(reservation.getId_borne())) {
            try {
                ReservationDAO.updateArriveeReservation(reservation);
                BorneDAO.updateStateOfBorne(new Borne(reservation.getId_borne()), EtatBorne.STATE_BUSY);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Vous êtes en train de recharger votre véhicule");
        } else {
            System.out.println("La borne " + reservation.getId_borne() + " est actuellement occupé");
            System.out.println("Veuillez utiliser la fonctionnalité 'Se présenter sans réservation'");
            res = Application.RETURN_FAILED;
        }
        return res;
    }

    /**
     * Gestion du cas ou l'utilisateur possède un abonnement
     *
     * @param res le retour de l'application
     * @return le retour de l'application
     */
    private int manageExistingAbonnement(int res) {
        Abonnement abonnement = AbonnementDAO.getAbonnementFromCurrentDate(Application.getCurrentClientId());
        if (isBorneDisponible(abonnement.getId_borne())) {
            try {
                BorneDAO.updateStateOfBorne(new Borne(abonnement.getId_borne()), EtatBorne.STATE_BUSY);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Vous êtes en train de recharger votre véhicule");
        } else {
            System.out.println("La borne " + abonnement.getId_borne() + " est actuellement occupé");
            System.out.println("Veuillez utiliser la fonctionnalité 'Se présenter sans réservation'");
            res = Application.RETURN_FAILED;
        }
        return res;
    }

    /**
     * Vérifie si la borne est utilisable
     *
     * @param id_borne l'identifiant de la borne
     * @return vrai si la borne n'est pas occupée par un autre utilisateur
     */
    private boolean isBorneDisponible(int id_borne) {
        return BorneDAO.isBorneWithEtatBorne(id_borne, EtatBorne.STATE_AVAILABLE)
                || BorneDAO.isBorneWithEtatBorne(id_borne, EtatBorne.STATE_RESERVED);
    }
}
