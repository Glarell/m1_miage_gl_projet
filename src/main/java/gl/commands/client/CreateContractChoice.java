package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.AbonnementDAO;
import gl.database.dao.BorneDAO;
import gl.database.model.Abonnement;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class CreateContractChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Conclure un contrat mensuel------]");
        Abonnement abonnement = this.initAbonnementValue();

        if (abonnement.getDebut_intervalle().compareTo(abonnement.getFin_intervalle()) >= 0) {
            System.out.println("Les intervalles ne sont pas bien renseignés !");
            return Application.RETURN_FAILED;
        }

        //Récupération des bornes disponibles pour les données saisies par l'utilisateur
        List<Integer> bornes_dispos = BorneDAO.getAllBorneFromDateDispo(abonnement.getDate_abonnement(),
                abonnement.getDebut_intervalle(), abonnement.getFin_intervalle());

        if (!bornes_dispos.isEmpty()) {
            abonnement.setId_borne(this.getBorne(bornes_dispos));
            try {
                AbonnementDAO.registrerAbonnement(abonnement);
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
     * Sélection de la borne ou l'utilisateur souhaite réserver
     *
     * @param bornes_dispos la liste des bornes disponibles pour ce créneau
     * @return la borne sélectionnée
     */
    private int getBorne(List<Integer> bornes_dispos) {
        bornes_dispos.forEach(x -> System.out.printf("-- Borne N°%d%n", x));
        int newValeur = Application.askForIntegerLine("Saisir le numéro de la borne :");
        while (!bornes_dispos.contains(newValeur)) {
            newValeur = Application.askForIntegerLine("Mauvaise saisie !\nSaisir le numéro de la borne :");
        }

        return newValeur;
    }

    /**
     * Initialisation des valeurs de base d'un nouvel abonnement de l'utilisateur
     *
     * @return le nouvel abonnement
     */
    private Abonnement initAbonnementValue() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Abonnement abonnement = new Abonnement();

        abonnement.setDate_abonnement(getUserDataUtils.getDate());
        abonnement.setDebut_intervalle(getUserDataUtils.getDebutIntervalle(formatter.format(abonnement.getDate_abonnement())));
        abonnement.setFin_intervalle(getUserDataUtils.getFinIntervalle(formatter.format(abonnement.getDate_abonnement())));
        abonnement.setId_client(Application.currentClient.getId_client());
        return abonnement;
    }
}
