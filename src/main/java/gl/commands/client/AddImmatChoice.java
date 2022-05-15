package gl.commands.client;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.PlaqueDAO;
import gl.database.model.Plaque;

import java.sql.SQLException;
import java.util.Scanner;

public class AddImmatChoice extends ChoicesAbstract {

    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Ajouter une immatriculation------]\n");
        //[A-Z]{2}-\d{3}-[A-Z]{2}
        String plaque_id = Application.askForLine("Saisir numéro d'immatriculation :");
        while (!plaque_id.matches("[A-Z]{2}-\\d{3}-[A-Z]{2}")) {
            plaque_id = Application.askForLine("Mauvaise saisie !\nSaisir numéro d'immatriculation :");
        }
        try {
            PlaqueDAO.insertNewPlaque(plaque_id);
            System.out.printf("La plaque d'immatriculation %s a été ajoutée !",plaque_id);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return Application.RETURN_SUCCESS;
    }
}
