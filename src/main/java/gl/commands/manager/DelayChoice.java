package gl.commands.manager;

import gl.application.Application;
import gl.application.User;
import gl.commands.ChoicesAbstract;
import gl.database.dao.VariableApplicationDAO;
import gl.database.model.VariableApplication;

import java.sql.SQLException;
import java.util.Scanner;

public class DelayChoice extends ChoicesAbstract {
    @Override
    public int execute(Scanner scanner, User user) {
        System.out.println("[------Définir le délai d'attente------]\n");
        VariableApplication variableApplication = VariableApplicationDAO.getVariableApplicationByName(VariableApplication.WAITING_TIME);
        System.out.println("la valeur actuelle du délai d'attente est de " + variableApplication.getValeur() + "min");

        int newValeur = Application.askForIntegerLine("Saisir le nouveau délai d'attente :");
        while (newValeur < 1) {
            newValeur = Application.askForIntegerLine("Mauvaise saisie !\nSaisir le nouveau délai d'attente :");
        }

        try {
            VariableApplicationDAO.updateVariableApplicationValue(variableApplication, newValeur);
        } catch (SQLException e) {
            System.out.println("Erreur : champs incompatables selon saisies !");
            this.execute(scanner, user);
        }

        return Application.RETURN_SUCCESS;
    }
}
