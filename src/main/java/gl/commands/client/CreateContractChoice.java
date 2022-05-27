package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.AbonnementDAO;
import gl.database.dao.BorneDAO;
import gl.database.dao.ClientDAO;
import gl.database.dao.TransactionDAO;
import gl.database.model.Abonnement;
import gl.database.model.Transaction;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class CreateContractChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Conclure un contrat mensuel------]");
        //Vérification de la limite d'abonnements / réservations
        if (ClientDAO.getNbReservationAndAbonnement(Application.getCurrentClientId()) >= 3) {
            System.out.println("Vous avez atteint le nombre maximum de réservations et d'abonnements");
            return Application.RETURN_FAILED;
        }

        Abonnement abonnement = CreateContractChoice.initAbonnementValue();

        if (CreateContractChoice.isIntervalInvalid(abonnement)) {
            System.out.println("Les intervalles ne sont pas bien renseignés !");
            return Application.RETURN_FAILED;
        }

        //Recherche d'un abonnement existant
        if (AbonnementDAO.hasExistingAbonnementFromSameUser(Application.getCurrentClientId(), abonnement)) {
            try {
                return CreateContractChoice.doesUserWantToMergeAbonnement(abonnement) ? Application.RETURN_SUCCESS : Application.RETURN_FAILED;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //Récupération des bornes disponibles pour les données saisies par l'utilisateur
        List<Integer> bornes_dispos = CreateContractChoice.getAllValidBorne(abonnement);

        if (!bornes_dispos.isEmpty()) {
            abonnement.setId_borne(CreateContractChoice.getBorne(bornes_dispos));
            try {
                AbonnementDAO.registrerAbonnement(abonnement);
                //Sauvegarde de la transaction de l'utilisateur
                TransactionDAO.registrerTransaction(new Transaction("Création d'un abonnement mensuel le " +
                        new SimpleDateFormat("MM-yyyy").format(abonnement.getDate_abonnement()),
                        Application.currentClient.getId_client()));
            } catch (SQLException e) {
                System.out.println("Erreur : champs incompatables selon saisies !");
                this.execute(scanner, user);
            }
        } else {
            System.out.println("Aucune borne n'est disponible, essayez sur d'autres créneaux :)");
            return Application.RETURN_FAILED;
        }
        return Application.RETURN_SUCCESS;
    }

    /**
     * Demande à l'utilisateur s'il souhaite fusionner ses abonnements
     *
     * @param newAbonnement le nouvel abonnement de l'utilisateur
     * @return vrai si l'utilisateur souhaite fusionner 2 abonnements
     * @throws SQLException renvoie une exception
     */
    public static boolean doesUserWantToMergeAbonnement(Abonnement newAbonnement) throws SQLException {
        System.out.println("Il existe un abonnement à votre nom une heure précédant l'abonnement que vous souhaitez effectuer");

        //Récupération du choix de l'utilisateur
        String doingMerge = Application.askForLine("Souhaitez vous fusionner vos abonnements (oui/non) : ");
        while (!doingMerge.matches("^(?:oui|non)$")) {
            doingMerge = Application.askForLine("Mauvaise saisie !\nSouhaitez vous fusionner vos abonnements (oui/non) : ");
        }
        boolean userDecision = doingMerge.equals("oui");

        if (userDecision) {
            Abonnement oldReservation = AbonnementDAO.getExistingAbonnementFromSameUser(Application.currentClient.getId_client(), newAbonnement);
            if (!(AbonnementDAO.hasExistingAbonnement(newAbonnement.getDate_abonnement(), oldReservation.getFin_intervalle(),
                    newAbonnement.getFin_intervalle(), oldReservation.getId_borne()))) {
                AbonnementDAO.updateMergeAbonnement(oldReservation, newAbonnement.getFin_intervalle());
                //Sauvegarde de la transaction de l'utilisateur
                TransactionDAO.registrerTransaction(new Transaction("Modification de l'abonnement mensuel du " +
                        new SimpleDateFormat("MM-yyyy").format(oldReservation.getDate_abonnement()),
                        Application.currentClient.getId_client()));
            } else {
                System.out.println("La borne ou vous avez réservé est occupé pendant l'intervalle " + oldReservation.getFin_intervalle() + " - " + newAbonnement.getFin_intervalle());
                userDecision = false;
            }
        }
        return userDecision;
    }

    /**
     * Sélection de la borne ou l'utilisateur souhaite réserver
     *
     * @param bornes_dispos la liste des bornes disponibles pour ce créneau
     * @return la borne sélectionnée
     */
    private static int getBorne(List<Integer> bornes_dispos) {
        bornes_dispos.forEach(x -> System.out.printf("-- Borne N°%d%n", x));
        int newValeur = Application.askForIntegerLine("Saisir le numéro de la borne :");
        while (!bornes_dispos.contains(newValeur)) {
            newValeur = Application.askForIntegerLine("Mauvaise saisie !\nSaisir le numéro de la borne :");
        }

        return newValeur;
    }

    /**
     * Vérifie si l'intervalle saisit par l'utilisateur est valide
     *
     * @param abonnement l'abonnement avec les données saisies par l'utilisateur
     * @return vrai si l'intervalle est invalide
     */
    public static boolean isIntervalInvalid(Abonnement abonnement) {
        return abonnement.getDebut_intervalle().compareTo(abonnement.getFin_intervalle()) >= 0;
    }

    /**
     * Récupération des bornes disponibles
     *
     * @param abonnement l'abonnement avec les données saisies par l'utilisateur
     * @return la liste des bornes valides
     */
    public static List<Integer> getAllValidBorne(Abonnement abonnement) {
        return BorneDAO.getAllBorneFromDateDispo(abonnement.getDate_abonnement(),
                abonnement.getDebut_intervalle(), abonnement.getFin_intervalle());
    }

    /**
     * Initialisation des valeurs de base d'un nouvel abonnement de l'utilisateur
     *
     * @return le nouvel abonnement
     */
    public static Abonnement initAbonnementValue() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Abonnement abonnement = new Abonnement();

        abonnement.setDate_abonnement(GetUserDataUtils.getDate());
        abonnement.setDebut_intervalle(GetUserDataUtils.getDebutIntervalle(formatter.format(abonnement.getDate_abonnement())));
        abonnement.setFin_intervalle(GetUserDataUtils.getFinIntervalle(formatter.format(abonnement.getDate_abonnement())));
        abonnement.setId_client(Application.getCurrentClientId());
        return abonnement;
    }
}
