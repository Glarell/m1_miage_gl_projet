package gl.commands.manager;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.BorneDAO;
import gl.database.model.Borne;
import gl.database.model.Notification;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GererBorneIndisponible extends ChoicesAbstract {

    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Gérer les bornes indisponibles------]");
        ArrayList<Borne> bornes = (ArrayList<Borne>) BorneDAO.getAllBorneFromState("indisponible");
        if (bornes.size() == 0) {
            System.out.println("Aucunes bornes indisponibles...");
            return Application.RETURN_FAILED;
        }
        System.out.printf("Liste des bornes indisponibles : \n" +
                "%s\n",bornes.stream().map(Borne::toString).reduce("\n", String::concat));
        ArrayList<Integer> listOfId = (ArrayList<Integer>) bornes.stream().map(Borne::getId_borne).collect(Collectors.toList());
        int id = Application.askForIntegerLine("Saisir l'identifiant de la borne à gérer : ");
        while (!listOfId.contains(id)) {
            id = Application.askForIntegerLine("Saisir l'identifiant de la borne à gérer : ");
        }
        try {
            BorneDAO.deleteAllReservationsToABorne(id);
            return Application.RETURN_SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Application.RETURN_FAILED;
    }
}
