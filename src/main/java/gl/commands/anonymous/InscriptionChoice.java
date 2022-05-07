package gl.commands.anonymous;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.ClientDAO;
import gl.database.model.Client;

import java.sql.SQLException;
import java.util.Scanner;

public class InscriptionChoice extends ChoicesAbstract {

    public InscriptionChoice(){

    }

    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Inscription------]\n");
        String firstname = Application.askForLine("Saisir prénom :");
        while (firstname.matches(".*\\d.*") || firstname.isEmpty()) {
            firstname = Application.askForLine("Mauvaise saisie !\nSaisir prénom :");
        }
        String lastname = Application.askForLine("Saisir nom :");
        while (lastname.matches(".*\\d.*") || lastname.isEmpty()) {
            lastname = Application.askForLine("Mauvaise saisie !\nSaisir nom :");
        }
        String address = Application.askForLine("Saisir adresse :");
        while (address.isEmpty()) {
            address = Application.askForLine("Mauvaise saisie !\nSaisir adresse :");
        }
        String tel = Application.askForLine("Saisir numéro de téléphone :");
        while (!tel.matches("\\d{10}")) {
            tel = Application.askForLine("Mauvaise saisie !\nSaisir numéro de téléphone :");
        }
        String email = Application.askForLine("Saisir email :");
        while (!email.matches(".+@.+\\..+") || ClientDAO.isEmailAlreadyExist(email)) {
            email = Application.askForLine("Mauvaise saisie ou email déjà enregistré !\nSaisir email :");
        }
        String password = Application.askForLine("Saisir mot de passe :");
        while (password.isEmpty()) {
            password = Application.askForLine("Mauvaise saisie !\nSaisir mot de passe :");
        }
        String card = Application.askForLine("Saisir numéro carte bancaire (16 chiffres) :");
        while (!card.matches("\\d{16}")) {
            card = Application.askForLine("Mauvaise saisie !\nSaisir numéro carte bancaire (16 chiffres) :");
        }
        // enregistrement du client
        Client client = new Client(lastname,firstname,address,tel,email,password,card);
        try {
            ClientDAO.registrerClient(client);
        } catch (SQLException throwables) {
            System.out.println("Erreur : champs incompatables selon saisies !");
            this.execute(scanner,user);
        }
        user.setState(Application.STATE_CONNECTED);
        return Application.RETURN_SUCCESS;
    }
}
