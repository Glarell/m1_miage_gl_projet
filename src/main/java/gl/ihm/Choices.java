package gl.ihm;

import gl.application.Application;
import gl.commands.ChoicesAbstract;
import gl.commands.ConnexionChoice;
import gl.commands.DeconnectionChoice;
import gl.commands.QuitChoice;
import gl.commands.anonymous.InscriptionChoice;
import gl.commands.client.*;
import gl.commands.manager.ConsultAllProfilesChoice;
import gl.commands.manager.DelayChoice;
import gl.commands.manager.PriceChoice;

import java.util.HashMap;

public class Choices {

    private final HashMap<Integer, String> choicesAnonymous = new HashMap<>();
    private HashMap<Integer, String> choicesConnected = new HashMap<>();
    private HashMap<Integer, String> choicesManager = new HashMap<>();
    private final HashMap<Integer, ChoicesAbstract> commandsAnonymous = new HashMap<>();
    private HashMap<Integer, ChoicesAbstract> commandsConnected = new HashMap<>();
    private HashMap<Integer, ChoicesAbstract> commandsManager = new HashMap<>();


    public Choices() {
        choicesAnonymous.put(1, "1 - M'inscrire");
        choicesAnonymous.put(2, "2 - Me connecter");
        choicesAnonymous.put(3, "3 - Quitter l'application");
        commandsAnonymous.put(1, new InscriptionChoice());
        commandsAnonymous.put(2, new ConnexionChoice());
        commandsAnonymous.put(3, new QuitChoice());
        choicesConnected = new HashMap<>();
        commandsConnected = new HashMap<>();
        choicesConnected.put(1, "1 - Vérifier la disponibilité des bornes");
        choicesConnected.put(2, "2 - Renseigner ma plaque d'immatriculation");
        choicesConnected.put(3, "3 - Modifier/Supprimer ma réservation");
        choicesConnected.put(4, "4 - Prolonger ma réservation");
        choicesConnected.put(5, "5 - Recharger sans réservations");
        choicesConnected.put(6, "6 - Conclure un contrat mensuel");
        choicesConnected.put(7, "7 - Consulter mon profil");
        choicesConnected.put(8, "8 - Se présenter à sa réservation");
        choicesConnected.put(9, "9 - Partir de sa réservation");
        choicesConnected.put(10, "10 - Consulter mon relevé mensuel");
        choicesConnected.put(11, "11 - Me déconnecter");
        choicesConnected.put(12, "12 - Quitter l'application");
        commandsConnected.put(1, new VerifDispoChoice());
        commandsConnected.put(2, new AddImmatChoice());
        commandsConnected.put(3, new ModifReservationChoice());
        commandsConnected.put(4, new ProlongerReservationChoice());
        commandsConnected.put(5, new WithoutReservationChoice());
        commandsConnected.put(6, new CreateContractChoice());
        commandsConnected.put(7, new ConsultProfileChoice());
        commandsConnected.put(8, new ShowUpChoice());
        commandsConnected.put(9, new GoAwayChoice());
        commandsConnected.put(10, new DonnerReleveMensuel());
        commandsConnected.put(11, new DeconnectionChoice());
        commandsConnected.put(12, new QuitChoice());
        choicesManager = new HashMap<>();
        commandsManager = new HashMap<>();
        choicesManager.put(1, "1 - Définir le délai d'attente");
        choicesManager.put(2, "2 - Définir le montant des frais supplémentaires");
        choicesManager.put(3, "3 - Consulter les profils des clients");
        choicesManager.put(4, "4 - Me déconnecter");
        choicesManager.put(5, "5 - Quitter l'application");
        commandsManager.put(1, new DelayChoice());
        commandsManager.put(2, new PriceChoice());
        commandsManager.put(3, new ConsultAllProfilesChoice());
        commandsManager.put(4, new DeconnectionChoice());
        commandsManager.put(5, new QuitChoice());
    }

    public void display(int state) {
        StringBuilder strBuilder = new StringBuilder();
        switch (state) {
            case (Application.STATE_ANONYMOUS):
                choicesAnonymous.forEach((k, v) -> {
                    strBuilder.append(v).append("\n");
                });
                break;
            case (Application.STATE_CONNECTED):
                choicesConnected.forEach((k, v) -> {
                    strBuilder.append(v).append("\n");
                });
                break;
            case (Application.STATE_MANAGER):
                choicesManager.forEach((k, v) -> {
                    strBuilder.append(v).append("\n");
                });
                break;
        }
        System.out.println(strBuilder.toString());
    }

    public ChoicesAbstract getFunction(int line, int state) {
        switch (state) {
            case (Application.STATE_ANONYMOUS):
                return commandsAnonymous.getOrDefault(line, null);
            case (Application.STATE_CONNECTED):
                return commandsConnected.getOrDefault(line, null);
            case (Application.STATE_MANAGER):
                return commandsManager.getOrDefault(line, null);
            default:
                return null;
        }
    }
}

