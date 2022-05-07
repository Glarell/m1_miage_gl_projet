package gl.commands;

import gl.application.Application;
import gl.application.User;
import gl.database.dao.ClientDAO;
import gl.database.model.Client;

import java.util.Scanner;

public class ConnexionChoice extends ChoicesAbstract {

    public ConnexionChoice() {

    }

    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Connexion------]\n");
        Client client = new Client();
        while (client.isNotReal()) {
                String email = Application.askForLine("Saisir email :");
            while (!email.matches(".+@.+\\..+")) {
                email = Application.askForLine("Mauvaise saisie !\nSaisir email :");
            }
            String password = Application.askForLine("Saisir mot de passe :");
            while (password.isBlank()) {
                password = Application.askForLine("Mauvaise saisie !\nSaisir mot de passe :");
            }
            client = ClientDAO.getClientByEmailPwd(email, password);
        }
        //connecte
        user.setState(Application.STATE_CONNECTED);
        return Application.RETURN_SUCCESS;
    }
}
